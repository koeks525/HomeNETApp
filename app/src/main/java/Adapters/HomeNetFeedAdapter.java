package Adapters;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.koeksworld.homenet.HomeNetFeedActivity;
import com.koeksworld.homenet.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import Communication.HomeNetService;
import HomeNETStream.FeedItemFragment;
import Models.HouseMember;
import Models.HousePost;
import Models.HousePostMetaData;
import Models.HousePostMetaDataViewModel;
import Models.HousePostViewModel;
import Models.User;
import ResponseModels.SingleResponse;
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
 * Created by Okuhle on 2017/07/01.
 */

public class HomeNetFeedAdapter extends RecyclerView.Adapter<HomeNetFeedAdapter.HomeNetFeedViewHolder> {

    private List<HousePostMetaDataViewModel> metaDataList;
    private List<HousePostViewModel> housePostList;
    private Activity currentActivity;

    public HomeNetFeedAdapter(List<HousePostMetaDataViewModel> metaDataList, List<HousePostViewModel> housePostList, Activity currentActivity) {
        this.metaDataList = metaDataList;
        this.currentActivity = currentActivity;
        this.housePostList = housePostList;
    }

    @Override
    public HomeNetFeedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View currentView = inflater.inflate(R.layout.news_feed_item, parent, false);
        return new HomeNetFeedViewHolder(currentView);
    }

    @Override
    public void onBindViewHolder(final HomeNetFeedViewHolder holder, int position) {
        HousePostViewModel selectedPost = housePostList.get(position);
        HousePostMetaDataViewModel metaData = metaDataList.get(position);
        /*HousePostMetaDataViewModel metaData = null;
        for (HousePostMetaDataViewModel data : metaDataList ){
            if (data.getHousePostID() == selectedPost.getHousePostID()) {
                metaData = data;
            }
        }*/

        holder.newsFeedTextTextView.setText(selectedPost.getPostText());
        holder.totalLikesTextView.setText(Integer.toString(metaData.getTotalLikes()));
        holder.totalDislikesTextView.setText(Integer.toString(metaData.getTotalDislikes()));
        holder.usernameTextView.setText(selectedPost.getName() +" "+selectedPost.getSurname());
        TextDrawable drawable = TextDrawable.builder().buildRect(selectedPost.getName().substring(0,1).toUpperCase() +selectedPost.getSurname().substring(0,1).toUpperCase(), Color.BLUE);
        holder.newsFeedProfileImageView.setImageDrawable(drawable);
        holder.totalCommentsTextView.setText(Integer.toString(metaData.getTotalComments()) + " Comments");
        holder.optionsImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(currentActivity, holder.optionsImageView);
                popupMenu.inflate(R.menu.news_feed_post_options_menu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch(item.getItemId()) {
                            case R.id.FlagPostOption:
                                //Show a dialog with the post information


                                break;
                            case R.id.ViewProfileOption:
                                //Show the user's profile

                                break;
                            case R.id.ViewHouseProfileOption:
                                //Show the house profile


                                break;



                        }
                        return false;
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return housePostList.size();
    }

    class HomeNetFeedViewHolder extends RecyclerView.ViewHolder{

        CardView newsFeedCard;
        TextView usernameTextView;
        TextView totalCommentsTextView;
        TextView newsFeedTextTextView;
        TextView totalLikesTextView;
        TextView totalDislikesTextView;
        ImageView newsFeedProfileImageView;
        ImageView optionsImageView;
        ImageView totalLikesImageView, totalDislikesImageView;
        SharedPreferences sharedPreferences;

        public HomeNetFeedViewHolder(View itemView) {
            super(itemView);
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(currentActivity);
            newsFeedCard = (CardView) itemView.findViewById(R.id.HomeNetNewsFeedCardView);
            usernameTextView = (TextView) itemView.findViewById(R.id.FeedItemUsernameTextView);
            newsFeedTextTextView = (TextView) itemView.findViewById(R.id.FeedItemPostTextView);
            totalLikesTextView = (TextView) itemView.findViewById(R.id.NewsFeedTotalLikesTextView);
            totalDislikesTextView = (TextView) itemView.findViewById(R.id.NewsFeedTotalDislikesTextView);
            optionsImageView = (ImageView) itemView.findViewById(R.id.FeedItemOptionsMenu);
            totalLikesImageView = (ImageView) itemView.findViewById(R.id.LikeImageView);
            totalDislikesImageView = (ImageView) itemView.findViewById(R.id.DislikeImageView);
            totalCommentsTextView = (TextView) itemView.findViewById(R.id.NewsFeedCommentsTextView);
            newsFeedProfileImageView = (ImageView) itemView.findViewById(R.id.FeedItemProfilePicture);
            newsFeedCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int index = getAdapterPosition();
                    HousePostViewModel selectedPost = housePostList.get(index);
                    HousePostMetaDataViewModel metaData = metaDataList.get(index);
                    Bundle newBundle = new Bundle();
                    newBundle.putParcelable("SelectedPost", selectedPost);
                    newBundle.putParcelable("MetaData", metaData);
                    FeedItemFragment fragment = new FeedItemFragment();
                    fragment.setArguments(newBundle);
                    FragmentTransaction transaction = currentActivity.getFragmentManager().beginTransaction();
                    transaction.replace(R.id.HomeNetFeedContentView, fragment, null);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            });
            totalLikesImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int selectedIndex = getAdapterPosition();
                    final HousePostViewModel selectedPost = housePostList.get(selectedIndex);
                    final Snackbar snackbar = Snackbar.make(view.getRootView(), "Registering Like...", Snackbar.LENGTH_INDEFINITE);
                    snackbar.show();
                    List<Protocol> protocolList = new ArrayList<Protocol>();
                    protocolList.add(Protocol.HTTP_1_1);
                    OkHttpClient client = new OkHttpClient.Builder().connectTimeout(2, TimeUnit.MINUTES).readTimeout(2, TimeUnit.MINUTES).protocols(protocolList).build();
                    Retrofit retrofit = new Retrofit.Builder().baseUrl(currentActivity.getResources().getString(R.string.homenet_link)).client(client).addConverterFactory(GsonConverterFactory.create()).build();
                    final HomeNetService service = retrofit.create(HomeNetService.class);
                    RequestBody emailBody = RequestBody.create(MultipartBody.FORM, sharedPreferences.getString("emailAddress", ""));
                    Call<SingleResponse<HousePostMetaData>> responseCall = service.registerLike("Bearer "+sharedPreferences.getString("authorization_token", ""), selectedPost.getHousePostID(), sharedPreferences.getString("emailAddress", ""), currentActivity.getResources().getString(R.string.homenet_client_string));
                    responseCall.enqueue(new Callback<SingleResponse<HousePostMetaData>>() {
                        @Override
                        public void onResponse(Call<SingleResponse<HousePostMetaData>> call, Response<SingleResponse<HousePostMetaData>> response) {
                            if (snackbar.isShown()) {
                                snackbar.dismiss();
                            }
                            if (response.isSuccessful()) {
                                HousePostMetaData data = response.body().getModel();
                                Call<SingleResponse<HousePostMetaDataViewModel>> postDataCall = service.getHousePostMetaData("Bearer "+sharedPreferences.getString("authorization_token", ""), selectedPost.getHousePostID(), currentActivity.getResources().getString(R.string.homenet_client_string));
                                postDataCall.enqueue(new Callback<SingleResponse<HousePostMetaDataViewModel>>() {
                                    @Override
                                    public void onResponse(Call<SingleResponse<HousePostMetaDataViewModel>> call, Response<SingleResponse<HousePostMetaDataViewModel>> response) {
                                        if (snackbar.isShown()) {
                                            snackbar.dismiss();
                                        }
                                        if (response.isSuccessful()) {
                                            HousePostMetaDataViewModel model = response.body().getModel();
                                            if (model != null) {
                                                totalLikesTextView.setText(Integer.toString(model.getTotalLikes()));
                                                totalDislikesTextView.setText(Integer.toString(model.getTotalDislikes()));
                                                totalCommentsTextView.setText(model.getTotalComments() + " Comments");
                                            }
                                        } else {
                                            try {
                                                Log.i("Error Information", response.errorBody().string());
                                            } catch (Exception error) {

                                            }
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<SingleResponse<HousePostMetaDataViewModel>> call, Throwable t) {
                                        if (snackbar.isShown()) {
                                            snackbar.dismiss();
                                        }

                                    }
                                });

                            }
                        }

                        @Override
                        public void onFailure(Call<SingleResponse<HousePostMetaData>> call, Throwable t) {
                            if (snackbar.isShown()) {
                                snackbar.dismiss();
                            }
                        }
                    });
                }
            });
            totalDislikesImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int selectedIndex = getAdapterPosition();
                    final HousePostViewModel selectedPost = housePostList.get(selectedIndex);
                    final Snackbar snackbar = Snackbar.make(view.getRootView(), "Registering Dislike...", Snackbar.LENGTH_INDEFINITE);
                    snackbar.show();
                    List<Protocol> protocolList = new ArrayList<Protocol>();
                    protocolList.add(Protocol.HTTP_1_1);
                    OkHttpClient client = new OkHttpClient.Builder().connectTimeout(2, TimeUnit.MINUTES).readTimeout(2, TimeUnit.MINUTES).protocols(protocolList).build();
                    Retrofit retrofit = new Retrofit.Builder().baseUrl(currentActivity.getResources().getString(R.string.homenet_link)).client(client).addConverterFactory(GsonConverterFactory.create()).build();
                    final HomeNetService service = retrofit.create(HomeNetService.class);
                    Call<SingleResponse<HousePostMetaData>> responseCall = service.registerDislike("Bearer "+sharedPreferences.getString("authorization_token", ""), selectedPost.getHousePostID(), sharedPreferences.getString("emailAddress", ""), currentActivity.getResources().getString(R.string.homenet_client_string));
                    responseCall.enqueue(new Callback<SingleResponse<HousePostMetaData>>() {
                        @Override
                        public void onResponse(Call<SingleResponse<HousePostMetaData>> call, Response<SingleResponse<HousePostMetaData>> response) {
                            if (snackbar.isShown()) {
                                snackbar.dismiss();
                            }
                            if (response.isSuccessful()) {
                                HousePostMetaData data = response.body().getModel();
                                Call<SingleResponse<HousePostMetaDataViewModel>> postDataCall = service.getHousePostMetaData("Bearer "+sharedPreferences.getString("authorization_token", ""), selectedPost.getHousePostID(), currentActivity.getResources().getString(R.string.homenet_client_string));
                                postDataCall.enqueue(new Callback<SingleResponse<HousePostMetaDataViewModel>>() {
                                    @Override
                                    public void onResponse(Call<SingleResponse<HousePostMetaDataViewModel>> call, Response<SingleResponse<HousePostMetaDataViewModel>> response) {
                                        if (snackbar.isShown()) {
                                            snackbar.dismiss();
                                        }
                                        if (response.isSuccessful()) {
                                            HousePostMetaDataViewModel model = response.body().getModel();
                                            if (model != null) {
                                                totalLikesTextView.setText(Integer.toString(model.getTotalLikes()));
                                                totalDislikesTextView.setText(Integer.toString(model.getTotalDislikes()));
                                                totalCommentsTextView.setText(model.getTotalComments() + " Comments");
                                            }
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<SingleResponse<HousePostMetaDataViewModel>> call, Throwable t) {
                                        if (snackbar.isShown()) {
                                            snackbar.dismiss();
                                        }
                                    }
                                });

                            }
                        }

                        @Override
                        public void onFailure(Call<SingleResponse<HousePostMetaData>> call, Throwable t) {
                            if (snackbar.isShown()) {
                                snackbar.dismiss();
                            }
                        }
                    });
                }

            });
        }


    }
}
