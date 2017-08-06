package Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Okuhle on 2017/08/05.
 */

public class AnnouncementCommentViewModel implements Parcelable {

    @SerializedName("name")
    private String name;
    @SerializedName("surname")
    private String surname;
    @SerializedName("emailAddress")
    private String emailAddress;
    @SerializedName("announcementCommentID")
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

    public AnnouncementCommentViewModel(String name, String surname, String emailAddress, int announcementCommentID, int houseMemberID, String comment, String dateAdded, int isDeleted, int isFlagged, int houseAnnouncementID) {
        this.name = name;
        this.surname = surname;
        this.emailAddress = emailAddress;
        this.announcementCommentID = announcementCommentID;
        this.houseMemberID = houseMemberID;
        this.comment = comment;
        this.dateAdded = dateAdded;
        this.isDeleted = isDeleted;
        this.isFlagged = isFlagged;
        this.houseAnnouncementID = houseAnnouncementID;
    }

    protected AnnouncementCommentViewModel(Parcel in) {
        name = in.readString();
        surname = in.readString();
        emailAddress = in.readString();
        announcementCommentID = in.readInt();
        houseMemberID = in.readInt();
        comment = in.readString();
        dateAdded = in.readString();
        isDeleted = in.readInt();
        isFlagged = in.readInt();
        houseAnnouncementID = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(surname);
        dest.writeString(emailAddress);
        dest.writeInt(announcementCommentID);
        dest.writeInt(houseMemberID);
        dest.writeString(comment);
        dest.writeString(dateAdded);
        dest.writeInt(isDeleted);
        dest.writeInt(isFlagged);
        dest.writeInt(houseAnnouncementID);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AnnouncementCommentViewModel> CREATOR = new Creator<AnnouncementCommentViewModel>() {
        @Override
        public AnnouncementCommentViewModel createFromParcel(Parcel in) {
            return new AnnouncementCommentViewModel(in);
        }

        @Override
        public AnnouncementCommentViewModel[] newArray(int size) {
            return new AnnouncementCommentViewModel[size];
        }
    };

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

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
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
