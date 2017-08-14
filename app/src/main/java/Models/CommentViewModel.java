package Models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Okuhle on 2017/08/08.
 */

public class CommentViewModel {

    @SerializedName("name")
    private String name;
    @SerializedName("surname")
    private String surname;
    @SerializedName("emailAddress")
    private String emailAddress;
    @SerializedName("housePostID")
    private int housePostID;
    @SerializedName("housePostCommentID")
    private int housePostCommentID;
    @SerializedName("comment")
    private String comment;
    @SerializedName("dateAdded")
    private String dateAdded;
    @SerializedName("houseMemberID")
    private int houseMemberID;

    public CommentViewModel(String name, String surname, String emailAddress, int housePostID, int housePostCommentID, String comment, String dateAdded, int houseMemberID) {
        this.name = name;
        this.surname = surname;
        this.emailAddress = emailAddress;
        this.housePostID = housePostID;
        this.housePostCommentID = housePostCommentID;
        this.comment = comment;
        this.dateAdded = dateAdded;
        this.houseMemberID = houseMemberID;
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

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public int getHousePostID() {
        return housePostID;
    }

    public void setHousePostID(int housePostID) {
        this.housePostID = housePostID;
    }

    public int getHousePostCommentID() {
        return housePostCommentID;
    }

    public void setHousePostCommentID(int housePostCommentID) {
        this.housePostCommentID = housePostCommentID;
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

    public int getHouseMemberID() {
        return houseMemberID;
    }

    public void setHouseMemberID(int houseMemberID) {
        this.houseMemberID = houseMemberID;
    }
}
