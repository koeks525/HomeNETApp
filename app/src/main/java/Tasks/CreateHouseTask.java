package Tasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;

import com.koeksworld.homenet.HomeManagerActivity;
import com.koeksworld.homenet.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import Communication.HomeNetService;
import Models.House;
import ResponseModels.SingleResponse;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.RequestBody;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Okuhle on 2017/06/19.
 */

public class CreateHouseTask extends AsyncTask<Integer, Integer, Integer> {

    private Retrofit retrofit;
    private HomeNetService service;
    private ProgressDialog progressDialog;
    private Activity currentActivity;
    private MultipartBody.Part imageBodyPart;
    private RequestBody houseName;
    private RequestBody description;
    private RequestBody emailAddress;
    private RequestBody houseLocation;
    private String clientCode;
    private SharedPreferences sharedPreferences;
    private String errorInformation = "";
    private List<Protocol> protocolList = new ArrayList<>();
    private String creationMessage = "";

    public CreateHouseTask(Activity currentActivity, MultipartBody.Part imageBodyPart, RequestBody houseName, RequestBody description, RequestBody emailAddress, RequestBody houseLocation, String clientCode) {
        this.currentActivity = currentActivity;
        this.imageBodyPart = imageBodyPart;
        this.houseName = houseName;
        this.description = description;
        this.emailAddress = emailAddress;
        this.houseLocation = houseLocation;
        this.clientCode = clientCode;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(currentActivity);
        protocolList.add(Protocol.HTTP_1_1);
        OkHttpClient client = new OkHttpClient.Builder().readTimeout(4, TimeUnit.MINUTES).connectTimeout(4, TimeUnit.MINUTES).protocols(protocolList).build();
        retrofit = new Retrofit.Builder().baseUrl(currentActivity.getResources().getString(R.string.homenet_link)).addConverterFactory(GsonConverterFactory.create()).client(client).build();
        service = retrofit.create(HomeNetService.class);

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(currentActivity);
        progressDialog.setMessage("Creating House. Please Wait... ");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    protected Integer doInBackground(Integer... integers) {
        try {
            Response<SingleResponse<House>> houseResponse = service.createHouse("Bearer "+sharedPreferences.getString("authorization_token", ""), houseName, description, houseLocation, emailAddress, clientCode, imageBodyPart).execute();
            if (houseResponse.isSuccessful()) {
                if (houseResponse.code() == 200) {
                    creationMessage = houseResponse.body().getMessage();
                    return 1;
                }
            } else {
                errorInformation = houseResponse.errorBody().string();
                return -1;
            }
        } catch (Exception error) {
            displayMessage("Error Creating House", error.getMessage(), new DialogInterface.OnClickListener() {
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

        if (integer == 1) {
            displayMessage("House Created!", "House Creation Message: \n\n" + creationMessage, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent newIntent = new Intent(currentActivity, HomeManagerActivity.class);
                    currentActivity.startActivity(newIntent);
                    currentActivity.finish();
                }
            });

        } else {
            displayMessage("Error Creating House", "House could not be created \n" + errorInformation, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    System.exit(0);
                }
            });
        }
    }

    private void displayMessage(final String title, final String message, final DialogInterface.OnClickListener listener) {
        currentActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder messageBox = new AlertDialog.Builder(currentActivity);
                messageBox.setTitle(title).setMessage(message).setPositiveButton("Got It", listener).setCancelable(false).show();
            }
        });
    }
}
