package mn.eq.health4men.Adapters;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import mn.eq.health4men.Members.MembersFragment;
import mn.eq.health4men.Objects.MemberItem;
import mn.eq.health4men.R;


/**
 * Created by eQ on 10/26/15.
 */
public class AdapterMembers extends RecyclerView.Adapter<AdapterMembers.ViewHolder> {

    private ArrayList<MemberItem> mDataset;
    private Context context;
    private int lastPosition = -1;
    public MembersFragment membersFragment;

    public class ViewHolder extends RecyclerView.ViewHolder {

        public RoundedImageView memberImageView;
        public TextView memberNameTextView;
        public TextView memberDistanceTextView;
        public LinearLayout backLayout;
        public View view;

        public ViewHolder(View v) {
            super(v);
            memberImageView = (RoundedImageView) v.findViewById(R.id.memberImageView);
            memberNameTextView = (TextView) v.findViewById(R.id.memberNameTextView);
            memberDistanceTextView = (TextView) v.findViewById(R.id.memberDistanceTextView);
            backLayout = (LinearLayout) v.findViewById(R.id.backLayout);
            view = v.findViewById(R.id.onlineView);
        }
    }

    public void add(MemberItem item,int position) {
        mDataset.add(position, item);
        notifyItemInserted(position);
    }

    public void remove(int position) {
        mDataset.remove(position);
        notifyItemRemoved(position);
    }

    public AdapterMembers(Context context, ArrayList<MemberItem> myDataset) {
        this.context = context;
        mDataset = myDataset;
    }

    @Override
    public AdapterMembers.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_good, parent, false);
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

        final MemberItem memberItem = mDataset.get(position);

        if (memberItem.getMemberImageURL().length() > 3){
            Picasso.with(context).load(memberItem.getMemberImageURL()).placeholder(R.drawable.placholder_member).into(holder.memberImageView);
        }else {
            holder.memberImageView.setImageResource(R.drawable.placholder_member);
        }

        holder.memberNameTextView.setText(memberItem.getMemberName() + ", " + memberItem.getMemberAge());

        if (memberItem.isMemberOnline())holder.view.setBackgroundResource(R.drawable.border_online);
        else holder.view.setBackgroundResource(R.drawable.border_offline);

        setAnimation(holder.backLayout, position);
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
