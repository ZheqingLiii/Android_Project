package com.group5.android_project;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;

public class CarShowPageActivity extends AppCompatActivity {
    int id;
    public WebView carImage;
    public TextView carPrice, carName, txtStartDate, txtEndDate, carDescription, contactButton;
    String description, ownerEmail;

    public static FirebaseUser mainUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.car_show_page);

        carImage = (WebView) findViewById(R.id.carImage);
        carPrice = (TextView) findViewById(R.id.carPrice);
        carName = (TextView) findViewById(R.id.carName);
        txtStartDate = (TextView) findViewById(R.id.txtStartDate);
        txtEndDate = (TextView) findViewById(R.id.txtEndDate);
        carDescription = (TextView) findViewById(R.id.carDescription);
        contactButton = (TextView) findViewById(R.id.contactButton);

        Intent i = getIntent();
        id = getIntent().getIntExtra("carId", 0);
        GetVehicleInfo getVehicleInfo = new GetVehicleInfo();
        String carUrl = "http://ec2-18-219-38-137.us-east-2.compute.amazonaws.com:3000/getCarInfo?CarID=" + id;
        getVehicleInfo.execute(carUrl);

        carName.setText(getIntent().getStringExtra("name").toString());
        txtStartDate.setText(getIntent().getStringExtra("startDate").toString());
        txtEndDate.setText(getIntent().getStringExtra("endDate").toString());

        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance();
        carPrice.setText(currencyFormatter.format(getIntent().getDoubleExtra("price", -1)) + "/day");

        carImage.getSettings().setLoadWithOverviewMode(true);
        carImage.getSettings().setUseWideViewPort(true);
        carImage.loadUrl(getIntent().getStringExtra("image"));
    }

    public class GetVehicleInfo extends AsyncTask<String, Void, String> {
        private static final String TAG = "GetCarInfo";

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            carDescription.setText(description);

            contactButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_EMAIL, new String[] {ownerEmail});
                    intent.putExtra(Intent.EXTRA_SUBJECT, "Interested in " + carName.getText());

                    startActivity(Intent.createChooser(intent, "Send Email"));
                }
            });

            Log.d(TAG, "onPostExecute parameter is " + s);
        }

        @Override
        protected String doInBackground(String... strings) {
            String carInfo = Utils.downloadXML(strings[0]);

            try {
                JSONArray carArray = null;
                carArray = new JSONArray(carInfo);
                JSONObject car = carArray.getJSONObject(0);

                description = car.getString("Detail");
                ownerEmail = car.getString("OwnerEmail");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return carInfo;
        }
    }
}
