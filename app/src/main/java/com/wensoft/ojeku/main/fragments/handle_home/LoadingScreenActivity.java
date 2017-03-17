package com.wensoft.ojeku.main.fragments.handle_home;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.wensoft.ojeku.R;
import com.wensoft.ojeku.config.APIConfig;
import com.wensoft.ojeku.database.DatabaseHandler;
import com.wensoft.ojeku.main.MainActivity;
import com.wensoft.ojeku.main.SplashActivity;
import com.wensoft.ojeku.main.fragments.handle_home.shuttle_service.RegularServiceActivity;
import com.wensoft.ojeku.main.handle_login.LandingActivity;
import com.wensoft.ojeku.pojo.Profil;
import com.wensoft.ojeku.singleton.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class LoadingScreenActivity extends AppCompatActivity {


    private ProgressDialog loading;
    private ArrayList<Profil> valuesProfil;
    private DatabaseHandler dataSource;
    private String token;


    public static final int HASIL = 2;

    private Button btCancel;
    private int batas = 0;
    private int delay = 3500;
    private String order_id;

    private Handler handler;
    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_screen);

        dataSource = new DatabaseHandler(this);
        valuesProfil = (ArrayList<Profil>) dataSource.getAllProfils();

        btCancel = (Button)  findViewById(R.id.buttonCancel);

        Intent ambil = getIntent();
        order_id = ambil.getStringExtra("order_id");

        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancel(order_id);
            }
        });

        perantaraCheck(order_id);

    }

    private void perantaraCheck(final String order_id){

        new Handler().postDelayed(new Thread() {
            @Override
            public void run() {
                check(order_id);
            }
        }, delay);
    }

    private void check(final String order_id) {
        
        for (Profil profil : valuesProfil){
            token = profil.getToken();
        }

        batas = batas + 1;
        if(batas==25){
            cancel(order_id);
        }else {

            StringRequest jsonObjReq = new StringRequest(Request.Method.POST, APIConfig.API_CHECK, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    //hideDialog();
                    try {
                        JSONObject jObj = new JSONObject(response);
                        boolean error = jObj.getBoolean("error");
                        if (!error) {
                            String pesan = jObj.getString("msg");
                            if (/*batas<=7&&!*/pesan.equals("Belum ada tanggapan dari driver")) {
                                perantaraCheck(order_id);
                            } else {
                                JSONObject data = jObj.getJSONObject("data");
                                String nama_driver = data.getString("nama_driver");
                                String plat_nomor = data.getString("plat_nomor");
                                String telepon = data.getString("telepon");
                                String avatar = data.getString("avatar");
                                //double driverLat = data.getDouble("latitude");
                                //double driverLng = data.getDouble("longitude");
                                Intent resultIntent = new Intent();
                                resultIntent.putExtra("nama_driver", nama_driver);
                                resultIntent.putExtra("plat_nomor", plat_nomor);
                                resultIntent.putExtra("telepon", telepon);
                                resultIntent.putExtra("avatar", avatar);
                                resultIntent.putExtra("order_id", order_id);
                                //resultIntent.putExtra("driverLat", driverLat);
                                //resultIntent.putExtra("driverLng", driverLng);
                                setResult(LoadingScreenActivity.RESULT_OK, resultIntent);
                                finish();
                            }
                        }
                    } catch (JSONException e) {
                        Toast.makeText(LoadingScreenActivity.this, "" + e, Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    //hideDialog();
                    Toast.makeText(LoadingScreenActivity.this, "" + error, Toast.LENGTH_SHORT).show();
                }
            }) {
                //Untuk post data menggunakan volley
                //Data yang dikirim adalah email dan password
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("order_id", order_id);
                    return params;
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    String bearer = "Bearer " + token;
                    Map<String, String> headersSys = super.getHeaders();
                    Map<String, String> headers = new HashMap<String, String>();
                    headersSys.remove("Authorization");
                    headers.put("Authorization", bearer);
                    headers.putAll(headersSys);
                    return headers;
                }
            };

            int socketTimeout = 40000; // 40 seconds. You can change it
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            jsonObjReq.setRetryPolicy(policy);
            AppController.getmInstance().addToRequestQueue(jsonObjReq);
        }
    }


    private void cancel(final String order_id) {

        loading = ProgressDialog.show(this,"Mohon Tunggu","Sedang membatalkan...",false,false);

        for (Profil profil : valuesProfil){
            token = profil.getToken();
        }

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, APIConfig.API_ORDER_CANCEL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                hideDialog();
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        Intent intent = new Intent(LoadingScreenActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                } catch (JSONException e) {
                    Toast.makeText(LoadingScreenActivity.this, ""+e, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                hideDialog();
                Toast.makeText(LoadingScreenActivity.this, ""+error, Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", order_id);
                return params;
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                String bearer = "Bearer " + token;
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", bearer);
                return headers;
            }
        };

        int socketTimeout = 40000; // 40 seconds. You can change it
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjReq.setRetryPolicy(policy);
        AppController.getmInstance().addToRequestQueue(jsonObjReq);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if(id==android.R.id.home) {

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

    }

    private void hideDialog() {
        if (loading.isShowing())
            loading.dismiss();
    }
}
