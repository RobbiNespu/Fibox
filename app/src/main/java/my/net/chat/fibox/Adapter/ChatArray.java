package my.net.chat.fibox.Adapter;

/**
 * Created by kamarulzaman on 1/2/15.
 */
public class ChatArray {
    private String id, message, message_time;
    private boolean myself;

    public ChatArray(String id, String message, String message_time, boolean myself) {
        this.id = id;
        this.message = message;
        this.message_time = message_time;
        this.myself = myself;
    }

    public String getID(){
        return this.id;
    }

    public String getMessage(){
        return this.message;
    }

    public String getMessageTime(){
        return this.message_time;
    }

    public boolean getMySelf() {
        return this.myself;
    }

}
