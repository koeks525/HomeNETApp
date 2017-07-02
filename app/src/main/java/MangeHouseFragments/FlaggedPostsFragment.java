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

import Adapters.FlaggedPostsAdapter;
import Models.HousePost;
import Models.HousePostFlag;

/**
 * A simple {@link Fragment} subclass.
 */
public class FlaggedPostsFragment extends Fragment {

    private List<HousePostFlag> housePostList = new ArrayList<>();
    private RecyclerView flaggedPostRecyclerView;
    private FlaggedPostsAdapter adapter;

    public FlaggedPostsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View currentView = inflater.inflate(R.layout.fragment_flagged_posts, container, false);
        initializeComponents(currentView);
        return currentView;
    }

    private void initializeComponents(View currentView) {
        setFakeData();
        flaggedPostRecyclerView = (RecyclerView) currentView.findViewById(R.id.FlaggedPostsRecyclerView);
        flaggedPostRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new FlaggedPostsAdapter(housePostList);
        flaggedPostRecyclerView.setAdapter(adapter);

    }

    private void setFakeData() {
        housePostList.clear();
        //For the sake of the fake data, we will use response to show description. Bad hack!
        housePostList.add(new HousePostFlag(0, "Post Flagged as Violent", "2017-03-02", 0, "This post encourages violence. Please take it down", 0, 1, 1));
        housePostList.add(new HousePostFlag(0, "Post Flagged as Racist", "2017-04-02", 0, "This post is very racist, and should not be allowed in the house", 0, 1, 1));
        housePostList.add(new HousePostFlag(0, "Post Flagged as Offensive", "2017-03-01", 0, "This post offends my friends who play computer games 10 hours a day", 0, 1, 1));
        housePostList.add(new HousePostFlag(0, "Post Flagged as Pornographic", "2017-03-05", 0, "This post is a snippet of a pornographic video posted on popular porn sites", 0, 1, 1));
        housePostList.add(new HousePostFlag(0, "Post Flagged as Hate Speech", "2017-03-07", 0, "This post encourages hate amongst the rich kids", 0, 1, 1));
        housePostList.add(new HousePostFlag(0, "Post Flagged as Violent", "2017-03-10", 0, "Another violent post in the house. Please deal with this", 0, 1, 1));
    }

}
