package mn.eq.health4men.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import mn.eq.health4men.Chat.ChatActivity;
import mn.eq.health4men.Objects.ChatItem;
import mn.eq.health4men.R;

/**
 * Created by xashaa on 8/20/15.
 */
public class ChatAdapter extends BaseAdapter {

    public Context context;
    public ArrayList<ChatItem> zurvasArray;
    public int userType;
    public ChatActivity chatActivity;
    public boolean isLaunched = false;

    public ChatAdapter(Context context, ArrayList<ChatItem> zurvasArray){
        this.context = context;
        this.zurvasArray = zurvasArray;
        userType = 1;
    }


    @Override
    public int getCount() {
        return zurvasArray.size();
    }

    @Override
    public Object getItem(int i) {
        return zurvasArray.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {

        ZurvasHolder zurvasHolder = new ZurvasHolder();
        ChatItem zurvasItem = zurvasArray.get(i);
            View vi=convertView;

        if(convertView==null)
            vi = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.cell_chat, null, false);

            if (zurvasItem.isMyMessage){
                LinearLayout teacher = (LinearLayout)vi.findViewById(R.id.cellUser);
                teacher.setVisibility(View.VISIBLE);

                LinearLayout user = (LinearLayout)vi.findViewById(R.id.cellTeacher);
                user.setVisibility(View.GONE);

                zurvasHolder.icon = (CircleImageView) vi.findViewById(R.id.imageViewUser);
                zurvasHolder.title = (TextView) vi.findViewById(R.id.titleUser);
                zurvasHolder.body = (TextView) vi.findViewById(R.id.bodyUser);
                zurvasHolder.date = (TextView) vi.findViewById(R.id.dateUser);
                zurvasHolder.isRead = (TextView) vi.findViewById(R.id.isReadUser);

            }else {
                LinearLayout teacher = (LinearLayout)vi.findViewById(R.id.cellUser);
                teacher.setVisibility(View.GONE);

                LinearLayout user = (LinearLayout)vi.findViewById(R.id.cellTeacher);
                user.setVisibility(View.VISIBLE);

                zurvasHolder.icon = (CircleImageView) vi.findViewById(R.id.imageViewTeacher);
                zurvasHolder.title = (TextView) vi.findViewById(R.id.titleTeacher);
                zurvasHolder.body = (TextView) vi.findViewById(R.id.bodyTeacher);
                zurvasHolder.date = (TextView) vi.findViewById(R.id.dateTeacher);
                zurvasHolder.isRead = (TextView) vi.findViewById(R.id.isReadTeacher);

            }
        if (zurvasItem.userImageURL.length() > 0){
            Picasso.with(context)
                    .load(zurvasItem.userImageURL)
                    .placeholder(R.drawable.img_user_default)
                    .into(zurvasHolder.icon);
        }else {
            zurvasHolder.icon.setImageResource(R.drawable.img_user_default);
        }


        zurvasHolder.title.setText(zurvasItem.userName);
        zurvasHolder.body.setText(zurvasItem.body);

        zurvasHolder.date.setText(zurvasItem.date.replace("-","."));

        if (zurvasItem.isRead){
            zurvasHolder.isRead.setText("Уншсан");
        }else {
            zurvasHolder.isRead.setText("");
        }

        if (i == 0){
            if (!isLaunched){
                chatActivity.getBeforeZurvas();
                isLaunched = true;
            }
        }
        if (i == (zurvasArray.size() - 1)){
            chatActivity.setRead();
        }

        return vi;
    }

    class ZurvasHolder {
        CircleImageView icon;
        TextView title;
        TextView body;
        TextView date;
        TextView isRead;
    }

    public String getDateStr(String str){
        String retString = "";

        String month = str.substring(5,7);
        String day = str.substring(8,10);

        int index = 5;
        int index1 = 8;

        if (month.charAt(0) == '0')index = 6;
        if (day.charAt(0) == '0')index1 = 9;

        retString = retString + str.substring(index,7) + " сарын ";
        retString = retString + str.substring(index1,10) + " ";
        retString = retString + str.substring(11,19);

        return retString;
    }
}
