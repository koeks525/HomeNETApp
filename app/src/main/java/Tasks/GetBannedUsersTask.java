package Tasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.annotation.RestrictTo;
import android.support.v7.app.AlertDialog;
import android.widget.ProgressBar;

import com.koeksworld.homenet.R;

import java.sql.Time;
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

public class GetBannedUsersTask extends AsyncTask<Integer, Integer, Integer> {

    private Retrofit retrofit;
    private HomeNetService service;
    private OkHttpClient client;
    private SharedPreferences sharedPreferences;
    private Activity currentActivity;
    private List<HouseMember> bannedMemberList = new ArrayList<>();
    private List<User> bannedUserList = new ArrayList<>();
    private List<Protocol> protocolList = new ArrayList<>();
    private ProgressDialog progressDialog;
    private int houseId;

    public GetBannedUsersTask(Activity currentActivity, int houseId) {
        this.currentActivity = currentActivity;
        protocolList.add(Protocol.HTTP_1_1);
        client = new OkHttpClient.Builder().readTimeout(2, TimeUnit.MINUTES).connectTimeout(2, TimeUnit.MINUTES).protocols(protocolList).build();
        retrofit = new Retrofit.Builder().baseUrl(currentActivity.getResources().getString(R.string.homenet_link)).addConverterFactory(GsonConverterFactory.create()).client(client).build();
        service = retrofit.create(HomeNetService.class);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(currentActivity);
        this.currentActivity = currentActivity;
        this.houseId = houseId;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(currentActivity);
        progressDialog.setMessage("Searching for banned user data. Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }
    @Override
    protected Integer doInBackground(Integer... integers) {
        try {
            Response<ListResponse<HouseMember>> bannedCall = service.getBannedHouseUsers("Bearer "+sharedPreferences.getString("authorization_code", ""), houseId, currentActivity.getResources().getString(R.string.homenet_client_string)).execute();
            if (bannedCall.isSuccessful()) {
                if (bannedCall.body().getModel() != null) {
                    for (HouseMember member : bannedCall.body().getModel()) {
                        bannedMemberList.add(member);
                    }
                }
                if (bannedMemberList.size() > 0) {
                    for (HouseMember bannedMember : bannedMemberList) {
                        Response<SingleResponse<User>> userCall = service.getUserById("Bearer: "+sharedPreferences.getString("authorization_token",""), bannedMember.getUserId(), currentActivity.getResources().getString(R.string.homenet_client_string)).execute();
                        if (userCall.isSuccessful()) {
                            if (userCall.body().getModel() != null) {
                                bannedUserList.add(userCall.body().getModel());
                            }
                        }
                    }
                }
            }

        } catch (Exception error) {
            displayMessage("Error Getting Banned Users", error.getMessage(), new DialogInterface.OnClickListener() {
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

        if (bannedMemberList.size() == 0) {
            displayMessage("No Banned Members", "No banned members were found for the given data", null);
        } else {
            //Something must be done here to process found data
        }

    }

    private void displayMessage(final String title, final String message, final DialogInterface.OnClickListener listener) {
        currentActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(currentActivity);
                builder.setTitle(title).setMessage(message).setPositiveButton("Ok", listener).setCancelable(false).show();
            }
        });
    }
}
