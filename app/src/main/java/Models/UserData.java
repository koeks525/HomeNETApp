package Models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Okuhle on 2017/07/02.
 */

public class UserData {

    @SerializedName("totalPosts")
    private int totalPosts;
    @SerializedName("totalHousesJoined")
    private int totalHousesJoined;
    @SerializedName("totalAnnouncements")
    private int totalAnnouncements;

    public UserData(int totalPosts, int totalHousesJoined, int totalAnnouncements) {
        this.totalPosts = totalPosts;
        this.totalHousesJoined = totalHousesJoined;
        this.totalAnnouncements = totalAnnouncements;
    }

    public int getTotalPosts() {
        return totalPosts;
    }

    public void setTotalPosts(int totalPosts) {
        this.totalPosts = totalPosts;
    }

    public int getTotalHousesJoined() {
        return totalHousesJoined;
    }

    public void setTotalHousesJoined(int totalHousesJoined) {
        this.totalHousesJoined = totalHousesJoined;
    }

    public int getTotalAnnouncements() {
        return totalAnnouncements;
    }

    public void setTotalAnnouncements(int totalAnnouncements) {
        this.totalAnnouncements = totalAnnouncements;
    }
}
