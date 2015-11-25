package mn.eq.health4men.UserEdit;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import mn.eq.health4men.Adapters.AdapterList;
import mn.eq.health4men.Adapters.AdapterMembers;
import mn.eq.health4men.R;
import mn.eq.health4men.Utils.RecyclerItemClickListener;


/**
 * Created by eQ on 10/29/15.
 */
public class PopUpFragment extends Fragment {
    private static final String GOOD_ITEM_KEY = "type";

    private View view;
    private TextView titleTextView;
    private TextView paymentDescriptionTextView;
    private TextView loanTextView;
    private RecyclerView recyclerView;
    LinearLayoutManager mLayoutManager;
    private AdapterList adapterList;
    private ArrayList<String> arrayList = new ArrayList<>();
    public ProfileEditFragment profileEditFragment;
    public int popUpType;

    public static PopUpFragment newInstance(int type){
        PopUpFragment popUpFragment = new PopUpFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(GOOD_ITEM_KEY, type);
        popUpFragment.setArguments(bundle);
        return popUpFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_payment_condition, container, false);
        view.setClickable(true);
        popUpType = getArguments().getInt(GOOD_ITEM_KEY);

        createInterface();

        return view;
    }

    public void createInterface(){

        popupateArrayList();

        {
            titleTextView = (TextView) view.findViewById(R.id.title);
            titleTextView.setText(getTitle());
        }

        {
            ImageButton closeButton = (ImageButton) view.findViewById(R.id.deliveryCloseButton);
            closeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    close();
                }
            });
        }

        recyclerView = (RecyclerView)view.findViewById(R.id.recycleView);
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        adapterList = new AdapterList(getActivity(), arrayList);
        recyclerView.setAdapter(adapterList);

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        profileEditFragment.change(popUpType, arrayList.get(position));
                        close();
                    }
                })
        );

    }

    private String getTitle(){
        String title = "";
        if (popUpType == 1)title = "Age";
        return title;
    }

    private void popupateArrayList(){
        arrayList.clear();

        if (popUpType == 1){
            for (int i = 21; i < 60 ; i ++){
                arrayList.add(i+"");
            }
        }
        if (popUpType == 2){
            for (int i = 100 ; i < 210 ; i ++){
                arrayList.add(i+" cm");
            }
        }
        if (popUpType == 3){
            for (int i = 30 ; i < 100 ; i ++){
                arrayList.add(i+" kg");
            }
        }
        if (popUpType == 4){
            arrayList.add("UB");
            arrayList.add("Arkhangai");
            arrayList.add("Bayan-Ulgii");
            arrayList.add("Bayan-Khongor");
            arrayList.add("Bulgan");
            arrayList.add("Govi-Altai");
            arrayList.add("Govi-Sumber");
            arrayList.add("Darhan-Uul");
            arrayList.add("Dorno Gobi");
            arrayList.add("Dornod");
            arrayList.add("Dund-Govi");
            arrayList.add("Zavhan");
            arrayList.add("Orhon");
            arrayList.add("Ovor Hangai");
            arrayList.add("Omno Gobi");
            arrayList.add("Sukhbaatar");
            arrayList.add("Selenge");
            arrayList.add("Tuv");
            arrayList.add("Uvs");
            arrayList.add("Hovd");
            arrayList.add("Hovsgol");
            arrayList.add("Hentii");
        }
        if (popUpType == 5){
            arrayList.add("Top");
            arrayList.add("Bottom");
            arrayList.add("Versality");
        }
        if (popUpType == 6){
            arrayList.add("Sex");
            arrayList.add("Chat");
            arrayList.add("Make Friends");
        }
    }

    private void close(){
        getActivity().getSupportFragmentManager().beginTransaction().remove(PopUpFragment.this).commit();
    }
}
