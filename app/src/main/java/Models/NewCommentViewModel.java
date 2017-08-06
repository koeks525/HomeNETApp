package Models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Okuhle on 2017/08/05.
 */

public class NewCommentViewModel {

    @SerializedName("houseAnnouncementID")
    private int houseAnnouncementID;
    @SerializedName("comment")
    private String comment;
    @SerializedName("emailAddress")
    private String emailAddress;

    public NewCommentViewModel(int houseAnnouncementID, String comment, String emailAddress) {
        this.houseAnnouncementID = houseAnnouncementID;
        this.comment = comment;
        this.emailAddress = emailAddress;
    }

    public int getHouseAnnouncementID() {
        return houseAnnouncementID;
    }

    public void setHouseAnnouncementID(int houseAnnouncementID) {
        this.houseAnnouncementID = houseAnnouncementID;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }
}
