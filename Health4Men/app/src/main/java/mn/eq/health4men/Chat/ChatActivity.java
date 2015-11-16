package mn.eq.health4men.Chat;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;

import mn.eq.health4men.Adapters.ChatAdapter;
import mn.eq.health4men.Objects.ChatItem;
import mn.eq.health4men.Objects.UserItem;
import mn.eq.health4men.R;
import mn.eq.health4men.Root.SplachScreenActivity;
import mn.eq.health4men.Utils.Utils;


/**
 * Created by xashaa on 8/20/15.
 */
@SuppressLint("ValidFragment")
public class ChatActivity extends AppCompatActivity {

    public ListView mainListView;
    public FrameLayout zurvasFooterLayout;
    public View view;
    public ChatAdapter adapter;
    public ArrayList<ChatItem> arrayList = new ArrayList<ChatItem>();
    public ImageView send;
    public EditText message;
    private boolean isFirstLaunch = true, isLastGetting = false, isFirstGetting = false;
    private int lastMessageIndex, firstMessageIndex, index = 0;
    public static ChatActivity chatActivity;
    public static String TAG = "Chat activity : ";
    public ImageView nochatImageView;
    CountDownTimer timer;
    private int messageID, childID;
    private ProgressDialog progressDialog;
    private UserItem userItem;

    public ChatActivity() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        chatActivity = this;

        configHeader();

        Bundle bundle = getIntent().getExtras();

        userItem = (UserItem) bundle.getSerializable("chat_user");

        init();

        send = (ImageView) findViewById(R.id.zurvasMessageSend);

        message = (EditText) findViewById(R.id.zurvasTextView);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("CLICK");
                if (message.getText().length() > 0) {
                    sendMessage();
                } else {

                }
            }
        });
    }

    private void sendMessage() {

        if (SplachScreenActivity.utils.isNetworkConnected(this)) {

            String url = Utils.MAIN_HOST + "chat_add.php";

            RequestParams params = new RequestParams();
            params.put("from_user", SplachScreenActivity.userItem.getUserID());
            params.put("to_user", userItem.getUserID());
            params.put("text", message.getText().toString());

            System.out.println(TAG + "url : " + url);
            System.out.println(TAG + "param : " + params.toString());


            Utils.client.post(url, params, new JsonHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);

                    System.out.println(TAG + "ADD SUCCESS" + response.toString());

                    try {
                        if (response.getInt("success") == 1) {
                            message.setText("");
                        } else {
                            SplachScreenActivity.utils.showToast("Сервертэй холбогдоход алдаа гарлаа");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        SplachScreenActivity.utils.showToast("Сервертэй холбогдоход алдаа гарлаа");
                    }

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    SplachScreenActivity.utils.showToast("Сервертэй холбогдоход алдаа гарлаа");
                }
            });

        } else {
            SplachScreenActivity.utils.showNoInternetAlert(this);
        }

    }

    private void configHeader() {
        ImageView backImageView = (ImageView) findViewById(R.id.menuIcon);
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                timer.cancel();
                timer = null;
            }
        });

        TextView headerTitleTextView = (TextView) findViewById(R.id.toolbarTitle);
        headerTitleTextView.setText("Зурвас");
    }

    public void init() {
        mainListView = (ListView) findViewById(R.id.zurvasListView);
        adapter = new ChatAdapter(this, arrayList);
        adapter.chatActivity = chatActivity;
        mainListView.setAdapter(adapter);

        timer = new CountDownTimer(5000000, 5000) {
            @Override
            public void onTick(long l) {
                if (isFirstLaunch) {
                    if (progressDialog == null) {
                        progressDialog = SplachScreenActivity.utils.getProgressDialog(ChatActivity.this, "Ачааллаж байна");
                        progressDialog.show();
                    }
                    getBeforeZurvas();
                } else getBeforeZurvas();
            }

            @Override
            public void onFinish() {
                this.cancel();
            }
        };

        timer.start();

    }

    public void getBeforeZurvas() {

        String url = Utils.MAIN_HOST + "chat.php";

        RequestParams params = new RequestParams();
        params.put("from_user", SplachScreenActivity.userItem.getUserID());
        params.put("to_user", userItem.getUserID());

        System.out.println(TAG + " before param : " + params);

        Utils.client.post(url, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                System.out.println(TAG + " before response : " + response.toString());

                arrayList.clear();

                if (progressDialog.isShowing()) progressDialog.dismiss();

                for (int i = response.length() - 1; i >= 0; i--) {
                    ChatItem chatItem = null;
                    try {
                        chatItem = new ChatItem(response.getJSONObject(i));

                        if (chatItem.userID == SplachScreenActivity.userItem.getUserID()) {
                            chatItem.userName = SplachScreenActivity.userItem.getUserName();
                            chatItem.isMyMessage = false;
                        } else {
                            chatItem.userName = userItem.getUserName();
                            chatItem.isMyMessage = true;
                        }

                        arrayList.add((response.length() - 1) - i, chatItem);
                        if (i == (response.length() - 1)) firstMessageIndex = chatItem.chatID;
                        if (isFirstLaunch && i == 0) lastMessageIndex = chatItem.chatID;

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                mainListView.setSelection(arrayList.size() - 1);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);


                JSONArray bodyArray = null;
                try {
                    JSONObject serverResponse = response.getJSONObject("response");
                    bodyArray = serverResponse.getJSONArray("chat");

                    if (isFirstLaunch) {
                        mainListView.setSelection(arrayList.size() - 1);
                        isFirstLaunch = false;
                    } else {
                        final JSONArray finalBodyArray = bodyArray;
                        mainListView.post(new Runnable() {
                            @Override
                            public void run() {
                                mainListView.setSelection(finalBodyArray.length() - 1);
                            }
                        });
                    }

                    if (bodyArray.length() > 0) {
                        index = 0;

//                        ZurvasActivity.changeIsRead();

                        adapter.notifyDataSetChanged();

                        setRead();

                        //ZurvasActivity.changeIsRead();

                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                adapter.isLaunched = false;
                            }
                        }, 500);

                    } else {
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                isFirstGetting = false;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                isFirstGetting = false;
                //System.out.println(TAG + " last zurvas error response : " + errorResponse.toString());

//                nochatImageView.setImageResource(R.drawable.img_no_internet);
//                nochatImageView.setVisibility(View.VISIBLE);

                if (progressDialog.isShowing()) progressDialog.dismiss();
                if (isFirstLaunch) {
                    isFirstLaunch = false;
//                    if (arrayList.size() == 0){
//                        nochatImageView.setVisibility(View.VISIBLE);
//                    }else {
//                        nochatImageView.setVisibility(View.GONE);
//                    }
                    showToast("Интернет холболт байхгүй байна");
                }
            }
        });
    }

    private void getLastZurvas() {

        if (isLastGetting) return;
        else isLastGetting = true;

        final String deviceID = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        String url = Utils.MAIN_HOST + "/p_cons/list.json";

        RequestParams params = new RequestParams();
        params.put("child_id", childID);
        params.put("message_id", lastMessageIndex);
        params.put("last_flag", 1);

        System.out.println(TAG + " last param : " + params);

        Utils.client.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                System.out.println(TAG + " last zurvas response : " + response.toString());
                JSONArray bodyArray = null;
                try {
                    JSONObject serverResponse = response.getJSONObject("response");
                    bodyArray = serverResponse.getJSONArray("chat");

                    {
                        for (int i = bodyArray.length() - 1; i >= 0; i--) {
                            ChatItem chatItem = new ChatItem(bodyArray.getJSONObject(i));
                            arrayList.add(chatItem);
                            if (i == 0) lastMessageIndex = chatItem.chatID;
                        }
                    }
                    if (bodyArray.length() > 0) {
                        boolean isOtherMessage = false;

                        boolean isMymesageContain = false;

                        for (int i = 0; i < bodyArray.length(); i++) {
                            ChatItem chatItem = new ChatItem(bodyArray.getJSONObject(i));
                            if (!isOtherMessage)
                                if (!chatItem.isMyMessage) {
                                    showToast("Шинэ зурвас ирлээ");
                                    isOtherMessage = true;
                                }
                            if (chatItem.isMyMessage) isMymesageContain = true;
                        }
                        if (isMymesageContain) {
                            mainListView.post(new Runnable() {
                                public void run() {
                                    mainListView.setSelection(mainListView.getCount() - 1);
                                }
                            });
                        }
                        index = 1;

                        setRead();
//                        ZurvasActivity.chatRootItem.lastChat = arrayList.get(arrayList.size() - 1).body;
//                        ZurvasActivity.changeIsRead();

                        adapter.notifyDataSetChanged();
                        //    if (isOtherMessage)SoundManager.playSound(1, 1);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                isLastGetting = false;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                isLastGetting = false;
                showToast("Сервертэй холбогдож чадсангүй");
                //System.out.println(TAG + " last zurvas error response : " + errorResponse.toString());
            }
        });
    }

    public void setRead() {

        final String deviceID = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);

        String url = Utils.MAIN_HOST + "/p_cons/set_read.json";

        RequestParams params = new RequestParams();
        params.put("child_id", childID);
        params.put("last_message_id", lastMessageIndex);

        System.out.println(TAG + "set read param: " + params);

        Utils.client.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                System.out.println(TAG + "set read response: " + response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        timer.cancel();
        timer = null;
    }

//    public void playSound(){
//        SoundManager.getInstance();
//        SoundManager.initSounds(this);
//        SoundManager.loadSounds();
//    }

//    public void shineMessege(){
//        LayoutInflater inflater = getLayoutInflater();
//
//        View layout = inflater.inflate(R.layout.toast, (ViewGroup) findViewById(R.id.toast_layout_id));
//        TextView text = (TextView) layout.findViewById(R.id.text);
//        text.setText("Шинэ зурвас ирлээ");
//
//        Toast toast = new Toast(getApplicationContext());
//        toast.setGravity(Gravity.CENTER, 0, 0);
//        toast.setDuration(Toast.LENGTH_LONG);
//        toast.setView(layout);
//        toast.show();
//    }

    public void showToast(String title) {
        Toast.makeText(this, title, Toast.LENGTH_SHORT).show();
    }


}
