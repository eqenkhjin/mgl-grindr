package mn.eq.health4men.Members;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import mn.eq.health4men.Chat.ChatActivity;
import mn.eq.health4men.Objects.UserItem;
import mn.eq.health4men.R;


/**
 * Created by Home on 11/14/2015.
 */
public class UserDetailActivity extends FragmentActivity {
    private TextView userAboutMe;
    private TextView userHeight;
    private TextView userWeight;
    private TextView userAge;
    private TextView userBodyType;
    private TextView userName;
    private ImageButton newMessage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_detail);
        userAboutMe = (TextView) findViewById(R.id.userAboutme);
        userAge = (TextView) findViewById(R.id.userAge);
        userHeight = (TextView) findViewById(R.id.userHeight);
        userWeight = (TextView) findViewById(R.id.userWeight);
        userName = (TextView) findViewById(R.id.userName);
        userBodyType = (TextView) findViewById(R.id.userBodyType);
        newMessage = (ImageButton) findViewById(R.id.newMessage);

        initData();

    }

    private void initData(){
        Bundle bundle = getIntent().getExtras();
        final UserItem userItem;
        userItem = (UserItem) bundle.getSerializable("detail");

        userAboutMe.setText(userItem.getUserAboutme());
        userAge.setText(userItem.getUserAge());
        userHeight.setText(userItem.getUserHeight());
        userWeight.setText(userItem.getUserWeight());
        userName.setText(userItem.getUserName());
        userBodyType.setText(userItem.getUserBodyType());

        newMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserDetailActivity.this, ChatActivity.class);

                Bundle bundle = new Bundle();
                bundle.putSerializable("chat_user", userItem);
                intent.putExtras(bundle);

                startActivity(intent);
            }
        });
    }

}
