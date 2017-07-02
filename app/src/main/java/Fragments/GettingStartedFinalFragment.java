package Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.koeksworld.homenet.HomeManagerActivity;
import com.koeksworld.homenet.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class GettingStartedFinalFragment extends Fragment implements View.OnClickListener {

    private Button finalStepButton;

    public GettingStartedFinalFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View currentView = inflater.inflate(R.layout.fragment_getting_started_final, container, false);
        initializeComponents(currentView);
        return currentView;
    }

    private void initializeComponents(View currentView) {
        finalStepButton = (Button) currentView.findViewById(R.id.ContinueToHomeNetButton);
        finalStepButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        //This will need to change in due course
        Intent homeIntent = new Intent(getActivity(), HomeManagerActivity.class);
        startActivity(homeIntent);
        getActivity().overridePendingTransition(0,0);
        getActivity().finish();
    }
}
