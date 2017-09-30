package Models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Okuhle on 2017/09/24.
 */

public class HouseAnnouncementViewModel {

    @SerializedName("announcementID")
    private int announcementID;
    @SerializedName("title")
    private String title;
    @SerializedName("message")
    private String message;
    @SerializedName("dateAdded")
    private String dateAdded;

    public HouseAnnouncementViewModel(int announcementID, String title, String message, String dateAdded) {
        this.announcementID = announcementID;
        this.title = title;
        this.message = message;
        this.dateAdded = dateAdded;
    }

    public int getAnnouncementID() {
        return announcementID;
    }

    public void setAnnouncementID(int announcementID) {
        this.announcementID = announcementID;
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
}
