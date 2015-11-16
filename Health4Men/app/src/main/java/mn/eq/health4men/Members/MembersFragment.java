package mn.eq.health4men.Members;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.logging.LogRecord;

import mn.eq.health4men.Adapters.AdapterMembers;
import mn.eq.health4men.Objects.UserItem;
import mn.eq.health4men.R;
import mn.eq.health4men.Root.SplachScreenActivity;
import mn.eq.health4men.Utils.RecyclerItemClickListener;
import mn.eq.health4men.Utils.Utils;

/**
 * Created by eQ on 11/14/15.
 */
public class MembersFragment extends Fragment {

    private View view;
    private RecyclerView recyclerView;
    private LinearLayoutManager mLayoutManager;
    private AdapterMembers adapterMembers;
    public int pageIndex = 1;
    private int perPage = 50;
    private static String TAG = "Members Fragment : ";
    private ProgressDialog progressDialog;
    public static ArrayList<UserItem> arrayList = new ArrayList<>();
    public MembersFragment membersFragment;
    public boolean isWaitResponse;
    public boolean canContinue = true;
    private Handler handler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog = new Utils().getProgressDialog(getActivity(), "Getting Members");
        membersFragment = this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_member,container,false);

        handler = new Handler();

        createInterface();

        getMembers();

        return view;
    }

    private void createInterface(){
        recyclerView = (RecyclerView)view.findViewById(R.id.recycleView);
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        adapterMembers = new AdapterMembers(getActivity(), arrayList);
        adapterMembers.membersFragment = membersFragment;
        recyclerView.setAdapter(adapterMembers);

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        UserItem userItem = arrayList.get(position);

                        final Intent intent = new Intent(getActivity(), UserDetailActivity.class);

                        Bundle bundle = new Bundle();
                        bundle.putSerializable("detail", userItem);
                        intent.putExtras(bundle);

                        handler.postDelayed(new Runnable() {
                            public void run() {
                                getActivity().startActivity(intent);
                            }
                        }, 500);

                        Animation animFadeIn = AnimationUtils.loadAnimation(getContext(),
                                R.anim.selector);
                        view.startAnimation(animFadeIn);

                    }
                })
        );

    }

    public void getMembers() {

        if (!canContinue)return;

        if (SplachScreenActivity.utils.isNetworkConnected(getActivity())) {

            String url = Utils.MAIN_HOST + "user_list.php";

            RequestParams params = new RequestParams();
            params.put("user_id",SplachScreenActivity.userItem.getUserID());
            params.put("page", pageIndex);
            params.put("per_page", perPage);

            System.out.println(TAG + "url : " + url);
            System.out.println(TAG + "param : " + params.toString());

            if (!progressDialog.isShowing()) progressDialog.show();

            Utils.client.post(url, params, new JsonHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    super.onSuccess(statusCode, headers, response);
                    if (progressDialog.isShowing()) progressDialog.dismiss();
                    System.out.println(TAG + "LOGIN SUCCESS" + response.toString());

                    for (int i = 0 ; i < response.length() ; i ++){

                        try {
                            arrayList.add(new UserItem(response.getJSONObject(i)));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    if (response.length() < perPage)canContinue = false;

                    adapterMembers.notifyDataSetChanged();

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
