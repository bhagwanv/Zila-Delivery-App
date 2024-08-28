package com.sk.ziladelivery.ui.views.fragment;


import android.content.Context;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sk.ziladelivery.R;
import com.sk.ziladelivery.databinding.FragmentDummyBinding;


/**
 * A simple {@link Fragment} subclass.
 */
public class DummyFragment extends Fragment {

    private Context context;
    private View convertView;
    private FragmentDummyBinding mBinding;

    public DummyFragment(){
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(convertView == null)
        {
            // Inflate the layout for this fragment
            mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_dummy, container, false);
        }
        return convertView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



    }

}
