package Tasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.widget.ProgressBar;

import com.koeksworld.homenet.MainActivity;
import com.koeksworld.homenet.R;
import com.squareup.picasso.Downloader;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import Communication.HomeNetService;
import Data.DatabaseHelper;
import Models.Country;
import Models.Key;
import ResponseModels.ListResponse;
import ResponseModels.SingleResponse;
import io.fabric.sdk.android.Fabric;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.facebook.FacebookSdk.getApplicationContext;

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
    private DatabaseHelper databaseHelper;
    private OkHttpClient client;
    private List<Protocol> protocolList = new ArrayList<>();
    private ProgressDialog bar;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private String TWITTER_SECRET = "";

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
        databaseHelper = new DatabaseHelper(currentActivity);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        if (!DatabaseHelper.checkDatabase(currentActivity, "HomeNET_DB")) {
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
        } catch (Exception error) {
            if (bar.isShowing()) {
                bar.cancel();
            }
        }
        return -1;
    }

    @Override
    protected void onPostExecute(Integer b) {
        if (bar.isShowing()) {
            bar.cancel();
        }

        if (keyList.size() > 0 && countryList.size() > 0) {
            databaseHelper.insertKeys(keyList);
            databaseHelper.insertCountries(countryList);
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
            Intent loginIntent = new Intent(getApplicationContext(), MainActivity.class);
            loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            currentActivity.startActivity(loginIntent);
            currentActivity.overridePendingTransition(0, 0);
            currentActivity.finish();
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

}

