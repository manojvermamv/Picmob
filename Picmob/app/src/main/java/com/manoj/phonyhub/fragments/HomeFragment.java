package com.manoj.phonyhub.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.manoj.phonyhub.R;
import com.manoj.phonyhub.activities.WallpaperActivity;
import com.manoj.phonyhub.adapter.PicsumRecycleAdapter;
import com.manoj.phonyhub.data.PicsumDataModel;
import com.manoj.phonyhub.interfaces.PicsumApi;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeFragment extends Fragment implements View.OnClickListener {

    private static final String LIST_STATE = "list_state";
    Button loadMoreWallpapersBtn;
    ProgressBar progressBar;
    RelativeLayout progressBarLayout;
    RecyclerView picsumRecyclerView, featured_recyclerView;
    int index = -1, top = -1;
    int page = 9, limit = 99;

    public HomeFragment() {
        // Requried empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        progressBar = view.findViewById(R.id.fragment_home_progressBar);
        progressBarLayout = view.findViewById(R.id.fragment_home_progressBarLayout);
        picsumRecyclerView = view.findViewById(R.id.picsum_recycleView);
        featured_recyclerView = view.findViewById(R.id.fragment_home_featured_recyclerView);
        loadMoreWallpapersBtn = view.findViewById(R.id.loadMoreWallpapers);

        loadMoreWallpapersBtn.setOnClickListener(this);
        progressBarLayout.setVisibility(View.VISIBLE);
        new loadPicsumDataFromApiBg().start();

        return view;
    }

//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (savedInstanceState == null) {
//            // HomeFragment may called first time
//            loadPicsumData();
//        } else {
//            // Else HomeFragment not the first Time, Restoreing RecycleView position
//            recycleViewState = savedInstanceState.getParcelable("Bundle_Recycle");
//            picsumRecyclerView.getLayoutManager().onRestoreInstanceState(recycleViewState);
//        }
//    }
//    @Override
//    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
//        super.onViewStateRestored(savedInstanceState);
//        if (savedInstanceState != null) {
//            recycleViewState = savedInstanceState.getParcelable("Bundle_Recycle");
//            Objects.requireNonNull(picsumRecyclerView.getLayoutManager()).onRestoreInstanceState(recycleViewState);
//        }
//    }
//    @Override
//    public void onSaveInstanceState(@NonNull Bundle outState) {
//        super.onSaveInstanceState(outState);
//        outState.putParcelable("Bundle_Recycle", Objects.requireNonNull(picsumRecyclerView.getLayoutManager()).onSaveInstanceState());
//    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (view == loadMoreWallpapersBtn) {
            startActivity(new Intent(getActivity(), WallpaperActivity.class));
        }
    }

//    private void loadPicsumDataFromApi() {
//        //Map<String, Integer> pageListMap = new HashMap<String, Integer>();
//        //pageListMap.put("page", 3);
//        //pageListMap.put("limit", 36);
//        PicsumApi.getClient().getPicsumList(page, limit).enqueue(new Callback<List<PicsumDataModel>>() {
//            @Override
//            public void onResponse(@NotNull Call<List<PicsumDataModel>> call, @NotNull Response<List<PicsumDataModel>> response) {
//                picsumDataList = response.body();
//                picsumRecyclerView.setLayoutManager(gridLayoutManager);
//                picsumRecycleAdapter = new PicsumRecycleAdapter(getActivity(), picsumDataList);
//                picsumRecyclerView.setAdapter(picsumRecycleAdapter);
//                progressBarLayout.setVisibility(View.GONE);
//            }
//
//            @Override
//            public void onFailure(@NotNull Call<List<PicsumDataModel>> call, @NotNull Throwable t) {
//                progressBarLayout.setVisibility(View.GONE);
//                networkErrorDialog();
//            }
//        });
//
//    }

    private void networkErrorDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AlertDialogTheme);
        builder.setTitle("Network Error");
        builder.setMessage("Check your internet connection and try again.");
        builder.setCancelable(false);
        builder.setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.create().show();
    }


    class loadPicsumDataFromApiBg extends Thread {
        @Override
        public void run() {
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3, GridLayoutManager.VERTICAL, false);

            PicsumApi.getClient().getPicsumList(page, limit).enqueue(new Callback<List<PicsumDataModel>>() {
                @Override
                public void onResponse(@NotNull Call<List<PicsumDataModel>> call, @NotNull Response<List<PicsumDataModel>> response) {
                    List<PicsumDataModel> picsumDataList = response.body();
                    PicsumRecycleAdapter picsumRecycleAdapter = new PicsumRecycleAdapter(getActivity(), picsumDataList);
                    picsumRecyclerView.setLayoutManager(gridLayoutManager);
                    picsumRecyclerView.setAdapter(picsumRecycleAdapter);
                    Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressBarLayout.setVisibility(View.GONE);
                            loadMoreWallpapersBtn.setVisibility(View.VISIBLE);
                        }
                    });
                }

                @Override
                public void onFailure(@NotNull Call<List<PicsumDataModel>> call, @NotNull Throwable t) {
                    Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
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
