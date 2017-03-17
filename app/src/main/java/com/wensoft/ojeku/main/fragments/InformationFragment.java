package com.wensoft.ojeku.main.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.wensoft.ojeku.R;
import com.wensoft.ojeku.main.handle_information.InformationMakanActivity;
import com.wensoft.ojeku.main.handle_information.InformationMobilActivity;
import com.wensoft.ojeku.main.handle_information.InformationOjekActivity;

/**
 * Created by farhan on 2/19/17.
 */

public class InformationFragment extends Fragment {

    private LinearLayout linMobil, linMotor, linFood;


    public InformationFragment() {
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
        final View view = inflater.inflate(R.layout.fragment_information, container, false);

        linMobil = (LinearLayout)  view.findViewById(R.id.linLayMobil);
        linMotor = (LinearLayout)  view.findViewById(R.id.linLayOjek);
        linFood = (LinearLayout)  view.findViewById(R.id.linLayFood);

        linMobil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), InformationMobilActivity.class);
                InformationFragment.this.startActivity(intent);
            }
        });

        linMotor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), InformationOjekActivity.class);
                InformationFragment.this.startActivity(intent);
            }
        });

        linFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), InformationMakanActivity.class);
                InformationFragment.this.startActivity(intent);
            }
        });

        return view;
    }

}