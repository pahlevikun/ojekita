package com.wensoft.ojeku.main.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.wensoft.ojeku.R;
import com.wensoft.ojeku.main.fragments.handle_home.shuttle_service.CarServiceActivity;
import com.wensoft.ojeku.main.fragments.handle_home.food_service.FoodServiceActivity;
import com.wensoft.ojeku.main.fragments.handle_home.shuttle_service.RegularServiceActivity;

/**
 * Created by farhan on 2/19/17.
 */

public class HomeFragment extends Fragment {

    private LinearLayout linLayMotor, linLayMobil, linLayFood;
    private Boolean gps_enabled = false;
    private LocationManager mLocationManager;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_home, container, false);

        final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        mLocationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        try {
            gps_enabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            if(!gps_enabled) {
                alert.setTitle("Peringatan!");
                alert.setMessage("GPS Anda sedang tidak aktif!\nAktifkan GPS?");
                alert.setPositiveButton("Ya",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                                Intent myIntent = new Intent( Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(myIntent);
                            }
                        });
                alert.show();
            }
        } catch(Exception ex) {

        }

        linLayMotor = (LinearLayout) view.findViewById(R.id.linMotor);
        linLayMobil = (LinearLayout) view.findViewById(R.id.linMobil);
        linLayFood = (LinearLayout) view.findViewById(R.id.linFood);

        linLayFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), FoodServiceActivity.class);
                startActivity(intent);
            }
        });

        linLayMotor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), RegularServiceActivity.class);
                startActivity(intent);
            }
        });

        linLayMobil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CarServiceActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

}