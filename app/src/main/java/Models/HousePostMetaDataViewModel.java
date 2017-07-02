package Models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Okuhle on 2017/07/01.
 */

public class HousePostMetaDataViewModel {

    @SerializedName("housePostID")
    private int housePostID;
    @SerializedName("totalLikes")
    private int totalLikes;
    @SerializedName("totalDislikes")
    private int totalDislikes;

    public HousePostMetaDataViewModel(int housePostID, int totalLikes, int totalDislikes) {
        this.housePostID = housePostID;
        this.totalLikes = totalLikes;
        this.totalDislikes = totalDislikes;
    }

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
}
