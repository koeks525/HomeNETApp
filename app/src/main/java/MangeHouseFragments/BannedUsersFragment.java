package MangeHouseFragments;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
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

import Adapters.BannedUsersAdapter;
import Adapters.EmptyAdapter;
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
public class BannedUsersFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView bannedUsersRecylcerView;
    private House selectedHouse;
    //private List<User> bannedUserList = new ArrayList<>();
    private OkHttpClient client;
    private HomeNetService service;
    private List<Protocol> protocolList;
    private Retrofit retrofit;
    private SharedPreferences sharedPreferences;
    private SwipeRefreshLayout refreshLayout;

    public BannedUsersFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View currentView = inflater.inflate(R.layout.fragment_banned_users, container, false);
        initializeComponents(currentView);
        initializeRetrofit();
        if (savedInstanceState != null) {
            selectedHouse = (House) savedInstanceState.getSerializable("SelectedHouse");
        } else {
            selectedHouse = (House) getArguments().getSerializable("SelectedHouse");
        }
        executeBannedUsersTask();
        return currentView;
    }

    private void initializeComponents(View currentView) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        bannedUsersRecylcerView = (RecyclerView) currentView.findViewById(R.id.BannedUsersRecyclerView);
        bannedUsersRecylcerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        bannedUsersRecylcerView.setAdapter(new EmptyAdapter());
        refreshLayout = (SwipeRefreshLayout) currentView.findViewById(R.id.BannedUsersRefreshLayout);
        refreshLayout.setOnRefreshListener(this);
    }

    private void executeBannedUsersTask() {
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Fetching banned members. Please wait...");
        dialog.setCancelable(false);
        dialog.show();
        Call<ListResponse<HouseMemberViewModel>> bannedCall = service.getBannedHouseMembers("Bearer "+sharedPreferences.getString("authorization_token",""), selectedHouse.getHouseID(), sharedPreferences.getString("emailAddress", ""), getResources().getString(R.string.homenet_client_string));
        bannedCall.enqueue(new Callback<ListResponse<HouseMemberViewModel>>() {
            @Override
            public void onResponse(Call<ListResponse<HouseMemberViewModel>> call, Response<ListResponse<HouseMemberViewModel>> response) {
                refreshLayout.setRefreshing(false);
                if (dialog.isShowing()) {
                    dialog.cancel();
                }
                if (response.isSuccessful()) {
                    List<HouseMemberViewModel> bannedList = response.body().getModel();
                    if (bannedList != null) {
                        BannedUsersAdapter adapter = new BannedUsersAdapter(bannedList);
                        bannedUsersRecylcerView.setAdapter(adapter);
                    } else {
                        displaySnackbar("No banned users found");
                    }
                } else {
                    displaySnackbar("No banned users found");
                }
            }
            @Override
            public void onFailure(Call<ListResponse<HouseMemberViewModel>> call, Throwable t) {
                refreshLayout.setRefreshing(false);
                if (dialog.isShowing()) {
                    dialog.cancel();
                }
                displayMessage("Critical Error", t.getMessage(), null);
            }
        });
    }

    private void displayMessage(String title, String message, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title).setMessage(message).setCancelable(false).setPositiveButton("Got it", listener).show();
    }

    private void displaySnackbar(String message) {

    }

    private void initializeRetrofit() {
        protocolList = new ArrayList<>();
        protocolList.add(Protocol.HTTP_1_1);
        client = new OkHttpClient.Builder().connectTimeout(2, TimeUnit.MINUTES).readTimeout(2, TimeUnit.MINUTES).protocols(protocolList).build();
        retrofit = new Retrofit.Builder().baseUrl(getResources().getString(R.string.homenet_link)).addConverterFactory(GsonConverterFactory.create()).client(client).build();
        service = retrofit.create(HomeNetService.class);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("SelectedHouse", selectedHouse);
    }


    @Override
    public void onRefresh() {
        refreshLayout.setRefreshing(true);
        executeBannedUsersTask();
    }
}
