package Fragments;

import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.jaredrummler.materialspinner.MaterialSpinner;
import com.koeksworld.homenet.R;

import java.util.ArrayList;

import Models.Country;
import Models.User;

/**
 * A simple {@link Fragment} subclass.
 */
public class FacebookSignupFragment extends Fragment {

    private EditText emailEditText, usernameEditText, passwordEditText;
    private MaterialSpinner countrySpinner, genderSpinner;
    private FloatingActionButton submitButton;
    private User partialUser;
    private ArrayList<Country> countries = new ArrayList<>();
    private ArrayList<String> entries = new ArrayList<>();
    private TextView toolbarTextView, nameLabel;
    public FacebookSignupFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View currentView = inflater.inflate(R.layout.fragment_facebook_signup, container, false);
        getData();
        initializeComponents(currentView);
        return currentView;
    }

    private void initializeComponents(View currentView) {
        emailEditText = (EditText) currentView.findViewById(R.id.FacebookEmailAddressEditText);
        passwordEditText = (EditText) currentView.findViewById(R.id.FacebookPasswordEditText);
        usernameEditText = (EditText) currentView.findViewById(R.id.FacebookUsernameEditText);
        toolbarTextView = (TextView) getActivity().findViewById(R.id.MainActivityToolbarTextView);
        genderSpinner = (MaterialSpinner) currentView.findViewById(R.id.FacebookGenderSpinner);
        countrySpinner = (MaterialSpinner) currentView.findViewById(R.id.FacebookCountrySpinner);
        nameLabel = (TextView) currentView.findViewById(R.id.FacebookTitleTextView);
        toolbarTextView.setText("Complete Facebook Registration");
        genderSpinner.setItems("Male", "Female");
        for (Country c : countries) {
            entries.add(c.getName());
        }
        countrySpinner.setItems(entries);
        String name = "Hi, "+partialUser.getName() + " "+partialUser.getSurname();
        nameLabel.setText(name);
    }

    public void getData() {
        countries = getArguments().getParcelableArrayList("Countries");
        partialUser = (User) getArguments().getSerializable("User");
    }

}
