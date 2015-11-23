package mn.eq.health4men.Members;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import mn.eq.health4men.Adapters.LibraryAdapter;
import mn.eq.health4men.Objects.UserItem;
import mn.eq.health4men.R;

/**
 * Created by eQ on 11/23/15.
 */
public class LibraryFragment extends Fragment {

    private View view;
    public static ViewPager viewPager;
    public UserItem userItem;
    public int currentPosition;
    private LibraryAdapter libraryAdapter;
    private Context context;
    private Fragment f;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_library,container,false);
        context = getContext();
        f = this;
        createInterface();

        return view;
    }

    private void createInterface(){
        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        libraryAdapter = new LibraryAdapter(getActivity().getSupportFragmentManager(),userItem
                .getAlbum());
        viewPager.setAdapter(libraryAdapter);
        viewPager.setCurrentItem(currentPosition);
    }

//    public void onBackPressed() {
////        if (popUpShowed) {
////            fragmentTransac.remove(libraryFragment).commit();
////            popUpShowed = false;
////        } else {
////            finish();
////        }
//    }
}
