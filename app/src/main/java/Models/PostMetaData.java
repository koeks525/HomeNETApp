package Models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Okuhle on 2017/07/01.
 */

public class PostMetaData {

    @SerializedName("housePostMetaDataID")
    private int housePostMetaDataID;
    @SerializedName("housePostID")
    private int housePostID;
    @SerializedName("userID")
    private int userID;
    @SerializedName("liked")
    private int liked;
    @SerializedName("disliked")
    private int disliked;
    @SerializedName("dateAdded")
    private String dateAdded;
    @SerializedName("housePost")
    private HousePost housePost;
    @SerializedName("user")
    private User user;

    public PostMetaData(int housePostMetaDataID, int housePostID, int userID, int liked, int disliked, String dateAdded) {
        this.housePostMetaDataID = housePostMetaDataID;
        this.housePostID = housePostID;
        this.userID = userID;
        this.liked = liked;
        this.disliked = disliked;
        this.dateAdded = dateAdded;
    }

    public PostMetaData(int housePostMetaDataID, int housePostID, int userID, int liked, int disliked, String dateAdded, HousePost housePost, User user) {
        this.housePostMetaDataID = housePostMetaDataID;
        this.housePostID = housePostID;
        this.userID = userID;
        this.liked = liked;
        this.disliked = disliked;
        this.dateAdded = dateAdded;
        this.housePost = housePost;
        this.user = user;
    }

    public int getHousePostMetaDataID() {
        return housePostMetaDataID;
    }

    public void setHousePostMetaDataID(int housePostMetaDataID) {
        this.housePostMetaDataID = housePostMetaDataID;
    }

    public int getHousePostID() {
        return housePostID;
    }

    public void setHousePostID(int housePostID) {
        this.housePostID = housePostID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getLiked() {
        return liked;
    }

    public void setLiked(int liked) {
        this.liked = liked;
    }

    public int getDisliked() {
        return disliked;
    }

    public void setDisliked(int disliked) {
        this.disliked = disliked;
    }

    public String getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
    }

    public HousePost getHousePost() {
        return housePost;
    }

    public void setHousePost(HousePost housePost) {
        this.housePost = housePost;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
