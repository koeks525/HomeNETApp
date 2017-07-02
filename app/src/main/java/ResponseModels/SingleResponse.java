package ResponseModels;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Okuhle on 2017/03/06.
 */

public class SingleResponse<T> {

    @SerializedName("didError")
    private boolean didError;
    @SerializedName("message")
    private String message;
    @SerializedName("model")
    private T model;

    public SingleResponse(boolean didError, String message, T model) {
        this.didError = didError;
        this.message = message;
        this.model = model;
    }

    public SingleResponse() {
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

    public T getModel() {
        return model;
    }

    public void setModel(T model) {
        this.model = model;
    }
}
