package com.group5.android_project.fragment;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.group5.android_project.R;

public class SearchFragment extends Fragment {
    ConstraintLayout searchLayout;
    RelativeLayout searchDropdown;
    TextView dropdownArrow;
    ListView carListView;
    boolean isDropdownActive = false;

    public SearchFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_search, container, false);

        searchLayout = (ConstraintLayout) view.findViewById(R.id.searchLayout);
        searchDropdown = (RelativeLayout) view.findViewById(R.id.searchDropdownLayout);
        dropdownArrow = (TextView) view.findViewById(R.id.dropdownArrow);
        searchLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isDropdownActive) {
                    searchDropdown.setVisibility(View.GONE);
                    dropdownArrow.setText("\u25BC");
                } else{
                    searchDropdown.setVisibility(View.VISIBLE);
                    dropdownArrow.setText("\u25B2");
                }
                isDropdownActive = !isDropdownActive;
            }
        });

        carListView = (ListView) view.findViewById(R.id.carListView);
        // TODO: setup adapter

        // Inflate the layout for this fragment
        return view;
    }

}
