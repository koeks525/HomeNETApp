package Fragments;


import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.jaredrummler.materialspinner.MaterialSpinner;
import com.koeksworld.homenet.R;
import com.koeksworld.homenet.WelcomeActivity;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import Communication.HomeNetService;
import Data.RealmHelper;
import Models.Country;
import Models.LoginViewModel;
import Models.Token;
import Models.User;
import ResponseModels.SingleResponse;
import Utilities.DeviceUtils;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateAccountFragment extends Fragment implements View.OnClickListener, DatePickerDialog.OnDateSetListener {


    private EditText nameEditText, surnameEditText, emailEditText, usernameEditText, passwordEditText, dateOfBirthEditText;
    private Button dateOfBirthButton;
    private MaterialSpinner countrySpinner, genderSpinner;
    private FloatingActionButton createUserButton;
    private List<Country> countryList;
    private ArrayList<String> rawCountryData;
    private int year, month, day;
    private String dateOfBirthString;
    private Retrofit retrofit;
    private HomeNetService service;
    private int executionCount = 0;
    private SharedPreferences sharedPreferences;
    private RealmHelper databaseHelper;
    private DeviceUtils deviceUtils;
    private SharedPreferences.Editor editor;
    private DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.ENGLISH);
    public CreateAccountFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View currentView = inflater.inflate(R.layout.fragment_create_account, container, false);
        initializeComponents(currentView);
        return currentView;
    }

    private void initializeComponents(View currentView) {
        databaseHelper = new RealmHelper();
        deviceUtils = new DeviceUtils(getActivity());
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        editor = sharedPreferences.edit();
        retrofit = new Retrofit.Builder().baseUrl(getResources().getString(R.string.homenet_link)).addConverterFactory(GsonConverterFactory.create()).build();
        service = retrofit.create(HomeNetService.class);
        countryList = databaseHelper.getCountries();
        rawCountryData = new ArrayList<>();
        for (Country c : countryList) {
            rawCountryData.add(c.getName());
        }
        nameEditText = (EditText) currentView.findViewById(R.id.CreateAccountNameEditText);
        surnameEditText = (EditText) currentView.findViewById(R.id.CreateAccountSurnameEditText);
        emailEditText = (EditText) currentView.findViewById(R.id.CreateAccountEmailAddress);
        usernameEditText = (EditText) currentView.findViewById(R.id.CreateAccountUsernameEditText);
        passwordEditText = (EditText) currentView.findViewById(R.id.CreateAccountPasswordEditText);
        dateOfBirthEditText = (EditText) currentView.findViewById(R.id.CreateAccountDateOfBirthEditText);
        dateOfBirthButton = (Button) currentView.findViewById(R.id.DateOfBirthButton);
        dateOfBirthButton.setOnClickListener(this);
        countrySpinner = (MaterialSpinner) currentView.findViewById(R.id.CreateAccountCountrySpinner);
        countrySpinner.setItems(rawCountryData);
        genderSpinner = (MaterialSpinner) currentView.findViewById(R.id.CreateAccountGenderSpinner);
        genderSpinner.setItems("Male","Female");
        createUserButton = (FloatingActionButton) currentView.findViewById(R.id.CreateAccountSignUpButton);
        createUserButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.DateOfBirthButton:
                DatePickerDialog dialog = new DatePickerDialog(getActivity(), this, year, month, day);
                dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                dialog.getDatePicker().updateDate(Calendar.YEAR, month, day);
                dialog.show();
                break;
            case R.id.CreateAccountSignUpButton:
                final User newUser = new User();
                if (year == 0 || month == 0 || day == 0) {
                    displayMessage("Invalid Date Information", "Please ensure that you have provided a valid date of birth");
                    return;
                }

                if (nameEditText.getText().toString().trim().length() <= 0) {
                    displayMessage("Name Required", "Please provide us with a valid name");
                    return;
                }

                if (surnameEditText.getText().toString().trim().length() <= 0) {
                    displayMessage("Surname Required", "Please provide us with a valid surname");
                    return;
                }

                if (emailEditText.getText().toString().trim().length() <= 0 || !emailEditText.getText().toString().trim().contains("@")) {
                    displayMessage("Email Required", "Please provide us with a valid email address");
                    return;
                }

                if (usernameEditText.getText().toString().trim().length() <= 0) {
                    displayMessage("Username Required", "Please provide us with a valid username");
                    return;
                }

                if (passwordEditText.getText().toString().trim().length() <= 0) {
                    displayMessage("Password Required", "Please provide us with a valid password");
                    return;
                }

                newUser.setId(0);
                newUser.setName(nameEditText.getText().toString().trim());
                newUser.setSurname(surnameEditText.getText().toString().trim());
                newUser.setEmail(emailEditText.getText().toString().trim());
                newUser.setUserName(usernameEditText.getText().toString().trim());
                newUser.setPassword(passwordEditText.getText().toString().trim());
                newUser.setIsDeleted(0);
                newUser.setGender(genderSpinner.getText().toString());
                String countrySelected = countrySpinner.getText().toString();
                newUser.setDateRegistered(dateFormat.format(new Date()));
                int index = 0;
                int count = 0;
                for (Country c : countryList) {
                    if (countrySelected.toUpperCase().equals(c.getName().toUpperCase())) {
                        index = count;
                        break;
                    }
                    count++;
                }
                newUser.setCountryID(index);
                newUser.setDateOfBirth(dateOfBirthString);
                processUserRegistration(newUser, view);
                break;

        }
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        year = datePicker.getYear();
        month = datePicker.getMonth() + 1;
        day = datePicker.getDayOfMonth();
        dateOfBirthString = year+"/"+month+"/"
                +day;
        dateOfBirthEditText.setText(dateOfBirthString);
    }

    private void processUserRegistration(User currentUser, final View currentView) {
        final Snackbar snackbar = Snackbar.make(currentView, "Processing Registration. Please wait...", Snackbar.LENGTH_INDEFINITE);
        ViewGroup viewGroup = (ViewGroup) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text).getParent();
        ProgressBar progressBar = new ProgressBar(currentView.getContext());
        viewGroup.addView(progressBar);
        snackbar.show();
        try {
            Call<SingleResponse<User>> userCall = service.createUser(currentUser, getResources().getString(R.string.homenet_client_string));
            userCall.enqueue(new retrofit2.Callback<SingleResponse<User>>() {
                @Override
                public void onResponse(Call<SingleResponse<User>> call, Response<SingleResponse<User>> response) {
                    if (snackbar.isShown()) {
                        snackbar.dismiss();
                    }
                    if (response.code() == 200) {
                        SingleResponse<User> userResponse = response.body();
                        User resultUser = userResponse.getModel();
                        editor.clear(); //Clear any data that might exist
                        editor.commit();
                        editor.putInt("userID", resultUser.getId());
                        editor.putString("username", resultUser.getUserName());
                        editor.putString("password", resultUser.getPassword());
                        editor.putString("emailAddress", resultUser.getEmail());
                        editor.commit();

                        if (resultUser != null) {
                            //Generate the token so that the user can proceed with other functionality
                            generateToken(resultUser, currentView);
                        }
                    } else if (response.code() == 400) {
                        ResponseBody errorResponse = response.errorBody();
                        if (errorResponse != null) {
                            try {
                                JSONObject responseObject = new JSONObject(errorResponse.string());
                                displayMessage("Error Processing Registration", responseObject.getString("message"));
                            } catch (Exception error) {

                            }
                        } else {
                            displayMessage("Error Registering User", "Error Creating user");
                        }
                    }
                }

                @Override
                public void onFailure(Call<SingleResponse<User>> call, Throwable t) {
                    if (snackbar.isShown()) {
                        snackbar.dismiss();
                    }
                    displayMessage("Critical Error Processing Registration", "A critical error occurred processing registration\n"+t.getMessage());
                }
            });
        } catch (Exception error) {
            displayMessage("Error Processing User Registration", error.getMessage());
        }
    }

    private void generateToken(final User currentUser, final View currentView) {
        final Snackbar snackbar = Snackbar.make(currentView, "Authorizing. Please wait...", Snackbar.LENGTH_INDEFINITE);
        ViewGroup viewGroup = (ViewGroup) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text).getParent();
        ProgressBar progressBar = new ProgressBar(currentView.getContext());
        viewGroup.addView(progressBar);
        snackbar.show();
        LoginViewModel model = new LoginViewModel(currentUser.getUserName(), currentUser.getPassword());
        try {
            Call<SingleResponse<Token>> tokenCall = service.createToken(model, getResources().getString(R.string.homenet_client_string));
            tokenCall.enqueue(new retrofit2.Callback<SingleResponse<Token>>() {
                @Override
                public void onResponse(Call<SingleResponse<Token>> call, Response<SingleResponse<Token>> response) {
                    if (snackbar.isShown()) {
                        snackbar.dismiss();
                    }
                    if (response.code() == 200) {
                        SingleResponse<Token> tokenResponse = response.body();
                        Token resultToken = tokenResponse.getModel();
                            editor.putString("authorization_token", resultToken.getTokenHandler());
                            editor.putString("token_expiry", resultToken.getDateExpires());
                            editor.commit();

                            Intent newIntent = new Intent(getActivity(), WelcomeActivity.class);
                            Bundle newBundle = new Bundle();
                            newBundle.putParcelable("user", currentUser);
                            newIntent.putExtra("user_bundle", newBundle);
                            getActivity().startActivity(newIntent);
                            getActivity().overridePendingTransition(0,0);
                            getActivity().finish();
                    } else if (response.code() == 400) {
                        try {
                            JSONObject responseObject = new JSONObject(response.errorBody().string());
                            displayMessage("Error Authorizing App", responseObject.getString("message"), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    revertFailedRegistration(currentUser.getId(), currentView);
                                }
                            });
                        } catch (Exception error) {

                        }
                    } else if (response.code() == 403) {
                        displayMessage("Error Authorizing App", "Application is not authorized to use the service");
                    } else if (response.code() == 500) {
                        displayMessage("Error Authorizing App", "Internal Server Error. Please try again later");
                    }

                }

                @Override
                public void onFailure(Call<SingleResponse<Token>> call, Throwable t) {
                    if (snackbar.isShown()) {
                        snackbar.dismiss();
                    }
                    displayMessage("Critical Error Authorizing Device", "An error occurred while authorizing the device\n"+t.getMessage());
                }
            });
        } catch (Exception error) {
            displayMessage("Error Authorizing Device", "An error occurred while authorzing the device\n" + error.getMessage());
        }
    }

    private void displayMessage(String title, String message) {
        AlertDialog.Builder messageBox = new AlertDialog.Builder(getActivity());
        messageBox.setTitle(title).setMessage(message).setCancelable(false).setPositiveButton("Got It", null).show();
    }

    public void displayMessage(String title, String message, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("Continue", listener);
        builder.setCancelable(false);
        builder.show();
    }

    public void revertFailedRegistration(int userId, View currentView) {
        Snackbar bar = Snackbar.make(currentView, "Reverting Changes. Please wait", Snackbar.LENGTH_INDEFINITE);
        ViewGroup viewGroup = (ViewGroup) bar.getView().findViewById(android.support.design.R.id.snackbar_text).getParent();
        ProgressBar progressBar = new ProgressBar(currentView.getContext());
        viewGroup.addView(progressBar);
        bar.show();
        Call<SingleResponse<User>> userRegCall = service.removeUser(userId);
        final SingleResponse<User> userResponse = new SingleResponse<>();
        userRegCall.enqueue(new retrofit2.Callback<SingleResponse<User>>() {
            @Override
            public void onResponse(Call<SingleResponse<User>> call, Response<SingleResponse<User>> response) {
                if (response.code() == 200) {
                    userResponse.setMessage(response.body().getMessage());
                    displayMessage("Error Reverting Registration", "The system could not save your newly created profile onto your device. Please try registration again");
                } else {
                    displayMessage("Error Reverting Registration", "An error occurred while reverting registration. Please try again later");
                }
            }

            @Override
            public void onFailure(Call<SingleResponse<User>> call, Throwable t) {
                displayMessage("Critical Error Reverting Changes", "An error occurred with reverting changes: \n"+t.getMessage());
            }
        });
    }


}
