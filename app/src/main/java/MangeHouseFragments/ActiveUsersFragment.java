package MangeHouseFragments;


import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
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
public class ActiveUsersFragment extends Fragment implements View.OnClickListener {


    //private List<User> bannedUserList = new ArrayList<>();
    private RecyclerView activeUsersRecyclerView;
    //private HouseUsersAdapter adapter;
    private ManageUsersTask task;
    private House selectedHouse;
    private FloatingActionButton refreshButton;
    public ActiveUsersFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View currentView =  inflater.inflate(R.layout.fragment_active_users, container, false);
        if (savedInstanceState != null) {
            selectedHouse = (House) savedInstanceState.getSerializable("SelectedHouse");
        } else {
            selectedHouse = (House) getArguments().getSerializable("SelectedHouse");
        }
        initializeComponents(currentView);
        return currentView;
    }

    private void initializeComponents(View currentView) {
        activeUsersRecyclerView = (RecyclerView) currentView.findViewById(R.id.ActiveUsersRecyclerView);
        activeUsersRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        refreshButton = (FloatingActionButton) currentView.findViewById(R.id.ActiveUsersFloatingButton);
        refreshButton.setOnClickListener(this);
        task = new ManageUsersTask(getActivity(), activeUsersRecyclerView, selectedHouse, 1);
        task.execute();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ActiveUsersFloatingButton:
                task = new ManageUsersTask(getActivity(), activeUsersRecyclerView, selectedHouse, 1);
                task.execute();
                break;
        }
    }





    /*private void setFakeData() {
        bannedUserList.add(new User(2, "Ngada", "Okuhle", "okuhle.ngada@outlook.com", "1994-10-13", "koeks525", "Okuhle*1994", "South Africa", "Me", "2017-06-15", 0, "Male", 1, ""));
        bannedUserList.add(new User(1, "Simmons", "Madea", "madea.simmons@outlook.com", "1984-10-13", "mabel2244", "Okuhle*1994", "United States", "Me", "2017-06-15", 0, "Male", 1, ""));
        bannedUserList.add(new User(4, "Ngumbela", "Lihle", "leengumbela@gmail.com", "1994-10-13", "leengumbela", "Okuhle*1994", "South Africa", "Me", "2017-06-15", 0, "Male", 1, ""));
        bannedUserList.add(new User(5, "Reed", "Alexandra", "alexreid@rania.com", "1996-10-13", "thisFish201", "Okuhle*1994", "South Africa", "Me", "2017-06-15", 0, "Male", 1, ""));
        bannedUserList.add(new User(0, "Jackson", "Janet", "janetjackson@outlook.com", "1950-10-13", "koeks525", "Okuhle*1994", "South Africa", "Me", "2017-06-15", 0, "Male", 1, ""));
        bannedUserList.add(new User(2, "Ngada", "Okuhle", "okuhle.ngada@outlook.com", "1994-10-13", "koeks525", "Okuhle*1994", "South Africa", "Me", "2017-06-15", 0, "Male", 1, ""));
        bannedUserList.add(new User(1, "Simmons", "Madea", "madea.simmons@outlook.com", "1984-10-13", "mabel2244", "Okuhle*1994", "United States", "Me", "2017-06-15", 0, "Male", 1, ""));
        bannedUserList.add(new User(4, "Ngumbela", "Lihle", "leengumbela@gmail.com", "1994-10-13", "leengumbela", "Okuhle*1994", "South Africa", "Me", "2017-06-15", 0, "Male", 1, ""));
        bannedUserList.add(new User(5, "Reed", "Alexandra", "alexreid@rania.com", "1996-10-13", "thisFish201", "Okuhle*1994", "South Africa", "Me", "2017-06-15", 0, "Male", 1, ""));
        bannedUserList.add(new User(0, "Jackson", "Janet", "janetjackson@outlook.com", "1950-10-13", "koeks525", "Okuhle*1994", "South Africa", "Me", "2017-06-15", 0, "Male", 1, ""));
    }*/

}
