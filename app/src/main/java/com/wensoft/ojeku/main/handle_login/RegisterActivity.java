package com.wensoft.ojeku.main.handle_login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.iid.FirebaseInstanceId;
import com.wensoft.ojeku.R;
import com.wensoft.ojeku.config.APIConfig;
import com.wensoft.ojeku.database.DatabaseHandler;
import com.wensoft.ojeku.main.MainActivity;
import com.wensoft.ojeku.pojo.FCM;
import com.wensoft.ojeku.pojo.Profil;
import com.wensoft.ojeku.singleton.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private EditText etName, etPhone, etEmail, etPassword;
    private Button btRegister;
    private String editName, editPhone, editEmail, editPassword;
    private ProgressDialog loading;
    private String token, username, mail, created_at, token_fcm, phone;
    private int user_id;
    private Date cDate = new Date();
    private DatabaseHandler dataSource;
    private ArrayList<FCM> valuesFCM;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etName = (EditText) findViewById(R.id.editTextRegisterName);
        etPhone = (EditText) findViewById(R.id.editTextRegisterPhone);
        etEmail = (EditText) findViewById(R.id.editTextRegisterEmail);
        etPassword = (EditText) findViewById(R.id.editTextRegisterPassword);
        btRegister = (Button) findViewById(R.id.buttonRegister);


        dataSource = new DatabaseHandler(this);

        valuesFCM = (ArrayList<FCM>) dataSource.getAllFCMs();
        for(FCM fcm : valuesFCM){
            token_fcm = fcm.getTokenFCM();
        }

        created_at = new SimpleDateFormat("yyyy-MM-dd").format(cDate);

        btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editName = etName.getText().toString().trim();
                editPhone = etPhone.getText().toString().trim();
                editEmail = etEmail.getText().toString().trim();
                editPassword = etPassword.getText().toString().trim();

                if(editPassword.length()<6){
                    Toast.makeText(RegisterActivity.this, "Password minimal 6 Karakter!", Toast.LENGTH_LONG).show();
                }
                if (editName.isEmpty() || (editPhone.isEmpty()&&editPassword.length()<6) || editEmail.isEmpty() || editPassword.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Kolom yang tersedia belum diisi dengan benar!", Toast.LENGTH_LONG).show();
                } else {
                    registerUser(editName,editEmail,editPassword,editPhone,token_fcm);
                }
            }
        });
    }

    private void registerUser (final String nama, final String email, final String password, final String telp, final String tokens) {
        String tag_string_req = "req_register";
        loading = ProgressDialog.show(this,"Mohon Tunggu","Sedang login...",false,false);

        StringRequest strReq = new StringRequest(Request.Method.POST, APIConfig.API_REGISTER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    if (!jObj.has("error")) {
                        JSONObject data = jObj.getJSONObject("user");
                        token = data.getString("token");
                        user_id = data.getInt("id");
                        username = data.getString("name");
                        mail = data.getString("email");
                        phone = data.getString("phone");
                        created_at = data.getString("created_at");

                        dataSource.tambahProfil(new Profil(user_id,username,mail,token,phone,created_at));
                        SharedPreferences sharedPreferences =  getSharedPreferences("DATA",MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("session",true);
                        editor.commit();

                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Registrasi Gagal!", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(getApplicationContext(), "Gagal Terhubung ke Server", Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", nama);
                params.put("email", email);
                params.put("password", password);
                params.put("phone", telp);
                params.put("fcm_token", "TOKENS");

                return params;
            }
        };

        int socketTimeout = 40000; // 40 seconds. You can change it
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        strReq.setRetryPolicy(policy);
        AppController.getmInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void hideDialog() {
        if (loading.isShowing())
            loading.dismiss();
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

}
