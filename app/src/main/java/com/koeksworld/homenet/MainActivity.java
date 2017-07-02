package com.koeksworld.homenet;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.FragmentTransaction;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;

import java.util.ArrayList;
import java.util.List;

import Data.DatabaseHelper;
import DialogFragments.AboutApplicationDialog;
import Fragments.LoginFragment;
import Fragments.LoginInformationFragment;
import Models.Country;
import Models.Key;
import Models.User;
import Utilities.DeviceUtils;
import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static String TWITTER_KEY;
    private static String TWITTER_SECRET ;
    private Toolbar mainActivityToolbar;
    private TextView toolbarTextView;
    private boolean isTablet, isLandscape;
    private CallbackManager fbCallbackManager;
    private LoginManager fbLoginManager;
    private TwitterAuthClient twitterClient;
    private DeviceUtils deviceUtils;
    private List<Country> countryList;
    private final User partialUser = new User();
    private DatabaseHelper databaseHelper;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Fragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getData();
        setSocialMedia();
        mainActivityToolbar = (Toolbar) findViewById(R.id.MainActivityToolbar);
        toolbarTextView = (TextView) findViewById(R.id.MainActivityToolbarTextView);
        toolbarTextView.setText("Log In To Continue");
        setSupportActionBar(mainActivityToolbar);
        loadLoginFragment();
    }

    private void getData() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sharedPreferences.edit();
        databaseHelper = new DatabaseHelper(this);
        countryList = databaseHelper.getCountries();
        loadKeys();
    }

    private void loadKeys() {
        List<Key> keysList = new ArrayList<>();
        keysList = databaseHelper.getKeys();
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
    private void setSocialMedia() {
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        FacebookSdk.sdkInitialize(this);
        fbCallbackManager = CallbackManager.Factory.create();
        fbLoginManager = LoginManager.getInstance();
        twitterClient = new TwitterAuthClient();
    }

    private void loadLoginFragment(){
        deviceUtils = new DeviceUtils(this);
        isTablet = deviceUtils.isTablet();
        if (isTablet) {
            toolbarTextView.setText("Log In To Continue");
            isLandscape = deviceUtils.isLandscape();
            if (isLandscape) {
                LoginFragment loginFragment = new LoginFragment();
                LoginInformationFragment loginInformationFragment = new LoginInformationFragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction loginTransaction = fragmentManager.beginTransaction();
                loginTransaction.replace(R.id.MainActivityContentViewTabletOne, loginFragment);
                loginTransaction.addToBackStack(null);
                loginTransaction.commit();

                FragmentTransaction informationTransaction = fragmentManager.beginTransaction();
                informationTransaction.replace(R.id.MainActivityContentViewTabletTwo, loginInformationFragment);
                informationTransaction.addToBackStack(null);
                informationTransaction.commit();
            } else {
                LoginFragment loginFragment = new LoginFragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.MainActivityContentViewTabletLand, loginFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        } else {
            FragmentManager fragmentManager = getFragmentManager();
            LoginFragment loginFragment = new LoginFragment();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.MainActivityContentViewMobile, loginFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.app_setttings_menu_1, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        fbCallbackManager.onActivityResult(requestCode, resultCode, data);
        twitterClient.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            /*case R.id.LoginWithFacebookOption:
                fbLoginManager.logInWithReadPermissions(this, Arrays.asList("public_profile", "email"));
                final JSONObject[] obj = new JSONObject[1];
                final Bundle params = new Bundle();
                params.putString("fields", "gender,email");
                fbLoginManager.registerCallback(fbCallbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {

                        Profile userProfile = Profile.getCurrentProfile();
                        if (userProfile == null) {
                            displaySnackBar("Error getting Facebook data. Please try again");
                            return;
                        }
                        partialUser.setName(userProfile.getFirstName());
                        partialUser.setSurname(userProfile.getLastName());
                        GraphRequest graphRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                try {
                                    //Email data cannot be shared if the app has not been approved by Facebook
                                    //partialUser.setEmailAddress(object.getString("email"));
                                    Intent socialIntent = new Intent(getApplicationContext(), SocialMediaSignupActivity.class);
                                    Bundle newBundle = new Bundle();
                                    newBundle.putParcelable("User", partialUser);
                                    newBundle.putParcelableArrayList("Countries", new ArrayList<>(countryList));
                                    newBundle.putInt("social_network", 0);
                                    socialIntent.putExtra("SocialMediaBundle", socialIntent);
                                    startActivity(socialIntent);
                                    overridePendingTransition(0,0);
                                } catch (Exception error) {
                                    displaySnackBar("Error Getting Facebook Data");
                                }
                            }
                        });
                        params.putString("fields", "id,name,email,gender,locale,age_range,timezone");
                        graphRequest.setParameters(params);
                        graphRequest.executeAsync();
                    }
                    @Override
                    public void onCancel() {
                        displaySnackBar("Login Cancelled!");

                    }

                    @Override
                    public void onError(FacebookException error) {
                        displaySnackBar("Error Logging into Facebook");
                        FirebaseCrash.log(error.getMessage());
                    }
                });


                break;
            case R.id.LoginWithTwitterOption:
                //Source: http://stackoverflow.com/questions/27867826/android-fabric-twittercore-login-without-twitterloginbutton
                final User twitterUser = new User();
                twitterClient.authorize(this, new Callback<TwitterSession>() {
                    @Override
                    public void success(Result<TwitterSession> result) {
                        twitterUser.setId(0);
                        twitterUser.setUserName(result.data.getUserName());
                        TwitterAuthToken token = result.data.getAuthToken();
                        TwitterApiClient client = new TwitterApiClient(Twitter.getSessionManager().getActiveSession());
                        Call<com.twitter.sdk.android.core.models.User> userCall = client.getAccountService().verifyCredentials(true, false);
                        userCall.enqueue(new Callback<com.twitter.sdk.android.core.models.User>() {
                            @Override
                            public void success(Result<com.twitter.sdk.android.core.models.User> result) {
                                twitterUser.setName(result.data.name);
                                twitterUser.setEmail(result.data.email);
                                twitterUser.setUserName(result.data.screenName);
                                //Other cool things we may want to pass to the app
                                Bundle twitterBundle = new Bundle();
                                twitterBundle.putString("TotalFollowers", Integer.toString(result.data.followersCount));
                                twitterBundle.putString("ProfilePictureURL", result.data.profileImageUrlHttps);
                                twitterBundle.putString("Location", result.data.location);
                                twitterBundle.putParcelableArrayList("Countries", new ArrayList<>(countryList));
                                twitterBundle.putParcelable("PartialUser", twitterUser);
                                twitterBundle.putInt("social_network", 1);

                                Intent socialIntent = new Intent(getApplicationContext(), SocialMediaSignupActivity.class);
                                socialIntent.putExtra("SocialMediaBundle", twitterBundle);
                                startActivity(socialIntent);
                                overridePendingTransition(0,0);
                            }

                            @Override
                            public void failure(TwitterException exception) {
                                displaySnackBar(exception.getMessage());
                            }
                        });
                    }

                    @Override
                    public void failure(TwitterException exception) {
                        displaySnackBar("Error Connecting to Twitter");
                    }
       });
                break;*/
            case R.id.AppAboutApplicationOption:
                AboutApplicationDialog dialog = new AboutApplicationDialog();
                dialog.show(getFragmentManager(), null);
                break;
            case R.id.ForgotPasswordOption:
                Intent newIntent = new Intent(this, ForgotPasswordActivity.class);
                startActivity(newIntent);
                overridePendingTransition(0,0);
                break;
            case R.id.AppPreferencesOption:
                Intent settingsIntent = new Intent(this, ApplicationSettingsActivity.class);
                startActivity(settingsIntent);

                /*if (deviceUtils.isTablet()) {
                    if (deviceUtils.isLandscape()) {
                        FragmentTransaction rightTransaction = getFragmentManager().beginTransaction();
                        rightTransaction.replace(R.id.MainActivityContentViewTabletOne, new AppSettings(), "AppSettings");
                        rightTransaction.addToBackStack("AppSettings");
                        rightTransaction.commit();
                    } else {
                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                        transaction.replace(R.id.MainActivityContentViewTabletLand, new AppSettings(), "AppSettings");
                        transaction.addToBackStack("AppSettings");
                        transaction.commit();
                    }
                } else {
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.MainActivityContentViewMobile, new AppSettings(), null);
                    transaction.addToBackStack("AppSettings");
                    transaction.commit();
                }*/

        }
        return true;

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("toolbar", toolbarTextView.getText().toString());
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(0,0);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (getFragmentManager().getBackStackEntryCount() == 0) {
            finish();
        }

    }
}
