package mn.eq.health4men.Objects;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by eQ on 11/24/15.
 */
public class IsOnline {

    private int userID;
    private boolean isOnline;

    public IsOnline(JSONObject object) throws JSONException {

        this.userID = object.getInt("id");
        if (object.getInt("is_online") == 1)this.isOnline = true;
        else this.isOnline = false;

    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setIsOnline(boolean isOnline) {
        this.isOnline = isOnline;
    }
}
