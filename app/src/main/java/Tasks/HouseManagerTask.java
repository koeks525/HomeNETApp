package Tasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;

import com.jaredrummler.materialspinner.MaterialSpinner;
import com.koeksworld.homenet.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import Communication.HomeNetService;
import Models.House;
import Models.HousePost;
import ResponseModels.ListResponse;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.HTTP;
/**
 * Created by Okuhle on 2017/06/17.
 */
public class HouseManagerTask extends AsyncTask<Integer, Integer, Integer> {

    private List<House> houseList = new ArrayList<>();
    private Activity currentActivity;
    private ProgressDialog progressDialog;
    private Retrofit retrofit;
    private List<Protocol> protocolList = new ArrayList<>();
    private SharedPreferences sharedPreferences;
    private HomeNetService service;
    private Map<House, List<HousePost>> houseMap = new HashMap<>();
    private MaterialSpinner spinner;
    private List<String> items = new ArrayList<>();

    public HouseManagerTask(Activity currentActivity, MaterialSpinner spinner){
        this.currentActivity = currentActivity;
        progressDialog = new ProgressDialog(currentActivity);
        protocolList.add(Protocol.HTTP_1_1);
        OkHttpClient client = new OkHttpClient.Builder().readTimeout(2, TimeUnit.MINUTES).connectTimeout(2, TimeUnit.MINUTES).protocols(protocolList).build();
        retrofit = new Retrofit.Builder().baseUrl(currentActivity.getResources().getString(R.string.homenet_link)).client(client).addConverterFactory(GsonConverterFactory.create()).build();
        service = retrofit.create(HomeNetService.class);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(currentActivity);
        this.spinner = spinner;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog.setMessage("Searching for House Data. Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    protected Integer doInBackground(Integer... integers) {
        try {
            Response<ListResponse<House>> houseResponse = service.getUserHouses("Bearer " +sharedPreferences.getString("authorization_token", ""), sharedPreferences.getString("emailAddress", ""), currentActivity.getResources().getString(R.string.homenet_client_string)).execute();
            if (houseResponse.isSuccessful()) {
                if (houseResponse.code() == 200) {
                    if (houseResponse.body().getModel() != null) {
                        for (House thisHouse : houseResponse.body().getModel()) {
                            houseList.add(thisHouse);
                        }
                    }
                }
                if (houseList.size() > 0) {
                    for (House thisHouse : houseList) {
                        Response<ListResponse<HousePost>> housePostResponse = service.getHousePosts("Bearer "+sharedPreferences.getString("authorization_token", ""), sharedPreferences.getString("emailAddress", ""), currentActivity.getResources().getString(R.string.homenet_client_string)).execute();
                        if (housePostResponse.isSuccessful()) {
                            if (housePostResponse.body().getModel() != null) {
                                if (housePostResponse.body().getModel().size() > 0) {
                                    houseMap.put(thisHouse, housePostResponse.body().getModel());
                                }
                            }
                        }
                    }
                }

                return 1;

            } else {
                //No house data was found for the user - some error may have occurred
                return 0;
            }
        } catch (Exception error) {
            if (progressDialog.isShowing()) {
                progressDialog.cancel();
            }
            return -1;
        }
    }

    @Override
    protected void onPostExecute(Integer integer) {
        super.onPostExecute(integer);
        if (progressDialog.isShowing()) {
            progressDialog.cancel();
        }

        if (houseList.size() > 0) {
            spinner.setItems(houseList);
        } else {
            //This must change at a later stage
            displayMessage("No houses found", "No houses were found! Why not create a house?", null);
        }
    }

    private void displayMessage(final String title, final String message, final DialogInterface.OnClickListener listener) {

        currentActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(currentActivity);
                builder.setCancelable(false);
                builder.setMessage(message).setTitle(title).setPositiveButton("Okay", listener).show();
            }
        });
    }
}

