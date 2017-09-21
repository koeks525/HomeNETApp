package Models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Okuhle on 2017/09/05.
 */

public class HouseMemberViewModel {

    @SerializedName("houseMemberID")
    private int houseMemberID;
    @SerializedName("userID")
    private int userID;
    @SerializedName("countryID")
    private int countryID;
    @SerializedName("name")
    private String name;
    @SerializedName("surname")
    private String surname;
    @SerializedName("emailAddress")
    private String emailAddress;
    @SerializedName("reason")
    private String reason;

    public HouseMemberViewModel(int houseMemberID, int userID, int countryID, String name, String surname, String emailAddress, String reason) {
        this.houseMemberID = houseMemberID;
        this.userID = userID;
        this.countryID = countryID;
        this.name = name;
        this.surname = surname;
        this.emailAddress = emailAddress;
        this.reason = reason;
    }

    public int getHouseMemberID() {
        return houseMemberID;
    }

    public void setHouseMemberID(int houseMemberID) {
        this.houseMemberID = houseMemberID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getCountryID() {
        return countryID;
    }

    public void setCountryID(int countryID) {
        this.countryID = countryID;
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

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
