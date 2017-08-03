package Fragments;


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

import Adapters.MessagesAdapter;
import Models.HouseAnnouncement;
import Tasks.GetHouseMessagesTask;

/**
 * A simple {@link Fragment} subclass.
 */
public class HouseMessagesFragment extends Fragment implements View.OnClickListener {

    private RecyclerView messagesRecyclerView;
    private GetHouseMessagesTask task;
    private FloatingActionButton refreshButton;

    public HouseMessagesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View cureentView = inflater.inflate(R.layout.fragment_house_messages, container, false);
        initializeComponents(cureentView);
        getData();
        return cureentView;
    }

    private void initializeComponents(View currentView) {
        messagesRecyclerView = (RecyclerView) currentView.findViewById(R.id.MessagesRecyclerView);
        messagesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        refreshButton = (FloatingActionButton) currentView.findViewById(R.id.MessagesActionButton);
        refreshButton.setOnClickListener(this);
    }

    private void getData() {
        task = new GetHouseMessagesTask(getActivity(), messagesRecyclerView);
        task.execute();
    }


    @Override
    public void onClick(View view) {
        task = new GetHouseMessagesTask(getActivity(), messagesRecyclerView);
        task.execute();
    }
}
