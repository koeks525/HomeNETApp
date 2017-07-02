package Adapters;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by Okuhle on 2017/06/14.
 */

public class HouseManagerPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> fragmentList = new ArrayList<>();
    private ArrayList<String> fragmentTitle = new ArrayList<>();
    public HouseManagerPagerAdapter(FragmentManager fm) {
        super(fm);
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

    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentTitle.get(position);
    }

    public ArrayList<String> getFragmentTitle() {
        return fragmentTitle;
    }

    public void setFragmentTitle(ArrayList<String> fragmentTitle) {
        this.fragmentTitle = fragmentTitle;
    }
}
