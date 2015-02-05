package my.net.chat.fibox.Adapter;

/**
 * Created by kamarulzaman on 1/2/15.
 */
public class ConversationArray {

    private String id, title, text, text_time, image_url;
    private int type;

    public ConversationArray(String id, String image_url, String title, String text, String text_time, int type) {
        this.title = title;
        this.text = text;
        this.text_time = text_time;
        this.type = type;
        this.id = id;
        this.image_url = image_url;
    }

    public String getID(){
        return this.id;
    }

    public String getTitle(){
        return this.title;
    }

    public String getText() {
        return this.text;
    }

    public String getTextTime() {
        return this.text_time;
    }

    public String getImageUrl() {
        return this.image_url;
    }

    public int getType() {
        return this.type;
    }

}
