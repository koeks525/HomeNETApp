package Tasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.widget.TextView;

import com.koeksworld.homenet.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import Communication.HomeNetService;
import Models.HousePostViewModel;
import Models.PostDetailsViewModel;
import ResponseModels.SingleResponse;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Okuhle on 2017/09/02.
 */

public class GetHousePostDetailsTask extends AsyncTask<Integer, Integer, Integer> {

    private OkHttpClient client;
    private Retrofit retrofit;
    private SharedPreferences sharedPreferences;
    private ProgressDialog dialog;
    private HomeNetService service;
    private Activity currentActivity;
    private List<Protocol> protocolList;
    private TextView totalLikes, totalDislikes, likeNames, dislikeNames;
    private String errorString;
    private HousePostViewModel selectedPost;
    private PostDetailsViewModel model;

    public GetHousePostDetailsTask(Activity currentActivity, TextView totalLikes, TextView totalDislikes, TextView likeNames, TextView dislikeNames, HousePostViewModel selectedPost) {
        this.selectedPost = selectedPost;
        this.currentActivity = currentActivity;
        this.totalLikes = totalLikes;
        this.totalDislikes = totalDislikes;
        this.likeNames = likeNames;
        this.dislikeNames = dislikeNames;
        protocolList = new ArrayList<>();
        protocolList.add(Protocol.HTTP_1_1);
        client = new OkHttpClient.Builder().connectTimeout(2, TimeUnit.MINUTES).connectTimeout(2, TimeUnit.MINUTES).protocols(protocolList).build();
        retrofit = new Retrofit.Builder().baseUrl(currentActivity.getResources().getString(R.string.homenet_link)).addConverterFactory(GsonConverterFactory.create()).client(client).build();
        service = retrofit.create(HomeNetService.class);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(currentActivity);
    }

    @Override
    protected void onPreExecute() {
        dialog = new ProgressDialog(currentActivity);
        dialog.setMessage("Fetching post details. Please wait...");
        dialog.setCancelable(false);
        dialog.show();
    }

    @Override
    protected Integer doInBackground(Integer... integers) {
        try {
            Response<SingleResponse<PostDetailsViewModel>> postCall = service.getPostDetails("Bearer "+sharedPreferences.getString("authorization_token", ""), selectedPost.getHousePostID(), currentActivity.getResources().getString(R.string.homenet_client_string)).execute();
            if (postCall.isSuccessful()) {
                model = postCall.body().getModel();
            } else {
                errorString += postCall.errorBody().string();
            }
            return 1;
        } catch (Exception error) {
            errorString += error.getMessage();
            return -1;
        }
    }

    @Override
    protected void onPostExecute(Integer integer) {
        if (dialog.isShowing()) {
            dialog.cancel();
        }
        if (model != null) {
            totalLikes.setText(Integer.toString(model.getTotalLikes()));
            totalDislikes.setText(Integer.toString(model.getTotalDislikes()));
            StringBuilder likesBuilder = new StringBuilder(" ");
            StringBuilder dislikeBuilder = new StringBuilder(" ");
            for (String user : model.getLikes()) {
                likesBuilder.append(user + "\n");
            }
            for (String user : model.getDislikes()) {
                dislikeBuilder.append(user +"\n");
            }
            likeNames.setText(likesBuilder.toString());
            dislikeNames.setText(dislikeBuilder.toString());
        }
        else {
            displayMessage("No Data", "No post data is available for the selected post", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
        }

    }

    private void displayMessage(String title, String message, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(currentActivity);
        builder.setTitle(title).setMessage(message).setPositiveButton("Okay", listener).setCancelable(false).show();
    }
}
