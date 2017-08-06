package Tasks;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;

import com.koeksworld.homenet.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import Communication.HomeNetService;
import HomeNETStream.FeedFragment;
import Models.House;
import Models.HousePost;
import ResponseModels.SingleResponse;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.RequestBody;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Multipart;

/**
 * Created by Okuhle on 2017/07/08.
 */

public class CreatePostTask extends AsyncTask<Integer, Integer, Integer> {

    private Retrofit retrofit;
    private OkHttpClient client;
    private HomeNetService service;
    private Activity currentActivity;
    private SharedPreferences sharedPreferences;
    private House selectedHouse;
    private String description, postLocation;
    private MultipartBody.Part postImage;
    private List<Protocol> protocolList = new ArrayList<>();
    private ProgressDialog progressDialog;
    private HousePost resultHousePost;
    private String errorInformation = "";

    public CreatePostTask(Activity currentActivity, House selectedHouse, String description, String postLocation, MultipartBody.Part postImage) {
        this.currentActivity = currentActivity;
        protocolList.add(Protocol.HTTP_1_1);
        client = new OkHttpClient.Builder().connectTimeout(3, TimeUnit.MINUTES).readTimeout(3, TimeUnit.MINUTES).protocols(protocolList).build();
        retrofit = new Retrofit.Builder().baseUrl(currentActivity.getResources().getString(R.string.homenet_link)).addConverterFactory(GsonConverterFactory.create()).client(client).build();
        service = retrofit.create(HomeNetService.class);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(currentActivity);
        this.selectedHouse = selectedHouse;
        this.description = description;
        this.postLocation = postLocation;
        this.postImage = postImage;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(currentActivity);
        progressDialog.setMessage("Saving your post. Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    protected Integer doInBackground(Integer... voids) {
        try {
           //Create a new house post record and upload the photo
            RequestBody emailBody = RequestBody.create(MultipartBody.FORM, sharedPreferences.getString("emailAddress", ""));
            RequestBody descriptionBody = RequestBody.create(MultipartBody.FORM, description);
            RequestBody locationBody = null;
            if (postLocation != null) {
                locationBody = RequestBody.create(MultipartBody.FORM, postLocation);
            }
            if (locationBody == null && postImage == null) {
                Response<SingleResponse<HousePost>> housePostCall = service.addHousePost("Bearer " + sharedPreferences.getString("authorization_token", ""), selectedHouse.getHouseID(), emailBody, descriptionBody, null, currentActivity.getResources().getString(R.string.homenet_client_string), null).execute();
                if (housePostCall.isSuccessful()) {
                    if (housePostCall.body().getModel() != null) {
                        resultHousePost = housePostCall.body().getModel();
                    }
                } else {
                    errorInformation = errorInformation + housePostCall.errorBody().string();
                }


            } else if (locationBody == null && postImage != null) {
                Response<SingleResponse<HousePost>> housePostCall = service.addHousePost("Bearer " + sharedPreferences.getString("authorization_token", ""), selectedHouse.getHouseID(), emailBody, descriptionBody, null, currentActivity.getResources().getString(R.string.homenet_client_string), null).execute();
                if (housePostCall.isSuccessful()) {
                    resultHousePost = housePostCall.body().getModel();
                } else {
                    errorInformation += housePostCall.errorBody().string();
                }
            } else if (locationBody != null && postImage == null) {
                Response<SingleResponse<HousePost>> housePostCall = service.addHousePost("Bearer " + sharedPreferences.getString("authorization_token", ""), selectedHouse.getHouseID(), emailBody, descriptionBody, null, currentActivity.getResources().getString(R.string.homenet_client_string), null).execute();
                if (housePostCall.isSuccessful()) {
                    resultHousePost = housePostCall.body().getModel();
                } else {
                    errorInformation += housePostCall.errorBody().string();
                }
            } else if (locationBody != null && postImage != null) {
                Response<SingleResponse<HousePost>> housePostCall = service.addHousePost("Bearer " + sharedPreferences.getString("authorization_token", ""), selectedHouse.getHouseID(), emailBody, descriptionBody, null, currentActivity.getResources().getString(R.string.homenet_client_string), null).execute();
                if (housePostCall.isSuccessful()) {
                    resultHousePost = housePostCall.body().getModel();
                } else {
                    errorInformation += housePostCall.errorBody().string();
                }

            }
            return 1;
        } catch (Exception error) {
            if (progressDialog.isShowing()) {
                progressDialog.cancel();
            }
            displayMessage("Error Creating Post", error.getMessage(), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    currentActivity.getFragmentManager().popBackStack();
                }
            });
        }
        return -1;
    }

    @Override
    protected void onPostExecute(Integer aVoid) {
        super.onPostExecute(aVoid);
        if(progressDialog.isShowing()) {
            progressDialog.cancel();
        }
        if (resultHousePost != null) {
            //The post should be successful - this means we can take them back to main page
            displayMessage("Post Saved!", "New Post has been saved successfully!", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    FragmentTransaction transaction = currentActivity.getFragmentManager().beginTransaction();
                    transaction.replace(R.id.HomeNetFeedContentView, new FeedFragment(), null);
                    transaction.commit();
                }
            });
        } else {
            displayMessage("Error Saving Post", "Post was not saved, please try again later", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
        }

    }

    private void displayMessage(final String title, final String message, final DialogInterface.OnClickListener listener) {
        currentActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(currentActivity);
                builder.setTitle(title).setMessage(message).setCancelable(false).setPositiveButton("Got it", listener).show();
            }
        });
    }
}
