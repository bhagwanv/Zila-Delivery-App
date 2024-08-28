package com.sk.ziladelivery.ui.views.fragment;
import android.content.Context;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sk.ziladelivery.R;
import com.sk.ziladelivery.databinding.FragmentProfileBinding;
import com.sk.ziladelivery.utilities.SharePrefs;
import com.sk.ziladelivery.ui.views.main.MainActivity;

public class ProfileFragment extends Fragment {
    private Context context;
    private View convertView;
    private FragmentProfileBinding mBinding;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
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
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false);
        setActionBarConfiguration();
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBinding.etMobile.setText(String.valueOf(SharePrefs.getInstance(context).getString(SharePrefs.MOBILE)));
       // mBinding.etEmmobile.setText(String.valueOf(SharePrefs.getInstance(context).getString(SharePrefs.MOBILE)));
        mBinding.etName.setText(String.valueOf(SharePrefs.getInstance(context).getString(SharePrefs.PEAOPLE_FIRST_NAME)));
        mBinding.etEmail.setText(String.valueOf(SharePrefs.getInstance(context).getString(SharePrefs.EMAILID)));

    }

    private void setActionBarConfiguration() {
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        //actionBar.setLogo(R.drawable.ic_action_bar_logo);
        final DrawerLayout drawer = (DrawerLayout) getActivity().findViewById(R.id.container);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setVisibility(View.VISIBLE);
        TextView startTimer = getActivity().findViewById(R.id.start_timer);
        TextView tittleTextView = (TextView) getActivity().findViewById(R.id.toolbar_title);
        TextView assignmentid = (TextView) getActivity().findViewById(R.id.assignmentid);
        assignmentid.setVisibility(View.GONE);
        startTimer.setVisibility(View.GONE);
        tittleTextView.setVisibility(View.VISIBLE);
        tittleTextView.setText(R.string.My_Profile);
        toolbar.setNavigationOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }
            }
        });
        ((MainActivity) getActivity()).toggle.syncState();
    }


}
