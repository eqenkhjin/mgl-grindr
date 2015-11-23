package mn.eq.health4men.Objects;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import mn.eq.health4men.Root.SplachScreenActivity;

/**
 * Created by xashaa on 8/20/15.
 */
public class ChatItem implements Serializable {


    public int chatID;
    public boolean isRead;
    public String date;
    public String userName;
    public String body;
    public int userID;
    public String userImageURL;
    public boolean isMyMessage;
    public String imageURL;
    public boolean hasImage = false;

    public ChatItem(JSONObject object) throws JSONException {

        chatID = object.getInt("chat_id");
        date = object.getString("created_at");
        body = object.getString("text");
        userImageURL = object.getString("profile_url");
        userID = object.getInt("from_user");

        if (object.has("picture")){
            imageURL = object.getString("picture");
            hasImage = true;
        }

    }
}
