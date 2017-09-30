package MangeHouseFragments;


import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.koeksworld.homenet.R;

import java.util.ArrayList;
import java.util.List;

import Adapters.FlaggedPostsAdapter;
import Adapters.PendingPostsAdapter;
import Models.House;
import Models.HousePostFlag;
import Tasks.GetHousePostsTask;

/**
 * A simple {@link Fragment} subclass.
 */
public class ResolvedPostsFragment extends Fragment implements View.OnClickListener {


    private House selectedHouse;
    private RecyclerView resolvedRecyclerView;
    private GetHousePostsTask task;
    private FloatingActionButton refreshButton;
    public ResolvedPostsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View currentView = inflater.inflate(R.layout.fragment_resolved_posts, container, false);
        initializeComponents(currentView);
        if (savedInstanceState != null) {
            selectedHouse = (House) savedInstanceState.getSerializable("SelectedHouse");
        } else {
            selectedHouse = (House) getArguments().getSerializable("SelectedHouse");
        }
        getData();
        return currentView;
    }

    private void initializeComponents(View currentView) {
        resolvedRecyclerView = (RecyclerView) currentView.findViewById(R.id.ResolvedPostsRecyclerView);
        resolvedRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        refreshButton = (FloatingActionButton) currentView.findViewById(R.id.ResolvedPostsRefreshButton);
        refreshButton.setOnClickListener(this);
    }

    private void getData() {
       task = new GetHousePostsTask(getActivity(), selectedHouse.getHouseID(), 2);
        task.execute();
    }

    @Override
    public void onClick(View view) {
        if (selectedHouse != null) {
            getData();
        }
    }
}
