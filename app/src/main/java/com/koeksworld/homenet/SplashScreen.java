package com.koeksworld.homenet;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.ImageView;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import Data.RealmHelper;
import Models.Key;
import Tasks.AppSetupTask;
import Utilities.DeviceUtils;
import io.fabric.sdk.android.Fabric;
import java.util.ArrayList;
import java.util.List;

import Communication.HomeNetService;
import Models.Country;
import io.realm.Realm;
import retrofit2.Retrofit;

public class SplashScreen extends AppCompatActivity {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static String TWITTER_KEY; //"t93jnVGNgYHRL1hL4cAZG7hUX";
    private static String TWITTER_SECRET; //"7jB0HTdVlvQRwX1WzwhJAzs6U3rGe3aKJ0PlLq7GbKiT38Sy2l";
    private int callCount = 0;
    private List<Country> countryList = new ArrayList<>();
    private List<Key> keysList = new ArrayList<>();
    private Retrofit retrofitClient;
    private HomeNetService homeNetService;
    private ImageView imageView;
    private DeviceUtils deviceUtils;
    private RealmHelper helper;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private AppSetupTask task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            setTheme(R.style.AppTheme);
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_splash_screen);
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            imageView = (ImageView) findViewById(R.id.SplashScreenImageView);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            loadImage();
            deviceUtils = new DeviceUtils(this);
            if (deviceUtils.checkNetworkConnection()) {
                task = new AppSetupTask(this);
                task.execute().wait();
            } else {
                displayMessage("No Network Connection", "Please connect to the internet", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        System.exit(0);
                    }
                });
            }
        } catch (Exception error) {

        }

    }

    private void loadImage() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;
        if (width > height) {
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.homenet_background_launch));
        } else {
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.homenet_background_vertical));
        }
    }

    private void checkKeys() {
        if (TWITTER_KEY != null && TWITTER_SECRET != null) {
            TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
            Fabric.with(this, new Twitter(authConfig));
            Intent loginIntent = new Intent(getApplicationContext(), MainActivity.class);
            loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(loginIntent);
            overridePendingTransition(0, 0);
            finish();
        } else {
            displayMessage("Error Getting Keys", "No keys were found on the system", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    System.exit(0);
                }
            });
        }
    }


    private void displayMessage(String title, String message, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder box = new AlertDialog.Builder(this);
        box.setTitle(title).setMessage(message).setCancelable(false).setPositiveButton("Okay", listener).show();
    }

    private void loadKeys() {
        for (Key thiskey : keysList) {
            if (thiskey.getName().trim().equalsIgnoreCase("twitter_api_key")) {
                TWITTER_KEY = thiskey.getValue();
            }
            if (thiskey.getName().trim().equalsIgnoreCase("twitter_api_secret")) {
                TWITTER_SECRET = thiskey.getValue();
            }
            if (thiskey.getName().trim().equalsIgnoreCase("facebook_app_id")) {
                editor.putString("facebook_app_id", thiskey.getValue());
                editor.commit();
            }
        }
    }
}
