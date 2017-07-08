package MangeHouseFragments;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.maps.MapView;
import com.koeksworld.homenet.R;

import Models.House;
import Tasks.EditHouseTask;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditHouseFragment extends Fragment {

    private MapView editHouseMapView;
    private EditText nameEditText, descriptionEditText, oneTimePinEditText, houseIDEditText;
    private CheckBox isPrivateCheckBox;
    private EditHouseTask editHouseTask;
    private House selectedHouse;

    public EditHouseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View currentView = inflater.inflate(R.layout.fragment_edit_house, container, false);
        initializeComponents(currentView, savedInstanceState);
        getHouseData();
        runTask();
        return currentView;
    }

    private void initializeComponents(View currentView, Bundle savedInstanceState) {
        editHouseMapView = (MapView) currentView.findViewById(R.id.EditHouseLocationMapView);
        editHouseMapView.onCreate(savedInstanceState);
        nameEditText = (EditText) currentView.findViewById(R.id.EditHouseNameEditText);
        descriptionEditText = (EditText) currentView.findViewById(R.id.EditHouseDescriptionEditText);
        oneTimePinEditText = (EditText) currentView.findViewById(R.id.EditHouseOneTimePinEditText);
        isPrivateCheckBox = (CheckBox) currentView.findViewById(R.id.EditHouseHousePrivacyCheckbox);
    }

    private void getHouseData() {
        selectedHouse = (House) getArguments().getSerializable("House");
    }

    private void runTask() {
        editHouseTask = new EditHouseTask(getActivity(), selectedHouse.getHouseID(), nameEditText, descriptionEditText, oneTimePinEditText, houseIDEditText, editHouseMapView, isPrivateCheckBox);
        editHouseTask.execute();
    }

    @Override
    public void onStart() {
        super.onStart();
        editHouseMapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        editHouseMapView.onStop();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        editHouseMapView.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        editHouseMapView.onDestroy(); //There is a problem here
    }

    @Override
    public void onPause() {
        super.onPause();
        editHouseMapView.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        editHouseMapView.onResume();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        editHouseMapView.onLowMemory();
    }
}
