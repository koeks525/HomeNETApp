package Tasks;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;

import com.koeksworld.homenet.R;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import Adapters.BannedUsersAdapter;
import Adapters.HouseUsersAdapter;
import Adapters.PendingUsersAdapter;
import Communication.HomeNetService;
import Models.House;
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
 * Created by Okuhle on 2017/06/22.
 */

public class ManageUsersTask extends AsyncTask<Integer, Integer, Integer> {

    private List<HouseMember> activeUserList;
    private List<HouseMember> bannedUserList;
    private List<HouseMember> pendingUserList;
    private Retrofit retrofit;
    private HomeNetService service;
    private OkHttpClient client;
    private Activity currentActivity;
    private SharedPreferences sharedPreferences;
    private ArrayList<Protocol> protocolList = new ArrayList<>();
    private ProgressDialog progressDialog;
    private RecyclerView recyclerView;
    private House selectedHouse;
    private String errorInformation = "";
    private ArrayList<User> userList = new ArrayList<>();
    private int operationMode =0;

    public ManageUsersTask(Activity currentActivity, RecyclerView recyclerView, House selectedHouse, int operationMode) {
        protocolList.add(Protocol.HTTP_1_1);
        this.currentActivity = currentActivity;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(currentActivity);
        activeUserList = new ArrayList<>();
        bannedUserList = new ArrayList<>();
        pendingUserList = new ArrayList<>();
        client = new OkHttpClient.Builder().readTimeout(2, TimeUnit.MINUTES).connectTimeout(2, TimeUnit.MINUTES).protocols(protocolList).build();
        retrofit = new Retrofit.Builder().baseUrl(currentActivity.getResources().getString(R.string.homenet_link)).client(client).addConverterFactory(GsonConverterFactory.create()).build();
        service = retrofit.create(HomeNetService.class);
        this.recyclerView = recyclerView;
        this.selectedHouse = selectedHouse;
        this.operationMode = operationMode;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(currentActivity);
        progressDialog.setMessage("Fetching House user data. Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    //1 - active users, 2 - pending users, 3 - banned users
    @Override
    protected Integer doInBackground(Integer... integers) {
        try {
            switch (operationMode) {
                case 1:
                    Response<ListResponse<HouseMember>> houseCall = service.getActiveHouseMembers("Bearer "+sharedPreferences.getString("authorization_token",""), selectedHouse.getHouseID(), currentActivity.getResources().getString(R.string.homenet_client_string)).execute();
                    if (houseCall.isSuccessful()) {
                        if (houseCall.body().getMessage() != null) {
                            activeUserList = houseCall.body().getModel();
                        }
                    } else {
                        errorInformation += houseCall.errorBody().string();
                    }
                    if (activeUserList != null) {
                        for (HouseMember membership : activeUserList) {
                            Response<SingleResponse<User>> userResponse = service.getUserById("Bearer "+sharedPreferences.getString("authorization_token", ""), membership.getUserId(), currentActivity.getString(R.string.homenet_client_string)).execute();
                            if (userResponse.isSuccessful()) {
                               if (userResponse.body().getModel() != null) {
                                   userList.add(userResponse.body().getModel());
                               }
                            } else {
                                errorInformation += userResponse.errorBody().string();
                            }
                        }
                    }
                    break;
                case 2:
                    Response<ListResponse<HouseMember>> nextHouseCall = service.getPendingHouseUsers("Bearer "+sharedPreferences.getString("authorization_token",""), selectedHouse.getHouseID(), currentActivity.getResources().getString(R.string.homenet_client_string)).execute();
                    if (nextHouseCall.isSuccessful()) {
                        if (nextHouseCall.body().getMessage() != null) {
                            pendingUserList = nextHouseCall.body().getModel();
                        }
                    } else {
                        errorInformation += nextHouseCall.errorBody().string();
                    }
                    if (pendingUserList != null) {
                        for (HouseMember membership : pendingUserList) {
                            Response<SingleResponse<User>> userResponse = service.getUserById("Bearer "+sharedPreferences.getString("authorization_token", ""), membership.getUserId(), currentActivity.getString(R.string.homenet_client_string)).execute();
                            if (userResponse.isSuccessful()) {
                                if (userResponse.body().getModel() != null) {
                                    userList.add(userResponse.body().getModel());
                                }
                            } else {
                                errorInformation += userResponse.errorBody().string();
                            }
                        }
                    }
                    break;
                case 3:
                    Response<ListResponse<HouseMember>> bannedHouseCall = service.getBannedHouseUsers("Bearer "+sharedPreferences.getString("authorization_token",""), selectedHouse.getHouseID(), currentActivity.getResources().getString(R.string.homenet_client_string)).execute();
                    if (bannedHouseCall.isSuccessful()) {
                        if (bannedHouseCall.body().getMessage() != null) {
                            bannedUserList = bannedHouseCall.body().getModel();
                        }
                    } else {
                        errorInformation += bannedHouseCall.errorBody().string();
                    }
                    if (bannedUserList != null) {
                        for (HouseMember membership : bannedUserList) {
                            Response<SingleResponse<User>> userResponse = service.getUserById("Bearer "+sharedPreferences.getString("authorization_token", ""), membership.getUserId(), currentActivity.getString(R.string.homenet_client_string)).execute();
                            if (userResponse.isSuccessful()) {
                                if (userResponse.body().getModel() != null) {
                                    userList.add(userResponse.body().getModel());
                                }
                            } else {
                                errorInformation += userResponse.errorBody().string();
                            }
                        }
                    }
                    break;

            }
            return 1;

        } catch (Exception error) {
            displayMessage("Error Fetching Data", error.getMessage(), new DialogInterface.OnClickListener() {
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
        if (userList.size() > 0) {
            switch (operationMode) {
                case 1:
                    HouseUsersAdapter adapter = new HouseUsersAdapter(userList);
                    recyclerView.setAdapter(adapter);
                    break;
                case 2:
                    PendingUsersAdapter pendingAdapter = new PendingUsersAdapter(userList);
                    recyclerView.setAdapter(pendingAdapter);
                    break;
                case 3:
                    BannedUsersAdapter bannedAdapter = new BannedUsersAdapter(userList);
                    recyclerView.setAdapter(bannedAdapter);
                    break;
            }
        } else {
            Snackbar.make(recyclerView, "No users found!", Snackbar.LENGTH_LONG).show();
        }

    }

    private void displayMessage(final String title, final String message, final DialogInterface.OnClickListener listener) {
        currentActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(currentActivity);
                builder.setTitle(title).setMessage(message).setPositiveButton("Okay", listener).setCancelable(false).show();
            }
        });
    }
}
