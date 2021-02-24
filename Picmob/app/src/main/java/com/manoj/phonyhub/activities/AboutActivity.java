package com.manoj.phonyhub.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.manoj.phonyhub.HelperMethod;
import com.manoj.phonyhub.R;

import de.hdodenhof.circleimageview.CircleImageView;


public class AboutActivity extends AppCompatActivity implements View.OnClickListener {

    // Defining variables
    Toolbar toolbar;
    TextView contact_us_text;
    ImageView contact_us_image;
    CircleImageView profile_pic;
    LinearLayout about_follow_us, about_explore_more, about_version, about_changelog, about_license;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        toolbar = findViewById(R.id.aboutToolbar);
        toolbar.setTitle("Information");
        toolbar.setNavigationIcon(R.drawable.ic_back_arrow_round_24);
        toolbar.setNavigationContentDescription("NavigationBack");
        toolbar.setNavigationOnClickListener(this);
        setSupportActionBar(toolbar);

        // Initalizing variables
        contact_us_image = findViewById(R.id.aboutImageView);
        profile_pic = findViewById(R.id.aboutProfilePic);
        contact_us_text = findViewById(R.id.aboutContactUs);
        about_follow_us = findViewById(R.id.about_follow_us);
        about_explore_more = findViewById(R.id.about_explore_more);
        about_version = findViewById(R.id.about_version);
        about_changelog = findViewById(R.id.about_changelog);
        about_license = findViewById(R.id.about_license);

        // setting on click listener
        contact_us_text.setOnClickListener(this);
        about_follow_us.setOnClickListener(this);
        about_explore_more.setOnClickListener(this);
        about_version.setOnClickListener(this);
        about_changelog.setOnClickListener(this);
        about_license.setOnClickListener(this);

        String profile_pic_url = "https://github.com/manoj_verma_mv/profilePic.jpg";
        String thumbnail_image_url = "https://github.com/dikshakumari5643/Pybelt/raw/master/fashion1.jpg";
        String original_image_url = "https://github.com/dikshakumari5643/Pybelt/raw/master/fashion1_ori.jpg";
        String image_url = "https://source.unsplash.com/480x220/?contact,technology";

        Glide.with(this)
                .load(image_url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(contact_us_image);

//        Glide.with(this)
//                .asBitmap()
//                .load(thumbnail_image_url)
//                .into(new CustomTarget<Bitmap>() {
//                    @Override
//                    public void onResourceReady(@NonNull Bitmap bitmap, @Nullable Transition<? super Bitmap> transition) {
//                        //Drawable drawable = new BitmapDrawable(context.getResources(), HelperMethod.fastblur(bitmap, 2.3f, 46));
//                        contact_us_image.setImageBitmap(HelperMethod.fastblur(bitmap, 2.3f, 54));
//                        Glide.with(AboutActivity.this)
//                                .load(original_image_url)
//                                .into(contact_us_image);
//                    }
//
//                    @Override
//                    public void onLoadCleared(@Nullable Drawable placeholder) {
//                    }
//                });

    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (v.getContentDescription() == "NavigationBack") {
            AboutActivity.super.onBackPressed();
        } else if (id == R.id.aboutContactUs) {
            HelperMethod.toast("ContactUS");
        } else if (id == R.id.about_follow_us) {
            Intent follow_intent = new Intent(Intent.ACTION_VIEW);
            follow_intent.setData(Uri.parse("https://www.instagram.com/manoj_verma_mv"));
            startActivity(follow_intent);
        } else if (id == R.id.about_explore_more) {
            HelperMethod.toast("ExploreMore");
        } else if (id == R.id.about_version) {
            HelperMethod.toast("Version");
        } else if (id == R.id.about_changelog) {
            HelperMethod.toast("Changelog");
        } else if (id == R.id.about_license) {
            HelperMethod.toast("License");
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }


}
