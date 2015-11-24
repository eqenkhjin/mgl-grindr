package mn.eq.health4men.LeftMenu;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import mn.eq.health4men.Adapters.NavigationDrawerAdapter;
import mn.eq.health4men.R;
import mn.eq.health4men.Root.SplachScreenActivity;


/**
 * Created by eQ on 8/28/15.
 */
public class FragmentDrawer extends Fragment {

    private static String TAG = FragmentDrawer.class.getSimpleName();

    private RecyclerView recyclerView;
    private DrawerLayout mDrawerLayout;
    private NavigationDrawerAdapter adapter;
    private View containerView;
    private static String[] titles = null;
    private FragmentDrawerListener drawerListener;
    public static TextView userNameTextView;
    public static TextView userEmailTextView;
    public static ImageView userImageView;

    public static View layout;


    public FragmentDrawer() {

    }

    public void setDrawerListener(FragmentDrawerListener listener) {
        this.drawerListener = listener;
    }

    public static List<NavDrawerItem> getData() {
        List<NavDrawerItem> data = new ArrayList<>();

        data.add(new NavDrawerItem("Members",R.drawable.members));
        data.add(new NavDrawerItem("Map",R.drawable.map));
        data.add(new NavDrawerItem("Profile edit",R.drawable.icon_edit));
        data.add(new NavDrawerItem("Log out",R.drawable.logout));

        return data;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);

        layout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

        recyclerView = (RecyclerView) layout.findViewById(R.id.drawerList);

        adapter = new NavigationDrawerAdapter(getActivity(), getData());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                drawerListener.onDrawerItemSelected(view, position);
                mDrawerLayout.closeDrawer(containerView);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        userNameTextView = (TextView) layout.findViewById(R.id.userNameTextView);
        userNameTextView.setText(SplachScreenActivity.userItem.getUserName());

        userEmailTextView = (TextView) layout.findViewById(R.id.userEmailTextView);
        userEmailTextView.setText(SplachScreenActivity.userItem.getUserEmail());

        userImageView = (ImageView) layout.findViewById(R.id.userImageView);

        if (SplachScreenActivity.userItem.getUserImageURL().length() > 3) {
            Picasso.with(getActivity()).load(SplachScreenActivity.userItem.getUserImageURL()).into(userImageView);
        } else {
            userImageView.setImageResource(R.drawable.placholder_member);
        }
        return layout;
    }

    public static void changeColor() {
        layout.setBackgroundColor(Color.parseColor("#125688"));
    }


    public void setUp(int fragmentId, DrawerLayout drawerLayout, final FrameLayout toolbar) {
        containerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;

    }

    public static interface ClickListener {
        public void onClick(View view, int position);

        public void onLongClick(View view, int position);
    }

    static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }


    }

    public interface FragmentDrawerListener {
        public void onDrawerItemSelected(View view, int position);
    }
}
