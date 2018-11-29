package com.group5.android_project;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class APIUtils extends AsyncTask<String, Void, String> {
    private static final String TAG = "DownloadVehicleInfo";
    String result;

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        this.result = s;
        Log.d(TAG, "onPostExecute parameter is " + s);
    }

    @Override
    protected String doInBackground(String... strings) {
        Log.d(TAG, "do in background");
        String vehicleInfo = downloadXML(strings[0]);
        if (vehicleInfo == null) {
            Log.e(TAG, "doInBackground, error downloading");
        }

        return vehicleInfo;
    }

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
}
