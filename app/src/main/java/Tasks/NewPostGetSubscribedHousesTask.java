package Tasks;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;

import com.jaredrummler.materialspinner.MaterialSpinner;
import com.koeksworld.homenet.R;

import org.json.JSONObject;

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
 * Created by Okuhle on 2017/07/24.
 */

public class NewPostGetSubscribedHousesTask extends AsyncTask<Integer, Integer, Integer> {

    private Retrofit retrofit;
    private HomeNetService service;
    private List<Protocol> protocolList;
    private SharedPreferences sharedPreferences;
    private MaterialSpinner materialSpinner;
    private List<House> houseList;
    private Activity currentActivity;
    private OkHttpClient client;
    private ProgressDialog dialog;
    private String errorString = "";

    public NewPostGetSubscribedHousesTask(MaterialSpinner materialSpinner, Activity currentActivity) {
        this.currentActivity = currentActivity;
        this.materialSpinner = materialSpinner;
        protocolList = new ArrayList<>();
        protocolList.add(Protocol.HTTP_1_1);
        client = new OkHttpClient.Builder().connectTimeout(2, TimeUnit.MINUTES).connectTimeout(2, TimeUnit.MINUTES).protocols(protocolList).build();
        retrofit = new Retrofit.Builder().baseUrl(currentActivity.getResources().getString(R.string.homenet_link)).client(client).addConverterFactory(GsonConverterFactory.create()).build();
        service = retrofit.create(HomeNetService.class);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(currentActivity);
        houseList = new ArrayList<>();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog = new ProgressDialog(currentActivity);
        dialog.setCancelable(false);
        dialog.setMessage("Getting latest house data. Please wait...");
        dialog.show();
    }

    @Override
    protected Integer doInBackground(Integer... integers) {
        try {
            RequestBody emailBody = RequestBody.create(MultipartBody.FORM, sharedPreferences.getString("emailAddress", ""));
            Response<ListResponse<House>> houseCall = service.getSubscribedHouses("Bearer "+sharedPreferences.getString("authorization_token", ""),emailBody , currentActivity.getResources().getString(R.string.homenet_client_string)).execute();
            if (houseCall.isSuccessful()) {
                for (House currentHouse : houseCall.body().getModel()) {
                    houseList.add(currentHouse);
                }
            } else {
                errorString = houseCall.errorBody().string();
            }

            return 1;
        } catch (Exception error) {
            if (dialog.isShowing()) {
                dialog.cancel();
            }
            errorString = error.getMessage() + "\n"+error.getStackTrace();
            return -1;
        }
    }

    @Override
    protected void onPostExecute(Integer integer) {
        super.onPostExecute(integer);
        if (dialog.isShowing()) {
            dialog.cancel();
        }
        if (errorString != "") {
            try {
                JSONObject errorObject = new JSONObject(errorString);
                displayMessage("Error Getting House Data", errorObject.getString("message"), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        currentActivity.getFragmentManager().popBackStack();
                    }
                });
            } catch (Exception error) {

            }
        } else {
            materialSpinner.setItems(houseList);
        }
    }

    private void displayMessage(final String title, final String message, final DialogInterface.OnClickListener listener) {
        currentActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder box = new AlertDialog.Builder(currentActivity);
                box.setTitle(title).setMessage(message).setPositiveButton("Okay", listener).setCancelable(false).show();
            }
        });
    }
}
