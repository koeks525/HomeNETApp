package Tasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;

import com.koeksworld.homenet.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import Adapters.PhotoGalleryAdapter;
import Communication.HomeNetService;
import Models.HousePostViewModel;
import Models.Picture;
import ResponseModels.ListResponse;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Okuhle on 2017/09/03.
 */

public class GetMultimediaPostsTask extends AsyncTask<Integer, Integer, Integer> {

    private List<Protocol> protocolList;
    private Retrofit retrofit;
    private HomeNetService service;
    private List<Picture> pictureList;
    private SharedPreferences sharedPreferences;
    private ProgressDialog progressDialog;
    private Activity currentActivity;
    private OkHttpClient client;
    private String errorString;
    private List<HousePostViewModel> postList;
    private RecyclerView recyclerView;

    public GetMultimediaPostsTask(Activity currentActivity, RecyclerView recyclerView) {
        protocolList = new ArrayList<>();
        protocolList.add(Protocol.HTTP_1_1);
        client = new OkHttpClient.Builder().connectTimeout(3, TimeUnit.MINUTES).readTimeout(4, TimeUnit.MINUTES).protocols(protocolList).build();
        retrofit = new Retrofit.Builder().baseUrl(currentActivity.getResources().getString(R.string.homenet_link)).addConverterFactory(GsonConverterFactory.create()).client(client).build();
        service = retrofit.create(HomeNetService.class);
        pictureList = new ArrayList<>();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(currentActivity);
        this.currentActivity = currentActivity;
        errorString = "";
        postList = new ArrayList<>();
        this.recyclerView = recyclerView;
    }

    @Override
    protected void onPreExecute() {
        progressDialog = new ProgressDialog(currentActivity);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching Images. Please wait...");
        progressDialog.show();
    }

    @Override
    protected Integer doInBackground(Integer... integers) {
        try {
            Response<ListResponse<HousePostViewModel>> postCall = service.getAllMultimediaPosts("Bearer "+sharedPreferences.getString("authorization_token", ""), sharedPreferences.getString("emailAddress", ""), currentActivity.getString(R.string.homenet_client_string)).execute();
            if (postCall.isSuccessful()) {
                if (postCall.body().getModel() != null) {
                    for (HousePostViewModel model : postCall.body().getModel()) {
                        postList.add(model);
                    }
                    if (postList.size() > 0) {
                        for (HousePostViewModel model : postList) {
                            Response<ResponseBody> pictureCall = service.getHousePostImage("Bearer " + sharedPreferences.getString("authorization_token", ""), model.getHousePostID(), currentActivity.getResources().getString(R.string.homenet_client_string)).execute();
                            if (pictureCall.isSuccessful()) {
                                    InputStream inputStream = null;
                                    OutputStream outputStream = null;
                                    try {
                                        File profileFile = new File(currentActivity.getExternalCacheDir() + File.separator + generateRandomString()+"tempImage2.jpg");
                                        byte[] fileReader = new byte[4096];
                                        long fileSize = pictureCall.body().contentLength();
                                        long fileSizeDownloaded = 0;
                                        inputStream = pictureCall.body().byteStream();
                                        outputStream = new FileOutputStream(profileFile);
                                        int c;
                                        while ((c = inputStream.read()) != -1) {
                                            outputStream.write(c);
                                        }
                                        outputStream.flush();
                                        inputStream.close();
                                        outputStream.close();
                                        Picture finalPicture = new Picture(profileFile);
                                        pictureList.add(finalPicture);
                                    } catch (Exception error) {

                                    } finally {
                                        if (inputStream != null) {
                                            inputStream.close();
                                        }
                                        if (outputStream != null) {
                                            outputStream.close();
                                        }
                                    }


                                } else {
                                errorString += pictureCall.errorBody().string();
                            }

                            }

                        }
                }
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
        if (progressDialog.isShowing()) {
            progressDialog.cancel();
        }
        if (pictureList.size() > 0) {
            PhotoGalleryAdapter adapter = new PhotoGalleryAdapter(currentActivity, pictureList);
            recyclerView.setAdapter(adapter);
        }
    }

    private String generateRandomString() {
        Random random = new Random();
        String finalString = "";
        for (int a = 0; a < 7; a++) {
            finalString = finalString + random.nextInt(9) + 1 ;
        }
        return finalString;
    }
}
