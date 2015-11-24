package mn.eq.health4men.Album;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;

import java.util.ArrayList;

import mn.eq.health4men.Adapters.AdapterMembers;
import mn.eq.health4men.Members.NewUserDetailActivity;
import mn.eq.health4men.Objects.UserImageItem;
import mn.eq.health4men.Objects.UserItem;
import mn.eq.health4men.R;
import mn.eq.health4men.Root.MainActivity;
import mn.eq.health4men.Utils.RecyclerItemClickListener;
import mn.eq.health4men.Utils.Utils;

/**
 * Created by Tamir on 11/23/2015.
 */
public class MyAlbum extends Fragment {
    private View view;
    private RecyclerView recyclerView;
    private ImageButton addPhoto;
    private ImageButton removePhoto;
    private LinearLayoutManager mLayoutManager;
    private AdapterAlbum adapterAlbum;
    public static ArrayList<UserImageItem> arrayList = new ArrayList<>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_album,container,false);
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
//        adapterAlbum = new AdapterMembers(getContext(), arrayList);
        recyclerView.setAdapter(adapterAlbum);

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

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

}
