package mn.eq.health4men.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import mn.eq.health4men.LeftMenu.NavDrawerItem;
import mn.eq.health4men.R;


/**
 * Created by eQ on 8/28/15.
 */
public class NavigationDrawerAdapter extends RecyclerView.Adapter<NavigationDrawerAdapter.MyViewHolder> {
    List<NavDrawerItem> data = Collections.emptyList();
    private LayoutInflater inflater;
    private Context context;
    private FrameLayout previousClicked;

    public NavigationDrawerAdapter(Context context, List<NavDrawerItem> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    public void delete(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.nav_drawer_row, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        NavDrawerItem current = data.get(position);

        holder.title.setText(current.getTitle());
        holder.logoImageView.setImageResource(current.getLogoID());

        holder.backLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (previousClicked != null) {
                    previousClicked.setBackgroundColor(Color.TRANSPARENT);
                }

                holder.backLayout.setBackgroundColor(Color.parseColor("#ddf5936f"));
                previousClicked = holder.backLayout;
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView logoImageView;
        FrameLayout menu1;
        FrameLayout menu2;
        TextView solo;
        View separator;
        FrameLayout backLayout;

        public MyViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.left_menu_row_title);
            logoImageView = (ImageView) itemView.findViewById(R.id.menu_logo);
            menu1 = (FrameLayout) itemView.findViewById(R.id.menu1);
            backLayout = (FrameLayout) itemView.findViewById(R.id.backLayout);
        }
    }
}
