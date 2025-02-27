package Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.File;

/**
 * Created by Okuhle on 2017/09/03.
 */

public class Picture implements Parcelable {

    private File imageFile;

    public Picture() {

    }

    public Picture(File imageFile) {
        this.imageFile = imageFile;
    }

    protected Picture(Parcel in) {
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Picture> CREATOR = new Creator<Picture>() {
        @Override
        public Picture createFromParcel(Parcel in) {
            return new Picture(in);
        }

        @Override
        public Picture[] newArray(int size) {
            return new Picture[size];
        }
    };

    public File getImageFile() {
        return imageFile;
    }

    public void setImageFile(File imageFile) {
        this.imageFile = imageFile;
    }
}
