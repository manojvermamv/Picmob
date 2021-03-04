package com.manoj.phonyhub.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.manoj.phonyhub.R;
import com.manoj.phonyhub.adapter.PicsumRecycleAdapter;
import com.manoj.phonyhub.data.PicsumDataModel;
import com.manoj.phonyhub.extra.EndlessRecyclerViewScrollListener;
import com.manoj.phonyhub.interfaces.PicsumApi;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WallpaperActivity extends AppCompatActivity implements View.OnClickListener {

    ProgressBar progressBar, bottom_progressBar;
    RelativeLayout progressBarLayout;
    RecyclerView picsumRecyclerView;
    int page = 1, limit = 24;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wallpaper_activity);
        Toolbar toolbar = findViewById(R.id.wallpaper_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Wallpapers");
        toolbar.setNavigationIcon(R.drawable.ic_back_arrow_round_24);
        toolbar.setNavigationContentDescription("WallpaperActivityBack");
        toolbar.setNavigationOnClickListener(this);

        progressBar = findViewById(R.id.wallpaper_progressBar);
        progressBarLayout = findViewById(R.id.wallpaper_progressBarLayout);
        bottom_progressBar = findViewById(R.id.wallpaper_bottom_progressBar);
        picsumRecyclerView = findViewById(R.id.wallpaper_recycleView);

        // Load first limited data from API
        progressBarLayout.setVisibility(View.VISIBLE);
        new loadPicsumData().start();

        // The long-running operation is run on a worker thread
        //new AsyncTask<Void, Integer, Long>() {
        //    @Override
        //    protected Long doInBackground(Void... voids) { return null; }
        //}.execute();

        //AsyncTask.execute(new Runnable() {
        //    @Override
        //    public void run() { }
        //});

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (v.getContentDescription() == "WallpaperActivityBack") {
            finish();
        }
    }

    private void networkErrorDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogTheme);
        builder.setTitle("Network Error");
        builder.setMessage("Check your internet connection and try again.");
        builder.setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.create().show();
    }

    private void loadMorePicsumData(List<PicsumDataModel> picsumDataList, PicsumRecycleAdapter picsumRecycleAdapter) {

        EndlessRecyclerViewScrollListener endlessScrollListener = new EndlessRecyclerViewScrollListener((GridLayoutManager) Objects.requireNonNull(picsumRecyclerView.getLayoutManager())) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView recyclerView) {
                // Load more data from API and adding to the recycler view
                PicsumApi.getClient().getPicsumList(page, limit).enqueue(new Callback<List<PicsumDataModel>>() {
                    @Override
                    public void onResponse(@NotNull Call<List<PicsumDataModel>> call, @NotNull Response<List<PicsumDataModel>> response) {
                        List<PicsumDataModel> moreData = response.body();
                        if (moreData != null) {
                            picsumDataList.addAll(moreData);
                            int curSize = picsumRecycleAdapter.getItemCount();
                            recyclerView.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    //picsumRecycleAdapter.notifyItemRangeInserted(curSize, picsumDataList.size());
                                    picsumRecycleAdapter.notifyItemRangeInserted(curSize, picsumDataList.size() - 1);
                                    //picsumRecycleAdapter.notifyItemRangeInserted(curSize, moreData.size());
                                }
                            }, 2000);
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<List<PicsumDataModel>> call, @NotNull Throwable t) {
                    }
                });
            }
        };

        // Attaching Scroll Listener with RecyclerView
        picsumRecyclerView.addOnScrollListener(endlessScrollListener);
    }

    class loadPicsumData extends Thread {
        @Override
        public void run() {
            GridLayoutManager gridLayoutManager = new GridLayoutManager(WallpaperActivity.this, 3, GridLayoutManager.VERTICAL, false);
            PicsumApi.getClient().getPicsumList(page, limit).enqueue(new Callback<List<PicsumDataModel>>() {
                @Override
                public void onResponse(@NotNull Call<List<PicsumDataModel>> call, @NotNull Response<List<PicsumDataModel>> response) {
                    List<PicsumDataModel> picsumDataList = response.body();
                    PicsumRecycleAdapter picsumRecycleAdapter = new PicsumRecycleAdapter(WallpaperActivity.this, picsumDataList);
                    picsumRecyclerView.setLayoutManager(gridLayoutManager);
                    picsumRecyclerView.setAdapter(picsumRecycleAdapter);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressBarLayout.setVisibility(View.GONE);
                        }
                    });
                    loadMorePicsumData(picsumDataList, picsumRecycleAdapter);
                }

                @Override
                public void onFailure(@NotNull Call<List<PicsumDataModel>> call, @NotNull Throwable t) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressBarLayout.setVisibility(View.GONE);
                            networkErrorDialog();
                        }
                    });
                }
            });
        }
    }

}
