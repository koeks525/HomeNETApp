package Tasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.bumptech.glide.Glide;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.koeksworld.homenet.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import Communication.HomeNetService;
import Data.RealmHelper;
import Models.User;
import Models.UserData;
import ResponseModels.SingleResponse;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Okuhle on 2017/08/13.
 */

public class GetUserProfileTask extends AsyncTask<Integer, Integer, Integer> {

    private Retrofit retrofit;
    private HomeNetService service;
    private OkHttpClient client;
    private List<Protocol> protocolList;
    private SharedPreferences sharedPreferences;
    private ProgressDialog dialog;
    private TextView nameTextView, surnameTextView, emailTextView, countryTextView, dateOfBirthTextView, dateRegisteredTextView, totalHouseTextView;
    private BarChart barChart;
    private ImageView profileImageView;
    private Activity currentActivity;
    private User foundUser;
    private String errorInformation = "";
    private File profileFile;
    private RealmHelper helper;
    private UserData userData;
    private Toolbar appToolbar;

    public GetUserProfileTask(TextView nameTextView, TextView surnameTextView, TextView emailTextView, TextView countryTextView, TextView dateOfBirthTextView, TextView dateRegisteredTextView, TextView totalHouseTextView, ImageView profileImageView, Activity currentActivity, BarChart barChart, Toolbar appToolbar) {
        protocolList = new ArrayList<>();
        protocolList.add(Protocol.HTTP_1_1);
        client = new OkHttpClient.Builder().readTimeout(2, TimeUnit.MINUTES).connectTimeout(2, TimeUnit.MINUTES).protocols(protocolList).build();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(currentActivity);
        retrofit = new Retrofit.Builder().baseUrl(currentActivity.getResources().getString(R.string.homenet_link)).client(client).addConverterFactory(GsonConverterFactory.create()).build();
        service = retrofit.create(HomeNetService.class);
        this.nameTextView = nameTextView;
        this.surnameTextView = surnameTextView;
        this.emailTextView = emailTextView;
        this.countryTextView = countryTextView;
        this.dateOfBirthTextView = dateOfBirthTextView;
        this.dateRegisteredTextView = dateRegisteredTextView;
        this.totalHouseTextView = totalHouseTextView;
        this.barChart = barChart;
        this.profileImageView = profileImageView;
        this.currentActivity = currentActivity;
        helper = new RealmHelper();
        this.appToolbar = appToolbar;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog = new ProgressDialog(currentActivity);
        dialog.setMessage("Fetching profile data. Please wait...");
        dialog.setCancelable(false);
        dialog.show();
    }

    //Source: https://futurestud.io/tutorials/retrofit-2-how-to-download-files-from-server
    @Override
    protected Integer doInBackground(Integer... integers) {
        try {
            Response<SingleResponse<User>> userCall = service.getUser("Bearer "+sharedPreferences.getString("authorization_token",""), sharedPreferences.getString("emailAddress", ""), currentActivity.getResources().getString(R.string.homenet_client_string)).execute();
            if (userCall.isSuccessful()) {
                foundUser = userCall.body().getModel();
            } else {
                try {
                    errorInformation += userCall.errorBody().string();
                } catch (Exception error) {

                }
            }
            if (foundUser != null) {
                RequestBody emailAddress = MultipartBody.create(MultipartBody.FORM, sharedPreferences.getString("emailAddress", ""));
                Response<SingleResponse<UserData>> statsCall = service.getUserOverviewReport("Bearer "+sharedPreferences.getString("authorization_token",""), emailAddress, currentActivity.getResources().getString(R.string.homenet_client_string)).execute();
                if (statsCall.isSuccessful()) {
                    userData = statsCall.body().getModel();
                } else {
                    errorInformation += statsCall.errorBody().string();
                }
                if(foundUser.getProfileImage() != null && foundUser.getProfileImage() != "") {
                    Response<ResponseBody> profileCall = service.getProfilePicture("Bearer "+sharedPreferences.getString("authorization_token", ""), sharedPreferences.getString("emailAddress", ""), currentActivity.getResources().getString(R.string.homenet_client_string)).execute();
                    if (profileCall.isSuccessful()) {
                        try {
                            InputStream inputStream = null;
                            OutputStream outputStream = null;
                            try {
                                profileFile = new File(currentActivity.getExternalFilesDir(null) + File.separator + "tempImage2.jpg");
                                byte [] fileReader = new byte[4096];
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

                        }


                    } else {
                        try {
                            errorInformation += profileCall.errorBody().string();
                        } catch (Exception error) {
                            return -1;
                        }
                    }
                }
            } else {
                return 1;
            }
        } catch (Exception error) {
            return -1;
        }
        return -1;
    }

    @Override
    protected void onPostExecute(Integer integer) {
        super.onPostExecute(integer);
        if (dialog.isShowing()) {
            dialog.cancel();
        }
        if (foundUser == null ){
            displayMessage("No user data", "No user data was found", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    currentActivity.onBackPressed();
                }
            });
        }
        if (foundUser != null) {
            appToolbar.setTitle(foundUser.getName() + " "+foundUser.getSurname());
            appToolbar.setTitleTextColor(Color.WHITE);
            nameTextView.setText(foundUser.getName());
            surnameTextView.setText(foundUser.getSurname());
            emailTextView.setText(foundUser.getEmail());
            countryTextView.setText(helper.getCountryById(foundUser.getCountryID()).getName());
            dateOfBirthTextView.setText(foundUser.getDateOfBirth());
            dateRegisteredTextView.setText(foundUser.getDateRegistered());
        }
        if (profileFile!= null) {
            Glide.with(currentActivity).load(profileFile).into(profileImageView);
        } else {
            TextDrawable drawable = TextDrawable.builder().buildRect(foundUser.getName().substring(0,1).toUpperCase() + foundUser.getSurname().substring(0,1).toUpperCase(), Color.BLUE);
            profileImageView.setImageDrawable(drawable);
        }
        if (userData != null) {
            List<BarEntry> barEntries = new ArrayList<>(3);
            BarEntry totalAnnouncements = new BarEntry(0f, userData.getTotalAnnouncements());
            BarEntry totalPosts = new BarEntry(1f, userData.getTotalPosts());
            BarEntry totalHousesJoined = new BarEntry(2f, userData.getTotalHousesJoined());
            barEntries.add(totalAnnouncements);
            barEntries.add(totalPosts);
            barEntries.add(totalHousesJoined);
            BarDataSet setOne = new BarDataSet(barEntries, "User Overview Report");
            BarData barData = new BarData(setOne);

            IAxisValueFormatter formatter = new IAxisValueFormatter() {

                final String [] values = new String[] {"Announcements", "Posts", "Joined Houses"};
                @Override
                public String getFormattedValue(float value, AxisBase axis) {
                    return values[(int) value];
                }
            };
            XAxis axis = barChart.getXAxis();
            axis.setPosition(XAxis.XAxisPosition.BOTTOM);
            axis.setGranularity(1f);
            axis.setValueFormatter(formatter);
            barChart.setData(barData);
            barChart.invalidate();




        }


    }

    private void displayMessage(String title, String message, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(currentActivity);
        builder.setTitle(title).setMessage(message).setPositiveButton("Okay", listener).setCancelable(false).show();
    }
}
