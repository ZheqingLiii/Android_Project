package com.group5.android_project;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


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
                            Log.d(TAG, "TEST " + ProfileFragment.vehicleList.get(i).getVeID().toString());

                            String model = txtModel.getText().toString();
                            String year = txtYear.getText().toString();
                            String city = txtCity.getText().toString();
                            String price = txtPrice.getText().toString();
                            String detail = txtDetail.getText().toString();

                            String startDate = txtStartDate.getText().toString();
                            String endDate = txtEndDate.getText().toString();
                            String startTime = txtStartTime.getText().toString();
                            String endTime = txtEndTime.getText().toString();


                            /*
                            // call api to update
                            UpdateVehicleInfo updateVehicleInfo = new UpdateVehicleInfo();
                            String url = "http://ec2-18-219-38-137.us-east-2.compute.amazonaws.com:3000/updateCarInfo?CarID="
                                    + ProfileFragment.vehicleList.get(i).getVeID();
                            if (!model.equals(ProfileFragment.vehicleList.get(i).getModel())) {
                                String modelUrl = url + "&Model=" + model;
                                Log.d(TAG, "onClick: url " + modelUrl);
                                updateVehicleInfo.execute(modelUrl);
                            }
                            if (!year.equals(ProfileFragment.vehicleList.get(i).getYear())) {
                                String yearUrl = url + "&Year=" + year;
                                Log.d(TAG, "onClick: url " + yearUrl);
                                updateVehicleInfo.execute(yearUrl);
                            }
                            if (!city.equals(ProfileFragment.vehicleList.get(i).getCity())) {
                                String cityUrl = url + "&HomeCity=" + city;
                                Log.d(TAG, "onClick: url " + cityUrl);
                                updateVehicleInfo.execute(cityUrl);
                            }
                            if (!price.equals(ProfileFragment.vehicleList.get(i).getPrice())) {
                                String priceUrl = url + "&PricePerDay=" + price;
                                Log.d(TAG, "onClick: url " + priceUrl);
                                updateVehicleInfo.execute(priceUrl);
                            }
                            if (!detail.equals(ProfileFragment.vehicleList.get(i).getDetail())) {
                                String detailUrl = url + "&Detail=" + detail;
                                Log.d(TAG, "onClick: url " + detailUrl);
                                updateVehicleInfo.execute(detailUrl);
                            }
                            if (avail != ProfileFragment.vehicleList.get(i).isAvailable()) {
                                String availUrl = url + "&isAvailable=" + avail;
                                Log.d(TAG, "onClick: url " + availUrl);
                                updateVehicleInfo.execute(availUrl);
                            }
                            */


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
        if (startDate.length() < 10 || endDate.length() < 10) {
            return true;
        }
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


    private static class UpdateVehicleInfo extends AsyncTask<String, Void, String> {
        private static final String TAG = "UpdateVehicleInfo";

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d(TAG, "onPostExecute parameter is " + s);
        }

        @Override
        protected String doInBackground(String... strings) {
            Log.d(TAG, "do in background starts with " + strings[0]);
            return UpdateVeInfo(strings[0]);
        }


        private String UpdateVeInfo(String urlPath) {
            StringBuilder xmlResult = new StringBuilder();

            try {
                URL url = new URL(urlPath);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                int response = connection.getResponseCode();
                Log.d(TAG, "UpdateVeInfo response: " + response);

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                int charsRead;
                char[] inputBuffer = new char[500];
                while (true) {
                    charsRead = reader.read(inputBuffer);
                    if (charsRead < 0) {
                        break;
                    }
                    if (charsRead > 0) {
                        xmlResult.append(String.copyValueOf(inputBuffer, 0, charsRead));
                    }
                }

                reader.close();

                Log.d(TAG, "UpdateVeInfo: Result: " + xmlResult.toString());

            } catch (MalformedURLException e) {
                Log.e(TAG, "UpdateVeInfo: Invalid URL" + e.getMessage());
            } catch (IOException e) {
                Log.e(TAG, "UpdateVeInfo: IOException reading data: " + e.getMessage());
            } catch (SecurityException e) {
                Log.e(TAG, "UpdateVeInfo: Security exception, " + e.getMessage());
                //e.printStackTrace();
            }

            return xmlResult.toString();

        }
    }

}
