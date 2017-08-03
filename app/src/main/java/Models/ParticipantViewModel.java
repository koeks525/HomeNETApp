package Models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Okuhle on 2017/08/02.
 */

public class ParticipantViewModel {

    @SerializedName("userID")
    private int userID;
    @SerializedName("name")
    private String name;
    @SerializedName("surname")
    private String surname;
    @SerializedName("emailAddress")
    private String emailAddress;
    @SerializedName("messageThreadParticipantID")
    private int messageThreadParticipantID;
    @SerializedName("isDeleted")
    private int isDeleted;
    @SerializedName("messageThreadID")
    private int messageThreadID;
    @SerializedName("houseMemberID")
    private int houseMemberID;

    public ParticipantViewModel(int userID, String name, String surname, String emailAddress, int messageThreadParticipantID, int isDeleted, int messageThreadID, int houseMemberID) {
        this.userID = userID;
        this.name = name;
        this.surname = surname;
        this.emailAddress = emailAddress;
        this.messageThreadParticipantID = messageThreadParticipantID;
        this.isDeleted = isDeleted;
        this.messageThreadID = messageThreadID;
        this.houseMemberID = houseMemberID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
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

    public int getMessageThreadParticipantID() {
        return messageThreadParticipantID;
    }

    public void setMessageThreadParticipantID(int messageThreadParticipantID) {
        this.messageThreadParticipantID = messageThreadParticipantID;
    }

    public int getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }

    public int getMessageThreadID() {
        return messageThreadID;
    }

    public void setMessageThreadID(int messageThreadID) {
        this.messageThreadID = messageThreadID;
    }

    public int getHouseMemberID() {
        return houseMemberID;
    }

    public void setHouseMemberID(int houseMemberID) {
        this.houseMemberID = houseMemberID;
    }
}
