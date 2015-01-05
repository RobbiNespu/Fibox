package my.net.fims.fibox.Controller;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;

import java.util.ArrayList;
import java.util.HashMap;

import my.net.fims.fibox.Adapter.ContactDataArray;

/**
 * Created by kamarulzaman on 1/2/15.
 */
public class CommonDataFunction {
    Context context;
    public CommonDataFunction(Context context){
        this.context = context;
    }

    public ArrayList<ContactDataArray> getContacts(){
        try{
            ArrayList<ContactDataArray> item = new ArrayList<ContactDataArray>();
            Cursor cursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
            if(cursor.moveToFirst()) {
                do{
                    String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    String phone_number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    String contact_id = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.RAW_CONTACT_ID));
                    item.add(new ContactDataArray(contact_id, name, phone_number));
                } while(cursor.moveToNext());
                return item;
            }
            return null;
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public HashMap<String, String> getContactDetail(String contactId) {
        try{

            HashMap<String, String> data = new HashMap<String, String>();
            Cursor cursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.RAW_CONTACT_ID+"="+contactId, null, null);
            if(cursor != null && cursor.moveToFirst())
            {
                String display_name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String phone_number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                String contactRawId = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.RAW_CONTACT_ID));
                data.put("display_name", display_name);
                data.put("phone_number", phone_number);
                data.put("contactRawId", contactRawId);
                return data;
            } else {
                return null;
            }

        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public HashMap<String, String> getContactDetailNumber(String phoneNumber) {
        try{

            HashMap<String, String> data = new HashMap<String, String>();
            Cursor cursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.NUMBER+"="+phoneNumber, null, null);
            if(cursor != null && cursor.moveToFirst())
            {
                String display_name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String phone_number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                String contactRawId = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.RAW_CONTACT_ID));
                data.put("display_name", display_name);
                data.put("phone_number", phone_number);
                data.put("contactRawId", contactRawId);
                return data;
            } else {
                return null;
            }

        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
