package com.group5.android_project.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.group5.android_project.R;
import com.group5.android_project.User;
import com.group5.android_project.VehicleAdapter;
import com.group5.android_project.Vehicle;
import com.group5.android_project.VehicleProfileActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProfileFragment extends Fragment {

    private static final String TAG = "ProfileFragment";
    public static ArrayList<Vehicle> vehicleList;
    public User user;
    ImageView userImage;
    VehicleAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        populateUserInfo();
        populateVehicleInfo();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_profile, container, false);
        return v;
    }

    public void populateUserInfo() {

        //sample
        String firstName = "FirstName";
        String lastName = "LastName";
        user = new User(firstName, lastName);
        user.setImageUrl("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQPR3s2vhWcPNvbnKdsKa4Fe1S-G2bu1qPKPOSg9cNfr-jCXH_pcQ");

    }

    public void populateVehicleInfo() {

        //sample
        vehicleList = new ArrayList<Vehicle>();
        Vehicle car1 = new Vehicle("Toyota Camry", "2018", "San Jose",
                "22 MPG, Regular Gas, 2 doors, 4 seats, Automatic transmission, Audio input, Convertible, USB input, Heated seats");
        car1.setStartDate("01/01/2018");
        car1.setEndDate("05/01/2018");
        car1.setStartTime("13:30");
        car1.setEndTime("22:30");
        car1.setImageUrl("https://crdms.images.consumerreports.org/c_lfill,w_480/prod/cars/cr/model-years/8073-2018-toyota-camry");
        vehicleList.add(car1);

        Vehicle car2 = new Vehicle("Toyota Camry", "2014", "Santa Clara",
                "22 MPG, Regular Gas, 2 doors, 4 seats, Automatic transmission, Audio input, Convertible, USB input, Heated seats");
        car2.setStartDate("02/02/2018");
        car2.setEndDate("02/02/2019");
        car2.setStartTime("09:00");
        car2.setEndTime("23:30");
        car2.setImageUrl("https://crdms.images.consumerreports.org/c_lfill,w_480/prod/cars/cr/model-years/8073-2018-toyota-camry");
        vehicleList.add(car2);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View v = getView();
        TextView firstName = v.findViewById(R.id.txtFirstName);
        userImage = v.findViewWithTag(R.id.userImageView);
        ListView vehicleListView = v.findViewById(R.id.lvVehicleListView);

        firstName.setText(user.getFirstName());
        try {
            //TODO
            Picasso.with(v.getContext()).load(user.getImageUrl()).into(userImage);
        } catch (Exception e) {
            Log.d(TAG, " user image null");
        }

        try {
            adapter = new VehicleAdapter(getActivity().getApplicationContext(), R.layout.list_vehicle, vehicleList);
            vehicleListView.setAdapter(adapter);
        } catch (Exception e) {
            Log.d(TAG, " adapter is null");
        }


        vehicleListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Vehicle curVehicle = (Vehicle) parent.getAdapter().getItem(position);

                        Intent i = new Intent(getActivity(), VehicleProfileActivity.class);
                        i.putExtra("vehicle", curVehicle);
                        i.putExtra("vehicleIndex", position);
                        startActivity(i);

                    }
                }
        );

    }

    @Override
    public void onResume() {
        super.onResume();
        //refresh changed vehicle data
        adapter.notifyDataSetChanged();
    }

}
