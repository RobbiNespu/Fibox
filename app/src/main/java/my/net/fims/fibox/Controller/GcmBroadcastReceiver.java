package my.net.fims.fibox.Controller;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.util.List;

import my.net.fims.fibox.Model.Conversation;
import my.net.fims.fibox.Model.Message;

/**
 * Created by kamarulzaman on 1/2/15.
 */
public class GcmBroadcastReceiver extends BroadcastReceiver {

    private CommonDataFunction commondata;
    private CommonFunction commonfunction;

    @Override
    public void onReceive(Context arg0, Intent arg1) {
        PowerManager mPowerManager = (PowerManager) arg0.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock mWakeLock = mPowerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "GCMBroadcastReceiver");
        mWakeLock.acquire();
        try{
            commondata = new CommonDataFunction(arg0);
            commonfunction = new CommonFunction(arg0);
            GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(arg0);
            String messageType = gcm.getMessageType(arg1);
            Bundle extras = arg1.getExtras();


            if(GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                Log.d("status", "Message type send error");
            }

            if(GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
                Log.d("status", "Message type send deleted");
            }

            if(GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                Log.d("status", "Message receiver "+extras.toString());
                try{
                    if(extras.getString("phone_number") != null  && extras.getString("message") != null && extras.getString("message_time") != null && extras.getString("gcm_type") != null)
                    {
                        boolean save_ok = false;
                        if(extras.getString("gcm_type").equals("chat"))
                        {
                            List<my.net.fims.fibox.Model.Conversation> conversations = my.net.fims.fibox.Model.Conversation.find(my.net.fims.fibox.Model.Conversation.class, "phone_Number = ? and chat_Type = ?", extras.getString("phone_number"), "chat");
                            if(conversations != null && conversations.size() > 0)
                            {
                                my.net.fims.fibox.Model.Conversation conversation = conversations.get(0);
                                my.net.fims.fibox.Model.Message save_message = new Message(Long.toString(conversation.getId()), extras.getString("phone_number"), "me", extras.getString("message"), commonfunction.getTimeStamp());
                                save_message.save();
                                save_ok = true;
                            } else {
                                my.net.fims.fibox.Model.Conversation new_conversation =  new my.net.fims.fibox.Model.Conversation(extras.getString("phone_number"), "chat");
                                new_conversation.save();
                                my.net.fims.fibox.Model.Message save_message = new Message(Long.toString(new_conversation.getId()), extras.getString("phone_number"), "me", extras.getString("message"), commonfunction.getTimeStamp());
                                save_message.save();
                                save_ok = true;
                            }
                            if(save_ok)
                            {
                              try{
                                  Intent chat_message = new Intent("my.net.fims.fibox.chatmessage");
                                  arg0.sendBroadcast(chat_message);
                              } catch(Exception e)
                              {
                                  e.printStackTrace();
                              }
                            }
                        }
                    }
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }

        } catch(Exception e) {
            e.printStackTrace();
        }
        setResultCode(Activity.RESULT_OK);
    }

}
