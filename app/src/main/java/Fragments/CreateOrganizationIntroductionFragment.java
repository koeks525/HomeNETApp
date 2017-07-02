package Fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.koeksworld.homenet.R;

import DialogFragments.CreateOrganizationDialogFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateOrganizationIntroductionFragment extends Fragment implements View.OnClickListener {


    private Button createOrganizationButton;
    public CreateOrganizationIntroductionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View currentView = inflater.inflate(R.layout.fragment_create_organization_introduction, container, false);
        initializeComponents(currentView);
        return currentView;
    }

    private void initializeComponents(View currentView) {
        createOrganizationButton = (Button) currentView.findViewById(R.id.CreateOrganizationIntroductionButton);
        createOrganizationButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        CreateOrganizationDialogFragment dialogFragment = new CreateOrganizationDialogFragment();
        dialogFragment.show(getFragmentManager(), null);
    }
}
