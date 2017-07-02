package Fragments;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.koeksworld.homenet.R;

import DialogFragments.SearchHousesDialogFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class FindHousesIntroductionFragment extends Fragment implements View.OnClickListener {


    private Button connectToHouse;
    public FindHousesIntroductionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View currentView = inflater.inflate(R.layout.fragment_find_houses_introduction, container, false);
        intializeComponents(currentView);
        return currentView;
    }

    private void intializeComponents(View currentView) {
        connectToHouse = (Button) currentView.findViewById(R.id.ConnectToHouseIntroductionButton);
        connectToHouse.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        SearchHousesDialogFragment dialogFragment = new SearchHousesDialogFragment();
        dialogFragment.show(getFragmentManager(), null);

    }
}
