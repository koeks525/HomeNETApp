package Adapters;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by Okuhle on 2017/04/15.
 */

//Source: https://developer.android.com/reference/android/support/v13/app/FragmentPagerAdapter.html
public class GettingStartedPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> fragmentList;
    public GettingStartedPagerAdapter(FragmentManager fm) {
        super(fm);
        fragmentList = new ArrayList<>();
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    public ArrayList<Fragment> getFragmentList() {
        return fragmentList;
    }

    public void setFragmentList(ArrayList<Fragment> fragmentList) {
        this.fragmentList = fragmentList;
    }
}