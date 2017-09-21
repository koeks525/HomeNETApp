package Tasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.widget.ImageView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.bumptech.glide.Glide;
import com.koeksworld.homenet.R;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import Communication.HomeNetService;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Okuhle on 2017/08/17.
 */

public class GetProfilePictureTask extends AsyncTask<Integer, Integer, Integer> {

    private ImageView profileImageView;
    private Activity currentActivity;
    private Retrofit retrofit;
    private OkHttpClient client;
    private HomeNetService service;
    private List<Protocol> protocolList;
    private SharedPreferences sharedPreferences;
    private File profileFile;
    private ProgressDialog dialog;

    public GetProfilePictureTask(Activity currentActivity, ImageView profileImageView, ProgressDialog dialog) {
        this.profileImageView = profileImageView;
        this.currentActivity = currentActivity;
        protocolList = new ArrayList<>();
        protocolList.add(Protocol.HTTP_1_1);
        client = new OkHttpClient.Builder().connectTimeout(3, TimeUnit.MINUTES).connectTimeout(3, TimeUnit.MINUTES).protocols(protocolList).build();
        retrofit = new Retrofit.Builder().baseUrl(currentActivity.getResources().getString(R.string.homenet_link)).client(client).addConverterFactory(GsonConverterFactory.create()).build();
        service = retrofit.create(HomeNetService.class);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(currentActivity);
        this.dialog = dialog;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Integer doInBackground(Integer... integers) {
        try {
            Response<ResponseBody> profileCall = service.getProfilePicture("Bearer "+sharedPreferences.getString("authorization_token", ""), sharedPreferences.getString("emailAddress",""), currentActivity.getResources().getString(R.string.homenet_client_string)).execute();
            if (profileCall.isSuccessful()) {
                try {
                    InputStream inputStream = null;
                    BufferedOutputStream outputStream = null;
                    try {
                        profileFile = new File(currentActivity.getExternalCacheDir() + File.separator + "tempImage3.jpg");
                        if (profileFile.exists()) {
                            profileFile.delete();
                            profileFile = new File(currentActivity.getExternalCacheDir() + File.separator + "tempImage3.jpg");
                        }
                        byte [] fileReader = new byte[8192];
                        long fileSize = profileCall.body().contentLength();
                        long fileSizeDownloaded = 0;
                        inputStream = new BufferedInputStream(profileCall.body().byteStream());
                        outputStream =new BufferedOutputStream(new FileOutputStream(profileFile));
                        int c;
                        while ((c = inputStream.read(fileReader)) != -1) {
                            outputStream.write(fileReader, 0, c);
                        }
                        outputStream.flush();
                        inputStream.close();
                        outputStream.close();
                        return 1;
                    } catch (Exception error) {
                        return -1;
                    } finally {
                        if (inputStream != null) {
                            inputStream.close();
                        }
                        if (outputStream != null) {
                            outputStream.close();
                        }
                    }



                } catch (Exception error) {

                }

            }
            return 1;
        } catch (Exception error) {

        }
        return -1;
    }

    @Override
    protected void onPostExecute(Integer integer) {
        super.onPostExecute(integer);
        if (dialog.isShowing()) {
            dialog.cancel();
        }
        if (profileFile != null) {
            Glide.with(currentActivity).load(profileFile).into(profileImageView);
        } else {
            TextDrawable drawable = TextDrawable.builder().buildRect(sharedPreferences.getString("name", "").substring(0,1).toUpperCase() + sharedPreferences.getString("surname", "").substring(0,1).toUpperCase(), Color.BLUE);
            profileImageView.setImageDrawable(drawable);
        }
    }
}
