package mn.eq.health4men.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import mn.eq.health4men.Members.MembersFragment;
import mn.eq.health4men.Objects.UserItem;
import mn.eq.health4men.R;


/**
 * Created by eQ on 10/26/15.
 */
public class AdapterList extends RecyclerView.Adapter<AdapterList.ViewHolder> {

    private ArrayList<String> mDataset;
    private Context context;
    private int lastPosition = -1;
    public MembersFragment membersFragment;

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textView;
        public FrameLayout backLayout;
        public View view;

        public ViewHolder(View v) {
            super(v);
            textView = (TextView) v.findViewById(R.id.textView);
            backLayout = (FrameLayout) v.findViewById(R.id.backLayout);
            view = v.findViewById(R.id.onlineView);
        }
    }

    public void add(String item,int position) {
        mDataset.add(position, item);
        notifyItemInserted(position);
    }

    public void remove(int position) {
        mDataset.remove(position);
        notifyItemRemoved(position);
    }

    public AdapterList(Context context, ArrayList<String> myDataset) {
        this.context = context;
        mDataset = myDataset;
    }

    @Override
    public AdapterList.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_list, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        String memberItem = mDataset.get(position);
        holder.textView.setText(memberItem);

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
