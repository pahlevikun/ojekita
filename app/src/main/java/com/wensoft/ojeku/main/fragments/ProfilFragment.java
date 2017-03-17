package com.wensoft.ojeku.main.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.wensoft.ojeku.R;
import com.wensoft.ojeku.database.DatabaseHandler;
import com.wensoft.ojeku.main.fragments.handle_profil.ChangeEmailActivity;
import com.wensoft.ojeku.main.fragments.handle_profil.ChangeProfilActivity;
import com.wensoft.ojeku.main.handle_login.LandingActivity;
import com.wensoft.ojeku.pojo.FCM;
import com.wensoft.ojeku.pojo.Profil;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by farhan on 2/19/17.
 */

public class ProfilFragment extends Fragment {

    private TextView tvLogout,tvPassword, tvNama, tvEmail, tvPhone;
    private Button btProfil;
    private DatabaseHandler dataSource;
    private ArrayList<Profil> valuesProfil;
    private String nama, email, telepon;

    public ProfilFragment() {
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
        View view =  inflater.inflate(R.layout.fragment_profile, container, false);

        dataSource = new DatabaseHandler(getActivity());
        valuesProfil = (ArrayList<Profil>) dataSource.getAllProfils();
        for(Profil profil : valuesProfil){
            nama = profil.getUsername();
            email = profil.getEmail();
            telepon = profil.getTelepon();
        }

        tvNama = (TextView) view.findViewById(R.id.textNamaUser);
        tvEmail = (TextView) view.findViewById(R.id.textEmailUser);
        tvPhone = (TextView) view.findViewById(R.id.textPhoneUser);
        tvLogout = (TextView) view.findViewById(R.id.textLogOut);
        tvPassword = (TextView) view.findViewById(R.id.textSandi);
        btProfil = (Button) view.findViewById(R.id.buttonUbahProfil);

        tvNama.setText(nama);
        tvEmail.setText(email);
        tvPhone.setText(telepon);

        btProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ChangeProfilActivity.class);
                startActivity(intent);
            }
        });

        tvPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ChangeEmailActivity.class);
                startActivity(intent);
            }
        });

        tvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataSource.hapusDbaseProfil();
                dataSource.close();

                SharedPreferences sharedPreferences =  getActivity().getSharedPreferences("DATA",MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("session",false);
                editor.commit();

                dataSource.hapusDbaseProfil();

                Intent intent = new Intent(getActivity(), LandingActivity.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                getActivity().finish();
            }
        });

        return view;
    }

}