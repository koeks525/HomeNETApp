package Tasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;

import com.koeksworld.homenet.R;

import org.json.JSONObject;

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

public class GetActiveUsersTask extends AsyncTask<Void, Void, List<HouseMember>> {

    private Retrofit retrofit;
    private HomeNetService service;
    private SharedPreferences sharedPreferences;
    private List<HouseMember> activeUsersList;
    private ProgressDialog progressDialog;
    private Activity currentActivity;
    private OkHttpClient client;
    private List<Protocol> protocolList = new ArrayList<>();
    private List<User> userList = new ArrayList<>();
    private int houseId;
    String errorInformation = "";

    public GetActiveUsersTask(Activity currentActivity, int houseId) {
        this.currentActivity = currentActivity;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(currentActivity);
        protocolList.add(Protocol.HTTP_1_1);
        client = new OkHttpClient.Builder().readTimeout(2, TimeUnit.MINUTES).connectTimeout(2, TimeUnit.MINUTES).protocols(protocolList).build();
        retrofit = new Retrofit.Builder().baseUrl(currentActivity.getResources().getString(R.string.homenet_link)).client(client).addConverterFactory(GsonConverterFactory.create()).build();
        service = retrofit.create(HomeNetService.class);
        activeUsersList = new ArrayList<>();
        this.houseId = houseId;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(currentActivity);
        progressDialog.setMessage("Searching for active user data. Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    protected List<HouseMember> doInBackground(Void... voids) {
        try {
            //Get House memberships
            /*Response<ListResponse<HouseMember>> houseMemberCall = service.getActiveHouseMembers("Bearer "+sharedPreferences.getString("authorization_token", ""), houseId, currentActivity.getResources().getString(R.string.homenet_client_string)).execute();
            if (houseMemberCall.isSuccessful()) {
                if (houseMemberCall.code() == 200) {
                    ListResponse<HouseMember> memberListResponse = houseMemberCall.body();
                    if (memberListResponse != null) {
                        for (HouseMember member : houseMemberCall.body().getModel()) {
                            activeUsersList.add(member);
                        }
                    }
                } else if (houseMemberCall.code() == 400) {
                    JSONObject object = new JSONObject(houseMemberCall.errorBody().string());
                    errorInformation = object.getString("message");
                    return null;
                } else if (houseMemberCall.code() == 500) {
                    JSONObject object = new JSONObject(houseMemberCall.errorBody().string());
                    errorInformation = object.getString("message");
                    return null;
                }

                if (activeUsersList.size() > 0) {

                    for (HouseMember member : activeUsersList) {
                        Response<SingleResponse<User>> userCall = service.getUserById("Bearer " + sharedPreferences.getString("authorization_token", ""), member.getUserId(), currentActivity.getResources().getString(R.string.homenet_client_string)).execute();
                        if (userCall.isSuccessful()) {
                            if (userCall.body().getModel() != null) {
                                userList.add(userCall.body().getModel());
                            }
                        } else {
                            errorInformation = userCall.errorBody().string();
                        }
                    }
                    return null;
                } else {
                    return null; //No house members were found for the selected house
                }
            } else {
                JSONObject object = new JSONObject(houseMemberCall.errorBody().string());
                errorInformation = object.getString("message");
                return null;
            }*/
        } catch (Exception error) {
            if (progressDialog.isShowing()) {
                progressDialog.cancel();
            }
            displayMessage("Error Processing Active User List", error.getMessage(), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    System.exit(0);
                }
            });
        }
        return null;
    }

    @Override
    protected void onPostExecute(List<HouseMember> houseMembers) {
        if (progressDialog.isShowing()) {
            progressDialog.cancel();
        }

        if (activeUsersList.size() == 0) {
            displayMessage("No Active Users", "No active users were found for the selected house. Check your pending list for users", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
        } else {
            //Update recyclerview controls with list data
        }
    }

    private void displayMessage(final String title, final String message, final DialogInterface.OnClickListener listener) {
        currentActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder messageBox = new AlertDialog.Builder(currentActivity);
                messageBox.setTitle(title).setMessage(message).setPositiveButton("OK", listener).setCancelable(false).show();
            }
        });
    }
}
