package Tasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import io.reactivex.Single;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Okuhle on 2017/07/13.
 */

public class UpdateHouseTask extends AsyncTask<Integer, Integer, Integer> {

    private Activity currentActivity;
    private SharedPreferences sharedPreferences;
    private Retrofit retrofit;
    private OkHttpClient client;
    private ProgressDialog progressDialog;
    private HomeNetService service;
    private House updateHouse;
    private List<Protocol> protocolList;
    private MultipartBody.Part imageFile;
    private String errorInformation = "";
    private House receivedHouse;

    private UpdateHouseTask(Activity currentActivity, House updatedHouse, MultipartBody.Part imageFile) {
        protocolList = new ArrayList<>();
        protocolList.add(Protocol.HTTP_1_1);
        this.currentActivity = currentActivity;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(currentActivity);
        client = new OkHttpClient.Builder().readTimeout(3, TimeUnit.MINUTES).connectTimeout(3, TimeUnit.MINUTES).protocols(protocolList).build();
        retrofit = new Retrofit.Builder().baseUrl(currentActivity.getResources().getString(R.string.homenet_link)).addConverterFactory(GsonConverterFactory.create()).client(client).build();
        service = retrofit.create(HomeNetService.class);
        this.imageFile = imageFile;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(currentActivity);
        progressDialog.setMessage("Updating House. Please Wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    protected Integer doInBackground(Integer... integers) {
        try {
            Response<SingleResponse<House>> updateCall = service.updateHouse("Bearer "+sharedPreferences.getString("authorization_token", ""), updateHouse.getHouseID(), updateHouse.getName(), updateHouse.getDescription(), sharedPreferences.getString("emailAddress", ""), updateHouse.getIsPrivate(), Integer.toString(updateHouse.getOneTimePin()), imageFile).execute();
            if (updateCall.isSuccessful()) {
               receivedHouse = updateCall.body().getModel();
            } else {
                errorInformation += updateCall.errorBody().string();
            }
            return 1;
        } catch (Exception error) {
            if (progressDialog.isShowing()) {
                progressDialog.cancel();
            }
            displayMessage("Error Processing Update", error.getMessage(), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    System.exit(0);
                }
            });
        }
        return -1;
    }

    @Override
    protected void onPostExecute(Integer integer) {
        super.onPostExecute(integer);
        if (progressDialog.isShowing()) {
            progressDialog.cancel();
        }
        if (receivedHouse != null) {
            displayMessage("Update Successful", "Your house has been updated successfully!", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
        } else {
            if (errorInformation != "") {
                displayMessage("Error Updating House", errorInformation, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
            } else {
                displayMessage("Error Updating House", "Something wrong happened with updating the house", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

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
                builder.setTitle(title).setMessage(message).setCancelable(false).setPositiveButton("Got it", listener);
                builder.show();
            }
        });
    }
}
