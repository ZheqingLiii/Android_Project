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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.group5.android_project.MainActivity;
import com.group5.android_project.R;
import com.group5.android_project.Vehicle;
import com.group5.android_project.VehicleAdapter;
import com.group5.android_project.VehicleProfileActivity;

import java.util.ArrayList;

public class ProfileFragment extends Fragment {

    private static final String TAG = "ProfileFragment";
    public static ArrayList<Vehicle> vehicleList;
    FirebaseUser user;
    ImageView userImage;
    VehicleAdapter adapter;
    Button signOutButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUser();
        populateVehicleInfo();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_profile, container, false);
        signOutButton = v.findViewById(R.id.signOutButton);
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                firebaseAuth.signOut();
//                startActivity(new Intent(getActivity(), SplashActivity.class));
                getActivity().finish();
            }
        });
        return v;
    }

    public void setUser() {
        user = FirebaseAuth.getInstance().getCurrentUser();
//        user.setImageUrl("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQPR3s2vhWcPNvbnKdsKa4Fe1S-G2bu1qPKPOSg9cNfr-jCXH_pcQ");
    }

    public void populateVehicleInfo() {
        vehicleList = MainActivity.mainVehicleList;

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View v = getView();
        TextView firstName = v.findViewById(R.id.txtFirstName);
        userImage = v.findViewWithTag(R.id.userImageView);
        ListView vehicleListView = v.findViewById(R.id.lvVehicleListView);

        firstName.setText(user.getDisplayName());

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
