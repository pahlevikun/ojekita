package com.wensoft.ojeku.main.fragments;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
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