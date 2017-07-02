package Utilities;

/**
 * Created by Okuhle on 2017/06/14.
 */

//Source: https://stackoverflow.com/questions/33338181/is-it-possible-to-show-progress-bar-when-upload-image-via-retrofit-2
public interface UploadCallbacks {
    void onProgressUpdate(int percentage);
    void onError();
    void onFinish();
}
