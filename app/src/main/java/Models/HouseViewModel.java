package Models;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Okuhle on 2017/08/18.
 */

public class HouseViewModel {

    @SerializedName("houseID")
    private int houseID;
    @SerializedName("name")
    private String name;
    @SerializedName("description")
    private String description;
    @SerializedName("houseImage")
    private String houseImage;
    @SerializedName("dateCreated")
    private String dateCreated;
    @SerializedName("totalMembers")
    private int totalMembers;
    @SerializedName("bannedUsers")
    private int bannedUsers;
    @SerializedName("owner")
    private String owner;
    @SerializedName("totalPosts")
    private int totalPosts;
    @SerializedName("totalAnnouncements")
    private int totalAnnouncements;
    @SerializedName("totalComments")
    private int totalComments;

    public HouseViewModel(int houseID, String name, String description, String houseImage, String dateCreated, int totalMembers, int bannedUsers, String owner, int totalPosts, int totalAnnouncements, int totalComments) {
        this.houseID = houseID;
        this.name = name;
        this.description = description;
        this.houseImage = houseImage;
        this.dateCreated = dateCreated;
        this.totalMembers = totalMembers;
        this.bannedUsers = bannedUsers;
        this.owner = owner;
        this.totalPosts = totalPosts;
        this.totalAnnouncements = totalAnnouncements;
        this.totalComments = totalComments;
    }

    public int getHouseID() {
        return houseID;
    }

    public void setHouseID(int houseID) {
        this.houseID = houseID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHouseImage() {
        return houseImage;
    }

    public void setHouseImage(String houseImage) {
        this.houseImage = houseImage;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public int getTotalMembers() {
        return totalMembers;
    }

    public void setTotalMembers(int totalMembers) {
        this.totalMembers = totalMembers;
    }

    public int getBannedUsers() {
        return bannedUsers;
    }

    public void setBannedUsers(int bannedUsers) {
        this.bannedUsers = bannedUsers;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public int getTotalPosts() {
        return totalPosts;
    }

    public void setTotalPosts(int totalPosts) {
        this.totalPosts = totalPosts;
    }

    public int getTotalAnnouncements() {
        return totalAnnouncements;
    }

    public void setTotalAnnouncements(int totalAnnouncements) {
        this.totalAnnouncements = totalAnnouncements;
    }

    public int getTotalComments() {
        return totalComments;
    }

    public void setTotalComments(int totalComments) {
        this.totalComments = totalComments;
    }
}
