package MangeHouseFragments;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.preference.PreferenceManager;
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

import Adapters.PendingUsersAdapter;
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
public class PendingUsersFragment extends Fragment {

    private House selectedHouse;
    private RecyclerView pendingUsersRecylcerView;
    private List<Protocol> protocolList;
    private HomeNetService service;
    private SharedPreferences sharedPreferences;
    private OkHttpClient client;
    private Retrofit retrofit;
    private List<HouseMemberViewModel> pendingUserList;

    public PendingUsersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View currentView = inflater.inflate(R.layout.fragment_pending_users, container, false);
        initializeComponents(currentView);
        initializeRetrofit();
        if (savedInstanceState != null) {
            selectedHouse = (House) savedInstanceState.getSerializable("SelectedHouse");
        } else {
            selectedHouse = (House) getArguments().getSerializable("SelectedHouse");
        }

        getPendingUsers();

        return currentView;
    }

    private void initializeComponents(View currentView) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        pendingUserList = new ArrayList<>();
        pendingUsersRecylcerView = (RecyclerView)currentView.findViewById(R.id.PendingUsersRecyclerView);
        pendingUsersRecylcerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void initializeRetrofit() {
        protocolList = new ArrayList<>();
        protocolList.add(Protocol.HTTP_1_1);
        client = new OkHttpClient.Builder().connectTimeout(2, TimeUnit.MINUTES).readTimeout(2, TimeUnit.MINUTES).protocols(protocolList).build();
        retrofit = new Retrofit.Builder().baseUrl(getResources().getString(R.string.homenet_link)).addConverterFactory(GsonConverterFactory.create()).client(client).build();
        service = retrofit.create(HomeNetService.class);
    }

    private void getPendingUsers() {
        pendingUserList.clear();
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Fetching pending users. Please wait...");
        dialog.setCancelable(false);
        dialog.show();
        Call<ListResponse<HouseMemberViewModel>> userCall = service.getPendingHouseUsers("Bearer "+sharedPreferences.getString("authorization_token",""), selectedHouse.getHouseID(), getResources().getString(R.string.homenet_client_string));
        userCall.enqueue(new Callback<ListResponse<HouseMemberViewModel>>() {
            @Override
            public void onResponse(Call<ListResponse<HouseMemberViewModel>> call, Response<ListResponse<HouseMemberViewModel>> response) {
                if (dialog.isShowing()) {
                    dialog.cancel();
                }
                if (response.isSuccessful()) {
                    if (response.body().getModel() != null) {
                        for(HouseMemberViewModel model : response.body().getModel()) {
                            pendingUserList.add(model);
                        }
                    }
                    if (pendingUserList.size() > 0) {
                        PendingUsersAdapter adapter = new PendingUsersAdapter(pendingUserList, getActivity(), selectedHouse);
                        pendingUsersRecylcerView.setAdapter(adapter);
                    } else {
                        displaySnackbar("No pending users found. Refresh to update");
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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("SelectedHouse", selectedHouse);
    }

    private void displayMessage(String title, String message, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title).setMessage(message).setPositiveButton("Okay", listener).setCancelable(false);
        builder.show();
    }
}
