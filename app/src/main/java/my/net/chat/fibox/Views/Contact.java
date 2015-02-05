package my.net.chat.fibox.Views;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.orm.SugarRecord;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import my.net.chat.fibox.Adapter.ContactAdapter;
import my.net.chat.fibox.Adapter.ContactArray;
import my.net.chat.fibox.Adapter.ContactDataArray;
import my.net.chat.fibox.Configuration.Config;
import my.net.chat.fibox.Configuration.Settings;
import my.net.chat.fibox.Controller.CommonDataFunction;
import my.net.chat.fibox.R;

/**
 * Created by kamarulzaman on 1/2/15.
 */
public class Contact extends ActionBarActivity {

    private Config config;
    private Settings settings;
    private CommonDataFunction commondata;

    private ActionBar actionbar;
    ArrayList<ContactArray> items = new ArrayList<ContactArray>();
    ArrayList<ContactDataArray> contacts;
    ContactAdapter adapter;
    ListView lview;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        config = new Config(getApplicationContext());
        settings = new Settings(getApplicationContext());
        commondata = new CommonDataFunction(getApplicationContext());

        SetupActionBar();
        setContentView(R.layout.contact_activity);
        lview = (ListView) findViewById(R.id.lview);
        lview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ContactArray item = items.get(position);
                Intent intent = new Intent(getApplicationContext(), Chat.class);
                intent.putExtra("phoneNumber", item.getPhoneNumber());
                startActivity(intent);
            }
        });
        setupConversation();
    }

    private void SetupActionBar(){
        actionbar = getSupportActionBar();
        actionbar.setTitle("Contacts");
        actionbar.setBackgroundDrawable(getResources().getDrawable(R.drawable.app_actionbar));
    }

    private void setupConversation() {
        try{
            TextView text_nocontact = (TextView) findViewById(R.id.text_nocontact);
            items.removeAll(items);
            List<my.net.chat.fibox.Model.Contact> contacts =  my.net.chat.fibox.Model.Contact.listAll(my.net.chat.fibox.Model.Contact.class);
            if(contacts.size() > 0) {
                for(my.net.chat.fibox.Model.Contact contact : contacts)
                {
                    HashMap<String, String> contactData = commondata.getContactDetail(contact.getContactId());
                    if(contactData != null)
                    {
                        items.add(new ContactArray(Long.toString(contact.getId()), contact.getPhoneNumber(), contact.getDisplayPicture(), contactData.get("display_name"), contact.getStatus(), 1));
                    }
                }
                lview.setVisibility(View.VISIBLE);
                text_nocontact.setVisibility(View.GONE);
            } else {
                text_nocontact.setVisibility(View.VISIBLE);
                lview.setVisibility(View.GONE);
            }
            adapter = new ContactAdapter(getApplicationContext(), items);
            adapter.notifyDataSetChanged();
            lview.setAdapter(adapter);
        } catch(Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("Search Contact").setIcon(getResources().getDrawable(R.drawable.ic_action_search)).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menu.add("Refresh Contact").setIcon(getResources().getDrawable(R.drawable.ic_action_refresh)).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getTitle().equals("Refresh Contact"))
        {
            AsyncContact();
        }
        return super.onOptionsItemSelected(item);
    }

    private void AsyncContact() {
        try{
            dialog = new ProgressDialog(Contact.this);
            dialog.setTitle("Loading");
            dialog.setMessage("Please wait, syncing your contacts...");
            dialog.setCancelable(false);
            dialog.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
        try{
            contacts = commondata.getContacts();
            if(contacts != null) {
                JSONArray contact_array = convertJSONContactArray();
                AsyncHttpClient client = new AsyncHttpClient();
                RequestParams params = new RequestParams();
                params.add("action", "get_contacts");
                params.add("data", contact_array.toString());
                params.add("phone_number", settings.getPhoneNumber());
                params.add("token", settings.getToken());
                params.add("api_key", config.getAPIKey());
                client.post(config.getAPIUrl(), params, new JsonHttpResponseHandler(){
                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        Toast.makeText(getApplicationContext(), "Sync contact failed", Toast.LENGTH_SHORT).show();
                        setSupportProgressBarIndeterminateVisibility(false);
                        try{
                            dialog.hide();
                        } catch(Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        try{
                            if(response.getBoolean("status"))
                            {
                                JSONArray jarray = response.getJSONArray("response");
                                if(jarray.length() > 0) {
                                    SugarRecord.deleteAll(my.net.chat.fibox.Model.Contact.class);
                                    for(int i = 0; i < jarray.length(); i++)
                                    {
                                        JSONObject jobject = jarray.getJSONObject(i);
                                        if(jobject.getBoolean("register")) {
                                            my.net.chat.fibox.Model.Contact contact = new my.net.chat.fibox.Model.Contact(jobject.getString("contact_id"), jobject.getString("phone_number"), jobject.getString("display_picture"), jobject.getString("status"));
                                            contact.save();
                                        }
                                    }
                                    setupConversation();
                                } else {
                                    Toast.makeText(getApplicationContext(), "No contact found", Toast.LENGTH_SHORT).show();
                                }
                            }
                            Toast.makeText(getApplicationContext(), "Sync contact successful", Toast.LENGTH_SHORT).show();
                            try{
                                dialog.hide();
                            } catch(Exception e) {
                                e.printStackTrace();
                            }
                        } catch(Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Sync contact failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } else {
                try{
                    dialog.hide();
                } catch(Exception e) {
                    e.printStackTrace();
                }
                Toast.makeText(getApplicationContext(), "No contacts found", Toast.LENGTH_SHORT).show();
            }
        } catch(Exception e) {
            e.printStackTrace();
            try{
                dialog.hide();
            } catch(Exception ea) {
                ea.printStackTrace();
            }
        }
    }

    private JSONArray convertJSONContactArray() {
        try{
            contacts = commondata.getContacts();
            if(contacts != null) {
                JSONArray phone_array = new JSONArray();
                for(ContactDataArray contact : contacts)
                {
                    JSONObject contact_object = new JSONObject();
                    String clean_number = contact.getPhoneNumber();
                    clean_number = clean_number.replace(" ", "");
                    clean_number = clean_number.replace("+", "");
                    clean_number = clean_number.replace("-", "");
                    clean_number = clean_number.replace("_", "");
                    contact_object.put("phone_number", clean_number);
                    contact_object.put("contact_id", contact.getContactID());
                    phone_array.put(contact_object);
                }

                JSONArray contact_array = new JSONArray();
                contact_array.put(phone_array);
                return contact_array;
            }
            return null;
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}
