package com.sk.ziladelivery.ui.views.main;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.sk.ziladelivery.R;
import com.sk.ziladelivery.databinding.ActivityTransparentBinding;

public class TransparentActivity extends AppCompatActivity {
    ActivityTransparentBinding mBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_transparent);
    }
}
