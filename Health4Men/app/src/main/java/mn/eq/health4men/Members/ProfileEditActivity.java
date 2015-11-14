package mn.eq.health4men.Members;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import mn.eq.health4men.R;


/**
 * Created by eQ on 11/14/15.
 */
public class ProfileEditActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);
        configHeader();
    }

    private void configHeader(){
        TextView toolbarTitle = (TextView) findViewById(R.id.toolbarTitle);
        toolbarTitle.setText("Chat");

        LinearLayout done = (LinearLayout) findViewById(R.id.done);
        toolbarTitle.setVisibility(View.VISIBLE);
    }

}
