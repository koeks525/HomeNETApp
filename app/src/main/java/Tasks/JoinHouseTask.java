package Tasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;

import com.koeksworld.homenet.HomeNetFeedActivity;
import com.koeksworld.homenet.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import Communication.HomeNetService;
import Models.House;
import Models.HouseMember;
import ResponseModels.SingleResponse;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Okuhle on 2017/06/28.
 */

public class JoinHouseTask extends AsyncTask<Integer, Integer, Integer> {

    private Retrofit retrofit;
    private SharedPreferences sharedPreferences;
    private Activity currentActivity;
    private HomeNetService service;
    private HouseMember houseMembership;
    private ProgressDialog progressDialog;
    private List<Protocol> protocolList = new ArrayList<>();
    private int houseID;
    private OkHttpClient client;
    private String emailAddress;
    private String errorInformation = "";

    public JoinHouseTask(Activity currentActivity, int houseID, String emailAddress) {
        this.houseID = houseID;
        this.currentActivity = currentActivity;
        protocolList.add(Protocol.HTTP_1_1);
        client = new OkHttpClient.Builder().readTimeout(2, TimeUnit.MINUTES).connectTimeout(2, TimeUnit.MINUTES).protocols(protocolList).build();
        retrofit = new Retrofit.Builder().baseUrl(currentActivity.getResources().getString(R.string.homenet_link)).addConverterFactory(GsonConverterFactory.create()).client(client).build();
        service = retrofit.create(HomeNetService.class);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(currentActivity);
        this.emailAddress = emailAddress;


    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(currentActivity);
        progressDialog.setMessage("Joining House... Please wait... ");
        progressDialog.show();
    }

    @Override
    protected Integer doInBackground(Integer... integers) {
        try {
            Response<SingleResponse<HouseMember>> houseMemberCall = service.joinHouse("Bearer "+sharedPreferences.getString("authorization_token", ""), houseID, emailAddress, currentActivity.getResources().getString(R.string.homenet_client_string)).execute();
            if (houseMemberCall.isSuccessful()) {
                houseMembership = houseMemberCall.body().getModel();
                return null;
            } else {
                errorInformation = houseMemberCall.errorBody().string();
            }
        } catch (Exception error) {
                if (progressDialog.isShowing()) {
                    progressDialog.cancel();
                }
                displayMessage("Error Processing House Join", error.getMessage(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        System.exit(0);
                    }
                });
        }
        return null;
    }

    @Override
    protected void onPostExecute(Integer integer) {
        super.onPostExecute(integer);
        if (progressDialog.isShowing()) {
            progressDialog.cancel();
        }
        if (houseMembership != null) {
            displayMessage("House Join Message", "You have successfully joined this house. The house administrator has received your request, and will respond shorty", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    //This must take the user to the feed screen, with all house posts being loaded.
                    Intent homeFeedIntent = new Intent(currentActivity, HomeNetFeedActivity.class);
                    currentActivity.startActivity(homeFeedIntent);
                    currentActivity.overridePendingTransition(0,0);
                    currentActivity.finish();
                }
            });
        } else {
            if (errorInformation != "") {
                displayMessage("Error Joining House", "The following error(s) occurred: \n" + errorInformation, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Close
                    }
                });
            }
        }
    }

    private void displayMessage(final String title, final String message, final DialogInterface.OnClickListener listener) {
        currentActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(currentActivity);
                builder.setTitle(title).setMessage(message).setCancelable(false).setPositiveButton("Okay", listener).show();
            }
        });
    }


}
