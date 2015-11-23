package mn.eq.health4men.Members;

import android.content.Intent;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import mn.eq.health4men.Adapters.AdapterImages;
import mn.eq.health4men.Adapters.AdapterMembers;
import mn.eq.health4men.Chat.ChatActivity;
import mn.eq.health4men.Chat.ImageFragment;
import mn.eq.health4men.Objects.UserImageItem;
import mn.eq.health4men.Objects.UserItem;
import mn.eq.health4men.R;
import mn.eq.health4men.Root.MainActivity;
import mn.eq.health4men.Utils.RecyclerItemClickListener;

/**
 * Created by Tamir on 11/21/2015.
 */
public class NewUserDetailActivity extends FragmentActivity {

    private TextView userAboutMe;
    private TextView userHeight;
    private TextView userWeight;
    private TextView userAge;
    private TextView userLookingFor;
    private TextView userName;
    private TextView userRole;
    private TextView userDistance;
    private TextView photoQuantity;
    private TextView userLocation;
    private ImageButton newMessage;
    private ImageView userImage;
    private FloatingActionButton msgButton;
    private View onlineView;
    private RecyclerView recyclerView;
    private LinearLayoutManager mLayoutManager;
    private LinearLayout scaledLinear;
    public AdapterImages adapterImages;
    private UserItem userItem;
    private int deviceHeight;
    private int deviceWidth;
    private FragmentManager fragmentManager;
    private LibraryFragment libraryFragment;
    private boolean popUpShowed = false;
    private LinearLayout albumLinear;
    FragmentTransaction fragmentTransac;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_detail);
        getDeviceWidth();
        fragmentManager = getSupportFragmentManager();

        Bundle bundle = getIntent().getExtras();
        userItem = (UserItem) bundle.getSerializable("detail");

        createInterface();

        initData();

        configHeader();

    }

    private void getDeviceWidth() {
        WindowManager w = getWindowManager();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            Point size = new Point();
            w.getDefaultDisplay().getSize(size);
            deviceHeight = size.y;
            deviceWidth = size.x;
        } else {
            Display d = w.getDefaultDisplay();
            deviceWidth = d.getWidth();
            deviceHeight = d.getHeight();

        }
    }

    private void initData() {

        if (userItem != null) {
            if (userItem.getUserImageURL().length() > 3) {
                Picasso.with(getBaseContext()).load(userItem.getUserImageURL()).into(userImage);
            } else
                userImage.setImageResource(R.drawable.placholder_member);
            userName.setText(userItem.getUserName());
            userAboutMe.setText(userItem.getUserAboutme());
            if(userItem.getAlbum().size() == 0){
                albumLinear.setVisibility(View.INVISIBLE);
            }
            photoQuantity.setText("Photos (" + userItem.getAlbum().size() + ")");
            if (userItem.isMemberOnline())
                onlineView.setBackgroundResource(R.drawable.border_online);
            else onlineView.setBackgroundResource(R.drawable.border_offline);
            userAge.setText(userItem.getUserAge());
            userHeight.setText(userItem.getUserHeight());
            userWeight.setText(userItem.getUserWeight());
        }
//        userAge.setText(", "+userItem.getUserAge());
//        userDistance.setText(userItem.getDistanceBetweenMe());
//        userHeight.setText(userItem.getUserHeight());
//        System.out.println("HEIGHT : "+userItem.getUserHeight());
//        userWeight.setText(userItem.getUserWeight());
//        userName.setText(userItem.getUserName());
//        userBodyType.setText(userItem.getUserBodyType());

    }

    private void configHeader() {
        ImageView backImageView = (ImageView) findViewById(R.id.menuIcon);
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

//        TextView headerTitleTextView = (TextView) findViewById(R.id.toolbarTitle);
//        headerTitleTextView.setText("Profile");
    }

    private void createInterfaceByCode() {

//        userAboutMe.setPadding(10, 5, 100, 5);

//        userImage.getLayoutParams().height = deviceHeight / 5 * 3;

        scaledLinear.getLayoutParams().height = deviceWidth / 4;

//        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(msgButton.getLayoutParams());
//        msgButton.getLayoutParams().width = deviceWidth / 5;
//        msgButton.getLayoutParams().height = deviceWidth / 5;
//        lp.setMargins(0, -deviceWidth / 8, 30, 0);
//        lp.addRule(RelativeLayout.BELOW, R.id.userImage);
//        lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
//        msgButton.setLayoutParams(lp);

        recyclerView.getLayoutParams().height = deviceWidth / 4;


    }

    private void createInterface() {

        userAboutMe = (TextView) findViewById(R.id.userAboutme);
        userAge = (TextView) findViewById(R.id.userAge);
        userHeight = (TextView) findViewById(R.id.userHeight);
        userWeight = (TextView) findViewById(R.id.userWeight);
        userName = (TextView) findViewById(R.id.userName);
        userImage = (ImageView) findViewById(R.id.userImage);
        userLookingFor = (TextView) findViewById(R.id.userLookingFor);
        userLocation = (TextView) findViewById(R.id.userLocation);
        userRole = (TextView) findViewById(R.id.userRole);
        onlineView = findViewById(R.id.onlineView);
        scaledLinear = (LinearLayout) findViewById(R.id.scaleLinear);
        photoQuantity = (TextView) findViewById(R.id.photoQuantity);
        albumLinear = (LinearLayout) findViewById(R.id.album_linear);
        recyclerView = (RecyclerView) findViewById(R.id.imgList);
        msgButton = (FloatingActionButton) findViewById(R.id.msgButton);
        recyclerView.setHasFixedSize(true);


        createInterfaceByCode();
//        recyclerView.getLayoutParams().height = MainActivity.deviceWidth/4;
//        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
//           recyclerView.setLayoutManager(new StaggeredGridLayoutManager(4,StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL));

        adapterImages = new AdapterImages(this, userItem.getAlbum());
//        adapterImages = membersFragment;
        recyclerView.setAdapter(adapterImages);
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        showLibrary(position);
//                        final Intent intent = new Intent(getActivity(), NewUserDetailActivity.class);
//
//                        Bundle bundle = new Bundle();
//                        bundle.putSerializable("detail", userItem);
//                        intent.putExtras(bundle);
//
//                        handler.postDelayed(new Runnable() {
//                            public void run() {
//                                getActivity().startActivity(intent);
//                            }
//                        }, 300);
//
//                        Animation animFadeIn = AnimationUtils.loadAnimation(getContext(),
//                                R.anim.selector);
//                        view.startAnimation(animFadeIn);

                    }
                })
        );


        msgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NewUserDetailActivity.this, ChatActivity.class);

                Bundle bundle = new Bundle();
                bundle.putSerializable("chat_user", userItem);
                intent.putExtras(bundle);

                startActivity(intent);
            }
        });

    }

    public void showLibrary(int position) {
        if (fragmentTransac == null) fragmentTransac = fragmentManager.beginTransaction();
        popUpShowed = true;
        libraryFragment = new LibraryFragment();
        libraryFragment.userItem = userItem;
        libraryFragment.currentPosition = position;

        fragmentTransac.add(R.id.container, libraryFragment);

        fragmentTransac.commit();

    }

    @Override
    public void onBackPressed() {
//        int count = getFragmentManager().getBackStackEntryCount();
//        getFragmentManager().popBackStack();

//        if(count==0){
//            super.onBackPressed();
//        } else {
//            getFragmentManager().popBackStack();
//
//        }
        if (popUpShowed) {
            fragmentTransac.remove(libraryFragment).commit();
            popUpShowed = false;
        } else {
            finish();
        }
    }
}
