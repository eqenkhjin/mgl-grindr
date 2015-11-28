package mn.eq.health4men.Login;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

import mn.eq.health4men.Members.MembersFragment;
import mn.eq.health4men.Objects.UserItem;
import mn.eq.health4men.R;
import mn.eq.health4men.Root.MainActivity;
import mn.eq.health4men.Root.RootActivity;
import mn.eq.health4men.Root.SplachScreenActivity;
import mn.eq.health4men.Utils.Utils;

/**
 * Created by eQ on 11/8/15.
 */
public class LoginActivity extends RootActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private TextView loginButton;
    private ProgressDialog progressDialog;
    private static String TAG = "Login Activity : ";
    private EditText emailEditText;
    private EditText passwordEditText;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
    private GoogleApiClient mGoogleApiClient;
    public static Location mLastLocation;
    private TextView registerTextView;
    private ImageView loginBack;
    private TextView titleTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        progressDialog = new Utils().getProgressDialog(this, "Logged in");
        createInterface();
        if (checkPlayServices()) {
            buildGoogleApiClient();
        }
    }

    public void createInterface(){

        titleTextView = (TextView) findViewById(R.id.titleTextView);
        loginBack = (ImageView) findViewById(R.id.loginBack);
        emailEditText = (EditText) findViewById(R.id.emailEditText);
        emailEditText.setText(SplachScreenActivity.utils.sharedPreferences.getString("EMAIL",""));
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        passwordEditText.setText(SplachScreenActivity.utils.sharedPreferences.getString("PASSWORD",
                ""));

        loginButton = (TextView) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkIsEmpty()) loginToServer();
            }
        });

        registerTextView = (TextView) findViewById(R.id.registerTextView);
        registerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        getDynamicLoginBack();

    }

    private void loginToServer() {

        if (SplachScreenActivity.utils.isNetworkConnected(this)) {

            String url = Utils.MAIN_HOST + "login.php";

            RequestParams params = new RequestParams();
            params.put("email", emailEditText.getText().toString());
            params.put("password", passwordEditText.getText().toString());

            System.out.println(TAG + "url : " + url);
            System.out.println(TAG + "param : " + params.toString());

            if (!progressDialog.isShowing()) progressDialog.show();

            Utils.client.post(url, params, new JsonHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);

                    if (progressDialog.isShowing()) progressDialog.dismiss();
                    System.out.println(TAG + "LOGIN SUCCESS" + response.toString());

                    SplachScreenActivity.utils.editor.putString("EMAIL", emailEditText.getText()
                            .toString());
                    SplachScreenActivity.utils.editor.putString("PASSWORD",passwordEditText
                            .getText().toString());
                    SplachScreenActivity.utils.editor.commit();

                    if (response.has("success")) {

                        try {
                            if (response.getInt("success") == 0){
                                SplachScreenActivity.utils.showAlert(LoginActivity.this,response.getString("message"));
                            }else {
                                SplachScreenActivity.userItem = new UserItem(response);
                                SplachScreenActivity.userItem.setUserEmail(emailEditText.getText().toString());
                                startMain();
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
                    SplachScreenActivity.utils.showToast(getString(R.string.server_error));
                }
            });

        } else {
            SplachScreenActivity.utils.showNoInternetAlert(this);
        }

    }

    private boolean checkIsEmpty() {
        if (emailEditText.getText().toString().length() == 0
                || passwordEditText.getText().toString().length() == 0) {
            SplachScreenActivity.utils.showToast("Please fill all fields");
            return false;
        }
        return true;
    }

    private void startMain(){
        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                Toast.makeText(getApplicationContext(),
                        "We're cant get your location.Please update your Google Play Service. ", Toast.LENGTH_LONG)
                        .show();
            } else {
                Toast.makeText(getApplicationContext(),
                        "We're cant get your location.Please update your Google Play Service. ", Toast.LENGTH_LONG)
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
        postLocationToServer();
        try {
            MembersFragment.adapterMembers.notifyDataSetChanged();
        }catch (Exception e){

        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    private void postLocationToServer() {

        int userID;

        try {
            userID = SplachScreenActivity.userItem.getUserID();
        }catch (Exception e){
            return;
        }

        if (SplachScreenActivity.utils.isNetworkConnected(this)) {

            String url = Utils.MAIN_HOST + "location.php";

            RequestParams params = new RequestParams();
            params.put("user_id", userID);
            params.put("urtrag", mLastLocation.getLongitude());
            params.put("urgurug", mLastLocation.getLatitude());

            System.out.println(TAG + " location param : "+params);

            Utils.client.post(url, params, new JsonHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);

                    System.out.println(TAG + " location response : " + response);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                }
            });

        }

    }

    private void getDynamicLoginBack(){

        if (SplachScreenActivity.utils.isNetworkConnected(this)) {

            String url = Utils.MAIN_HOST + "url.php";

            RequestParams params = new RequestParams();

            Utils.client.get(url, new JsonHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    super.onSuccess(statusCode, headers, response);
                    try {
                        JSONObject object = response.getJSONObject(response.length() - 1);

                        try {
                            Picasso.with(LoginActivity.this).load(object.getString("url"))
                                    .placeholder(R
                                            .drawable.login_back).into(loginBack);
                            titleTextView.setText(object.getString("text"));
                            SplachScreenActivity.utils.editor.putString("IMAGEURL", object
                                    .getString("url"));
                            SplachScreenActivity.utils.editor.putString("TEXT", object
                                    .getString("text"));
                            SplachScreenActivity.utils.editor.commit();
                        }catch (Exception e){

                        }


                    } catch (JSONException e) {
                        e.printStackTrace();

                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);            try {
                        Picasso.with(LoginActivity.this).load(SplachScreenActivity.utils
                                .sharedPreferences.getString("IMAGEURL","")).placeholder(R.drawable
                                .login_back).into(loginBack);
                        titleTextView.setText(SplachScreenActivity.utils.sharedPreferences.getString
                                ("TEXT","Welcome to Health4men"));
                    }catch (Exception e){

                    }
                }
            });

        }else {
            try {
                Picasso.with(LoginActivity.this).load(SplachScreenActivity.utils
                        .sharedPreferences.getString("IMAGEURL","")).placeholder(R.drawable
                        .login_back).into(loginBack);
                titleTextView.setText(SplachScreenActivity.utils.sharedPreferences.getString
                        ("TEXT","Welcome to Health4men"));
            }catch (Exception e){

            }
        }

    }
}
