package Tasks;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;

import com.jaredrummler.materialspinner.MaterialSpinner;
import com.koeksworld.homenet.R;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import Communication.HomeNetService;
import Models.House;
import ResponseModels.ListResponse;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.RequestBody;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Okuhle on 2017/08/03.
 */

public class GetSubscribedHousesTask extends AsyncTask<Integer, Integer, Integer> {

    private HomeNetService service;
    private Retrofit retrofit;
    private OkHttpClient client;
    private SharedPreferences sharedPreferences;
    private Activity currentActivity;
    private MaterialSpinner housesSpinner;
    private List<House> houseList;
    private List<Protocol> protocolList;
    private ProgressDialog dialog;
    private String errorInformation = "";


    public GetSubscribedHousesTask(Activity currentActivity, MaterialSpinner housesSpinner) {
        protocolList = new ArrayList<>();
        protocolList.add(Protocol.HTTP_1_1);
        this.currentActivity = currentActivity;
        this.housesSpinner = housesSpinner;
        client = new OkHttpClient.Builder().connectTimeout(2, TimeUnit.MINUTES).readTimeout(2, TimeUnit.MINUTES).protocols(protocolList).build();
        retrofit = new Retrofit.Builder().baseUrl(currentActivity.getResources().getString(R.string.homenet_link)).client(client).addConverterFactory(GsonConverterFactory.create()).build();
        service = retrofit.create(HomeNetService.class);
        houseList = new ArrayList<>();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(currentActivity);
    }

    @Override
    protected void onPreExecute() {
        dialog = new ProgressDialog(currentActivity);
        dialog.setMessage("Getting Data Please Wait");
        dialog.setCancelable(false);
        dialog.show();
    }

    @Override
    protected Integer doInBackground(Integer... integers) {
        try {
            RequestBody emailBody = RequestBody.create(MultipartBody.FORM, sharedPreferences.getString("emailAddress", ""));
            Response<ListResponse<House>> response = service.getSubscribedHouses("Bearer "+sharedPreferences.getString("authorization_token", ""), emailBody, currentActivity.getResources().getString(R.string.homenet_client_string)).execute();
            if (response.isSuccessful()) {
                houseList = response.body().getModel();
            } else {
                errorInformation += response.errorBody().string();
            }
            return 1;
        } catch (Exception error) {
            if (dialog.isShowing()) {
                dialog.cancel();
            }
            errorInformation += error.getMessage();
            return -1;
        }
    }

    @Override
    protected void onPostExecute(Integer integer) {
        if (dialog.isShowing()) {
            dialog.cancel();
        }
        if (errorInformation != "") {
            displayMessage("Error Getting Houses", errorInformation, null);
        } else if (houseList != null) {
            if (houseList.size() > 0) {
                housesSpinner.setItems(houseList);
            } else {
                displayMessage("No Houses Found", "No houses were found. Please join a house", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        currentActivity.getFragmentManager().popBackStack();
                    }
                });
            }
        }
    }

    private void displayMessage(final String title, final String message, final DialogInterface.OnClickListener listener)
    {
        currentActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder messageBox = new AlertDialog.Builder(currentActivity);
                messageBox.setTitle(title).setMessage(message).setCancelable(false).setPositiveButton("GOt it", listener);
                messageBox.show();
            }
        });
    }}
