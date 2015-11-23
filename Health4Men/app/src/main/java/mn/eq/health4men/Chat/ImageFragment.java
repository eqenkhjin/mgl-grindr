package mn.eq.health4men.Chat;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import mn.eq.health4men.R;


/**
 * Created by eQ on 10/29/15.
 */
public class ImageFragment extends Fragment {
    private static final String GOOD_ITEM_KEY = "goodItem";

    private View view;
    private String imageURL;
    private ImageView bigImageView;
    public int type;
    private FrameLayout back;

    public static ImageFragment newInstance(String imageURL){
        ImageFragment imageFragment = new ImageFragment();
        Bundle bundle = new Bundle();
        bundle.putString(GOOD_ITEM_KEY, imageURL);
        imageFragment.setArguments(bundle);
        return imageFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_imageview, container, false);
        view.setClickable(true);
        imageURL =  getArguments().getString(GOOD_ITEM_KEY);

        createInterface();

        return view;
    }

    public void createInterface(){
        bigImageView = (ImageView) view.findViewById(R.id.big_image);
        Picasso.with(getActivity()).load(imageURL).into(bigImageView);

        back = (FrameLayout) view.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().remove(ImageFragment.this)
                        .commit();
                ChatActivity.isPopupShowed = false;

            }
        });
    }



}
