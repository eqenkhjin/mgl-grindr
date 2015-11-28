package mn.eq.health4men.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

import mn.eq.health4men.Album.AlbumImageFragment;
import mn.eq.health4men.Chat.ImageFragment;
import mn.eq.health4men.Members.LibraryFragment;
import mn.eq.health4men.Objects.UserImageItem;

/**
 * Created by eQ on 11/23/15.
 */
public class LibraryAdapter extends FragmentStatePagerAdapter {

    ArrayList<UserImageItem> arrayList;
    public LibraryFragment libraryFragment;
    private boolean b;

    public LibraryAdapter(FragmentManager fragmentManager,ArrayList<UserImageItem> arrayList,
                          LibraryFragment libraryFragment,boolean b){
        super(fragmentManager);
        this.arrayList = arrayList;
        this.libraryFragment = libraryFragment;
        this.b = b;
    }



    @Override
    public Fragment getItem(int position) {

        if(b){
            UserImageItem userImageItem = arrayList.get(position);
            ImageFragment imageFragment = ImageFragment.newInstance(userImageItem.getImageURL());
            imageFragment.libraryFragment = libraryFragment;
            return imageFragment;
        } else {
            UserImageItem userImageItem = arrayList.get(position);
            AlbumImageFragment imageFragment = AlbumImageFragment.newInstance(arrayList.get(position));
            imageFragment.libraryFragment = libraryFragment;
            return imageFragment;
        }
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }
}
