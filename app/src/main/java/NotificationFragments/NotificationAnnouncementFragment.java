package NotificationFragments;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.koeksworld.homenet.HomeNetFeedActivity;
import com.koeksworld.homenet.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import Communication.HomeNetService;
import Models.HouseAnnouncement;
import Models.HouseAnnouncementViewModel;
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
public class NotificationAnnouncementFragment extends Fragment {

    private TextView announcementTitleTextView, announcementMessageTextView;
    private SharedPreferences sharedPreferences;
    private Retrofit retrofit;
    private List<Protocol> protocolList;
    private OkHttpClient client;
    private HomeNetService service;
    private int announcementID;
    private HouseAnnouncementViewModel announcementModel;
    private TextView toolbarTextView;

    public NotificationAnnouncementFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View currentView = inflater.inflate(R.layout.fragment_notification_announcement, container, false);
        initializeComponents(currentView);
        initializeRetrofit();
        return currentView;
    }

    private void getData() {
        announcementID = getArguments().getInt("announcementID");
        if (announcementID <= 0) {
            displayMessage("No Data", "No announcement data found. You will be taken to your news feed.", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent mainIntent = new Intent(getActivity(), HomeNetFeedActivity.class);
                    startActivity(mainIntent);
                    getActivity().finish();
                }
            });
        } else {
            getAnnouncementData();
        }
    }
    private void getAnnouncementData() {
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Getting announcement data. Please wait...");
        dialog.setCancelable(false);
        dialog.show();
        Call<SingleResponse<HouseAnnouncementViewModel>> announceCall = service.getHouseAnnouncement("Bearer "+sharedPreferences.getString("authorization_token", ""), announcementID, getResources().getString(R.string.homenet_client_string));
        announceCall.enqueue(new Callback<SingleResponse<HouseAnnouncementViewModel>>() {
            @Override
            public void onResponse(Call<SingleResponse<HouseAnnouncementViewModel>> call, Response<SingleResponse<HouseAnnouncementViewModel>> response) {
                if (dialog.isShowing()) {
                    dialog.cancel();
                }
                if (response.isSuccessful()) {
                    if (response.body().getModel() != null) {
                        announcementModel = response.body().getModel();
                    }
                    if (announcementModel != null) {
                        announcementTitleTextView.setText(announcementModel.getTitle());
                        announcementMessageTextView.setText(announcementModel.getMessage());
                    }
                } else {
                    displayMessage("Error", "Something went wrong with getting announcement data. You will be taken to your news feed", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent thisOne = new Intent(getActivity(), HomeNetFeedActivity.class);
                            startActivity(thisOne);
                            getActivity().finish();
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<SingleResponse<HouseAnnouncementViewModel>> call, Throwable t) {
                if (dialog.isShowing()) {
                    dialog.cancel();
                }
            }
        });


    }
    private void initializeComponents(View currentView) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        announcementTitleTextView = (TextView) currentView.findViewById(R.id.NotificationAnnouncementTitleTextView);
        announcementMessageTextView = (TextView) currentView.findViewById(R.id.NotificationAnnouncementMessageTextView);
        toolbarTextView = (TextView) getActivity().findViewById(R.id.NotificationAnnouncementTitleToolbarTextView);
        toolbarTextView.setText("New Announcement");

    }
    private void initializeRetrofit() {
        protocolList = new ArrayList<>();
        protocolList.add(Protocol.HTTP_1_1);
        client = new OkHttpClient.Builder().readTimeout(2, TimeUnit.MINUTES).connectTimeout(2, TimeUnit.MINUTES).protocols(protocolList).build();
        retrofit = new Retrofit.Builder().baseUrl(getResources().getString(R.string.homenet_link)).addConverterFactory(GsonConverterFactory.create()).client(client).build();
        service = retrofit.create(HomeNetService.class);
    }
    private void displayMessage(String title, String message, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title).setMessage(message).setPositiveButton("Okay", listener).setCancelable(false).show();
    }
}
