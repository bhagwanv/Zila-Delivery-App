package com.sk.ziladelivery.ui.views.fragment;

import android.app.Activity;
import android.content.Context;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.core.view.GravityCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sk.ziladelivery.R;
import com.sk.ziladelivery.databinding.SearchFragBinding;
import com.sk.ziladelivery.ui.views.main.MainActivity;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {
    private Context context;
    SearchFragBinding mBinding;
    private boolean isViewLoaded;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();
    MainActivity activity;
    boolean Tasklist;
    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        activity = (MainActivity)context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mBinding == null) {
            // Inflate the layout for this fragment
            mBinding = DataBindingUtil.inflate(inflater, R.layout.search_frag, container, false);
        }
        return mBinding.getRoot();
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(!isViewLoaded)
        {  setTabLayout();

            isViewLoaded = true;
        }
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            Tasklist = bundle.getBoolean("Task");
        }
        setActionBarConfiguration();

    }
    private void setTabLayout() {

        tabLayout = mBinding.searchToolbar.tabs;
        tabLayout.canScrollHorizontally(0);
        tabLayout.addTab(tabLayout.newTab().setText("OrderID"));
        tabLayout.addTab(tabLayout.newTab().setText("AssignmentID"));

        activity.getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, new OrderIDSearchFragment()).commit();
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                switch (position) {
                    case 0:
                        activity. getSupportFragmentManager().beginTransaction()
                                .replace(R.id.framelayout, new OrderIDSearchFragment()).commit();
                        break;
                    case 1:
                        Bundle bundle = new Bundle();
                        bundle.putBoolean("Task", Tasklist);
                        Fragment fragment = new AssignmentIDSearchFragment();
                        fragment.setArguments(bundle);
                        /*activity.switchContentWithStack(fragment);*/
                        activity. getSupportFragmentManager().beginTransaction()
                                .replace(R.id.framelayout, fragment).commit();
                        break;

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }

    /*private void setupViewPager(ViewPager viewPager)
    {
        AssignmentTabAdapter adapter = new AssignmentTabAdapter(getChildFragmentManager(), mFragmentList, mFragmentTitleList);
        addFragment(new AssignmentIDSearchFragment(), "AssignmentID");
        addFragment(new OrderIDSearchFragment(), "OrderID");
        viewPager.setAdapter(adapter);
    }
    public void addFragment(Fragment fragment, String title)
    {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }*/
    private void setActionBarConfiguration()
    {
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        //actionBar.setLogo(R.drawable.ic_action_bar_logo);
        final DrawerLayout drawer = getActivity().findViewById(R.id.container);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setVisibility(View.VISIBLE);
        TextView tittleTextView =  getActivity().findViewById(R.id.toolbar_title);
        TextView starttimer =  getActivity().findViewById(R.id.start_timer);
        TextView  tv_assignmentid = getActivity().findViewById(R.id.assignmentid);
        tv_assignmentid.setVisibility(View.GONE);
        starttimer.setVisibility(View.GONE);
        tittleTextView.setVisibility(View.VISIBLE);
        tittleTextView.setText(getActivity().getString(R.string.my_assignment));
        tittleTextView.setText(R.string.Search);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }
            }
        });
        ((MainActivity) getActivity()).toggle.syncState();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Utils.keyboard(activity);
       // Utils.keyboardHide(activity);
    }
}
