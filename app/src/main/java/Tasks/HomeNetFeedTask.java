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
    private ArrayList<HousePost> housePostList = new ArrayList<>();
    private ArrayList<HousePostMetaDataViewModel> housePostMetaDataList = new ArrayList<>();
    private ArrayList<HouseMember> membershipList = new ArrayList<>();
    private ArrayList<House> houseList = new ArrayList<>();
    private ArrayList<User> userList = new ArrayList<>();
    private String errorInformation = "";
    private RecyclerView feedRecyclerView;
    private HomeNetFeedAdapter feedAdapter;

    public HomeNetFeedTask(Activity currentActivity, RecyclerView feedRecyclerView) {
        this.currentActivity = currentActivity;
        protocolList.add(Protocol.HTTP_1_1);
        client = new OkHttpClient.Builder().protocols(protocolList).connectTimeout(2, TimeUnit.MINUTES).readTimeout(2, TimeUnit.MINUTES).build();
        retrofit = new Retrofit.Builder().baseUrl(currentActivity.getResources().getString(R.string.homenet_link)).addConverterFactory(GsonConverterFactory.create()).client(client).build();
        service = retrofit.create(HomeNetService.class);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(currentActivity);
        this.feedRecyclerView = feedRecyclerView;
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
            Response<ListResponse<HousePost>> housePostCall = service.getHousePosts("Bearer "+sharedPreferences.getString("authorization_token", ""), sharedPreferences.getString("emailAddress", ""), currentActivity.getResources().getString(R.string.homenet_client_string)).execute();
            if (housePostCall.isSuccessful()) {
                if (housePostCall.body().getModel() != null) {
                    housePostList = new ArrayList<>(housePostCall.body().getModel());
                }
                if (housePostList.size() > 0) {
                    for (HousePost post : housePostList) {
                        Response<SingleResponse<HouseMember>> memberCall = service.getHouseMember("Bearer "+sharedPreferences.getString("authorization_token", ""), post.getHouseMemberID(), currentActivity.getResources().getString(R.string.homenet_client_string)).execute();
                        if (memberCall.isSuccessful()) {
                            if (memberCall.body().getModel() != null) {
                                membershipList.add(memberCall.body().getModel());
                                Response<SingleResponse<House>> houseCall = service.getHouse("Bearer "+sharedPreferences.getString("authorization_token", ""), memberCall.body().getModel().getHouseID(), currentActivity.getResources().getString(R.string.homenet_client_string)).execute();
                                if (houseCall.isSuccessful()) {
                                    if (houseCall.body().getModel() != null) {
                                        houseList.add(houseCall.body().getModel());
                                    }
                                }
                                Response<SingleResponse<User>> userCall = service.getUserById("Bearer "+sharedPreferences.getString("authorization_token", ""), memberCall.body().getModel().getUserId(), currentActivity.getResources().getString(R.string.homenet_client_string)).execute();
                                if (userCall.isSuccessful()) {
                                    if (userCall.body().getModel() != null) {
                                        userList.add(userCall.body().getModel());
                                    }
                                }
                            }
                        }

                    }
                }
            }
            else {
                errorInformation = errorInformation + housePostCall.errorBody().string();
            }
            return 1;
        } catch (Exception error) {
            displayMessage("Error Processing Feed Data", error.getMessage(), new DialogInterface.OnClickListener() {
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
        if (dialog.isShowing()) {
            dialog.cancel();
        }
        if (housePostList.size() > 0 && membershipList.size() > 0 && userList.size() > 0 && housePostMetaDataList.size() > 0) {
            feedAdapter = new HomeNetFeedAdapter(housePostList, userList, membershipList, housePostMetaDataList, currentActivity);
            feedRecyclerView.setAdapter(feedAdapter);






            /*FeedFragment feedFragment = new FeedFragment();
            Bundle newBundle = new Bundle();
            newBundle.putParcelableArrayList("HousePostList", housePostList);
            newBundle.putParcelableArrayList("MembershipList", membershipList);
            newBundle.putParcelableArrayList("UserList", userList);
            newBundle.putParcelableArrayList("HousePostMetaData", housePostMetaDataList);
            newBundle.putParcelableArrayList("HouseList", houseList);
            feedFragment.setArguments(newBundle);
            FragmentManager manager = currentActivity.getFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.HomeNetFeedContentView, feedFragment, null);
            transaction.commit();*/
        } else {
            displaySnackbar("No Posts Found. Refresh to get latest posts");

            /*FeedFragment feedFragment = new FeedFragment();
            FragmentTransaction transaction = currentActivity.getFragmentManager().beginTransaction();
            transaction.replace(R.id.HomeNetFeedContentView, feedFragment, null);
            transaction.commit();*/
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

    private void displaySnackbar(String message) {
        Snackbar.make(feedRecyclerView, message, Snackbar.LENGTH_LONG).show();
    }
}
