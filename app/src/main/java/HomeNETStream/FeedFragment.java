package HomeNETStream;


import android.app.FragmentTransaction;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.koeksworld.homenet.R;

import java.util.ArrayList;

import Adapters.HomeNetFeedAdapter;
import Models.House;
import Models.HouseMember;
import Models.HousePost;
import Models.HousePostMetaData;
import Models.HousePostMetaDataViewModel;
import Models.User;
import Tasks.HomeNetFeedTask;

/**
 * A simple {@link Fragment} subclass.
 */
public class FeedFragment extends Fragment implements View.OnClickListener {

    private RecyclerView recyclerView;
    private FloatingActionButton refreshButton;
    private ArrayList<House> houseList;
    private HomeNetFeedAdapter feedAdapter;
    private HomeNetFeedTask feedTask;

    public FeedFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View currentView = inflater.inflate(R.layout.fragment_feed, container, false);
        initializeComponents(currentView);
        runFeedTask();
        return currentView;
    }

    private void initializeComponents(View currentView) {
        recyclerView = (RecyclerView) currentView.findViewById(R.id.HomeNetFeedRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        refreshButton = (FloatingActionButton) currentView.findViewById(R.id.FeedRefreshButton);
        refreshButton.setOnClickListener(this);
    }

    private void runFeedTask() {
        feedTask = new HomeNetFeedTask(getActivity(), recyclerView);
        feedTask.execute();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.FeedRefreshButton:
                feedTask = new HomeNetFeedTask(getActivity(), recyclerView);
                feedTask.execute();
                break;
            case R.id.HomeNetFeedNewPostButton:
                Bundle fragBundle = new Bundle();
                fragBundle.putParcelableArrayList("HouseList", houseList);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                NewPostFragment fragment = new NewPostFragment();
                transaction.replace(R.id.HomeNetFeedContentView, fragment, null);
                transaction.addToBackStack("NewPostTransaction");
                transaction.commit();

                break;
        }
    }

   /* private void checkData(View currentView) {
        if (housePostList == null || houseMemberList.size() == 0) {
            Snackbar.make(currentView.findViewById(R.id.HomeNetFeedView), "No Feed Data Found.", Snackbar.LENGTH_INDEFINITE).show();
        }
    }*/

   /* private void getData() {
        housePostList = getArguments().getParcelableArrayList("HousePostList");
        houseMemberList = getArguments().getParcelableArrayList("MembershipList");
        userList = getArguments().getParcelableArrayList("UserList");
        metaDataList = getArguments().getParcelableArrayList("HousePostMetaData");
        houseList = getArguments().getParcelableArrayList("HouseList");
    }*/

    /*private void setFeedAdapter() {
        feedAdapter = new HomeNetFeedAdapter(housePostList, userList, houseMemberList, metaDataList, getActivity());
        recyclerView.setAdapter(feedAdapter);
    }*/
}
