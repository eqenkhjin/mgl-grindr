package mn.eq.health4men.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import mn.eq.health4men.Members.MembersFragment;
import mn.eq.health4men.Objects.UserImageItem;
import mn.eq.health4men.Objects.UserItem;
import mn.eq.health4men.R;
import mn.eq.health4men.Root.MainActivity;

/**
 * Created by Tamir on 11/21/2015.
 */
public class AdapterImages extends RecyclerView.Adapter<AdapterImages.ViewHolder> {

    private boolean isList = true;
    private ArrayList<UserImageItem> mDataset;
    private Context context;
    private int lastPosition = -1;
    public MembersFragment membersFragment;

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView img;

        public ViewHolder(View v) {
            super(v);
            img = (ImageView) v.findViewById(R.id.imgPicture);
            RelativeLayout layout = (RelativeLayout) v.findViewById(R.id.row_image_relative);
            layout.getLayoutParams().width = MainActivity.deviceWidth/4;
            layout.getLayoutParams().height = MainActivity.deviceWidth/4;
        }
    }

    public void add(UserImageItem item,int position) {
        mDataset.add(position, item);
        notifyItemInserted(position);
    }

    public void remove(int position) {
        mDataset.remove(position);
        notifyItemRemoved(position);
    }

    public AdapterImages(Context context, ArrayList<UserImageItem> myDataset) {
        this.isList = isList;
        this.context = context;
        mDataset = myDataset;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                                        int viewType) {
        View v;
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_image, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

//        if (position == mDataset.size() - 1){
//            if (!membersFragment.isWaitResponse){
//                membersFragment.pageIndex = membersFragment.pageIndex + 1;
//                membersFragment.getMembers();
//            }
//        }

        UserImageItem imgItem = mDataset.get(position);
        if (imgItem.getImageURL().length() > 3) {

            System.out.println("IMAGE URL : " + imgItem.getImageURL());

            Picasso.with(context).load(imgItem.getImageURL()).placeholder(R.drawable.placholder_member)
                    .into(holder.img);
        } else {
            holder.img.setImageResource(R.drawable.placholder_member);
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

