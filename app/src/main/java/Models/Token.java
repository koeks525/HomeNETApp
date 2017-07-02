package Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Okuhle on 2017/04/13.
 */

public class Token implements Serializable {

    @SerializedName("tokenHandler")
    private String tokenHandler;
    @SerializedName("dateExpires")
    private String dateExpires;

    public Token(String tokenHandler, String dateExpires) {
        this.tokenHandler = tokenHandler;
        this.dateExpires = dateExpires;
    }

    public String getTokenHandler() {
        return tokenHandler;
    }

    public void setTokenHandler(String tokenHandler) {
        this.tokenHandler = tokenHandler;
    }

    public String getDateExpires() {
        return dateExpires;
    }

    public void setDateExpires(String dateExpires) {
        this.dateExpires = dateExpires;
    }
}
