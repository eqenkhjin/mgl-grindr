package mn.eq.health4men.LeftMenu;

/**
 * Created by eQ on 8/28/15.
 */
public class NavDrawerItem {
    private String title;
    private int logoID;

    public NavDrawerItem() {

    }

    public NavDrawerItem(String title,int logoID){
        this.title = title;
        this.logoID = logoID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getLogoID() {
        return logoID;
    }

    public void setLogoID(int logoID) {
        this.logoID = logoID;
    }


}
