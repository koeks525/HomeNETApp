package Models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Okuhle on 2017/04/01.
 */

public class Key {

    @SerializedName("keyID")
    public int keyID;
    @SerializedName("name")
    public String name;
    @SerializedName("description")
    private String description;
    @SerializedName("value")
    private String value;
    @SerializedName("isDeleted")
    private int isDeleted;

    public Key(int keyID, String name, String description, String value, int isDeleted) {
        this.keyID = keyID;
        this.name = name;
        this.description = description;
        this.value = value;
        this.isDeleted = isDeleted;
    }

    public int getKeyID() {
        return keyID;
    }

    public void setKeyID(int keyID) {
        this.keyID = keyID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }
}
