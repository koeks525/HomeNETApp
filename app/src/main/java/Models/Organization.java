package Models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Okuhle on 2017/05/21.
 */

public class Organization {

    @SerializedName("organizationID")
    private int organizationID;
    @SerializedName("categoryID")
    private int categoryID;
    @SerializedName("dateAdded")
    private String dateAdded;
    @SerializedName("description")
    private String description;
    @SerializedName("emailAddress")
    private String emailAddress;
    @SerializedName("facebookID")
    private String facebookID;
    @SerializedName("location")
    private String location;
    @SerializedName("name")
    private String name;
    @SerializedName("organizationPhoto")
    private String organizationPhoto;
    @SerializedName("skypeID")
    private String skypeID;
    @SerializedName("telephoneNumber")
    private String telephoneNumber;
    @SerializedName("twitterID")
    private String twitterID;
    @SerializedName("userID")
    private int userID;

    public Organization(int organizationID, int categoryID, String dateAdded, String description, String emailAddress, String facebookID, String location, String name, String organizationPhoto, String skypeID, String telephoneNumber, String twitterID, int userID) {
        this.organizationID = organizationID;
        this.categoryID = categoryID;
        this.dateAdded = dateAdded;
        this.description = description;
        this.emailAddress = emailAddress;
        this.facebookID = facebookID;
        this.location = location;
        this.name = name;
        this.organizationPhoto = organizationPhoto;
        this.skypeID = skypeID;
        this.telephoneNumber = telephoneNumber;
        this.twitterID = twitterID;
        this.userID = userID;
    }

    public int getOrganizationID() {
        return organizationID;
    }

    public void setOrganizationID(int organizationID) {
        this.organizationID = organizationID;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    public String getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getFacebookID() {
        return facebookID;
    }

    public void setFacebookID(String facebookID) {
        this.facebookID = facebookID;
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

    public String getOrganizationPhoto() {
        return organizationPhoto;
    }

    public void setOrganizationPhoto(String organizationPhoto) {
        this.organizationPhoto = organizationPhoto;
    }

    public String getSkypeID() {
        return skypeID;
    }

    public void setSkypeID(String skypeID) {
        this.skypeID = skypeID;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

    public String getTwitterID() {
        return twitterID;
    }

    public void setTwitterID(String twitterID) {
        this.twitterID = twitterID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }
}
