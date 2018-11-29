package com.group5.android_project;

import android.app.DatePickerDialog;
import android.os.AsyncTask;
<<<<<<< HEAD
import android.os.Bundle;
=======
>>>>>>> ace73addaaf79221511b37a0f30f5c733b1f9063
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.DatePicker;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.JsonParser;
import com.group5.android_project.fragment.PostFragment;
import com.group5.android_project.fragment.ProfileFragment;
import com.group5.android_project.fragment.SearchFragment;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private static final String TAG = "MainActivity";
    public static String mainflag = null;
    public static ArrayList<Vehicle> mainVehicleList;
    public String mainSearchStartDate;
    public String mainSearchEndDate;
    public String postStartDate;
    public String postEndDate;
    public static FirebaseUser mainUser;
    BottomNavigationView bottomNavigationView;
    PostFragment postFragment;
    ProfileFragment profileFragment;
    SearchFragment searchFragment;
    MenuItem prevMenuItem;
    private ViewPager viewPager;
    public static Vehicle postVehicle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainVehicleList = new ArrayList<Vehicle>();

        viewPager = findViewById(R.id.viewpager);
        bottomNavigationView = findViewById(R.id.navigation);

        mainUser = FirebaseAuth.getInstance().getCurrentUser();
        String userID = mainUser.getUid();
        //String userID = "schen"; // for testing
        Log.d(TAG, "onCreate: user name: " + userID);
        String veUrl = "http://ec2-18-219-38-137.us-east-2.compute.amazonaws.com:3000/getCarsByKey?OwnerID=" + userID;
        DownloadVehicleInfo downloadVeInfo = new DownloadVehicleInfo();
        downloadVeInfo.execute(veUrl);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.action_search:
                                viewPager.setCurrentItem(0);
                                break;
                            case R.id.action_post:
                                viewPager.setCurrentItem(1);
                                break;
                            case R.id.action_profile:
                                viewPager.setCurrentItem(2);
                                break;
                        }
                        return false;
                    }
                }
        );

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }

            @Override
            public void onPageSelected(int i) {
                if (prevMenuItem != null) {
                    prevMenuItem.setChecked(false);
                } else {
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }
                Log.d("page", "onPageSelected: " + i);

                if (i == 2 && postVehicle != null) {
                    ProfileFragment.vehicleList.add(postVehicle);
                    postVehicle = null;
                }

                bottomNavigationView.getMenu().getItem(i).setChecked(true);
                prevMenuItem = bottomNavigationView.getMenu().getItem(i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        setupViewPager(viewPager);

    }


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        postFragment = new PostFragment();
        profileFragment = new ProfileFragment();
        searchFragment = new SearchFragment();

        adapter.addFragment(searchFragment);
        adapter.addFragment(postFragment);
        adapter.addFragment(profileFragment);

        viewPager.setAdapter(adapter);
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
        if (mainflag.equals("searchStartDate")) {
            mainSearchStartDate = resultDate;
            searchFragment.setSearchStartDate(resultDate);
        } else if (mainflag.equals("searchEndDate")) {
            mainSearchEndDate = resultDate;
            searchFragment.setSearchEndDate(resultDate);
        } else if (mainflag.equals("postStartDate")) {
            postStartDate = resultDate;
            postFragment.setSearchStartDate(resultDate);
        } else if (mainflag.equals("postEndDate")) {
            postEndDate = resultDate;
            postFragment.setSearchEndDate(resultDate);
        }

    }


    private static class DownloadVehicleInfo extends AsyncTask<String, Void, String> {
        private static final String TAG = "DownloadVehicleInfo";

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d(TAG, "onPostExecute parameter is " + s);
        }

        @Override
        protected String doInBackground(String... strings) {
            Log.d(TAG, "do in background starts with " + strings[0]);
            String vehicleID = downloadXML(strings[0]);
            if (vehicleID == null) {
                Log.e(TAG, "doInBackground, error downloading");
            }

<<<<<<< HEAD
            return vehicleInfo;
=======
            ArrayList<Integer> CarIdList = new ArrayList<>();
            // get Car ID
            try {
                JSONArray jsonArray = new JSONArray(vehicleID);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject e = jsonArray.getJSONObject(i);
                    CarIdList.add(e.getInt("CarID"));
                    Log.d(TAG, "doInBackground: Car ID " + e.getInt("CarID"));
                }

            } catch (Exception e) {
                Log.d(TAG, "doInBackground: parse CarID, JSON array creation failed");
            }

            final String carUrl = "http://ec2-18-219-38-137.us-east-2.compute.amazonaws.com:3000/getCarInfo?CarID=";
            for (Integer id : CarIdList) {
                String url = carUrl + Integer.toString(id);
                String vehicleInfo = downloadXML(url);
                Log.d(TAG, "doInBackground: vehicleInfo " + vehicleInfo);

                // parse vehicle info
                try {
                    JSONArray jsonArray = new JSONArray(vehicleInfo);
                    JSONObject obj = jsonArray.getJSONObject(0);
                    String model = obj.getString("Model");
                    String year = obj.getString("Year");
                    String startDate = obj.getString("CurrentStartDate");
                    if (startDate.equals("null")) {
                        startDate = "";
                    }
                    String endDate = obj.getString("CurrentEndDate");
                    if (endDate.equals("null")) {
                        endDate = "";
                    }
                    String city = obj.getString("HomeCity");
                    String price = String.valueOf(obj.getDouble("PricePerDay"));
                    String detail = obj.getString("Detail");
                    Integer veID = obj.getInt("CarID");

                    Vehicle curVe = new Vehicle(model, year, city, price, detail);
                    curVe.setStartDate(startDate);
                    curVe.setEndDate(endDate);
                    curVe.setVeID(veID);

                    mainVehicleList.add(curVe);

                } catch (Exception e) {
                    Log.d(TAG, "doInBackground: vehicle info parsing failed");
                }
            }

            return vehicleID;
>>>>>>> ace73addaaf79221511b37a0f30f5c733b1f9063
        }

        private String downloadXML(String urlPath) {
            StringBuilder xmlResult = new StringBuilder();
            try {
                URL url = new URL(urlPath);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                int response = connection.getResponseCode();
                Log.d(TAG, "downloadXML response: " + response);

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

                return xmlResult.toString();

            } catch (MalformedURLException e) {
                Log.e(TAG, "downloadXML: Invalid URL" + e.getMessage());
            } catch (IOException e) {
                Log.e(TAG, "downloadXML: IOException reading data: " + e.getMessage());
            } catch (SecurityException e) {
                Log.e(TAG, "downloadXML: Security exception, " + e.getMessage());
                //e.printStackTrace();
            }

            return null;
        }
    }
}
