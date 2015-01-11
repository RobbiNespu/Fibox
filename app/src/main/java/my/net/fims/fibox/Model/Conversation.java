package my.net.fims.fibox.Model;

import com.orm.SugarRecord;

/**
 * Created by kamarulzaman on 1/2/15.
 */
public class Conversation extends SugarRecord<Conversation>{

    public String chatType, phoneNumber, lastConversation;

    public Conversation(){

    }

    public Conversation(String phoneNumber, String chatType, String lastConversation){
        this.chatType = chatType;
        this.phoneNumber = phoneNumber;
        this.lastConversation = lastConversation;
    }

    public String getChatType(){
        return this.chatType;
    }

    public String getPhoneNumber(){
        return this.phoneNumber;
    }

    public String getLastConversation() { return this.lastConversation; }

}
