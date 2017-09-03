package HomeNETStream;


import android.app.FragmentTransaction;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
public class FeedFragment extends Fragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView recyclerView;
    private FloatingActionButton refreshButton, newPostButton;
    private ArrayList<House> houseList;
    private HomeNetFeedAdapter feedAdapter;
    private HomeNetFeedTask feedTask;
    private TextView toolbarTextView;
    private SwipeRefreshLayout swipeRefreshLayout;

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
        swipeRefreshLayout = (SwipeRefreshLayout) currentView.findViewById(R.id.FeedItemRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //refreshButton = (FloatingActionButton) currentView.findViewById(R.id.FeedRefreshButton);
        newPostButton = (FloatingActionButton) currentView.findViewById(R.id.HomeNetFeedNewPostButton);
        newPostButton.setOnClickListener(this);
        //refreshButton.setOnClickListener(this);
        toolbarTextView = (TextView) getActivity().findViewById(R.id.HomeNetFeedToolbarTextView);
        toolbarTextView.setText("Your Feed");
    }

    private void runFeedTask() {
        feedTask = new HomeNetFeedTask(getActivity(), recyclerView, swipeRefreshLayout);
        feedTask.execute();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            /*case R.id.FeedRefreshButton:
                feedTask = new HomeNetFeedTask(getActivity(), recyclerView);
                feedTask.execute();
                break;*/
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

    @Override
    public void onRefresh() {
        feedTask = new HomeNetFeedTask(getActivity(), recyclerView, swipeRefreshLayout);
        feedTask.execute();
    }
}
