package HomeNETStream;


import android.app.FragmentTransaction;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.koeksworld.homenet.R;

import Tasks.GetHouseAnnouncementsTask;
import Tasks.GetUserAnnouncementsTask;

/**
 * A simple {@link Fragment} subclass.
 */
public class AnnoucementFragment extends Fragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;
    private FloatingActionButton newAnnouncementButton;
    private GetUserAnnouncementsTask task;
    private TextView toolbarTextView;

    public AnnoucementFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View currentView = inflater.inflate(R.layout.fragment_annoucement, container, false);
        initializeComponents(currentView);
        task = new GetUserAnnouncementsTask(getActivity(), recyclerView, refreshLayout);
        task.execute();
        return currentView;
    }

    private void initializeComponents(View currentView) {
        recyclerView = (RecyclerView) currentView.findViewById(R.id.ViewAnnouncementsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        refreshLayout = (SwipeRefreshLayout) currentView.findViewById(R.id.AnnouncementsSwipeLayout);
        refreshLayout.setOnRefreshListener(this);
        newAnnouncementButton = (FloatingActionButton) currentView.findViewById(R.id.NewAnnouncementButton);
        newAnnouncementButton.setOnClickListener(this);
        toolbarTextView = (TextView) getActivity().findViewById(R.id.HomeNetFeedToolbarTextView);
        toolbarTextView.setText("Announcements");
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.NewAnnouncementButton:
                NewAnnouncementFragment fragment = new NewAnnouncementFragment();
                FragmentTransaction transaction = getActivity().getFragmentManager().beginTransaction();
                transaction.replace(R.id.HomeNetFeedContentView, fragment, null);
                transaction.addToBackStack(null);
                transaction.commit();
                break;
        }
    }

    @Override
    public void onRefresh() {
        task = new GetUserAnnouncementsTask(getActivity(), recyclerView, refreshLayout);
        task.execute();
    }
}
