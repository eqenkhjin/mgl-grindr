package mn.eq.health4men.Objects;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Tamir on 11/21/2015.
 */
public class UserImageItem implements Serializable {

    private int imageId;
    private String imageURL;
    private boolean canShow;
    private int type;
    private boolean isDeletable;

    public UserImageItem(JSONObject object) throws JSONException {


        if (object.getInt("type") == 0)this.canShow = true;
        else this.canShow = false;
        this.type = object.getInt("type");
        this.imageURL = object.getString("picture");
        this.imageId = object.getInt("id");
        this.isDeletable = false;
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
    public void setImageId(int id){
        this.imageId = id;
    }
    public int getImageId(){
        return imageId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isDeletable() {
        return isDeletable;
    }

    public void setIsDeletable(boolean isDeletable) {
        this.isDeletable = isDeletable;
    }
}
