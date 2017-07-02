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
import Models.User;

/**
 * A simple {@link Fragment} subclass.
 */
public class PendingUsersFragment extends Fragment {

    private List<User> pendingUsers = new ArrayList<>();
    private HouseUsersAdapter adapter;
    private RecyclerView pendingUsersRecylcerView;

    public PendingUsersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View currentView = inflater.inflate(R.layout.fragment_pending_users, container, false);
        initializeComponents(currentView);
        return currentView;
    }

    private void initializeComponents(View currentView) {
        pendingUsers.clear();
        setFakeData();
        pendingUsersRecylcerView = (RecyclerView)currentView.findViewById(R.id.PendingUsersRecyclerView);
        pendingUsersRecylcerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new HouseUsersAdapter(pendingUsers);
        pendingUsersRecylcerView.setAdapter(adapter);
    }

    private void setFakeData() {
        pendingUsers.add(new User(2, "Ngada", "Okuhle", "okuhle.ngada@outlook.com", "1994-10-13", "koeks525", "Okuhle*1994", "South Africa", "Me", "2017-06-15", 0, "Male", 1, ""));
        pendingUsers.add(new User(1, "Simmons", "Madea", "madea.simmons@outlook.com", "1984-10-13", "mabel2244", "Okuhle*1994", "United States", "Me", "2017-06-15", 0, "Male", 1, ""));
        pendingUsers.add(new User(4, "Ngumbela", "Lihle", "leengumbela@gmail.com", "1994-10-13", "leengumbela", "Okuhle*1994", "South Africa", "Me", "2017-06-15", 0, "Male", 1, ""));
    }

}
