package Tasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;

import com.koeksworld.homenet.LandingActivity;
import com.koeksworld.homenet.MainActivity;
import com.koeksworld.homenet.R;
import com.koeksworld.homenet.WelcomeActivity;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import Communication.HomeNetService;
import Data.RealmHelper;
import Models.Category;
import Models.Country;
import Models.Key;
import Models.LoginViewModel;
import Models.Token;
import Models.User;
import ResponseModels.ListResponse;
import ResponseModels.SingleResponse;
import io.fabric.sdk.android.Fabric;
import io.reactivex.internal.disposables.ListCompositeDisposable;
import io.realm.RealmConfiguration;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.facebook.FacebookSdk.getApplicationContext;
import static com.facebook.FacebookSdk.removeLoggingBehavior;

/**
 * Created by Okuhle on 2017/04/09.
 */

public class AppSetupTask extends AsyncTask<Integer, Integer, Integer> {

    private String TWITTER_KEY = "";
    private Activity currentActivity;
    private Snackbar snackBar;
    private Retrofit retrofit;
    private HomeNetService homeNetService;
    private List<Country> countryList;
    private List<Key> keyList;
    private RealmHelper realmHelper;
    private OkHttpClient client;
    private List<Protocol> protocolList = new ArrayList<>();
    private ProgressDialog bar;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private String TWITTER_SECRET = "";
    private List<Category> categoryList;

    public AppSetupTask(Activity currentActivity) {
        this.currentActivity = currentActivity;
        protocolList.add(Protocol.HTTP_1_1);
        client = new OkHttpClient.Builder().readTimeout(2, TimeUnit.MINUTES).connectTimeout(2, TimeUnit.MINUTES).protocols(protocolList).build();
        retrofit = new Retrofit.Builder().baseUrl(currentActivity.getResources().getString(R.string.homenet_link)).addConverterFactory(GsonConverterFactory.create()).client(client).build();
        homeNetService = retrofit.create(HomeNetService.class);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(currentActivity);
        editor = sharedPreferences.edit();
        countryList = new ArrayList<>();
        keyList = new ArrayList<>();
        realmHelper = new RealmHelper();
        categoryList = new ArrayList<>();
    }

    @Override
    protected void onPreExecute() {
        if (!realmHelper.exists()) {
            bar = new ProgressDialog(currentActivity);
            bar.setCancelable(false);
            bar.setMessage("Setting Up. Please wait...");
            bar.show();
        } else {
            loadKeys();
            checkKeys();
        }

    }

    @Override
    protected Integer doInBackground(Integer... params) {
        try {
           Response<ListResponse<Country>> countryCall = homeNetService.getCountries(currentActivity.getResources().getString(R.string.homenet_client_string)).execute();
            if (countryCall.isSuccessful()) {
                for (Country thisCountry : countryCall.body().getModel()) {
                    countryList.add(thisCountry);

                }
            }

            Response<ListResponse<Key>> keyCall = homeNetService.getKeys(currentActivity.getResources().getString(R.string.homenet_client_string)).execute();
            if (keyCall.isSuccessful()) {
                for (Key thisKey : keyCall.body().getModel()) {
                    keyList.add(thisKey);

                }
            }
            Response<ListResponse<Category>> categoryCall = homeNetService.getCategories(currentActivity.getResources().getString(R.string.homenet_client_string)).execute();
            if (categoryCall.isSuccessful()) {
                for (Category thisOne : categoryCall.body().getModel()) {
                    categoryList.add(thisOne);
                }
            }
        } catch (Exception error) {
            if (bar.isShowing()) {
                bar.cancel();
            }
        }
        return -1;
    }

    @Override
    protected void onPostExecute(Integer b) {
        if (bar != null ) {
            if (bar.isShowing()) {
                bar.cancel();

            }
        }
        if (!realmHelper.exists()) {
            if (keyList.size() > 0 && countryList.size() > 0 && categoryList.size() > 0) {
                realmHelper.addKeys(keyList);
                realmHelper.addCountries(countryList);
                realmHelper.addCategories(categoryList);
                loadKeys();
                checkKeys();
            } else {
                displayMessage("No Keys Found", "No keys were found. Try again later", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        System.exit(0);
                    }
                });
            }
        }


    }

    private void displayMessage(final String title, final String message, final DialogInterface.OnClickListener listener) {
        currentActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder messageBox = new AlertDialog.Builder(currentActivity);
                messageBox.setTitle(title).setMessage(message).setCancelable(false).setPositiveButton("Ok", listener).show();
            }
        });
    }

    private void checkKeys() {
        if (TWITTER_KEY != null && TWITTER_SECRET != null) {
            TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
            Fabric.with(currentActivity, new Twitter(authConfig));
            if (sharedPreferences.getBoolean("RememberCredentialsOption", false)) {
                if (sharedPreferences.getString("username", "") != "" && sharedPreferences.getString("password", "") != "") {
                    if (sharedPreferences.getString("authorization_token", "") != "") {
                        Call<SingleResponse<User>> userCall = homeNetService.loginUser("Bearer "+sharedPreferences.getString("authorization_token", ""), new LoginViewModel(sharedPreferences.getString("username", ""), sharedPreferences.getString("password", "")), currentActivity.getResources().getString(R.string.homenet_client_string));
                        userCall.enqueue(new Callback<SingleResponse<User>>() {
                            @Override
                            public void onResponse(Call<SingleResponse<User>> call, Response<SingleResponse<User>> response) {
                                if (response.code() == 200) {
                                    //refresh the login data
                                    editor.putInt("userID", response.body().getModel().getId());
                                    editor.putString("username", response.body().getModel().getUserName());
                                    editor.putString("password", response.body().getModel().getPassword());
                                    editor.putString("name", response.body().getModel().getName());
                                    editor.putString("surname", response.body().getModel().getSurname());
                                    editor.putString("emailAddress", response.body().getModel().getEmail());
                                    editor.commit();
                                    //load the landing intent
                                    loadLandingScreen();
                                } else if (response.code() == 401 || response.code() == 403) {
                                    //The token may have expired, or the user may not have a token
                                    getTokenThenLogin();
                                }
                            }

                            @Override
                            public void onFailure(Call<SingleResponse<User>> call, Throwable t) {
                                displayMessage("Error Validating Credentials", t.getMessage(), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        System.exit(0);
                                    }
                                });
                            }
                        });

                    } else {
                        //try and generate the login token then login
                        getTokenThenLogin();
                    }
                } else {
                    //No login credentials stored - so go to login screen
                    loadLoginScreen();
                }
            } else {
                loadLoginScreen();
            }
        } else {
            displayMessage("Error Getting Keys", "No keys were found on the system", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    System.exit(0);
                }
            });
        }
    }

    private void loadKeys() {
        for (Key thiskey : keyList) {
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

    private void loadLoginScreen() {
        Intent loginIntent = new Intent(getApplicationContext(), MainActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        currentActivity.startActivity(loginIntent);
        currentActivity.overridePendingTransition(0, 0);
        currentActivity.finish();
    }

    private void getTokenThenLogin() {
        final LoginViewModel model = new LoginViewModel(sharedPreferences.getString("username", ""), sharedPreferences.getString("password", ""));
        Call<SingleResponse<Token>> tokenCall = homeNetService.createToken(model, currentActivity.getResources().getString(R.string.homenet_client_string));
        tokenCall.enqueue(new Callback<SingleResponse<Token>>() {
            @Override
            public void onResponse(Call<SingleResponse<Token>> call, Response<SingleResponse<Token>> response) {
                if (response.code() == 200) {
                    editor.putString("authorization_token", response.body().getModel().getTokenHandler());
                    editor.putString("expiry_date", response.body().getModel().getDateExpires());
                    editor.commit();
                    editor.putString("username", model.getUsername());
                    editor.putString("password", model.getPassword());
                    editor.commit();
                    //The user may have many houses - so take them to the landing screen
                    loadLandingScreen();
                } else {
                    editor.putString("username", "");
                    editor.putString("password", "");
                    editor.commit();
                    displayMessage("Error Validating Credentials", "Credentials stored on the system are not valid. As a result, these will be cleared, and the system will exit", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            System.exit(0);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<SingleResponse<Token>> call, Throwable t) {
                displayMessage("Error Validating Login Credentials", t.getMessage(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        System.exit(0);
                    }
                });
            }
        });
    }

    private void loadLandingScreen(){
        Intent newIntent = new Intent(currentActivity, WelcomeActivity.class);
        currentActivity.startActivity(newIntent);
        currentActivity.finish();

    }

}

