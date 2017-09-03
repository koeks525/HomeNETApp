package Tasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.bumptech.glide.Glide;
import com.koeksworld.homenet.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import Communication.HomeNetService;
import Models.House;
import Models.HouseViewModel;
import ResponseModels.ListResponse;
import ResponseModels.SingleResponse;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Okuhle on 2017/08/30.
 */

public class GetHouseProfileTask extends AsyncTask<Integer, Integer, Integer> {

    private TextView name, description, members, dateCreated;
    private ImageView houseImageView;
    private Retrofit retrofit;
    private List<Protocol> protocolList;
    private ProgressDialog dialog;
    private Activity currentActivity;
    private OkHttpClient client;
    private HomeNetService service;
    private SharedPreferences sharedPreferences;
    private House selectedHouse;
    private HouseViewModel model;
    private String errorString;
    private File profileFile;

    public GetHouseProfileTask(TextView name, TextView description, TextView members, TextView dateCreated, ImageView houseImageView, Activity currentActivity, House selectedHouse) {
        this.currentActivity= currentActivity;
        this.name = name;
        this.description = description;
        this.members = members;
        this.dateCreated = dateCreated;
        this.houseImageView = houseImageView;
        protocolList = new ArrayList<>();
        protocolList.add(Protocol.HTTP_1_1);
        client = new OkHttpClient.Builder().protocols(protocolList).connectTimeout(3, TimeUnit.MINUTES).readTimeout(3, TimeUnit.MINUTES).build();
        retrofit = new Retrofit.Builder().baseUrl(currentActivity.getResources().getString(R.string.homenet_link)).client(client).addConverterFactory(GsonConverterFactory.create()).build();
        service = retrofit.create(HomeNetService.class);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(currentActivity);
        this.selectedHouse = selectedHouse;
    }

    @Override
    protected void onPreExecute() {
        dialog = new ProgressDialog(currentActivity);
        dialog.setMessage("Fetching House Information. Please wait...");
        dialog.setCancelable(false);
        dialog.show();
    }

    @Override
    protected Integer doInBackground(Integer... integers) {
        try {
            Response<SingleResponse<HouseViewModel>> houseCall = service.generateHouseMetricsReport("Bearer "+sharedPreferences.getString("authorization_token",""), selectedHouse.getHouseID(), currentActivity.getResources().getString(R.string.homenet_client_string)).execute();
            if (houseCall.isSuccessful()) {
                if (houseCall.body().getModel() != null) {
                    model = houseCall.body().getModel();
                    if (model.getHouseImage() != null) {
                        Response<ResponseBody> profileCall = service.getHouseProfileImage("Bearer " + sharedPreferences.getString("authorization_token", ""), selectedHouse.getHouseID(), currentActivity.getResources().getString(R.string.homenet_client_string)).execute();
                        if (profileCall.isSuccessful()) {
                            try {
                                InputStream inputStream = null;
                                OutputStream outputStream = null;
                                try {
                                    profileFile = new File(currentActivity.getExternalFilesDir(null) + File.separator + "tempImage3.jpg");
                                    if (profileFile.exists()) {
                                        boolean result = profileFile.delete();
                                        profileFile = new File(currentActivity.getExternalFilesDir(null) + File.separator + "tempImage3.jpg");
                                    }
                                    byte[] fileReader = new byte[4096];
                                    long fileSize = profileCall.body().contentLength();
                                    long fileSizeDownloaded = 0;
                                    inputStream = profileCall.body().byteStream();
                                    outputStream = new FileOutputStream(profileFile);
                                    int c;
                                    while ((c = inputStream.read()) != -1) {
                                        outputStream.write(c);
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
                                errorString = errorString + error.getMessage();
                            }
                        }


                    }
                }

            }
            return 1;

        } catch (Exception error) {
            errorString = errorString + error.getMessage();
            return -1;
        }
    }

    @Override
    protected void onPostExecute(Integer integer) {
        if (dialog.isShowing()) {
            dialog.cancel();
        }
        if (model != null) {
            name.setText(model.getName());
            description.setText(model.getDescription());
            dateCreated.setText(model.getDateCreated());
            members.setText(Integer.toString(model.getTotalMembers()));
            if (profileFile != null) {
                Glide.with(currentActivity).load(profileFile).into(houseImageView);
            } else {
                TextDrawable drawable = TextDrawable.builder().buildRect(model.getName().substring(0,2).toUpperCase(), Color.BLUE);
                houseImageView.setImageDrawable(drawable);
            }
        } else {
            displayMessage("No Data Received", "No house data was received from the server. Please try again later", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    currentActivity.getFragmentManager().popBackStack();
                }
            });
        }

    }

    private void displayMessage(String title, String message, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder box = new AlertDialog.Builder(currentActivity);
        box.setTitle(title).setMessage(message).setPositiveButton("Ok", listener);
        box.setCancelable(false).show();
    }
}
