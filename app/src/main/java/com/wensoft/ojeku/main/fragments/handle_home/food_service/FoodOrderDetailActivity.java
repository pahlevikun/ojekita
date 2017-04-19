package com.wensoft.ojeku.main.fragments.handle_home.food_service;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Geocoder;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;
import com.wensoft.ojeku.R;
import com.wensoft.ojeku.config.APIConfig;
import com.wensoft.ojeku.config.GPSTracker;
import com.wensoft.ojeku.database.DatabaseHandler;
import com.wensoft.ojeku.main.fragments.handle_home.LoadingScreenActivity;
import com.wensoft.ojeku.main.fragments.handle_home.LoadingScreenFoodActivity;
import com.wensoft.ojeku.main.fragments.handle_home.shuttle_service.CarServiceActivity;
import com.wensoft.ojeku.main.fragments.handle_home.shuttle_service.RegularServiceActivity;
import com.wensoft.ojeku.pojo.Cart;
import com.wensoft.ojeku.pojo.Profil;
import com.wensoft.ojeku.singleton.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FoodOrderDetailActivity extends AppCompatActivity {

    private String name,id,token,alamatTujuan, alamatJemput, noteTujuan,noteFood,alamat, message;
    private StringBuffer sMenu,sHarga;
    private float harga, jarakKM, kmInDec;
    private double latJemput, lngJemput, latTujuan, lngTujuan,lat,lng;
    private LatLng latLngJemput, latLngTujuan;
    private int hargaMenu = 0, hargaTotal = 0;

    private TextView tvMenu, tvHarga, tvBiayaOjek, tvBiayaMenu, tvBiayaTotal;
    private ArrayList<Cart> foodCart = new ArrayList<Cart>();
    private EditText etTujuan, etNoteTujuan,etNoteFood;
    private Button btPesan;

    private ProgressDialog loading;
    private ArrayList<Profil> valuesProfil;
    private DatabaseHandler dataSource;
    private GPSTracker gps;
    private Geocoder geocoder;

    private static final int DESTINATION = 2;
    private static final int OUTPUT = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_order_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar(). setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        tvMenu = (TextView) findViewById(R.id.textViewMenu);
        tvHarga = (TextView) findViewById(R.id.textViewHarga);
        tvBiayaOjek = (TextView) findViewById(R.id.textViewTotalAntar);
        tvBiayaMenu = (TextView) findViewById(R.id.textViewTotalHarga);
        tvBiayaTotal = (TextView) findViewById(R.id.textViewTotalBayar);
        etTujuan = (EditText) findViewById(R.id.etTujuan);
        etNoteTujuan = (EditText) findViewById(R.id.etNoteTujuan);
        btPesan = (Button) findViewById(R.id.buttonSelesai);
        etNoteFood = (EditText) findViewById(R.id.etNoteFood);

        dataSource = new DatabaseHandler(this);
        valuesProfil = (ArrayList<Profil>) dataSource.getAllProfils();

        Intent ambil = getIntent();
        foodCart = (ArrayList<Cart>)getIntent().getExtras().getSerializable("order");
        id = ambil.getStringExtra("id");
        name = ambil.getStringExtra("nama");
        lat = ambil.getDoubleExtra("latitude",0);
        lng = ambil.getDoubleExtra("longitude",0);
        alamat = ambil.getStringExtra("alamat");

        sMenu = new StringBuffer();
        sHarga = new StringBuffer();
        for (int i = 0; i<foodCart.size();i++){
            sMenu.append(""+foodCart.get(i).getMenu()+" x "+foodCart.get(i).getJumlah()+"\n");
            sHarga.append("Rp. "+foodCart.get(i).getTotal_harga()+",-\n");
            hargaMenu = hargaMenu + Integer.parseInt(foodCart.get(i).getTotal_harga());
        }
        tvMenu.setText(sMenu);
        tvHarga.setText(sHarga);
        tvBiayaMenu.setText("Rp. "+hargaMenu+",-");
        tvBiayaOjek.setText("Pilih lokasi dahulu");
        tvBiayaTotal.setText("-");

        etTujuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search(DESTINATION);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            Place place = PlacePicker.getPlace(data, this);
            switch (requestCode) {
                case DESTINATION:
                    try{
                        try{
                            etTujuan.setText(place.getAddress().toString());
                        }catch(Exception e){
                            etTujuan.setText(place.getName().toString());
                        }
                        latTujuan = place.getLatLng().latitude;
                        lngTujuan = place.getLatLng().longitude;
                        requestHarga();
                        Log.e("nama",place.getName().toString());
                        Log.e("alamat",etTujuan.getText().toString());
                        Log.e("slatitude", String.valueOf(lat));
                        Log.e("slongitude",String.valueOf(lng));
                        Log.e("elatitude", ""+String.valueOf(latTujuan));
                        Log.e("elongitude", ""+String.valueOf(lngTujuan));
                        Log.e("harga", ""+harga);
                        Log.e("jarak", ""+kmInDec);
                        alamatTujuan = place.getAddress().toString();
                        Log.d("lokasi ambil",latTujuan+","+lngTujuan);
                    }catch(Exception e){
                        Toast.makeText(this, "Koneksi tidak stabil, periksa koneksi atau pilih dengan perlahan!", Toast.LENGTH_LONG).show();
                    }
                    break;
            }
        }
    }

    private void search(int kode) {
        try {
            //Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY).build(this);
            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
            startActivityForResult(builder.build(this), kode);
        } catch (GooglePlayServicesRepairableException e) {

            GoogleApiAvailability.getInstance().getErrorDialog(this, e.getConnectionStatusCode(), 0).show();
        } catch (GooglePlayServicesNotAvailableException e) {
            String message = "Google Play Services is not available: " +
                    GoogleApiAvailability.getInstance().getErrorString(e.errorCode);
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        } catch(Exception e){
            Toast.makeText(this, "Koneksi tidak stabil, periksa koneksi atau pilih dengan perlahan!", Toast.LENGTH_LONG).show();
        }
    }

    public void requestHarga(){

            CalculationByDistance(lat,lng,latTujuan,lngTujuan);

        Log.d("HASIL",""+kmInDec);
            /*if(kmInDec>50){
                final AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setTitle("Peringatan");
                alert.setMessage("Tidak bisa memesan lebih dari 50 KM");
                alert.setPositiveButton("Ya",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                            }
                        });
                alert.show();
            }else {*/
                if(kmInDec<=5){
                    jarakKM = kmInDec;
                    harga = 15000;
                }else {
                    jarakKM = kmInDec;
                    jarakKM = jarakKM - 10;
                    harga = jarakKM * 1500;
                    harga = harga + 25000;
                }
                hargaTotal = Math.round((hargaMenu + Math.round(harga))/1000)*1000;
                tvBiayaOjek.setText("Rp. "+Math.round(harga)+",-");
                tvBiayaTotal.setText("Rp. "+hargaTotal+",-");

                /*tvJarak.setText(kmInDec+" KM");
                noteTujuan = etNoteTujuan.getText().toString();
                noteJemput = etNoteJemput.getText().toString();*/
                if(etNoteTujuan.getText().toString().length()==0){
                    noteTujuan = "tidak ada catatan";
                }else{
                    noteTujuan = etNoteTujuan.getText().toString();
                }
                if(etNoteFood.getText().toString().length()==0){
                    noteFood = "tidak ada catatan";
                }else{
                    noteFood = etNoteFood.getText().toString();
                }

                btPesan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        makeOrder(""+lat, ""+lng, ""+latTujuan, ""+lngTujuan,""+harga,  name+", "+alamat, alamatTujuan,noteTujuan,"gakdipake",""+kmInDec,id,noteFood,""+hargaMenu);
                    }
                });

        //}
    }

    public double CalculationByDistance(/*LatLng StartP, LatLng EndP*/double lat1, double lon1, double lat2, double lon2) {
        int Radius = 6371;// radius of earth in Km
        /*double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;*/
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult = Radius * c;
        double km = valueResult / 1;
        DecimalFormat newFormat = new DecimalFormat("####");
        float abcd = Float.valueOf(newFormat.format(km));
        kmInDec = abcd;
        double meter = valueResult % 1000;
        int meterInDec = Integer.valueOf(newFormat.format(meter));
        Log.i("Radius Value", "" + valueResult + "   KM  " + abcd
                + " Meter   " + meterInDec);

        return Radius * c;
    }

    private void makeOrder(final String startLat, final String startLng, final String endLat, final String endLng, final String harga, final String Saddress, final String Eaddress, final String Snote, final String Enote, final String distance, final String resid, final String foodnotes, final String foodprice) {

        loading = ProgressDialog.show(this,"Mohon Tunggu","Sedang memesan...",false,false);

        Log.d("lokasi",endLat+","+endLng);

        for (Profil profil : valuesProfil){
            token = profil.getToken();
        }

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, APIConfig.API_ORDER_REGULAR, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                hideDialog();
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        String order_id = jObj.getString("order_id");
                        Intent intent = new Intent(FoodOrderDetailActivity.this, LoadingScreenFoodActivity.class);
                        intent.putExtra("order_id",order_id);
                        intent.putExtra("start_latitude",startLat);
                        intent.putExtra("start_longitude",startLng);
                        intent.putExtra("end_latitude",endLat);
                        intent.putExtra("end_longitude",endLng);
                        intent.putExtra("notes",Snote);
                        intent.putExtra("total_price",harga);
                        intent.putExtra("distance",distance);
                        intent.putExtra("address",Saddress);
                        intent.putExtra("endaddress",Eaddress);
                        intent.putExtra("notefood",foodnotes);
                        intent.putExtra("food_price",foodprice);
                        startActivityForResult(intent,OUTPUT);
                    }else{
                        if (jObj.has("msg")){
                            message = jObj.getString("msg");
                        }else if(jObj.has("message")){
                            message = jObj.getString("message");
                        }
                        final AlertDialog.Builder alert = new AlertDialog.Builder(FoodOrderDetailActivity.this);
                        alert.setTitle("Peringatan");
                        alert.setMessage(""+message);
                        alert.setPositiveButton("Ya",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // TODO Auto-generated method stub
                                    }
                                });
                        alert.show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(FoodOrderDetailActivity.this, ""+e, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                hideDialog();
                //Toast.makeText(FoodOrderDetailActivity.this, ""+error, Toast.LENGTH_SHORT).show();
                Toast.makeText(FoodOrderDetailActivity.this, "Harus memilih Menu!", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("type", "3");
                params.put("start_latitude",startLat);
                params.put("start_longitude",startLng);
                params.put("end_latitude",endLat);
                params.put("end_longitude",endLng);
                params.put("notes","Tidak ada notes");
                params.put("endnotes",Snote);
                params.put("total_price",harga);
                params.put("distance", distance);
                params.put("address",Saddress);
                params.put("endaddress",Eaddress);
                params.put("restaurant_id",resid);
                params.put("foodnotes", foodnotes);
                params.put("food_price", foodprice);
                for (int j = 0; j < foodCart.size(); j++) {
                    params.put("food_id[" +j+ "]", foodCart.get(j).getIdMenu()+","+foodCart.get(j).getJumlah()+","+foodCart.get(j).getHarga()+","+foodCart.get(j).getTotal_harga());
                }
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
            //Intent intentKill = new Intent("finish_activity");
            //sendBroadcast(intentKill);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void hideDialog() {
        if (loading.isShowing())
            loading.dismiss();
    }

    @Override
    public void onBackPressed() {
        //Intent intentKill = new Intent("finish_activity");
        //sendBroadcast(intentKill);
        finish();
    }
}


