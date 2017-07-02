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

import Communication.HomeNetService;
import Models.House;
import ResponseModels.SingleResponse;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Okuhle on 2017/06/24.
 */

public class EditHouseTask extends AsyncTask<Integer, Integer, Integer> {

    private Activity currentActivity;
    private SharedPreferences sharedPreferences;
    private Retrofit retrofit;
    private HomeNetService service;
    private House house;
    private ProgressDialog progressDialog;
    private int houseID;
    private OkHttpClient client;
    private List<Protocol> protocolList = new ArrayList<>();


    public EditHouseTask(Activity currentActivity, int houseID) {
        this.currentActivity = currentActivity;
        protocolList.add(Protocol.HTTP_1_1);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(currentActivity);
        client = new OkHttpClient.Builder().readTimeout(3, TimeUnit.MINUTES).connectTimeout(3, TimeUnit.MINUTES).protocols(protocolList).build();
        retrofit = new Retrofit.Builder().baseUrl(currentActivity.getResources().getString(R.string.homenet_link)).client(client).addConverterFactory(GsonConverterFactory.create()).build();
        service = retrofit.create(HomeNetService.class);
        this.houseID = houseID;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(currentActivity);
        progressDialog.setMessage("Fetching house data. Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    protected Integer doInBackground(Integer... integers) {
        try {
            Response<SingleResponse<House>> houseCall = service.getHouse("Bearer "+sharedPreferences.getString("authorization_token", ""), houseID, currentActivity.getResources().getString(R.string.homenet_client_string)).execute();
            if (houseCall.isSuccessful()) {
                house = houseCall.body().getModel();
            }
            return null;
        } catch (Exception error) {
            if (progressDialog.isShowing()) {
                progressDialog.cancel();
            }
            displayMessage("Critical Error", "An error occurred while getting house data. The system will exit\n" + error.getMessage(), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    System.exit(0);
                }
            });

        }
        return null;
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

