package Models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Okuhle on 2017/08/04.
 */

public class NewAnnouncementViewModel {

    @SerializedName("title")
    private String title;
    @SerializedName("message")
    private String message;
    @SerializedName("houseID")
    private int houseID;
    @SerializedName("emailAddress")
    private String emailAddress;

    public NewAnnouncementViewModel(String title, String message, int houseID, String emailAddress) {
        this.title = title;
        this.message = message;
        this.houseID = houseID;
        this.emailAddress = emailAddress;
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

    public int getHouseID() {
        return houseID;
    }

    public void setHouseID(int houseID) {
        this.houseID = houseID;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }
}
