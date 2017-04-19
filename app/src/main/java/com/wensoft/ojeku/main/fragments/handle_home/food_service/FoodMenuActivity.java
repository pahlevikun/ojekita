package com.wensoft.ojeku.main.fragments.handle_home.food_service;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.wensoft.ojeku.R;
import com.wensoft.ojeku.adapter.FoodMenuAdapter;
import com.wensoft.ojeku.config.APIConfig;
import com.wensoft.ojeku.database.DatabaseHandler;
import com.wensoft.ojeku.pojo.FoodMenu;
import com.wensoft.ojeku.pojo.Profil;
import com.wensoft.ojeku.singleton.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FoodMenuActivity extends AppCompatActivity {

    private String id, name, token, akhir;
    private StringBuffer responseText;
    private ProgressDialog loading;
    private Button btPesan;
    private ListView listView;
    private ImageView imageView;
    private LinearLayout linearLayout;

    private ArrayList<Profil> valuesProfil;
    private DatabaseHandler dataSource;

    public FoodMenuAdapter adapter = null;
    private ArrayList<FoodMenu> dataList = new ArrayList<FoodMenu>();
    private ArrayList<String> menuPilih = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar(). setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        Intent ambil = getIntent();
        id = ambil.getStringExtra("id");
        name = ambil.getStringExtra("nama");
        setTitle("Menu "+name);

        dataSource = new DatabaseHandler(this);
        valuesProfil = (ArrayList<Profil>) dataSource.getAllProfils();

        listView = (ListView) findViewById(R.id.listViewFoodMenu);
        btPesan = (Button) findViewById(R.id.buttonPilihMenu);
        linearLayout = (LinearLayout) findViewById(R.id.linLayImageKosong);

        linearLayout.setVisibility(View.GONE);
        btPesan.setVisibility(View.GONE);

        makeJsonObjectRequest();
        checkButtonClick();
    }

    private void makeJsonObjectRequest() {
        loading = ProgressDialog.show(this,"Mohon Tunggu","Sedang memuat...",false,false);

        for(Profil profil : valuesProfil){
            token = profil.getToken();
        }
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, APIConfig.API_FOOD_MENU, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                hideDialog();
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        JSONArray dataArray = jObj.getJSONArray("data");
                        try {
                            dataList = new ArrayList<FoodMenu>();
                            for (int i=0; i<dataArray.length();i++){
                                JSONObject isi = dataArray.getJSONObject(i);
                                String id = isi.getString("id");
                                String restaurants_id = isi.getString("restaurants_id");
                                String name = isi.getString("name");
                                String price = isi.getString("price");
                                dataList.add(new FoodMenu(String.valueOf(i),id,restaurants_id,name,price,false));
                            }
                            int param = dataList.size();
                            if(param!=0){
                                linearLayout.setVisibility(View.GONE);
                                btPesan.setVisibility(View.VISIBLE);
                            }else{
                                linearLayout.setVisibility(View.VISIBLE);
                                btPesan.setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            Toast.makeText(FoodMenuActivity.this, e.toString(), Toast.LENGTH_LONG).show();
                        }
                    }else{
                        Toast.makeText(FoodMenuActivity.this, ""+error, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(FoodMenuActivity.this, ""+e, Toast.LENGTH_SHORT).show();
                }
                adapter = new FoodMenuAdapter(FoodMenuActivity.this, R.id.text, dataList);
                listView.setClickable(false);
                listView.setSelector(android.R.color.transparent);
                listView.setAdapter(adapter);
                listView.setFastScrollEnabled(true);
                listView.setTextFilterEnabled(true);
                listView.setScrollingCacheEnabled(true);
                adapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                hideDialog();
                Toast.makeText(FoodMenuActivity.this, ""+error, Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("restaurants_id", id);

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
        // Adding request to request queue
        AppController.getmInstance().addToRequestQueue(jsonObjReq);
    }

    private void checkButtonClick() {

        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        btPesan.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                responseText = new StringBuffer();
                responseText.append("Menu yang Anda pilih :\n");

                menuPilih.clear();

                ArrayList<FoodMenu> menuList = adapter.menuList;
                for(int i=0;i<menuList.size();i++){
                    FoodMenu menu = menuList.get(i);
                    if(menu.isSelected()){
                        responseText.append(menu.getNm()+", ");
                        menuPilih.add(menu.getIdMenu());
                    }
                }

                akhir = responseText.substring(0, responseText.length()-2) + ".";
                if(menuPilih.size()==0){
                    alert.setTitle("Peringatan");
                    alert.setMessage("Silahkan pilih menu terlebih dahulu!");
                    alert.setPositiveButton("Ya",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // TODO Auto-generated method stub
                                }
                            });
                    alert.show();
                }else if(menuPilih.size()!=0){
                    alert.setTitle("Peringatan");
                    alert.setMessage(akhir);
                    alert.setPositiveButton("Ya",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // TODO Auto-generated method stub
                                }
                            });
                    alert.setNegativeButton("Tidak",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // TODO Auto-generated method stub
                                }
                            });
                    alert.show();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id==android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void hideDialog() {
        if (loading.isShowing())
            loading.dismiss();
    }
}
