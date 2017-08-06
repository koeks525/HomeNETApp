package Models;

import android.support.annotation.StringRes;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Okuhle on 2017/08/06.
 */

public class HousePostViewModel {

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
