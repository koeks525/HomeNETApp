package Tasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;

import com.koeksworld.homenet.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import Adapters.HouseAnnouncementsAdapter;
import Communication.HomeNetService;
import Models.HouseAnnouncement;
import ResponseModels.ListResponse;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Okuhle on 2017/08/04.
 */

public class GetUserAnnouncementsTask extends AsyncTask<Integer, Integer, Integer> {

    private OkHttpClient client;
    private List<Protocol> protocolList;
    private Retrofit retrofit;
    private HomeNetService service;
    private SharedPreferences sharedPreferences;
    private List<HouseAnnouncement> houseAnnouncementList;
    private ProgressDialog dialog;
    private Activity currentActivity;
    private RecyclerView recyclerView;
    private String errorInformation = "";
    private SwipeRefreshLayout refreshLayout;

    public GetUserAnnouncementsTask(Activity currentActivity, RecyclerView recyclerView, SwipeRefreshLayout refreshLayout) {
        this.currentActivity = currentActivity;
        this.recyclerView = recyclerView;
        protocolList = new ArrayList<>();
        protocolList.add(Protocol.HTTP_1_1);
        client = new OkHttpClient.Builder().readTimeout(2, TimeUnit.MINUTES).connectTimeout(2, TimeUnit.MINUTES).protocols(protocolList).build();
        retrofit = new Retrofit.Builder().baseUrl(currentActivity.getResources().getString(R.string.homenet_link)).client(client).addConverterFactory(GsonConverterFactory.create()).build();
        service = retrofit.create(HomeNetService.class);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(currentActivity);
        houseAnnouncementList = new ArrayList<>();
        this.refreshLayout = refreshLayout;
    }

    @Override
    protected void onPreExecute() {
        dialog = new ProgressDialog(currentActivity);
        dialog.setMessage("Fetching Announcements. Please wait...");
        dialog.setCancelable(false);
        dialog.show();
    }

    @Override
    protected Integer doInBackground(Integer... integers) {
        try {
            Response<ListResponse<HouseAnnouncement>> announcementCall = service.getAllAnnouncements("Bearer "+sharedPreferences.getString("authorization_token", ""), sharedPreferences.getString("emailAddress", ""), currentActivity.getResources().getString(R.string.homenet_client_string)).execute();
            if (announcementCall.isSuccessful()) {
                if (announcementCall.body().getModel() != null) {
                    houseAnnouncementList = announcementCall.body().getModel();
                }
            } else {
                errorInformation += announcementCall.errorBody().string();
            }
            return 1;
        } catch (Exception error) {
            if (dialog.isShowing()) {
                dialog.cancel();
            }
            errorInformation = error.getMessage();
            return -1;
        }
    }

    @Override
    protected void onPostExecute(Integer integer) {
        if (dialog.isShowing()) {
            dialog.cancel();
        }
        refreshLayout.setRefreshing(false);
        if (errorInformation != "") {
            displayMessage("Error Getting Announcements", errorInformation, null);
        } else {
            if (houseAnnouncementList != null && houseAnnouncementList.size() > 0) {
                //Create an adapter for this
                HouseAnnouncementsAdapter adapter = new HouseAnnouncementsAdapter(houseAnnouncementList, currentActivity);
                recyclerView.setAdapter(adapter);
            } else {
                Snackbar.make(currentActivity.findViewById(android.R.id.content), "No Announcements found", Snackbar.LENGTH_LONG).show();
            }
        }
    }

    private void displayMessage(String title, String message, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder messageBox = new AlertDialog.Builder(currentActivity);
        messageBox.setTitle(title).setMessage(message).setPositiveButton("Ok", listener).setCancelable(false).show();
    }
}
