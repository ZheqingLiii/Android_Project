package com.group5.android_project;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class carSearchAdapter extends ArrayAdapter<String> {
    private final Activity context;
    private final Integer[] ids;
    private final double[] distances;

    private static final String TAG = "DownloadVehicleInfo";

    public carSearchAdapter(Activity context, Integer[] ids, double[] distances) {
        super(context, R.layout.car_search_list_item);

        this.context = context;
        this.ids = ids;
        this.distances = distances;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View row = inflater.inflate(R.layout.car_search_list_item, null, true);

        ImageView carImage = (ImageView) row.findViewById(R.id.carImage);
        TextView carName = (TextView) row.findViewById(R.id.carName);
        TextView carPrice = (TextView) row.findViewById(R.id.carPrice);

        StringBuilder xmlResult = new StringBuilder();
        try {
            URL url = new URL("http://ec2-18-219-38-137.us-east-2.compute.amazonaws.com:3000/getCarInfo?CarID=" + ids[position]);
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

            String result = xmlResult.toString();

        } catch (MalformedURLException e) {
            Log.e(TAG, "downloadXML: Invalid URL" + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "downloadXML: IOException reading data: " + e.getMessage());
        } catch (SecurityException e) {
            Log.e(TAG, "downloadXML: Security exception, " + e.getMessage());
            //e.printStackTrace();
        }

        carName.setText(xmlResult);

        return row;
    }
}
