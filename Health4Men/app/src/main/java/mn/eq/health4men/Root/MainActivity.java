package mn.eq.health4men.Root;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cocosw.bottomsheet.BottomSheet;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.ArrayList;

import mn.eq.health4men.LeftMenu.FragmentDrawer;
import mn.eq.health4men.Login.LoginActivity;
import mn.eq.health4men.Map.FragmentMap;
import mn.eq.health4men.Members.MembersFragment;
import mn.eq.health4men.R;
import mn.eq.health4men.Root.RootActivity;
import mn.eq.health4men.UserEdit.PopUpFragment;
import mn.eq.health4men.UserEdit.ProfileEditFragment;
import mn.eq.health4men.Utils.Utils;

public class MainActivity extends RootActivity implements FragmentDrawer.FragmentDrawerListener {

    private FragmentDrawer drawerFragment;
    private DrawerLayout drawerLayout;
    private int lastSelectedIndex = -1;
    private FragmentManager fragmentManager = getSupportFragmentManager();
    private TextView toolbarTitle;
    private FrameLayout toolbar;
    private ImageButton menuIcon;
    private ImageButton whiteStar;
    private LinearLayout done;
    public MainActivity mainActivity;
    private boolean popUpShowed;
    private static String TAG = "Profile Edit : ";
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainActivity = this;
        createInterface();
    }

    private void createInterface(){
        progressDialog = new Utils().getProgressDialog(this, "Нэвтэрч байна");

        toolbar = (FrameLayout) findViewById(R.id.toolbar);

        done = (LinearLayout) findViewById(R.id.done);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserInformation();
            }
        });

        whiteStar = (ImageButton) findViewById(R.id.whiteStar);

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

        displayView(0);
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
        displayView(position);
    }

    public void displayView(int position) {
        Fragment fragment = null;

        String title = getString(R.string.app_name);

        if (position == lastSelectedIndex) return;
        if (position != 6)lastSelectedIndex = position;

        if (position == 2){
            whiteStar.setVisibility(View.GONE);
            done.setVisibility(View.VISIBLE);
        }else {
            whiteStar.setVisibility(View.VISIBLE);
            done.setVisibility(View.GONE);
        }

        if (position == 0) {
            title = "Members";

            fragment = new MembersFragment();

        }

        if (position == 1) {
            title = "Map";

            fragment = new FragmentMap();

        }

        if (position == 2) {
            title = "Profile edit";

            ProfileEditFragment profileEditFragment = new ProfileEditFragment().newInstance(SplachScreenActivity.userItem);
            profileEditFragment.mainActivity = mainActivity;
            fragment = profileEditFragment;

        }

        if (position == 3) {

            Intent intent = new Intent(MainActivity.this,LoginActivity.class);
            startActivity(intent);

            finish();

        }else {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container, fragment);
            fragmentTransaction.commit();

            toolbarTitle.setText(title);
        }

    }

    public void showPopUp(int type,ProfileEditFragment profileEditFragment) {
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
            params.put("body_type", SplachScreenActivity.userItem.getUserBodyType());
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

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.app_name_mn))
                .setMessage(getString(R.string.quit))
                .setPositiveButton("Quit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
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
