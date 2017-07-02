package Fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.koeksworld.homenet.R;

import DialogFragments.CreateHouseDialogFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateHouseIntroductionFragment extends Fragment implements View.OnClickListener {


    private Button action;
    public CreateHouseIntroductionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View currentView = inflater.inflate(R.layout.fragment_create_house_introduction, container, false);
        initializeComponents(currentView);
        return currentView;
    }

    public void initializeComponents(View currentView) {
        action = (Button) currentView.findViewById(R.id.CreateHouseIntroButton);
        action.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        CreateHouseDialogFragment dialogFragment = new CreateHouseDialogFragment();
        dialogFragment.show(getFragmentManager(), null);
    }
}
