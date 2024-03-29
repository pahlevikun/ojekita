package com.wensoft.ojeku.main.fragments.handle_home.food_service;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.wensoft.ojeku.adapter.FoodCategoryAdapter;
import com.wensoft.ojeku.customlibrary.NonScrollGridView;
import com.wensoft.ojeku.R;
import com.wensoft.ojeku.adapter.FoodBannerAdapter;
import com.wensoft.ojeku.customlibrary.NonScrollListView;
import com.wensoft.ojeku.config.APIConfig;
import com.wensoft.ojeku.database.DatabaseHandler;
import com.wensoft.ojeku.pojo.FoodBanner;
import com.wensoft.ojeku.pojo.FoodCategory;
import com.wensoft.ojeku.pojo.Profil;
import com.wensoft.ojeku.singleton.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FoodServiceActivity extends AppCompatActivity {

    private LinearLayout linLayResto, linLayKategori;
    private NonScrollListView listView;
    private NonScrollGridView gridView;

    private ProgressDialog loading;
    private ArrayList<Profil> valuesProfil;
    private DatabaseHandler dataSource;
    private String token;

    private TextView tvRestoran, tvKategori;


    public FoodBannerAdapter adapterListView;
    public FoodCategoryAdapter adapterGridview;
    private List<FoodBanner> foodBannerList = new ArrayList<FoodBanner>();
    private List<FoodCategory> foodCategoryList = new ArrayList<FoodCategory>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_service);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        setTitle("Kita Antar");

        tvRestoran = (TextView) findViewById(R.id.tvGetRestaurants);
        tvKategori = (TextView) findViewById(R.id.tvGetCategory);
        listView = (NonScrollListView) findViewById(R.id.listViewBannerMakanan);
        gridView = (NonScrollGridView) findViewById(R.id.gridViewBannerKategori);
        listView.setClickable(true);
        gridView.setClickable(true);
        adapterListView = new FoodBannerAdapter(this, foodBannerList);
        adapterGridview = new FoodCategoryAdapter(this, foodCategoryList);
        listView.setAdapter(adapterListView);
        gridView.setAdapter(adapterGridview);

        listView.setScrollContainer(false);
        gridView.setScrollContainer(false);

        dataSource = new DatabaseHandler(this);
        valuesProfil = (ArrayList<Profil>) dataSource.getAllProfils();

        makeRequest();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(FoodServiceActivity.this, FoodMenuWebActivity.class);
                intent.putExtra("id",foodBannerList.get(position).getId());
                intent.putExtra("nama",foodBannerList.get(position).getName());
                intent.putExtra("latitude",foodBannerList.get(position).getLatitude());
                intent.putExtra("longitude",foodBannerList.get(position).getLongitude());
                intent.putExtra("alamat",foodBannerList.get(position).getAlamat());
                startActivity(intent);
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(FoodServiceActivity.this, FoodRestaurantsCategoryActivity.class);
                intent.putExtra("id",foodCategoryList.get(position).getIdCat());
                intent.putExtra("nama",foodCategoryList.get(position).getName());
                startActivity(intent);
            }
        });

        tvRestoran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FoodServiceActivity.this, FoodRestaurantsActivity.class);
                startActivity(intent);
            }
        });
        tvKategori.setVisibility(View.GONE);
    }

    private void makeRequest() {

        loading = ProgressDialog.show(this,"Mohon Tunggu","Sedang memuat...",false,false);

        for (Profil profil : valuesProfil){
            token = profil.getToken();
        }

        StringRequest jsonObjReq = new StringRequest(Request.Method.GET, APIConfig.API_GET_RESTAURANTS, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        JSONArray array = jObj.getJSONArray("data");
                        for(int i = 0; i < array.length(); i++){
                            JSONObject data = array.getJSONObject(i);
                            String id = data.getString("id");
                            String category_id = data.getString("category_id");
                            String name = data.getString("name");
                            String open_time = data.getString("open_time");
                            String close_time = data.getString("close_time");
                            Double latitude = data.getDouble("latitude");
                            Double longitude = data.getDouble("longitude");
                            String is_banner = data.getString("is_banner");
                            String image = data.getString("image");
                            String alamat = data.getString("alamat");
                            if(is_banner.equals("1")){
                                foodBannerList.add(new FoodBanner(String.valueOf(i),id,category_id,name,open_time,
                                        close_time,latitude,longitude,is_banner,image,alamat));
                            }
                        }
                        getKategori();
                    }
                } catch (JSONException e) {
                    Toast.makeText(FoodServiceActivity.this, ""+e, Toast.LENGTH_SHORT).show();
                    hideDialog();
                }
                adapterListView.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                hideDialog();
                Toast.makeText(FoodServiceActivity.this, ""+error, Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
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

    private void getKategori() {

        for(Profil profil : valuesProfil){
            token = profil.getToken();
        }
        StringRequest jsonObjReq = new StringRequest(Request.Method.GET, APIConfig.API_FOOD_CATEGORY, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                hideDialog();
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        JSONArray dataArray = jObj.getJSONArray("data");
                        try {
                            for (int i=0; i<dataArray.length();i++){
                                JSONObject isi = dataArray.getJSONObject(i);
                                String id = isi.getString("id");
                                String name = isi.getString("name");
                                String banner = isi.getString("banner");
                                String is_featured = isi.getString("is_featured");
                                Log.d("HASIL 1",banner+"");
                                foodCategoryList.add(new FoodCategory(String.valueOf(i),id,name,banner,is_featured));
                            }
                        } catch (JSONException e) {
                            Toast.makeText(FoodServiceActivity.this, e.toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                } catch (JSONException e) {
                    Toast.makeText(FoodServiceActivity.this, ""+e, Toast.LENGTH_SHORT).show();
                }
                adapterGridview.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                hideDialog();
                Toast.makeText(FoodServiceActivity.this, ""+error, Toast.LENGTH_SHORT).show();
            }
        }){
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
