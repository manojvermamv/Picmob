package com.manoj.phonyhub.activities;

import android.animation.LayoutTransition;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.ImageViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.manoj.phonyhub.HelperMethod;
import com.manoj.phonyhub.MyApp;
import com.manoj.phonyhub.R;
import com.manoj.phonyhub.adapter.SearchRecyclerAdapter;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity implements SearchRecyclerAdapter.ItemClickListener {

    Toolbar toolbar;
    Context context;
    String searchString;
    ProgressBar progressBar;
    ArrayList<String> mainSearchList = new ArrayList<>();
    ImageView notFoundImageView, search_back_arrow;
    SearchView search_edit_text;
    RecyclerView recyclerView;
    SearchRecyclerAdapter recyclerAdapter;
    Context context_global = MyApp.getContext();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        toolbar = findViewById(R.id.search_toolbar);
//        toolbar.setNavigationIcon(R.drawable.ic_back_arrow_filled_24);
//        toolbar.setNavigationContentDescription("Back Button");
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                SearchActivity.super.onBackPressed();
//            }
//        });
        setSupportActionBar(toolbar);

        //search_back_arrow = findViewById(R.id.search_back_arrow);;
        //search_back_arrow.setOnClickListener(this);
        //search_edit_text = findViewById(R.id.search_edit_text);
        //notFoundImageView = findViewById(R.id.search_not_found_imageView);
        progressBar = findViewById(R.id.search_progressBar);
        recyclerView = findViewById(R.id.search_recycle_view);

        searchString = getIntent().getExtras().getString("Search", "");
        String url = "https://source.unsplash.com/1024x580/?" + searchString;
        mainSearchList.add(url);
        int i;
        for (i = 1; i <= 36; i++) {
            mainSearchList.add(url);
        }

        // setup the recycle view
        recyclerView.setLayoutManager(new LinearLayoutManager(SearchActivity.this, LinearLayoutManager.VERTICAL, false));
        recyclerAdapter = new SearchRecyclerAdapter(SearchActivity.this, mainSearchList);
        recyclerAdapter.setClickListener(SearchActivity.this);
        recyclerView.setAdapter(recyclerAdapter);

//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        }, 5000);

//        Glide.with(SearchActivity.this).load(R.raw.not_found_animated1).into(notFoundImageView);


    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onItemClick(View view, int position) {
        HelperMethod.toastLong("Clicked " + recyclerAdapter.getItem(position) + " on row number " + position);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.home_menu, menu);
        MenuItem searchMenuItem = menu.findItem(R.id.home_menu_search);
        SearchView searchView = (SearchView) searchMenuItem.getActionView();
        searchView.setIconifiedByDefault(false);
        searchView.setQueryHint("Explore more");
        searchView.setQuery(searchString, true);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String url = "https://source.unsplash.com/1024x580/?" + query;
                mainSearchList.clear();
                for (int i = 1; i <= 36; i++) {
                    mainSearchList.add(url);
                }
                recyclerAdapter.notifyDataSetChanged();
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        ImageView searchClose = searchView.findViewById(androidx.appcompat.R.id.search_mag_icon);
        searchClose.setVisibility(View.GONE);
        ImageView searchBtn = searchView.findViewById(androidx.appcompat.R.id.search_button);
        searchBtn.setClickable(true);
        searchBtn.setFocusable(true);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HelperMethod.toast("ImageView searchBtn");
            }
        });
//        searchClose.setClickable(true);
//        searchClose.setFocusable(true);
//        searchClose.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                HelperMethod.toast("ImageView onClose");
//            }
//        });
//        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
//            @Override
//            public boolean onClose() {
//                HelperMethod.toast("Direct onClose");
//                return false;
//            }
//        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

//    public void showPopupMenu(View view) {
//        PopupMenu popup = new PopupMenu(this, view);
//        popup.getMenuInflater().inflate(R.menu.main_menu, popup.getMenu());
//        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//            public boolean onMenuItemClick(MenuItem item) {
//                switch (item.getItemId()) {
//                    case R.id.menuTest1:
//                        Intent test1Intent = new Intent(HomeActivity.this, Test1Activity.class);
//                        startActivity(test1Intent);
//                        break;
//                }
//                return true;
//            }
//        });
//        popup.show();
//    }

}