package my.net.fims.fibox.Controller;

import android.content.Context;
import android.text.format.DateUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

import my.net.fims.fibox.Configuration.Config;

/**
 * Created by kamarulzaman on 1/2/15.
 */
public class CommonFunction {

    Context context;
    Config config;

    public CommonFunction(Context context){
        this.context = context;
        this.config = new Config(context);
    }

    public String getTimeStamp(){
        Long tsLong = System.currentTimeMillis()/1000;
        String ts = tsLong.toString();
        return ts;
    }

    public String friendlyDateTime(Long timestamp){
        try{
            timestamp = timestamp*1000;
            Date df = new Date(timestamp);
            String time = new SimpleDateFormat("hh:mm a").format(df);
            if(DateUtils.isToday(timestamp))
            {
                return time;
            } else {
                return time; //for now, return same as the time
            }
        } catch(Exception e) {
            return null;
        }
    }

    public String getProfilePictureURL(String phone_number) {
        return config.getAPIUrl()+"/avatar/"+phone_number;
    }

}
