package com.sk.ziladelivery.ui.views.viewmodels;

import android.app.Activity;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.sk.ziladelivery.R;
import com.squareup.picasso.Picasso;

public class MainModel {
    String name;
    String number;
    private String profileImage;
    private static Activity activity;

    public MainModel(Activity activity) {
        MainModel.activity = activity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getImageUrl() {
        // The URL will usually come from a model (i.e Profile)
        return profileImage;
    }

    @BindingAdapter({"imageUrl"})
    public static void loadImage(ImageView view, String imageUrl) {
       /* Glide.with(activity)
                .load(imageUrl)
                .placeholder(R.drawable.profile_round)
                .into(view);*/
        Picasso.get().load(imageUrl) .placeholder(R.drawable.profile_round).into(view);


    }
}
