package Fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.koeksworld.homenet.R;

import java.util.ArrayList;

import Adapters.HouseManagerPagerAdapter;
import MangeHouseFragments.ActiveUsersFragment;
import MangeHouseFragments.BannedUsersFragment;
import MangeHouseFragments.EditHouseFragment;
import MangeHouseFragments.FlaggedPostsFragment;
import MangeHouseFragments.PendingUsersFragment;
import MangeHouseFragments.ResolvedPostsFragment;
import Models.House;

/**
 * A simple {@link Fragment} subclass.
 */
public class HouseManagerEndFragment extends Fragment {


    private ViewPager viewPager;
    private TabLayout tabLayout;
    private String mode = "";
    private ArrayList<String> fragmentTitle = new ArrayList<>();
    private ArrayList<Fragment> fragmentList = new ArrayList<>();
    private HouseManagerPagerAdapter adapter;
    private House selectedHouse;

    public HouseManagerEndFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View currentView = inflater.inflate(R.layout.fragment_house_manager_end, container, false);
        if (savedInstanceState != null) {
            selectedHouse = savedInstanceState.getParcelable("SelectedHouse");
        } else {
            selectedHouse = getArguments().getParcelable("SelectedHouse");
        }
        initializeComponents(currentView, savedInstanceState);

        return currentView;
    }

    private void initializeComponents(View currentView, Bundle savedInstanceState) {
        //This will obviously run when ever a tablet is detected
        viewPager = (ViewPager) currentView.findViewById(R.id.HouseManagerViewPager);
        tabLayout = (TabLayout) currentView.findViewById(R.id.HouseManagerTabLayout);

        adapter = new HouseManagerPagerAdapter(getChildFragmentManager());
        mode = getArguments().getString("mode");
        if (savedInstanceState != null) {
            if (savedInstanceState.getString("mode", "") != "") {
                mode = savedInstanceState.getString("mode", "");
            }
        }
        if (mode.equalsIgnoreCase("edit_house")) {
            Bundle bundle = new Bundle();
            bundle.putParcelable("SelectedHouse", selectedHouse);
            EditHouseFragment editHouseFragment = new EditHouseFragment();
            editHouseFragment.setArguments(bundle);
            AboutHouseFragment aboutHouseFragment = new AboutHouseFragment();
            aboutHouseFragment.setArguments(bundle);
            fragmentList.clear();
            fragmentTitle.clear();
            fragmentList.add(editHouseFragment);
            fragmentList.add(aboutHouseFragment);
            fragmentTitle.add("Details");
            fragmentTitle.add("About");
            adapter.setFragmentList(fragmentList);
            adapter.setFragmentTitle(fragmentTitle);
            viewPager.setAdapter(adapter);
            tabLayout.setupWithViewPager(viewPager);
        } else if (mode.equalsIgnoreCase("manage_users")) {
            Bundle bundle = new Bundle();
            bundle.putParcelable("SelectedHouse", selectedHouse);
            BannedUsersFragment bannedUsersFragment = new BannedUsersFragment();
            bannedUsersFragment.setArguments(bundle);
            PendingUsersFragment pendingUsersFragment = new PendingUsersFragment();
            pendingUsersFragment.setArguments(bundle);
            ActiveUsersFragment activeUsersFragment = new ActiveUsersFragment();
            activeUsersFragment.setArguments(bundle);
            fragmentTitle.clear();
            fragmentList.clear();
            fragmentTitle.add("Active");
            fragmentTitle.add("Pending");
            fragmentTitle.add("Banned");
            fragmentList.add(activeUsersFragment);
            fragmentList.add(pendingUsersFragment);
            fragmentList.add(bannedUsersFragment);
            adapter = new HouseManagerPagerAdapter(getChildFragmentManager());
            adapter.setFragmentTitle(fragmentTitle);
            adapter.setFragmentList(fragmentList);
            viewPager.setAdapter(adapter);
            tabLayout.setupWithViewPager(viewPager);
        } else if (mode.equalsIgnoreCase("manage_content")) {
            Bundle bundle = new Bundle();
            bundle.putParcelable("SelectedHouse", selectedHouse);
            FlaggedPostsFragment flaggedPostsFragment = new FlaggedPostsFragment();
            flaggedPostsFragment.setArguments(bundle);
            ResolvedPostsFragment resolvedPostsFragment = new ResolvedPostsFragment();
            resolvedPostsFragment.setArguments(bundle);
            fragmentList.clear();
            fragmentTitle.clear();
            fragmentTitle.add("Flagged");
            fragmentTitle.add("Resolved");
            fragmentList.add(flaggedPostsFragment);
            fragmentList.add(resolvedPostsFragment);
            adapter = new HouseManagerPagerAdapter(getChildFragmentManager());
            adapter.setFragmentList(fragmentList);
            adapter.setFragmentTitle(fragmentTitle);
            viewPager.setAdapter(adapter);
            tabLayout.setupWithViewPager(viewPager);
        } else if (mode.equalsIgnoreCase("messages")) {
            Bundle bundle = new Bundle();
            bundle.putParcelable("SelectedHouse", selectedHouse);
            adapter = new HouseManagerPagerAdapter(getChildFragmentManager());
            HouseAnnouncementsFragment announcementsFragment = new HouseAnnouncementsFragment();
            announcementsFragment.setArguments(bundle);
            HouseMessagesFragment houseMessagesFragment = new HouseMessagesFragment();
            houseMessagesFragment.setArguments(bundle);
            fragmentList.clear();
            fragmentTitle.clear();
            fragmentTitle.add("Announcements");
            fragmentTitle.add("Messages");
            fragmentList.add(announcementsFragment);
            fragmentList.add(houseMessagesFragment);
            adapter.setFragmentList(fragmentList);
            adapter.setFragmentTitle(fragmentTitle);
            viewPager.setAdapter(adapter);
            tabLayout.setupWithViewPager(viewPager);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("mode", mode);
    }


}
