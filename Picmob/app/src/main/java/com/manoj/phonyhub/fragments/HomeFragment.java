package com.manoj.phonyhub.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.manoj.phonyhub.R;
import com.manoj.phonyhub.adapter.PicsumRecycleAdapter;
import com.manoj.phonyhub.data.PicsumDataModel;
import com.manoj.phonyhub.extra.EndlessRecyclerViewScrollListener;
import com.manoj.phonyhub.interfaces.PicsumApi;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeFragment extends Fragment implements View.OnClickListener {

    private static final String LIST_STATE = "list_state";
    ProgressBar progressBar;
    RecyclerView picsumRecyclerView;
    GridLayoutManager gridLayoutManager;
    PicsumRecycleAdapter picsumRecycleAdapter;
    List<PicsumDataModel> picsumDataList;
    PicsumDataModel dataModel;
    int index = -1, top = -1;
    int page = 1, limit = 36;
    private Parcelable recycleViewState;
    private Bundle savedState = null;

    public HomeFragment() {
        // Requried empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        progressBar = view.findViewById(R.id.fragment_home_progressBar);
        picsumRecyclerView = view.findViewById(R.id.picsum_recycleView);

        // Load first limited data from API
        gridLayoutManager = new GridLayoutManager(getActivity(), 3, GridLayoutManager.VERTICAL, false);
        loadPicsumDataFromApi();

        EndlessRecyclerViewScrollListener endlessScrollListener = new EndlessRecyclerViewScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Load more data from API and adding to the recycler view
                loadMorePicsumDataFromApi(view, page);
            }
        };

        // Attaching Scroll Listener with RecyclerView
        picsumRecyclerView.addOnScrollListener(endlessScrollListener);

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
    public void onClick(View v) {
        int id = v.getId();
    }

    private void loadPicsumDataFromApi() {
        //Map<String, Integer> pageListMap = new HashMap<String, Integer>();
        //pageListMap.put("page", 3);
        //pageListMap.put("limit", 36);
        progressBar.setVisibility(View.VISIBLE);
        PicsumApi.getClient().getPicsumList(page, limit).enqueue(new Callback<List<PicsumDataModel>>() {
            @Override
            public void onResponse(@NotNull Call<List<PicsumDataModel>> call, @NotNull Response<List<PicsumDataModel>> response) {
                picsumDataList = response.body();
                picsumRecyclerView.setLayoutManager(gridLayoutManager);
                picsumRecycleAdapter = new PicsumRecycleAdapter(getActivity(), picsumDataList);
                picsumRecyclerView.setAdapter(picsumRecycleAdapter);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(@NotNull Call<List<PicsumDataModel>> call, @NotNull Throwable t) {
                progressBar.setVisibility(View.GONE);
                networkErrorDialog();
            }
        });

    }

    private void loadMorePicsumDataFromApi(RecyclerView recyclerView, int page) {
        //picsumDataList.add(null);
        //picsumRecycleAdapter.notifyItemInserted(picsumDataList.size() - 1);
        PicsumApi.getClient().getPicsumList(page, limit).enqueue(new Callback<List<PicsumDataModel>>() {
            @Override
            public void onResponse(@NotNull Call<List<PicsumDataModel>> call, @NotNull Response<List<PicsumDataModel>> response) {
                //picsumDataList.remove(picsumDataList.size() - 1);
                List<PicsumDataModel> moreData = response.body();
                picsumDataList.addAll(moreData);
                int curSize = picsumRecycleAdapter.getItemCount();
                recyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        //picsumRecycleAdapter.notifyItemRangeInserted(curSize, picsumDataList.size());
                        //picsumRecycleAdapter.notifyItemRangeInserted(curSize, picsumDataList.size() - 1);
                        picsumRecycleAdapter.notifyItemRangeInserted(curSize, moreData.size());
                    }
                });
            }

            @Override
            public void onFailure(@NotNull Call<List<PicsumDataModel>> call, @NotNull Throwable t) {
                networkErrorDialog();
            }
        });

    }

//    @Override
//    public void onPause() {
//        super.onPause();
//        // read current state of RecycleView position
//        if (layoutManager != null) {
//            index = layoutManager.findFirstVisibleItemPosition();
//            View view = picsumRecyclerView.getChildAt(0);
//            top = (view == null) ? 0 : (view.getTop() - picsumRecyclerView.getPaddingTop());
//        }
//        //savedRecycleState = layoutManager.onSaveInstanceState();
//    }
//    @Override
//    public void onResume() {
//        super.onResume();
//        // set RecycleView position
//        if (index != -1 && layoutManager != null) {
//            layoutManager.scrollToPositionWithOffset(index, top);
//        }
//        //layoutManager.onRestoreInstanceState(savedRecycleState);
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

}
