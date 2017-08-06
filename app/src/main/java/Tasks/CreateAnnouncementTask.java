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
import Models.HouseAnnouncement;
import Models.NewAnnouncementViewModel;
import ResponseModels.SingleResponse;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Okuhle on 2017/08/04.
 */

public class CreateAnnouncementTask extends AsyncTask<Integer, Integer, Integer> {

    private OkHttpClient client;
    private List<Protocol> protocolList;
    private Retrofit retrofit;
    private HomeNetService service;
    private SharedPreferences sharedPreferences;
    private HouseAnnouncement result;
    private ProgressDialog dialog;
    private Activity currentActivity;
    private String errorInformation = "";
    private NewAnnouncementViewModel model;

    public CreateAnnouncementTask(NewAnnouncementViewModel model, Activity currentActivity) {
        this.currentActivity = currentActivity;
        protocolList = new ArrayList<>();
        protocolList.add(Protocol.HTTP_1_1);
        client = new OkHttpClient.Builder().readTimeout(2, TimeUnit.MINUTES).connectTimeout(2, TimeUnit.MINUTES).protocols(protocolList).build();
        retrofit = new Retrofit.Builder().baseUrl(currentActivity.getResources().getString(R.string.homenet_link)).client(client).addConverterFactory(GsonConverterFactory.create()).build();
        service = retrofit.create(HomeNetService.class);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(currentActivity);
        this.model = model;
    }
    @Override
    protected void onPreExecute() {
        dialog = new ProgressDialog(currentActivity);
        dialog.setMessage("Creating Announcement. Please wait...");
        dialog.setCancelable(false);
        dialog.show();
    }
    @Override
    protected Integer doInBackground(Integer... integers) {
        try {
            Response<SingleResponse<HouseAnnouncement>> announcementCall = service.createAnnouncement("Bearer "+sharedPreferences.getString("authorization_token", ""), model, currentActivity.getResources().getString(R.string.homenet_client_string) ).execute();
            if (announcementCall.isSuccessful()) {
                result = announcementCall.body().getModel();
            } else {
                errorInformation = announcementCall.errorBody().string();
            }
            return 1;
        } catch (Exception error) {
            errorInformation = error.getMessage();
            return -1;
        }
    }
    @Override
    protected void onPostExecute(Integer integer) {
        if (dialog.isShowing()) {
            dialog.cancel();
        }
        if (errorInformation != "") {
            displayMessage("Error Creating Announcement", errorInformation, null);
        } else {
            if (result != null) {
                displayMessage("Announcement Created Successfully!", "Your announcement has been created successfully! Users of the house will receive notifications and email", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        currentActivity.getFragmentManager().popBackStack();
                    }
                });
            } else {
                displayMessage("No Result Data", "No resulting data was received", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        currentActivity.getFragmentManager().popBackStack();
                    }
                });
            }
        }
    }

    private void displayMessage(String title, String message, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(currentActivity);
        builder.setTitle(title).setMessage(message).setPositiveButton("okay", listener).setCancelable(false).show();

    }
}
