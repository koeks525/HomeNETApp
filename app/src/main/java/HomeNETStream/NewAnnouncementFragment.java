package HomeNETStream;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.jaredrummler.materialspinner.MaterialSpinner;
import com.koeksworld.homenet.R;

import Models.House;
import Models.NewAnnouncementViewModel;
import Tasks.CreateAnnouncementTask;
import Tasks.GetSubscribedHousesTask;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewAnnouncementFragment extends Fragment implements View.OnClickListener {


    private EditText titleEditText, descriptionEditText;
    private MaterialSpinner houseSpinner;
    private FloatingActionButton createButton;
    private GetSubscribedHousesTask task;
    private SharedPreferences sharedPreferences;
    private TextView toolbarTextView;
    public NewAnnouncementFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View currentView = inflater.inflate(R.layout.fragment_new_announcement, container, false);
        initializeComponents(currentView);
        task = new GetSubscribedHousesTask(getActivity(), houseSpinner);
        task.execute();
        return currentView;
    }

    private void initializeComponents(View currentView) {
        titleEditText = (EditText) currentView.findViewById(R.id.NewAnnouncementTitleTextView);
        descriptionEditText = (EditText)currentView.findViewById(R.id.NewAnnouncementDescriptionTextView);
        houseSpinner = (MaterialSpinner) currentView.findViewById(R.id.NewAnnouncementHouseSpinner);
        createButton = (FloatingActionButton) currentView.findViewById(R.id.CreateAnnouncementButton);
        createButton.setOnClickListener(this);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        toolbarTextView = (TextView) getActivity().findViewById(R.id.HomeNetFeedToolbarTextView);
        toolbarTextView.setText("New Announcement");
    }

    @Override
    public void onClick(View view) {
        if (titleEditText.getText().toString() == "") {
            displaySnackbar("Please enter a valid title");
            return;
        }
        if (descriptionEditText.getText().toString() == "") {
            displaySnackbar("Please enter a valid description");
            return;
        }
        int selectedItem = houseSpinner.getSelectedIndex();
        House selectedHouse = (House) houseSpinner.getItems().get(selectedItem);
        NewAnnouncementViewModel model = new NewAnnouncementViewModel(titleEditText.getText().toString(), descriptionEditText.getText().toString(), selectedHouse.getHouseID(), sharedPreferences.getString("emailAddress", ""));
        CreateAnnouncementTask task1 = new CreateAnnouncementTask(model, getActivity());
        task1.execute();
    }

    private void displaySnackbar(String message) {
        Snackbar.make(getActivity().findViewById(android.R.id.content), message, BaseTransientBottomBar.LENGTH_LONG).show();
    }
}
