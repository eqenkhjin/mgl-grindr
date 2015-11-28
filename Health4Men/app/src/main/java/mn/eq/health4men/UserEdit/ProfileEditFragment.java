package mn.eq.health4men.UserEdit;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import mn.eq.health4men.Objects.UserItem;
import mn.eq.health4men.R;
import mn.eq.health4men.Root.MainActivity;
import mn.eq.health4men.Root.RootActivity;
import mn.eq.health4men.Root.SplachScreenActivity;
import mn.eq.health4men.Utils.Utils;


/**
 * Created by eQ on 11/14/15.
 */
public class ProfileEditFragment extends Fragment {

    private View view;
    private UserItem userItem;
    private static final String GOOD_ITEM_KEY = "userItem";
    public MainActivity mainActivity;
    private FrameLayout ageFrame, heightFrame, weightFrame, ethnicityFrame, bodyTypeFrame, lookingForFrame;
    public ProfileEditFragment profileEditFragment;
    private TextView ageTextView, heightTextView, weightTextView, ethnicityTextView, bodyTypeTextView, lookingForTextView;
    public static EditText aboutUserEditText;
    public static EditText userNameEditText;
    public static ImageView userImageView;
    public Uri fileUri;
    public static final int MEDIA_TYPE_IMAGE = 1;
    private static final int RESULT_LOAD_IMAGE = 200;
    private static final String IMAGE_DIRECTORY_NAME = "health4men";
    public static final int MEDIA_TYPE_VIDEO = 2;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;

    public static ProfileEditFragment newInstance(UserItem userItem) {
        ProfileEditFragment deliveryFragment = new ProfileEditFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(GOOD_ITEM_KEY, userItem);
        deliveryFragment.setArguments(bundle);
        return deliveryFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_profile_edit, container, false);
        profileEditFragment = this;
        userItem = (UserItem) getArguments().getSerializable(GOOD_ITEM_KEY);

        createInterface();

        configViewFromObject();

        return view;
    }

    private void createInterface() {
        userNameEditText = (EditText) view.findViewById(R.id.userNameEditText);
        aboutUserEditText = (EditText) view.findViewById(R.id.aboutUserEditText);

        ageFrame = (FrameLayout) view.findViewById(R.id.ageFrame);
        ageFrame.setOnClickListener(onClick);

        heightFrame = (FrameLayout) view.findViewById(R.id.heightFrame);
        heightFrame.setOnClickListener(onClick);

        weightFrame = (FrameLayout) view.findViewById(R.id.weightFrame);
        weightFrame.setOnClickListener(onClick);

        ethnicityFrame = (FrameLayout) view.findViewById(R.id.ethnicityFrame);
        ethnicityFrame.setOnClickListener(onClick);

        bodyTypeFrame = (FrameLayout) view.findViewById(R.id.bodyTypeFrame);
        bodyTypeFrame.setOnClickListener(onClick);

        lookingForFrame = (FrameLayout) view.findViewById(R.id.lookingForFrame);
        lookingForFrame.setOnClickListener(onClick);

        ageTextView = (TextView) view.findViewById(R.id.ageTextView);
        heightTextView = (TextView) view.findViewById(R.id.heightTextView);
        weightTextView = (TextView) view.findViewById(R.id.weightTextView);
        ethnicityTextView = (TextView) view.findViewById(R.id.ethnicityTextView);
        bodyTypeTextView = (TextView) view.findViewById(R.id.bodyTypeTextView);
        lookingForTextView = (TextView) view.findViewById(R.id.lookingForTextView);

        userImageView = (ImageView) view.findViewById(R.id.userImageView);

        if (SplachScreenActivity.userItem.getUserImageURL().length() > 3){
            Picasso.with(getActivity()).load(SplachScreenActivity.userItem.getUserImageURL())
                    .placeholder(R.drawable.placholder_member).into(userImageView);
        }else {
            userImageView.setImageResource(R.drawable.placholder_member);
        }

        userImageView.setOnClickListener(new View.OnClickListener() {
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

    View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.ageFrame) mainActivity.showPopUp(1, profileEditFragment);
            if (v.getId() == R.id.heightFrame) mainActivity.showPopUp(2, profileEditFragment);
            if (v.getId() == R.id.weightFrame) mainActivity.showPopUp(3, profileEditFragment);
            if (v.getId() == R.id.ethnicityFrame) mainActivity.showPopUp(4, profileEditFragment);
            if (v.getId() == R.id.bodyTypeFrame) mainActivity.showPopUp(5, profileEditFragment);
            if (v.getId() == R.id.lookingForFrame) mainActivity.showPopUp(6, profileEditFragment);
        }
    };

    public void change(int type, String text) {

        if (type == 1) {
            ageTextView.setText(text);
            SplachScreenActivity.userItem.setUserAge(text);
        }
        if (type == 2) {
            heightTextView.setText(text);
            SplachScreenActivity.userItem.setUserHeight(text);
        }
        if (type == 3) {
            weightTextView.setText(text);
            SplachScreenActivity.userItem.setUserWeight(text);
        }
        if (type == 4) {
            ethnicityTextView.setText(text);
            SplachScreenActivity.userItem.setUserCountry(text);
        }
        if (type == 5) {
            bodyTypeTextView.setText(text);
            SplachScreenActivity.userItem.setUserBodyType(text);
        }
        if (type == 6) {
            lookingForTextView.setText(text);
            SplachScreenActivity.userItem.setUserLookingFor(text);
        }

    }

    private void configViewFromObject() {
        userNameEditText.setText(SplachScreenActivity.userItem.getUserName());
        aboutUserEditText.setText(SplachScreenActivity.userItem.getUserAboutme());
        ageTextView.setText(SplachScreenActivity.userItem.getUserAge());
        heightTextView.setText(SplachScreenActivity.userItem.getUserHeight());
        weightTextView.setText(SplachScreenActivity.userItem.getUserWeight());
        ethnicityTextView.setText(SplachScreenActivity.userItem.getUserCountry());
        bodyTypeTextView.setText(SplachScreenActivity.userItem.getUserBodyType());
        lookingForTextView.setText(SplachScreenActivity.userItem.getUserLookingFor());
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

                MainActivity.imageURL = f.getPath();

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
                        userImageView.setImageBitmap(Bitmap.createScaledBitmap(picture, (int) (90 * SplachScreenActivity.utils.density * ratio), (int) (90 * SplachScreenActivity.utils.density), false));

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

                MainActivity.imageURL = f.getPath();

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
                        userImageView.setImageBitmap(Bitmap.createScaledBitmap(picture, (int) (90 * SplachScreenActivity.utils.density * ratio), (int) (90 * SplachScreenActivity.utils.density), false));
                    }

                }
            }
        }

    }
}
