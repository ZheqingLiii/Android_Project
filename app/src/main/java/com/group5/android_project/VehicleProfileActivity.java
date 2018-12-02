package com.group5.android_project;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import com.group5.android_project.fragment.ProfileFragment;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;


public class VehicleProfileActivity extends AppCompatActivity
        implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private static final String TAG = "VehicleProfileActivity";
    public static String flag = null;
    public TextView txtStartDate;
    public TextView txtEndDate;
    public TextView txtStartTime;
    public TextView txtEndTime;
    private Button btnSave;
    Uri file;
    private EditText txtModel;
    private EditText txtYear;
    private EditText txtCity;
    private EditText txtPrice;
    private EditText txtDetail;
    private Switch switchAvail;
    private Vehicle profileVehicle;
    private Button btnUpload;
    private String carID;
    private WebView webView;

    int images[] = {R.drawable.car_default, R.drawable.car_default};
    //ImageSliderPagerAdapter imageSlider;
    //private ViewPager viewPager;

    private static File getOutputMediaFile() {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "CameraDemo");

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return new File(mediaStorageDir.getPath() + File.separator +
                "IMG_" + timeStamp + ".jpg");
    }

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
        //viewPager = findViewById(R.id.veImage_viewPager);
        webView = findViewById(R.id.webView);

        //imageSlider = new ImageSliderPagerAdapter(this, images);
        //viewPager.setAdapter(imageSlider);

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

        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        if (profileVehicle.getImageUrl() != null && profileVehicle.getImageUrl().length() > 1) {
            String setUrl = "http://18.219.38.137/home/team5/ftp/files/CarID" + profileVehicle.getVeID().toString() + ".jpg";
            webView.loadUrl(setUrl);
        } else {
            webView.loadUrl("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTdAGzADdM8qUH4OmLwHy10u9vXicBdaHPpOCj-G4XWNdWl38Oa");
        }

        carID = profileVehicle.getVeID().toString();
        btnUpload = findViewById(R.id.button_image);
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture(v);
            }
        });

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {

                new Post().execute(1, 1, 1);
                Log.i("Pic Log: ", "Here 2: " + file.getPath());
            }
        }
    }

    public void uploadFile(File fileName) {
        FTPClient ftpClient = new FTPClient();

        try {
            ftpClient.connect("18.219.38.137");
            ftpClient.login("team5", "coen268");
            ftpClient.changeWorkingDirectory("/files");
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            BufferedInputStream buffIn = null;

            buffIn = new BufferedInputStream(new FileInputStream(this.getContentResolver().openFileDescriptor(file, "r").getFileDescriptor()));
            ftpClient.enterLocalPassiveMode();
            String imageName = "CarID" + carID + ".jpg";
            Log.d(TAG, "uploadFile: imageName: " + imageName);
            ftpClient.storeFile(imageName, buffIn);
            buffIn.close();
            ftpClient.logout();
            ftpClient.disconnect();
            Log.d("FTP code: ", "Success");
            String imageUrl = "http://18.219.38.137/home/team5/ftp/files/" + imageName;
            profileVehicle.setImageUrl(imageUrl);

        } catch (Exception e) {
            Log.d("FTP code: ", "Error1");
            e.printStackTrace();
            try {
                Log.d("FTP code: ", "Error2");
                ftpClient.disconnect();
            } catch (Exception e2) {
                Log.d("FTP code: ", "Error3");
                e2.printStackTrace();
            }
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                Log.d("Pic Log: ", "xHere 1");
                btnUpload.setEnabled(true);
                Log.d("Pic Log: ", "xHere 2");
            }
        }
    }


    public void takePicture(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        file = FileProvider.getUriForFile(VehicleProfileActivity.this, BuildConfig.APPLICATION_ID, getOutputMediaFile());
        Log.d("Pic Log: ", "uploaded started: " + file.getPath());

        intent.putExtra(MediaStore.EXTRA_OUTPUT, file);
        Log.d("Pic Log: ", "intent set");
        this.startActivityForResult(intent, 100);

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

                            String model = txtModel.getText().toString();
                            String year = txtYear.getText().toString();
                            String city = txtCity.getText().toString();
                            String price = txtPrice.getText().toString();
                            String detail = txtDetail.getText().toString();
                            String startDate = txtStartDate.getText().toString();
                            String endDate = txtEndDate.getText().toString();
                            String startTime = txtStartTime.getText().toString();
                            String endTime = txtEndTime.getText().toString();
                            String available = String.valueOf(avail);



                            // call api to update
                            UpdateVehicleInfo updateVehicleInfo = new UpdateVehicleInfo();
                            String url = "http://ec2-18-219-38-137.us-east-2.compute.amazonaws.com:3000/updateCarInfoBulk?CarID="
                                    + ProfileFragment.vehicleList.get(i).getVeID()
                                    + "&Model=" + model.replaceAll("\\s", "")
                                    + "&Year=" + year.replaceAll("\\s", "")
                                    + "&HomeCity=" + city.replaceAll("\\s", "")
                                    + "&PricePerDay=" + price.replaceAll("\\s", "")
                                    + "&Detail=" + detail.replaceAll("\\s", "")
                                    + "&isAvailable=" + available
                                    + "&lat=" + profileVehicle.getLat()
                                    + "&lng=" + profileVehicle.getLng();
                            Log.d(TAG, "onClick: url " + url);
                            updateVehicleInfo.execute(url);




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
                            ProfileFragment.vehicleList.get(i).setImageUrl(profileVehicle.getImageUrl());


                            finish();
                        }
                    }
                }
        );
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

            String updateBulk = UpdateVeInfo(strings[0]);
            return updateBulk;
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

    private class Post extends AsyncTask<Integer, Integer, Integer> {
        protected void onPreExecute() {
        }

        @Override
        protected Integer doInBackground(Integer... arg) {
            uploadFile(new File(file.getPath()));
            return 0;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            webView.getSettings().setLoadWithOverviewMode(true);
            webView.getSettings().setUseWideViewPort(true);
            webView.loadUrl(profileVehicle.getImageUrl());
        }
    }

}
