package com.sk.ziladelivery.ui.views.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.sk.ziladelivery.R;
import com.sk.ziladelivery.databinding.FragmentAssignmentBinding;
import com.sk.ziladelivery.ui.views.adapter.AssignmentTabAdapter;
import com.sk.ziladelivery.ui.views.main.MainActivity;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyAssignmentFragment extends Fragment {
    private MainActivity activity;
    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();
    private Context context;
    private FragmentAssignmentBinding mBinding;
    private boolean isViewLoaded;


    public MyAssignmentFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
        this.context = context;
        activity = (MainActivity) context;
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mBinding == null) {
            mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_assignment, container, false);
        }
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NotNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!isViewLoaded) {
            setTabLayout();
            isViewLoaded = true;
        }
        setActionBarConfiguration();
    }

    private void setTabLayout() {
        TabLayout tabLayout = mBinding.hometoolbar.tabs;
        ViewPager viewPager = mBinding.hometoolbar.viewpager;
        tabLayout.setupWithViewPager(viewPager);
        setupViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        AssignmentTabAdapter adapter = new AssignmentTabAdapter(getChildFragmentManager(), mFragmentList, mFragmentTitleList);
        addFragment(new PendingFragment(), context.getString(R.string.Pending));
        addFragment(new PartialFragment(), context.getString(R.string.Partial));
        viewPager.setAdapter(adapter);
    }

    private void addFragment(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }

    private void setActionBarConfiguration() {
        final DrawerLayout drawer = getActivity().findViewById(R.id.container);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setVisibility(View.VISIBLE);
        TextView tittleTextView = getActivity().findViewById(R.id.toolbar_title);
        TextView starttimer = getActivity().findViewById(R.id.start_timer);
        TextView tv_assignmentid = getActivity().findViewById(R.id.assignmentid);
        RelativeLayout rvTiltleLayout = getActivity().findViewById(R.id.title_layout);
        tv_assignmentid.setVisibility(View.GONE);
        starttimer.setVisibility(View.GONE);
        tittleTextView.setVisibility(View.VISIBLE);
        tittleTextView.setClickable(false);
        tittleTextView.setFocusable(false);
        tittleTextView.setBackgroundResource(0);
        TextView timer = getActivity().findViewById(R.id.tv_timer);
        timer.setVisibility(View.GONE);
        activity.startBreak.setVisibility(View.GONE);
        tittleTextView.setText(getActivity().getString(R.string.my_assignment));
        ImageView menu = getActivity().findViewById(R.id.drawer_menu);
        menu.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_menu_three_dot));
        toolbar.setNavigationOnClickListener(v -> {
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                drawer.openDrawer(GravityCompat.START);
            }
        });
        ((MainActivity) getActivity()).toggle.syncState();
    }
}