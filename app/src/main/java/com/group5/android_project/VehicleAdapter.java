package com.group5.android_project;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;


public class VehicleAdapter<T extends Vehicle> extends ArrayAdapter {

    private static final String TAG = "VehicleAdapter";
    private final int layoutResource;
    private final LayoutInflater layoutInflator;
    private final Context context;
    private List<T> vehicleList;


    public VehicleAdapter(@NonNull Context context, @LayoutRes int resource, List<T> vList) {
        super(context, R.layout.list_vehicle, vList);
        this.context = context;
        this.layoutResource = resource;
        this.layoutInflator = LayoutInflater.from(context);
        this.vehicleList = vList;
    }

    @Override
    public int getCount() {
        return vehicleList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;

        if (convertView == null) {
            Log.d(TAG, "getview: called with null convertView");
            convertView = layoutInflator.inflate(layoutResource, parent, false);

            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            Log.d(TAG, "getView: provided a convertView");
            viewHolder = (ViewHolder) convertView.getTag();
        }

        T curr = vehicleList.get(position);

        viewHolder.tvModel.setText(curr.getModel());
        viewHolder.tvYear.setText(curr.getYear());
        viewHolder.tvCity.setText(curr.getCity());
        viewHolder.tvDetail.setText(curr.getDetail());

        Picasso.with(context).load(curr.getImageUrl()).into(viewHolder.tvImage);


        return convertView;
    }

    private class ViewHolder {
        final TextView tvModel;
        final TextView tvYear;
        final TextView tvCity;
        final TextView tvDetail;
        final ImageView tvImage;

        ViewHolder(View v) {
            this.tvModel = v.findViewById(R.id.txtModel);
            this.tvYear = v.findViewById(R.id.txtYear);
            this.tvCity = v.findViewById(R.id.txtCity);
            this.tvDetail = v.findViewById(R.id.txtDetail);
            this.tvImage = v.findViewById(R.id.imgView);
        }
    }
}
