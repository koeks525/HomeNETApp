package Models;
import android.graphics.Bitmap;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
/**
 * Created by Okuhle on 2017/04/16.
 */

public class House implements Serializable {

    @SerializedName("houseID")
    private int houseID;
    @SerializedName("dateCreated")
    private String dateCreated;
    @SerializedName("description")
    private String description;
    @SerializedName("houseImage")
    private String houseImage; //Reference to image on web server.
    @SerializedName("isDeleted")
    private int isDeleted;
    @SerializedName("location")
    private String location;
    @SerializedName("name")
    private String name;
    @SerializedName("userID")
    private int userID;

    public House() {

    }

    public int getHouseID() {
        return houseID;
    }

    public void setHouseID(int houseID) {
        this.houseID = houseID;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
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

    public int getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }
}
