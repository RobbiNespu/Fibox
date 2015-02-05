package my.net.chat.fibox;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

import my.net.chat.fibox.Configuration.Config;
import my.net.chat.fibox.Configuration.Settings;
import my.net.chat.fibox.Views.Conversation;
import my.net.chat.fibox.Views.Register;


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
