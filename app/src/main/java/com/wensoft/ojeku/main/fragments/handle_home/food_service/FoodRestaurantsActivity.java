package com.wensoft.ojeku.main.fragments.handle_home.food_service;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
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
import com.wensoft.ojeku.adapter.FoodBannerAdapter;
import com.wensoft.ojeku.adapter.FoodRestaurantsAdapter;
import com.wensoft.ojeku.config.APIConfig;
import com.wensoft.ojeku.customlibrary.NonScrollListView;
import com.wensoft.ojeku.database.DatabaseHandler;
import com.wensoft.ojeku.pojo.FoodBanner;
import com.wensoft.ojeku.pojo.Profil;
import com.wensoft.ojeku.singleton.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FoodRestaurantsActivity extends AppCompatActivity {


    private ProgressDialog loading;
    private ArrayList<Profil> valuesProfil;
    private List<FoodBanner> foodBannerList = new ArrayList<FoodBanner>();
    private DatabaseHandler dataSource;
    private String token;
    private ListView listView;
    private FoodRestaurantsAdapter adapterListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_restaurants);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        setTitle("Daftar Restoran");

        dataSource = new DatabaseHandler(this);
        valuesProfil = (ArrayList<Profil>) dataSource.getAllProfils();

        listView = (ListView) findViewById(R.id.listViewFoodRestaurants);
        adapterListView = new FoodRestaurantsAdapter(this, foodBannerList);
        listView.setAdapter(adapterListView);

        makeRequest();


    }

    private void makeRequest() {

        loading = ProgressDialog.show(this,"Mohon Tunggu","Sedang memuat...",false,false);

        for (Profil profil : valuesProfil){
            token = profil.getToken();
        }

        StringRequest jsonObjReq = new StringRequest(Request.Method.GET, APIConfig.API_GET_RESTAURANTS, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                hideDialog();
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
                            if(is_banner.equals("0")){
                                foodBannerList.add(new FoodBanner(String.valueOf(i),id,category_id,name,open_time,
                                        close_time,latitude,longitude,is_banner,""));
                            }
                        }
                    }
                } catch (JSONException e) {
                    Toast.makeText(FoodRestaurantsActivity.this, ""+e, Toast.LENGTH_SHORT).show();
                    hideDialog();
                }
                adapterListView.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                hideDialog();
                Toast.makeText(FoodRestaurantsActivity.this, ""+error, Toast.LENGTH_SHORT).show();
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
