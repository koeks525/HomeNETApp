package Models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Okuhle on 2017/08/03.
 */

public class UserViewModel {

    @SerializedName("userID")
    private int userID;
    @SerializedName("userName")
    private String userName;
    @SerializedName("name")
    private String name;
    @SerializedName("surname")
    private String surname;
    @SerializedName("profilePicture")
    private String profilePicture;
    @SerializedName("emailAddress")
    private String emailAddress;

    public UserViewModel(int userID, String userName, String name, String surname, String profilePicture, String emailAddress) {
        this.userID = userID;
        this.userName = userName;
        this.name = name;
        this.surname = surname;
        this.profilePicture = profilePicture;
        this.emailAddress = emailAddress;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    @Override
    public String toString() {
        return name + " "+surname;
    }
}
