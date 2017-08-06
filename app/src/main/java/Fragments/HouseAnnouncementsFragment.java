package Fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.koeksworld.homenet.R;

import java.util.ArrayList;
import java.util.List;

import Adapters.MessagesAdapter;
import Models.House;
import Models.HouseAnnouncement;
import Tasks.GetHouseAnnouncementsTask;

/**
 * A simple {@link Fragment} subclass.
 */
public class HouseAnnouncementsFragment extends Fragment {

    private RecyclerView announcementsRecyclerView;
    private List<HouseAnnouncement> houseAnnouncementList = new ArrayList<>();
    private MessagesAdapter adapter;
    private House selectedHouse;
    private GetHouseAnnouncementsTask task;
    public HouseAnnouncementsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View currentView = inflater.inflate(R.layout.fragment_house_announcements, container, false);
        initializeComponents(currentView);
        if (savedInstanceState != null) {
            selectedHouse = savedInstanceState.getParcelable("SelectedHouse");
        } else {
            selectedHouse = getArguments().getParcelable("SelectedHouse");
        }
        task = new GetHouseAnnouncementsTask(getActivity(), announcementsRecyclerView, selectedHouse.getHouseID());
        task.execute();
        return currentView;
    }

    private void initializeComponents(View currentView) {
        announcementsRecyclerView = (RecyclerView) currentView.findViewById(R.id.AnnouncementsRecyclerView);
        announcementsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        announcementsRecyclerView.setAdapter(adapter);
    }



}
