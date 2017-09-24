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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.github.mikephil.charting.renderer.scatter.ChevronUpShapeRenderer;
import com.koeksworld.homenet.HomeNetFeedActivity;
import com.koeksworld.homenet.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import Communication.HomeNetService;
import Models.HousePost;
import Models.HousePostMetaData;
import Models.HousePostMetaDataViewModel;
import Models.HousePostViewModel;
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
public class FlaggedPostFragment extends Fragment implements View.OnClickListener {

    private HousePostViewModel housePost;
    private int housePostID;
    private Retrofit retrofit;
    private SharedPreferences sharedPreferences;
    private OkHttpClient client;
    private List<Protocol> protocolList;
    private HomeNetService service;
    private HousePostMetaDataViewModel metaDataViewModel;

    private TextView nameSurnameTextView, descriptionTextView, totalCommentsTextView, totalLikesTextView, totalDislikesTextView;
    private TextView toolbarTextView;
    private ImageView profileImageView;
    private FloatingActionButton deleteButton, unflagButton;

    public FlaggedPostFragment() {
        // Required empty public constructor
    }

    private void getData() {
        housePostID = getArguments().getInt("housePostID");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View currentView = inflater.inflate(R.layout.fragment_flagged_post, container, false);
        initializeComponents(currentView);
        initializeRetrofit();
        getData();
        if (housePostID <= 0) {
            displayMessage("No Data", "No house post data was received. You will be taken to your news feed", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent main = new Intent(getActivity(), HomeNetFeedActivity.class);
                    startActivity(main);
                    getActivity().finish();
                }
            });
        } else {
            getHousePostData();
        }
        return currentView;
    }

    private void displayMessage(String title, String message, DialogInterface.OnClickListener listen) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title).setMessage(message).setPositiveButton("Okay", listen).setCancelable(false).show();
    }

    private void initializeRetrofit() {
        protocolList = new ArrayList<>();
        client = new OkHttpClient.Builder().connectTimeout(2, TimeUnit.MINUTES).readTimeout(2, TimeUnit.MINUTES).protocols(protocolList).build();
        retrofit = new Retrofit.Builder().baseUrl(getResources().getString(R.string.homenet_link)).addConverterFactory(GsonConverterFactory.create()).client(client).build();
        service = retrofit.create(HomeNetService.class);
    }

    private void getHousePostData() {
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setCancelable(false);
        dialog.setMessage("Getting post data. Please wait... ");
        dialog.show();
        final Call<SingleResponse<HousePostViewModel>> housePostCall = service.getHousePostData("Bearer "+sharedPreferences.getString("authorization_token", ""), housePostID, getResources().getString(R.string.homenet_client_string));
        housePostCall.enqueue(new Callback<SingleResponse<HousePostViewModel>>() {
            @Override
            public void onResponse(Call<SingleResponse<HousePostViewModel>> call, Response<SingleResponse<HousePostViewModel>> response) {
                if (dialog.isShowing()) {
                    dialog.cancel();
                }
                if (response.isSuccessful()) {
                    if (response.body().getModel() != null) {
                        housePost = response.body().getModel();
                        toolbarTextView.setText(housePost.getName() + " "+ housePost.getSurname() + "'s Post");
                        loadData();

                    }
                } else {
                    displayMessage("No Data", "No post data was found. You will be taken to the main feed", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent thisOne = new Intent(getActivity(), HomeNetFeedActivity.class);
                            startActivity(thisOne);
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
            }
        });
    }

    private void initializeComponents(View currentView) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        toolbarTextView = (TextView) getActivity().findViewById(R.id.NotificationFlaggedPostToolbarTextView);
        toolbarTextView.setText("House Post");
        nameSurnameTextView = (TextView) currentView.findViewById(R.id.NotificationFlaggedPostItemNameSurnameTextView);
        descriptionTextView = (TextView) currentView.findViewById(R.id.NotificationFlaggedPostDescriptionTextView);
        totalCommentsTextView = (TextView) currentView.findViewById(R.id.NotificationFlaggedPostItemTotalCommentsTextView);
        totalDislikesTextView = (TextView) currentView.findViewById(R.id.NotificationFlaggedPostItemDislikesTextView);
        totalLikesTextView = (TextView) currentView.findViewById(R.id.NotificationFlaggedPostItemTotalLikesTextView);
        profileImageView = (ImageView) currentView.findViewById(R.id.NotificationFlaggedPostItemProfileImageView);
        deleteButton = (FloatingActionButton) currentView.findViewById(R.id.NotificationFlaggedPostDeletePostButton);
        deleteButton.setOnClickListener(this);
        unflagButton = (FloatingActionButton) currentView.findViewById(R.id.NotificationFlaggedPostRemovePostButton);
        unflagButton.setOnClickListener(this);
    }
    private void loadData() {
        nameSurnameTextView.setText(housePost.getName() + " "+housePost.getSurname());
        TextDrawable drawable = TextDrawable.builder().buildRect(housePost.getName().substring(0,1).toUpperCase() + housePost.getSurname().substring(0,1).toUpperCase(), Color.BLUE);
        profileImageView.setImageDrawable(drawable);
        descriptionTextView.setText(housePost.getPostText());
        getHousePostMetaData();
    }
    private void getHousePostMetaData() {
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Fetching post meta data. Please wait...");
        dialog.setCancelable(false);
        dialog.show();
        final Call<SingleResponse<HousePostMetaDataViewModel>> metaDataCall = service.getHousePostMetaData("Bearer "+sharedPreferences.getString("authorization_token", ""), housePostID, getResources().getString(R.string.homenet_client_string));
        metaDataCall.enqueue(new Callback<SingleResponse<HousePostMetaDataViewModel>>() {
            @Override
            public void onResponse(Call<SingleResponse<HousePostMetaDataViewModel>> call, Response<SingleResponse<HousePostMetaDataViewModel>> response) {
                if (dialog.isShowing()) {
                    dialog.cancel();
                }
                if (response.isSuccessful()) {
                    if (response.body().getModel() != null) {
                        metaDataViewModel = response.body().getModel();
                        totalCommentsTextView.setText(metaDataViewModel.getTotalLikes() + " Comments");
                        totalDislikesTextView.setText(Integer.toString(metaDataViewModel.getTotalDislikes()));
                        totalLikesTextView.setText(Integer.toString(metaDataViewModel.getTotalLikes()));
                    }
                } else {
                    Snackbar.make(getView(), "No meta data", Snackbar.LENGTH_LONG).show();
                    totalLikesTextView.setText("0");
                    totalDislikesTextView.setText("0");
                    totalCommentsTextView.setText("0 Comments");
                }
            }
            @Override
            public void onFailure(Call<SingleResponse<HousePostMetaDataViewModel>> call, Throwable t) {
                if (dialog.isShowing()) {
                    dialog.cancel();
                }
                displayMessage("Critical Error", "Critical Error getting house post meta data. You will be taken to your news feed", new DialogInterface.OnClickListener() {
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
        switch (view.getId()) {

            case R.id.NotificationFlaggedPostDeletePostButton:

                break;
            case R.id.NotificationFlaggedPostRemovePostButton:

                break;
        }
    }
}
