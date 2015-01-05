package my.net.fims.fibox.Configuration;

import android.content.Context;

/**
 * Created by kamarulzaman on 1/1/15.
 */
public class Config {
    Context context;
    private static final String api_url = "http://sudo.my/api_fibox.php";
    private static final String api_key = "abc123";
    private static final String gcm_id = "751989037298";

    public Config(Context context){
        this.context = context;
    }

    public String getAPIUrl(){
        return this.api_url;
    }

    public String getAPIKey() {
        return this.api_key;
    }

    public String getGCMID(){
        return this.gcm_id;
    }
}
