package mn.eq.health4men.Objects;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Tamir on 11/21/2015.
 */
public class UserImageItem implements Serializable {


    private String imageURL;
    private boolean canShow;


    public UserImageItem(JSONObject object) throws JSONException {


        if (object.getInt("type") == 0)this.canShow = true;
        else this.canShow = false;

        this.imageURL = object.getString("picture");
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public boolean isCanShow() {
        return canShow;
    }

    public void setCanShow(boolean canShow) {
        this.canShow = canShow;
    }

    private boolean checkIsEmpty(String str){
        if (str.replace(" ","").length() == 0)return true;
        return false;
    }
}
