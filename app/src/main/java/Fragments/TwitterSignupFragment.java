package Fragments;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.jaredrummler.materialspinner.MaterialSpinner;
import com.koeksworld.homenet.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import Models.Country;
import Models.User;

/**
 * A simple {@link Fragment} subclass.
 */
public class TwitterSignupFragment extends Fragment {

    private User partialUser;
    private TextView toolbarTextView, messageText;
    private TextView usernameTextView;
    private EditText emailEditText, usernameEditText, passwordEditText;
    private MaterialSpinner genderSpinner, countrySpinner;
    private ArrayList<Country> countryList;
    private List<String> rawCountryList = new ArrayList<>();
    private String location, totalFollowers, profileURL;

    private TextInputLayout emailTextInputLayout, usernameTextInputLayout, passwordTextInputLayout;


    public TwitterSignupFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_twitter_signup, container, false);
        initializeComponents(view);
        setupData();
        return view;
    }

    private void initializeComponents(View currentView) {
        toolbarTextView = (TextView) getActivity().findViewById(R.id.MainActivityToolbarTextView);
        toolbarTextView.setText("Complete Twitter Registration");
        partialUser = (User) getArguments().getSerializable("PartialUser");
        totalFollowers = (String) getArguments().getString("TotalFollowers");
        profileURL = (String) getArguments().getString("ProfilePictureURL");
        location = (String) getArguments().getString("Location");
        countryList = (ArrayList) getArguments().getParcelableArrayList("Countries");
        usernameTextView = (TextView) currentView.findViewById(R.id.TwitterNameTextView);
        emailEditText = (EditText) currentView.findViewById(R.id.TwitterEmailEditText);
        usernameEditText = (EditText) currentView.findViewById(R.id.TwitterUsernameEditText);
        passwordEditText = (EditText) currentView.findViewById(R.id.TwitterPasswordEditText);
        genderSpinner = (MaterialSpinner) currentView.findViewById(R.id.TwitterGenderSpinner);
        countrySpinner = (MaterialSpinner) currentView.findViewById(R.id.TwitterCountrySpinner);
        messageText = (TextView) currentView.findViewById(R.id.TwitterSignupMessageTextView);
        emailTextInputLayout = (TextInputLayout) currentView.findViewById(R.id.TwitterEmailHint);
        usernameTextInputLayout = (TextInputLayout) currentView.findViewById(R.id.TwitterUsernameHint);
        passwordTextInputLayout = (TextInputLayout) currentView.findViewById(R.id.TwitterPasswordHint);
    }

    private void determineSelectedCountry(String locationData)
    {
        int found = -1;
        int count = 0;
        for (String c : rawCountryList) {
            if (locationData.toUpperCase().contains(c.toUpperCase())) {
                found = count;
                break;
            } else {
                count++;
            }
        }
        if (found != -1) {
            countrySpinner.setSelectedIndex(found);
        }
    }
    private void setupData()
    {
        String data = "Hi, "+partialUser.getName()+ " !";
        usernameTextView.setText(data);
        if (Integer.parseInt(totalFollowers) > 0) {
            messageText.setText("With HomeNet, you will not lose contact with your "+totalFollowers +" followers! Tap the signup floating button to start the fun!");
        }
        emailEditText.setText(partialUser.getEmail());
        usernameTextView.setText(partialUser.getName());
        usernameEditText.setText(partialUser.getUserName());
        genderSpinner.setItems("Male", "Female");
        for (Country c : countryList) {
            rawCountryList.add(c.getName());
        }
        countrySpinner.setItems(rawCountryList);
        if (location != null) {
            determineSelectedCountry(location);
        }

    }

    private void setupValidation()
    {
        emailTextInputLayout.setError("E-Mail is required");
        emailEditText.setError(null);

    }

}
