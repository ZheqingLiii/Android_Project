package com.group5.android_project.fragment;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.group5.android_project.MainActivity;
import com.group5.android_project.MainDatePickerFragment;
import com.group5.android_project.R;

public class SearchFragment extends Fragment {
    ConstraintLayout searchLayout;
    RelativeLayout searchDropdown;
    TextView dropdownArrow;
    ListView carListView;
    Button btnSearchStartDate;
    Button btnSearchEndDate;
    public TextView txtsearchStartDate;
    public TextView txtsearchEndDate;
    public TextView txtsearchStartDate1;
    public TextView txtsearchEndDate1;

    boolean isDropdownActive = false;

    public SearchFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_search, container, false);

        final MainActivity mainActivity = (MainActivity) getActivity();
        searchLayout = view.findViewById(R.id.searchLayout);
        searchDropdown = view.findViewById(R.id.searchDropdownLayout);
        dropdownArrow = view.findViewById(R.id.dropdownArrow);
        txtsearchStartDate = view.findViewById(R.id.txtStartDate);
        txtsearchEndDate = view.findViewById(R.id.txtEndDate);
        txtsearchStartDate1 = view.findViewById(R.id.startDateTextView);
        txtsearchEndDate1 = view.findViewById(R.id.endDateTextView);
        btnSearchStartDate = view.findViewById(R.id.btnStartDate);
        btnSearchEndDate = view.findViewById(R.id.btnEndDate);
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

        btnSearchStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.mainflag = "searchStartDate";
                MainDatePickerFragment dateFragment = new MainDatePickerFragment();
                dateFragment.show(getFragmentManager(), "mainDatePicker");
            }
        });
        //txtsearchStartDate.setText(mainActivity.mainSearchStartDate);
        //txtsearchStartDate1.setText(mainActivity.mainSearchStartDate);

        btnSearchEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.mainflag = "searchEndDate";
                MainDatePickerFragment dateFragment = new MainDatePickerFragment();
                dateFragment.show(getFragmentManager(), "mainDatePicker");
            }
        });
        //txtsearchEndDate.setText(mainActivity.mainSearchEndDate);
        //txtsearchEndDate1.setText(mainActivity.mainSearchEndDate);

        carListView = view.findViewById(R.id.carListView);
        // TODO: setup adapter

        // Inflate the layout for this fragment
        return view;
    }


    public void setSearchStartDate(String date) {
        txtsearchStartDate.setText(date);
        txtsearchStartDate1.setText(date);
    }

    public void setSearchEndDate(String date) {
        txtsearchEndDate.setText(date);
        txtsearchEndDate1.setText(date);
    }

}
