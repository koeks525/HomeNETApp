package Models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Okuhle on 2017/06/22.
 */

public class HouseMember {

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

    public HouseMember(int houseMemberID, String dateApplied, String dateApproved, String dateLeft, int isDeleted, int userId) {
        this.houseMemberID = houseMemberID;
        this.dateApplied = dateApplied;
        this.dateApproved = dateApproved;
        this.dateLeft = dateLeft;
        this.isDeleted = isDeleted;
        this.userId = userId;
    }

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
}
