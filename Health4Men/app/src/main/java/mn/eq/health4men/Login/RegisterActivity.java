package mn.eq.health4men.Login;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import mn.eq.health4men.R;
import mn.eq.health4men.Root.RootActivity;
import mn.eq.health4men.Root.SplachScreenActivity;
import mn.eq.health4men.Utils.Utils;

/**
 * Created by eQ on 11/10/15.
 */
public class RegisterActivity extends RootActivity {

    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText confirmPasswordEditText;
    private EditText ageEditText;

    private Button loginButton;

    private static String TAG = "Register Activity : ";
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        progressDialog = SplachScreenActivity.utils.getProgressDialog(this, "Registering");

        configHeader();
        createInterface();
    }

    private void configHeader() {
        {
            TextView toolbarTitle = (TextView) findViewById(R.id.toolbarTitle);
            toolbarTitle.setText("Sign Up");
        }
        {
            ImageButton menuIcon = (ImageButton) findViewById(R.id.menuIcon);
            menuIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
    }

    private void createInterface() {

        emailEditText = (EditText) findViewById(R.id.emailEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        confirmPasswordEditText = (EditText) findViewById(R.id.confirmPasswordEditText);
        ageEditText = (EditText) findViewById(R.id.ageEditText);

        loginButton = (Button) findViewById(R.id.loginButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkIsEmpty())
                    if (isPasswordMatch())
                        registerRequest();
            }
        });

    }

    private boolean checkIsEmpty() {
        if (emailEditText.getText().toString().length() == 0
                || passwordEditText.getText().toString().length() == 0
                || confirmPasswordEditText.getText().toString().length() == 0
                || ageEditText.getText().toString().length() == 0) {
            SplachScreenActivity.utils.showToast("Please fill all fields");
            return false;
        }
        return true;
    }

    private boolean isPasswordMatch() {
        if (passwordEditText.getText().toString().equalsIgnoreCase(confirmPasswordEditText.getText().toString()))
            return true;
        else{
            SplachScreenActivity.utils.showToast("Password didn't match");
            return false;
        }
    }

    private void registerRequest() {

        if (SplachScreenActivity.utils.isNetworkConnected(this)) {

            String url = Utils.MAIN_HOST + "register.php";

            RequestParams params = new RequestParams();
            params.put("email", emailEditText.getText().toString());
            params.put("password", passwordEditText.getText().toString());
            params.put("age","29");


            System.out.println(TAG + "url : " + url);
            System.out.println(TAG + "param : " + params.toString());

            if (!progressDialog.isShowing()) progressDialog.show();

            Utils.client.post(url, params, new JsonHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);

                    if (progressDialog.isShowing()) progressDialog.dismiss();
                    System.out.println(TAG + "REGISTER SUCCESS" + response.toString());

                    if (response.has("success")) {

                        try {
                            if (response.getInt("success") == 0){
                                SplachScreenActivity.utils.showAlert(RegisterActivity.this,response.getString("message"));
                            }else {
                                SplachScreenActivity.utils.showToast(response.getString("message"));
                                finish();
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
}
