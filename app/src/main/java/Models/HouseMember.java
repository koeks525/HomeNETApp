package Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Okuhle on 2017/06/22.
 */

public class HouseMember implements Parcelable {

    @SerializedName("houseMemberID")
    private int houseMemberID;
    @SerializedName("dateApplied")
    private String dateApplied;
    @SerializedName("dateApproved")
    private String dateApproved;
    @SerializedName("dateLeft")
    private String dateLeft;
    @SerializedName("isDeleted")
    private int isDeleted;
    @SerializedName("userID")
    private int userId;
    @SerializedName("houseID")
    private int houseID;

    public HouseMember(int houseMemberID, String dateApplied, String dateApproved, String dateLeft, int isDeleted, int userId, int houseID) {
        this.houseMemberID = houseMemberID;
        this.dateApplied = dateApplied;
        this.dateApproved = dateApproved;
        this.dateLeft = dateLeft;
        this.isDeleted = isDeleted;
        this.userId = userId;
        this.houseID = houseID;
    }

    protected HouseMember(Parcel in) {
        houseMemberID = in.readInt();
        dateApplied = in.readString();
        dateApproved = in.readString();
        dateLeft = in.readString();
        isDeleted = in.readInt();
        userId = in.readInt();
        houseID = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(houseMemberID);
        dest.writeString(dateApplied);
        dest.writeString(dateApproved);
        dest.writeString(dateLeft);
        dest.writeInt(isDeleted);
        dest.writeInt(userId);
        dest.writeInt(houseID);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<HouseMember> CREATOR = new Creator<HouseMember>() {
        @Override
        public HouseMember createFromParcel(Parcel in) {
            return new HouseMember(in);
        }

        @Override
        public HouseMember[] newArray(int size) {
            return new HouseMember[size];
        }
    };

    public int getHouseMemberID() {
        return houseMemberID;
    }

    public void setHouseMemberID(int houseMemberID) {
        this.houseMemberID = houseMemberID;
    }

    public String getDateApplied() {
        return dateApplied;
    }

    public void setDateApplied(String dateApplied) {
        this.dateApplied = dateApplied;
    }

    public String getDateApproved() {
        return dateApproved;
    }

    public void setDateApproved(String dateApproved) {
        this.dateApproved = dateApproved;
    }

    public String getDateLeft() {
        return dateLeft;
    }

    public void setDateLeft(String dateLeft) {
        this.dateLeft = dateLeft;
    }

    public int getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getHouseID() {
        return houseID;
    }

    public void setHouseID(int houseID) {
        this.houseID = houseID;
    }
}
