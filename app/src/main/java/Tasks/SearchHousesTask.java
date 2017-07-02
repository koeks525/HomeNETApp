package Tasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.koeksworld.homenet.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import Adapters.SearchHousesAdapter;
import Communication.HomeNetService;
import Models.House;
import Models.SearchViewModel;
import ResponseModels.ListResponse;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.RequestBody;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Okuhle on 2017/06/27.
 */

public class SearchHousesTask extends AsyncTask<Integer, String, Integer> {

    private Activity currentActivity;
    private SharedPreferences sharedPreferences;
    private List<House> houseList = new ArrayList<>();
    private List<Protocol> protocolList = new ArrayList<>();
    private Retrofit retrofit;
    private HomeNetService service;
    private ProgressDialog dialog;
    private OkHttpClient client;
    private String searchParams;
    private RecyclerView recyclerView;
    private SearchHousesAdapter adapter;

    public SearchHousesTask(Activity currentActivity, String searchParams, RecyclerView searchRecyclerView){
        this.searchParams = searchParams;
        this.currentActivity = currentActivity;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(currentActivity);
        protocolList.add(Protocol.HTTP_1_1);
        client = new OkHttpClient.Builder().readTimeout(2, TimeUnit.MINUTES).connectTimeout(2, TimeUnit.MINUTES).protocols(protocolList).build();
        retrofit = new Retrofit.Builder().baseUrl(currentActivity.getResources().getString(R.string.homenet_link)).client(client).addConverterFactory(GsonConverterFactory.create()).build();
        service = retrofit.create(HomeNetService.class);
        recyclerView = searchRecyclerView;
        adapter = new SearchHousesAdapter(currentActivity);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog = new ProgressDialog(currentActivity);
        dialog.setMessage("Searching Houses. Please wait...");
        dialog.setCancelable(false);
        dialog.show();
    }

    @Override
    protected Integer doInBackground(Integer... integers) {
        try {
            SearchViewModel searchParameter = new SearchViewModel(searchParams);
            Response<ListResponse<House>> houseCall = service.searchHouses("Bearer "+sharedPreferences.getString("authorization_token", ""), searchParameter, currentActivity.getResources().getString(R.string.homenet_client_string)).execute();
            if (houseCall.isSuccessful()) {
                if (houseCall.body().getModel() != null) {
                    for (House thisHouse : houseCall.body().getModel()) {
                        houseList.add(thisHouse);
                    }
                }
            } else {
                String error = houseCall.errorBody().string();
            }
        } catch (Exception error) {
            if (dialog.isShowing()) {
                dialog.cancel();
            }
            displayMessage("Error Searching Houses", error.getMessage(), null);
        }
        return null;
    }

    @Override
    protected void onPostExecute(Integer integer) {
        super.onPostExecute(integer);
        if (dialog.isShowing()) {
            dialog.cancel();
        }
        if (houseList.size() > 0) {
            adapter.setHousesList(houseList);
            recyclerView.setAdapter(adapter);
        } else {
            Toast.makeText(currentActivity, "No Houses found", Toast.LENGTH_LONG).show();
        }
    }

    private void displayMessage(final String title, final String message, final DialogInterface.OnClickListener listener) {
        currentActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder messageBox = new AlertDialog.Builder(currentActivity);
                messageBox.setTitle(title).setMessage(message).setCancelable(false).setPositiveButton("Okay", listener).show();
            }
        });
    }
}
