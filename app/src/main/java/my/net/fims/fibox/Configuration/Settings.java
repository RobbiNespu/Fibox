package my.net.fims.fibox.Configuration;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.provider.ContactsContract;

import java.util.HashMap;

/**
 * Created by kamarulzaman on 1/1/15.
 */
public class Settings {

    Context context;
    SharedPreferences pref;

    public Settings(Context context) {
        this.context = context;
        pref = context.getSharedPreferences("fibox", Context.MODE_PRIVATE);

    }

    public boolean isRegistered() {
        try{
            return pref.getBoolean("registered", false);
        } catch(Exception e) {
            return false;
        }
    }

    public void setRegister(String phone_number) {
        try{
            SharedPreferences.Editor edit = pref.edit();
            edit.putString("phone_number", phone_number);
            edit.commit();
        } catch(Exception e) {

        }
    }

    public void setDeviceID(String deviceID, String token) {
        try{
            SharedPreferences.Editor edit = pref.edit();
            edit.putString("device_id", deviceID);
            edit.putString("token", token);
            edit.putBoolean("registered", true);
            edit.commit();
        } catch(Exception e) {

        }
    }

    public String getToken() {
        try{
            return pref.getString("token", "");
        } catch(Exception e) {
            return null;
        }
    }

    public String getPhoneNumber() {
        try{
            return pref.getString("phone_number", "");
        } catch(Exception e) {
            return "";
        }
    }
}
