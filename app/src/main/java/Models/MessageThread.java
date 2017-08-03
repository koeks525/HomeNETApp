package Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Okuhle on 2017/07/29.
 */

public class MessageThread implements Parcelable{

    @SerializedName("messageThreadID")
    private int messageThreadID;
    @SerializedName("title")
    private String title;
    @SerializedName("priority")
    private int priority;
    @SerializedName("houseMemberID")
    private int houseMemberID;

    public MessageThread(int messageThreadID, String title, int priority, int houseMemberID) {
        this.messageThreadID = messageThreadID;
        this.title = title;
        this.priority = priority;
        this.houseMemberID = houseMemberID;
    }

    protected MessageThread(Parcel in) {
        messageThreadID = in.readInt();
        title = in.readString();
        priority = in.readInt();
        houseMemberID = in.readInt();
    }

    public static final Creator<MessageThread> CREATOR = new Creator<MessageThread>() {
        @Override
        public MessageThread createFromParcel(Parcel in) {
            return new MessageThread(in);
        }

        @Override
        public MessageThread[] newArray(int size) {
            return new MessageThread[size];
        }
    };

    public int getMessageThreadID() {
        return messageThreadID;
    }

    public void setMessageThreadID(int messageThreadID) {
        this.messageThreadID = messageThreadID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getHouseMemberID() {
        return houseMemberID;
    }

    public void setHouseMemberID(int houseMemberID) {
        this.houseMemberID = houseMemberID;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(messageThreadID);
        parcel.writeString(title);
        parcel.writeInt(priority);
        parcel.writeInt(houseMemberID);
    }
}
