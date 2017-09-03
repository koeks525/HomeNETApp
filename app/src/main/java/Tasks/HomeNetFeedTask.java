package Tasks;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.koeksworld.homenet.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import Adapters.HomeNetFeedAdapter;
import Communication.HomeNetService;
import HomeNETStream.FeedFragment;
import Models.House;
import Models.HouseMember;
import Models.HousePost;
import Models.HousePostMetaData;
import Models.HousePostMetaDataViewModel;
import Models.HousePostViewModel;
import Models.User;
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
    private List<HousePostViewModel> postList;
    private List<HousePostMetaDataViewModel> metaDataList;
    private String errorInformation = "";
    private RecyclerView feedRecyclerView;
    private HomeNetFeedAdapter feedAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;


    public HomeNetFeedTask(Activity currentActivity, RecyclerView feedRecyclerView, SwipeRefreshLayout swipeRefreshLayout) {
        this.currentActivity = currentActivity;
        protocolList.add(Protocol.HTTP_1_1);
        client = new OkHttpClient.Builder().protocols(protocolList).connectTimeout(2, TimeUnit.MINUTES).readTimeout(2, TimeUnit.MINUTES).build();
        retrofit = new Retrofit.Builder().baseUrl(currentActivity.getResources().getString(R.string.homenet_link)).addConverterFactory(GsonConverterFactory.create()).client(client).build();
        service = retrofit.create(HomeNetService.class);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(currentActivity);
        this.feedRecyclerView = feedRecyclerView;
        postList = new ArrayList<>();
        metaDataList = new ArrayList<>();
        this.swipeRefreshLayout = swipeRefreshLayout;
    }

    @Override
    protected void onPreExecute() {
        dialog = new ProgressDialog(currentActivity);
        dialog.setMessage("Fetching feed data. Please wait...");
        dialog.setCancelable(false);
        dialog.show();
    }

    @Override
    protected Integer doInBackground(Integer... integers) {
        try {
           Response<ListResponse<HousePostViewModel>> listCall = service.getAllHousePosts("Bearer "+sharedPreferences.getString("authorization_token", ""), sharedPreferences.getString("emailAddress", ""), currentActivity.getResources().getString(R.string.homenet_client_string)).execute();
            if (listCall.isSuccessful()) {
                if (listCall.body().getModel() != null) {
                    for (HousePostViewModel model : listCall.body().getModel()) {
                        postList.add(model);
                        Response<SingleResponse<HousePostMetaDataViewModel>> call = service.getHousePostMetrics("Bearer "+sharedPreferences.getString("authorization_token", ""), model.getHousePostID(), currentActivity.getResources().getString(R.string.homenet_client_string)).execute();
                        if (call.isSuccessful()) {
                            HousePostMetaDataViewModel data = call.body().getModel();
                            if (model != null) {
                                metaDataList.add(data);
                            }
                        } else {
                            errorInformation = call.errorBody().string();
                        }
                    }
                }
            } else {
                errorInformation = listCall.errorBody().string();
            }
        } catch (Exception error) {
            if (dialog.isShowing()) {
                dialog.cancel();
            }
            errorInformation = error.getMessage();
        }
        return -1;
    }

    @Override
    protected void onPostExecute(Integer integer) {
        swipeRefreshLayout.setRefreshing(false);
        if (dialog.isShowing()) {
            dialog.cancel();
        }
        if (errorInformation != "") {
            displayMessage("Error Generating Feed", errorInformation, null);
        } else if (postList.size() <= 0) {
            displaySnackbar("No posts found. Refresh to get latest posts");
        } else if (postList.size() > 0){
            feedAdapter = new HomeNetFeedAdapter(metaDataList, postList, currentActivity);
            feedRecyclerView.setAdapter(feedAdapter);
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

    private void displaySnackbar(String message) {//error is here
        Snackbar.make(currentActivity.findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show();
    }
}
