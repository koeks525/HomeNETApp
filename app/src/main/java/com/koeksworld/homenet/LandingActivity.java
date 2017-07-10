package com.koeksworld.homenet;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.List;

import Communication.HomeNetService;
import Data.RealmHelper;
import Models.House;
import ResponseModels.ListResponse;
import Tasks.UserCheckUpTask;
import Utilities.DeviceUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LandingActivity extends AppCompatActivity {

    private List<House> houseList;
    private Retrofit retrofit;
    private HomeNetService service;
    private DeviceUtils deviceUtils;
    private RealmHelper dbHelper;
    private SharedPreferences sharedPreferences;
    private UserCheckUpTask task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        deviceUtils = new DeviceUtils(this);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        task = new UserCheckUpTask(this);
        dbHelper = new RealmHelper();
        if (deviceUtils.checkNetworkConnection()) {
            initializeRetrofit();
            task.execute();

        } else {
            displayMessage("No Network Connection", "Please enable network access (WiFi or 3G) and try again", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    //positive listener action

                }
            }, "Retry", "Exit App", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    //Negative listener action
                }
            });
        }


    }

    private void initializeRetrofit() {
        deviceUtils = new DeviceUtils(this);
        retrofit = new Retrofit.Builder().baseUrl(getResources().getString(R.string.homenet_link)).addConverterFactory(GsonConverterFactory.create()).build();
        service = retrofit.create(HomeNetService.class);
    }

    private void displayMessage(String title, String message, DialogInterface.OnClickListener positiveListener, String positiveButton, String negativeButton, DialogInterface.OnClickListener negativeListener) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton(positiveButton, positiveListener);
        alertDialog.setNegativeButton(negativeButton, negativeListener);
        alertDialog.show();
    }
}
