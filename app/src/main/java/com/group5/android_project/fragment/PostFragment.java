package com.group5.android_project.fragment;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;


import com.group5.android_project.BuildConfig;
import com.group5.android_project.MainActivity;
import com.group5.android_project.R;
import com.group5.android_project.Vehicle;


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



public class PostFragment extends Fragment {

    private static final String TAG = "PostFragment";
    public static String flag = null;
    private TextView txtStartDate;
    private TextView txtEndDate;
    private Button btnSubmit;
    private EditText txtModel;
    private EditText txtYear;
    private EditText txtCity;
    private EditText txtPrice;
    private EditText txtDetail;
    private Switch switchAvail;
    private EditText txtLocation;
    public Vehicle postVehicle;


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
        txtLocation = v.findViewById(R.id.txtLocation);
        btnSubmit = v.findViewById(R.id.btnSubmit);
        switchAvail = v.findViewById(R.id.switchAvail);
        switchAvail.setChecked(true);
        //webView = v.findViewById(R.id.webViewCar);





        /*
        Button btnSetStart = v.findViewById(R.id.btnSetStartDate);
        Button btnSetEnd = v.findViewById(R.id.btnSetEndDate);


        btnSetStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.mainflag = "postStartDate";
                MainDatePickerFragment dateFragment = new MainDatePickerFragment();
                dateFragment.show(getFragmentManager(), "mainDatePicker");
            }
        });

        btnSetEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.mainflag = "postEndDate";
                MainDatePickerFragment dateFragment = new MainDatePickerFragment();
                dateFragment.show(getFragmentManager(), "mainDatePicker");
            }
        });

        */

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SubmitVehicle();
            }
        });

        return v;
    }





    public void setSearchStartDate(String date) {
        txtStartDate.setText(date);
    }

    public void setSearchEndDate(String date) {
        txtEndDate.setText(date);
    }

    public void SubmitVehicle() {
        String model = txtModel.getText().toString();
        String year = txtYear.getText().toString();
        String city = txtCity.getText().toString();
        String price = txtPrice.getText().toString();
        String detail = txtDetail.getText().toString();
        /*
        String startDate = txtStartDate.getText().toString();
        if (startDate.length() < 1) {
            startDate = "";
        }
        String endDate = txtEndDate.getText().toString();
        if (endDate.length() < 1) {
            endDate = "";
        }
        */
        Boolean avail = switchAvail.isChecked();


        if (model.equals("") || year.equals("") || city.equals("") || price.equals("") || detail.equals("")) {
            Log.d(TAG, "information incomplete");
            AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
            alert.setTitle("Information incomplete");
            alert.setMessage("Please fill in all the text.");
            alert.setCancelable(false);
            alert.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            AlertDialog alertDialog = alert.create();
            alertDialog.show();
        }
        /*
        //if available, and start date is later than end date, show alert
        else if (avail && (!DateValidation(startDate, endDate))) {
            Log.d(TAG, "date validation failed");
            AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
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

        } */
        else {
            postVehicle = new Vehicle(model, year, city, price, detail);
            //postVehicle.setStartDate(startDate);
            //postVehicle.setEndDate(endDate);
            postVehicle.setAvailable(avail);

            //put postVehicle to main activity
            MainActivity.postVehicle = postVehicle;

            // api
            String urlPath = "http://ec2-18-219-38-137.us-east-2.compute.amazonaws.com:3000/putCarInfo?"
                    + "Model=" + postVehicle.getModel()
                    + "&Year=" + postVehicle.getYear()
                    + "&Color=Black"
                    + "&HomeCity=" + postVehicle.getCity()
                    + "&OwnerID=" + MainActivity.mainUser.getUid()
                    + "&PricePerDay=" + postVehicle.getPrice()
                    + "&Detail=" + postVehicle.getDetail()
                    + "&isAvailable=" + String.valueOf(postVehicle.isAvailable());

            Log.d(TAG, "PutVehicleInfo: URL " + urlPath);
            PutVehicleInfo putVehicleInfo = new PutVehicleInfo();
            putVehicleInfo.execute(urlPath);


            Log.d(TAG, "Submit a new vehicle");
            AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
            alert.setTitle("Successful");
            alert.setMessage("Your new car is been added");
            alert.setCancelable(false);
            alert.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //txtStartDate.setText("");
                    //txtEndDate.setText("");
                    txtModel.setText("");
                    txtYear.setText("");
                    txtCity.setText("");
                    txtPrice.setText("");
                    txtDetail.setText("");
                    txtLocation.setText("");
                    switchAvail.setChecked(true);

                    //clear postVehicle
                    postVehicle = null;
                    dialog.cancel();
                }
            });
            AlertDialog alertDialog = alert.create();
            alertDialog.show();
        }
    }

    private boolean DateValidation(String startDate, String endDate) {
        if (startDate.length() < 1 || endDate.length() < 1) {
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


    private static class PutVehicleInfo extends AsyncTask<String, Void, String> {
        private static final String TAG = "PutVehicleInfo";

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d(TAG, "onPostExecute parameter is " + s);
            //add CarID from response
            if (s.length() == 0) {
                Log.d(TAG, "onPostExecute: No car ID returned");
            } else {
                Integer carId = Integer.valueOf(s);
                MainActivity.postVehicle.setVeID(carId);
            }

        }

        @Override
        protected String doInBackground(String... strings) {
            Log.d(TAG, "do in background starts with " + strings[0]);
            return PutVehicleInformation(strings[0]);
        }


        private String PutVehicleInformation(String urlPath) {
            StringBuilder xmlResult = new StringBuilder();

            try {
                URL url = new URL(urlPath);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                int response = connection.getResponseCode();
                Log.d(TAG, "PutVehicleInfo response: " + response);

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

                Log.d(TAG, "PutVehicleInfo: Result: " + xmlResult.toString());

            } catch (MalformedURLException e) {
                Log.e(TAG, "PutVehicleInfo: Invalid URL" + e.getMessage());
            } catch (IOException e) {
                Log.e(TAG, "PutVehicleInfo: IOException reading data: " + e.getMessage());
            } catch (SecurityException e) {
                Log.e(TAG, "PutVehicleInfo: Security exception, " + e.getMessage());
                //e.printStackTrace();
            }

            return xmlResult.toString();

        }
    }

}
