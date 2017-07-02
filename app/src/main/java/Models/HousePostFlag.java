package Models;
import com.google.gson.annotations.SerializedName;
/**
 * Created by Okuhle on 2017/06/15.
 */

public class HousePostFlag {

    @SerializedName("housePostFlagID")
    private int housePostFlagID;
    @SerializedName("message")
    private String message;
    @SerializedName("dateFlagged")
    private String dateFlagged;
    @SerializedName("isDeleted")
    private int isDeleted;
    @SerializedName("responseMessage")
    private String responseMessage;
    @SerializedName("isFlagged")
    private int isFlagged;
    @SerializedName("houseMemberID")
    private int houseMemberID;
    @SerializedName("housePostID")
    private int housePostID;

    public HousePostFlag(int housePostFlagID, String message, String dateFlagged, int isDeleted, String responseMessage, int isFlagged, int houseMemberID, int housePostID) {
        this.housePostFlagID = housePostFlagID;
        this.message = message;
        this.dateFlagged = dateFlagged;
        this.isDeleted = isDeleted;
        this.responseMessage = responseMessage;
        this.isFlagged = isFlagged;
        this.houseMemberID = houseMemberID;
        this.housePostID = housePostID;
    }

    public int getHousePostFlagID() {
        return housePostFlagID;
    }

    public void setHousePostFlagID(int housePostFlagID) {
        this.housePostFlagID = housePostFlagID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDateFlagged() {
        return dateFlagged;
    }

    public void setDateFlagged(String dateFlagged) {
        this.dateFlagged = dateFlagged;
    }

    public int getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public int getIsFlagged() {
        return isFlagged;
    }

    public void setIsFlagged(int isFlagged) {
        this.isFlagged = isFlagged;
    }

    public int getHouseMemberID() {
        return houseMemberID;
    }

    public void setHouseMemberID(int houseMemberID) {
        this.houseMemberID = houseMemberID;
    }

    public int getHousePostID() {
        return housePostID;
    }

    public void setHousePostID(int housePostID) {
        this.housePostID = housePostID;
    }
}
