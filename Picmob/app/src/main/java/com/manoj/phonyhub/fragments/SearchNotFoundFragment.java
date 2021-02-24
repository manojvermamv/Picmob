package com.manoj.phonyhub.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.manoj.phonyhub.R;

public class SearchNotFoundFragment extends Fragment {

    public SearchNotFoundFragment() {
        // Requried empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_not_found_fragment, container, false);
        return view;
    }


}
