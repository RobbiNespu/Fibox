package my.net.fims.fibox.Model;

import com.orm.SugarRecord;

/**
 * Created by kamarulzaman on 1/1/15.
 */
public class Contact extends SugarRecord<Contact> {

    String phone_number, display_picture, status, contact_id;
    public Contact(){

    }

    public Contact(String contact_id, String phone_number, String display_picture, String status)
    {
        //this.name = name;
        this.phone_number = phone_number;
        this.display_picture = display_picture;
        this.status = status;
        this.contact_id = contact_id;
    }

    public String getContactId() {
        return this.contact_id;
    }

    public String getPhoneNumber() {
        return this.phone_number;
    }

    public String getDisplayPicture() {
        return this.display_picture;
    }

    public String getStatus() {
        return this.status;
    }
}
