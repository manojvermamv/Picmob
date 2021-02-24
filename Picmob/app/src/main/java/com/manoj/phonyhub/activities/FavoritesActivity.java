package com.manoj.phonyhub.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.manoj.phonyhub.R;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;

public class FavoritesActivity extends AppCompatActivity {

    ArrayList<String> savedImages;

    public static ArrayList<String> getSavedImagesArrayList(Context context, String key) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        Gson gson = new Gson();
        String json = prefs.getString(key, null);
        Type type = new TypeToken<ArrayList<String>>() {
        }.getType();
        return gson.fromJson(json, type);
    }

    public static void saveImagesArrayList(Context context, ArrayList<String> list, String key) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(key, json);
        editor.apply();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        Toolbar toolbar = findViewById(R.id.favorites_toolbar);
        toolbar.setTitle("Favorites");
        toolbar.setNavigationIcon(R.drawable.ic_back_arrow_round_24);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        TextView textView = findViewById(R.id.favorites_temp_textview);
        savedImages = getSavedImagesArrayList(this, "saved_image");
        if (savedImages != null) {
            String str = Arrays.toString(savedImages.toArray());
            textView.setText(str);
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}