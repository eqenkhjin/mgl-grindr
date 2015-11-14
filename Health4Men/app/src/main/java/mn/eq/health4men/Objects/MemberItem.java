package mn.eq.health4men.Objects;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by eQ on 11/14/15.
 */
public class MemberItem implements Serializable {

    private int memberID;
    private String memberName;
    private String memberImageURL;
    private String memberCoordinateX;
    private String memberCoordinateY;
    private String memberAge;
    private boolean memberOnline;

    public MemberItem(){

    }

    public MemberItem(JSONObject object) throws JSONException {
        this.memberID = object.getInt("id");
        this.memberName = object.getString("name");
        this.memberImageURL = object.getString("profile_url");
        this.memberCoordinateX = object.getString("urtrag");
        this.memberCoordinateY = object.getString("urgurug");
        this.memberAge = object.getString("age");

        if (object.getString("is_online").equalsIgnoreCase("1"))this.memberOnline = true;
        if (object.getString("is_online").equalsIgnoreCase("0"))this.memberOnline = false;
    }

    public int getMemberID() {
        return memberID;
    }

    public void setMemberID(int memberID) {
        this.memberID = memberID;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getMemberImageURL() {
        return memberImageURL;
    }

    public void setMemberImageURL(String memberImageURL) {
        this.memberImageURL = memberImageURL;
    }

    public String getMemberCoordinateX() {
        return memberCoordinateX;
    }

    public void setMemberCoordinateX(String memberCoordinateX) {
        this.memberCoordinateX = memberCoordinateX;
    }

    public String getMemberCoordinateY() {
        return memberCoordinateY;
    }

    public void setMemberCoordinateY(String memberCoordinateY) {
        this.memberCoordinateY = memberCoordinateY;
    }

    public String getMemberAge() {
        return memberAge;
    }

    public void setMemberAge(String memberAge) {
        this.memberAge = memberAge;
    }

    public boolean isMemberOnline() {
        return memberOnline;
    }

    public void setMemberOnline(boolean memberOnline) {
        this.memberOnline = memberOnline;
    }
}
