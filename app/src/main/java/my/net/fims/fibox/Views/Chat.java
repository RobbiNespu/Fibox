package my.net.fims.fibox.Views;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import my.net.fims.fibox.Adapter.ChatAdapter;
import my.net.fims.fibox.Adapter.ChatArray;
import my.net.fims.fibox.Configuration.Config;
import my.net.fims.fibox.Configuration.Settings;
import my.net.fims.fibox.Controller.CommonDataFunction;
import my.net.fims.fibox.Controller.CommonFunction;
import my.net.fims.fibox.Model.Message;
import my.net.fims.fibox.R;

/**
 * Created by kamarulzaman on 1/2/15.
 */
public class Chat extends ActionBarActivity {

    private Settings settings;
    private Config config;
    private ActionBar actionbar;
    private CommonFunction commonfunction;
    ArrayList<ChatArray> items = new ArrayList<ChatArray>();
    ChatAdapter adapter;
    ListView lview;
    EditText message;
    Bundle bundle_data;
    my.net.fims.fibox.Model.Contact contact;
    CommonDataFunction commondata;
    HashMap<String, String> userdata;
    private String conversationId;
    private String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        settings = new Settings(getApplicationContext());
        config = new Config(getApplicationContext());
        commondata = new CommonDataFunction(getApplicationContext());
        commonfunction = new CommonFunction(getApplicationContext());

        SetupActionBar();
        setContentView(R.layout.chat_activity);

        try{
            bundle_data = getIntent().getExtras();
            phoneNumber = bundle_data.getString("phoneNumber");
            List<my.net.fims.fibox.Model.Contact> contacts =  my.net.fims.fibox.Model.Contact.find(my.net.fims.fibox.Model.Contact.class, "phoneNumber = ?", phoneNumber);

            if(contacts != null && contacts.size() > 0)
            {
                my.net.fims.fibox.Model.Contact contact = contacts.get(0);
                userdata = commondata.getContactDetail(contact.getContactId());
                if(userdata != null) {
                    actionbar.setTitle(userdata.get("display_name"));
                } else {
                    actionbar.setTitle(phoneNumber);
                }
            } else {
                actionbar.setTitle(phoneNumber);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

        if(!setupConversation())
        {
            Toast.makeText(getApplicationContext(), "Start conversation failed", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(), Conversation.class));
            finish();
        }

        lview = (ListView) findViewById(R.id.lview);
        setupChat();
        Button btnSendMessage = (Button) findViewById(R.id.btnSendMessage);
        message = (EditText) findViewById(R.id.message_input);
        btnSendMessage.setOnClickListener(new SendMessageHandler());

        try{
            registerReceiver(new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    Toast.makeText(getApplicationContext(), "Ok receive message", Toast.LENGTH_SHORT).show();
                    setupChat();
                }
            }, new IntentFilter("my.net.fims.fibox.chatmessage"));
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    private boolean setupConversation(){
        try{
            List<my.net.fims.fibox.Model.Conversation> conversation = my.net.fims.fibox.Model.Conversation.find(my.net.fims.fibox.Model.Conversation.class, "phone_Number = ? and chat_Type = ?", phoneNumber, "chat");
            if(conversation != null && conversation.size() > 0)
            {
                my.net.fims.fibox.Model.Conversation conversation_data = conversation.get(0);
                conversationId = Long.toString(conversation_data.getId());
            } else {
                my.net.fims.fibox.Model.Conversation new_conversation =  new my.net.fims.fibox.Model.Conversation(phoneNumber, "chat");
                new_conversation.save();
                conversationId = Long.toString(new_conversation.getId());
            }
            return true;
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private class SendMessageHandler implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            try{
                if(message.getText().length() > 0)
                {
                    sendMessage(message.getText().toString());
                    my.net.fims.fibox.Model.Message save_message = new Message(conversationId, "me", phoneNumber, message.getText().toString(), commonfunction.getTimeStamp());
                    save_message.save();
                    items.add(new ChatArray(save_message.getId().toString(), message.getText().toString(), commonfunction.friendlyDateTime(Long.parseLong(commonfunction.getTimeStamp())), true));
                    message.setText("");
                    refreshChat();
                }
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void sendMessage(String message){
        try{
            AsyncHttpClient client = new AsyncHttpClient();
            RequestParams params = new RequestParams();
            params.add("api_key", config.getAPIKey());
            params.add("action", "send_message");
            params.add("phone_number", phoneNumber);
            params.add("message", message);
            client.post(config.getAPIUrl(), params, new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    try{
                        Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                    } catch(Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Send message failed", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    Toast.makeText(getApplicationContext(), "Send message failed", Toast.LENGTH_SHORT).show();
                }
            });

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void setupChat() {
        try{
            items.removeAll(items);
            String[] queryWhere = { conversationId };
            List<my.net.fims.fibox.Model.Message> message = Message.find(my.net.fims.fibox.Model.Message.class, "conversation_Id = ?", queryWhere, null, "replyTime asc", null);
            if(message != null && message.size() > 0)
            {
                for(my.net.fims.fibox.Model.Message msg : message)
                {
                    Long parseTimestamp = Long.parseLong(msg.getReplyTime());
                    if(msg.getFromId().equals("me"))
                    {
                        items.add(new ChatArray(msg.getId().toString(), msg.getReplyText(), commonfunction.friendlyDateTime(parseTimestamp), true));
                    } else {
                        items.add(new ChatArray(msg.getId().toString(), msg.getReplyText(), commonfunction.friendlyDateTime(parseTimestamp), false));
                    }
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        adapter = new ChatAdapter(getApplicationContext(), items);
        lview.setAdapter(adapter);
    }

    private void refreshChat(){
        try{
            adapter.notifyDataSetChanged();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void SetupActionBar(){
        actionbar = getSupportActionBar();
        actionbar.setTitle("Chat");
        //actionbar.setSubtitle("Online");
        actionbar.setBackgroundDrawable(getResources().getDrawable(R.drawable.app_actionbar));
    }
}
