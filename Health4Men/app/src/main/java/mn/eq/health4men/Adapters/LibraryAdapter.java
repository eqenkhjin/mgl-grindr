package mn.eq.health4men.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

import mn.eq.health4men.Chat.ImageFragment;
import mn.eq.health4men.Objects.UserImageItem;

/**
 * Created by eQ on 11/23/15.
 */
public class LibraryAdapter extends FragmentStatePagerAdapter {

    ArrayList<UserImageItem> arrayList;

    public LibraryAdapter(FragmentManager fragmentManager,ArrayList<UserImageItem> arrayList){
        super(fragmentManager);
        this.arrayList = arrayList;
    }

    @Override
    public Fragment getItem(int position) {

        UserImageItem userImageItem = arrayList.get(position);
        ImageFragment imageFragment = ImageFragment.newInstance(userImageItem.getImageURL());

        return imageFragment;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }
}
