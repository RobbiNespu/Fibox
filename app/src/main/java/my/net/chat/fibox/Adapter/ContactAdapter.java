package my.net.chat.fibox.Adapter;

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

import java.util.ArrayList;

import my.net.chat.fibox.R;

/**
 * Created by kamarulzaman on 1/2/15.
 */
public class ContactAdapter extends BaseAdapter {

    Context context;
    ArrayList<ContactArray> items = new ArrayList<ContactArray>();
    Transformation mTransformation;

    public ContactAdapter(Context context, ArrayList<ContactArray> items) {
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
            final ContactArray item = (ContactArray) this.getItem(position);
            if(convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(context).inflate(R.layout.contact_row, parent, false);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            try{

                holder.id = (TextView) convertView.findViewById(R.id.id);
                holder.name = (TextView) convertView.findViewById(R.id.name);
                holder.image_url = (ImageView) convertView.findViewById(R.id.image_url);
                holder.status = (TextView) convertView.findViewById(R.id.status);
                holder.type = (TextView) convertView.findViewById(R.id.type);
                holder.phone_number = (TextView) convertView.findViewById(R.id.phone_number);

                String type = "Mobile";
                switch(item.getType()) {
                    case 1:
                        type = "Mobile";
                        break;
                    default:
                        type = "Mobile";
                        break;
                }

                holder.id.setText(item.getID());
                holder.name.setText(item.getName());
                holder.status.setText(item.getStatus());
                holder.type.setText(type);
                holder.phone_number.setText(item.getPhoneNumber());
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
        TextView name;
        ImageView image_url;
        TextView status;
        TextView type;
        TextView phone_number;
    }
}
