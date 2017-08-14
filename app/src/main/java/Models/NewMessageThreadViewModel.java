package Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Okuhle on 2017/08/03.
 */

public class NewMessageThreadViewModel {

    @SerializedName("houseID")
    private int houseID;
    @SerializedName("threadTitle")
    private String threadTitle;
    @SerializedName("threadMessage")
    private String threadMessage;
    @SerializedName("emailAddress")
    private String emailAddress;
    @SerializedName("dateSent")
    private String dateSent;
    @SerializedName("senderEmail")
    private String senderEmail;
    @SerializedName("participants")
    private List<MessageThreadParticipant> participants;
    public NewMessageThreadViewModel(int houseID, String threadTitle, String threadMessage, String emailAddress, String dateSent, String senderEmail, List<MessageThreadParticipant> participants) {
        this.houseID = houseID;
        this.threadTitle = threadTitle;
        this.threadMessage = threadMessage;
        this.emailAddress = emailAddress;
        this.dateSent = dateSent;
        this.participants = participants;
        this.senderEmail = senderEmail;
    }


    public NewMessageThreadViewModel() {

    }

    public int getHouseID() {
        return houseID;
    }

    public void setHouseID(int houseID) {
        this.houseID = houseID;
    }

    public String getThreadTitle() {
        return threadTitle;
    }

    public void setThreadTitle(String threadTitle) {
        this.threadTitle = threadTitle;
    }

    public String getThreadMessage() {
        return threadMessage;
    }

    public void setThreadMessage(String threadMessage) {
        this.threadMessage = threadMessage;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getDateSent() {
        return dateSent;
    }

    public void setDateSent(String dateSent) {
        this.dateSent = dateSent;
    }

    public List<MessageThreadParticipant> getParticipants() {
        return participants;
    }

    public void setParticipants(List<MessageThreadParticipant> participants) {
        this.participants = participants;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }
}
