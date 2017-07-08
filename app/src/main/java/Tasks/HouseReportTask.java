package Tasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.koeksworld.homenet.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import Communication.HomeNetService;
import Models.HomeData;
import Models.House;
import ResponseModels.SingleResponse;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Okuhle on 2017/07/06.
 */

public class HouseReportTask extends AsyncTask<Integer,Integer,Integer> {

    private Retrofit retrofit;
    private HomeNetService service;
    private OkHttpClient client;
    private SharedPreferences sharedPreferences;
    private BarChart houseReportChart;
    private HomeData homeData;
    private Activity currentActivity;
    private ProgressDialog progressDialog;
    private List<Protocol> protocolList = new ArrayList<>();
    private int houseID;
    private House selectedHouse;
    private String errorInformation = "";
    public HouseReportTask(Activity currentActivity, BarChart houseReportChart, int houseID) {
        this.currentActivity = currentActivity;
        this.houseReportChart = houseReportChart;
        protocolList.add(Protocol.HTTP_1_1);
        client = new OkHttpClient.Builder().readTimeout(2, TimeUnit.MINUTES).connectTimeout(2, TimeUnit.MINUTES).protocols(protocolList).build();
        retrofit = new Retrofit.Builder().baseUrl(currentActivity.getResources().getString(R.string.homenet_link)).client(client).addConverterFactory(GsonConverterFactory.create()).build();
        service = retrofit.create(HomeNetService.class);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(currentActivity);
        this.houseID = houseID;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(currentActivity);
        progressDialog.setMessage("Getting house metrics. Please wait... ");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    protected Integer doInBackground(Integer... integers) {
        try {
            Response<SingleResponse<House>> houseData = service.getHouse("Bearer "+sharedPreferences.getString("authorization_token",""), houseID, currentActivity.getResources().getString(R.string.homenet_client_string)).execute();
            if (houseData.isSuccessful()) {
                if (houseData.body().getModel() != null) {
                    selectedHouse = houseData.body().getModel();
                }
            } else {
                errorInformation += houseData.errorBody().string();
            }

            Response<SingleResponse<HomeData>> homeDataCall = service.getHouseOverviewReport("Bearer "+sharedPreferences.getString("authorization_token", ""), houseID, currentActivity.getResources().getString(R.string.homenet_client_string)).execute();
            if (homeDataCall.isSuccessful()) {
                if (homeDataCall.body().getModel() != null) {
                    homeData = homeDataCall.body().getModel();
                }
            } else {
               errorInformation = homeDataCall.errorBody().string();
            }
            return 1;
        } catch (Exception error) {
            if (progressDialog.isShowing()) {
                progressDialog.cancel();
            }
            displayMessage("Error Getting House Metrics", error.getMessage(), new DialogInterface.OnClickListener() {
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
        if (progressDialog.isShowing()) {
            progressDialog.cancel();
        }
        if (homeData != null && selectedHouse != null) {
            generateBarChart();
        } else {
            if (errorInformation != "") {
                displayMessage("Error Getting Metrics", errorInformation, null);
            } else {
                displayMessage("No Metrics Found", "No metrics data could be located", null);
            }
        }
    }

    private void displayMessage(final String title, final String message, final DialogInterface.OnClickListener listener) {
        currentActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder messageBox = new AlertDialog.Builder(currentActivity);
                messageBox.setTitle(title).setMessage(message).setCancelable(false).setPositiveButton("Got it", listener).show();
            }
        });
    }

    private void generateBarChart() {
        List<BarEntry> entries = new ArrayList<>();
        BarDataSet barDataSet;
        BarData barData;
        XAxis xAxis;

        entries.add(new BarEntry(0, homeData.getTotalActiveUsers())); //Get total active user
        entries.add(new BarEntry(1, homeData.getTotalBannedUsers())); //Get total banned users
        entries.add(new BarEntry(2, homeData.getTotalPendingUsers())); //Get total pending users
        entries.add(new BarEntry(3, homeData.getTotalUsers())); //Get total users
        entries.add(new BarEntry(4, homeData.getTotalPosts())); //Get total posts in the house
        barDataSet = new BarDataSet(entries, selectedHouse.getName() + "Metrics");
        xAxis = houseReportChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            private String [] values = {"Active Users", "Banned Users", "Pending Users", "Users", "Posts"};
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return values[(int) value];
            }
        });
        barData = new BarData(barDataSet);
        houseReportChart.setData(barData);
        houseReportChart.invalidate();
    }
}
