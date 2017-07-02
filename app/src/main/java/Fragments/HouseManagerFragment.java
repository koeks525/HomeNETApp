package Fragments;


import android.app.FragmentTransaction;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BubbleChart;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.koeksworld.homenet.R;

import java.util.ArrayList;
import java.util.List;

import MangeHouseFragments.ActiveUsersFragment;
import MangeHouseFragments.PendingUsersFragment;
import Tasks.HouseManagerTask;
import Utilities.DeviceUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class HouseManagerFragment extends Fragment implements View.OnClickListener {

    private CardView manageFriendsCard, manageContentCard, editHouseCard, settingsCard, messagesCard, helpCard;
    private DeviceUtils deviceUtils;
    private MaterialSpinner housesSpinner;
    private HouseManagerTask houseManagerTask;
    int result;

    public HouseManagerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View currentView =  inflater.inflate(R.layout.fragment_house_manager, container, false);
        initializeComponents(currentView);
        if (savedInstanceState == null) {
            getData();
        } else {
            housesSpinner.setItems(savedInstanceState.getStringArrayList("items"));
        }
        return currentView;
    }

    private void getData() {
        houseManagerTask = new HouseManagerTask(getActivity(), housesSpinner);
        houseManagerTask.execute();
    }


    private void initializeComponents(View currentView) {
        deviceUtils = new DeviceUtils(getActivity());
        manageFriendsCard = (CardView) currentView.findViewById(R.id.ManageHomeUsersCardView);
        manageContentCard = (CardView) currentView.findViewById(R.id.ManageHomePostsCardView);
        editHouseCard = (CardView) currentView.findViewById(R.id.ManageHomeEditHouseCardView);
        settingsCard = (CardView) currentView.findViewById(R.id.ManageHomeHouseSettingsCardView);
        messagesCard = (CardView) currentView.findViewById(R.id.ManageHomeMessagesCardView);
        helpCard = (CardView) currentView.findViewById(R.id.ManageHomeHelpCardView);
        housesSpinner = (MaterialSpinner) currentView.findViewById(R.id.HouseManagerSpinner);
        manageFriendsCard.setOnClickListener(this);
        manageContentCard.setOnClickListener(this);
        editHouseCard.setOnClickListener(this);
        settingsCard.setOnClickListener(this);
        messagesCard.setOnClickListener(this);
        helpCard.setOnClickListener(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ArrayList<String> itemsList = new ArrayList<>();
        List<String> items = housesSpinner.getItems();
        if (items != null) {
            for (String item : items) {
                itemsList.add(item);
            }
        }
            outState.putStringArrayList("items", itemsList);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //We will not use the backstack, we will restore these when the screen is rotated.
            case R.id.ManageHomeEditHouseCardView:
                if (deviceUtils.isTablet()) {
                    if (deviceUtils.isLandscape()) {
                        FragmentTransaction manageUsersTransaction = getFragmentManager().beginTransaction();
                        Bundle bundle = new Bundle();
                        bundle.putString("mode","edit_house");
                        HouseManagerEndFragment endFragment = new HouseManagerEndFragment();
                        endFragment.setArguments(bundle);
                        manageUsersTransaction.replace(R.id.HomeManagerActivityContentViewTabletTwo, endFragment, null);
                        manageUsersTransaction.commit();
                    } else {
                        //Load this layout on a tablet on portrait, and add to the backstack
                        FragmentTransaction tabletLandTransaction = getFragmentManager().beginTransaction();
                        Bundle newBunde = new Bundle();
                        newBunde.putString("mode", "edit_house");
                        HouseManagerEndFragment endFragmentTwo = new HouseManagerEndFragment();
                        endFragmentTwo.setArguments(newBunde);
                        tabletLandTransaction.replace(R.id.HomeManagerActivityContentViewTabletLand, endFragmentTwo, null);
                        tabletLandTransaction.addToBackStack(null);
                        tabletLandTransaction.commit();
                    }
                } else {
                    //Load for a mobile phone - only one layout
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    HouseManagerEndFragment endFragmentThree = new HouseManagerEndFragment();
                    Bundle bundleThree = new Bundle();
                    bundleThree.putString("mode", "edit_house");
                    endFragmentThree.setArguments(bundleThree);
                    transaction.replace(R.id.HomeManagerActivityContentViewTabletLand, endFragmentThree, null);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }

                break;
            case R.id.ManageHomeUsersCardView:
                if (deviceUtils.isTablet()) {
                    if (deviceUtils.isLandscape()) {
                      //Landscape mode tablet
                        FragmentTransaction transactionThree = getFragmentManager().beginTransaction();
                        HouseManagerEndFragment endFragment = new HouseManagerEndFragment();
                        Bundle newBundle = new Bundle();
                        newBundle.putString("mode", "manage_users");
                        endFragment.setArguments(newBundle);
                        transactionThree.replace(R.id.HomeManagerActivityContentViewTabletTwo, endFragment);
                        transactionThree.commit();

                    } else {
                        //Portrait tablet mode
                        FragmentTransaction transactionFour = getFragmentManager().beginTransaction();
                        HouseManagerEndFragment endFragmentTwo = new HouseManagerEndFragment();
                        Bundle nextBundle = new Bundle();
                        nextBundle.putString("mode", "manage_users");
                        endFragmentTwo.setArguments(nextBundle);
                        transactionFour.replace(R.id.HomeManagerActivityContentViewTabletLand, endFragmentTwo);
                        transactionFour.addToBackStack(null);
                        transactionFour.commit();
                    }
                } else {
                    //Mobile phone
                    FragmentTransaction transactionFive = getFragmentManager().beginTransaction();
                    HouseManagerEndFragment endFragmentFive = new HouseManagerEndFragment();
                    Bundle nextBundle = new Bundle();
                    nextBundle.putString("mode", "manage_users");
                    endFragmentFive.setArguments(nextBundle);
                    transactionFive.replace(R.id.HomeManagerActivityContentViewTabletLand, endFragmentFive);
                    transactionFive.addToBackStack(null);
                    transactionFive.commit();
                }



                break;
            case R.id.ManageHomePostsCardView:
                if (deviceUtils.isTablet()) {
                    if (deviceUtils.isLandscape()) {
                        //Landscape mode tablet
                        FragmentTransaction transactionThree = getFragmentManager().beginTransaction();
                        HouseManagerEndFragment endFragment = new HouseManagerEndFragment();
                        Bundle newBundle = new Bundle();
                        newBundle.putString("mode", "manage_content");
                        endFragment.setArguments(newBundle);
                        transactionThree.replace(R.id.HomeManagerActivityContentViewTabletTwo, endFragment);
                        transactionThree.commit();

                    } else {
                        //Portrait tablet mode
                        FragmentTransaction transactionFour = getFragmentManager().beginTransaction();
                        HouseManagerEndFragment endFragmentTwo = new HouseManagerEndFragment();
                        Bundle nextBundle = new Bundle();
                        nextBundle.putString("mode", "manage_content");
                        endFragmentTwo.setArguments(nextBundle);
                        transactionFour.replace(R.id.HomeManagerActivityContentViewTabletLand, endFragmentTwo);
                        transactionFour.commit();
                    }
                } else {
                    //Mobile phone
                    FragmentTransaction transactionFive = getFragmentManager().beginTransaction();
                    HouseManagerEndFragment endFragmentFive = new HouseManagerEndFragment();
                    Bundle nextBundle = new Bundle();
                    nextBundle.putString("mode", "manage_content");
                    endFragmentFive.setArguments(nextBundle);
                    transactionFive.replace(R.id.HomeManagerActivityContentViewTabletLand, endFragmentFive);
                    transactionFive.addToBackStack(null);
                    transactionFive.commit();
                }
                break;
            case R.id.ManageHomeMessagesCardView:
                if (deviceUtils.isTablet()) {
                    if (deviceUtils.isLandscape()) {
                        //Landscape mode tablet
                        FragmentTransaction transactionThree = getFragmentManager().beginTransaction();
                        HouseManagerEndFragment endFragment = new HouseManagerEndFragment();
                        Bundle newBundle = new Bundle();
                        newBundle.putString("mode", "messages");
                        endFragment.setArguments(newBundle);
                        transactionThree.replace(R.id.HomeManagerActivityContentViewTabletTwo, endFragment);
                        transactionThree.commit();

                    } else {
                        //Portrait tablet mode
                        FragmentTransaction transactionFour = getFragmentManager().beginTransaction();
                        HouseManagerEndFragment endFragmentTwo = new HouseManagerEndFragment();
                        Bundle nextBundle = new Bundle();
                        nextBundle.putString("mode", "messages");
                        endFragmentTwo.setArguments(nextBundle);
                        transactionFour.replace(R.id.HomeManagerActivityContentViewTabletLand, endFragmentTwo);
                        transactionFour.commit();
                    }
                } else {
                    //Mobile phone
                    FragmentTransaction transactionFive = getFragmentManager().beginTransaction();
                    HouseManagerEndFragment endFragmentFive = new HouseManagerEndFragment();
                    Bundle nextBundle = new Bundle();
                    nextBundle.putString("mode", "messages");
                    endFragmentFive.setArguments(nextBundle);
                    transactionFive.replace(R.id.HomeManagerActivityContentViewTabletLand, endFragmentFive);
                    transactionFive.addToBackStack(null);
                    transactionFive.commit();
                }
                break;
        }
    }


}
