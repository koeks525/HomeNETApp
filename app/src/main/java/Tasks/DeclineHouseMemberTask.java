package Tasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
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

public class DeclineHouseMemberTask extends AsyncTask<Integer, Integer, Integer> {

    private Activity currentActivity;
    private House selectedHouse;
    private OkHttpClient client;
    private List<Protocol> protocolList;
    private SharedPreferences sharedPreferences;
    private Retrofit retrofit;
    private HomeNetService service;
    private ProgressDialog dialog;
    private int adapterPosition;
    private String emailAddress, errorString;
    private HouseMember houseMember;
    private PendingUsersAdapter adapter;

    public DeclineHouseMemberTask(Activity currentActivity, House selectedHouse, int adapterPosition, String emailAddress, PendingUsersAdapter adapter) {
        this.currentActivity = currentActivity;
        this.selectedHouse = selectedHouse;
        protocolList = new ArrayList<>();
        protocolList.add(Protocol.HTTP_1_1);
        client = new OkHttpClient.Builder().connectTimeout(2, TimeUnit.MINUTES).readTimeout(2, TimeUnit.MINUTES).protocols(protocolList).build();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(currentActivity);
        retrofit = new Retrofit.Builder().baseUrl(currentActivity.getResources().getString(R.string.homenet_link)).addConverterFactory(GsonConverterFactory.create()).client(client).build();
        service = retrofit.create(HomeNetService.class);
        this.adapterPosition = adapterPosition;
        this.emailAddress = emailAddress;
        this.adapter = adapter;
    }

    @Override
    protected void onPreExecute() {
        dialog = new ProgressDialog(currentActivity);
        dialog.setMessage("Processing decline request. Please wait...");
        dialog.setCancelable(false);
        dialog.show();
    }

    @Override
    protected Integer doInBackground(Integer... integers) {
        try {
            Response<SingleResponse<HouseMember>> declineCall = service.declineHouseMember("Bearer "+sharedPreferences.getString("authorization_token", ""), selectedHouse.getHouseID(), emailAddress, sharedPreferences.getString("emailAddress", ""), currentActivity.getResources().getString(R.string.homenet_client_string)).execute();
            if (declineCall.isSuccessful()) {
                houseMember = declineCall.body().getModel();
                return 1;
            } else {
                errorString = declineCall.errorBody().string();
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
        if (houseMember == null) {
            displayMessage("Error Declining Join", "Something went wrong with declining member request. Please try again later.", null);
        } else {
            displayMessage("Request Declined", "The selected member join request has been declined. Notification has been sent.", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    adapter.removeUserFromList(adapterPosition);
                }
            });
        }

    }

    private void displayMessage(String title, String message, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(currentActivity);
        builder.setTitle(title).setMessage(message).setPositiveButton("Okay", listener).setCancelable(false).show();
    }
}
