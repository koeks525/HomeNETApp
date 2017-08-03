package HomeNETStream;


import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.koeksworld.homenet.R;

import Tasks.GetHouseAnnouncementsTask;

/**
 * A simple {@link Fragment} subclass.
 */
public class AnnoucementFragment extends Fragment implements View.OnClickListener {


    private RecyclerView recyclerView;
    private FloatingActionButton refreshButton;


    public AnnoucementFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_annoucement, container, false);
    }

    private void initializeComponents(View currentView) {
        recyclerView = (RecyclerView) currentView.findViewById(R.id.ViewAnnouncementsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        refreshButton = (FloatingActionButton) currentView.findViewById(R.id.ViewAnnouncementRefreshButton);
        refreshButton.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {

    }
}
