package mn.eq.health4men.Objects;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by eQ on 11/14/15.
 */
public class UserItem implements Serializable {

    private int userID;
    private String userName;
    private String userRegisterDate;
    private String userDisplayName;
    private String userImageURL;
    private String userHeight;
    private String userWeight;
    private String userAge;
    private String userCountry;
    private String userRelationshipStatus;
    private String userAboutme;
    private String userBodyType;
    private String userLookingFor;
    private String userCoordinateX;
    private String userCooordinateY;
    private String userEmail;

    public UserItem(JSONObject object) throws JSONException {
        this.userID = object.getInt("id");
        this.userName = object.getString("user_nicename");
        this.userRegisterDate = object.getString("user_registered");
        this.userDisplayName = object.getString("display_name");
        this.userImageURL = object.getString("profile_url");
        this.userHeight = object.getString("height");
        this.userWeight = object.getString("weight");
        this.userAge = object.getString("age");
        this.userCountry = object.getString("country");
        this.userRelationshipStatus = object.getString("relationship_status");
        this.userAboutme = object.getString("about_me");
        this.userBodyType = object.getString("body_type");
        this.userLookingFor = object.getString("looking_for");
        this.userCoordinateX = object.getString("urtrag");
        this.userCooordinateY = object.getString("urgurug");
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserRegisterDate() {
        return userRegisterDate;
    }

    public void setUserRegisterDate(String userRegisterDate) {
        this.userRegisterDate = userRegisterDate;
    }

    public String getUserDisplayName() {
        return userDisplayName;
    }

    public void setUserDisplayName(String userDisplayName) {
        this.userDisplayName = userDisplayName;
    }

    public String getUserImageURL() {
        return userImageURL;
    }

    public void setUserImageURL(String userImageURL) {
        this.userImageURL = userImageURL;
    }

    public String getUserHeight() {
        return userHeight;
    }

    public void setUserHeight(String userHeight) {
        this.userHeight = userHeight;
    }

    public String getUserWeight() {
        return userWeight;
    }

    public void setUserWeight(String userWeight) {
        this.userWeight = userWeight;
    }

    public String getUserAge() {
        return userAge;
    }

    public void setUserAge(String userAge) {
        this.userAge = userAge;
    }

    public String getUserCountry() {
        return userCountry;
    }

    public void setUserCountry(String userCountry) {
        this.userCountry = userCountry;
    }

    public String getUserRelationshipStatus() {
        return userRelationshipStatus;
    }

    public void setUserRelationshipStatus(String userRelationshipStatus) {
        this.userRelationshipStatus = userRelationshipStatus;
    }

    public String getUserAboutme() {
        return userAboutme;
    }

    public void setUserAboutme(String userAboutme) {
        this.userAboutme = userAboutme;
    }

    public String getUserBodyType() {
        return userBodyType;
    }

    public void setUserBodyType(String userBodyType) {
        this.userBodyType = userBodyType;
    }

    public String getUserLookingFor() {
        return userLookingFor;
    }

    public void setUserLookingFor(String userLookingFor) {
        this.userLookingFor = userLookingFor;
    }

    public String getUserCoordinateX() {
        return userCoordinateX;
    }

    public void setUserCoordinateX(String userCoordinateX) {
        this.userCoordinateX = userCoordinateX;
    }

    public String getUserCooordinateY() {
        return userCooordinateY;
    }

    public void setUserCooordinateY(String userCooordinateY) {
        this.userCooordinateY = userCooordinateY;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
