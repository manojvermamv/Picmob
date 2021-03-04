package com.manoj.phonyhub.test;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.manoj.phonyhub.R;

import java.util.ArrayList;

public class EarthquakeCustomAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    ArrayList<ModelEarthquake> arrayList;
    int VIEW_TYPE_ITEM = 0;
    int VIEW_TYPE_LOADING = 1;

    public EarthquakeCustomAdapter(Context context, ArrayList<ModelEarthquake> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.earthquake_list_item, parent, false);
            return new viewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bottom_item_loading, parent, false);
            return new loadingHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof viewHolder) {
            populateItems((viewHolder) holder, position);
        }
    }

    private void populateItems(viewHolder holder, int position) {
        Context onBindContext = holder.itemView.getContext();
        ModelEarthquake dataModel = arrayList.get(position);

        holder.magnitude_text.setText(dataModel.getMagnitude());
        holder.location_text.setText(dataModel.getLocation());
        holder.date_text.setText(dataModel.getDate());

        holder.list_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(onBindContext, dataModel.getLocation(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemViewType(int position) {
        if (arrayList.get(position) != null) {
            return VIEW_TYPE_ITEM;
        } else {
            return VIEW_TYPE_LOADING;
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    static class viewHolder extends RecyclerView.ViewHolder {
        TextView magnitude_text;
        TextView location_text;
        TextView date_text;
        LinearLayout list_layout;

        public viewHolder(View itemView) {
            super(itemView);
            magnitude_text = itemView.findViewById(R.id.earthquake_magnitude);
            location_text = itemView.findViewById(R.id.earthquake_location);
            date_text = itemView.findViewById(R.id.earthquake_date);
            list_layout = itemView.findViewById(R.id.earthquake_linear_layout);
        }

    }

    static class loadingHolder extends RecyclerView.ViewHolder {
        public loadingHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

}
