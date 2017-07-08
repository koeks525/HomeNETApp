package com.koeksworld.homenet;

import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.roughike.bottombar.BottomBar;

import HomeNETStream.FeedFragment;
import Tasks.HomeNetFeedTask;
import Utilities.DeviceUtils;

public class HomeNetFeedActivity extends AppCompatActivity {

    private HomeNetFeedTask feedTask;
    private DeviceUtils deviceUtils;
    private Toolbar appToolbar;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private TextView toolbarTextView;
    private SharedPreferences sharedPreferences;
    private TextView nameSurnameTextView;
    private TextView emailTextView;
    private BottomBar feedBottomBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_net_feed);
        initializeComponents();
        setupHeaderView();
        if (savedInstanceState == null) {
            toolbarTextView.setText("Your Feed");
        } else {
            toolbarTextView.setText(savedInstanceState.getString("toolbar"));
        }
        getSupportActionBar().setTitle(null);
        setSupportActionBar(appToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        loadFeedFragment();
    }

    private void setupHeaderView() {
        View headerView = navigationView.getHeaderView(0);
        nameSurnameTextView = (TextView) headerView.findViewById(R.id.UserNameSurnameTextView);
        emailTextView = (TextView) headerView.findViewById(R.id.HeaderEmailAddressTextView);
        nameSurnameTextView.setText(sharedPreferences.getString("name", "") + " "+sharedPreferences.getString("surname", ""));
        emailTextView.setText(sharedPreferences.getString("emailAddress", ""));
    }

    //Source: https://stackoverflow.com/questions/33194594/navigationview-get-find-header-layout
    private void initializeComponents() {
        deviceUtils = new DeviceUtils(this);
        feedBottomBar = (BottomBar) findViewById(R.id.HomeNetFeedBottomBar);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        appToolbar = (Toolbar) findViewById(R.id.HomeNetFeedToolbar);
        toolbarTextView = (TextView) findViewById(R.id.HomeNetFeedToolbarTextView);
        navigationView = (NavigationView) findViewById(R.id.HomeNetFeedNavigationView);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                //Check item
                drawerLayout.closeDrawers();
                return true;
            }
        });
        drawerLayout = (DrawerLayout) findViewById(R.id.FeedDrawerLayout);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("toolbar", toolbarTextView.getText().toString());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.app_setttings_menu_1, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);

    }

    private void loadFeedFragment() {
        FeedFragment feedFragment = new FeedFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.HomeNetFeedContentView, feedFragment, null);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
