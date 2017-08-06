package Models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Okuhle on 2017/08/03.
 */

public class MessageThreadParticipant {

    @SerializedName("messageThreadParticipantID")
    private int messageThreadParticipantID;
    @SerializedName("isDeleted")
    private int isDeleted;
    @SerializedName("messageThreadID")
    private int messageThreadID;
    @SerializedName("houseMemberID")
    private int houseMemberID;

    public MessageThreadParticipant(int messageThreadParticipantID, int isDeleted, int messageThreadID, int houseMemberID) {
        this.messageThreadParticipantID = messageThreadParticipantID;
        this.isDeleted = isDeleted;
        this.messageThreadID = messageThreadID;
        this.houseMemberID = houseMemberID;
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
