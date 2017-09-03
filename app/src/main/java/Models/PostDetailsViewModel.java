package Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Okuhle on 2017/09/02.
 */

public class PostDetailsViewModel implements Parcelable{

    @SerializedName("housePostID")
    private int housePostID;
    @SerializedName("totalLikes")
    private int totalLikes;
    @SerializedName("totalDislikes")
    private int totalDislikes;
    @SerializedName("likes")
    private List<String> likes;
    @SerializedName("dislikes")
    private List<String> dislikes;

    public PostDetailsViewModel(int housePostID, int totalLikes, int totalDislikes, List<String> likes, List<String> dislikes) {
        this.housePostID = housePostID;
        this.totalLikes = totalLikes;
        this.totalDislikes = totalDislikes;
        this.likes = likes;
        this.dislikes = dislikes;
    }

    protected PostDetailsViewModel(Parcel in) {
        housePostID = in.readInt();
        totalLikes = in.readInt();
        totalDislikes = in.readInt();
        likes = in.createStringArrayList();
        dislikes = in.createStringArrayList();
    }

    public static final Creator<PostDetailsViewModel> CREATOR = new Creator<PostDetailsViewModel>() {
        @Override
        public PostDetailsViewModel createFromParcel(Parcel in) {
            return new PostDetailsViewModel(in);
        }

        @Override
        public PostDetailsViewModel[] newArray(int size) {
            return new PostDetailsViewModel[size];
        }
    };

    public int getHousePostID() {
        return housePostID;
    }

    public void setHousePostID(int housePostID) {
        this.housePostID = housePostID;
    }

    public int getTotalLikes() {
        return totalLikes;
    }

    public void setTotalLikes(int totalLikes) {
        this.totalLikes = totalLikes;
    }

    public int getTotalDislikes() {
        return totalDislikes;
    }

    public void setTotalDislikes(int totalDislikes) {
        this.totalDislikes = totalDislikes;
    }

    public List<String> getLikes() {
        return likes;
    }

    public void setLikes(List<String> likes) {
        this.likes = likes;
    }

    public List<String> getDislikes() {
        return dislikes;
    }

    public void setDislikes(List<String> dislikes) {
        this.dislikes = dislikes;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(housePostID);
        parcel.writeInt(totalLikes);
        parcel.writeInt(totalDislikes);
        parcel.writeStringList(likes);
        parcel.writeStringList(dislikes);
    }
}
