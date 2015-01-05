package my.net.fims.fibox.Views;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import my.net.fims.fibox.Adapter.ConversationAdapter;
import my.net.fims.fibox.Adapter.ConversationArray;
import my.net.fims.fibox.Configuration.Config;
import my.net.fims.fibox.Configuration.Settings;
import my.net.fims.fibox.Controller.CommonDataFunction;
import my.net.fims.fibox.Controller.CommonFunction;
import my.net.fims.fibox.R;

/**
 * Created by kamarulzaman on 1/2/15.
 */
public class Conversation extends ActionBarActivity {

    private Config config;
    private Settings settings;
    private ActionBar actionbar;
    private CommonDataFunction commondata;
    private CommonFunction commonfunction;
    ArrayList<ConversationArray> items = new ArrayList<ConversationArray>();
    ConversationAdapter adapter;
    ListView lview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        config = new Config(getApplicationContext());
        settings = new Settings(getApplicationContext());
        commondata = new CommonDataFunction(getApplicationContext());
        commonfunction = new CommonFunction(getApplicationContext());

        SetupActionBar();
        setContentView(R.layout.conversation_activity);
        lview = (ListView) findViewById(R.id.lview);
        setupConversation();
        try{
            registerReceiver(new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    setupConversation();
                }
            }, new IntentFilter("my.net.fims.fibox.chatmessage"));
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupConversation();
    }

    private void SetupActionBar(){
        actionbar = getSupportActionBar();
        actionbar.setTitle("Conversation");
        actionbar.setBackgroundDrawable(getResources().getDrawable(R.drawable.app_actionbar));
    }

    private void setupConversation() {
        try{
            List<my.net.fims.fibox.Model.Conversation> conversations = my.net.fims.fibox.Model.Conversation.listAll(my.net.fims.fibox.Model.Conversation.class);
            if(conversations != null && conversations.size() > 0)
            {
                items.removeAll(items);
                for(my.net.fims.fibox.Model.Conversation conversation : conversations)
                {
                    if(conversation.getChatType().equals("chat"))
                    {
                        List<my.net.fims.fibox.Model.Contact> find_contact =  my.net.fims.fibox.Model.Contact.find(my.net.fims.fibox.Model.Contact.class, "phoneNumber = ? ", conversation.getPhoneNumber());
                        if(find_contact != null && find_contact.size() > 0)
                        {
                            my.net.fims.fibox.Model.Contact contact = find_contact.get(0);
                            HashMap<String, String> contactDetail = commondata.getContactDetail(contact.getContactId());
                            if(contactDetail != null)
                            {
                                String[] queryFind = { conversation.getId().toString() };
                                List<my.net.fims.fibox.Model.Message> message = my.net.fims.fibox.Model.Message.find(my.net.fims.fibox.Model.Message.class, "conversation_Id = ?", queryFind, null, "replyTime desc", "1");
                                if(message != null && message.size() > 0) {
                                    my.net.fims.fibox.Model.Message msg = message.get(0);
                                    items.add(new ConversationArray(conversation.getPhoneNumber(), contact.getDisplayPicture(), contactDetail.get("display_name"), msg.getReplyText(), commonfunction.friendlyDateTime(Long.parseLong(msg.getReplyTime())), 1));
                                }
                            }
                        } else {
                            String[] queryFind = { conversation.getId().toString() };
                            List<my.net.fims.fibox.Model.Message> message = my.net.fims.fibox.Model.Message.find(my.net.fims.fibox.Model.Message.class, "conversation_Id = ?", queryFind, null, "replyTime desc", "1");
                            if(message != null && message.size() > 0) {
                                my.net.fims.fibox.Model.Message msg = message.get(0);
                                items.add(new ConversationArray(conversation.getPhoneNumber(), commonfunction.getProfilePictureURL(conversation.getPhoneNumber()),conversation.getPhoneNumber(), msg.getReplyText(), commonfunction.friendlyDateTime(Long.parseLong(msg.getReplyTime())), 1));
                            }
                        }
                    }
                }
            } else {
                //no conversation found, start a new chat
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

        try{
            adapter = new ConversationAdapter(getApplicationContext(), items);
            lview.setAdapter(adapter);
            lview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    ConversationArray item = items.get(position);
                    Intent intent = new Intent(getApplicationContext(), Chat.class);
                    intent.putExtra("phoneNumber", item.getID());
                    startActivity(intent);
                }
            });
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("Search Conversation").setIcon(getResources().getDrawable(R.drawable.ic_action_search)).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menu.add("Contacts").setIcon(getResources().getDrawable(R.drawable.ic_action_chat)).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getTitle().equals("Contacts"))
        {
            startActivity(new Intent(this, Contact.class));
        }
        return super.onOptionsItemSelected(item);
    }
}
