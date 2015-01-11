package my.net.fims.fibox.Views;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;
import com.orm.SugarRecord;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import my.net.fims.fibox.Adapter.ContactDataArray;
import my.net.fims.fibox.Configuration.Config;
import my.net.fims.fibox.Configuration.Settings;
import my.net.fims.fibox.Controller.CommonDataFunction;
import my.net.fims.fibox.R;

/**
 * Created by kamarulzaman on 1/1/15.
 */
public class Register extends ActionBarActivity {

    private Config config;
    private Settings settings;
    private ActionBar actionbar;
    private Button btnRegister;
    private ProgressDialog dialog;
    private CommonDataFunction commondata;
    ArrayList<ContactDataArray> contacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        config = new Config(getApplicationContext());
        settings = new Settings(getApplicationContext());
        commondata = new CommonDataFunction(getApplicationContext());

        SetupActionBar();
        setContentView(R.layout.register_activity);
        try{
           btnRegister = (Button) findViewById(R.id.btnRegister);
           final EditText phone_number = (EditText) findViewById(R.id.phone_number);
           btnRegister.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   try{
                       btnRegister.setEnabled(false);
                       dialog = new ProgressDialog(Register.this);
                       dialog.setTitle("Loading");
                       dialog.setMessage("Please wait, registering your account...");
                       dialog.setCancelable(false);
                       dialog.show();
                   } catch(Exception e) {
                       e.printStackTrace();
                   }

                   AsyncHttpClient client = new AsyncHttpClient();
                   RequestParams params = new RequestParams();
                   params.add("api_key", config.getAPIKey());
                   params.add("action", "register");
                   params.add("phone_number", phone_number.getText().toString());
                   client.post(config.getAPIUrl(), params, new JsonHttpResponseHandler() {
                       @Override
                       public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                           super.onSuccess(statusCode, headers, response);
                           try{
                               btnRegister.setEnabled(true);
                               dialog.cancel();
                           } catch(Exception e) {
                               e.printStackTrace();
                           }
                           try{
                               if(response.getBoolean("status"))
                               {
                                   switch(response.getInt("result"))
                                   {
                                       case 0:
                                           Toast.makeText(getApplicationContext(), "The phone number you have entered is already registered", Toast.LENGTH_SHORT).show();
                                           break;
                                       case 1:
                                           settings.setRegister(phone_number.getText().toString());
                                           RegisterGCMBackground register = new RegisterGCMBackground(phone_number.getText().toString(), response.getString("userkeys"));
                                           register.execute();
                                           Intent start = new Intent(Register.this, FirstTimeSetup.class);
                                           startActivity(start);
                                           finish();
                                           break;
                                       default:
                                           Toast.makeText(getApplicationContext(), "Sign up failed", Toast.LENGTH_SHORT).show();
                                           break;
                                   }
                               } else {
                                   Toast.makeText(getApplicationContext(), "Sign up failed", Toast.LENGTH_SHORT).show();
                               }
                           } catch(Exception e) {
                               e.printStackTrace();
                               Toast.makeText(getApplicationContext(), "Sign up failed", Toast.LENGTH_SHORT).show();
                           }
                       }

                       @Override
                       public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                           super.onFailure(statusCode, headers, throwable, errorResponse);
                           Toast.makeText(getApplicationContext(), "Sign up failed", Toast.LENGTH_SHORT).show();
                           try{
                               btnRegister.setEnabled(true);
                               dialog.cancel();
                           } catch(Exception e) {
                               e.printStackTrace();
                           }
                       }
                   });
               }
           });




        } catch(Exception e) {
            e.printStackTrace();
        }

    }


    private class RegisterGCMBackground extends AsyncTask<Void, Void, Void>{

        private String phone_number;
        private GoogleCloudMessaging gcm;
        private String userkeys;
        private int MAX_ATTEMPTS = 10;
        private int BACKOFF_MILLI_SECONDS = 1000;

        private RegisterGCMBackground(String phone_number, String userkeys){
           this.phone_number = phone_number;
           this.userkeys = userkeys;
        }

        @Override
        protected Void doInBackground(Void... params) {
            Random random = new Random();
            long backoff = BACKOFF_MILLI_SECONDS + random.nextInt(1000);
            for(int i = 0; i < MAX_ATTEMPTS; i++)
            {
                try{
                    if(gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
                    }
                    final String deviceId = gcm.register(config.getGCMID());
                    SyncHttpClient client = new SyncHttpClient();
                    RequestParams parameter = new RequestParams();
                    parameter.add("api_key", config.getAPIKey());
                    parameter.add("action", "registerID");
                    parameter.add("deviceID", deviceId);
                    parameter.add("userkeys", userkeys);
                    parameter.add("phone_number", phone_number);
                    client.post(config.getAPIUrl(), parameter, new JsonHttpResponseHandler() {
                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            super.onSuccess(statusCode, headers, response);
                            try{
                                if(response.getBoolean("status")) {
                                    settings.setDeviceID(deviceId, response.getString("token"));
                                }
                            } catch(Exception e) {

                            }
                        }
                    });
                } catch(IOException e){
                    if(i == MAX_ATTEMPTS)
                    {
                        break;
                    }
                    try{
                        Thread.sleep(backoff);
                    } catch (InterruptedException e1) {

                    }
                    e.printStackTrace();
                    backoff *= 2;
                }
            }
            return null;
        }
    }

    private void SetupActionBar(){
        actionbar = getSupportActionBar();
        actionbar.setTitle("Sign up");
        actionbar.setBackgroundDrawable(getResources().getDrawable(R.drawable.app_actionbar));
    }
}
