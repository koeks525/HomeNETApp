package Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Okuhle on 2017/03/02.
 */

public class Country implements Serializable, Parcelable {

    @SerializedName("countryID")
    private int countryID;
    @SerializedName("name")
    private String name;
    @SerializedName("isDeleted")
    private int isDeleted;

    public Country() {}

    public Country(int countryID, String name, int isDeleted) {
        this.countryID = countryID;
        this.name = name;
        this.isDeleted = isDeleted;
    }

    protected Country(Parcel in) {
        countryID = in.readInt();
        name = in.readString();
        isDeleted = in.readInt();
    }

    public static final Creator<Country> CREATOR = new Creator<Country>() {
        @Override
        public Country createFromParcel(Parcel in) {
            return new Country(in);
        }

        @Override
        public Country[] newArray(int size) {
            return new Country[size];
        }
    };

    public int getCountryID() {
        return countryID;
    }

    public void setCountryID(int countryID) {
        this.countryID = countryID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(countryID);
        parcel.writeString(name);
        parcel.writeInt(isDeleted);
    }
}
