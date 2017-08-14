package Models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Okuhle on 2017/08/08.
 */

public class CommentPartialModel {

    @SerializedName("emailAddress")
    private String emailAddress;
    @SerializedName("comment")
    private String comment;
    @SerializedName("housePostID")
    private int housePostID;

    public CommentPartialModel(String emailAddress, String comment, int housePostID) {
        this.emailAddress = emailAddress;
        this.comment = comment;
        this.housePostID = housePostID;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getHousePostID() {
        return housePostID;
    }

    public void setHousePostID(int housePostID) {
        this.housePostID = housePostID;
    }
}
