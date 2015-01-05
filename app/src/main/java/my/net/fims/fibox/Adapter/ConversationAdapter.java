package my.net.fims.fibox.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.makeramen.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import org.w3c.dom.Text;

import java.util.ArrayList;

import my.net.fims.fibox.R;

/**
 * Created by kamarulzaman on 1/2/15.
 */
public class ConversationAdapter extends BaseAdapter {

    Context context;
    ArrayList<ConversationArray> items = new ArrayList<ConversationArray>();
    Transformation mTransformation;

    public ConversationAdapter(Context context, ArrayList<ConversationArray> items) {
        mTransformation = new RoundedTransformationBuilder()
                .borderColor(Color.parseColor("#efefeb"))
                .borderWidthDp(1)
                .cornerRadiusDp(30)
                .oval(false)
                .build();
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        try{
            ViewHolder  holder;
            final ConversationArray item = (ConversationArray) this.getItem(position);
            if(convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(context).inflate(R.layout.conversation_row, parent, false);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            try{

                holder.id = (TextView) convertView.findViewById(R.id.id);
                holder.title = (TextView) convertView.findViewById(R.id.title);
                holder.image_url = (ImageView) convertView.findViewById(R.id.image_url);
                holder.text = (TextView) convertView.findViewById(R.id.text);
                holder.text_time = (TextView) convertView.findViewById(R.id.text_time);

                holder.id.setText(item.getID());
                holder.title.setText(item.getTitle());
                holder.text.setText(item.getText());
                holder.text_time.setText(item.getTextTime());
                Picasso.with(convertView.getContext()).load(item.getImageUrl()).fit().transform(mTransformation).into(holder.image_url);

            } catch(Exception e) {
                e.printStackTrace();
            }

        } catch(Exception e) {
            e.printStackTrace();
        }
        return convertView;
    }

    class ViewHolder{
        TextView id;
        TextView title;
        ImageView image_url;
        TextView text;
        TextView text_time;
    }
}
