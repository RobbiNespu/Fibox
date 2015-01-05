package my.net.fims.fibox.Model;

import com.orm.SugarRecord;

/**
 * Created by kamarulzaman on 1/2/15.
 */
public class Conversation extends SugarRecord<Conversation>{

    String chatType, phoneNumber;

    public Conversation(){

    }

    public Conversation(String phoneNumber, String chatType){
        this.chatType = chatType;
        this.phoneNumber = phoneNumber;
    }

    public String getChatType(){
        return this.chatType;
    }

    public String getPhoneNumber(){
        return this.phoneNumber;
    }

}
