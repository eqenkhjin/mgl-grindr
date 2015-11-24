package mn.eq.health4men.Login;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import mn.eq.health4men.Objects.UserItem;
import mn.eq.health4men.R;
import mn.eq.health4men.Root.MainActivity;
import mn.eq.health4men.Root.RootActivity;
import mn.eq.health4men.Root.SplachScreenActivity;
import mn.eq.health4men.Utils.Utils;

/**
 * Created by eQ on 11/8/15.
 */
public class LoginActivity extends RootActivity {

    private TextView loginButton;
    private ProgressDialog progressDialog;
    private static String TAG = "Login Activity : ";
    private EditText emailEditText;
    private EditText passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        progressDialog = new Utils().getProgressDialog(this, "Logged in");
        createInterface();
    }

    public void createInterface(){

        emailEditText = (EditText) findViewById(R.id.emailEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);

        loginButton = (TextView) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkIsEmpty()) loginToServer();
            }
        });

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

}
