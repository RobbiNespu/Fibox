package my.net.fims.fibox.Adapter;

/**
 * Created by kamarulzaman on 1/2/15.
 */
public class ContactArray {

    private String id, status, image_url, name, phone_number;
    private int type;

    public ContactArray(String id, String phone_number, String image_url, String name, String status, int type) {
        this.name = name;
        this.status = status;
        this.type = type;
        this.id = id;
        this.image_url = image_url;
        this.phone_number = phone_number;
    }

    public String getID(){
        return this.id;
    }

    public String getName(){
        return this.name;
    }

    public String getStatus() {
        return this.status;
    }

    public String getImageUrl() {
        return this.image_url;
    }

    public String getPhoneNumber(){ return this.phone_number; }

    public int getType() {
        return this.type;
    }


}
