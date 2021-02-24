package com.manoj.phonyhub.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.manoj.phonyhub.R;
import com.manoj.phonyhub.adapter.TestRecyclerAdapter;
import com.manoj.phonyhub.data.PicsumDataModel;
import com.manoj.phonyhub.extra.EndlessRecyclerViewScrollListener;
import com.manoj.phonyhub.interfaces.TestApiInterface;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TestActivity extends AppCompatActivity {

    int page = 1, limit = 10;
    ProgressBar progressBar;
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    TestRecyclerAdapter adapter;
    List<PicsumDataModel> picsumDataList;
    EndlessRecyclerViewScrollListener scrollListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_test);
        setSupportActionBar(toolbar);

        //progressBar = findViewById(R.id.progress_bar_test);
        recyclerView = findViewById(R.id.recycler_view_test);

        // Initialize adapter
        //adapter = new TestRecyclerAdapter(this, picsumDataList);
        // Set Layout Manager
        layoutManager = new LinearLayoutManager(TestActivity.this);
        //recyclerView.setLayoutManager(layoutManager);
        //recyclerView.setAdapter(adapter);

        loadDataFromApi(page);

        EndlessRecyclerViewScrollListener scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                loadMoreDataFromApi(page, view);
            }
        };

        recyclerView.addOnScrollListener(scrollListener);

    }

    private void loadMoreDataFromApi(int page, RecyclerView view) {
        // Intialize retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://picsum.photos")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        // Create Api Interface
        TestApiInterface apiInterface = retrofit.create(TestApiInterface.class);
        // Intialize call
        Call<List<PicsumDataModel>> call = apiInterface.getPicsumList(page, limit);

        call.enqueue(new Callback<List<PicsumDataModel>>() {
            @Override
            public void onResponse(@NotNull Call<List<PicsumDataModel>> call, @NotNull Response<List<PicsumDataModel>> response) {
                //progressBar.setVisibility(View.GONE);
                List<PicsumDataModel> moreData = response.body();
                picsumDataList.addAll(moreData);
                int curSize = adapter.getItemCount();

                view.post(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyItemRangeInserted(curSize, picsumDataList.size() - 1);
                    }
                });

                //adapter = new TestRecyclerAdapter(TestActivity.this, picsumDataList);

                //LinearLayoutManager layoutManager = new LinearLayoutManager(TestActivity.this);
                //recyclerView.setLayoutManager(layoutManager);
                //recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(@NotNull Call<List<PicsumDataModel>> call, @NotNull Throwable t) {

            }
        });

    }

    private void loadDataFromApi(int page) {
        // Intialize retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://picsum.photos")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        // Create Api Interface
        TestApiInterface apiInterface = retrofit.create(TestApiInterface.class);
        // Intialize call
        Call<List<PicsumDataModel>> call = apiInterface.getPicsumList(page, limit);

        call.enqueue(new Callback<List<PicsumDataModel>>() {
            @Override
            public void onResponse(@NotNull Call<List<PicsumDataModel>> call, @NotNull Response<List<PicsumDataModel>> response) {
                //progressBar.setVisibility(View.GONE);
                picsumDataList = response.body();
                adapter = new TestRecyclerAdapter(TestActivity.this, picsumDataList);
                recyclerView.setLayoutManager(layoutManager);
                //adapter.notifyDataSetChanged();
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(@NotNull Call<List<PicsumDataModel>> call, @NotNull Throwable t) {

            }
        });

    }

}