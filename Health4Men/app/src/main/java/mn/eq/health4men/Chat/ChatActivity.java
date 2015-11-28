package mn.eq.health4men.Chat;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Config;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import org.apache.http.HttpEntity;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
//import org.json.JSONObject;

//import org.apache.http.client.ClientProtocolException;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.entity.mime.content.FileBody;
//import org.apache.http.entity.mime.content.StringBody;
//import org.apache.http.impl.client.DefaultHttpClient;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import mn.eq.health4men.Adapters.ChatAdapter;
import mn.eq.health4men.Objects.ChatItem;
import mn.eq.health4men.Objects.UserItem;
import mn.eq.health4men.R;
import mn.eq.health4men.Root.SplachScreenActivity;
import mn.eq.health4men.Utils.AndroidMultiPartEntity;
import mn.eq.health4men.Utils.Utils;


/**
 * Created by xashaa on 8/20/15.
 */
@SuppressLint("ValidFragment")
public class ChatActivity extends FragmentActivity {

    public ListView mainListView;
    public FrameLayout zurvasFooterLayout;
    public View view;
    public ChatAdapter adapter;
    public ArrayList<ChatItem> arrayList = new ArrayList<ChatItem>();
    public ImageView send;
    public EditText message;
    private boolean isFirstLaunch = true;
    public static ChatActivity chatActivity;
    public static String TAG = "Chat activity : ";
    CountDownTimer timer;
    private ProgressDialog progressDialog;
    private UserItem userItem;
    public Uri fileUri;
    public static final int MEDIA_TYPE_IMAGE = 1;
    private static final int RESULT_LOAD_IMAGE = 200;
    private static final String IMAGE_DIRECTORY_NAME = "health4men";
    public static final int MEDIA_TYPE_VIDEO = 2;
    private ImageView addImageView;
    private String imageURL;
    public static boolean isPopupShowed = false;
    private ImageFragment imageFragment;
    private FragmentManager fragmentManager;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;

    public ChatActivity() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        chatActivity = this;

        fragmentManager = getSupportFragmentManager();

        Bundle bundle = getIntent().getExtras();

        userItem = (UserItem) bundle.getSerializable("chat_user");
        configHeader();

        init();

        send = (ImageView) findViewById(R.id.zurvasMessageSend);

        message = (EditText) findViewById(R.id.zurvasTextView);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });
    }

    private void sendMessage() {

        if (SplachScreenActivity.utils.isNetworkConnected(this)) {

            new UploadFileToServer().execute();
            return;

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
        headerTitleTextView.setText(userItem.getUserName()+", "+userItem.getUserAge());
    }

    public void init() {
        mainListView = (ListView) findViewById(R.id.zurvasListView);
        adapter = new ChatAdapter(this, arrayList);
//        adapter.chatActivity = chatActivity;
        mainListView.setAdapter(adapter);

        addImageView = (ImageView) findViewById(R.id.addImageView);

        addImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(ChatActivity.this)
                        .setTitle(getString(R.string.app_name))
                        .setMessage("Choose image type")
                        .setNegativeButton("Camera", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if (isDeviceSupportCamera()) {
                                    captureImage();
                                }
                            }
                        })
                        .setPositiveButton("Library", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent inte = new Intent(
                                        Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
                                startActivityForResult(inte, RESULT_LOAD_IMAGE);
                            }
                        })
                        .show();


            }
        });

        timer = new CountDownTimer(2500000, 2500) {
            @Override
            public void onTick(long l) {
                if (isFirstLaunch) {
                    if (progressDialog == null) {
                        progressDialog = SplachScreenActivity.utils.getProgressDialog
                                (ChatActivity.this, "Loading");
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

    private boolean isDeviceSupportCamera() {
        if (this.getApplicationContext().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {
            return true;
        } else {
            return false;
        }
    }

    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
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

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                if (isFirstLaunch){

                    try {
                        mainListView.setSelection(arrayList.size() - 1);
                    }catch (Exception e){

                    }

                    isFirstLaunch = false;
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);

                if (progressDialog.isShowing()) progressDialog.dismiss();
                if (isFirstLaunch) {
                    isFirstLaunch = false;
                    showToast(getString(R.string.no_internet));
                }

            }

        });
    }

    @Override
    public void onBackPressed() {

        if (isPopupShowed){
            getSupportFragmentManager().beginTransaction().remove(imageFragment).commit();
            isPopupShowed = false;
        }else {
            finish();
            timer.cancel();
            timer = null;
        }

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


    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    private static File getOutputMediaFile(int type) {

        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIRECTORY_NAME);

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
                        + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }
        return mediaFile;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == RESULT_LOAD_IMAGE) {
            if (resultCode == -1) {

                fileUri = data.getData();

                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = this.getContentResolver().query(fileUri,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();
                Bitmap photo = BitmapFactory.decodeFile(picturePath);

                float imageWidth = (float) photo.getWidth();
                float imageHeight = (float) photo.getHeight();

                if (imageWidth > 1000 || imageHeight > 1000) {
                    if (imageWidth > 1000) {
                        photo = Bitmap.createScaledBitmap(photo, 1000, (int) ((1000 * imageHeight) / imageWidth), false);
                    } else {
                        photo = Bitmap.createScaledBitmap(photo, (int) ((1000 * imageWidth) / imageHeight), 1000, false);
                    }
                }

                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.JPEG, 80, bytes);
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                        Locale.getDefault()).format(new Date());
                File f = new File(Environment.getExternalStorageDirectory()
                        + File.separator + timeStamp + "_homework.jpg");

                imageURL = f.getPath();

                try {
                    f.createNewFile();
                    FileOutputStream fo = new FileOutputStream(f);
                    fo.write(bytes.toByteArray());
                    fo.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }


                Bitmap picture = BitmapFactory.decodeFile(f.getPath());
                if (picture != null) {
                    if (picture.getWidth() > 0 && picture.getHeight() > 0) {
                        float ratio = (float)picture.getWidth()/(float)picture.getHeight();
                        addImageView.setImageBitmap(picture);
                    }

                }
            }
        }

        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {

            if (resultCode == RESULT_OK) {
                Bitmap photo = BitmapFactory.decodeFile(fileUri.getPath());

                float imageWidth = (float) photo.getWidth();
                float imageHeight = (float) photo.getHeight();

                if (imageWidth > 1000 || imageHeight > 1000) {
                    if (imageWidth > 1000) {
                        photo = Bitmap.createScaledBitmap(photo, 1000, (int) ((1000 * imageHeight) / imageWidth), false);
                    } else {
                        photo = Bitmap.createScaledBitmap(photo, (int) ((1000 * imageWidth) / imageHeight), 1000, false);
                    }
                }

                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.JPEG, 80, bytes);
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                        Locale.getDefault()).format(new Date());
                File f = new File(Environment.getExternalStorageDirectory()
                        + File.separator + timeStamp + "_homework.jpg");

                imageURL = f.getPath();

                try {
                    f.createNewFile();
                    FileOutputStream fo = new FileOutputStream(f);
                    fo.write(bytes.toByteArray());
                    fo.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }


                Bitmap picture = BitmapFactory.decodeFile(f.getPath());
                if (picture != null) {
                    if (picture.getWidth() > 0 && picture.getHeight() > 0) {
                        float ratio = (float)picture.getWidth()/(float)picture.getHeight();
                        addImageView.setImageBitmap(picture);
                    }

                }
            }
        }

    }

    private class UploadFileToServer extends AsyncTask<Void, Integer, String> {
        @Override
        protected void onPreExecute() {
            // setting progress bar to zero
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
        }

        @Override
        protected String doInBackground(Void... params) {
            return uploadFile();
        }

        @SuppressWarnings("deprecation")
        private String uploadFile() {
            String responseString = null;

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(Utils.MAIN_HOST + "chat_add.php");

            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {

                            @Override
                            public void transferred(long num) {
                            }
                        });

                if (imageURL != null){

                    File sourceFile = new File(imageURL);
                    entity.addPart("picture", new FileBody(sourceFile));
                }

                entity.addPart("from_user", new StringBody(SplachScreenActivity.userItem
                        .getUserID()+""));
                entity.addPart("to_user", new StringBody(userItem.getUserID()+""));
                entity.addPart("text", new StringBody(message.getText().toString()));


                httppost.setEntity(entity);

                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();

                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    responseString = EntityUtils.toString(r_entity);
                } else {
                    responseString = "Error occurred! Http Status Code: "
                            + statusCode;
                }

            } catch (ClientProtocolException e) {
                responseString = e.toString();
            } catch (IOException e) {
                responseString = e.toString();
            }

            return responseString;

        }

        @Override
        protected void onPostExecute(String result) {

            System.out.println("WEWE : "+result);

            JSONObject response = null;
            try {
                response = new JSONObject(result);

                if (response.getInt("success") == 1) {
                    message.setText("");
                    addImageView.setImageResource(R.drawable.image);
                    imageURL = null;
                } else {
                    SplachScreenActivity.utils.showToast("Сервертэй холбогдоход алдаа гарлаа");
                }

            } catch (JSONException e) {
                e.printStackTrace();
                SplachScreenActivity.utils.showToast("Сервертэй холбогдоход алдаа гарлаа");

            }


            super.onPostExecute(result);
        }

    }

    public void showBigImage(String imageURL){
        FragmentTransaction fragmentTransac = fragmentManager.beginTransaction();
        imageFragment = ImageFragment.newInstance(imageURL);
        fragmentTransac.add(R.id.container, imageFragment);
        fragmentTransac.commit();
        isPopupShowed = true;

    }



}
