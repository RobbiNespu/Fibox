package my.net.fims.fibox.Adapter;

/**
 * Created by kamarulzaman on 1/2/15.
 */
public class ContactDataArray {
    String contact_id, display_name, phone_number;

    public ContactDataArray(String contact_id, String display_name, String phone_number){
        this.contact_id = contact_id;
        this.display_name = display_name;
        this.phone_number = phone_number;
    }

    public String getContactID() {
        return this.contact_id;
    }

    public String getDisplayName() {
        return this.display_name;
    }

    public String getPhoneNumber() {
        return this.phone_number;
    }
}
