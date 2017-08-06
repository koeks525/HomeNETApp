package Models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Okuhle on 2017/08/06.
 */

public class AnnouncementComment {

    @SerializedName("AnnouncementCommentID")
    private int announcementCommentID;
    @SerializedName("houseMemberID")
    private int houseMemberID;
    @SerializedName("comment")
    private String comment;
    @SerializedName("dateAdded")
    private String dateAdded;
    @SerializedName("isDeleted")
    private int isDeleted;
    @SerializedName("isFlagged")
    private int isFlagged;
    @SerializedName("houseAnnouncementID")
    private int houseAnnouncementID;

    public AnnouncementComment(int announcementCommentID, int houseMemberID, String comment, String dateAdded, int isDeleted, int isFlagged, int houseAnnouncementID) {
        this.announcementCommentID = announcementCommentID;
        this.houseMemberID = houseMemberID;
        this.comment = comment;
        this.dateAdded = dateAdded;
        this.isDeleted = isDeleted;
        this.isFlagged = isFlagged;
        this.houseAnnouncementID = houseAnnouncementID;
    }

    public int getAnnouncementCommentID() {
        return announcementCommentID;
    }

    public void setAnnouncementCommentID(int announcementCommentID) {
        this.announcementCommentID = announcementCommentID;
    }

    public int getHouseMemberID() {
        return houseMemberID;
    }

    public void setHouseMemberID(int houseMemberID) {
        this.houseMemberID = houseMemberID;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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

    public int getHouseAnnouncementID() {
        return houseAnnouncementID;
    }

    public void setHouseAnnouncementID(int houseAnnouncementID) {
        this.houseAnnouncementID = houseAnnouncementID;
    }
}
