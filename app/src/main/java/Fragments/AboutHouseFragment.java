package Fragments;


import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.crash.FirebaseCrash;
import com.koeksworld.homenet.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import Communication.HomeNetService;
import Models.House;
import Models.HouseViewModel;
import ResponseModels.SingleResponse;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class AboutHouseFragment extends Fragment implements View.OnClickListener {

    private BarChart aboutHouseChart;
    private List<BarEntry> sampleEntries = new ArrayList<>();
    private BarDataSet barDataSet;
    private BarData barData;
    private XAxis xAxis;
    private Retrofit retrofit;
    private SharedPreferences sharedPreferences;
    private HomeNetService service;
    private OkHttpClient client;
    private List<Protocol> protocolList;
    private House selectedHouse;
    private HouseViewModel viewModel;
    private FloatingActionButton retryButton;

    public AboutHouseFragment() {
        // Required empty public constructor
    }

    private void initializeRetrofit() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        protocolList = new ArrayList<>();
        protocolList.add(Protocol.HTTP_1_1);
        client = new OkHttpClient.Builder().readTimeout(2, TimeUnit.MINUTES).connectTimeout(2, TimeUnit.MINUTES).protocols(protocolList).retryOnConnectionFailure(true).build();
        retrofit = new Retrofit.Builder().baseUrl(getResources().getString(R.string.homenet_link)).addConverterFactory(GsonConverterFactory.create()).client(client).build();
        service = retrofit.create(HomeNetService.class);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View currentView = inflater.inflate(R.layout.fragment_about_house, container, false);
        initializeComponents(currentView);
        initializeRetrofit();
        getData();
        if (selectedHouse != null) {
            generateHouseMetricsReport();
        }
        return currentView;
    }

    private void initializeComponents(View currentView) {
        aboutHouseChart = (BarChart) currentView.findViewById(R.id.AboutHouseBarChart);
        retryButton = (FloatingActionButton) currentView.findViewById(R.id.AboutHouseRefreshButton);
        retryButton.setOnClickListener(this);

    }

    private void generateHouseMetricsReport() {
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Generating house report. Please wait...");
        dialog.setCancelable(false);
        dialog.show();
        Call<SingleResponse<HouseViewModel>> houseCall = service.generateHouseMetricsReport("Bearer "+sharedPreferences.getString("authorization_token", ""), selectedHouse.getHouseID(), getResources().getString(R.string.homenet_client_string));
        houseCall.enqueue(new Callback<SingleResponse<HouseViewModel>>() {
            @Override
            public void onResponse(Call<SingleResponse<HouseViewModel>> call, Response<SingleResponse<HouseViewModel>> response) {
                if (dialog.isShowing()) {
                    dialog.cancel();
                }
                if (response.isSuccessful()) {
                    if (response.body().getModel() != null) {
                        viewModel = response.body().getModel();

                        List<BarEntry> barEntries = new ArrayList<BarEntry>();
                        barEntries.add(new BarEntry(0, viewModel.getTotalComments()));
                        barEntries.add(new BarEntry(1, viewModel.getTotalPosts()));
                        barEntries.add(new BarEntry(2, viewModel.getTotalAnnouncements()));
                        barEntries.add(new BarEntry(3, viewModel.getTotalMembers()));
                        barEntries.add(new BarEntry(4, viewModel.getBannedUsers()));
                        BarDataSet barDataSet = new BarDataSet(barEntries, viewModel.getName() +" Report");
                        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
                        BarData barData = new BarData(barDataSet);
                        XAxis axis = aboutHouseChart.getXAxis();
                        axis.setGranularity(1f);
                        axis.setPosition(XAxis.XAxisPosition.BOTTOM);
                        axis.setValueFormatter(new IAxisValueFormatter() {
                            String [] values = new String [] {"Comments", "Posts", "Announcements", "Members","Banned Users"};
                            @Override
                            public String getFormattedValue(float value, AxisBase axis) {
                                return values[(int) value];
                            }
                        });
                        aboutHouseChart.setData(barData);
                        aboutHouseChart.invalidate();
                    } else {
                        Snackbar.make(getView(), "No data", Snackbar.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<SingleResponse<HouseViewModel>> call, Throwable t) {
                if (dialog.isShowing()) {
                    dialog.cancel();
                }
                FirebaseCrash.log(t.getMessage());
            }
        });
    }

    private void getData() {
        selectedHouse = getArguments().getParcelable("SelectedHouse");
    }

    @Override
    public void onClick(View view) {
        if (selectedHouse != null) {
            generateHouseMetricsReport();
        }
    }

    //Get house details report

}
