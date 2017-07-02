package Fragments;


import android.app.DialogFragment;
import android.bluetooth.BluetoothClass;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.koeksworld.homenet.R;

import org.json.JSONObject;

import Communication.HomeNetService;
import Models.User;
import ResponseModels.SingleResponse;
import Utilities.DeviceUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class ForgotPasswordFragment extends Fragment implements View.OnClickListener {

    private Retrofit retrofit;
    private HomeNetService service;
    private EditText emailAddressEditText;
    private Button resetButton;
    private EditText dateOfBirthEditText;
    private DeviceUtils deviceUtils;

    public ForgotPasswordFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View currentView = inflater.inflate(R.layout.fragment_forgot_password, container, false);
        deviceUtils = new DeviceUtils(getActivity());
        initializeComponents(currentView);
        initializeRetrofit();
        return currentView;
    }

    private void initializeComponents(View currentView) {
        emailAddressEditText = (EditText) currentView.findViewById(R.id.ResetPasswordEmailEditText);
        dateOfBirthEditText = (EditText) currentView.findViewById(R.id.ResetPasswordDOBEditText);
        resetButton = (Button) currentView.findViewById(R.id.ResetPasswordButton);
        resetButton.setOnClickListener(this);
    }

    private void initializeRetrofit()
    {
        retrofit = new Retrofit.Builder().baseUrl(getResources().getString(R.string.homenet_link)).addConverterFactory(GsonConverterFactory.create()).build();
        service = retrofit.create(HomeNetService.class);
    }

    @Override
    public void onClick(View view) {
        if (deviceUtils.checkNetworkConnection()) {
            final Snackbar bar = Snackbar.make(view, "Please wait. Reset Password Processing...", Snackbar.LENGTH_INDEFINITE);
            ViewGroup viewGroup = (ViewGroup) bar.getView().findViewById(android.support.design.R.id.snackbar_text).getParent();
            ProgressBar progressBar = new ProgressBar(view.getContext());
            viewGroup.addView(progressBar);
            bar.show();

            if (emailAddressEditText.getText().toString().length() <= 0) {
                bar.dismiss();
                displaySnackBar("Please enter an email address", view);
                return;
            }

            if (dateOfBirthEditText.getText().toString().length() <= 0) {
                bar.dismiss();
                displaySnackBar("Please enter a date of birth", view);
                return;
            }
            String emailAddress = emailAddressEditText.getText().toString().trim();
            String dateOfBirth = dateOfBirthEditText.getText().toString().trim();
            Call<SingleResponse<User>> resetCall = service.forgotPassword(emailAddress, dateOfBirth, getResources().getString(R.string.homenet_client_string));
            resetCall.enqueue(new Callback<SingleResponse<User>>() {
                @Override
                public void onResponse(Call<SingleResponse<User>> call, Response<SingleResponse<User>> response) {
                    if (bar.isShown()) {
                        bar.dismiss();
                    }
                    switch (response.code()) {
                        case 200:
                            SingleResponse<User> userResponse = response.body();
                            displayMessage("Forgot Password Message", userResponse.getMessage(), null);
                            break;
                        case 400:
                            try {
                                JSONObject object = new JSONObject(response.errorBody().string());
                                displayMessage("Error Processing Reset Password", object.getString("message"), null);
                            } catch (Exception error) {
                                displayMessage("Error Parsing JSON Object", error.getMessage(), null);
                            }
                            break;
                        case 500:
                            displayMessage("Server Error", "Something is up with the server. Please contact support. ", null);
                            break;
                    }
                }

                @Override
                public void onFailure(Call<SingleResponse<User>> call, Throwable t) {
                    if (bar.isShown()) {
                        bar.dismiss();
                    }
                    displayMessage("Critical Error", t.getMessage(), null); //Should we close because this is a critical error
                }
            });




        } else {
            displaySnackBar("Please check network connection", view);
        }
    }

    private void displaySnackBar(String message, View currentView) {
        Snackbar.make(currentView, message, Snackbar.LENGTH_LONG).show();
    }

    private void displayMessage(String title, String message, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder messageBox = new AlertDialog.Builder(getActivity());
        messageBox.setTitle(title).setMessage(message).setPositiveButton("Got it", listener).setCancelable(false);
        messageBox.show();
    }
}
