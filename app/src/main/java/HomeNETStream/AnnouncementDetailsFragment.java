package HomeNETStream;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.BaseTransientBottomBar;
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
import android.widget.TextView;
import android.widget.Toast;

import com.koeksworld.homenet.R;

import java.io.IOException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import Adapters.AnnouncementCommentsAdapter;
import Communication.HomeNetService;
import Models.AnnouncementComment;
import Models.AnnouncementCommentViewModel;
import Models.HouseAnnouncement;
import Models.NewCommentViewModel;
import ResponseModels.ListResponse;
import ResponseModels.SingleResponse;
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
public class AnnouncementDetailsFragment extends Fragment implements View.OnClickListener {

    private HouseAnnouncement selectedAnnouncement;
    private TextView titleTextView;
    private TextView messageTextView;
    private RecyclerView repliesRecyclerView;
    private EditText replyEditText;
    private FloatingActionButton replyButton, refreshButton;
    private TextView toolbarTextView;
    private Retrofit retrofit;
    private HomeNetService service;
    private OkHttpClient client;
    private List<Protocol> protocolList;
    private List<AnnouncementCommentViewModel> announcementList;
    private SharedPreferences sharedPreferences;
    private String errorString = "";

    public AnnouncementDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View currentView = inflater.inflate(R.layout.fragment_announcement_details, container, false);
        initializeComponents(currentView);
        if (savedInstanceState == null) {
            selectedAnnouncement = getArguments().getParcelable("SelectedAnnouncement");
        } else {
            selectedAnnouncement = savedInstanceState.getParcelable("SelectedAnnouncement");
        }
        setup();
        initializeRetrofit();
        initializeData();
        getAnnouncementComments();
        return currentView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("SelectedAnnouncement", selectedAnnouncement);
    }

    private void initializeComponents(View currentView) {
        titleTextView = (TextView) currentView.findViewById(R.id.AnnouncementTitleTextView);
        toolbarTextView = (TextView) getActivity().findViewById(R.id.HomeNetFeedToolbarTextView);
        toolbarTextView.setText("Announcement Details");
        messageTextView = (TextView) currentView.findViewById(R.id.AnnouncementMessageTextView);
        repliesRecyclerView = (RecyclerView) currentView.findViewById(R.id.AnnouncementRepliesRecyclerView);
        repliesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        replyEditText = (EditText) currentView.findViewById(R.id.AnnouncementReplyEditText);
        replyButton = (FloatingActionButton) currentView.findViewById(R.id.AnnouncementReplyButton);
        replyButton.setOnClickListener(this);
        refreshButton = (FloatingActionButton) currentView.findViewById(R.id.AnnouncementRefreshButton);
        refreshButton.setOnClickListener(this);
    }

    private void initializeData() {
        announcementList = new ArrayList<>();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.AnnouncementReplyButton:
                if (replyEditText.getText().toString() == "") {
                    displaySnackbar("Please provide a comment");
                    return;
                }

                if (replyEditText.getText().toString().length() <= 2) {
                    displaySnackbar("Please provide a comment longer than 2 characters");
                    return;
                }

                NewCommentViewModel newCommentViewModel = new NewCommentViewModel(selectedAnnouncement.getHouseAnnouncementID(), replyEditText.getText().toString().trim(), sharedPreferences.getString("emailAddress", ""));
                addNewComment(newCommentViewModel);

                break;
            case R.id.AnnouncementRefreshButton:
                getAnnouncementComments();
                break;
        }
    }

    private void setup() {
        titleTextView.setText(selectedAnnouncement.getTitle());
        messageTextView.setText(selectedAnnouncement.getMessage());
    }

    private void addNewComment(NewCommentViewModel model) {
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Sending Comment. Please wait...");
        dialog.setCancelable(false);
        dialog.show();
        Call<SingleResponse<AnnouncementComment>> newCommentCall = service.createAnnouncementComment("Bearer "+sharedPreferences.getString("authorization_token",""), model, getActivity().getResources().getString(R.string.homenet_client_string));
        newCommentCall.enqueue(new Callback<SingleResponse<AnnouncementComment>>() {
            @Override
            public void onResponse(Call<SingleResponse<AnnouncementComment>> call, Response<SingleResponse<AnnouncementComment>> response) {
                if (dialog.isShowing()) {
                    dialog.cancel();
                }
                if (response.isSuccessful()) {
                    displayMessage("Comment Saved", "Comment Saved Successfully!", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            getAnnouncementComments();
                        }
                    });
                } else {
                    try {
                        errorString = response.errorBody().string();
                        displayMessage("Error", errorString, null);
                    } catch (Exception error) {

                    }
                }
            }

            @Override
            public void onFailure(Call<SingleResponse<AnnouncementComment>> call, Throwable t) {
                if (dialog.isShowing()) {
                    dialog.cancel();
                }
                errorString = t.getMessage();
                displayMessage("Crticial Error", errorString, null);
            }
        });
    }

    private void initializeRetrofit() {
        protocolList = new ArrayList<>();
        protocolList.add(Protocol.HTTP_1_1);
        client = new OkHttpClient.Builder().readTimeout(2, TimeUnit.MINUTES).connectTimeout(2, TimeUnit.MINUTES).protocols(protocolList).build();
        retrofit = new Retrofit.Builder().baseUrl(getActivity().getResources().getString(R.string.homenet_link)).addConverterFactory(GsonConverterFactory.create()).client(client).build();
        service = retrofit.create(HomeNetService.class);
    }

    private void getAnnouncementComments() {
        announcementList.clear();
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Fetching comments. Please wait...");
        dialog.setCancelable(false);
        dialog.show();
        Call<ListResponse<AnnouncementCommentViewModel>> commentCall = service.getAnnouncementComments("Bearer "+sharedPreferences.getString("authorization_token",""), selectedAnnouncement.getHouseAnnouncementID(), getActivity().getResources().getString(R.string.homenet_client_string));
        commentCall.enqueue(new Callback<ListResponse<AnnouncementCommentViewModel>>() {
            @Override
            public void onResponse(Call<ListResponse<AnnouncementCommentViewModel>> call, Response<ListResponse<AnnouncementCommentViewModel>> response) {
                if (dialog.isShowing()) {
                    dialog.cancel();
                }
                if (response.isSuccessful()) {
                    if (response.body().getModel() != null) {
                        for(AnnouncementCommentViewModel model : response.body().getModel()) {
                            announcementList.add(model);
                        }
                        AnnouncementCommentsAdapter adapter = new AnnouncementCommentsAdapter(announcementList, getActivity());
                        repliesRecyclerView.setAdapter(adapter);
                    } else {
                        Snackbar.make(getActivity().findViewById(android.R.id.content), "No comments found", Toast.LENGTH_LONG).show();
                    }
                } else if (response.code() == 404) {
                    Snackbar.make(getActivity().findViewById(android.R.id.content), "No comments found", Toast.LENGTH_LONG).show();
                } else {
                    try {
                        errorString = response.errorBody().string();
                        displayMessage("Critical Error", errorString, null);
                    } catch (Exception error) {

                    }
                }
            }

            @Override
            public void onFailure(Call<ListResponse<AnnouncementCommentViewModel>> call, Throwable t) {
                if (dialog.isShowing()) {
                    dialog.cancel();
                }
                errorString = t.getMessage();
                displayMessage("Critical Error", errorString, null);


            }
        });
    }

    private void displayMessage(String title, String message, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder messageBox = new AlertDialog.Builder(getActivity());
        messageBox.setTitle(title).setMessage(message).setPositiveButton("Got it", listener).setCancelable(false).show();
    }

    private void displaySnackbar(String message) {
        Snackbar.make(getActivity().findViewById(android.R.id.content), message, BaseTransientBottomBar.LENGTH_LONG).show();
    }


}
