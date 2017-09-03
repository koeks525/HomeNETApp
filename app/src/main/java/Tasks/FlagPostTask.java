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
import Models.HousePostFlag;
import Models.HousePostViewModel;
import ResponseModels.SingleResponse;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.RequestBody;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Okuhle on 2017/09/02.
 */

public class FlagPostTask extends AsyncTask<Integer, Integer, Integer> {

    private ProgressDialog progressDialog;
    private Retrofit retrofit;
    private HomeNetService service;
    private OkHttpClient client;
    private SharedPreferences sharedPreferences;
    private String errorString;
    private List<Protocol> protocolList;
    private HousePostViewModel postModel;
    private Activity currentActivity;
    private String reason;
    private HousePostFlag flaggedPost;

    public FlagPostTask(Activity currentActivity, HousePostViewModel model, String flagReason) {
        this.currentActivity = currentActivity;
        postModel = model;
        protocolList = new ArrayList<>();
        protocolList.add(Protocol.HTTP_1_1);
        client = new OkHttpClient.Builder().connectTimeout(2, TimeUnit.MINUTES).connectTimeout(2, TimeUnit.MINUTES).protocols(protocolList).build();
        retrofit = new Retrofit.Builder().baseUrl(currentActivity.getResources().getString(R.string.homenet_link)).addConverterFactory(GsonConverterFactory.create()).client(client).build();
        service = retrofit.create(HomeNetService.class);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(currentActivity);
        reason = flagReason;
    }

    @Override
    protected void onPreExecute() {
        progressDialog = new ProgressDialog(currentActivity);
        progressDialog.setMessage("Flagging post. Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    protected Integer doInBackground(Integer... integers) {
        try {
            RequestBody flagReason = RequestBody.create(MultipartBody.FORM, reason);
            RequestBody emailAddress = RequestBody.create(MultipartBody.FORM, sharedPreferences.getString("emailAddress", ""));
            Response<SingleResponse<HousePostFlag>> flagCall = service.flagHousePost("Bearer "+sharedPreferences.getString("authorization_token",""), currentActivity.getResources().getString(R.string.homenet_client_string), postModel.getHousePostID(), flagReason, emailAddress).execute();
            if (flagCall.isSuccessful()) {
                flaggedPost = flagCall.body().getModel();
            } else {
                errorString += flagCall.errorBody().string();
            }
            return 1;
        } catch (Exception error) {
            errorString += error.getMessage();
            return -1;
        }
    }

    @Override
    protected void onPostExecute(Integer integer) {
        if (progressDialog.isShowing()) {
            progressDialog.cancel();
        }
        if (flaggedPost != null) {
            displayMessage("House Post Flagged", "The selected house post has been flagged. House administrator has been notified, and will repond to the request", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
        } else {
            displayMessage("No data returned", "No data could was returned from the server. Please try again later", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
        }
    }

    private void displayMessage(String title, String message, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder messageBox = new AlertDialog.Builder(currentActivity);
        messageBox.setTitle(title).setMessage(message).setPositiveButton("Okay", listener).setCancelable(false);
        messageBox.show();
    }
}
