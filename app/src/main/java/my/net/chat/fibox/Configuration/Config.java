package my.net.chat.fibox.Configuration;

import android.content.Context;

/**
 * Created by kamarulzaman on 1/1/15.
 */
public class Config {
    Context context;
    private static final String api_url = "http://API_URL";
    private static final String api_key = "API KEY";
    private static final String gcm_id = "Google Project iD";

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
