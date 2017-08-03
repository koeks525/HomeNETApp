package Models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Okuhle on 2017/06/16.
 */

public class HouseAnnouncement {

    @SerializedName("announcementCommentID")
    private int announcementCommentID;
    @SerializedName("title")
    private String title;
    @SerializedName("message")
    private String message;
    @SerializedName("dateAdded")
    private String dateAdded;
    @SerializedName("isDeleted")
    private int isDeleted;
    @SerializedName("isFlagged")
    private int isFlagged;

    public HouseAnnouncement(int announcementCommentID, String title, String message, String dateAdded, int isDeleted, int isFlagged) {
        this.announcementCommentID = announcementCommentID;
        this.title = title;
        this.message = message;
        this.dateAdded = dateAdded;
        this.isDeleted = isDeleted;
        this.isFlagged = isFlagged;
    }

    public int getAnnouncementCommentID() {
        return announcementCommentID;
    }

    public void setAnnouncementCommentID(int announcementCommentID) {
        this.announcementCommentID = announcementCommentID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
    }

    public int getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }

    public int getIsFlagged() {
        return isFlagged;
    }

    public void setIsFlagged(int isFlagged) {
        this.isFlagged = isFlagged;
    }
}
