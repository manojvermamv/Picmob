package com.manoj.phonyhub.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.manoj.phonyhub.R;

public class EndlessScrollingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_endless_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_endless);
        setSupportActionBar(toolbar);

        RecyclerView recyclerView = findViewById(R.id.recycler_view_endless);
        TextView textView = findViewById(R.id.text_view_endless);
        recyclerView.setVisibility(View.GONE);


    }


}