package com.manoj.phonyhub.test;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.manoj.phonyhub.R;

import java.util.ArrayList;


public class EarthquakeActivity extends AppCompatActivity {

    ArrayList<ModelEarthquake> earthquakesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_earthquake_activity);

        // Create a fake list of earthquake locations.
        earthquakesList = new ArrayList<>();
        earthquakesList.add(new ModelEarthquake("7.2", "San Francisco", "Feb 2 2016"));
        earthquakesList.add(new ModelEarthquake("4.9", "London", "May 9 2012"));
        earthquakesList.add(new ModelEarthquake("2.2", "Tokyo", "Jan 22 2017"));
        earthquakesList.add(new ModelEarthquake("3.4", "Mexico City", "Sep 11 2010"));
        earthquakesList.add(new ModelEarthquake("6.5", "Moscow", "Jun 18 2014"));
        earthquakesList.add(new ModelEarthquake("2.9", "Rio de Janeiro", "July 27 2016"));
        earthquakesList.add(new ModelEarthquake("1.7", "SanciscoSancisco", "Oct 25 2011"));
        earthquakesList.add(new ModelEarthquake("5.3", "Los Angles", "March 15 2015"));

        // Find a reference to the {@link RecyclerView} in the layout
        RecyclerView earthquakeListView = findViewById(R.id.earthquake_recycler_view);

        // Create a new {@link CustomAdapter} of earthquakes
        EarthquakeCustomAdapter adapter = new EarthquakeCustomAdapter(this, earthquakesList);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        // Set the adapter on the {@link RecyclerView}
        // so the list can be populated in the user interface
        earthquakeListView.setLayoutManager(layoutManager);
        earthquakeListView.setAdapter(adapter);
    }
}
