package mn.eq.health4men.Adapters;

import android.content.Context;
import android.location.Location;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import mn.eq.health4men.Members.MembersFragment;
import mn.eq.health4men.Objects.UserItem;
import mn.eq.health4men.R;
import mn.eq.health4men.Root.MainActivity;


/**
 * Created by eQ on 10/26/15.
 */
public class AdapterMembers extends RecyclerView.Adapter<AdapterMembers.ViewHolder> {

    private boolean isList = true;
    private ArrayList<UserItem> mDataset;
    private Context context;
    private int lastPosition = -1;
    public MembersFragment membersFragment;

    public class ViewHolder extends RecyclerView.ViewHolder {

        public RoundedImageView memberImageView;
        public TextView memberNameTextView;
        public TextView memberDistanceTextView;
        public LinearLayout backLayout;
        public ImageView memberImage;
        public View view;

        public ViewHolder(View v) {
            super(v);
            if(isList){
                memberImageView = (RoundedImageView) v.findViewById(R.id.memberImageView);
                memberNameTextView = (TextView) v.findViewById(R.id.memberNameTextView);
                memberDistanceTextView = (TextView) v.findViewById(R.id.memberDistanceTextView);
                backLayout = (LinearLayout) v.findViewById(R.id.backLayout);
                view = v.findViewById(R.id.onlineView);
            } else {
                LinearLayout layout = (LinearLayout) v.findViewById(R.id.row_columned_linear);
                layout.getLayoutParams().width = MainActivity.deviceWidth/3;
                layout.getLayoutParams().height = MainActivity.deviceWidth/3;
                memberImage = (ImageView) v.findViewById(R.id.row_columned_image);
                memberNameTextView = (TextView) v.findViewById(R.id.row_columned_text);
                view = v.findViewById(R.id.onlineView);
            }

        }
    }

    public void add(UserItem item,int position) {
        mDataset.add(position, item);
        notifyItemInserted(position);
    }

    public void remove(int position) {
        mDataset.remove(position);
        notifyItemRemoved(position);
    }

    public AdapterMembers(Context context, ArrayList<UserItem> myDataset,boolean isList) {
        this.isList = isList;
        this.context = context;
        mDataset = myDataset;
    }

    @Override
    public AdapterMembers.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        View v;
        if(isList){
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_good, parent, false);

        } else {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_columned, parent, false);
        }
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        if (position == mDataset.size() - 1){
                if (!membersFragment.isWaitResponse){
                    membersFragment.pageIndex = membersFragment.pageIndex + 1;
                    membersFragment.getMembers();
                }
        }

        UserItem memberItem = mDataset.get(position);
        if(isList) {
            if (memberItem.getUserImageURL().length() > 3) {
                Picasso.with(context).load(memberItem.getUserImageURL()).placeholder(R.drawable.placholder_member).into(holder.memberImageView);
            } else {
                holder.memberImageView.setImageResource(R.drawable.placholder_member);
            }

            holder.memberNameTextView.setText(memberItem.getUserName() + ", " + memberItem.getUserAge());

            if (memberItem.isMemberOnline())
                holder.view.setBackgroundResource(R.drawable.border_online);
            else holder.view.setBackgroundResource(R.drawable.border_offline);

            holder.memberDistanceTextView.setText(memberItem.getDistanceBetweenMe());
        } else {
            if (memberItem.getUserImageURL().length() > 3) {
                Picasso.with(context).load(memberItem.getUserImageURL()).into(holder.memberImage);
            } else {
                holder.memberImage.setImageResource(R.drawable.placholder_member);
            }
            if (memberItem.isMemberOnline())
                holder.view.setBackgroundResource(R.drawable.border_online);
            else holder.view.setBackgroundResource(R.drawable.border_offline);
            holder.memberNameTextView.setText(memberItem.getDistanceBetweenMe());
        }

        //setAnimation(holder.backLayout, position);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    private void setAnimation(View viewToAnimate, int position)
    {
        if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }


}
