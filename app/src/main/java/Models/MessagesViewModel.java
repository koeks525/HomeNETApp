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

    public MessagesViewModel(int messageThreadMessageID, String message, String dateSent, int messageThreadID, int houseMemberID) {
        this.messageThreadMessageID = messageThreadMessageID;
        this.message = message;
        this.dateSent = dateSent;
        this.messageThreadID = messageThreadID;
        this.houseMemberID = houseMemberID;
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
}
