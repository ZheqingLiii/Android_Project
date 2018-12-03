package com.group5.android_project;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.ArrayList;

public class CarSearchAdapter extends ArrayAdapter<String> {
    private final Activity context;
    private final ArrayList<Integer> ids;
    private final ArrayList<Double> distances, prices;
    private final ArrayList<String> names, imgURLs;

    private static final String TAG = "DownloadVehicleInfo";

    public CarSearchAdapter(Activity context, ArrayList<Integer> ids, ArrayList<Double> distances,
                            ArrayList<String> names, ArrayList<Double> prices, ArrayList<String> imgURLs) {
        super(context, R.layout.car_search_list_item);

        this.context = context;
        this.ids = ids;
        this.distances = distances;
        this.names = names;
        this.prices = prices;
        this.imgURLs = imgURLs;
    }

    @Override
    public int getCount() {
        return ids.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View row = inflater.inflate(R.layout.car_search_list_item, null, true);

        WebView carImage = row.findViewById(R.id.carImage);
        TextView carName = row.findViewById(R.id.carName);
        TextView carPrice = row.findViewById(R.id.carPrice);
        TextView carDistance = row.findViewById(R.id.distanceTextView);

        String url = "http://18.219.38.137/home/team5/ftp/files/CarID" + ids.get(position).toString() + ".jpg";
        carImage.getSettings().setLoadWithOverviewMode(true);
        carImage.getSettings().setUseWideViewPort(true);
        carImage.loadUrl(url);

        carName.setText(names.get(position));

        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance();
        carPrice.setText(currencyFormatter.format(prices.get(position)) + "/day");

        double distance = Math.round(distances.get(position) * 10) / 10.0;
        carDistance.setText(distance + "");

        return row;
    }
}
