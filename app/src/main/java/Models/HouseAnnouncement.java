package Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Okuhle on 2017/06/16.
 */

public class HouseAnnouncement implements Parcelable {

    @SerializedName("houseAnnouncementID")
    private int houseAnnouncementID;
    @SerializedName("title")
    private String title;
    @SerializedName("message")
    private String message;
    @SerializedName("dateAdded")
    private String dateAdded;
    @SerializedName("houseID")
    private int houseID;
    @SerializedName("houseMemberID")
    private int houseMemberID;
    @SerializedName("isDeleted")
    private int isDeleted;
    @SerializedName("isFlagged")
    private int isFlagged;

    public HouseAnnouncement(int houseAnnouncementID, String title, String message, String dateAdded, int houseID, int houseMemberID, int isDeleted, int isFlagged) {
        this.houseAnnouncementID = houseAnnouncementID;
        this.title = title;
        this.message = message;
        this.dateAdded = dateAdded;
        this.houseID = houseID;
        this.houseMemberID = houseMemberID;
        this.isDeleted = isDeleted;
        this.isFlagged = isFlagged;
    }

    protected HouseAnnouncement(Parcel in) {
        houseAnnouncementID = in.readInt();
        title = in.readString();
        message = in.readString();
        dateAdded = in.readString();
        houseID = in.readInt();
        houseMemberID = in.readInt();
        isDeleted = in.readInt();
        isFlagged = in.readInt();
    }

    public static final Creator<HouseAnnouncement> CREATOR = new Creator<HouseAnnouncement>() {
        @Override
        public HouseAnnouncement createFromParcel(Parcel in) {
            return new HouseAnnouncement(in);
        }

        @Override
        public HouseAnnouncement[] newArray(int size) {
            return new HouseAnnouncement[size];
        }
    };

    public int getHouseAnnouncementID() {
        return houseAnnouncementID;
    }

    public void setHouseAnnouncementID(int houseAnnouncementID) {
        this.houseAnnouncementID = houseAnnouncementID;
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

    public int getHouseID() {
        return houseID;
    }

    public void setHouseID(int houseID) {
        this.houseID = houseID;
    }

    public int getHouseMemberID() {
        return houseMemberID;
    }

    public void setHouseMemberID(int houseMemberID) {
        this.houseMemberID = houseMemberID;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(houseAnnouncementID);
        parcel.writeString(title);
        parcel.writeString(message);
        parcel.writeString(dateAdded);
        parcel.writeInt(houseID);
        parcel.writeInt(houseMemberID);
        parcel.writeInt(isDeleted);
        parcel.writeInt(isFlagged);
    }
}
