package Models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Okuhle on 2017/05/12.
 */

public class HousePost {

    @SerializedName("housePostID")
    private int housePostID;
    @SerializedName("title")
    private String title;
    @SerializedName("postText")
    private String postText;
    @SerializedName("location")
    private String location;
    @SerializedName("isDeleted")
    private int isDeleted;
    @SerializedName("houseMemberID")
    private int houseMemberID;

    public HousePost(int housePostID, String title, String postText, String location, int isDeleted, int houseMemberID) {
        this.housePostID = housePostID;
        this.title = title;
        this.postText = postText;
        this.location = location;
        this.isDeleted = isDeleted;
        this.houseMemberID = houseMemberID;
    }

    public int getHousePostID() {
        return housePostID;
    }

    public void setHousePostID(int housePostID) {
        this.housePostID = housePostID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPostText() {
        return postText;
    }

    public void setPostText(String postText) {
        this.postText = postText;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }

    public int getHouseMemberID() {
        return houseMemberID;
    }

    public void setHouseMemberID(int houseMemberID) {
        this.houseMemberID = houseMemberID;
    }
}
