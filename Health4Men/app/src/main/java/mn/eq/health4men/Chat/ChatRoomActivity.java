package mn.eq.health4men.Chat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import mn.eq.health4men.Adapters.AdapterMembers;
import mn.eq.health4men.Members.MembersFragment;
import mn.eq.health4men.Members.NewUserDetailActivity;
import mn.eq.health4men.Objects.UserItem;
import mn.eq.health4men.R;
import mn.eq.health4men.Root.MainActivity;
import mn.eq.health4men.Utils.RecyclerItemClickListener;

/**
 * Created by eQ on 11/27/15.
 */
public class ChatRoomActivity extends FragmentActivity {


    RecyclerView recyclerView;
    LinearLayoutManager mLayoutManager;
    AdapterMembers adapterMembers;
    private Handler handler;
    private ArrayList<UserItem> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);
        handler = new Handler();

        for (UserItem userItem : MembersFragment.arrayList){
            if (userItem.getBeforeChat() ==1){
                list.add(userItem);
            }
        }

        createInterface();
        configHeader();
    }

    private void configHeader() {
        ImageView backImageView = (ImageView) findViewById(R.id.menuIcon);
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

            }
        });

        TextView headerTitleTextView = (TextView) findViewById(R.id.toolbarTitle);
        headerTitleTextView.setText("Chat History");
    }

    private void createInterface(){


        recyclerView = (RecyclerView)findViewById(R.id.recycleView);
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        adapterMembers = new AdapterMembers(this, list,true);
        recyclerView.setAdapter(adapterMembers);

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, new RecyclerItemClickListener
                        .OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        UserItem userItem = list.get(position);

                        final Intent intent = new Intent(ChatRoomActivity.this, ChatActivity.class);

                        Bundle bundle = new Bundle();
                        bundle.putSerializable("chat_user", userItem);
                        intent.putExtras(bundle);

                        startActivity(intent);

                    }
                })
        );

    }
}
