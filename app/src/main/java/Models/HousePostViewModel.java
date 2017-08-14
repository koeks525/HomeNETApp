package Models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.StringRes;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Okuhle on 2017/08/06.
 */

public class HousePostViewModel implements Parcelable {

    @SerializedName("housePostID")
    private int housePostID;
    @SerializedName("name")
    private String name;
    @SerializedName("surname")
    private String surname;
    @SerializedName("postText")
    private String postText;
    @SerializedName("houseMemberID")
    private int houseMemberID;
    @SerializedName("isFlagged")
    private int isFlagged;
    @SerializedName("mediaResource")
    private String mediaResource;
    @SerializedName("datePosted")
    private String datePosted;
    @SerializedName("isDeleted")
    private int isDeleted;
    @SerializedName("emailAddress")
    private String emailAddress;

    public HousePostViewModel(int housePostID, String name, String surname, String postText, int houseMemberID, int isFlagged, String mediaResource, String datePosted, int isDeleted, String emailAddress) {
        this.name = name;
        this.surname = surname;
        this.postText = postText;
        this.houseMemberID = houseMemberID;
        this.isFlagged = isFlagged;
        this.mediaResource = mediaResource;
        this.datePosted = datePosted;
        this.isDeleted = isDeleted;
        this.emailAddress = emailAddress;
        this.housePostID = housePostID;
    }

    protected HousePostViewModel(Parcel in) {
        housePostID = in.readInt();
        name = in.readString();
        surname = in.readString();
        postText = in.readString();
        houseMemberID = in.readInt();
        isFlagged = in.readInt();
        mediaResource = in.readString();
        datePosted = in.readString();
        isDeleted = in.readInt();
        emailAddress = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(housePostID);
        dest.writeString(name);
        dest.writeString(surname);
        dest.writeString(postText);
        dest.writeInt(houseMemberID);
        dest.writeInt(isFlagged);
        dest.writeString(mediaResource);
        dest.writeString(datePosted);
        dest.writeInt(isDeleted);
        dest.writeString(emailAddress);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<HousePostViewModel> CREATOR = new Creator<HousePostViewModel>() {
        @Override
        public HousePostViewModel createFromParcel(Parcel in) {
            return new HousePostViewModel(in);
        }

        @Override
        public HousePostViewModel[] newArray(int size) {
            return new HousePostViewModel[size];
        }
    };

    public int getHousePostID() {
        return housePostID;
    }

    public void setHousePostID(int housePostID) {
        this.housePostID = housePostID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPostText() {
        return postText;
    }

    public void setPostText(String postText) {
        this.postText = postText;
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

    public String getDatePosted() {
        return datePosted;
    }

    public void setDatePosted(String datePosted) {
        this.datePosted = datePosted;
    }

    public int getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }
}
