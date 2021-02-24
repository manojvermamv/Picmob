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
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.facebook.shimmer.Shimmer;
import com.facebook.shimmer.ShimmerDrawable;
import com.manoj.phonyhub.R;

import java.util.List;

public class SearchRecyclerAdapter extends RecyclerView.Adapter<SearchRecyclerAdapter.ViewHolder> {

    private Context context;
    private List<String> itemDataList;
    private ItemClickListener mitemClickListener;

    public SearchRecyclerAdapter(Context context, List<String> itemDataList) {
        this.context = context;
        this.itemDataList = itemDataList;
    }

    // Inflate the row layout from xml
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.search_item_layout, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the all view in above inflated layout in each row(counts of rows from below getItemCount() Method)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String url = itemDataList.get(position);
        //String url = "https://source.unsplash.com/1024x580/?bike,water";

        Shimmer shimmer = new Shimmer.AlphaHighlightBuilder()
                .setDuration(1800L)
                .setBaseAlpha(0.4F)
                .setHighlightAlpha(0.3F)
                .setDirection(Shimmer.Direction.LEFT_TO_RIGHT)
                .setAutoStart(true)
                .build();

        ShimmerDrawable shimmerDrawable = new ShimmerDrawable();
        shimmerDrawable.setShimmer(shimmer);
        Glide.with(context).load(url)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .placeholder(shimmerDrawable)
                .into(holder.imageView);
        holder.textView.setText(url);
    }

    // total counts of rows
    @Override
    public int getItemCount() {
        return itemDataList.size();
    }

    // convenience method for getting data at click position
    public String getItem(int id) {
        return itemDataList.get(id);
    }

    // allow clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mitemClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click event
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView;
        TextView textView;

        ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.search_item_image_view);
            textView = itemView.findViewById(R.id.search_item_text_view);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mitemClickListener != null) {
                mitemClickListener.onItemClick(v, getAdapterPosition());
            }
        }

    }


}
