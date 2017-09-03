package Fragments;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.app.Fragment;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.koeksworld.homenet.R;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import Communication.HomeNetService;
import Models.House;
import Models.HouseMember;
import Models.HouseViewModel;
import ResponseModels.ListResponse;
import ResponseModels.SingleResponse;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyHousesFragment extends Fragment implements View.OnClickListener {
    private TextView houseNameTextView, descriptionTextView, ownerTextView, totalMembersTextView, totalBannedTextView, dateCreatedTextView;
    private Button leaveHouseButton;
    private BarChart houseChart;
    private OkHttpClient client;
    private Retrofit retrofit;
    private HomeNetService service;
    private List<Protocol> protocolList;
    private List<House> subscribedHouseList;
    private ProgressDialog dialog;
    private SharedPreferences sharedPreferences;
    private TextView toolbarTextView;
    private MaterialSpinner houseSpinner;
    public MyHousesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View currentView = inflater.inflate(R.layout.fragment_my_houses, container, false);
        initializeComponents(currentView);
        initializeRetrofit();
        getSubscribedHouses();
        return currentView;
    }

    private void initializeComponents(View currentView) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        subscribedHouseList = new ArrayList<>();
        dialog = new ProgressDialog(getActivity());
        dateCreatedTextView  = (TextView) currentView.findViewById(R.id.MyHouseHouseDateCreatedTextView);
        houseNameTextView = (TextView) currentView.findViewById(R.id.MyHouseHouseNameTextView);
        descriptionTextView = (TextView) currentView.findViewById(R.id.MyHouseHouseDescriptionTextView);
        ownerTextView = (TextView) currentView.findViewById(R.id.MyHouseHouseOwnerTextView);
        totalMembersTextView = (TextView) currentView.findViewById(R.id.MyHouseHouseTotalMembersTextView);
        totalBannedTextView = (TextView) currentView.findViewById(R.id.MyHouseHouseTotalBannedMembersTextView);
        leaveHouseButton = (Button) currentView.findViewById(R.id.MyHousesLeaveHouseButton);
        leaveHouseButton.setOnClickListener(this);
        toolbarTextView = (TextView) getActivity().findViewById(R.id.HomeNetFeedToolbarTextView);
        toolbarTextView.setText("My Houses");
        houseSpinner = (MaterialSpinner) currentView.findViewById(R.id.MyHouseSelectHouseSpinner);
        houseChart = (BarChart) currentView.findViewById(R.id.MyHouseHouseMetricsChart);
        houseSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                House selectedHouse = (House) houseSpinner.getItems().get(position);
                getHouseMetrics(selectedHouse);

            }
        });
    }

    private void getHouseMetrics(House selectedHouse) {
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Fetching latest house data. Please wait...");
        dialog.setCancelable(false);
        dialog.show();
        Call<SingleResponse<HouseViewModel>> dataCall = service.generateHouseMetricsReport("Bearer "+sharedPreferences.getString("authorization_token", ""), selectedHouse.getHouseID(), getResources().getString(R.string.homenet_client_string));
        dataCall.enqueue(new Callback<SingleResponse<HouseViewModel>>() {
            @Override
            public void onResponse(Call<SingleResponse<HouseViewModel>> call, Response<SingleResponse<HouseViewModel>> response) {
                if (dialog.isShowing()) {
                    dialog.cancel();
                }
                if (response.isSuccessful()) {
                    HouseViewModel viewModel = response.body().getModel();
                    if (viewModel != null) {
                        houseNameTextView.setText(viewModel.getName());
                        descriptionTextView.setText(viewModel.getDescription());
                        totalMembersTextView.setText(Integer.toString(viewModel.getTotalMembers()));
                        dateCreatedTextView.setText(viewModel.getDateCreated());
                        ownerTextView.setText(viewModel.getOwner());
                        totalBannedTextView.setText(Integer.toString(viewModel.getBannedUsers()));
                        drawGraph(viewModel);
                    }
                } else {
                    try {
                        displayMessage("Error", response.errorBody().string(), null);
                    } catch (Exception ex) {}
                }
            }

            @Override
            public void onFailure(Call<SingleResponse<HouseViewModel>> call, Throwable t) {
                if (dialog.isShowing()) {
                    dialog.cancel();
                }
                displayMessage("Critical Error", t.getMessage(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        getActivity().getFragmentManager().popBackStack();
                    }
                });
            }
        });

    }
    private void initializeRetrofit() {
        protocolList = new ArrayList<>();
        protocolList.add(Protocol.HTTP_1_1);
        client = new OkHttpClient.Builder().connectTimeout(2, TimeUnit.MINUTES).readTimeout(2, TimeUnit.MINUTES).protocols(protocolList).build();
        retrofit = new Retrofit.Builder().baseUrl(getResources().getString(R.string.homenet_link)).client(client).addConverterFactory(GsonConverterFactory.create()).build();
        service = retrofit.create(HomeNetService.class);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.MyHousesLeaveHouseButton:
                int selectedIndex = houseSpinner.getSelectedIndex();
                House selectedHouse = (House) houseSpinner.getItems().get(selectedIndex);
                final ProgressDialog dialog = new ProgressDialog(getActivity());
                dialog.setMessage("Leaving house. Please wait...");
                dialog.setCancelable(false);
                dialog.show();
                Call<SingleResponse<HouseMember>> leaveCall = service.leaveHouse("Bearer "+sharedPreferences.getString("authorization_token", ""), selectedHouse.getHouseID(), sharedPreferences.getString("emailAddress", ""), getResources().getString(R.string.homenet_client_string));
                leaveCall.enqueue(new Callback<SingleResponse<HouseMember>>() {
                    @Override
                    public void onResponse(Call<SingleResponse<HouseMember>> call, Response<SingleResponse<HouseMember>> response) {
                        if (dialog.isShowing()) {
                            dialog.cancel();
                        }
                        if (response.isSuccessful()) {
                            displayMessage("Success", "You have successfully left the selected house", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    getActivity().getFragmentManager().popBackStack();
                                }
                            });
                        } else {
                            try {
                                displayMessage("Critical Error", response.errorBody().string(), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                });
                            } catch (Exception error) {

                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<SingleResponse<HouseMember>> call, Throwable t) {
                        if (dialog.isShowing()) {
                            dialog.cancel();
                        }
                    }
                });
                break;
        }
    }
    private void getSubscribedHouses() {
        dialog.setCancelable(false);
        dialog.setMessage("Fetching latest house data. Please wait... ");
        dialog.show();
        RequestBody emailBody = RequestBody.create(MultipartBody.FORM, sharedPreferences.getString("emailAddress", ""));
        Call<ListResponse<House>> houseCall = service.getSubscribedHouses("Bearer "+sharedPreferences.getString("authorization_token", ""), emailBody, getResources().getString(R.string.homenet_client_string));
        houseCall.enqueue(new Callback<ListResponse<House>>() {
            @Override
            public void onResponse(Call<ListResponse<House>> call, Response<ListResponse<House>> response) {
                if (response.isSuccessful()) {
                    if (response.body().getModel() != null) {
                        for(House thisHouse : response.body().getModel()) {
                            subscribedHouseList.add(thisHouse);
                        }
                    }
                    if (subscribedHouseList.size() > 0) {
                        houseSpinner.setItems(subscribedHouseList);
                        House defaultHouse = subscribedHouseList.get(0);
                        //Call which will get the
                        Call<SingleResponse<HouseViewModel>> dataCall = service.generateHouseMetricsReport("Bearer "+sharedPreferences.getString("authorization_token", ""), defaultHouse.getHouseID(), getResources().getString(R.string.homenet_client_string));
                        dataCall.enqueue(new Callback<SingleResponse<HouseViewModel>>() {
                            @Override
                            public void onResponse(Call<SingleResponse<HouseViewModel>> call, Response<SingleResponse<HouseViewModel>> response) {
                                if (dialog.isShowing()) {
                                    dialog.cancel();
                                }
                                if (response.isSuccessful()) {
                                    HouseViewModel viewModel = response.body().getModel();
                                    if (viewModel != null) {
                                        houseNameTextView.setText(viewModel.getName());
                                        descriptionTextView.setText(viewModel.getDescription());
                                        totalMembersTextView.setText(Integer.toString(viewModel.getTotalMembers()));
                                        dateCreatedTextView.setText(viewModel.getDateCreated());
                                        ownerTextView.setText(viewModel.getOwner());
                                        totalBannedTextView.setText(Integer.toString(viewModel.getBannedUsers()));
                                        drawGraph(viewModel);
                                    } else {
                                        displayMessage("No Subscribed Houses", "No subscribed houses were found", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                getActivity().getFragmentManager().popBackStack();
                                            }
                                        });
                                    }
                                } else {
                                    try {
                                        displayMessage("Error", response.errorBody().string(), null);
                                    } catch (Exception ex) {}
                                }
                            }

                            @Override
                            public void onFailure(Call<SingleResponse<HouseViewModel>> call, Throwable t) {
                                if (dialog.isShowing()) {
                                    dialog.cancel();
                                }
                                displayMessage("Critical Error", t.getMessage(), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        getActivity().getFragmentManager().popBackStack();
                                    }
                                });
                            }
                        });




                    }
                } else {
                    dialog.cancel();
                    displayMessage("Error Getting Houses", response.errorBody().toString(), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            getActivity().getFragmentManager().popBackStack();
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<ListResponse<House>> call, Throwable t) {
                if(dialog.isShowing()) {
                    dialog.cancel();
                }
                displayMessage("Critical Error", t.getMessage(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        getActivity().getFragmentManager().popBackStack();
                    }
                });
            }
        });
    }

    private void drawGraph(HouseViewModel viewModel) {
        List<BarEntry> entryList = new ArrayList<>();
        entryList.add(new BarEntry(0, viewModel.getTotalPosts()));
        entryList.add(new BarEntry(1, viewModel.getTotalAnnouncements()));
        entryList.add(new BarEntry(2, viewModel.getTotalComments()));
        BarDataSet barDataSet = new BarDataSet(entryList, "House overview report");
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        BarData barData = new BarData(barDataSet);
        XAxis axis = houseChart.getXAxis();
        axis.setGranularity(1f);
        axis.setPosition(XAxis.XAxisPosition.BOTTOM);
        axis.setValueFormatter(new IAxisValueFormatter() {
            String [] values = new String [] {"Posts", "Announcements", "Comments"};
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return values[(int) value];
            }
        });
        houseChart.setData(barData);
        houseChart.getAxisLeft().setDrawGridLines(false);
        houseChart.getXAxis().setDrawGridLines(false);
        houseChart.invalidate();



    }

    private void displayMessage(String title, String message, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder messageBox = new AlertDialog.Builder(getActivity());
        messageBox.setTitle(title).setMessage(message).setPositiveButton("Okay", listener).setCancelable(false);
        messageBox.show();
    }
}
