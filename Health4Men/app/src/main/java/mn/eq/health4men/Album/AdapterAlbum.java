package mn.eq.health4men.Album;

/**
 * Created by Tamir on 11/24/2015.
 */

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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import mn.eq.health4men.Members.MembersFragment;
import mn.eq.health4men.Objects.UserImageItem;
import mn.eq.health4men.Objects.UserItem;
import mn.eq.health4men.R;
import mn.eq.health4men.Root.MainActivity;


/**
 * Created by eQ on 10/26/15.
 */
public class AdapterAlbum extends RecyclerView.Adapter<AdapterAlbum.ViewHolder> {

    private boolean isList = true;
    private ArrayList<UserImageItem> mDataset;
    private Context context;
    private int lastPosition = -1;
//    public MembersFragment membersFragment;

    public class ViewHolder extends RecyclerView.ViewHolder {


        public RelativeLayout layout;
        public ImageView albumImage;
        public View view;

        public ViewHolder(View v) {
            super(v);
            layout = (RelativeLayout) v.findViewById(R.id.row_image_relative);
            albumImage = (ImageView) v.findViewById(R.id.imgPicture);

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

    public AdapterAlbum(Context context, ArrayList<UserImageItem> myDataset) {
        this.isList = isList;
        this.context = context;
        mDataset = myDataset;
    }

    @Override
    public AdapterAlbum.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                        int viewType) {
        View v;
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_album, parent, false);
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

        UserImageItem albumItem = mDataset.get(position);
        if (albumItem.getImageURL().length() > 3) {
            Picasso.with(context).load(albumItem.getImageURL()).into(holder.albumImage);
        } else {
            holder.albumImage.setImageResource(R.drawable.placholder_member);
        }

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
