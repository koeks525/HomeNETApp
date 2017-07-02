package Utilities;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;

/**
 * Created by Okuhle on 2017/03/05.
 */

public class DeviceUtils {

    private Activity currentActivity;

    public DeviceUtils(Activity currentActivity) {
        this.currentActivity = currentActivity;
    }

    public boolean isTablet () {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        currentActivity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = (int) (displayMetrics.widthPixels / displayMetrics.density);
        int height = (int) (displayMetrics.heightPixels / displayMetrics.density);
        int min = Math.min(width, height);
        if (min >= 600) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isLandscape()
    {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        currentActivity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = Math.round(displayMetrics.widthPixels / displayMetrics.density);
        int height = Math.round(displayMetrics.heightPixels / displayMetrics.density);
        if (width > height) {
            return true;
        } else {
            return false;
        }
    }

    //Source: http://stackoverflow.com/questions/4238921/detect-whether-there-is-an-internet-connection-available-on-android
    public boolean checkNetworkConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) currentActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }


}
