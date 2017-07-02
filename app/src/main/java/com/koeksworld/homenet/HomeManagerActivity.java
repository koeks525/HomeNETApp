package com.koeksworld.homenet;

import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

import Fragments.HouseManagerEndFragment;
import Fragments.HouseManagerFragment;
import Utilities.DeviceUtils;

public class HomeManagerActivity extends AppCompatActivity {

    private DeviceUtils deviceUtils;
    private TextView toolbarTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_manager);
        if (savedInstanceState == null) {
            initializeComponents(savedInstanceState);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    public void initializeComponents(Bundle savedInstanceState) {
        toolbarTextView = (TextView) findViewById(R.id.HomeManagerActivityToolbarTextView);
        toolbarTextView.setText("Manage Your House");
        deviceUtils = new DeviceUtils(this);
        if (deviceUtils.isTablet()) {
            if (deviceUtils.isLandscape()) {
                //by default we will load the edit house fragment
                FragmentTransaction one = getFragmentManager().beginTransaction();
                one.replace(R.id.HomeManagerActivityContentViewTabletOne, new HouseManagerFragment());
                //one.addToBackStack(null);
                one.commit();
                FragmentTransaction two = getFragmentManager().beginTransaction();
                Bundle houseManagerBundle = new Bundle();
                houseManagerBundle.putString("mode", "edit_house");
                HouseManagerEndFragment fragment = new HouseManagerEndFragment();
                fragment.setArguments(houseManagerBundle);
                two.replace(R.id.HomeManagerActivityContentViewTabletTwo, fragment);
                //two.addToBackStack(null);
                two.commit();
            } else {
                FragmentTransaction landTransaction = getFragmentManager().beginTransaction();
                landTransaction.replace(R.id.HomeManagerActivityContentViewTabletLand, new HouseManagerFragment());
                landTransaction.commit();
            }
        } else {
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.HomeManagerActivityContentViewTabletLand, new HouseManagerFragment(), null);
            //transaction.addToBackStack(null);
            transaction.commit();
        }
    }
}
