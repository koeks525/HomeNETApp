package Models;
import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
/**
 * Created by Okuhle on 2017/04/16.
 */

public class House implements Serializable, Parcelable {

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
    @SerializedName("oneTimePin")
    private int oneTimePin;
    @SerializedName("isPrivate")
    private int isPrivate;

    public House(){}

    public House(int houseID, String dateCreated, String description, String houseImage, int isDeleted, String location, String name, int userID, int oneTimePin, int isPrivate) {
        this.houseID = houseID;
        this.dateCreated = dateCreated;
        this.description = description;
        this.houseImage = houseImage;
        this.isDeleted = isDeleted;
        this.location = location;
        this.name = name;
        this.userID = userID;
        this.oneTimePin = oneTimePin;
        this.isPrivate = isPrivate;
    }

    protected House(Parcel in) {
        houseID = in.readInt();
        dateCreated = in.readString();
        description = in.readString();
        houseImage = in.readString();
        isDeleted = in.readInt();
        location = in.readString();
        name = in.readString();
        userID = in.readInt();
        oneTimePin = in.readInt();
        isPrivate = in.readInt();
    }

    public static final Creator<House> CREATOR = new Creator<House>() {
        @Override
        public House createFromParcel(Parcel in) {
            return new House(in);
        }

        @Override
        public House[] newArray(int size) {
            return new House[size];
        }
    };

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

    public int getOneTimePin() {
        return oneTimePin;
    }

    public void setOneTimePin(int oneTimePin) {
        this.oneTimePin = oneTimePin;
    }

    public int getIsPrivate() {
        return isPrivate;
    }

    public void setIsPrivate(int isPrivate) {
        this.isPrivate = isPrivate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(houseID);
        parcel.writeString(dateCreated);
        parcel.writeString(description);
        parcel.writeString(houseImage);
        parcel.writeInt(isDeleted);
        parcel.writeString(location);
        parcel.writeString(name);
        parcel.writeInt(userID);
        parcel.writeInt(oneTimePin);
        parcel.writeInt(isPrivate);
    }
}
