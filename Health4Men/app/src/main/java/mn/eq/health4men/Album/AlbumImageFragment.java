package mn.eq.health4men.Album;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import mn.eq.health4men.Members.LibraryFragment;
import mn.eq.health4men.Objects.UserImageItem;
import mn.eq.health4men.Objects.UserItem;
import mn.eq.health4men.R;
import mn.eq.health4men.Root.MainActivity;
import mn.eq.health4men.Root.SplachScreenActivity;
import mn.eq.health4men.Utils.Utils;

/**
 * Created by Tamir on 11/24/2015.
 */
public class AlbumImageFragment extends Fragment {
    private static final String GOOD_ITEM_KEY = "goodItem";
    private View view;
    private UserImageItem userImageItem;
    private ImageView bigImageView;
    private ImageView permission;
    private FrameLayout back;
    public LibraryFragment libraryFragment;
    private FrameLayout layout;
    private static String TAG = "ALBUM : ";
    private ProgressDialog progressDialog;

    public static AlbumImageFragment newInstance(UserImageItem userImageItem) {
        AlbumImageFragment imageFragment = new AlbumImageFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(GOOD_ITEM_KEY, userImageItem);
        imageFragment.setArguments(bundle);
        return imageFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog = new Utils().getProgressDialog(getActivity(), "Changing image permission");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_albumimage, container, false);
        view.setClickable(true);
        userImageItem = (UserImageItem) getArguments().getSerializable(GOOD_ITEM_KEY);

        createInterface();

        return view;
    }

    public void createInterface() {
        bigImageView = (ImageView) view.findViewById(R.id.big_image);
        permission = (ImageView) view.findViewById(R.id.permission);
        layout = (FrameLayout) view.findViewById(R.id.album_relative);
        Picasso.with(getActivity()).load(userImageItem.getImageURL()).into(bigImageView);

        if (userImageItem.getType() == 1) {
            permission.setImageResource(R.drawable.img_public);
        } else {
            permission.setImageResource(R.drawable.img_private);
        }

        permission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String description = "";

                if (userImageItem.getType() == 1) {
                    description = "This image will never shown another user";
                } else {
                    description = "This image will shown to all user";
                }

                showDialog(description);

            }
        });

        back = (FrameLayout) view.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                libraryFragment.hide();

            }
        });
    }

    private void showDialog(String description) {
        new AlertDialog.Builder(getActivity())
                .setTitle("Are you sure ?")
                .setMessage(description)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (userImageItem.getType() == 1){
                            changeImagePermission(0);
                        }else {
                            changeImagePermission(1);
                        }
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(R.drawable.logo_smal)
                .show();
    }

    private void changeImagePermission(final int permission) {

        if (SplachScreenActivity.utils.isNetworkConnected(getActivity())) {

            String url = Utils.MAIN_HOST + "album_update.php";

            RequestParams params = new RequestParams();
            params.put("album_id", userImageItem.getImageId());
            params.put("type", permission);

            System.out.println(TAG + "url : " + url);
            System.out.println(TAG + "param : " + params.toString());

            if (!progressDialog.isShowing()) progressDialog.show();

            Utils.client.post(url, params, new JsonHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);

                    if (progressDialog.isShowing()) progressDialog.dismiss();
                    System.out.println(TAG + "SUCCESS" + response.toString());

                    if (response.has("success")){
                        try {
                            if (response.getInt("success") == 1){
                                SplachScreenActivity.utils.showToast("Succesfully changed image permission");
                                userImageItem.setType(permission);
                                createInterface();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            SplachScreenActivity.utils.showToast(getString(R.string.server_error));
                        }
                    }else {
                        SplachScreenActivity.utils.showToast(getString(R.string.server_error));
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
            SplachScreenActivity.utils.showNoInternetAlert(getActivity());
        }

    }


}
