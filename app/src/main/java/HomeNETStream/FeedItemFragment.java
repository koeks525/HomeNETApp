package HomeNETStream;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.koeksworld.homenet.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import Adapters.FeedItemAdapter;
import Communication.HomeNetService;
import DialogFragments.PhotoDetailsFragment;
import Models.CommentPartialModel;
import Models.CommentViewModel;
import Models.HousePostMetaDataViewModel;
import Models.HousePostViewModel;
import Models.Picture;
import ResponseModels.ListResponse;
import ResponseModels.SingleResponse;
import Tasks.PostDetailsTask;
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
public class FeedItemFragment extends Fragment implements View.OnClickListener {


    private FloatingActionButton replyButton;
    private FloatingActionButton refreshButton;
    private TextView nameSurnameTextView, likeTextView, dislikeTextView, feedItemTextView, commentsTextView, postImageTextView;
    private ImageView profileImageView, likeImage, dislikeImage;
    private OkHttpClient client;
    private Retrofit retrofit;
    private HomeNetService service;
    private List<Protocol> protocolList;
    private HousePostViewModel selectedPost;
    private HousePostMetaDataViewModel metaData;
    private TextView toolbarTextView;
    private SharedPreferences sharedPreferences;
    private RecyclerView commentRecyclerView;
    private EditText replyEditText;
    private Picture pictureObject;
    private Bundle savedInstance;

    public FeedItemFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View currentView = inflater.inflate(R.layout.fragment_feed_item, container, false);
        initializeComponents(currentView);
        initializeRetrofit();
        if (savedInstanceState == null) {
            selectedPost = getArguments().getParcelable("SelectedPost");
            metaData = getArguments().getParcelable("MetaData");
        } else {
            selectedPost = savedInstanceState.getParcelable("SelectedPost");
            metaData = savedInstanceState.getParcelable("MetaData");
            savedInstance = savedInstanceState;
        }
        setup();
        getPostComments();
        return currentView;
    }

    private void initializeComponents(View currentView) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        replyButton = (FloatingActionButton) currentView.findViewById(R.id.FeedItemReplyButton);
        refreshButton = (FloatingActionButton) currentView.findViewById(R.id.FeedItemRefreshButton);
        replyButton.setOnClickListener(this);
        refreshButton.setOnClickListener(this);
        nameSurnameTextView = (TextView) currentView.findViewById(R.id.FeedItemNameSurnameTextView);
        likeTextView = (TextView) currentView.findViewById(R.id.FeedItemTotalLikesTextView);
        dislikeTextView = (TextView) currentView.findViewById(R.id.FeedItemDislikesTextView);
        likeImage = (ImageView) currentView.findViewById(R.id.FeedItemLikeImageView);
        dislikeImage = (ImageView) currentView.findViewById(R.id.FeedItemDislikesImageView);
        profileImageView = (ImageView) currentView.findViewById(R.id.FeedItemProfileImageView);
        toolbarTextView = (TextView) getActivity().findViewById(R.id.HomeNetFeedToolbarTextView);
        feedItemTextView = (TextView) currentView.findViewById(R.id.FeedItemTextView);
        commentsTextView = (TextView) currentView.findViewById(R.id.FeedItemTotalCommentsTextView);
        commentRecyclerView = (RecyclerView) currentView.findViewById(R.id.FeedItemRecyclerView);
        commentRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        replyEditText = (EditText) currentView.findViewById(R.id.FeedItemReplyEditText);
        postImageTextView = (TextView) currentView.findViewById(R.id.FeedItemViewImageTextView);
        postImageTextView.setOnClickListener(this);
    }

    private void initializeRetrofit() {
        protocolList = new ArrayList<>();
        protocolList.add(Protocol.HTTP_1_1);
        client = new OkHttpClient.Builder().connectTimeout(3, TimeUnit.MINUTES).readTimeout(3, TimeUnit.MINUTES).protocols(protocolList).build();
        retrofit = new Retrofit.Builder().baseUrl(getResources().getString(R.string.homenet_link)).addConverterFactory(GsonConverterFactory.create()).client(client).build();
        service = retrofit.create(HomeNetService.class);
    }

    private void setup() {
        pictureObject = new Picture();
        if (selectedPost != null && metaData != null) {
            toolbarTextView.setText(selectedPost.getName() + " "+selectedPost.getSurname()+"'s Post");
            nameSurnameTextView.setText(selectedPost.getName() +" "+selectedPost.getSurname());
            feedItemTextView.setText(selectedPost.getPostText());
            likeTextView.setText(Integer.toString(metaData.getTotalLikes()));
            dislikeTextView.setText(Integer.toString(metaData.getTotalDislikes()));
            commentsTextView.setText(Integer.toString(metaData.getTotalComments()) +" Comments");

            TextDrawable drawable = TextDrawable.builder().buildRect(selectedPost.getName().substring(0,1).toUpperCase()+selectedPost.getSurname().substring(0,1).toUpperCase(), Color.BLUE);
            profileImageView.setImageDrawable(drawable);
        } else {
            displayMessage("Post Data Missing", "Post data has gone missing. Our enginners will be notified of this glitch", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    getActivity().getFragmentManager().popBackStack();
                }
            });
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("SelectedPost", selectedPost);
        outState.putParcelable("MetaData", metaData);
    }

    private void displayMessage(String title, String message, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder messageBox = new AlertDialog.Builder(getActivity());
        messageBox.setTitle(title).setMessage(message).setCancelable(false).setPositiveButton("Okay", listener).show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.FeedItemReplyButton:
                String comment = replyEditText.getText().toString().trim();
                if (comment == "") {
                    displaySnackbar("Please enter some text");
                    return;
                }
                addComment(comment);

                break;
            case R.id.FeedItemRefreshButton:
                getPostComments();

                break;
        }
    }

    private void executeGetPictureData() {
        try {
            PostDetailsTask task = new PostDetailsTask(getActivity(), selectedPost, metaData, postImageTextView);
            task.execute().wait();
        } catch (Exception error) {

        }
    }

    private void getPostComments() {
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setCancelable(false);
        dialog.setMessage("Searching for comments. Please wait...");
        dialog.show();
        final Call<ListResponse<CommentViewModel>> commentCall = service.getComments("Bearer "+sharedPreferences.getString("authorization_token", ""), selectedPost.getHousePostID(), getResources().getString(R.string.homenet_client_string));
        commentCall.enqueue(new Callback<ListResponse<CommentViewModel>>() {
            @Override
            public void onResponse(Call<ListResponse<CommentViewModel>> call, Response<ListResponse<CommentViewModel>> response) {
                if (dialog.isShowing()) {
                    dialog.cancel();
                }
                if (response.code() == 200) {
                    List<CommentViewModel> list = response.body().getModel();
                    if (list == null) {
                        displaySnackbar("No Posts found. Please refresh");
                        executeGetPictureData();
                    } else {
                        FeedItemAdapter adapter = new FeedItemAdapter(list);
                        commentRecyclerView.setAdapter(adapter);
                        executeGetPictureData();
                    }


                } else if (response.code() == 404) {
                    displaySnackbar("No comments found!");
                    executeGetPictureData();
                } else {
                    try {
                        displayMessage("Error", response.errorBody().string(), null);

                    } catch (Exception error) {

                    }
                }
            }

            @Override
            public void onFailure(Call<ListResponse<CommentViewModel>> call, Throwable t) {
                if (dialog.isShowing()) {
                    dialog.cancel();
                }
                displayMessage("Critical Error", t.getMessage(), null);
            }
        });

    }

    public void addComment(String comment) {
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Saving comment. Please wait...");
        dialog.setCancelable(false);
        dialog.show();
        CommentPartialModel model = new CommentPartialModel(sharedPreferences.getString("emailAddress", ""), comment, selectedPost.getHousePostID());
        Call<SingleResponse<CommentViewModel>> newComment = service.addComment("Bearer "+sharedPreferences.getString("authorization_token", ""), model, getResources().getString(R.string.homenet_client_string));
        newComment.enqueue(new Callback<SingleResponse<CommentViewModel>>() {
            @Override
            public void onResponse(Call<SingleResponse<CommentViewModel>> call, Response<SingleResponse<CommentViewModel>> response) {
                if (dialog.isShowing()) {
                    dialog.cancel();
                }

                if (response.code() == 200 ) {
                    CommentViewModel model = null;
                    if (response.body().getModel() != null) {
                        model = response.body().getModel();
                    }
                    if (model != null) {
                        displayMessage("Comment Saved", "Comment has been saved successfully!", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                getPostComments();
                            }
                        });
                    }

                } else if (response.code() == 404) {
                    displaySnackbar("Couldnt add comment");
                } else {
                    try {
                        displayMessage("Error", response.errorBody().string(), null);
                    } catch (Exception error) {

                    }
                }


            }

            @Override
            public void onFailure(Call<SingleResponse<CommentViewModel>> call, Throwable t) {
                if (dialog.isShowing()) {
                    dialog.cancel();
                }
                displayMessage("Critical Error", t.getMessage(), null);
            }
        });

    }

    public void displaySnackbar(String message) {
        Snackbar.make(getView(),message, Snackbar.LENGTH_LONG ).show();
    }
}
