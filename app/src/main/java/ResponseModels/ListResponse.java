package ResponseModels;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Okuhle on 2017/03/02.
 */

public class ListResponse<T> {

    @SerializedName("didError")
    private boolean didError;
    @SerializedName("message")
    private String message;
    @SerializedName("model")
    private List<T> model;

    public ListResponse() {
    }

    public ListResponse(boolean didError, String message, List<T> model) {
        this.didError = didError;
        this.message = message;
        this.model = model;
    }

    public boolean isDidError() {
        return didError;
    }

    public void setDidError(boolean didError) {
        this.didError = didError;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<T> getModel() {
        return model;
    }

    public void setModel(List<T> model) {
        this.model = model;
    }
}
