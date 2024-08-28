package com.sk.ziladelivery.ui.views.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import java.util.List;

public class AssignmentTabAdapter extends FragmentPagerAdapter {

    private final List<Fragment> mFragmentList;
    private final List<String> mFragmentTitleList;

    public AssignmentTabAdapter(FragmentManager fm, List<Fragment> mFragmentList, List<String> mFragmentTitleList) {
        super(fm);
        this.mFragmentList = mFragmentList;
        this.mFragmentTitleList = mFragmentTitleList;
    }

    @Override
    public Fragment getItem(int i) {
        return mFragmentList.get(i);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }
}
