package com.group5.android_project;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import com.group5.android_project.fragment.ProfileFragment;


public class VehicleProfileActivity extends AppCompatActivity
        implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private static final String TAG = "VehicleProfileActivity";
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
    private Vehicle profileVehicle;

    int images[] = {R.drawable.car_default, R.drawable.car_default};
    ImageSliderPagerAdapter imageSlider;
    private ViewPager viewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vehicle_profile);

        txtModel = findViewById(R.id.txtModel);
        txtYear = findViewById(R.id.txtYear);
        txtCity = findViewById(R.id.txtCity);
        txtPrice = findViewById(R.id.txtPrice);
        txtDetail = findViewById(R.id.txtDetail);
        txtStartDate = findViewById(R.id.selectedStartDate);
        txtEndDate = findViewById(R.id.selectedEndDate);
        txtStartTime = findViewById(R.id.selectedStartTime);
        txtEndTime = findViewById(R.id.selectedEndTime);
        btnSave = findViewById(R.id.btnSave);
        switchAvail = findViewById(R.id.switchAvail);
        Button btnSetStart = findViewById(R.id.btnSetStartDate);
        Button btnSetEnd = findViewById(R.id.btnSetEndDate);
        Button btnStartTime = findViewById(R.id.btnStartTime);
        Button btnEndTime = findViewById(R.id.btnEndTime);
        viewPager = findViewById(R.id.veImage_viewPager);

        imageSlider = new ImageSliderPagerAdapter(this, images);
        viewPager.setAdapter(imageSlider);

        //get values from intent
        profileVehicle = getIntent().getParcelableExtra("vehicle");

        txtModel.setText(profileVehicle.getModel());
        txtYear.setText(profileVehicle.getYear());
        txtCity.setText(profileVehicle.getCity());
        txtPrice.setText(profileVehicle.getPrice());
        txtDetail.setText(profileVehicle.getDetail());
        txtStartDate.setText(profileVehicle.getStartDate());
        txtEndDate.setText(profileVehicle.getEndDate());
        txtStartTime.setText(profileVehicle.getStartTime());
        txtEndTime.setText(profileVehicle.getEndTime());
        switchAvail.setChecked(profileVehicle.isAvailable());

        btnSetStart.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        flag = "startDate";
                        showDatePickerDialog(v);
                    }
                }
        );

        btnSetEnd.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        flag = "endDate";
                        showDatePickerDialog(v);
                    }
                }
        );

        btnStartTime.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        flag = "startTime";
                        showTimePickerDialog(v);
                    }
                }
        );

        btnEndTime.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        flag = "endTime";
                        showTimePickerDialog(v);
                    }
                }
        );

        UpdateVehicle();
    }

    public void showDatePickerDialog(View v) {
        DatePickerFragment dateFragment = new DatePickerFragment();
        dateFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void showTimePickerDialog(View v) {
        TimePickerFragment timeFragment = new TimePickerFragment();
        timeFragment.show(getSupportFragmentManager(), "timePicker");
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        month += 1;
        Log.d(TAG, "onDataSet " + Integer.toString(month) + " " + Integer.toString(day));
        String nyear, nmonth, nday;
        nyear = Integer.toString(year);
        if (day < 10) {
            nday = "0" + Integer.toString(day);
        } else {
            nday = Integer.toString(day);
        }
        if (month < 10) {
            nmonth = "0" + Integer.toString(month);
        } else {
            nmonth = Integer.toString(month);
        }

        String resultDate = nday + "/" + nmonth + "/" + nyear;
        if (flag.equals("startDate")) {
            txtStartDate.setText(resultDate);
        } else if (flag.equals("endDate")) {
            txtEndDate.setText(resultDate);
        }

    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        String nhour, nminute;
        if (hourOfDay < 10) {
            nhour = "0" + Integer.toString(hourOfDay);
        } else {
            nhour = Integer.toString(hourOfDay);
        }
        if (minute < 10) {
            nminute = "0" + Integer.toString(minute);
        } else {
            nminute = Integer.toString(minute);
        }

        String resultTime = nhour + ":" + nminute;
        if (flag.equals("startTime")) {
            txtStartTime.setText(resultTime);
        } else if (flag.equals("endTime")) {
            txtEndTime.setText(resultTime);
        }
    }

    public void UpdateVehicle() {
        btnSave.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Boolean avail = switchAvail.isChecked();
                        //if available, and start date is later than end date, show alert
                        if (avail && (!DateValidation())) {
                            Log.d(TAG, "date validation failed");
                            AlertDialog.Builder alert = new AlertDialog.Builder(VehicleProfileActivity.this);
                            alert.setTitle("Date");
                            alert.setMessage("Your end date should be later than your start date");
                            alert.setCancelable(false);
                            alert.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                            AlertDialog alertDialog = alert.create();
                            alertDialog.show();
                        } else {
                            //save and go back to profile
                            int i = getIntent().getIntExtra("vehicleIndex", 0);
                            Log.d(TAG, txtModel.getText().toString());
                            Log.d(TAG, ProfileFragment.vehicleList.get(i).getModel());

                            String model = txtModel.getText().toString();
                            String year = txtYear.getText().toString();
                            String city = txtCity.getText().toString();
                            String price = txtPrice.getText().toString();
                            String detail = txtDetail.getText().toString();
                            String startDate = txtStartDate.getText().toString();
                            String endDate = txtEndDate.getText().toString();
                            String startTime = txtStartTime.getText().toString();
                            String endTime = txtEndTime.getText().toString();


                            ProfileFragment.vehicleList.get(i).setModel(model);
                            ProfileFragment.vehicleList.get(i).setYear(year);
                            ProfileFragment.vehicleList.get(i).setCity(city);
                            ProfileFragment.vehicleList.get(i).setPrice(price);
                            ProfileFragment.vehicleList.get(i).setDetail(detail);
                            ProfileFragment.vehicleList.get(i).setStartDate(startDate);
                            ProfileFragment.vehicleList.get(i).setEndDate(endDate);
                            ProfileFragment.vehicleList.get(i).setStartTime(startTime);
                            ProfileFragment.vehicleList.get(i).setEndTime(endTime);
                            ProfileFragment.vehicleList.get(i).setAvailable(avail);

                            finish();
                        }
                    }
                }
        );
    }

    private boolean DateValidation() {
        String startDate = txtStartDate.getText().toString();
        String endDate = txtEndDate.getText().toString();
        int startyear = Integer.valueOf(startDate.substring(6, 10));
        int endyear = Integer.valueOf(endDate.substring(6, 10));
        if (startyear > endyear) {
            return false;
        }
        if (startyear < endyear) {
            return true;
        }

        int startmonth = Integer.valueOf(startDate.substring(3, 5));
        int endmonth = Integer.valueOf(endDate.substring(3, 5));
        if (startmonth > endmonth) {
            return false;
        }
        if (startmonth < endmonth) {
            return true;
        }

        int startday = Integer.valueOf(startDate.substring(0, 2));
        int endday = Integer.valueOf(endDate.substring(0, 2));
        return startday < endday;
    }

}
