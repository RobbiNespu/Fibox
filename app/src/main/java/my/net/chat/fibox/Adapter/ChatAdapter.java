package my.net.chat.fibox.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

import my.net.chat.fibox.R;


/**
 * Created by kamarulzaman on 1/2/15.
 */
public class ChatAdapter extends BaseAdapter {

    ArrayList<ChatArray> items;
    Context context;

    public ChatAdapter(Context context, ArrayList<ChatArray> items){
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
            ViewHolder holder;
            ChatArray item = (ChatArray) this.getItem(position);
            if(convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.chat_row, parent, false);
                holder = new ViewHolder();
                holder.chat_id = (TextView) convertView.findViewById(R.id.chat_id);
                holder.message = (TextView) convertView.findViewById(R.id.message);
                holder.message_time = (TextView) convertView.findViewById(R.id.message_time);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }


            holder.chat_id.setText(item.getID());
            holder.message.setText(item.getMessage());
            holder.message_time.setText(item.getMessageTime());

            TableLayout tbllayout = (TableLayout) convertView.findViewById(R.id.chatrow);
            TableRow tblrow = (TableRow) convertView.findViewById(R.id.row);
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) tbllayout.getLayoutParams();

            if(item.getMySelf()) {
                tbllayout.setBackgroundResource(R.drawable.balloon_outgoing_normal);
                tbllayout.setPadding(30, 20, 50, 20);
                holder.message_time.setTextColor(Color.parseColor("#838383"));
                holder.message.setTextColor(Color.parseColor("#282828"));
                lp.gravity = Gravity.RIGHT;

            } else {
                tbllayout.setBackgroundResource(R.drawable.balloon_incoming_normal);
                tbllayout.setPadding(50, 20, 30, 20);
                holder.message_time.setTextColor(Color.parseColor("#838383"));
                holder.message.setTextColor(Color.parseColor("#282828"));
                lp.gravity = Gravity.LEFT;
            }
            tbllayout.setLayoutParams(lp);
            return convertView;
        } catch(Exception e) {
            e.printStackTrace();
            return convertView;
        }
    }

    class ViewHolder{
        TextView chat_id;
        TextView message;
        TextView message_time;
    }
}
