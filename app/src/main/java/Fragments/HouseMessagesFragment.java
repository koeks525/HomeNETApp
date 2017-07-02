package Fragments;


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

import Adapters.MessagesAdapter;
import Models.HouseAnnouncement;

/**
 * A simple {@link Fragment} subclass.
 */
public class HouseMessagesFragment extends Fragment {

    private RecyclerView messagesRecyclerView;
    private MessagesAdapter adapter;
    private List<HouseAnnouncement> houseAnnouncementList = new ArrayList<>();

    public HouseMessagesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View cureentView = inflater.inflate(R.layout.fragment_house_messages, container, false);
        setFakeData();
        initializeComponents(cureentView);
        return cureentView;
    }

    private void initializeComponents(View currentView) {
        messagesRecyclerView = (RecyclerView) currentView.findViewById(R.id.MessagesRecyclerView);
        messagesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new MessagesAdapter(houseAnnouncementList);
        messagesRecyclerView.setAdapter(adapter);
    }

    private void setFakeData() {
        houseAnnouncementList.clear();
        houseAnnouncementList.add(new HouseAnnouncement(0, "The Kitchen Situation", "Can someone please respond to the kitchen being messy", "2017-04-20", 0, 0));
        houseAnnouncementList.add(new HouseAnnouncement(0, "The Swimming Pool Situation", "The swimming pool is scheduled for maintenance sometime this week", "2017-04-20", 0, 0));
        houseAnnouncementList.add(new HouseAnnouncement(0, "The Crazy Landlord", "The landlord is talking about the house rent not being up to date. Can someone confirm this", "2017-04-20", 0, 0));
        houseAnnouncementList.add(new HouseAnnouncement(0, "The Assignment", "Someone left their assignment in the kitchen. Please collect it from me", "2017-04-20", 0, 0));
    }

}
