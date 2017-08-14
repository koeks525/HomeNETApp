package Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Okuhle on 2017/07/01.
 */

public class HousePostMetaDataViewModel implements Parcelable{

    @SerializedName("housePostID")
    private int housePostID;
    @SerializedName("totalLikes")
    private int totalLikes;
    @SerializedName("totalDislikes")
    private int totalDislikes;
    @SerializedName("totalComments")
    public int totalComments;

    public HousePostMetaDataViewModel(int housePostID, int totalLikes, int totalDislikes, int totalComments) {
        this.housePostID = housePostID;
        this.totalLikes = totalLikes;
        this.totalDislikes = totalDislikes;
        this.totalComments = totalComments;
    }

    protected HousePostMetaDataViewModel(Parcel in) {
        housePostID = in.readInt();
        totalLikes = in.readInt();
        totalDislikes = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(housePostID);
        dest.writeInt(totalLikes);
        dest.writeInt(totalDislikes);
        dest.writeInt(totalComments);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<HousePostMetaDataViewModel> CREATOR = new Creator<HousePostMetaDataViewModel>() {
        @Override
        public HousePostMetaDataViewModel createFromParcel(Parcel in) {
            return new HousePostMetaDataViewModel(in);
        }

        @Override
        public HousePostMetaDataViewModel[] newArray(int size) {
            return new HousePostMetaDataViewModel[size];
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

    public int getTotalComments() {
        return totalComments;
    }

    public void setTotalComments(int totalComments) {
        this.totalComments = totalComments;
    }
}
