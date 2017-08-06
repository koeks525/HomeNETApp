package Models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Okuhle on 2017/08/02.
 */

public class MessagesViewModel {

    @SerializedName("messageThreadMessageID")
    private int messageThreadMessageID;
    @SerializedName("message")
    private String message;
    @SerializedName("dateSent")
    private String dateSent;
    @SerializedName("messageThreadID")
    private int messageThreadID;
    @SerializedName("houseMemberID")
    private int houseMemberID;
    @SerializedName("name")
    private String name;
    @SerializedName("surname")
    private String surname;
    @SerializedName("emailAddress")
    private String emailAddress;

    public MessagesViewModel(int messageThreadMessageID, String message, String dateSent, int messageThreadID, int houseMemberID, String name, String surname, String emailAddress) {
        this.messageThreadMessageID = messageThreadMessageID;
        this.message = message;
        this.dateSent = dateSent;
        this.messageThreadID = messageThreadID;
        this.houseMemberID = houseMemberID;
        this.name = name;
        this.surname = surname;
        this.emailAddress = emailAddress;
    }

    public int getMessageThreadMessageID() {
        return messageThreadMessageID;
    }

    public void setMessageThreadMessageID(int messageThreadMessageID) {
        this.messageThreadMessageID = messageThreadMessageID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDateSent() {
        return dateSent;
    }

    public void setDateSent(String dateSent) {
        this.dateSent = dateSent;
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
}
