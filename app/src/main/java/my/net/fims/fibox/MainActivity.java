package my.net.fims.fibox;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import my.net.fims.fibox.Configuration.Config;
import my.net.fims.fibox.Configuration.Settings;
import my.net.fims.fibox.Views.Conversation;
import my.net.fims.fibox.Views.Register;


public class MainActivity extends ActionBarActivity {

    private Config config;
    private Settings settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        config = new Config(getApplicationContext());
        settings = new Settings(getApplicationContext());
        if(settings.isRegistered()) {
            startActivity(new Intent(this, Conversation.class));
            finish();
        } else {
            startActivity(new Intent(this, Register.class));
            finish();
        }
    }
}
