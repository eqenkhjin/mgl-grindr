package mn.eq.health4men.Album;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import mn.eq.health4men.Members.LibraryFragment;
import mn.eq.health4men.R;

/**
 * Created by Tamir on 11/24/2015.
 */
public class AlbumImageFragment extends Fragment {
    private static final String GOOD_ITEM_KEY = "goodItem";
    private static final String GOOD_ITEM_TYPE = "type";
    private View view;
    private String imageURL;
    private ImageView bigImageView;
    private TextView permission;
    public int type;
    private FrameLayout back;
    public LibraryFragment libraryFragment;

    public static AlbumImageFragment newInstance(String imageURL){
        AlbumImageFragment imageFragment = new AlbumImageFragment();
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
        view = inflater.inflate(R.layout.fragment_albumimage, container, false);
        view.setClickable(true);
        imageURL =  getArguments().getString(GOOD_ITEM_KEY);

        createInterface();

        return view;
    }

    public void createInterface(){
        bigImageView = (ImageView) view.findViewById(R.id.big_image);
        permission = (TextView) view.findViewById(R.id.permission);
        Picasso.with(getActivity()).load(imageURL).into(bigImageView);
        if(type == 1){
            permission.setText("public");
        } else {
            permission.setText("private");
        }
        permission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
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



}
