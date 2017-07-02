package Tasks;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.koeksworld.homenet.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import Communication.HomeNetService;
import Models.HousePost;
import Models.HousePostMetaDataViewModel;
import ResponseModels.ListResponse;
import ResponseModels.SingleResponse;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Okuhle on 2017/06/30.
 */
public class HomeNetFeedTask extends AsyncTask<Integer, Integer, Integer> {

    private Activity currentActivity;
    private Retrofit retrofit;
    private HomeNetService service;
    private SharedPreferences sharedPreferences;
    private View headerView;
    private OkHttpClient client;
    private List<Protocol> protocolList = new ArrayList<>();
    private ProgressDialog dialog;
    private List<HousePost> housePostList = new ArrayList<>();
    private List<HousePostMetaDataViewModel> housePostMetaDataList = new ArrayList<>();
    private int houseID;
    private String errorInformation = "";

    public HomeNetFeedTask(Activity currentActivity, int houseID) {
        this.currentActivity = currentActivity;
        protocolList.add(Protocol.HTTP_1_1);
        client = new OkHttpClient.Builder().protocols(protocolList).connectTimeout(2, TimeUnit.MINUTES).readTimeout(2, TimeUnit.MINUTES).build();
        retrofit = new Retrofit.Builder().baseUrl(currentActivity.getResources().getString(R.string.homenet_link)).addConverterFactory(GsonConverterFactory.create()).client(client).build();
        service = retrofit.create(HomeNetService.class);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(currentActivity);
        this.houseID = houseID;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog = new ProgressDialog(currentActivity);
        dialog.setMessage("Fetching feed data. Please wait...");
        dialog.setCancelable(false);
        dialog.show();
    }
    //Get all house posts, all memberships, all user data, post metric data
    @Override
    protected Integer doInBackground(Integer... integers) {
        try {
            Response<ListResponse<HousePost>> housePostCall = service.getHousePosts("Bearer "+sharedPreferences.getString("authorization_token", ""), houseID, currentActivity.getResources().getString(R.string.homenet_client_string)).execute();
            if (housePostCall.isSuccessful()) {
                if (housePostCall.body().getModel() != null) {
                    for (HousePost post : housePostCall.body().getModel()) {
                        housePostList.add(post);
                        Response<SingleResponse<HousePostMetaDataViewModel>> housePostMetaCall = service.getHousePostMetaData("Bearer "+sharedPreferences.getString("authorization_token", ""), post.getHousePostID(), currentActivity.getResources().getString(R.string.homenet_client_string)).execute();
                        if (housePostMetaCall.isSuccessful()) {
                            if (housePostMetaCall.body().getModel() != null) {
                                housePostMetaDataList.add(housePostMetaCall.body().getModel());
                            }
                        }
                    }
                }

            } else {
                errorInformation = housePostCall.errorBody().string();
            }
        } catch (Exception error) {
            if (dialog.isShowing()) {
                dialog.cancel();
            }
            displayMessage("Error Getting Houses", error.getMessage(), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    System.exit(0);
                }
            });
        }
        return null;
    }

    @Override
    protected void onPostExecute(Integer integer) {
        super.onPostExecute(integer);
        if (dialog.isShowing()) {
            dialog.cancel();
        }
        if ()
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
