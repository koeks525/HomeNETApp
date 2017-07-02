package Models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Okuhle on 2017/05/23.
 */

public class LoginViewModel {

    @SerializedName("username")
    private String username;
    @SerializedName("password")
    private String password;

    public LoginViewModel(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
