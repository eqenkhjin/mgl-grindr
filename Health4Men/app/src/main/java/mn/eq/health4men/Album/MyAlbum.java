package mn.eq.health4men.Album;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import mn.eq.health4men.R;
import mn.eq.health4men.Utils.Utils;

/**
 * Created by Tamir on 11/23/2015.
 */
public class MyAlbum extends Fragment {
    private View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_member,container,false);


        return view;
    }

}
