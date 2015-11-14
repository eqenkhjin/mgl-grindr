package mn.eq.health4men.Root;

import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import java.sql.SQLException;
import java.util.ArrayList;

import mn.eq.health4men.LeftMenu.FragmentDrawer;
import mn.eq.health4men.Members.MembersFragment;
import mn.eq.health4men.R;
import mn.eq.health4men.Root.RootActivity;

public class MainActivity extends RootActivity implements FragmentDrawer.FragmentDrawerListener {

    private FragmentDrawer drawerFragment;
    private DrawerLayout drawerLayout;
    private int lastSelectedIndex = -1;
    private FragmentManager fragmentManager = getSupportFragmentManager();
    private TextView toolbarTitle;
    private FrameLayout toolbar;
    private ImageButton menuIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createInterface();
    }

    private void createInterface(){

        toolbar = (FrameLayout) findViewById(R.id.toolbar);

        drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), toolbar);
        drawerFragment.setDrawerListener(this);

        toolbarTitle = (TextView) findViewById(R.id.toolbarTitle);

        menuIcon = (ImageButton) findViewById(R.id.menuIcon);
        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });

        displayView(0);
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
        displayView(position);
    }

    public void displayView(int position) {
        Fragment fragment = null;

        String title = getString(R.string.app_name);

        if (position == lastSelectedIndex) return;
        if (position != 6)lastSelectedIndex = position;

        if (position == 0) {
            title = "Members";

            fragment = new MembersFragment();

            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container, fragment);
            fragmentTransaction.commit();
        }

        if (position == 1) {
            title = "Map";

            fragment = new MembersFragment();

            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container, fragment);
            fragmentTransaction.commit();
        }

        if (position == 2) {

        }

        toolbarTitle.setText(title);

    }

}
