package com.koeksworld.homenet;

import android.app.Fragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.viewpagerindicator.LinePageIndicator;

import java.util.ArrayList;

import Adapters.GettingStartedPagerAdapter;
import Communication.HomeNetService;
import Fragments.CreateHouseIntroductionFragment;
import Fragments.CreateOrganizationIntroductionFragment;
import Fragments.FindHousesIntroductionFragment;
import Fragments.GettingStartedFinalFragment;
import Tasks.UserCheckUpTask;
import Utilities.DeviceUtils;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WelcomeActivity extends AppCompatActivity {



    private UserCheckUpTask checkUpTask;
    private DeviceUtils deviceUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
            checkUpTask = new UserCheckUpTask(this);
            deviceUtils = new DeviceUtils(this);
            if (deviceUtils.checkNetworkConnection()) {
                checkUpTask.execute();
            } else {
                displayMessage("No Network Connection", "To continue, please check your network connection", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        System.exit(0);
                    }
                });
            }

    }

    private void displayMessage(String title, String message, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title).setMessage(message).setCancelable(false).setPositiveButton("Got it", listener);
        builder.show();
    }
}
