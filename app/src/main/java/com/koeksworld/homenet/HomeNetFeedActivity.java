package com.koeksworld.homenet;

import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import Communication.HomeNetService;
import Fragments.EditProfileFragment;
import Fragments.HomeNetProfileFragment;
import Fragments.HouseMessagesFragment;
import Fragments.MyHousesFragment;
import Fragments.NewHouseFragment;
import HomeNETStream.AnnoucementFragment;
import HomeNETStream.FeedFragment;
import HomeNETStream.PhotoGalleryFragment;
import HomeNETStream.SearchHousesFragment;
import Models.House;
import ResponseModels.ListResponse;
import Tasks.HomeNetFeedTask;
import Utilities.DeviceUtils;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeNetFeedActivity extends AppCompatActivity {

    private HomeNetFeedTask feedTask;
    private DeviceUtils deviceUtils;
    private Toolbar appToolbar;
    private NavigationView navigationView;
    private ActionBarDrawerToggle drawerToggle;
    private DrawerLayout drawerLayout;
    private TextView toolbarTextView;
    private TextView nameSurnameTextView;
    private TextView emailTextView;
    private BottomBar feedBottomBar;
    private ImageView profileImageView;
    private HomeNetService service;
    private Retrofit retrofit;
    private OkHttpClient client;
    private List<Protocol> protocolList;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_net_feed);
        initializeComponents();
        setupHeaderView();
        toolbarTextView.setText("Your Feed");
        initializeRetrofit();
        //getSupportActionBar().setTitle(null);
        setSupportActionBar(appToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (savedInstanceState == null) {
            loadFeedFragment();
        }
    }

    private void initializeRetrofit() {
        protocolList = new ArrayList<>();
        protocolList.add(Protocol.HTTP_1_1);
        client = new OkHttpClient.Builder().connectTimeout(2, TimeUnit.MINUTES).readTimeout(2, TimeUnit.MINUTES).protocols(protocolList).build();
        retrofit = new Retrofit.Builder().baseUrl(getResources().getString(R.string.homenet_link)).client(client).addConverterFactory(GsonConverterFactory.create()).build();
        service = retrofit.create(HomeNetService.class);
    }

    private void setupHeaderView() {
        View headerView = navigationView.inflateHeaderView(R.layout.homenet_feed_menu_header);
        nameSurnameTextView = (TextView) headerView.findViewById(R.id.UserNameSurnameTextView);
        emailTextView = (TextView) headerView.findViewById(R.id.HeaderEmailAddressTextView);
        nameSurnameTextView.setText(sharedPreferences.getString("name", "") + " "+sharedPreferences.getString("surname", ""));
        emailTextView.setText(sharedPreferences.getString("emailAddress", ""));
        profileImageView = (ImageView) headerView.findViewById(R.id.UserProfileImageView);
        String name =  sharedPreferences.getString("name", "").substring(0,1);
        String surname = sharedPreferences.getString("surname", "").substring(0,1);
        TextDrawable drawable = TextDrawable.builder().buildRect(name.toUpperCase() + surname.toUpperCase(), Color.BLUE);
        profileImageView.setImageDrawable(drawable);

    }

    //Source: https://stackoverflow.com/questions/33194594/navigationview-get-find-header-layout
    private void initializeComponents() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        deviceUtils = new DeviceUtils(this);
        feedBottomBar = (BottomBar) findViewById(R.id.HomeNetFeedBottomBar);
        feedBottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                switch (tabId) {
                    case R.id.MessagesTab:
                        if (getSupportActionBar() != null) {
                            if (!getSupportActionBar().isShowing()) {
                                getSupportActionBar().show();
                            }
                        }
                        HouseMessagesFragment messagesFragment = new HouseMessagesFragment();
                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                        transaction.replace(R.id.HomeNetFeedContentView, messagesFragment, null);
                        transaction.addToBackStack(null);
                        transaction.commit();
                        break;
                    case R.id.AnnouncementsTab:
                        if (getSupportActionBar() != null) {
                            if (!getSupportActionBar().isShowing()) {
                                getSupportActionBar().show();
                            }
                        }
                        AnnoucementFragment annoucementFragment = new AnnoucementFragment();
                        FragmentTransaction transactionTwo = getFragmentManager().beginTransaction();
                        transactionTwo.replace(R.id.HomeNetFeedContentView, annoucementFragment, null);
                        transactionTwo.addToBackStack(null);
                        transactionTwo.commit();
                        break;
                    case R.id.PhotosTab:
                        if (getSupportActionBar() != null) {
                            if (!getSupportActionBar().isShowing()) {
                                getSupportActionBar().show();
                            }
                        }
                        PhotoGalleryFragment fragment = new PhotoGalleryFragment();
                        FragmentTransaction transactionLate = getFragmentManager().beginTransaction();
                        transactionLate.replace(R.id.HomeNetFeedContentView, fragment, null);
                        transactionLate.addToBackStack(null);
                        transactionLate.commit();
                        break;
                    case R.id.YourFeedTab:
                        if (getSupportActionBar() != null) {
                            if (!getSupportActionBar().isShowing()) {
                                getSupportActionBar().show();
                            }
                        }
                        FeedFragment feedFragment = new FeedFragment();
                        FragmentTransaction transactionThree = getFragmentManager().beginTransaction();
                        transactionThree.replace(R.id.HomeNetFeedContentView, feedFragment, null);
                        transactionThree.addToBackStack(null);
                        transactionThree.commit();


                        break;

                }
            }
        });

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        appToolbar = (Toolbar) findViewById(R.id.HomeNetFeedToolbar);
        toolbarTextView = (TextView) findViewById(R.id.HomeNetFeedToolbarTextView);
        navigationView = (NavigationView) findViewById(R.id.HomeNetFeedNavigationMenu);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.SearchOption:
                        if (!getSupportActionBar().isShowing()){
                            getSupportActionBar().show();
                        }
                        drawerLayout.closeDrawers();
                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                        transaction.replace(R.id.HomeNetFeedContentView, new SearchHousesFragment(), null);
                        transaction.addToBackStack(null);
                        transaction.commit();
                        break;
                    case R.id.AnnouncementsOption:
                        drawerLayout.closeDrawers();
                        if (!getSupportActionBar().isShowing()){
                            getSupportActionBar().show();
                        }
                        AnnoucementFragment annoucementFragment = new AnnoucementFragment();
                        FragmentTransaction transactionTwo = getFragmentManager().beginTransaction();
                        transactionTwo.replace(R.id.HomeNetFeedContentView, annoucementFragment, null);
                        transactionTwo.addToBackStack(null);
                        transactionTwo.commit();
                        break;
                    case R.id.ViewProfileOption:
                        drawerLayout.closeDrawers();
                        getSupportActionBar().hide();
                        HomeNetProfileFragment profileFragment = new HomeNetProfileFragment();
                        FragmentTransaction third = getFragmentManager().beginTransaction();
                        third.replace(R.id.HomeNetFeedContentView, profileFragment, null);
                        third.addToBackStack(null);
                        third.commit();

                        break;
                    case R.id.EditProfileOption:
                        drawerLayout.closeDrawers();
                        getSupportActionBar().hide();
                        EditProfileFragment editProfileFragment = new EditProfileFragment();
                        FragmentTransaction forth = getFragmentManager().beginTransaction();
                        forth.replace(R.id.HomeNetFeedContentView, editProfileFragment, null);
                        forth.addToBackStack(null);
                        forth.commit();
                        break;
                    case R.id.NewHouseOption:
                        drawerLayout.closeDrawers();
                        if (!getSupportActionBar().isShowing()){
                            getSupportActionBar().show();
                        }
                        NewHouseFragment newHouseFragment = new NewHouseFragment();
                        FragmentTransaction fifth = getFragmentManager().beginTransaction();
                        fifth.replace(R.id.HomeNetFeedContentView, newHouseFragment, null);
                        fifth.addToBackStack(null);
                        fifth.commit();
                        break;
                    case R.id.MyHousesOption:
                        drawerLayout.closeDrawers();
                        if (!getSupportActionBar().isShowing()){
                            getSupportActionBar().show();
                        }
                        MyHousesFragment myHousesFragment = new MyHousesFragment();
                        FragmentTransaction sixth = getFragmentManager().beginTransaction();
                        sixth.replace(R.id.HomeNetFeedContentView, myHousesFragment, null);
                        sixth.addToBackStack(null);
                        sixth.commit();
                        break;
                    case R.id.SettingsOption:
                        drawerLayout.closeDrawers();
                        Intent settingsIntent = new Intent(HomeNetFeedActivity.this, ApplicationSettingsActivity.class);
                        startActivity(settingsIntent);
                        break;
                    case R.id.HomeManager:
                        drawerLayout.closeDrawers();
                        checkHouseOwnership();
                        break;
                }
                return true;
            }
        });
        drawerLayout = (DrawerLayout) findViewById(R.id.FeedDrawerLayout);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, appToolbar, R.string.open_drawer, R.string.close_drawer);
        drawerToggle.setDrawerIndicatorEnabled(true);
        drawerLayout.setDrawerListener(drawerToggle);
        drawerToggle.syncState();
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
        if (item.getItemId() == android.R.id.home) {
            if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
                drawerLayout.closeDrawer(Gravity.LEFT);
            } else {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        }


        return super.onOptionsItemSelected(item);

    }

    private void checkHouseOwnership() {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Checking house ownership status. Please wait...");
        dialog.setCancelable(false);
        dialog.show();
        Call<ListResponse<House>> houseCall = service.getUserHouses("Bearer "+sharedPreferences.getString("authorization_token", ""), sharedPreferences.getString("emailAddress", ""), getResources().getString(R.string.homenet_client_string));
        houseCall.enqueue(new Callback<ListResponse<House>>() {
            @Override
            public void onResponse(Call<ListResponse<House>> call, Response<ListResponse<House>> response) {
                if (dialog.isShowing()) {
                    dialog.cancel();
                }
                if (response.isSuccessful()) {
                    if (response.body().getModel() != null) {
                        List<House> ownedHouses = response.body().getModel();
                        if (ownedHouses != null) {
                            if (ownedHouses.size() > 0){
                                Intent toManager = new Intent(HomeNetFeedActivity.this, HomeManagerActivity.class);
                                startActivity(toManager);
                            } else {
                                displayMessage("Unauthorized", "You do not own any houses. To manage houses, please create a house, or own a house", null);
                            }
                        } else {
                            displayMessage("Unauthorized", "You do not own any houses. To manage houses, please create a house, or own a house", null);
                        }

                    } else {
                        displayMessage("Unauthorized", "You do not own any houses. To manage houses, please create a house, or own a house", null);
                    }
                } else {
                    try {
                        displayMessage("Error Checking Ownership", response.errorBody().string(), null);
                    } catch (Exception error) {

                    }
                }
            }

            @Override
            public void onFailure(Call<ListResponse<House>> call, Throwable t) {
                if (dialog.isShowing()) {
                    dialog.cancel();
                }
                displayMessage("Critical Error", t.getMessage(), null);

            }
        });


    }

    private void loadFeedFragment() {
        FeedFragment feedFragment = new FeedFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.HomeNetFeedContentView, feedFragment, null);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (!getSupportActionBar().isShowing()) {
            getSupportActionBar().show();
        }
    }

    private void displayMessage(String title, String message, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder messageBox = new AlertDialog.Builder(this);
        messageBox.setTitle(title).setMessage(message).setPositiveButton("Okay", listener);
        messageBox.show();
    }
}
