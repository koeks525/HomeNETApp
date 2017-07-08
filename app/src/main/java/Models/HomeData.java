package Models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Okuhle on 2017/07/02.
 */

public class HomeData {

    @SerializedName("totalPosts")
    private int totalPosts;
    @SerializedName("totalUsers")
    private int totalUsers;
    @SerializedName("totalBannedUsers")
    private int totalBannedUsers;
    @SerializedName("totalPendingUsers")
    private int totalPendingUsers;
    @SerializedName("totalActiveUsers")
    private int totalActiveUsers;
    @SerializedName("dateCreated")
    private String dateCreated;

    public HomeData(int totalPosts, int totalUsers, int totalBannedUsers, int totalPendingUsers, int totalActiveUsers, String dateCreated) {
        this.totalPosts = totalPosts;
        this.totalUsers = totalUsers;
        this.totalBannedUsers = totalBannedUsers;
        this.totalPendingUsers = totalPendingUsers;
        this.totalActiveUsers = totalActiveUsers;
        this.dateCreated = dateCreated;
    }

    public int getTotalPosts() {
        return totalPosts;
    }

    public void setTotalPosts(int totalPosts) {
        this.totalPosts = totalPosts;
    }

    public int getTotalUsers() {
        return totalUsers;
    }

    public void setTotalUsers(int totalUsers) {
        this.totalUsers = totalUsers;
    }

    public int getTotalBannedUsers() {
        return totalBannedUsers;
    }

    public void setTotalBannedUsers(int totalBannedUsers) {
        this.totalBannedUsers = totalBannedUsers;
    }

    public int getTotalPendingUsers() {
        return totalPendingUsers;
    }

    public void setTotalPendingUsers(int totalPendingUsers) {
        this.totalPendingUsers = totalPendingUsers;
    }

    public int getTotalActiveUsers() {
        return totalActiveUsers;
    }

    public void setTotalActiveUsers(int totalActiveUsers) {
        this.totalActiveUsers = totalActiveUsers;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }
}
