package HomeNETStream;


import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.koeksworld.homenet.R;

import Tasks.GetMultimediaPostsTask;

/**
 * A simple {@link Fragment} subclass.
 */
public class PhotoGalleryFragment extends Fragment {

    private RecyclerView recyclerView;
    private TextView toolbarTextView;
    public PhotoGalleryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View currentView = inflater.inflate(R.layout.fragment_photo_gallery, container, false);
        initializeComponents(currentView);
        initiateTask();
        return currentView;
    }

    private void initializeComponents(View currentView) {
        recyclerView = (RecyclerView) currentView.findViewById(R.id.PhotoGalleryRecyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyclerView.setHasFixedSize(true);
        toolbarTextView = (TextView) getActivity().findViewById(R.id.HomeNetFeedToolbarTextView);
        toolbarTextView.setText("Your Photo Gallery");
    }

    private void initiateTask() {
        GetMultimediaPostsTask task = new GetMultimediaPostsTask(getActivity(), recyclerView);
        task.execute();
    }



}
