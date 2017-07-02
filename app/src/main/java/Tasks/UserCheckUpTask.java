package Tasks;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.koeksworld.homenet.R;
import com.viewpagerindicator.LinePageIndicator;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import Adapters.GettingStartedPagerAdapter;
import Communication.HomeNetService;
import Data.DatabaseHelper;
import Fragments.CreateHouseIntroductionFragment;
import Fragments.CreateOrganizationIntroductionFragment;
import Fragments.FindHousesIntroductionFragment;
import Fragments.GettingStartedFinalFragment;
import Models.House;
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
    private List<House> houseList = new ArrayList<>();
    private List<Organization> organizationList = new ArrayList();
    private List<HousePost> housePostList = new ArrayList<>();
    private User currentUser;
    private DatabaseHelper dbHelper;
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

    public UserCheckUpTask(Activity currentActivity) {
        this.currentActivity = currentActivity;
        protocolList.add(Protocol.HTTP_1_1);
        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(1, TimeUnit.MINUTES).readTimeout(1, TimeUnit.MINUTES).protocols(protocolList).build();
        retrofit = new Retrofit.Builder().baseUrl(currentActivity.getResources().getString(R.string.homenet_link)).client(client).addConverterFactory(GsonConverterFactory.create()).build();
        service = retrofit.create(HomeNetService.class);
        dbHelper = new DatabaseHelper(currentActivity);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(currentActivity);
        errorString = "";
        deviceUtils = new DeviceUtils(currentActivity);
        currentView = currentActivity.findViewById(android.R.id.content);
        fragmentManager = currentActivity.getFragmentManager();
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
            Response<ListResponse<House>> houseResponse = service.getUserHouses("Bearer " + sharedPreferences.getString("authorization_token", ""), sharedPreferences.getString("emailAddress", ""), currentActivity.getResources().getString(R.string.homenet_client_string)).execute();

            switch (houseResponse.code()) {
                case 200:
                    if (houseResponse.body().getModel() != null) {
                        for (House thisHouse : houseResponse.body().getModel()) {
                            houseList.add(thisHouse);
                        }
                    }
                break;
                case 404:
                    JSONObject errorObject = new JSONObject(houseResponse.errorBody().string());
                    errorString = errorString + "\n"+errorObject.getString("message");
                    break;
                case 400:
                    JSONObject errorObjectTwo = new JSONObject(houseResponse.errorBody().string());
                    errorString = errorString + "\n"+errorObjectTwo.getString("message");
                    break;
                case 401:
                    JSONObject errorObjectThree = new JSONObject(houseResponse.errorBody().string());
                    errorString = errorString + "\n"+errorObjectThree.getString("message");
                    break;
            }
            Response<ListResponse<HousePost>> postCall = service.getUserPosts("Bearer " + sharedPreferences.getString("authorization_token", ""), sharedPreferences.getInt("userID", 0), currentActivity.getResources().getString(R.string.homenet_client_string)).execute();
            switch (postCall.code()) {
                case 200:
                    if (postCall.body().getModel() != null) {
                        for (HousePost post : postCall.body().getModel()) {
                            housePostList.add(post);
                        }
                    }
                    break;
                case 404:
                    JSONObject errorObject = new JSONObject(postCall.errorBody().string());
                    errorString = errorString + "\n"+errorObject.getString("message");
                    break;
                case 400:
                    JSONObject errorObjectTwo = new JSONObject(postCall.errorBody().string());
                    errorString = errorString + "\n"+errorObjectTwo.getString("message");
                    break;
                case 401:
                JSONObject errorObjectThree = new JSONObject(postCall.errorBody().string());
                errorString = errorString + "\n"+errorObjectThree.getString("message");
                break;

            }
            Response<ListResponse<Organization>> orgCall = service.getUserOrganizations("Bearer " + sharedPreferences.getString("authorization_token", ""), sharedPreferences.getString("emailAddress", ""), currentActivity.getResources().getString(R.string.homenet_client_string)).execute();
            switch (orgCall.code()) {
                case 200:
                    if (orgCall.body().getModel() != null) {
                        for (Organization thisOrganization : orgCall.body().getModel()) {
                            organizationList.add(thisOrganization);
                        }
                    }
                    break;
                case 404:
                    JSONObject errorObject = new JSONObject(orgCall.errorBody().string());
                    errorString = errorString + "\n"+errorObject.getString("message");
                    break;
                case 400:
                    JSONObject errorObjectTwo = new JSONObject(orgCall.errorBody().string());
                    errorString = errorString + "\n"+errorObjectTwo.getString("message");
                    break;
                case 401:
                    JSONObject errorObjectThree = new JSONObject(orgCall.errorBody().string());
                    errorString = errorString + "\n"+errorObjectThree.getString("message");
                    break;
            }
            return 1;
        } catch (Exception error) {

            displayMessage("Error Getting User Initial Data", error.getMessage(), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    System.exit(0); //Close the mobile app
                }
            });
            return -1;

        }
    }

    @Override
    protected void onPostExecute(Integer integer) {
        if (bar.isShown()) {
            bar.dismiss();
        }
        //No need to check if it is a tablet or phone
        if (integer == 1) {
            initializeComponents();
        }
        //No need for else - caught errors will close the program
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
