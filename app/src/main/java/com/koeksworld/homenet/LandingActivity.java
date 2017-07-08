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

    //Landing activity must check if the user owns a house. If they do, take them to a page where they can see details to their house
    //Show the user streams from all their houses
    //If the user doesnt manage any houses, check house membership, then get all posts from the houses they are in
    //Check if the user owns an organization. If they do, then take them to the organization manager
    //If the above has all returned 0, then show the user a landing fragment where they can decide what they want to do.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        deviceUtils = new DeviceUtils(this);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        dbHelper = new RealmHelper();
        if (deviceUtils.checkNetworkConnection()) {
            initializeRetrofit();

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

    private void checkUserHouses() {
        final Snackbar bar = Snackbar.make(this.getWindow().getCurrentFocus(), "Checking Linked Data. Please wait", Snackbar.LENGTH_INDEFINITE);
        ViewGroup viewGroup = (ViewGroup) bar.getView().findViewById(android.support.design.R.id.snackbar_text).getParent();
        viewGroup.addView(new ProgressBar(this));
        bar.show();


        Call<ListResponse<House>> houseCall = service.getUserHouses("Bearer "+ sharedPreferences.getString("authorization_token", ""), sharedPreferences.getString("emailAddress", ""), getResources().getString(R.string.homenet_client_string));
        houseCall.enqueue(new Callback<ListResponse<House>>() {
            @Override
            public void onResponse(Call<ListResponse<House>> call, Response<ListResponse<House>> response) {
                if (bar.isShown()) {
                    bar.dismiss();
                }
                ListResponse<House> houseResponse;
                switch (response.code()) {
                    case 200:
                        houseResponse = response.body();
                        if (houseResponse.getModel() != null) {
                            if (houseResponse.getModel().size() > 0) {
                                //A user owns one or more houses, save this data to the phone
                                houseList = houseResponse.getModel();
                                //This house list must be passed to the "House Manager"

                            }
                        }

                }
            }

            @Override
            public void onFailure(Call<ListResponse<House>> call, Throwable t) {
                if (bar.isShown()) {
                    bar.dismiss();
                }
            }
        });

    }
}
