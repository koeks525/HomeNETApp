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
public class BannedUsersFragment extends Fragment {

    private RecyclerView bannedUsersRecylcerView;
    private List<User> bannedUserList = new ArrayList<>();
    private HouseUsersAdapter adapter;

    public BannedUsersFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View currentView = inflater.inflate(R.layout.fragment_banned_users, container, false);
        initializeComponents(currentView);
        return currentView;
    }

    private void initializeComponents(View currentView) {
        bannedUserList.clear();
        createFakeUsers();
        bannedUsersRecylcerView = (RecyclerView) currentView.findViewById(R.id.BannedUsersRecyclerView);
        bannedUsersRecylcerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new HouseUsersAdapter(bannedUserList);
        bannedUsersRecylcerView.setAdapter(adapter);

    }

    private void createFakeUsers() {
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

    }

}
