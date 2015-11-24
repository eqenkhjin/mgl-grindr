package mn.eq.health4men.Album;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import mn.eq.health4men.Adapters.AdapterMembers;
import mn.eq.health4men.Members.LibraryFragment;
import mn.eq.health4men.Members.NewUserDetailActivity;
import mn.eq.health4men.Objects.UserImageItem;
import mn.eq.health4men.Objects.UserItem;
import mn.eq.health4men.R;
import mn.eq.health4men.Root.MainActivity;
import mn.eq.health4men.Root.SplachScreenActivity;
import mn.eq.health4men.Utils.RecyclerItemClickListener;
import mn.eq.health4men.Utils.Utils;

/**
 * Created by Tamir on 11/23/2015.
 */
public class MyAlbum extends Fragment {

    private ProgressDialog progressDialog;
    private View view;
    private RecyclerView recyclerView;
    private ImageButton addPhoto;
    private ImageButton removePhoto;
    private LinearLayoutManager mLayoutManager;
    private AdapterAlbum adapterAlbum;
    private static String TAG = "MyAlbum fragment : ";
    private  ArrayList<UserImageItem> arrayList = new ArrayList<>();
    private FragmentManager fragmentManager;
    private LibraryFragment libraryFragment;
    private UserItem userItem;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_album, container, false);
        fragmentManager = getActivity().getSupportFragmentManager();
        progressDialog = new Utils().getProgressDialog(getActivity(), "Getting photos");
        getPhotos();

        createInterface();

        return view;
    }
    private void createInterface(){

        recyclerView = (RecyclerView) view.findViewById(R.id.recycleView);
        addPhoto = (ImageButton) view.findViewById(R.id.addAlbum);
        removePhoto = (ImageButton) view.findViewById(R.id.deleteAlbum);
        recyclerView = (RecyclerView)view.findViewById(R.id.recycleView);
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());

        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        adapterAlbum = new AdapterAlbum(getActivity(), arrayList);
        recyclerView.setAdapter(adapterAlbum);
        SplachScreenActivity.userItem.setAlbum(arrayList);
//        userItem.setAlbum(SplachScreenActivity.userItem.getAlbum());

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                            showLibrary(position);
//                        UserItem userItem = arrayList.get(position);
//
//                        final Intent intent = new Intent(getActivity(), NewUserDetailActivity.class);
//
//                        Bundle bundle = new Bundle();
//                        bundle.putSerializable("detail", userItem);
//                        intent.putExtras(bundle);
//
//                        handler.postDelayed(new Runnable() {
//                            public void run() {
//                                getActivity().startActivity(intent);
//                            }
//                        }, 300);
//
//                        Animation animFadeIn = AnimationUtils.loadAnimation(getContext(),
//                                R.anim.selector);
//                        view.startAnimation(animFadeIn);

                    }
                })


        );

        addPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        removePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
    public void showLibrary(int position) {
        FragmentTransaction fragmentTransac = fragmentManager.beginTransaction();
        libraryFragment = LibraryFragment.newInstance();
        libraryFragment.userItem = SplachScreenActivity.userItem;
        libraryFragment.currentPosition = position;
        fragmentTransac.add(R.id.album_frame, libraryFragment);
        fragmentTransac.commit();
//        popUpShowed = true;
    }
    public void getPhotos() {

//        if (!canContinue)return;

        if (SplachScreenActivity.utils.isNetworkConnected(getActivity())) {

            String url = Utils.MAIN_HOST + "album.php";

            RequestParams params = new RequestParams();
            params.put("user_id",SplachScreenActivity.userItem.getUserID());


            System.out.println(TAG + "url : " + url);
            System.out.println(TAG + "param : " + params.toString());

            if (!progressDialog.isShowing()) progressDialog.show();

            Utils.client.post(url, params, new JsonHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    super.onSuccess(statusCode, headers, response);
                    if (progressDialog.isShowing()) progressDialog.dismiss();
                    System.out.println(TAG + "LOGIN SUCCESS" + response.toString());
                    arrayList.clear();

                    for (int i = 0 ; i < response.length() ; i ++){

                        try {
                            UserImageItem b =new UserImageItem(response.getJSONObject(i));
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



}
