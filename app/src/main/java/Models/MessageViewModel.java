package Models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Okuhle on 2017/08/12.
 */

public class MessageViewModel {

    @SerializedName("messageThreadID")
    private int messageThreadID;
    @SerializedName("houseMemberID")
    private int houseMemberID;
    @SerializedName("message")
    private String message;
    @SerializedName("emailAddress")
    private String emailAddress;

    public MessageViewModel(int messageThreadID, int houseMemberID,String message, String emailAddress) {
        this.messageThreadID = messageThreadID;
        this.message = message;
        this.emailAddress = emailAddress;
        this.houseMemberID = houseMemberID;
    }

    public int getMessageThreadID() {
        return messageThreadID;
    }

    public int getHouseMemberID() {
        return houseMemberID;
    }

    public void setHouseMemberID(int houseMemberID) {
        this.houseMemberID = houseMemberID;
    }

    public void setMessageThreadID(int messageThreadID) {
        this.messageThreadID = messageThreadID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }
}
