package mn.eq.health4men.Root;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.location.Location;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cocosw.bottomsheet.BottomSheet;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.ArrayList;

import mn.eq.health4men.Album.MyAlbum;
import mn.eq.health4men.LeftMenu.FragmentDrawer;
import mn.eq.health4men.Login.LoginActivity;
import mn.eq.health4men.Map.FragmentMap;
import mn.eq.health4men.Members.MembersFragment;
import mn.eq.health4men.Objects.IsOnline;
import mn.eq.health4men.Objects.UserItem;
import mn.eq.health4men.R;
import mn.eq.health4men.Root.RootActivity;
import mn.eq.health4men.UserEdit.PopUpFragment;
import mn.eq.health4men.UserEdit.ProfileEditFragment;
import mn.eq.health4men.Utils.Utils;

public class MainActivity extends RootActivity implements FragmentDrawer.FragmentDrawerListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private FragmentDrawer drawerFragment;
    private DrawerLayout drawerLayout;
    private int lastSelectedIndex = -1;
    private FragmentManager fragmentManager = getSupportFragmentManager();
    private TextView toolbarTitle;
    private FrameLayout toolbar;
    private ImageButton menuIcon;
    private LinearLayout done;
    private boolean popUpShowed;
    private static String TAG = "MAIN ACTIVITY : ";
    private ProgressDialog progressDialog;
    private ProgressDialog logOutDialog;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
    private GoogleApiClient mGoogleApiClient;
    private MembersFragment membersFragment;

    public MainActivity mainActivity;
    public static Location mLastLocation;
    public static ImageButton whiteStar;
    public static int deviceWidth;
    public static int deviceHeight;
    public static FloatingActionButton msgButton;
    private ImageButton searchButton;
    private CountDownTimer isOnlineTimer;
    private ArrayList<IsOnline> isOnlineArrayList = new ArrayList<>();
    private boolean onlineRequestFinished = true,isSearchEnabled = false;
    private LinearLayout toolbarLayout;
    private EditText searchEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mainActivity = this;
        getDeviceWidth();
        createInterface();
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

    private void createInterface() {
        progressDialog = new Utils().getProgressDialog(this, "Logging in");
        logOutDialog = new Utils().getProgressDialog(this,"Logging out");

        toolbar = (FrameLayout) findViewById(R.id.toolbar);

        msgButton = (FloatingActionButton) findViewById(R.id.msgButton);

        done = (LinearLayout) findViewById(R.id.done);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserInformation();
            }
        });

        drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), toolbar);
        drawerFragment.setDrawerListener(this);

        toolbarTitle = (TextView) findViewById(R.id.toolbarTitle);

        menuIcon = (ImageButton) findViewById(R.id.menuIcon);
        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });

        whiteStar = (ImageButton) findViewById(R.id.whiteStar);

        searchButton = (ImageButton) findViewById(R.id.search);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isSearchEnabled){
                    showSearch();
                }else {
                    showMenu();
                }
            }
        });

        searchEditText = (EditText) findViewById(R.id.searchEditText);

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                MembersFragment.searchArrayList.clear();

                for (UserItem userItem : MembersFragment.arrayList){
                    if (userItem.getUserName().toLowerCase().contains(s.toString().toLowerCase())){
                        MembersFragment.searchArrayList.add(userItem);
                    }
                }
                MembersFragment.adapterMembers.notifyDataSetChanged();

            }
        });

        toolbarLayout = (LinearLayout) findViewById(R.id.toolbarLayout);

        displayView(0);

        if (checkPlayServices()) {

            buildGoogleApiClient();
        }

        isOnlineRequest();

    }

    public void showMenu(){
        searchEditText.setText("");
        toolbarLayout.setVisibility(View.VISIBLE);
        searchEditText.setVisibility(View.GONE);
        isSearchEnabled = !isSearchEnabled;
    }

    public void showSearch(){
        toolbarLayout.setVisibility(View.GONE);
        searchEditText.setVisibility(View.VISIBLE);
        isSearchEnabled = !isSearchEnabled;
    }

    private void isOnlineRequest() {
        isOnlineTimer = new CountDownTimer(25000000, 5000) {
            @Override
            public void onTick(long l) {

                if(onlineRequestFinished){
                    onlineRequestFinished = false;
                    isOnlineRequestToServer();
                }
            }

            @Override
            public void onFinish() {
                this.cancel();
            }
        };

        isOnlineTimer.start();
    }

    private void isOnlineRequestToServer() {

        if (SplachScreenActivity.utils.isNetworkConnected(this)) {

            String url = Utils.MAIN_HOST + "is_online.php";
            Utils.client.get(url, new JsonHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    super.onSuccess(statusCode, headers, response);

                    isOnlineArrayList.clear();

                    for (int i = 0; i < response.length(); i++) {
                        try {
                            isOnlineArrayList.add(new IsOnline(response.getJSONObject(i)));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    configMainListFromIsOnline();
                    MembersFragment.adapterMembers.notifyDataSetChanged();
                    onlineRequestFinished = true;

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    onlineRequestFinished = true;
                }
            });
        }else {
            SplachScreenActivity.utils.showToast(getString(R.string.no_internet));
            onlineRequestFinished = true;
        }
    }

    private void configMainListFromIsOnline() {

        for (IsOnline isOnline : isOnlineArrayList) {
            changeIsOnline(isOnline);
        }

    }

    private void changeIsOnline(IsOnline isOnline) {
        System.out.println(TAG + "CHANGE IS ONLINE");

        for (UserItem userItem : MembersFragment.arrayList) {

            if (userItem.getUserID() == isOnline.getUserID()) {

                System.out.println(TAG + userItem.getUserName());

                userItem.setMemberOnline(isOnline.isOnline());
                return;
            }

        }

    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
        displayView(position);
    }

    public void displayView(int position) {
        Fragment fragment = null;
        try {
            showMenu();
        }catch (Exception e){

        }

        String title = getString(R.string.app_name);

        if (position == lastSelectedIndex) return;
        if (position != 6) lastSelectedIndex = position;

        if (position == 2 ) {
            whiteStar.setVisibility(View.GONE);
            done.setVisibility(View.VISIBLE);
        }
        if(position==0){
            whiteStar.setVisibility(View.VISIBLE);
            done.setVisibility(View.GONE);
            searchButton.setVisibility(View.VISIBLE);
        }
        if(position == 1){
            whiteStar.setVisibility(View.GONE);
            searchButton.setVisibility(View.GONE);
        }

        if (position == 0) {
            title = "Members";

            membersFragment = new MembersFragment();

            fragment = membersFragment;

            msgButton.setVisibility(View.VISIBLE);
            done.setVisibility(View.GONE);
        }

        if (position == 1) {
            title = "My album";

            fragment = new MyAlbum();

            msgButton.setVisibility(View.GONE);
            done.setVisibility(View.GONE);
        }

        if (position == 2) {
            title = "Profile edit";

            ProfileEditFragment profileEditFragment = new ProfileEditFragment().newInstance(SplachScreenActivity.userItem);
            profileEditFragment.mainActivity = mainActivity;
            fragment = profileEditFragment;

            msgButton.setVisibility(View.GONE);
            done.setVisibility(View.VISIBLE);

        }

        if (position == 3) {

            msgButton.setVisibility(View.GONE);
            logout(2);

        } else {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container, fragment);
            fragmentTransaction.commit();

            toolbarTitle.setText(title);
        }

    }

    public void showPopUp(int type, ProfileEditFragment profileEditFragment) {
        FragmentTransaction fragmentTransac = fragmentManager.beginTransaction();
        PopUpFragment popUpFragment = PopUpFragment.newInstance(type);
        popUpFragment.profileEditFragment = profileEditFragment;
        fragmentTransac.add(R.id.mainContainer, popUpFragment);
        fragmentTransac.commit();
        popUpShowed = true;
    }

    private void updateUserInformation() {

        if (SplachScreenActivity.utils.isNetworkConnected(this)) {

            String url = Utils.MAIN_HOST + "user_update.php";
            SplachScreenActivity.userItem.setUserName(ProfileEditFragment.userNameEditText.getText().toString());
            SplachScreenActivity.userItem.setUserAboutme(ProfileEditFragment.aboutUserEditText.getText().toString());

            RequestParams params = new RequestParams();
            params.put("id", SplachScreenActivity.userItem.getUserID());
            params.put("age", SplachScreenActivity.userItem.getUserAge());
            params.put("name", SplachScreenActivity.userItem.getUserName());
            params.put("height", SplachScreenActivity.userItem.getUserHeight());
            params.put("weight", SplachScreenActivity.userItem.getUserWeight());
            params.put("country", SplachScreenActivity.userItem.getUserCountry());
            params.put("looking_for", SplachScreenActivity.userItem.getUserLookingFor());
            params.put("relationship_status", SplachScreenActivity.userItem.getUserRelationshipStatus());
            params.put("about_me", SplachScreenActivity.userItem.getUserAboutme());

            System.out.println(TAG + "url : " + url);
            System.out.println(TAG + "param : " + params.toString());

            if (!progressDialog.isShowing()) progressDialog.show();

            Utils.client.post(url, params, new JsonHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);

                    if (progressDialog.isShowing()) progressDialog.dismiss();
                    System.out.println(TAG + "UPDATE SUCCESS" + response.toString());

                    if (response.has("success")) {

                        try {
                            if (response.getInt("success") == 0) {
                                SplachScreenActivity.utils.showAlert(MainActivity.this, response.getString("message"));
                            } else {
                                SplachScreenActivity.utils.showAlert(MainActivity.this, "Мэдээллийг амжилттай илгээлээ");

                                FragmentDrawer.userNameTextView.setText(SplachScreenActivity.userItem.getUserName());
                                FragmentDrawer.userEmailTextView.setText(SplachScreenActivity.userItem.getUserEmail());
                                Picasso.with(MainActivity.this).load(SplachScreenActivity.userItem.getUserImageURL()).into(FragmentDrawer.userImageView);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    if (progressDialog.isShowing()) progressDialog.dismiss();
                    SplachScreenActivity.utils.showToast("Сервертэй холбогдоход алдаа гарлаа");
                }
            });

        } else {
            SplachScreenActivity.utils.showNoInternetAlert(MainActivity.this);
        }

    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(getApplicationContext(),
                        "Please update your Google Play Service. We're cant get your location.", Toast.LENGTH_LONG)
                        .show();
            }
            return false;
        }
        return true;
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLastLocation = LocationServices.FusedLocationApi
                .getLastLocation(mGoogleApiClient);

        if (membersFragment != null) {
            membersFragment.adapterMembers.notifyDataSetChanged();
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onBackPressed() {

        if (isSearchEnabled){
            showMenu();
        }else {
            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.app_name_mn))
                    .setMessage(getString(R.string.quit))
                    .setPositiveButton("Quit", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            logout(1);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                        }
                    })
                    .setIcon(R.drawable.alert_logo)
                    .show();
        }

    }

    public void logout(final int type){

        if (SplachScreenActivity.utils.isNetworkConnected(this)) {

            String url = Utils.MAIN_HOST + "mobile_online.php";

            RequestParams params = new RequestParams();
            params.put("id",SplachScreenActivity.userItem.getUserID());
            params.put("is_online", 0);

            System.out.println("LOG OUT : URL ->" + url);
            System.out.println("LOG OUT : PARAMS ->"+params);

            if (!logOutDialog.isShowing()) logOutDialog.show();

            Utils.client.post(url,params, new JsonHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    if (logOutDialog.isShowing()) logOutDialog.dismiss();
                    if (type == 1)finish();
                    if (type == 2){
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);

                        finish();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    if (logOutDialog.isShowing()) logOutDialog.dismiss();
                    if (type == 1)finish();
                    if (type == 2){
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);

                        finish();
                    }
                }
            });
        }else {
            if (logOutDialog.isShowing()) logOutDialog.dismiss();
            if (type == 1)finish();
            if (type == 2){
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);

                finish();
            }
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        System.out.println("ON DESTROY");

        if (SplachScreenActivity.utils.isNetworkConnected(this)) {

            String url = Utils.MAIN_HOST + "mobile_online.php";

            RequestParams params = new RequestParams();
            params.put("id",SplachScreenActivity.userItem.getUserID());
            params.put("is_online", 0);

            System.out.println("LOG OUT : URL ->" + url);
            System.out.println("LOG OUT : PARAMS ->"+params);

            Utils.client.post(url,params, new JsonHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                }
            });
        }else {

        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        System.out.println("ON STOP");

        if (SplachScreenActivity.utils.isNetworkConnected(this)) {

            String url = Utils.MAIN_HOST + "mobile_online.php";

            RequestParams params = new RequestParams();
            params.put("id",SplachScreenActivity.userItem.getUserID());
            params.put("is_online", 0);

            System.out.println("LOG OUT : URL ->" + url);
            System.out.println("LOG OUT : PARAMS ->"+params);

            Utils.client.post(url,params, new JsonHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                }
            });
        }else {
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (SplachScreenActivity.utils.isNetworkConnected(this)) {

            String url = Utils.MAIN_HOST + "mobile_online.php";

            RequestParams params = new RequestParams();
            params.put("id",SplachScreenActivity.userItem.getUserID());
            params.put("is_online", 1);

            System.out.println("LOG OUT : URL ->" + url);
            System.out.println("LOG OUT : PARAMS ->"+params);

            Utils.client.post(url,params, new JsonHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                }
            });
        }else {

        }
    }
}