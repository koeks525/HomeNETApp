package MangeHouseFragments;


import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.koeksworld.homenet.R;

import java.util.ArrayList;
import java.util.List;

import Adapters.HouseUsersAdapter;
import Models.House;
import Models.User;
import Tasks.ManageUsersTask;

/**
 * A simple {@link Fragment} subclass.
 */
public class PendingUsersFragment extends Fragment {

    private House selectedHouse;
    private RecyclerView pendingUsersRecylcerView;
    private ManageUsersTask task;

    public PendingUsersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View currentView = inflater.inflate(R.layout.fragment_pending_users, container, false);
        initializeComponents(currentView);
        if (savedInstanceState != null) {
            selectedHouse = (House) savedInstanceState.getSerializable("SelectedHouse");
        } else {
            selectedHouse = (House) getArguments().getSerializable("SelectedHouse");
        }
        executeGetPendingUsers();
        return currentView;
    }

    private void initializeComponents(View currentView) {
        pendingUsersRecylcerView = (RecyclerView)currentView.findViewById(R.id.PendingUsersRecyclerView);
        pendingUsersRecylcerView.setLayoutManager(new LinearLayoutManager(getActivity()));

    }

    private void executeGetPendingUsers() {
        task = new ManageUsersTask(getActivity(), pendingUsersRecylcerView, selectedHouse, 2);
        task.execute();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("SelectedHouse", selectedHouse);
    }
}
