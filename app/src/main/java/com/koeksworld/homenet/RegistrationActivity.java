package com.koeksworld.homenet;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.List;

import Data.RealmHelper;
import DialogFragments.AboutApplicationDialog;
import Fragments.CreateAccountFragment;
import Fragments.CreateAccountInformationFragment;
import Models.Country;
import Utilities.DeviceUtils;

public class RegistrationActivity extends AppCompatActivity {

    private DeviceUtils deviceUtils;
    private boolean isTablet, isLandscape;
    private FragmentManager fragmentManager;
    private CreateAccountFragment createAccountFragment;
    private CreateAccountInformationFragment createAccountInformationFragment;
    private List<Country> countryArrayList;
    private Bundle regBundle;
    private Toolbar toolbar;
    private TextView toolbarTextView;
    private RealmHelper databaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        initializeToolbar();
        deviceUtils = new DeviceUtils(this);
        fragmentManager = getFragmentManager();
        getData();
        isTablet = deviceUtils.isTablet();
        if (isTablet) {
            isLandscape = deviceUtils.isLandscape();
            if (isLandscape) {
                createAccountFragment = new CreateAccountFragment();
                createAccountFragment.setArguments(regBundle);
                FragmentTransaction transactionOne = fragmentManager.beginTransaction();
                transactionOne.replace(R.id.RegistrationActivityTabletLandscapeViewOne, createAccountFragment, "CreateAccountFragment");
                transactionOne.commit();

                createAccountInformationFragment = new CreateAccountInformationFragment();
                FragmentTransaction transactionTwo = fragmentManager.beginTransaction();
                transactionTwo.replace(R.id.RegistrationActivityTabletLandscapeViewTwo, createAccountInformationFragment, "CreateAccountInformationFragment");
                transactionTwo.commit();
            } else {
                createAccountFragment = new CreateAccountFragment();
                createAccountFragment.setArguments(regBundle);
                FragmentTransaction transactionThree = fragmentManager.beginTransaction();
                transactionThree.replace(R.id.RegistrationActivityTabletPortraitView, createAccountFragment, "CreateAccountFragment");
                transactionThree.commit();
            }
        } else {
            createAccountFragment = new CreateAccountFragment();
            createAccountFragment.setArguments(regBundle);
            FragmentTransaction transactionFour = fragmentManager.beginTransaction();
            transactionFour.replace(R.id.RegistrationActivityMobilePortraitView, createAccountFragment, "CreateAccountFragment");
            transactionFour.commit();
        }

    }

    private void getData()
    {
        databaseHelper = new RealmHelper();
        countryArrayList = databaseHelper.getCountries();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void initializeToolbar() {
        toolbar = (Toolbar) findViewById(R.id.RegistrationActivityToolbar);
        toolbarTextView = (TextView) findViewById(R.id.RegistrationActivityToolbarTextView);
        toolbarTextView.setText("Create Your Account");
        setSupportActionBar(toolbar);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_app_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.AboutApplicationOption:
                AboutApplicationDialog dialog = new AboutApplicationDialog();
                dialog.show(getFragmentManager(), null);
                break;
        }
        return true;
    }
}
