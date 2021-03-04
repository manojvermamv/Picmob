package com.manoj.phonyhub.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.manoj.phonyhub.R;
import com.manoj.phonyhub.adapter.PicsumRecycleAdapter;
import com.manoj.phonyhub.data.PicsumDataModel;
import com.manoj.phonyhub.interfaces.PicsumIDInteface;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FavoritesActivity extends AppCompatActivity {

    ArrayList<String> savedImages;
    RecyclerView recyclerView;
    RelativeLayout progressBarLayout;
    ProgressBar progressBar;
    TextView textView;

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

        textView = findViewById(R.id.favorites_temp_textview);
        progressBarLayout = findViewById(R.id.favorites_progressBarLayout);
        progressBar = findViewById(R.id.favorites_progressBar);
        recyclerView = findViewById(R.id.favorites_recycleView);

    }

    @Override
    protected void onResume() {
        super.onResume();
        savedImages = getSavedImagesArrayList(this, "saved_image");
        if (savedImages != null) {
            // String ids = Arrays.toString(savedImages.toArray());
            progressBarLayout.setVisibility(View.VISIBLE);
            new fetchDataInBg().start();
        } else {
            recyclerView.setVisibility(View.GONE);
            textView.setVisibility(View.VISIBLE);
            textView.setText(R.string.no_items);
        }
    }

    @Override
    public void onBackPressed() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAndRemoveTask();
        } else {
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

    static class CallingFrom {
        public static Retrofit retrofit = null;

        // set up the api interface and http client
        public static PicsumIDInteface getApi() {
            if (retrofit == null) {
                retrofit = new Retrofit.Builder()
                        .baseUrl("https://picsum.photos/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
            }
            return retrofit.create(PicsumIDInteface.class);
        }
    }

    class fetchDataInBg extends Thread {
        @Override
        public void run() {

            List<PicsumDataModel> dataModelList = new ArrayList<>();
            GridLayoutManager layoutManager = new GridLayoutManager(FavoritesActivity.this, 3, GridLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(layoutManager);

            for (int i = 0; i < savedImages.size(); i++) {
                int finalI = i;
                CallingFrom.getApi().getImageInfo(Integer.parseInt(savedImages.get(i))).enqueue(new Callback<PicsumDataModel>() {
                    @Override
                    public void onResponse(@NotNull Call<PicsumDataModel> call, @NotNull Response<PicsumDataModel> response) {
                        dataModelList.add(response.body());
                        if (finalI == savedImages.size() - 1) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    progressBarLayout.setVisibility(View.GONE);
                                }
                            });
                            PicsumRecycleAdapter recycleAdapter = new PicsumRecycleAdapter(FavoritesActivity.this, dataModelList);
                            recyclerView.setAdapter(recycleAdapter);
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<PicsumDataModel> call, @NotNull Throwable t) {
                        if (finalI == savedImages.size() - 1) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    progressBarLayout.setVisibility(View.GONE);
                                    networkErrorDialog();
                                }
                            });
                        }
                    }
                });
            }

        }
    }

}