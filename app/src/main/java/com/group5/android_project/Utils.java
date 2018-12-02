package com.group5.android_project;

import android.location.Address;
import android.location.Geocoder;
import android.util.Log;
import android.view.View;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Locale;

public class Utils {
    private static final String TAG = "DownloadVehicleInfo";

    public static String downloadXML(String urlPath) {
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

    public static double[] addressToLatLong(String addressString, View view) {
        double[] latlong = new double[2];

        Geocoder geocoder = new Geocoder(view.getContext(), Locale.getDefault());
        try {
            List addressList = geocoder.getFromLocationName(addressString, 1);
            if (addressList != null && addressList.size() > 0) {
                Address address = (Address) addressList.get(0);
                latlong[0] = address.getLatitude();
                latlong[1] = address.getLongitude();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return latlong;
    }
}
