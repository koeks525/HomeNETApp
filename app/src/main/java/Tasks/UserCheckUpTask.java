package Tasks;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.firebase.iid.FirebaseInstanceId;
import com.koeksworld.homenet.ApplicationSingleton;
import com.koeksworld.homenet.HomeManagerActivity;
import com.koeksworld.homenet.HomeNetFeedActivity;
import com.koeksworld.homenet.R;
import com.koeksworld.homenet.TokenSingleton;
import com.viewpagerindicator.LinePageIndicator;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.SocketFactory;

import Adapters.GettingStartedPagerAdapter;
import Communication.HomeNetService;
import Data.RealmHelper;
import Fragments.CreateHouseIntroductionFragment;
import Fragments.CreateOrganizationIntroductionFragment;
import Fragments.FindHousesIntroductionFragment;
import Fragments.GettingStartedFinalFragment;
import Models.House;
import Models.HouseMember;
import Models.HousePost;
import Models.Organization;
import Models.User;
import ResponseModels.ListResponse;
import ResponseModels.SingleResponse;
import Utilities.DeviceUtils;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Okuhle on 2017/05/21.
 */

public class UserCheckUpTask extends AsyncTask<Integer, Integer, Integer> {

    private Activity currentActivity;
    private Retrofit retrofit;
    private HomeNetService service;
    private ArrayList<House> houseList = new ArrayList<>();
    //private ArrayList<Organization> organizationList = new ArrayList();
   // private ArrayList<HousePost> housePostList = new ArrayList<>();
    private ArrayList<HouseMember> userMembershipList = new ArrayList<>();

    private User currentUser;
    private RealmHelper dbHelper;
    private SharedPreferences sharedPreferences;
    private String errorString;
    private DeviceUtils deviceUtils;
    private Snackbar bar;
    private View currentView;
    private FragmentManager fragmentManager;

    //Everything relating to the welcome activity
    private ViewPager viewPager;
    private CreateHouseIntroductionFragment createHouseFragment;
    private CreateOrganizationIntroductionFragment createOrganizationFragment;
    private FindHousesIntroductionFragment findHousesFragment;
    private GettingStartedPagerAdapter startedAdapter;
    private GettingStartedFinalFragment finalFragment;
    private LinePageIndicator indicator;
    private ArrayList<Fragment> fragmentList;
    private List<Protocol> protocolList = new ArrayList<>();
    private SharedPreferences.Editor editor;
    private RealmHelper helper;
    String token;

    public UserCheckUpTask(Activity currentActivity) {
        this.currentActivity = currentActivity;
        protocolList.add(Protocol.HTTP_1_1);
        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(2, TimeUnit.MINUTES).readTimeout(2, TimeUnit.MINUTES).protocols(protocolList).socketFactory(SocketFactory.getDefault()).build();
        retrofit = new Retrofit.Builder().baseUrl(currentActivity.getResources().getString(R.string.homenet_link)).client(client).addConverterFactory(GsonConverterFactory.create()).build();
        service = retrofit.create(HomeNetService.class);
        dbHelper = new RealmHelper();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(currentActivity);
        editor = sharedPreferences.edit();
        errorString = "";
        deviceUtils = new DeviceUtils(currentActivity);
        currentView = currentActivity.findViewById(android.R.id.content);
        fragmentManager = currentActivity.getFragmentManager();
        helper = new RealmHelper();
        token = FirebaseInstanceId.getInstance().getToken();
    }

    @Override
    protected void onPreExecute() {
        bar = Snackbar.make(currentView, "Please wait while loading... ", Snackbar.LENGTH_INDEFINITE);
        ViewGroup viewGroup = (ViewGroup) bar.getView().findViewById(android.support.design.R.id.snackbar_text).getParent();
        ProgressBar progressBar = new ProgressBar(currentActivity);
        viewGroup.addView(progressBar);
        bar.show();
    }

    @Override
    protected Integer doInBackground(Integer... integers) {
        try {
            //Save the firebase token for the selected user
            Response<SingleResponse<User>> userCall = service.getUser("Bearer "+sharedPreferences.getString("authorization_token", ""), sharedPreferences.getString("emailAddress", ""), currentActivity.getResources().getString(R.string.homenet_client_string)).execute();
            if (userCall.isSuccessful()) {
                User foundUser = userCall.body().getModel();
                foundUser.setFirebaseMessagingToken(token);
                Response<SingleResponse<User>> updateCall = service.updateUser("Bearer "+sharedPreferences.getString("authorization_token", ""), foundUser, currentActivity.getResources().getString(R.string.homenet_client_string)).execute();
                if (updateCall.isSuccessful()) {
                    editor.putString("firebase_token", updateCall.body().getModel().getFirebaseMessagingToken());
                    editor.commit();
                } else {
                    errorString += errorString + "\n"+updateCall.errorBody().string();
                }
                updateCall = null;
            }
            userCall = null;
            //Check the users memberships
            Response<ListResponse<HouseMember>> membershipCall = service.getUserMemberships("Bearer "+sharedPreferences.getString("authorization_token", ""), sharedPreferences.getString("emailAddress", ""), currentActivity.getResources().getString(R.string.homenet_client_string)).execute();

            if (membershipCall.isSuccessful()) {
                if (membershipCall.body().getModel() != null) {
                    for (HouseMember member : membershipCall.body().getModel()) {
                        userMembershipList.add(member);
                    }
                }
            } else {
                errorString = errorString + membershipCall.errorBody().string();
            }
            membershipCall = null;
            //Check the users houses
            Response<ListResponse<House>> userHouseCall = service.getUserHouses("Bearer "+sharedPreferences.getString("authorization_token", ""), sharedPreferences.getString("emailAddress", ""), currentActivity.getResources().getString(R.string.homenet_client_string)).execute();
            if (userHouseCall.isSuccessful()) {
                if (userHouseCall.body().getModel() != null) {
                    for (House house : userHouseCall.body().getModel()) {
                        houseList.add(house);
                    }
                }
            } else {
                errorString += userHouseCall.errorBody().string();
            }
            userHouseCall = null;
            return 1;
            //Organizations are out of the question - this wont be added yet

        } catch (Exception error) {
            errorString += error.getMessage() + "\n";
            return -1;

        }
    }

    @Override
    protected void onPostExecute(Integer integer) {
        if (bar.isShown()) {
            bar.dismiss();
        }

        if (houseList.size() > 0 || userMembershipList.size() > 0) {
            if (userMembershipList.size() > 0) {
                Intent newIntent = new Intent(currentActivity, HomeNetFeedActivity.class);
                Bundle newBundle = new Bundle();
                newBundle.putParcelableArrayList("MembershipList", userMembershipList);
                newIntent.putExtra("MembershipBundle", newBundle);
                currentActivity.startActivity(newIntent);
                currentActivity.finish();
            } else if (houseList.size() > 0 ){
                Intent nextIntent = new Intent(currentActivity, HomeManagerActivity.class);
                Bundle nextBundle = new Bundle();
                nextBundle.putParcelableArrayList("HouseList", houseList);
                nextIntent.putExtra("HouseBundle", nextBundle);
                currentActivity.startActivity(nextIntent);
                currentActivity.finish();
            }
        } else {

            if (errorString != "") {
                displayMessage("Error Getting Setup Information", errorString, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        System.exit(0);
                    }
                });
            } else  {
                //Take them to the guide
                initializeComponents();
            }
        }

    }

    private void displayMessage(final String title, final String message, final DialogInterface.OnClickListener listener) {
        currentActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(currentActivity);
                builder.setTitle(title).setMessage(message).setCancelable(false).setPositiveButton("Got it", listener);
                builder.show();
            }
        });
    }
    //Source: http://viewpagerindicator.com/
    private void initializeComponents() {
        fragmentList = new ArrayList<>();
        viewPager = (ViewPager) currentActivity.findViewById(R.id.HomeNetWelcomeViewPager);
        indicator = (LinePageIndicator) currentActivity.findViewById(R.id.HomeNetWelcomeLineIndicator);
        indicator.setBackgroundColor(currentActivity.getResources().getColor(R.color.colorPrimary));
        indicator.setSelectedColor(currentActivity.getResources().getColor(R.color.colorAccent));
        indicator.setLineWidth(100);
        indicator.setStrokeWidth(10);
        createHouseFragment = new CreateHouseIntroductionFragment();
        createOrganizationFragment = new CreateOrganizationIntroductionFragment();
        findHousesFragment = new FindHousesIntroductionFragment();
        finalFragment = new GettingStartedFinalFragment();
        fragmentList.add(createHouseFragment);
        fragmentList.add(createOrganizationFragment);
        fragmentList.add(findHousesFragment);
        fragmentList.add(finalFragment);
        startedAdapter = new GettingStartedPagerAdapter(fragmentManager);
        startedAdapter.setFragmentList(fragmentList);
        viewPager.setAdapter(startedAdapter);
        indicator.setViewPager(viewPager);
    }
}
