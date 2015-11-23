package mn.eq.health4men.Root;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import mn.eq.health4men.Login.LoginActivity;
import mn.eq.health4men.Members.NewUserDetailActivity;
import mn.eq.health4men.Objects.UserItem;
import mn.eq.health4men.R;
import mn.eq.health4men.Utils.Utils;


/**
 * Created by eQ on 11/3/15.
 */
public class SplachScreenActivity extends RootActivity {

    public static String userName;
    public static String password;
    public static Utils utils;
    public static UserItem userItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        utils = new Utils(this);

        setContentView(R.layout.activity_splash_screen);



        {
            LinearLayout logoImageView = (LinearLayout) findViewById(R.id.logo);
            ObjectAnimator anim = ObjectAnimator.ofFloat(logoImageView, "translationY", -60);
            anim.setDuration(1400);
            anim.start();
        }

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent intent = new Intent(SplachScreenActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }, 3000);

    }

}
