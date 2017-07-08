package Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Okuhle on 2017/05/12.
 */

public class HousePost implements Parcelable {

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
    @SerializedName("isFlagged")
    private int isFlagged;
    @SerializedName("mediaResource")
    private String mediaResource;
    //No need to resize since Composer compresses the image

    public HousePost(int housePostID, String title, String postText, String location, int isDeleted, int houseMemberID, int isFlagged, String mediaResource) {
        this.housePostID = housePostID;
        this.title = title;
        this.postText = postText;
        this.location = location;
        this.isDeleted = isDeleted;
        this.houseMemberID = houseMemberID;
        this.isFlagged = isFlagged;
        this.mediaResource = mediaResource;
    }

    protected HousePost(Parcel in) {
        housePostID = in.readInt();
        title = in.readString();
        postText = in.readString();
        location = in.readString();
        isDeleted = in.readInt();
        houseMemberID = in.readInt();
        isFlagged = in.readInt();
        mediaResource = in.readString();
    }

    public static final Creator<HousePost> CREATOR = new Creator<HousePost>() {
        @Override
        public HousePost createFromParcel(Parcel in) {
            return new HousePost(in);
        }

        @Override
        public HousePost[] newArray(int size) {
            return new HousePost[size];
        }
    };

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

    public int getIsFlagged() {
        return isFlagged;
    }

    public void setIsFlagged(int isFlagged) {
        this.isFlagged = isFlagged;
    }

    public String getMediaResource() {
        return mediaResource;
    }

    public void setMediaResource(String mediaResource) {
        this.mediaResource = mediaResource;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(housePostID);
        parcel.writeString(title);
        parcel.writeString(postText);
        parcel.writeString(location);
        parcel.writeInt(isDeleted);
        parcel.writeInt(houseMemberID);
        parcel.writeInt(isFlagged);
        parcel.writeString(mediaResource);
    }
}
