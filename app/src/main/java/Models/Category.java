package Models;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Okuhle on 2017/04/17.
 */

public class Category extends RealmObject{

    @PrimaryKey
    @SerializedName("categoryID")
    private int categoryID;
    @SerializedName("name")
    private String name;
    @SerializedName("isDeleted")
    private int isDeleted;

    public Category() {

    }

    public Category(int categoryID, String name, int isDeleted) {
        this.categoryID = categoryID;
        this.name = name;
        this.isDeleted = isDeleted;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
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
}
