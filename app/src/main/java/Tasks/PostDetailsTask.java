package Tasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.crash.FirebaseCrash;
import com.koeksworld.homenet.R;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import Communication.HomeNetService;
import DialogFragments.PhotoDetailsFragment;
import Models.CommentViewModel;
import Models.HousePost;
import Models.HousePostMetaData;
import Models.HousePostMetaDataViewModel;
import Models.HousePostViewModel;
import Models.Picture;
import ResponseModels.ListResponse;
import ResponseModels.SingleResponse;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Okuhle on 2017/09/15.
 */

public class PostDetailsTask extends AsyncTask<Integer, Integer, Integer> {

    private Activity currentActivity;
    private ProgressDialog progressDialog;
    private Retrofit retrofit;
    private HomeNetService service;
    private SharedPreferences sharedPreferences;
    private OkHttpClient client;
    private List<Protocol> protocolList;
    private List<CommentViewModel> commentList;
    private File commentPost;
    private HousePostViewModel postViewModel;
    private HousePostMetaDataViewModel metaDataViewModel;
    private String errorString;
    private HousePost housePost;
    private File postPicture;
    private TextView imageTextView;

    public PostDetailsTask(Activity currentActivity, HousePostViewModel postViewModel, HousePostMetaDataViewModel metaData, TextView imageTextView) {
        this.currentActivity = currentActivity;
        this.postViewModel = postViewModel;
        this.metaDataViewModel = metaData;
        protocolList = new ArrayList<>();
        protocolList.add(Protocol.HTTP_1_1);
        client = new OkHttpClient.Builder().protocols(protocolList).connectTimeout(2, TimeUnit.MINUTES).readTimeout(2, TimeUnit.MINUTES).retryOnConnectionFailure(true).build();
        retrofit = new Retrofit.Builder().baseUrl(currentActivity.getResources().getString(R.string.homenet_link)).client(client).addConverterFactory(GsonConverterFactory.create()).build();
        service = retrofit.create(HomeNetService.class);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(currentActivity);
        commentList = new ArrayList<>();
        this.imageTextView = imageTextView;

    }

    @Override
    protected void onPreExecute() {
        progressDialog = new ProgressDialog(currentActivity);
        progressDialog.setMessage("Downloading post image data. Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    protected Integer doInBackground(Integer... integers) {
        try {
            Response<SingleResponse<HousePost>> postCall = service.getHousePost("Bearer "+sharedPreferences.getString("authorization_token", ""), postViewModel.getHousePostID(), currentActivity.getResources().getString(R.string.homenet_client_string)).execute();
            if (postCall.isSuccessful()) {
                if (postCall.body().getModel() != null) {
                    housePost = postCall.body().getModel();
                }
                if (housePost != null) {
                    if (housePost.getMediaResource() != null) {
                        Response<ResponseBody> pictureCall = service.getHousePostImage("Bearer "+sharedPreferences.getString("authorization_token", ""), housePost.getHousePostID(), currentActivity.getResources().getString(R.string.homenet_client_string)).execute();
                        if (pictureCall.isSuccessful()) {
                                InputStream inputStream = null;
                                BufferedOutputStream outputStream = null;
                                try {
                                    postPicture = new File(currentActivity.getExternalCacheDir() + File.separator + "postImage.jpg");
                                    if (postPicture.exists()) {
                                        postPicture.delete();
                                        postPicture = new File(currentActivity.getExternalCacheDir() + File.separator + "postImage.jpg");
                                    }
                                    byte [] fileReader = new byte[8192];
                                    inputStream = new BufferedInputStream(pictureCall.body().byteStream());
                                    outputStream =new BufferedOutputStream(new FileOutputStream(postPicture));
                                    int c;
                                    while ((c = inputStream.read(fileReader)) != -1) {
                                        outputStream.write(fileReader, 0, c);
                                    }
                                    outputStream.flush();
                                    inputStream.close();
                                    outputStream.close();

                                } catch (Exception error) {
                                    errorString += error.getMessage();
                                } finally {
                                    if (inputStream != null) {
                                        inputStream.close();
                                    }
                                    if (outputStream != null) {
                                        outputStream.close();
                                    }
                                }
                        }
                    }
                   }
            } else {
                errorString += postCall.errorBody().string();

            }
            return 1;
        } catch (Exception error) {
            FirebaseCrash.log(error.getMessage());
            errorString = "Error processing comment request";
            return -1;
        }
    }

    @Override
    protected void onPostExecute(Integer integer) {
        if (progressDialog.isShowing()) {
            progressDialog.cancel();
        }
        if (postPicture != null) {
            if (postPicture.length() > 0) {
                final Picture newPicture = new Picture(postPicture);
                imageTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("SelectedPhoto",newPicture);
                        bundle.putParcelable("SelectedPost", postViewModel);
                        PhotoDetailsFragment dialog = new PhotoDetailsFragment();
                        dialog.setArguments(bundle);
                        dialog.show(currentActivity.getFragmentManager(), null);
                    }
                });
            } else {
                imageTextView.setVisibility(View.GONE);
            }
        } else {
            imageTextView.setVisibility(View.GONE);
        }
    }
}
