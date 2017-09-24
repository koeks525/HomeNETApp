package NotificationFragments;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import com.google.firebase.crash.FirebaseCrash;
import com.koeksworld.homenet.HomeNetFeedActivity;
import com.koeksworld.homenet.R;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import Adapters.FeedItemAdapter;
import Communication.HomeNetService;
import Models.CommentViewModel;
import Models.HousePost;
import Models.HousePostMetaData;
import Models.HousePostMetaDataViewModel;
import Models.HousePostViewModel;
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
public class NotificationNewPostFragment extends Fragment implements View.OnClickListener {

    private int housePostID;
    private TextView nameTextView;
    private ImageView nameSurnameImageView;
    private TextView postDescriptionTextView;
    private TextView likesTextView;
    private TextView dislikesTextView;
    private TextView totalCommentsTextView;
    private HousePost housePost;
    private RecyclerView commentsRecyclerView;
    private FloatingActionButton replyButton, refreshButton;
    private EditText replyEditText;
    private Retrofit retrofit;
    private SharedPreferences sharedPreferences;
    private List<Protocol> protocolList;
    private OkHttpClient client;
    private HomeNetService service;
    private HousePostViewModel model;
    private HousePostMetaDataViewModel metaData;
    private List<CommentViewModel> commentList;

    public NotificationNewPostFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View currentView = inflater.inflate(R.layout.fragment_notification_new_post, container, false);
        initializeComponents(currentView);
        getData();
        initializeRetrofit();
        if (housePostID <= 0) {
            displayMessage("No data", "No house post data has been received. You will be taken to your news feed", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent home = new Intent(getActivity(), HomeNetFeedActivity.class);
                    startActivity(home);
                    getActivity().finish();
                }
            });
        } else {
            getHousePostMetaData();
        }
        return currentView;

    }

    private void getData() {
        housePostID = getArguments().getInt("housePostID");
    }
    private void initializeComponents(View currentView) {
        commentList = new ArrayList<>();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        nameTextView = (TextView) currentView.findViewById(R.id.NotificationPostItemNameSurnameTextView);
        nameSurnameImageView = (ImageView) currentView.findViewById(R.id.NotificationPostItemProfileImageView);
        postDescriptionTextView = (TextView) currentView.findViewById(R.id.NotificationPostItemTextView);
        likesTextView = (TextView) currentView.findViewById(R.id.NotificationPostItemTotalLikesTextView);
        dislikesTextView = (TextView) currentView.findViewById(R.id.NotificationPostItemDislikesTextView);
        commentsRecyclerView = (RecyclerView) currentView.findViewById(R.id.NotificationPostItemRecyclerView);
        commentsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        replyButton = (FloatingActionButton) currentView.findViewById(R.id.NotificationPostItemReplyButton);
        refreshButton = (FloatingActionButton) currentView.findViewById(R.id.NotificationPostItemRefreshButton);
        replyEditText = (EditText) currentView.findViewById(R.id.NotificationPostItemReplyEditText);
        totalCommentsTextView = (TextView) currentView.findViewById(R.id.NotificationPostItemTotalCommentsTextView);
        replyButton.setOnClickListener(this);
        refreshButton.setOnClickListener(this);
    }

    private void initializeRetrofit() {
        protocolList = new ArrayList<>();
        protocolList.add(Protocol.HTTP_1_1);
        client = new OkHttpClient.Builder().connectTimeout(2, TimeUnit.MINUTES).readTimeout(2, TimeUnit.MINUTES).protocols(protocolList).build();
        retrofit = new Retrofit.Builder().baseUrl(getResources().getString(R.string.homenet_link)).client(client).addConverterFactory(GsonConverterFactory.create()).build();
        service = retrofit.create(HomeNetService.class);
    }
    private void displayMessage(String title, String message, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title).setMessage(message).setCancelable(false).setPositiveButton("Ok", listener).show();
    }
    private void getHousePostMetaData() {
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Fetching house post. Please wait...");
        dialog.setCancelable(false);
        dialog.show();
        Call<SingleResponse<HousePost>> postData = service.getHousePost("Bearer "+sharedPreferences.getString("authorization_token", ""), housePostID, getResources().getString(R.string.homenet_client_string));
        postData.enqueue(new Callback<SingleResponse<HousePost>>() {
            @Override
            public void onResponse(Call<SingleResponse<HousePost>> call, Response<SingleResponse<HousePost>> response) {
                if (response.isSuccessful()) {
                    if (response.body().getModel() != null) {
                        housePost = response.body().getModel();
                    }
                    if (housePost != null) {
                        dialog.setMessage("Fetching additional house post data. Please wait...");
                        Call<SingleResponse<HousePostViewModel>> postCall = service.getHousePostData("Bearer " +sharedPreferences.getString("authorization_token",""), housePost.getHousePostID(), getResources().getString(R.string.homenet_client_string));
                        postCall.enqueue(new Callback<SingleResponse<HousePostViewModel>>() {
                            @Override
                            public void onResponse(Call<SingleResponse<HousePostViewModel>> call, Response<SingleResponse<HousePostViewModel>> response) {
                                if (response.isSuccessful()) {
                                    if (response.body().getModel() != null) {
                                        model = response.body().getModel();
                                        nameTextView.setText(model.getName() + " "+model.getSurname());
                                        postDescriptionTextView.setText(model.getPostText());
                                        TextDrawable nameSurname = TextDrawable.builder().buildRect(model.getName().substring(0,1).toUpperCase() + model.getSurname().substring(0,1).toUpperCase(), Color.BLUE);
                                        nameSurnameImageView.setImageDrawable(nameSurname);
                                        final Call<SingleResponse<HousePostMetaDataViewModel>> metaDataCall = service.getHousePostMetaData("Bearer "+sharedPreferences.getString("authorization_token", ""), housePost.getHousePostID(), getResources().getString(R.string.homenet_client_string));
                                        metaDataCall.enqueue(new Callback<SingleResponse<HousePostMetaDataViewModel>>() {
                                            @Override
                                            public void onResponse(Call<SingleResponse<HousePostMetaDataViewModel>> call, Response<SingleResponse<HousePostMetaDataViewModel>> response) {
                                                if (response.isSuccessful()) {
                                                    if (response.body().getModel() != null) {
                                                        metaData = response.body().getModel();
                                                        likesTextView.setText(Integer.toString(metaData.getTotalLikes()));
                                                        dislikesTextView.setText(Integer.toString(metaData.getTotalDislikes()));
                                                        totalCommentsTextView.setText(Integer.toString(metaData.getTotalComments()) + " Comments");
                                                    }
                                                    if (metaData != null) {
                                                        dialog.setMessage("Fetching post comments. Please wait...");
                                                        Call<ListResponse<CommentViewModel>> commentCall = service.getComments("Bearer "+sharedPreferences.getString("authorization_token", ""), housePost.getHousePostID(), getResources().getString(R.string.homenet_client_string));
                                                        commentCall.enqueue(new Callback<ListResponse<CommentViewModel>>() {
                                                            @Override
                                                            public void onResponse(Call<ListResponse<CommentViewModel>> call, Response<ListResponse<CommentViewModel>> response) {
                                                                if (response.isSuccessful()) {
                                                                    if (dialog.isShowing()) {
                                                                        dialog.cancel();
                                                                    }
                                                                    if (response.body().getModel() != null) {
                                                                        for(CommentViewModel comment : response.body().getModel()) {
                                                                            commentList.add(comment);

                                                                        }
                                                                        if (commentList.size() > 0) {
                                                                            FeedItemAdapter adapter = new FeedItemAdapter(commentList);
                                                                            commentsRecyclerView.setAdapter(adapter);
                                                                        }


                                                                    } else {
                                                                        Snackbar.make(getView(), "No comments yet", Snackbar.LENGTH_LONG).show();
                                                                    }
                                                                }

                                                            }

                                                            @Override
                                                            public void onFailure(Call<ListResponse<CommentViewModel>> call, Throwable t) {
                                                                if (dialog.isShowing()) {
                                                                    dialog.cancel();
                                                                }
                                                                FirebaseCrash.log(t.getMessage());
                                                                displayMessage("Critical Error", "Error getting post comments. You will be taken to your news feed", new DialogInterface.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                                        Intent main = new Intent(getActivity(), HomeNetFeedActivity.class);
                                                                        startActivity(main);
                                                                        getActivity().finish();
                                                                    }
                                                                });

                                                            }
                                                        });

                                                    }

                                                } else {
                                                    if (dialog.isShowing()) {
                                                        dialog.cancel();
                                                    }
                                                    displayMessage("Error", "No house post meta data found", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialogInterface, int i) {
                                                            Intent main = new Intent(getActivity(), HomeNetFeedActivity.class);
                                                            startActivity(main);
                                                            getActivity().finish();
                                                        }
                                                    });
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<SingleResponse<HousePostMetaDataViewModel>> call, Throwable t) {
                                                if (dialog.isShowing()) {
                                                    dialog.cancel();
                                                }
                                                FirebaseCrash.log(t.getMessage());
                                                displayMessage("Critical Error", "Error getting meta data. You will be taken to the news feed", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        Intent newIntent  = new Intent(getActivity(), HomeNetFeedActivity.class);
                                                        startActivity(newIntent);
                                                        getActivity().finish();
                                                    }
                                                });
                                            }
                                        });

                                    }
                                    else {
                                        displayMessage("Error", "No post data was found. You will be taken to your news feed", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                Intent newIntent = new Intent(getActivity(), HomeNetFeedActivity.class);
                                                startActivity(newIntent);
                                                getActivity().finish();
                                            }
                                        });
                                    }
                                } else {
                                    if (dialog.isShowing()) {
                                        dialog.cancel();
                                    }
                                    displayMessage("Error", "Something went wrong with fetching house post data. You will be taken to your news feed", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            Intent mainIntent = new Intent(getActivity(), HomeNetFeedActivity.class);
                                            startActivity(mainIntent);
                                            getActivity().finish();
                                        }
                                    });
                                }
                            }

                            @Override
                            public void onFailure(Call<SingleResponse<HousePostViewModel>> call, Throwable t) {
                                if (dialog.isShowing()) {
                                    dialog.cancel();
                                }
                                displayMessage("Critical Error", "Error Getting post data. You will be taken to your news feed", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Intent main = new Intent(getActivity(), HomeNetFeedActivity.class);
                                        startActivity(main);
                                        getActivity().finish();
                                    }
                                });
                            }
                        });


                    }


                } else {
                    displayMessage("House Post Error", "Something went wrong with getting house post data. As a result, you will be taken to your news feed", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent newIntent = new Intent(getActivity(), HomeNetFeedActivity.class);
                            startActivity(newIntent);
                            getActivity().finish();
                        }
                    });
                }
            }
            @Override
            public void onFailure(Call<SingleResponse<HousePost>> call, Throwable t) {
                if (dialog.isShowing()) {
                    dialog.cancel();
                }
                displayMessage("Critical Error", "Something went wrong with getting house post data. System will take you to the news feed", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent main = new Intent(getActivity(), HomeNetFeedActivity.class);
                        startActivity(main);
                        getActivity().finish();
                    }
                });
            }
        });


    }


    @Override
    public void onClick(View view) {

    }
}
