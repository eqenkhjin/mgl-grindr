package mn.eq.health4men.Album;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import mn.eq.health4men.Adapters.AdapterMembers;
import mn.eq.health4men.LeftMenu.FragmentDrawer;
import mn.eq.health4men.Members.LibraryFragment;
import mn.eq.health4men.Members.NewUserDetailActivity;
import mn.eq.health4men.Objects.UserImageItem;
import mn.eq.health4men.Objects.UserItem;
import mn.eq.health4men.R;
import mn.eq.health4men.Root.MainActivity;
import mn.eq.health4men.Root.SplachScreenActivity;
import mn.eq.health4men.Utils.AndroidMultiPartEntity;
import mn.eq.health4men.Utils.RecyclerItemClickListener;
import mn.eq.health4men.Utils.Utils;

/**
 * Created by Tamir on 11/23/2015.
 */
public class MyAlbum extends Fragment {

    private ProgressDialog progressDialog;
    private ProgressDialog deleteDialog;
    private View view;
    private RecyclerView recyclerView;
    private Button addPhoto;
    private Button removePhoto;
    private LinearLayoutManager mLayoutManager;
    private AdapterAlbum adapterAlbum;
    private static String TAG = "MyAlbum fragment : ";
    public static ArrayList<UserImageItem> arrayList = new ArrayList<>();
    private FragmentManager fragmentManager;
    private LibraryFragment libraryFragment;
    private UserItem userItem;
    public Uri fileUri;
    public static final int MEDIA_TYPE_IMAGE = 1;
    private static final int RESULT_LOAD_IMAGE = 200;
    private static final String IMAGE_DIRECTORY_NAME = "health4men";
    public static final int MEDIA_TYPE_VIDEO = 2;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private ProgressDialog editDialog;
    private String addImageURL;
    private boolean firstTimeSuccesfullyGet;
    public static boolean isPopUpShowed;
    private boolean isDeletable;
    public MyAlbum myAlbum;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_album, container, false);
        fragmentManager = getActivity().getSupportFragmentManager();
        progressDialog = new Utils().getProgressDialog(getActivity(), "Getting photos");
        editDialog = new Utils().getProgressDialog(getActivity(), "Uploading image");
        deleteDialog = new Utils().getProgressDialog(getActivity(), "Deleting images");
        myAlbum = this;
        getPhotos();

        createInterface();
        try {
            createJSON();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return view;
    }

    private void createInterface() {

        recyclerView = (RecyclerView) view.findViewById(R.id.recycleView);
        addPhoto = (Button) view.findViewById(R.id.addAlbum);

        removePhoto = (Button) view.findViewById(R.id.deleteAlbum);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycleView);
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());

        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        adapterAlbum = new AdapterAlbum(getActivity(), arrayList,myAlbum);
        recyclerView.setAdapter(adapterAlbum);
        SplachScreenActivity.userItem.setAlbum(arrayList);

        addPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(getActivity())
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

        removePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isDeletable){
                    isDeletable = true;

                    adapterAlbum.isDeletable = isDeletable;
                    adapterAlbum.notifyDataSetChanged();

                }else {

                    int deleteCount = 0 ;

                    for (UserImageItem userImageItem : arrayList){
                        if (userImageItem.isDeletable())deleteCount++;
                    }

                    if (deleteCount > 0){
                        showAlert(getActivity(),"Are you sure want to delete selected " +
                                ""+deleteCount+" photo");
                    }else {
                        isDeletable = false;

                        adapterAlbum.isDeletable = isDeletable;
                        adapterAlbum.notifyDataSetChanged();
                    }

                }
            }
        });

    }

    public void showLibrary(int position) {
        FragmentTransaction fragmentTransac = fragmentManager.beginTransaction();
        libraryFragment = LibraryFragment.newInstance();
        libraryFragment.userItem = SplachScreenActivity.userItem;
        libraryFragment.currentPosition = position;
        libraryFragment.b = false;
        fragmentTransac.add(R.id.album_frame, libraryFragment);
        fragmentTransac.commit();
        isPopUpShowed = true;
    }

    public void getPhotos() {

        if (SplachScreenActivity.utils.isNetworkConnected(getActivity())) {

            String url = Utils.MAIN_HOST + "album.php";

            RequestParams params = new RequestParams();
            params.put("user_id", SplachScreenActivity.userItem.getUserID());

            System.out.println(TAG + "url : " + url);
            System.out.println(TAG + "param : " + params.toString());

            if (!progressDialog.isShowing()) progressDialog.show();

            Utils.client.post(url, params, new JsonHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    super.onSuccess(statusCode, headers, response);
                    if (progressDialog.isShowing()) progressDialog.dismiss();

                    arrayList.clear();

                    for (int i = 0; i < response.length(); i++) {

                        try {
                            UserImageItem b = new UserImageItem(response.getJSONObject(i));
                            arrayList.add(b);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                    adapterAlbum.notifyDataSetChanged();

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    if (progressDialog.isShowing()) progressDialog.dismiss();
                    SplachScreenActivity.utils.showToast("Сервертэй холбогдоход алдаа гарлаа");
                }

            });

        } else {
            SplachScreenActivity.utils.showNoInternetAlert(getActivity());
        }

    }

    private void createJSON() throws JSONException {

        JSONObject object = new JSONObject();

        object.put("id", 30);
        JSONArray array = new JSONArray();

        array.put(20);
        array.put(10);
        object.put("delete", array);

        System.out.println(object.toString());

    }

    private void uploadImage() {
        if (SplachScreenActivity.utils.isNetworkConnected(getActivity())) {

            if (!editDialog.isShowing()) editDialog.show();

            new UploadFileToServer().execute();
            return;

        } else {
            SplachScreenActivity.utils.showNoInternetAlert(getActivity());
        }
    }

    private boolean isDeviceSupportCamera() {
        if (getActivity().getApplicationContext().getPackageManager().hasSystemFeature(
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

                Cursor cursor = getActivity().getContentResolver().query(fileUri,
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

                addImageURL = f.getPath();

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
                        float ratio = (float) picture.getWidth() / (float) picture.getHeight();
//                        userImageView.setImageBitmap(Bitmap.createScaledBitmap(picture, (int) (90 * SplachScreenActivity.utils.density * ratio), (int) (90 * SplachScreenActivity.utils.density), false));
                        uploadImage();
                    }
                }
            }
        }

        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {

            if (resultCode == -1) {
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

                addImageURL = f.getPath();

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
                        float ratio = (float) picture.getWidth() / (float) picture.getHeight();
//                        userImageView.setImageBitmap(Bitmap.createScaledBitmap(picture, (int) (90 * SplachScreenActivity.utils.density * ratio), (int) (90 * SplachScreenActivity.utils.density), false));
                        uploadImage();
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
            HttpPost httppost = new HttpPost(Utils.MAIN_HOST + "add_album.php");

            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {

                            @Override
                            public void transferred(long num) {
                            }
                        });

                if (addImageURL != null) {

                    File sourceFile = new File(addImageURL);
                    entity.addPart("picture", new FileBody(sourceFile));
                }


                entity.addPart("user_id", new StringBody(SplachScreenActivity.userItem
                        .getUserID() + ""));

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
                System.out.println("1 : " + e.getLocalizedMessage());
            } catch (IOException e) {
                responseString = e.toString();
                System.out.println("2 : " + e.getLocalizedMessage());

            }

            return responseString;

        }

        @Override
        protected void onPostExecute(String result) {

            JSONObject response = null;
            try {
                response = new JSONObject(result);


                if (response.getInt("success") == 1) {

                    System.out.println(result);

                    getPhotos();

                    if (editDialog.isShowing()) editDialog.dismiss();
                    SplachScreenActivity.utils.showAlert(getActivity(),
                            "Information succesfully sent");

                } else {
                    SplachScreenActivity.utils.showAlert(getActivity(), getString(R.string
                            .server_error));

                    if (editDialog.isShowing()) editDialog.dismiss();
                }

            } catch (JSONException e) {
                e.printStackTrace();
                SplachScreenActivity.utils.showAlert(getActivity(), getString(R.string
                        .server_error));

                if (editDialog.isShowing()) editDialog.dismiss();
            }


            super.onPostExecute(result);
        }

    }

    public void hide(){
        getActivity().getSupportFragmentManager().beginTransaction().remove(libraryFragment)
                .commit();
        isPopUpShowed = false;
    }

    public void showAlert(Context context,String text){
        new AlertDialog.Builder(context)
                .setTitle(context.getString(R.string.app_name_mn))
                .setMessage(text)
                .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            deletePhotos();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                })
                .setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setIcon(R.drawable.alert_logo)
                .show();
    }

    public void deletePhotos() throws JSONException {
        if (SplachScreenActivity.utils.isNetworkConnected(getActivity())) {

            JSONArray array = new JSONArray();
            JSONObject object = new JSONObject();
            object.put("id",SplachScreenActivity.userItem.getUserID());
            JSONArray deleteArray = new JSONArray();

            for (UserImageItem userImageItem: arrayList){

                if (userImageItem.isDeletable()){
                    deleteArray.put(userImageItem.getImageId());
                }

            }
            object.put("delete",deleteArray);
            array.put(object);


            String url = Utils.MAIN_HOST + "album_delete.php";

            RequestParams params = new RequestParams();
            params.put("json", array.toString());

            System.out.println(TAG + "url : " + url);
            System.out.println(TAG + "param : " + params.toString());

            if (!deleteDialog.isShowing()) deleteDialog.show();

            Utils.client.post(url, params, new JsonHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);

                    if (deleteDialog.isShowing()) deleteDialog.dismiss();
                    System.out.println(TAG + "LOGIN SUCCESS" + response.toString());

                    if (response.has("success")) {
                        try {
                            if (response.getInt("success") == 1){
                                SplachScreenActivity.utils.showAlert(getActivity(),"Succesfully " +
                                        "deleted");
                                getPhotos();
                            }else {
                                SplachScreenActivity.utils.showAlert(getActivity(),"Can't delete " +
                                        "images. Please try again later");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            SplachScreenActivity.utils.showAlert(getActivity(), "Can't delete " +
                                    "images. Please try again later");
                        }
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    if (deleteDialog.isShowing()) deleteDialog.dismiss();
                    SplachScreenActivity.utils.showToast(getString(R.string.server_error));
                }
            });

        } else {
            SplachScreenActivity.utils.showNoInternetAlert(getActivity());
        }
    }

}
