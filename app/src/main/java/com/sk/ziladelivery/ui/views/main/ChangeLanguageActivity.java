package com.sk.ziladelivery.ui.views.main;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.sk.ziladelivery.R;
import com.sk.ziladelivery.databinding.ActivityChangeLanguageBinding;
import com.sk.ziladelivery.utilities.LocaleHelper;
import com.sk.ziladelivery.utilities.SharePrefs;


public class ChangeLanguageActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout backPressIV;
    private CheckBox hindiCB, englishCB;
    private String langu = "en";
    Resources resources;
    ActivityChangeLanguageBinding binding;
    Handler handler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_change_language);


        //init view
        initView();
        //checked hindi, english checkbox
        if (LocaleHelper.getLanguage(ChangeLanguageActivity.this).equalsIgnoreCase("en")) {
            englishCB.setChecked(true);
        } else if (LocaleHelper.getLanguage(ChangeLanguageActivity.this).equalsIgnoreCase("hi")) {
            hindiCB.setChecked(true);
        }
    }


    private void initView()
    {
        langu = SharePrefs.getInstance(ChangeLanguageActivity.this).getString(SharePrefs.LANGUAGE);
        hindiCB = binding.cbHindi;
        englishCB = binding.cbEnglish;
        binding.back.setOnClickListener(this);
        englishCB.setOnClickListener(this);
        hindiCB.setOnClickListener(this);
    }

    @Override
    protected void onResume()
    {
        super.onResume();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
            {
               onBackPressed();
                break;
            }
            case R.id.cb_english:
            {
                handler.postDelayed(() -> {
                    if (englishCB.isChecked()) {
                        hindiCB.setChecked(false);
                        LocaleHelper.setLocale(ChangeLanguageActivity.this, "en");
                        resources = ChangeLanguageActivity.this.getResources();
                        startActivity(new Intent(ChangeLanguageActivity.this, SplashScreenActivity.class));
                    } else {
                        englishCB.setChecked(true);
                    }
                }, 100);
                break;
            }

            case R.id.cb_hindi:
                {
                handler.postDelayed(() -> {
                    if (hindiCB.isChecked()) {
                        englishCB.setChecked(false);
                        LocaleHelper.setLocale(ChangeLanguageActivity.this, "hi");
                        resources = ChangeLanguageActivity.this.getResources();
                        startActivity(new Intent(ChangeLanguageActivity.this, SplashScreenActivity.class));
                    } else {
                        hindiCB.setChecked(true);
                    }
                }, 100);

                break;
            }
        }
    }

    @Override
    public void onBackPressed()
    {
        startActivity( new Intent(ChangeLanguageActivity.this, MainActivity.class));
        finish();

    }

    @Override
    protected void attachBaseContext(Context newBase)
    {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }



}

