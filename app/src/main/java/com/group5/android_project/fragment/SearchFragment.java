package com.group5.android_project.fragment;

import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.group5.android_project.CarSearchAdapter;
import com.group5.android_project.MainActivity;
import com.group5.android_project.MainDatePickerFragment;
import com.group5.android_project.R;
import com.group5.android_project.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SearchFragment extends Fragment {
    private View view;
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
    TextView locationTextView;
    double[] latlong = new double[2];

    boolean isDropdownActive = false;

    Utils utils = new Utils();

    public SearchFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_search, container, false);

        final MainActivity mainActivity = (MainActivity) getActivity();
        searchLayout = view.findViewById(R.id.searchLayout);
        searchDropdown = view.findViewById(R.id.searchDropdownLayout);
        dropdownArrow = view.findViewById(R.id.dropdownArrow);

        locationTextView = view.findViewById(R.id.locationTextView);
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

        btnSearchEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.mainflag = "searchEndDate";
                MainDatePickerFragment dateFragment = new MainDatePickerFragment();
                dateFragment.show(getFragmentManager(), "mainDatePicker");
            }
        });

        search();

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

    public void search() {
        String addressString = locationTextView.getText().toString();
        double[] latlong = new double[2];

        Geocoder geocoder = new Geocoder(view.getContext(), Locale.getDefault());
        try {
            List addressList = geocoder.getFromLocationName(addressString, 1);
            if (addressList != null && addressList.size() > 0) {
                Address address = (Address) addressList.get(0);
                latlong[0] = address.getLatitude();
                latlong[1] = address.getLongitude();

                String url = "http://ec2-18-219-38-137.us-east-2.compute.amazonaws.com:3000/getCarsByLocation?Lat=" + latlong[0] + "&Long=" + latlong[1];
                DownloadVehicleInfo downloadVeInfo = new DownloadVehicleInfo(getActivity(), view);
                downloadVeInfo.execute(url);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public class DownloadVehicleInfo extends AsyncTask<String, Void, String> {
        private static final String TAG = "DownloadVehicleInfo";
        Activity ctx;
        View view;

        public DownloadVehicleInfo(Activity ctx, View view) {
            this.ctx = ctx;
            this.view = view;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d(TAG, "onPostExecute parameter is " + s);
        }

        @Override
        protected String doInBackground(String... strings) {
            String carList = Utils.downloadXML(strings[0]);

            ArrayList<Integer> carIdList = new ArrayList<Integer>();
            ArrayList<Double> carDistanceList = new ArrayList<Double>();
            ArrayList<String> carNameList = new ArrayList<String>();
            ArrayList<Double> carPriceList = new ArrayList<Double>();
            ArrayList<String> carImgURL = new ArrayList<String>();

            // get Car ID
            try {
                JSONArray jsonArray = new JSONArray(carList);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject car = jsonArray.getJSONObject(i);
                    int id = car.getInt("CarID");
                    carIdList.add(id);
                    carDistanceList.add(car.getDouble("distance"));

                    String carUrl = "http://ec2-18-219-38-137.us-east-2.compute.amazonaws.com:3000/getCarInfo?CarID=" + id;
                    String carInfo = Utils.downloadXML(carUrl);
                    JSONArray carArray = new JSONArray(carInfo);
                    car = carArray.getJSONObject(0);

                    StringBuilder nameBuffer = new StringBuilder();
                    nameBuffer.append(car.getString("Color"))
                            .append(" ").append(car.getString("Year"))
                            .append(" ").append(car.getString("Model"));
                    carNameList.add(nameBuffer.toString());

                    carPriceList.add(car.optDouble("PricePerDay"));

                    carImgURL.add(car.optString("CarPhotoURL"));
                }

                CarSearchAdapter adapter = new CarSearchAdapter(ctx, carIdList, carDistanceList, carNameList, carPriceList, carImgURL);
                ListView carListView = view.findViewById(R.id.carListView);
//                carListView.setAdapter(adapter);
            } catch (Exception e) {
                Log.d(TAG, "doInBackground: parse CarID, JSON array creation failed");
            }

            return carList;
        }
    }
}
//http://ec2-18-219-38-137.us-east-2.compute.amazonaws.com:3000/getCarsByLocation?Lat=37.338832&Long=-121.895871