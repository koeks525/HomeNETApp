package MangeHouseFragments;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.koeksworld.homenet.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import Adapters.HouseUsersAdapter;
import Communication.HomeNetService;
import Models.House;
import Models.HouseMemberViewModel;
import ResponseModels.ListResponse;
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
public class ActiveUsersFragment extends Fragment implements View.OnClickListener {


    //private List<User> bannedUserList = new ArrayList<>();
    private RecyclerView activeUsersRecyclerView;
    //private HouseUsersAdapter adapter;
    private House selectedHouse;
    private FloatingActionButton refreshButton;
    private OkHttpClient client;
    private HomeNetService service;
    private Retrofit retrofit;
    private List<Protocol> protocolList;
    private SharedPreferences sharedPreferences;
    private List<HouseMemberViewModel> housePostList;

    public ActiveUsersFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View currentView =  inflater.inflate(R.layout.fragment_active_users, container, false);
        if (savedInstanceState != null) {
            selectedHouse = savedInstanceState.getParcelable("SelectedHouse");
        } else {
            Bundle bundle = getArguments();
            selectedHouse = bundle.getParcelable("SelectedHouse");
        }
        initializeComponents(currentView);
        initializeRetrofit();
        executeGetActiveUsers();
        return currentView;
    }

    private void initializeComponents(View currentView) {
        housePostList = new ArrayList<>();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        activeUsersRecyclerView = (RecyclerView) currentView.findViewById(R.id.ActiveUsersRecyclerView);
        activeUsersRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        refreshButton = (FloatingActionButton) currentView.findViewById(R.id.ActiveUsersFloatingButton);
        refreshButton.setOnClickListener(this);

    }

    private void initializeRetrofit() {
        protocolList = new ArrayList<>();
        protocolList.add(Protocol.HTTP_1_1);
        client = new OkHttpClient.Builder().connectTimeout(2, TimeUnit.MINUTES).readTimeout(2, TimeUnit.MINUTES).protocols(protocolList).build();
        retrofit = new Retrofit.Builder().baseUrl(getResources().getString(R.string.homenet_link)).addConverterFactory(GsonConverterFactory.create()).client(client).build();
        service = retrofit.create(HomeNetService.class);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ActiveUsersFloatingButton:
                executeGetActiveUsers();
                break;
        }
    }

    private void executeGetActiveUsers() {
        housePostList.clear();
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Fetching active user list. Please wait...");
        dialog.setCancelable(false);
        dialog.show();
        Call<ListResponse<HouseMemberViewModel>> activeList = service.getActiveHouseMembers("Bearer "+sharedPreferences.getString("authorization_token", ""), selectedHouse.getHouseID(), getResources().getString(R.string.homenet_client_string));
        activeList.enqueue(new Callback<ListResponse<HouseMemberViewModel>>() {
            @Override
            public void onResponse(Call<ListResponse<HouseMemberViewModel>> call, Response<ListResponse<HouseMemberViewModel>> response) {
                if (dialog.isShowing()) {
                    dialog.cancel();
                }
                if (response.isSuccessful()) {
                    if (response.body().getModel() != null) {
                        for(HouseMemberViewModel model : response.body().getModel())  {
                            housePostList.add(model);
                        }
                    }
                    if (housePostList.size() > 0) {
                        HouseUsersAdapter adapter = new HouseUsersAdapter(housePostList);
                        activeUsersRecyclerView.setAdapter(adapter);
                    } else {
                        displaySnackbar("No active users found. Refresh to update");
                    }
                }
            }

            @Override
            public void onFailure(Call<ListResponse<HouseMemberViewModel>> call, Throwable t) {
                if (dialog.isShowing()) {
                    dialog.cancel();
                }
                displayMessage("Critical Error", t.getMessage(), null);
            }
        });

    }

    private void displaySnackbar(String message) {
        Snackbar.make(getView(), message, Snackbar.LENGTH_LONG).show();
    }

    private void displayMessage(String title, String message, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title).setMessage(message).setCancelable(false).setPositiveButton("Got it", listener);
        builder.show();
    }
}
