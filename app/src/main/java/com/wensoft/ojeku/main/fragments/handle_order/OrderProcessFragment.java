package com.wensoft.ojeku.main.fragments.handle_order;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import com.wensoft.ojeku.adapter.OrderProcessAdapter;
import com.wensoft.ojeku.config.APIConfig;
import com.wensoft.ojeku.database.DatabaseHandler;
import com.wensoft.ojeku.pojo.OrderComplete;
import com.wensoft.ojeku.pojo.Profil;
import com.wensoft.ojeku.singleton.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by farhan on 3/4/17.
 */

public class OrderProcessFragment extends Fragment {

    private ListView listView;
    private ProgressDialog loading;
    private String token;
    private OrderProcessAdapter adapter;
    private List<OrderComplete> dataList = new ArrayList<OrderComplete>();
    private ArrayList<Profil> valuesProfil;
    private DatabaseHandler dataSource;
    private ImageView imageView;
    private LinearLayout kosong;

    public OrderProcessFragment() {
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


        dataSource = new DatabaseHandler(getActivity());
        valuesProfil = (ArrayList<Profil>) dataSource.getAllProfils();
        View view =  inflater.inflate(R.layout.fragment_order_complete, container, false);

        listView = (ListView) view.findViewById(R.id.listOrderFinish);

        adapter = new OrderProcessAdapter(getActivity(), dataList);
        dataList.clear();
        listView.setAdapter(adapter);
        makeJsonObjectRequest();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.notifyDataSetChanged();
            }
        });

        return view;
    }

    private void makeJsonObjectRequest() {


        for(Profil profil : valuesProfil){
            token = profil.getToken();
        }
        StringRequest jsonObjReq = new StringRequest(Request.Method.GET, APIConfig.API_HISTORY_ONGOING, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        JSONArray dataArray = jObj.getJSONArray("data");
                        try {
                            for (int i=0; i<dataArray.length();i++){
                                JSONObject isi = dataArray.getJSONObject(i);
                                String invoice = isi.getString("invoice_number");
                                String alamat = isi.getString("alamat_tujuan");
                                String order_type = isi.getString("order_type");
                                dataList.add(new OrderComplete(String.valueOf(i),invoice,order_type,alamat));
                            }
                        } catch (JSONException e) {
                            Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                } catch (JSONException e) {
                    Toast.makeText(getActivity(), ""+e, Toast.LENGTH_SHORT).show();
                }
                adapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), ""+error, Toast.LENGTH_SHORT).show();
            }
        }){
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



    private void hideDialog() {
        if (loading.isShowing())
            loading.dismiss();
    }

}