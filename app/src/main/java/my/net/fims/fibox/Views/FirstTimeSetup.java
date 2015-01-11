package my.net.fims.fibox.Views;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.orm.SugarRecord;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import my.net.fims.fibox.Adapter.ContactDataArray;
import my.net.fims.fibox.Configuration.Config;
import my.net.fims.fibox.Configuration.Settings;
import my.net.fims.fibox.Controller.CommonDataFunction;
import my.net.fims.fibox.R;

/**
 * Created by kamarulzaman on 1/11/15.
 */
public class FirstTimeSetup extends ActionBarActivity {

    ArrayList<ContactDataArray> contacts;
    private ActionBar actionbar;
    private Config config;
    private Settings settings;
    private CommonDataFunction commondata;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        config = new Config(getApplicationContext());
        settings = new Settings(getApplicationContext());
        commondata = new CommonDataFunction(getApplicationContext());

        SetupActionBar();
        setContentView(R.layout.firsttimesetup_activity);
        AsyncContact();
    }

    private void SetupActionBar(){
        actionbar = getSupportActionBar();
        actionbar.setTitle("Account Setup");
        actionbar.setBackgroundDrawable(getResources().getDrawable(R.drawable.app_actionbar));
    }

    private void AsyncContact() {
        try{
            dialog = new ProgressDialog(FirstTimeSetup.this);
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
                        startActivity(new Intent(getApplicationContext(), Conversation.class));
                        finish();
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        try{
                            if(response.getBoolean("status"))
                            {
                                JSONArray jarray = response.getJSONArray("response");
                                if(jarray.length() > 0) {
                                    SugarRecord.deleteAll(my.net.fims.fibox.Model.Contact.class);
                                    for(int i = 0; i < jarray.length(); i++)
                                    {
                                        JSONObject jobject = jarray.getJSONObject(i);
                                        if(jobject.getBoolean("register")) {
                                            my.net.fims.fibox.Model.Contact contact = new my.net.fims.fibox.Model.Contact(jobject.getString("contact_id"), jobject.getString("phone_number"), jobject.getString("display_picture"), jobject.getString("status"));
                                            contact.save();
                                        }
                                    }
                                    startActivity(new Intent(getApplicationContext(), Conversation.class));
                                    finish();
                                } else {
                                    startActivity(new Intent(getApplicationContext(), Conversation.class));
                                    finish();
                                    Toast.makeText(getApplicationContext(), "No contact found", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "Sync contact successful", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), Conversation.class));
                                finish();
                            }
                        } catch(Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Sync contact failed", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), Conversation.class));
                            finish();
                        }
                    }
                });
            } else {
                Toast.makeText(getApplicationContext(), "No contacts found", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), Conversation.class));
                finish();
            }
        } catch(Exception e) {
            e.printStackTrace();
            startActivity(new Intent(getApplicationContext(), Conversation.class));
            finish();
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
