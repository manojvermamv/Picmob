package com.manoj.phonyhub.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.facebook.shimmer.Shimmer;
import com.facebook.shimmer.ShimmerDrawable;
import com.manoj.phonyhub.HelperMethod;
import com.manoj.phonyhub.R;
import com.manoj.phonyhub.data.PicsumDataModel;

import java.util.ArrayList;
import java.util.List;

public class TestRecyclerAdapter extends RecyclerView.Adapter<TestRecyclerAdapter.mViewHolder> {

    Context context;
    List<PicsumDataModel> picsumItemList;

    public TestRecyclerAdapter(Context context, List<PicsumDataModel> picsumItemList) {
        this.context = context;
        this.picsumItemList = picsumItemList;
    }

    @NonNull
    @Override
    public mViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_test, parent, false);
        return new mViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull mViewHolder holder, int position) {
        Context onBindViewContext = holder.itemView.getContext();
        PicsumDataModel dataModel = picsumItemList.get(position);

        String id = dataModel.getId();
        String author_name = dataModel.getAuthor();
        String thumbnail_url_small = dataModel.getUrl() + "/download?force=true&w=640";
        String thumbnail_url = "https://picsum.photos/id/" + id + "/640/920";

        Shimmer shimmer = new Shimmer.AlphaHighlightBuilder()
                .setDuration(1800L)
                .setBaseAlpha(0.4F)
                .setHighlightAlpha(0.8F)
                .setDirection(Shimmer.Direction.LEFT_TO_RIGHT)
                .setAutoStart(true)
                .build();

        ShimmerDrawable shimmerDrawable = new ShimmerDrawable();
        shimmerDrawable.setShimmer(shimmer);

        Glide.with(onBindViewContext)
                .load(thumbnail_url_small)
                .placeholder(shimmerDrawable)
                .into(holder.imageView);

        holder.textView.setText(author_name);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HelperMethod.toast("Clicked On ID " + dataModel.getId());
            }
        });

    }

    @Override
    public int getItemCount() {
        return picsumItemList.size();
    }

    static class mViewHolder extends RecyclerView.ViewHolder {
        // init the item view's
        public TextView textView;
        public ImageView imageView;

        public mViewHolder(View itemView) {
            super(itemView);
            // get the reference of item view's
            textView = itemView.findViewById(R.id.text_view_test);
            imageView = itemView.findViewById(R.id.image_view_test);
        }

    }

}
