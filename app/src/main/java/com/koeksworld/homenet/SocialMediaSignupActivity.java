package com.koeksworld.homenet;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;

import Fragments.CreateAccountInformationFragment;
import Fragments.FacebookSignupFragment;
import Models.Country;
import Utilities.DeviceUtils;

public class SocialMediaSignupActivity extends AppCompatActivity {

    private Bundle socialMediaBundle;
    private ArrayList<Country> countryList;
    int network = 0;
    private DeviceUtils deviceUtils;
    private boolean isTablet, isLandscape;
    private FragmentManager manager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_media_signup);
        deviceUtils = new DeviceUtils(this);
        manager = getFragmentManager();
        getData();
        loadSocialMediaFragments();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void getData() {
        socialMediaBundle = getIntent().getExtras();
        countryList = socialMediaBundle.getParcelableArrayList("Countries");
        network = socialMediaBundle.getInt("social_network");
    }

    private void loadSocialMediaFragments() {
        if (network == 1) {
            FacebookSignupFragment signupFragment = new FacebookSignupFragment();
            signupFragment.setArguments(socialMediaBundle);
            CreateAccountInformationFragment fragment = new CreateAccountInformationFragment();
            if (deviceUtils.isTablet()) {
                if (deviceUtils.isLandscape()) {
                    FragmentTransaction transactionOne = manager.beginTransaction();

                }
            }
        } else if (network == 2) {

        } else {
            //go back because we coulnt mathc things up
            this.onBackPressed();
        }
    }
}
