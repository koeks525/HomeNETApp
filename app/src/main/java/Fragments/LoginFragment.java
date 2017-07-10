package Fragments;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.koeksworld.homenet.ForgotPasswordActivity;
import com.koeksworld.homenet.R;
import com.koeksworld.homenet.RegistrationActivity;
import com.koeksworld.homenet.WelcomeActivity;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import Communication.HomeNetService;
import Data.RealmHelper;
import Models.Country;
import Models.LoginViewModel;
import Models.Token;
import Models.User;
import ResponseModels.SingleResponse;
import Utilities.DeviceUtils;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment implements View.OnClickListener {

    private Button registerButton,loginButton;
    private TextInputLayout usernameHint,passwordHint;
    private EditText usernameEditText,passwordEditText;
    private List<Country> countryList;
    private RealmHelper databaseHelper;
    private SharedPreferences sharedPreferences;
    private DeviceUtils deviceUtils;
    private Retrofit retrofit;
    private HomeNetService service;
    private SharedPreferences.Editor editor;
    private TextView forgotPasswordTextView;

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        getData();
        registerButton = (Button) view.findViewById(R.id.RegisterButton);
        usernameHint = (TextInputLayout) view.findViewById(R.id.LoginUsernameHint);
        passwordHint = (TextInputLayout) view.findViewById(R.id.LoginPasswordHint);
        loginButton = (Button) view.findViewById(R.id.LoginButton);
        usernameEditText = (EditText) view.findViewById(R.id.LoginFragmentUsernameEditText);
        passwordEditText = (EditText) view.findViewById(R.id.LoginFragmentPasswordEditText);
        forgotPasswordTextView = (TextView) view.findViewById(R.id.forgotPasswordTextView);
        forgotPasswordTextView.setOnClickListener(this);
        loginButton.setOnClickListener(this);
        registerButton.setOnClickListener(this);
        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.RegisterButton:
                Intent loginIntent = new Intent(getActivity(), RegistrationActivity.class);
                startActivity(loginIntent);
                //overridePendingTransition(0,0);
            case R.id.LoginButton:
                if (usernameEditText.getText().toString().trim().length() == 0) {
                    displaySnackBar("Username is required", view);
                    return;

                }
                if (passwordEditText.getText().toString().trim().length() == 0) {
                    displaySnackBar("Password is required", view);
                    return;
                }

                LoginViewModel model = new LoginViewModel(usernameEditText.getText().toString().trim(), passwordEditText.getText().toString().trim());

                if (sharedPreferences.getString("authorization_token", "").equalsIgnoreCase("")) {
                    generateToken(model, view);
                } else {
                    processLoginRequest(model, view);
                }


                break;
            case R.id.forgotPasswordTextView:


                Intent forgotIntent = new Intent(getActivity(), ForgotPasswordActivity.class);
                startActivity(forgotIntent);
                getActivity().overridePendingTransition(0,0);


                break;
        }
    }

    private void getData() {
        databaseHelper = new RealmHelper();
        countryList = databaseHelper.getCountries();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        editor = sharedPreferences.edit();
        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(1, TimeUnit.MINUTES).readTimeout(1, TimeUnit.MINUTES).protocols(Arrays.asList(Protocol.HTTP_1_1)).build();
        retrofit = new Retrofit.Builder().baseUrl(getResources().getString(R.string.homenet_link)).client(client).addConverterFactory(GsonConverterFactory.create()).build();
        service = retrofit.create(HomeNetService.class);
    }

    public void displaySnackBar(String message, View currentView) {
        Snackbar.make(currentView, message, Snackbar.LENGTH_SHORT).show();
    }

    public Snackbar displaySnackBarProgress(String message, View currentView) {
        Snackbar bar = Snackbar.make(currentView, message, Snackbar.LENGTH_INDEFINITE);
        ViewGroup viewGroup = (ViewGroup) bar.getView().findViewById(android.support.design.R.id.snackbar_text).getParent();
        ProgressBar progressBar = new ProgressBar(currentView.getContext());
        viewGroup.addView(progressBar);
        bar.show();
        return bar;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public void generateToken(final LoginViewModel model, final View currentView) {
        final Snackbar bar = displaySnackBarProgress("Processing Login. Please wait...", currentView);
        final Call<SingleResponse<Token>> tokenCall = service.createToken(model, getResources().getString(R.string.homenet_client_string));
        tokenCall.enqueue(new Callback<SingleResponse<Token>>() {
            @Override
            public void onResponse(Call<SingleResponse<Token>> call, Response<SingleResponse<Token>> response) {
                bar.dismiss();
                SingleResponse<Token> tokenResponse = response.body();
                if (response.code() == 200) {
                    if (!tokenResponse.isDidError()) {
                        Token token = tokenResponse.getModel();
                        editor.putString("authorization_token", token.getTokenHandler());
                        editor.putString("expiry_date", token.getDateExpires());
                        editor.commit();
                        //Now with the token, proceed to logging in the user
                        processLoginRequest(model, currentView);
                    } else {
                        displaySnackBar(tokenResponse.getMessage(), currentView);
                    }
                } else {
                    if (tokenResponse.getMessage() != null) {
                        displaySnackBar(tokenResponse.getMessage(), currentView);
                    } else {
                        displaySnackBar("Error creating Authorization Token", currentView);
                    }
                }
            }

            @Override
            public void onFailure(Call<SingleResponse<Token>> call, Throwable t) {
                bar.dismiss();
            }
        });
    }

    private void processLoginRequest(final LoginViewModel model, final View currentView) {
        final Snackbar snackbar = Snackbar.make(currentView, "Logging in. Please wait...", Snackbar.LENGTH_INDEFINITE);
        ViewGroup viewGroup = (ViewGroup) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text).getParent();
        ProgressBar progressBar = new ProgressBar(currentView.getContext());
        viewGroup.addView(progressBar);
        snackbar.show();
        if (sharedPreferences.getString("authorization_token", "") != "") {
            Call<SingleResponse<User>> userResponse = service.loginUser("Bearer "+ sharedPreferences.getString("authorization_token", ""), model, getResources().getString(R.string.homenet_client_string));
            userResponse.enqueue(new Callback<SingleResponse<User>>() {
                @Override
                public void onResponse(Call<SingleResponse<User>> call, Response<SingleResponse<User>> response) {
                    if (snackbar.isShown()) {
                        snackbar.dismiss();
                    }
                    if (response.code() == 200) {
                        SingleResponse<User> userResponse = response.body();
                        User resultUser = userResponse.getModel();
                        editor.putString("username", resultUser.getUserName());
                        editor.putString("password", resultUser.getPassword());
                        editor.putString("emailAddress", resultUser.getEmail());
                        editor.putString("name", resultUser.getName());
                        editor.putString("surname", resultUser.getSurname());
                        editor.putInt("userID", resultUser.getId());
                        editor.commit();
                        //SQLite does not want to add my user object - ignore for now... will sort later (doesnt Google Firebase have something for this situation)
                        //Load the landing screen (we need to have some logic which checks what the user has [houses, friends, etc])
                        //I need coffee before i figure this out
                        Intent welcomeIntent = new Intent(getActivity(), WelcomeActivity.class);
                        startActivity(welcomeIntent);
                        getActivity().finish();
                    }

                    //Should the token have expired, create a new token
                    else if (response.code() == 403) {
                        generateToken(model, currentView);
                    } else if (response.code() == 401) {
                        try {
                            JSONObject errorObject = new JSONObject(response.errorBody().string());
                            displayMessage("Error Processing Login", errorObject.getString("message"), null);

                        } catch (Exception error) {
                            displayMessage("Critical Error", error.getMessage(), null);
                        }
                    } else if (response.code() == 404 || response.code() == 400) {
                        try {
                            JSONObject obj = new JSONObject(response.errorBody().string());
                            displayMessage("Login Error", obj.getString("message"), null);
                        } catch (Exception error) {

                        }
                    }
                }

                @Override
                public void onFailure(Call<SingleResponse<User>> call, Throwable t) {
                    if (snackbar.isShown()) {
                        snackbar.dismiss();
                    }
                    displayMessage("Critical Error Processing Login", t.getMessage(), null);
                }
            });

        } else {
            generateToken(model, currentView);
        }

    }

    public void displayMessage(String title, String message, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title).setMessage(message).setPositiveButton("Got it", listener).setCancelable(false);
        builder.show();
    }
}
