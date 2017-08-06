package HomeNETStream;


import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.jaredrummler.materialspinner.MaterialSpinner;
import com.koeksworld.homenet.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import Communication.HomeNetService;
import Models.House;
import Models.NewMessageThreadViewModel;
import Models.ParticipantViewModel;
import Models.User;
import Models.UserViewModel;
import ResponseModels.ListResponse;
import Tasks.GetSubscribedHousesTask;
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
public class CreateMessageFragment extends Fragment implements View.OnClickListener {

    private EditText messageTitleTextView;
    private EditText messageDescriptionEditText;
    private FloatingActionButton submitButton;
    private MaterialSpinner userSpinner, houseSpinner;
    private House selectedHouse;
    private List<House> userHouses;
    private GetSubscribedHousesTask task;
    private OkHttpClient client;
    private Retrofit retrofit;
    private HomeNetService service;
    private List<Protocol> protocolList;
    private SharedPreferences sharedPreferences;
    public CreateMessageFragment() {
        // Required empty public constructor
    }

    private void initializeRetrofit() {
        protocolList = new ArrayList<>();
        protocolList.add(Protocol.HTTP_1_1);
        client = new OkHttpClient.Builder().connectTimeout(2, TimeUnit.MINUTES).readTimeout(2, TimeUnit.MINUTES).protocols(protocolList).build();
        retrofit = new Retrofit.Builder().baseUrl(getResources().getString(R.string.homenet_link)).client(client).addConverterFactory(GsonConverterFactory.create()).build();
        service = retrofit.create(HomeNetService.class);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View currentView = inflater.inflate(R.layout.fragment_create_message, container, false);
        initializeComponents(currentView);
        initializeRetrofit();
        task = new GetSubscribedHousesTask(getActivity(), houseSpinner);
        task.execute();
        return currentView;
    }

    private void initializeComponents(View currentView) {
        messageTitleTextView = (EditText) currentView.findViewById(R.id.MessageTitleTextView);
        messageDescriptionEditText = (EditText) currentView.findViewById(R.id.MessageDescriptionTextView);
        submitButton = (FloatingActionButton) currentView.findViewById(R.id.SendPrivateMessageButton);
        userSpinner = (MaterialSpinner) currentView.findViewById(R.id.SelectRecepientSpinner);
        houseSpinner = (MaterialSpinner) currentView.findViewById(R.id.SelectHouseSpinner);
        houseSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                selectedHouse = (House) item;


            }
        });
        submitButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (messageTitleTextView.getText().toString() == "") {
            displaySnackbar("Please enter a valid title");
            return;
        }
        if (messageDescriptionEditText.getText().toString() == "") {
            displaySnackbar("Please enter a description");
            return;
        }
        List<ParticipantViewModel> list = new ArrayList<>();
        NewMessageThreadViewModel model = new NewMessageThreadViewModel();
        model.setThreadTitle(messageTitleTextView.getText().toString());
        model.setThreadMessage(messageDescriptionEditText.getText().toString());
        model.setEmailAddress(sharedPreferences.getString("emailAddress", ""));
        model.setHouseID(selectedHouse.getHouseID());

        int selectedIndex = userSpinner.getSelectedIndex();
        UserViewModel selected = (UserViewModel) userSpinner.getItems().get(selectedIndex);
        ParticipantViewModel user = new ParticipantViewModel();
        user.setEmailAddress(selected.getEmailAddress());
        //user.setIsDeleted(0);


    }

    private void displaySnackbar(String message) {
        Snackbar.make(getActivity().findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show();
    }

    private void getUsersInHouse() {
        Snackbar snackbar = Snackbar.make(getActivity().findViewById(android.R.id.content), "Finding User List. Please wait...", Snackbar.LENGTH_INDEFINITE);
        snackbar.show();
        Call<ListResponse<UserViewModel>> userCall = service.getUsersInHouse("Bearer "+sharedPreferences.getString("authorization_token", ""), selectedHouse.getHouseID(), getResources().getString(R.string.homenet_client_string));
        userCall.enqueue(new Callback<ListResponse<UserViewModel>>() {
            @Override
            public void onResponse(Call<ListResponse<UserViewModel>> call, Response<ListResponse<UserViewModel>> response) {
                if (response.isSuccessful()) {
                    List<UserViewModel> list = response.body().getModel();
                    if (list != null) {
                        userSpinner.setItems(list);
                    } else {
                        displayMessage("Error Getting User List", "List could not be displayed", null);
                    }
                }
            }

            @Override
            public void onFailure(Call<ListResponse<UserViewModel>> call, Throwable t) {
                displayMessage("Critical Error Getting User List", t.getMessage(), null);
            }
        });
    }

    private void displayMessage(String title, String message, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder messageBox = new AlertDialog.Builder(getActivity());
        messageBox.setTitle(title).setMessage(message).setPositiveButton("Okay", listener).show();

    }


}
