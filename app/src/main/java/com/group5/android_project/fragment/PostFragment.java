package com.group5.android_project.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import com.group5.android_project.DatePickerFragment;
import com.group5.android_project.R;
import com.group5.android_project.TimePickerFragment;
import com.group5.android_project.Vehicle;
import com.group5.android_project.VehicleProfileActivity;

public class PostFragment extends Fragment {

    private static final String TAG = "PostFragment";
    public static String flag = null;
    public TextView txtStartDate;
    public TextView txtEndDate;
    public TextView txtStartTime;
    public TextView txtEndTime;
    private Button btnSave;
    private EditText txtModel;
    private EditText txtYear;
    private EditText txtCity;
    private EditText txtPrice;
    private EditText txtDetail;
    private Switch switchAvail;
    private Vehicle postVehicle;

    public PostFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.activity_post, container, false);
        txtModel = v.findViewById(R.id.txtModel);
        txtYear = v.findViewById(R.id.txtYear);
        txtCity = v.findViewById(R.id.txtCity);
        txtPrice = v.findViewById(R.id.txtPrice);
        txtDetail = v.findViewById(R.id.txtDetail);
        txtStartDate = v.findViewById(R.id.selectedStartDate);
        txtEndDate = v.findViewById(R.id.selectedEndDate);
        txtStartTime = v.findViewById(R.id.selectedStartTime);
        txtEndTime = v.findViewById(R.id.selectedEndTime);
        btnSave = v.findViewById(R.id.btnSave);
        switchAvail = v.findViewById(R.id.switchAvail);
        switchAvail.setChecked(true);
        Button btnSetStart = v.findViewById(R.id.btnSetStartDate);
        Button btnSetEnd = v.findViewById(R.id.btnSetEndDate);
        Button btnStartTime = v.findViewById(R.id.btnStartTime);
        Button btnEndTime = v.findViewById(R.id.btnEndTime);

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);


    }


}
