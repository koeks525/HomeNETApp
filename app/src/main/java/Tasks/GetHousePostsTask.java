package Tasks;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;

import com.koeksworld.homenet.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import Adapters.FlaggedPostsAdapter;
import Adapters.PendingPostsAdapter;
import Adapters.PendingUsersAdapter;
import Communication.HomeNetService;
import Models.HousePostFlag;
import ResponseModels.ListResponse;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Okuhle on 2017/07/12.
 */

public class GetHousePostsTask extends AsyncTask<Integer,Integer,Integer> {

    private Activity currentActivity;
    private OkHttpClient client;
    private HomeNetService service;
    private Retrofit retrofit;
    private SharedPreferences sharedPreferences;
    private ArrayList<HousePostFlag> flaggedList = new ArrayList<>();
    private int houseID;
    private List<Protocol> protocolList = new ArrayList<>();
    private ProgressDialog progressDialog;
    private int modeOfOperation; //1 - flagged posts, pending posts
    private RecyclerView recyclerView;
    private String errorInformation = "";


    public GetHousePostsTask(Activity currentActivity, int houseID, int modeOfOperation) {
        this.currentActivity = currentActivity;
        this.modeOfOperation = modeOfOperation;
        protocolList.add(Protocol.HTTP_1_1);
        client = new OkHttpClient.Builder().readTimeout(2, TimeUnit.MINUTES).connectTimeout(2, TimeUnit.MINUTES).protocols(protocolList).build();
        retrofit = new Retrofit.Builder().baseUrl(currentActivity.getResources().getString(R.string.homenet_link)).client(client).addConverterFactory(GsonConverterFactory.create()).build();
        service = retrofit.create(HomeNetService.class);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(currentActivity);
        progressDialog.setTitle("Fetching house post data. Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    protected Integer doInBackground(Integer... integers) {
        try {
            switch (modeOfOperation) {
                case 1:
                    Response<ListResponse<HousePostFlag>> postCall = service.getFlaggedHousePosts("Bearer "+sharedPreferences.getString("authorization_token", ""), houseID, currentActivity.getResources().getString(R.string.homenet_client_string)).execute();
                    if (postCall.isSuccessful()) {
                        if (postCall.body().getModel() != null) {
                            for (HousePostFlag flaggedPost : postCall.body().getModel()) {
                                flaggedList.add(flaggedPost);
                            }
                        }
                    } else {
                        errorInformation += postCall.errorBody().string();
                    }

                    break;
                case 2:
                    Response<ListResponse<HousePostFlag>> pendingCall = service.getHousePendingPosts("Bearer "+sharedPreferences.getString("authorization_token", ""), houseID, currentActivity.getResources().getString(R.string.homenet_client_string)).execute();
                    if (pendingCall.isSuccessful()) {
                        if (pendingCall.body().getModel() != null) {
                            for (HousePostFlag flag : pendingCall.body().getModel()) {
                                flaggedList.add(flag);
                            }
                        }
                    } else {
                        errorInformation += pendingCall.errorBody().string();
                    }
                    break;
            }
        } catch (Exception error) {
            if (progressDialog.isShowing()) {
                progressDialog.cancel();
            }
        }
        return -1;
    }

    @Override
    protected void onPostExecute(Integer integer) {
        super.onPostExecute(integer);
        if (progressDialog.isShowing()) {
            progressDialog.cancel();
        }
        if (errorInformation != "") {
            displayMessage("Error Getting Posts", errorInformation, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    currentActivity.getFragmentManager().popBackStack();
                }
            });
        }
        if (flaggedList.size() > 0) {
            if (modeOfOperation == 1) {
                FlaggedPostsAdapter pendingAdapter = new FlaggedPostsAdapter(flaggedList);
                recyclerView.setAdapter(pendingAdapter);
            } else if (modeOfOperation == 2) {
                PendingPostsAdapter adapter = new PendingPostsAdapter(flaggedList);
                recyclerView.setAdapter(adapter);
            }
        } else {
            Snackbar.make(currentActivity.getCurrentFocus(), "No Post data found", Snackbar.LENGTH_LONG).show();
        }
    }

    private void displayMessage(final String title, final String message, final DialogInterface.OnClickListener listener) {
        currentActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder messageBox = new AlertDialog.Builder(currentActivity);
                messageBox.setTitle(title).setMessage(message).setCancelable(false).setPositiveButton("Okay", listener);
                messageBox.show();
            }
        });
    }
}
