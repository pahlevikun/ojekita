package com.wensoft.ojeku.main.fragments.handle_home.food_service;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
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
import com.wensoft.ojeku.adapter.FoodCategoryAdapter;
import com.wensoft.ojeku.adapter.FoodCategoryListAdapter;
import com.wensoft.ojeku.adapter.FoodMenuAdapter;
import com.wensoft.ojeku.config.APIConfig;
import com.wensoft.ojeku.database.DatabaseHandler;
import com.wensoft.ojeku.pojo.FoodCategoryList;
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

    private String id, name, token;
    private ProgressDialog loading;
    private Button btPesan;
    public FoodMenuAdapter adapter;
    private ListView listView;
    private List<FoodMenu> dataList = new ArrayList<FoodMenu>();
    private ArrayList<Profil> valuesProfil;
    private DatabaseHandler dataSource;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        Intent ambil = getIntent();
        id = ambil.getStringExtra("id");
        name = ambil.getStringExtra("name");
        setTitle("Menu "+name);


        dataSource = new DatabaseHandler(this);
        valuesProfil = (ArrayList<Profil>) dataSource.getAllProfils();

        listView = (ListView) findViewById(R.id.listViewFoodMenu);
        btPesan = (Button) findViewById(R.id.buttonPesan);

        adapter = new FoodMenuAdapter(this, dataList);
        listView.setAdapter(adapter);
        makeJsonObjectRequest();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(FoodMenuActivity.this, ""+dataList.get(position).getNm(), Toast.LENGTH_SHORT).show();
            }
        });
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
                            for (int i=0; i<dataArray.length();i++){
                                JSONObject isi = dataArray.getJSONObject(i);
                                String idCat = isi.getString("id");
                                String idRes = isi.getString("restaurants_id");
                                String name = isi.getString("name");
                                String price = isi.getString("price");
                                dataList.add(new FoodMenu(String.valueOf(i),idCat,idRes,name,price,"0"));
                            }
                        } catch (JSONException e) {
                            Toast.makeText(FoodMenuActivity.this, e.toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                } catch (JSONException e) {
                    Toast.makeText(FoodMenuActivity.this, ""+e, Toast.LENGTH_SHORT).show();
                }
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
        // Adding request to request queue
        AppController.getmInstance().addToRequestQueue(jsonObjReq);
    }

    public void refreshList(){
        adapter.notifyDataSetChanged();
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
