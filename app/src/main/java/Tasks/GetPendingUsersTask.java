package Tasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;

import com.koeksworld.homenet.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import Communication.HomeNetService;
import Models.HouseMember;
import Models.User;
import ResponseModels.ListResponse;
import ResponseModels.SingleResponse;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Okuhle on 2017/06/23.
 */

public class GetPendingUsersTask extends AsyncTask<Integer, Integer, Integer> {

    private Retrofit retrofit;
    private Activity currentActivity;
    private HomeNetService service;
    private List<HouseMember> pendingUserList = new ArrayList<>();
    private List<User> userList = new ArrayList<>();
    private SharedPreferences sharedPreferences;
    private int houseID;
    private OkHttpClient client;
    private List<Protocol> protocolList = new ArrayList<>();
    private ProgressDialog progressDialog;

    public GetPendingUsersTask(Activity currentActivity, int houseID) {
        this.houseID = houseID;
        this.currentActivity = currentActivity;
        protocolList.add(Protocol.HTTP_1_1);
        client = new OkHttpClient.Builder().readTimeout(2, TimeUnit.MINUTES).connectTimeout(2, TimeUnit.MINUTES).protocols(protocolList).build();
        retrofit = new Retrofit.Builder().baseUrl(currentActivity.getResources().getString(R.string.homenet_link)).addConverterFactory(GsonConverterFactory.create()).client(client).build();
        service = retrofit.create(HomeNetService.class);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(currentActivity);
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(currentActivity);
        progressDialog.setMessage("Searching for pending users. Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    protected Integer doInBackground(Integer... integers) {
        try {
            Response<ListResponse<HouseMember>> memberCall = service.getPendingHouseUsers("Bearer "+sharedPreferences.getString("authorization_token", ""), houseID, currentActivity.getResources().getString(R.string.homenet_client_string)).execute();
            if (memberCall.isSuccessful()) {
                if (memberCall.body().getModel() != null) {
                    for (HouseMember member : memberCall.body().getModel()) {
                        pendingUserList.add(member);
                    }
                }

                if (pendingUserList.size() == 0) {
                    return null;
                }

                for (HouseMember member : pendingUserList) {
                    Response<SingleResponse<User>> userCall = service.getUserById("Bearer "+sharedPreferences.getString("authorization_token", ""), member.getUserId(), currentActivity.getResources().getString(R.string.homenet_client_string)).execute();
                    if (userCall.isSuccessful()) {
                        if (userCall.body().getModel() != null)
                            userList.add(userCall.body().getModel());
                    }
                }
            }
            return null;
        } catch (Exception error) {
            if (progressDialog.isShowing()) {
                progressDialog.cancel();
            }
            displayMessage("Error Getting Pending Users", error.getMessage(), new DialogInterface.OnClickListener() {
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
        if (pendingUserList.size() == 0) {
            displayMessage("No Pending Users", "No pending users were found on the system for the house", null);
        }
        //Further processing is needed
        }

    private void displayMessage(final String title, final String message, final DialogInterface.OnClickListener listener) {
        currentActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(currentActivity);
                builder.setTitle(title).setMessage(message).setCancelable(false).setPositiveButton("Ok", listener).show();
            }
        });
    }
}
