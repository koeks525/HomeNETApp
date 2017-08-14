package HomeNETStream;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.jaredrummler.materialspinner.MaterialSpinner;
import com.koeksworld.homenet.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import Communication.HomeNetService;
import Models.House;
import Models.MessageThread;
import Models.NewMessageThreadViewModel;
import Models.User;
import Models.UserViewModel;
import ResponseModels.ListResponse;
import ResponseModels.SingleResponse;
import Tasks.GetSubscribedHousesTask;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewMessageFragment extends Fragment implements View.OnClickListener, MaterialSpinner.OnItemSelectedListener {

    private EditText titleEditText, descriptionEditText;
    private MaterialSpinner houseSpinner, userSpinner;
    private FloatingActionButton submitButton;
    private TextView toolbarTextView;
    private Retrofit retrofit;
    private OkHttpClient client;
    private List<Protocol> protocolList;
    private HomeNetService service;
    private SharedPreferences sharedPreferences;
    private List<UserViewModel> houseUserList;

    public NewMessageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View currentView = inflater.inflate(R.layout.fragment_new_message, container, false);
        initializeRetrofit();
        initializeComponents(currentView);
        getSubscribedHouses();

        return currentView;
    }

    private void initializeComponents(View currentView) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        titleEditText = (EditText) currentView.findViewById(R.id.NewMessageTitleEditText);
        descriptionEditText = (EditText) currentView.findViewById(R.id.NewMessageDescriptionEditText);
        houseSpinner = (MaterialSpinner) currentView.findViewById(R.id.NewMessageSelectHouseSpinner);
        userSpinner = (MaterialSpinner) currentView.findViewById(R.id.NewMessageSelectUserSpinner);
        submitButton = (FloatingActionButton) currentView.findViewById(R.id.NewMessageSendMessageButton);
        toolbarTextView = (TextView) getActivity().findViewById(R.id.HomeNetFeedToolbarTextView);
        toolbarTextView.setText("New Message");
        submitButton.setOnClickListener(this);
        houseSpinner.setOnItemSelectedListener(this);
        houseSpinner.setSelectedIndex(0);

    }

    private void initializeRetrofit() {
        protocolList = new ArrayList<>();
        protocolList.add(Protocol.HTTP_1_1);
        client = new OkHttpClient.Builder().connectTimeout(2, TimeUnit.MINUTES).connectTimeout(2, TimeUnit.MINUTES).protocols(protocolList).build();
        retrofit = new Retrofit.Builder().baseUrl(getResources().getString(R.string.homenet_link)).client(client).addConverterFactory(GsonConverterFactory.create()).build();
        service = retrofit.create(HomeNetService.class);
        houseUserList = new ArrayList<>();
    }
    @Override
    public void onClick(View view) {
        if (titleEditText.getText().toString() == "") {
            displaySnackbar("Please enter a title");
            return;
        }

        if (descriptionEditText.getText().toString() == "") {
            displaySnackbar("Please enter message contents");
            return;
        }

        if (userSpinner.getItems().size() <= 0) {
            displaySnackbar("No users are on the list");
            return;
        }

        if (houseSpinner.getItems().size() <= 0) {
            displaySnackbar("No houses on the list");
            return;
        }

        int selectedHouse = houseSpinner.getSelectedIndex();
        int userIndex = userSpinner.getSelectedIndex();
        UserViewModel selectedUser = (UserViewModel) userSpinner.getItems().get(userIndex);
        if (selectedUser.getEmailAddress() == sharedPreferences.getString("emailAddress", "")) {
            displaySnackbar("You cannot message yourself");
            return;
        }
        House house = (House) houseSpinner.getItems().get(selectedHouse);
        NewMessageThreadViewModel model = new NewMessageThreadViewModel(house.getHouseID(), titleEditText.getText().toString().trim(), descriptionEditText.getText().toString().trim(), selectedUser.getEmailAddress(),"", sharedPreferences.getString("emailAddress", ""), null);
        sendMessage(model);


    }

    @Override
    public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
        House selectedHouse = (House) houseSpinner.getItems().get(position);
        if (selectedHouse != null) {
            getHouseUsers(selectedHouse);
        }

    }

    private void getSubscribedHouses() {
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setCancelable(false);
        dialog.setMessage("Fetching data. Please wait...");
        dialog.show();
        RequestBody emailBody = RequestBody.create(MultipartBody.FORM, sharedPreferences.getString("emailAddress", ""));
        Call<ListResponse<House>> houseCall = service.getSubscribedHouses("Bearer "+sharedPreferences.getString("authorization_token", ""), emailBody, getResources().getString(R.string.homenet_client_string));
        houseCall.enqueue(new Callback<ListResponse<House>>() {
            @Override
            public void onResponse(Call<ListResponse<House>> call, Response<ListResponse<House>> response) {
                if (dialog.isShowing()) {
                    dialog.cancel();
                }
                if (response.isSuccessful()) {
                    List<House> houseList = response.body().getModel();
                    if (houseList != null) {
                        if (houseList.size() > 0) {
                            houseSpinner.setItems(houseList);
                            if (houseList.size() == 1) {
                                House selected = houseList.get(0);
                                getHouseUsers(selected);
                            }
                        }
                    }
                } else {
                    displaySnackbar("Error Getting Houses");
                }
            }

            @Override
            public void onFailure(Call<ListResponse<House>> call, Throwable t) {
                if (dialog.isShowing()) {
                    dialog.cancel();
                }
                displaySnackbar("Critical Error");
            }
        });
    }

    private void getHouseUsers(House selectedHouse) {
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Fetching house users. Please wait...");
        dialog.setCancelable(false);
        dialog.show();
        Call<ListResponse<UserViewModel>> userCall = service.getUsersInHouse("Bearer "+sharedPreferences.getString("authorization_token", ""), selectedHouse.getHouseID(), getResources().getString(R.string.homenet_client_string));
        userCall.enqueue(new Callback<ListResponse<UserViewModel>>() {
            @Override
            public void onResponse(Call<ListResponse<UserViewModel>> call, Response<ListResponse<UserViewModel>> response) {
                if (dialog.isShowing()) {
                    dialog.cancel();
                }
                if (response.isSuccessful()) {
                    List<UserViewModel> userList = response.body().getModel();
                    if (userList != null) {
                        for (UserViewModel model : userList) {
                            houseUserList.add(model);
                        }
                    }
                    if (houseUserList.size() > 0) {
                        userSpinner.setItems(houseUserList);
                    } else {
                        displaySnackbar("No users found");
                    }
                }
            }

            @Override
            public void onFailure(Call<ListResponse<UserViewModel>> call, Throwable t) {
                if (dialog.isShowing()) {
                    dialog.cancel();
                }
            }
        });
    }

    private void sendMessage(NewMessageThreadViewModel model) {
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Sending message. Please wait...");
        dialog.setCancelable(false);
        dialog.show();
        Call<SingleResponse<MessageThread>> newMessage = service.createMessageThread("Bearer "+sharedPreferences.getString("authorization_token", ""), model, getResources().getString(R.string.homenet_client_string));
        newMessage.enqueue(new Callback<SingleResponse<MessageThread>>() {
            @Override
            public void onResponse(Call<SingleResponse<MessageThread>> call, Response<SingleResponse<MessageThread>> response) {
                if (dialog.isShowing()) {
                    dialog.cancel();
                }
                if (response.isSuccessful()) {
                    MessageThread results = response.body().getModel();
                    if (results != null) {
                        displayMessage("Message Created", "Message has been created successfully!", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                getActivity().getFragmentManager().popBackStack();
                            }
                        });
                    }
                } else {
                    try {
                        displayMessage("Error", response.errorBody().string(), null);
                    } catch (Exception error) {

                    }
                }
            }

            @Override
            public void onFailure(Call<SingleResponse<MessageThread>> call, Throwable t) {
                if (dialog.isShowing()) {
                    dialog.cancel();
                }
                displayMessage("Critical Error", t.getMessage(), null);
            }
        });


    }

    private void displayMessage(String title, String message, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title).setMessage(message).setCancelable(false).setPositiveButton("Okay", listener);
        builder.show();
    }

    private void displaySnackbar(String message) {
        Snackbar.make(getView(), message, Snackbar.LENGTH_INDEFINITE).show();
    }
}
