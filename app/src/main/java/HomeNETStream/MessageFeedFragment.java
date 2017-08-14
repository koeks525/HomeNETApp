package HomeNETStream;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.app.Fragment;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.koeksworld.homenet.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import Communication.HomeNetService;
import Models.MessageThread;
import Models.MessageThreadMessage;
import Models.MessageViewModel;
import ResponseModels.SingleResponse;
import Tasks.GetMessageThreadMessagesTask;
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
public class MessageFeedFragment extends Fragment implements View.OnClickListener {

    private RecyclerView recyclerView;
    private EditText replyEditText;
    private FloatingActionButton replyButton, refreshButton;
    private MessageThread selectedThread;
    private GetMessageThreadMessagesTask task;
    private TextView toolbarTextView;
    private SharedPreferences sharedPreferences;
    private OkHttpClient client;
    private List<Protocol> protocolList;
    private Retrofit retrofit;
    private HomeNetService service;


    public MessageFeedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View currentView = inflater.inflate(R.layout.fragment_message_feed, container, false);
        initializeComponents(currentView);
        initializeRetrofit();
        if (savedInstanceState == null) {
            getData();
        } else {
            selectedThread = savedInstanceState.getParcelable("SelectedThread");
        }

        task = new GetMessageThreadMessagesTask(getActivity(), recyclerView, selectedThread);
        task.execute();
        return currentView;

    }

    private void initializeComponents(View currentView) {
        recyclerView = (RecyclerView) currentView.findViewById(R.id.MessagesFeedRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        replyEditText = (EditText) currentView.findViewById(R.id.MessageFeedEditText);
        replyButton = (FloatingActionButton) currentView.findViewById(R.id.MessageFeedSendReplyButton);
        refreshButton = (FloatingActionButton) currentView.findViewById(R.id.MessageDetailsRefreshButton);
        refreshButton.setOnClickListener(this);
        toolbarTextView = (TextView) getActivity().findViewById(R.id.HomeNetFeedToolbarTextView);
        toolbarTextView.setText("Message Details");
        replyButton.setOnClickListener(this);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("SelectedThread", selectedThread);
    }

    private void getData() {
        selectedThread = getArguments().getParcelable("SelectedThread");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.MessageDetailsRefreshButton:
                task = new GetMessageThreadMessagesTask(getActivity(), recyclerView, selectedThread);
                task.execute();
                break;
            case R.id.MessageFeedSendReplyButton:
                if (replyEditText.getText().toString() == "") {
                    displaySnackbar("Please enter valid text");
                    return;
                }

                MessageViewModel model = new MessageViewModel(selectedThread.getMessageThreadID(), selectedThread.getHouseMemberID(), replyEditText.getText().toString(), sharedPreferences.getString("emailAddress", ""));
                addMessageToThread(model);


                break;
        }
    }

    private void displaySnackbar(String message) {
        Snackbar.make(getView(), message, Snackbar.LENGTH_LONG).show();
    }

    private void addMessageToThread(MessageViewModel newModel) {
        //Selected thread - get house member id.
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Adding reply. Please wait...");
        dialog.setCancelable(false);
        dialog.show();
        Call<SingleResponse<MessageThreadMessage>> newMessageCall = service.addMessageToThread("Bearer "+sharedPreferences.getString("authorization_token",""), newModel, getResources().getString(R.string.homenet_client_string));
        newMessageCall.enqueue(new Callback<SingleResponse<MessageThreadMessage>>() {
            @Override
            public void onResponse(Call<SingleResponse<MessageThreadMessage>> call, Response<SingleResponse<MessageThreadMessage>> response) {
                if (dialog.isShowing()) {
                    dialog.cancel();
                }
                if (response.isSuccessful()) {
                    if (response.body().getModel() != null) {
                        displayMessage("Reply Saved!", "Reply saved successfully!", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                task = new GetMessageThreadMessagesTask(getActivity(), recyclerView, selectedThread);
                                task.execute();
                            }
                        });
                    } else {
                        try {
                            displayMessage("Error", response.errorBody().string(), null);
                        } catch (Exception error) {

                        }
                    }
                } else {
                    try {
                        displayMessage("Error Saving Reply", response.errorBody().string(), null);
                    } catch (Exception error) {}
                }

            }

            @Override
            public void onFailure(Call<SingleResponse<MessageThreadMessage>> call, Throwable t) {
                if (dialog.isShowing()) {
                    dialog.cancel();
                }
                displayMessage("Critical Error", t.getMessage(), null);
            }
        });

    }

    private void initializeRetrofit() {
        protocolList = new ArrayList<>();
        protocolList.add(Protocol.HTTP_1_1);
        client = new OkHttpClient.Builder().readTimeout(2, TimeUnit.MINUTES).connectTimeout(2, TimeUnit.MINUTES).protocols(protocolList).build();
        retrofit = new Retrofit.Builder().baseUrl(getResources().getString(R.string.homenet_link)).client(client).addConverterFactory(GsonConverterFactory.create()).build();
        service = retrofit.create(HomeNetService.class);
    }

    private void displayMessage(String title, String message, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setTitle(title).setMessage(message).setPositiveButton("Okay", listener).setCancelable(false);
        alert.show();
    }
}
