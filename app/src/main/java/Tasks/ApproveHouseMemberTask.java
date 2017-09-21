package Tasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;

import com.koeksworld.homenet.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import Adapters.PendingUsersAdapter;
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
 * Created by Okuhle on 2017/09/04.
 */

public class ApproveHouseMemberTask extends AsyncTask<Integer, Integer, Integer> {

    private OkHttpClient client;
    private HomeNetService service;
    private Retrofit retrofit;
    private Activity currentActivity;
    private SharedPreferences sharedPreferences;
    private List<Protocol> protocolList;
    private ProgressDialog dialog;
    private House selectedHouse;
    private String emailAddress;
    private HouseMember resultHouseMember;
    private String errorString;
    private PendingUsersAdapter adapter;
    private int adapterPosition;

    public ApproveHouseMemberTask(Activity currentActivity, House selectedHouse, String emailAddress, PendingUsersAdapter adapter, int adapterPosition) {
        this.selectedHouse = selectedHouse;
        protocolList = new ArrayList<>();
        protocolList.add(Protocol.HTTP_1_1);
        client = new OkHttpClient.Builder().connectTimeout(2, TimeUnit.MINUTES).readTimeout(2, TimeUnit.MINUTES).protocols(protocolList).build();
        retrofit = new Retrofit.Builder().baseUrl(currentActivity.getResources().getString(R.string.homenet_link)).client(client).addConverterFactory(GsonConverterFactory.create()).build();
        service = retrofit.create(HomeNetService.class);
        this.currentActivity = currentActivity;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(currentActivity);
        this.emailAddress = emailAddress;
        this.adapter = adapter;
        this.adapterPosition = adapterPosition;
    }

    @Override
    protected void onPreExecute() {
        dialog = new ProgressDialog(currentActivity);
        dialog.setMessage("Approving house member. Please wait...");
        dialog.setCancelable(false);
        dialog.show();
    }

    @Override
    protected Integer doInBackground(Integer... integers) {
        try {
            Response<SingleResponse<HouseMember>> approveCall = service.approveHouseMember("Bearer "+sharedPreferences.getString("authorization_token",""), selectedHouse.getHouseID(), emailAddress, sharedPreferences.getString("emailAddress", ""), currentActivity.getResources().getString(R.string.homenet_client_string)).execute();
            if (approveCall.isSuccessful()) {
               resultHouseMember = approveCall.body().getModel();
                return 1;
            } else {
                errorString = approveCall.errorBody().string();
                return -1;
            }
        } catch (Exception error) {
            errorString = error.getMessage();
            return -1;
        }
    }

    @Override
    protected void onPostExecute(Integer integer) {
        if (dialog.isShowing()) {
            dialog.cancel();
        }
        if (resultHouseMember != null) {
            displayMessage("Approval Successful", "The selected house member has been approved successfully! User has been notified", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    adapter.removeUserFromList(adapterPosition);
                }
            });
        } else {
            displayMessage("Error Approving Member", "Something went wrong with approving the house member", null);
        }
    }

    private void displayMessage(String title, String message, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(currentActivity);
        builder.setTitle(title).setMessage(message).setCancelable(false).setPositiveButton("Okay", listener).show();
    }
}
