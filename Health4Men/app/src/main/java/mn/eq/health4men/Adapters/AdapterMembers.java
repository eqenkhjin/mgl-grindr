package mn.eq.health4men.Adapters;

import android.content.ContentValues;
import android.content.Context;
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
import android.widget.TextView;

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
    private boolean isSpeakButtonLongPressed = false;
    public int type;
    public boolean isEditable = false;
    private int duration = 100;
    public MembersFragment membersFragment;

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView goodImageView;
        public TextView goodBigPriceTextView;
        public FrameLayout backLayout;
        public TextView goodNameTextView;
        public TextView goodSizeTextView;
        public TextView goodSmallPriceTextView;
        public TextView goodDescriptionTextView;
        public TextView goodProducerNameTextView;
        public ImageButton deliveryImageView;
        public TextView goodCountEditText;
        public ImageButton minusButton;
        public ImageButton plusButton;
        public ImageView goodTypeImageView;
        public ImageButton paymentImageButton;
        public TextView goodTypeStringTextView;
        public TextView packageCountTextView;
        public FrameLayout edit;
        public CheckBox checkBox;

        public ViewHolder(View v) {
            super(v);
            goodImageView = (ImageView) v.findViewById(R.id.goodImageView);
            goodBigPriceTextView = (TextView) v.findViewById(R.id.goodBigPriceTextView);
            backLayout = (FrameLayout) v.findViewById(R.id.backLayout);
            goodNameTextView = (TextView) v.findViewById(R.id.goodNameTextView);
            goodSizeTextView = (TextView) v.findViewById(R.id.goodSizeTextView);
            goodSmallPriceTextView = (TextView) v.findViewById(R.id.goodSmallPriceTextView);
            goodDescriptionTextView = (TextView) v.findViewById(R.id.goodDescriptionTextView);
            goodProducerNameTextView = (TextView) v.findViewById(R.id.goodProducerNameTextView);
            deliveryImageView = (ImageButton) v.findViewById(R.id.deliveryImageView);
            goodCountEditText = (TextView) v.findViewById(R.id.goodCountEditText);
            minusButton = (ImageButton) v.findViewById(R.id.minusButton);
            plusButton = (ImageButton) v.findViewById(R.id.plusButton);
            goodTypeImageView = (ImageView) v.findViewById(R.id.goodTypeImageView);
            paymentImageButton = (ImageButton) v.findViewById(R.id.paymentImageButton);
            goodTypeStringTextView = (TextView) v.findViewById(R.id.goodTypeStringTextView);
            packageCountTextView = (TextView) v.findViewById(R.id.packageCountTextView);
            edit = (FrameLayout) v.findViewById(R.id.edit);
            checkBox = (CheckBox) v.findViewById(R.id.good_checkbox);
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
            if (type == 0){
                if (!companyDetailActivity.isWaitResponse)
                    companyDetailActivity.productRequest();
            }
        }

        final MemberItem memberItem = mDataset.get(position);

        Picasso.with(context).load(memberItem.getMemberImageURL()).into(holder.goodImageView);
        holder.goodNameTextView.setText(goodItem.getGoodName());
        holder.goodSizeTextView.setText(goodItem.getGoodSize());
        holder.goodDescriptionTextView.setText(goodItem.getGoodDescription());
        holder.goodProducerNameTextView.setText(goodItem.getGoodProducerName().toUpperCase());

        System.out.println(goodItem.getGoodProducerName());

        holder.goodCountEditText.setText(goodItem.getGoodOrderCount() + "");
        holder.goodTypeStringTextView.setText(goodItem.getGoodTypeString());
        holder.packageCountTextView.setText("х/т: "+goodItem.getGoodPackageCount() + "");
        if (goodItem.getGoodType() == 1)holder.goodTypeImageView.setImageResource(R.drawable.new_good);
        if (goodItem.getGoodType() == 2)holder.goodTypeImageView.setImageResource(R.drawable.sale);
        if (goodItem.getGoodType() == 3)holder.goodTypeImageView.setImageResource(R.drawable.price_up);
        if (goodItem.getGoodType() == 4)holder.goodTypeImageView.setImageResource(R.drawable.sale);

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.checkBox.setChecked(!holder.checkBox.isChecked());
                goodItem.setIsDeletable(holder.checkBox.isChecked());
            }
        });

//        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                goodItem.setIsDeletable(isChecked);
//            }
//        });

        try {
            holder.goodSmallPriceTextView.setText("Үнэ: " + MainActivity.utils.formatUne(goodItem.getGoodPrice() + "")+"₮");
            holder.goodBigPriceTextView.setText(MainActivity.utils.formatUne((goodItem.getGoodPrice() * goodItem.getGoodOrderCount())+"")+"₮");
        }catch (Exception e){

        }
        setAnimation(holder.backLayout, position);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    private void setAnimation(View viewToAnimate, int position)
    {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

}
