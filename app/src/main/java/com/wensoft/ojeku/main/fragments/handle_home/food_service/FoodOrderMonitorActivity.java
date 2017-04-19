package com.wensoft.ojeku.main.fragments.handle_home.food_service;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.squareup.picasso.Picasso;
import com.wensoft.ojeku.R;
import com.wensoft.ojeku.config.APIConfig;
import com.wensoft.ojeku.config.DirectionsJSONParser;
import com.wensoft.ojeku.config.GPSTracker;
import com.wensoft.ojeku.database.DatabaseHandler;
import com.wensoft.ojeku.main.MainActivity;
import com.wensoft.ojeku.pojo.Profil;
import com.wensoft.ojeku.singleton.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FoodOrderMonitorActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.OnConnectionFailedListener {

    private ProgressDialog loading;
    private ArrayList<Profil> valuesProfil;
    private DatabaseHandler dataSource;
    private String token;
    private float harga, jarakKM, kmInDec;
    private Polyline polyline;

    private ImageView imageView;
    private View view;


    private GPSTracker gps;
    private GoogleMap mGoogleMap;
    private GoogleApiClient mGoogleApiClient;
    private double latJemput, lngJemput, latTujuan, lngTujuan;
    private LatLng latLngJemput, latLngTujuan;

    private Button btPesan, btCancel, btFinish, btTelepon, btSms, btFoodDetail;
    private EditText etJemput, etTujuan, etNoteJemput, etNoteTujuan;
    private String start_latitude, start_longitude, end_latitude, end_longitude, alamatJemput, alamatTujuan, food_price,total_price, id_order, jarak;
    private TextView tvJarak, tvHarga, tvNama, tvPlat, tvInvoice, tvPhone;

    private LinearLayout layoutButton, linLayOrderInfo, linLayDriver;


    private String invoice,order_id,Snote,distance,Saddress,Eaddress, startLat, startLng, endLat, endLng, nama, plat_nomor,telepon,avatar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_serv);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map1);
        mapFragment.getMapAsync(FoodOrderMonitorActivity.this);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                //.addConnectionCallbacks(this)
                //.addOnConnectionFailedListener(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();


        btPesan = (Button) findViewById(R.id.buttonPesan);
        btCancel = (Button) findViewById(R.id.buttonCancel);
        btFinish = (Button) findViewById(R.id.buttonSelesai);
        layoutButton = (LinearLayout) findViewById(R.id.linLayButton);
        etJemput = (EditText) findViewById(R.id.et_pickup);
        etTujuan = (EditText) findViewById(R.id.et_destination);
        etNoteJemput = (EditText) findViewById(R.id.et_note_pick);
        etNoteTujuan = (EditText) findViewById(R.id.et_note_destination);
        linLayOrderInfo = (LinearLayout) findViewById(R.id.layout_order_driver);
        linLayDriver = (LinearLayout) findViewById(R.id.linLayDriver);
        tvJarak = (TextView) findViewById(R.id.tv_distance_order) ;
        tvHarga = (TextView) findViewById(R.id.tv_price_order);
        imageView = (ImageView) findViewById(R.id.profile_image);
        tvNama = (TextView) findViewById(R.id.tvNama);
        tvPlat = (TextView) findViewById(R.id.tvPlat);
        tvInvoice = (TextView) findViewById(R.id.tvInvoice);
        tvPhone = (TextView) findViewById(R.id.tvPhone);
        btTelepon = (Button) findViewById(R.id.btTelepon);
        btSms = (Button) findViewById(R.id.btSMS);
        btFoodDetail = (Button) findViewById(R.id.btDetailFood);

        view = (View) findViewById(R.id.pembatas);

        view.setVisibility(View.GONE);
        etJemput.setVisibility(View.GONE);
        etNoteJemput.setVisibility(View.GONE);

        Intent ambil = getIntent();
        id_order = ambil.getStringExtra("order_id");
        /*start_latitude = ambil.getStringExtra("start_latitude");
        start_longitude = ambil.getStringExtra("start_longitude");
        end_latitude = ambil.getStringExtra("end_latitude");
        end_longitude = ambil.getStringExtra("end_longitude");
        alamatJemput = ambil.getStringExtra("alamat_penjemputan");
        alamatTujuan = ambil.getStringExtra("alamat_tujuan");
        total_price = ambil.getStringExtra("total_price");
        jarak = ambil.getStringExtra("jarak");*/

        startLat = ambil.getStringExtra("start_latitude");
        startLng = ambil.getStringExtra("start_longitude");
        endLat = ambil.getStringExtra("end_latitude");
        endLng = ambil.getStringExtra("end_longitude");
        Snote = ambil.getStringExtra("notes");
        total_price = ambil.getStringExtra("total_price");
        food_price = ambil.getStringExtra("food_price");
        distance = ambil.getStringExtra("distance");
        Saddress = ambil.getStringExtra("address");
        Eaddress = ambil.getStringExtra("endaddress");
        invoice = ambil.getStringExtra("invoice");

        nama = ambil.getStringExtra("nama_driver");
        plat_nomor = ambil.getStringExtra("plat_nomor");
        telepon = ambil.getStringExtra("telepon");
        avatar = ambil.getStringExtra("avatar");
        final String urlAvatar = "http://ojekita.com/"+avatar;

        Log.d("HASIL",startLat+","+startLng);
        Log.d("HASIL",endLat+","+endLng);
        Log.d("HASIL",Snote+"");
        Log.d("HASIL",Snote+"");
        Log.d("HASIL",total_price+"");
        Log.d("HASIL",distance+"");
        Log.d("HASIL",Eaddress+"");
        Log.d("lokasi ketiga",endLat+","+endLng);

        latJemput = Double.valueOf(startLat);
        lngJemput = Double.valueOf(startLng);
        latTujuan = Double.valueOf(endLat);
        lngTujuan = Double.valueOf(endLng);

        latLngJemput = new LatLng(latJemput, lngJemput);
        latLngTujuan = new LatLng(latTujuan, lngTujuan);

        String url = getDirectionsUrl(latLngJemput, latLngTujuan);
        FoodOrderMonitorActivity.DownloadTask downloadTask = new FoodOrderMonitorActivity.DownloadTask();
        downloadTask.execute(url);

        etJemput.setText(alamatJemput);
        etJemput.setVisibility(View.GONE);
        etNoteJemput.setVisibility(View.GONE);
        etTujuan.setText(Eaddress);
        tvHarga.setText("Rp. "+total_price+",-\n(Rp. "+food_price+",-)");
        tvJarak.setText(distance+" KM");
        tvInvoice.setText("No Order : "+invoice);

        dataSource = new DatabaseHandler(this);
        valuesProfil = (ArrayList<Profil>) dataSource.getAllProfils();

        //btCancel.setVisibility(View.GONE);

        linLayOrderInfo.setVisibility(View.VISIBLE);
        linLayDriver.setVisibility(View.GONE);

        btPesan.setVisibility(View.GONE);
        layoutButton.setVisibility(View.VISIBLE);
        etJemput.setEnabled(false);
        etTujuan.setEnabled(false);
        etNoteTujuan.setEnabled(false);
        etNoteJemput.setEnabled(false);
        linLayDriver.setVisibility(View.VISIBLE);
        tvNama.setText(nama);
        tvPlat.setText(plat_nomor);
        tvPhone.setText(telepon);
        Picasso.with(this).load(urlAvatar).into(imageView);
        btTelepon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", telepon, null));
                startActivity(intent);
            }
        });
        btSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", telepon, null));
                startActivity(intent);
            }
        });
        btFoodDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FoodOrderMonitorActivity.this, FoodDetailActivity.class);
                intent.putExtra("order_id",id_order);
                startActivity(intent);
            }
        });

        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert.setTitle("Peringatan");
                alert.setMessage("Batalkan order?");
                alert.setPositiveButton("Ya",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                                cancel(id_order);
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
        });
        btFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FoodOrderMonitorActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mGoogleMap = googleMap;

        /*gps = new GPSTracker(OrderDetailActivity.this);
        LatLng sydney = new LatLng(gps.getLatitude(), gps.getLongitude());*/
        mGoogleMap.moveCamera(CameraUpdateFactory.zoomTo(14));
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLngJemput));

        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        //mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
        mGoogleMap.getUiSettings().setAllGesturesEnabled(true);
        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
        mGoogleMap.getUiSettings().setCompassEnabled(true);
        mGoogleMap.getUiSettings().setMapToolbarEnabled(false);

        mGoogleMap.addMarker(new MarkerOptions().position(latLngJemput).title("Lokasi Pembelian Makanan").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_dest)));
        mGoogleMap.addMarker(new MarkerOptions().position(latLngTujuan).title("Lokasi Tujuan").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_del)));

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {}

    private String getDirectionsUrl(LatLng origin,LatLng dest){

        // Origin of route
        String str_origin = "origin="+origin.latitude+","+origin.longitude;
        // Destination of route
        String str_dest = "destination="+dest.latitude+","+dest.longitude;
        // Sensor enabled
        String sensor = "sensor=false";
        // Building the parameters to the web service
        String parameters = str_origin+"&"+str_dest+"&"+sensor;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;

        return url;
    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while( ( line = br.readLine()) != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }catch(Exception e){
            //Log.d("Exception while downloading url", e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    private class DownloadTask extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try{
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            FoodOrderMonitorActivity.ParserTask parserTask = new FoodOrderMonitorActivity.ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }

    /** A class to parse the Google Places in JSON format */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> >{

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try{
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            }catch(Exception e){
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();

            // Traversing through all the routes
            for(int i=0;i<result.size();i++){
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for(int j=0;j<path.size();j++){
                    HashMap<String,String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(4);
                lineOptions.color(getResources().getColor(R.color.colorAccent));
            }

            mGoogleMap.addPolyline(lineOptions);
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
                        Intent intent = new Intent(FoodOrderMonitorActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                } catch (JSONException e) {
                    Toast.makeText(FoodOrderMonitorActivity.this, ""+e, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                hideDialog();
                Toast.makeText(FoodOrderMonitorActivity.this, ""+error, Toast.LENGTH_SHORT).show();
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
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id==android.R.id.home) {
            Intent intent = new Intent(FoodOrderMonitorActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(FoodOrderMonitorActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void hideDialog() {
        if (loading.isShowing())
            loading.dismiss();
    }

}
