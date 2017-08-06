package Tasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
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
 * Created by Okuhle on 2017/07/12.
 */

public class GetHouseAnnouncementsTask extends AsyncTask<Integer, Integer, Integer> {

    private Retrofit retrofit;
    private OkHttpClient client;
    private SharedPreferences sharedPreferences;
    private HomeNetService service;
    private ProgressDialog progressDialog;
    private RecyclerView recyclerView;
    private Activity currentActivity;
    private int houseID;
    private List<Protocol> protocolList;
    private List<HouseAnnouncement> announcementList;

    public GetHouseAnnouncementsTask(Activity currentActivity, RecyclerView recyclerView, int houseID) {
        protocolList = new ArrayList<>();
        protocolList.add(Protocol.HTTP_1_1);
        this.currentActivity = currentActivity;
        client = new OkHttpClient.Builder().readTimeout(2, TimeUnit.MINUTES).connectTimeout(2, TimeUnit.MINUTES).protocols(protocolList).build();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(currentActivity);
        retrofit = new Retrofit.Builder().baseUrl(currentActivity.getResources().getString(R.string.homenet_link)).addConverterFactory(GsonConverterFactory.create()).client(client).build();
        service = retrofit.create(HomeNetService.class);
        this.houseID = houseID;
        this.recyclerView = recyclerView;
        announcementList = new ArrayList<>();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(currentActivity);
        progressDialog.setMessage("Fetching Announcement data. Please wait... ");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    protected Integer doInBackground(Integer... integers) {
        try {
            Response<ListResponse<HouseAnnouncement>> response = service.getHouseAnnouncements("Bearer "+sharedPreferences.getString("authorization_token", ""), houseID ,currentActivity.getResources().getString(R.string.homenet_client_string)).execute();
            if (response.isSuccessful()) {
                if (response.body().getModel() != null) {
                    for (HouseAnnouncement announcement : response.body().getModel()) {
                        announcementList.add(announcement);
                    }
                }
            }
            return 1;
        } catch (Exception error) {
            displayMessage("Error Processing Announcements", error.getMessage(), new DialogInterface.OnClickListener() {
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
        if (announcementList.size() > 0) {
            HouseAnnouncementsAdapter adapter = new HouseAnnouncementsAdapter(announcementList, currentActivity);
            recyclerView.setAdapter(adapter);
        } else {
            Snackbar.make(currentActivity.getCurrentFocus(),"No House Announcements found!", BaseTransientBottomBar.LENGTH_SHORT).show();
        }
    }

    private void displayMessage(final String title, final String message, final DialogInterface.OnClickListener listener) {
        currentActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(currentActivity);
                builder.setTitle(title).setMessage(message).setPositiveButton("Got It", listener).setCancelable(false).show();
            }
        });
    }
}
