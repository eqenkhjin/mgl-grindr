package mn.eq.health4men.Objects;

import android.location.Location;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

import mn.eq.health4men.Root.MainActivity;
import mn.eq.health4men.Root.SplachScreenActivity;

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
    private String userLocation;
//    private String userBodyType;
    private String userLookingFor;
    private String userCoordinateX;
    private String userCooordinateY;
    private String userEmail;
    private String distanceBetweenMe;
    private String userRole;
    private boolean memberOnline;
    private ArrayList<UserImageItem> album = new ArrayList<>();

    private String noInformation = "no information";

    public UserItem(JSONObject object) throws JSONException {

        this.userID = object.getInt("id");

        if (checkIsEmpty(object.getString("user_nicename")))this.userName = noInformation;
        else this.userName = object.getString("user_nicename");

        if (checkIsEmpty(object.getString("user_registered")))this.userRegisterDate = noInformation;
        else this.userRegisterDate = object.getString("user_registered");

        if (checkIsEmpty(object.getString("display_name")))this.userDisplayName = noInformation;
        else this.userDisplayName = object.getString("display_name");

        this.userImageURL = object.getString("profile_url");

        if (checkIsEmpty(object.getString("height")))this.userHeight = noInformation;
        else this.userHeight = object.getString("height");

        if (checkIsEmpty(object.getString("weight")))this.userWeight = noInformation;
        else this.userWeight = object.getString("weight");


        if (checkIsEmpty(object.getString("age")))this.userAge = noInformation;
        else this.userAge = object.getString("age");

        if (checkIsEmpty(object.getString("country")))this.userCountry = noInformation;
        else this.userCountry = object.getString("country");

        if (checkIsEmpty(object.getString("relationship_status")))this.userRelationshipStatus = noInformation;
        else this.userRelationshipStatus = object.getString("relationship_status");

        if (checkIsEmpty(object.getString("about_me")))this.userAboutme = noInformation;
        else this.userAboutme = object.getString("about_me");

        if (checkIsEmpty(object.getString("looking_for")))this.userLookingFor = noInformation;
        else this.userLookingFor = object.getString("looking_for");

        this.userCoordinateX = object.getString("urtrag");
        this.userCooordinateY = object.getString("urgurug");

        if (object.has("is_online")){
            if (object.getString("is_online").equalsIgnoreCase("1"))this.memberOnline = true;
            if (object.getString("is_online").equalsIgnoreCase("0"))this.memberOnline = false;
        }

        if (MainActivity.mLastLocation == null){
            distanceBetweenMe = "Can't find location.";
        }else {
            if (this.userCoordinateX.length() > 3 && this.userCooordinateY.length() > 3){
                Location loc2 = new Location("");
                loc2.setLatitude(Double.parseDouble(this.userCoordinateX));
                loc2.setLongitude(Double.parseDouble(this.userCooordinateY));

                float distanceInMeters = MainActivity.mLastLocation.distanceTo(loc2);

                int distance = (int)distanceInMeters;

                if (distance/1000 > 0){
                    distanceBetweenMe = (distance / 1000 + " km " + distance % 1000 + " metr away");
                }else {
                    distanceBetweenMe = (distance%1000+" metr away");
                }

            }else {
                distanceBetweenMe = "No information user location";
            }
        }

        if (object.has("album")){
            JSONArray array = object.getJSONArray("album");

            if (array.length() > 0){

                for (int i = 0 ; i < array.length() ; i ++){
                    this.album.add(new UserImageItem(array.getJSONObject(i)));
                }

            }

        }
    }

    private boolean checkIsEmpty(String str){
        if (str.replace(" ","").length() == 0)return true;
        return false;
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

//    public String getUserBodyType() {
//        return userBodyType;
//    }
//
//    public void setUserBodyType(String userBodyType) {
//        this.userBodyType = userBodyType;
//    }

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

    public boolean isMemberOnline() {
        return memberOnline;
    }

    public void setMemberOnline(boolean memberOnline) {
        this.memberOnline = memberOnline;
    }

    public String getDistanceBetweenMe() {
        return distanceBetweenMe;
    }

    public void setDistanceBetweenMe(String distanceBetweenMe) {
        this.distanceBetweenMe = distanceBetweenMe;
    }

    public String getUserLocation() {
        return userLocation;
    }

    public void setUserLocation(String userLocation) {
        this.userLocation = userLocation;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public ArrayList<UserImageItem> getAlbum() {
        return album;
    }

    public void setAlbum(ArrayList<UserImageItem> album) {
        this.album = album;
    }

    public String getNoInformation() {
        return noInformation;
    }

    public void setNoInformation(String noInformation) {
        this.noInformation = noInformation;
    }
}
