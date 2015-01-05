package my.net.fims.fibox.Model;

import com.orm.SugarRecord;

/**
 * Created by kamarulzaman on 1/3/15.
 */
public class Message extends SugarRecord<Message>{
    private String conversationId, from_id, to_id, reply_text, reply_time;

    public Message() {

    }

    public Message(String conversationId, String from_id, String to_id, String reply_text, String reply_time){
        this.conversationId = conversationId;
        this.from_id = from_id;
        this.to_id = to_id;
        this.reply_text = reply_text;
        this.reply_time = reply_time;
    }

    public String getConversationId(){
        return this.conversationId;
    }

    public String getFromId(){
        return this.from_id;
    }

    public String getToId(){
        return this.to_id;
    }

    public String getReplyText(){
        return this.reply_text;
    }

    public String getReplyTime(){
        return this.reply_time;
    }

}
