package com.wensoft.ojeku.main.fragments.handle_home.shuttle_service;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
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
import com.wensoft.ojeku.main.fragments.handle_home.LoadingScreenActivity;
import com.wensoft.ojeku.main.fragments.handle_home.food_service.FoodOrderDetailActivity;
import com.wensoft.ojeku.pojo.Markers;
import com.wensoft.ojeku.pojo.Profil;
import com.wensoft.ojeku.singleton.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;



public class RegularServiceActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.OnConnectionFailedListener/*,
        GoogleApiClient.ConnectionCallbacks,
        LocationListener*/ {

    private ProgressDialog loading;
    private ArrayList<Profil> valuesProfil;
    private DatabaseHandler dataSource;
    private String token;
    private float harga, jarakKM, kmInDec;
    private Polyline polyline;

    private ImageView imageView;


    private List<Markers> markerList = new ArrayList<Markers>();
    private List<Address> addresses;
    private GPSTracker gps;
    private Geocoder geocoder;
    private GoogleMap mGoogleMap;
    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private Marker mCurrLocationMarker, markerJemput, markerTujuan, markerDriver;
    private double latJemput, lngJemput, latTujuan, lngTujuan;
    private LatLng latLngJemput, latLngTujuan;

    private Button btPesan, btCancel, btFinish, btTelepon, btSms;
    private EditText etJemput, etTujuan, etNoteJemput, etNoteTujuan;
    private String jemput, tujuan, alamatJemput, alamatTujuan, noteJemput, noteTujuan, message;
    private TextView tvJarak, tvHarga, tvNama, tvPlat, tvPhone, tvInvoice;

    private static final int PICK = 1;
    private static final int DESTINATION = 2;
    private static final int OUTPUT = 3;
    LinearLayout layoutButton, linLayOrderInfo, linLayDriver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regular_service);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map1);
        mapFragment.getMapAsync(RegularServiceActivity.this);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                //.addConnectionCallbacks(this)
                //.addOnConnectionFailedListener(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();


        btSms = (Button) findViewById(R.id.btSMS);
        btTelepon = (Button) findViewById(R.id.btTelepon);
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
        tvNama = (TextView) findViewById(R.id.tvNama);
        tvPlat = (TextView) findViewById(R.id.tvPlat);
        tvInvoice = (TextView) findViewById(R.id.tvInvoice);
        tvPhone = (TextView) findViewById(R.id.tvPhone);
        imageView = (ImageView) findViewById(R.id.profile_image);

        layoutButton.setVisibility(View.GONE);

        dataSource = new DatabaseHandler(this);
        valuesProfil = (ArrayList<Profil>) dataSource.getAllProfils();

        // disini gw buat ketika edittext di pencet dia bakal nyari alamat pake autocomplete place
        etJemput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search(PICK);
            }
        });

        // disini gw buat ketika edittext di pencet dia bakal nyari tujuan pake autocomplete place
        etTujuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search(DESTINATION);
            }
        });
        //makeJsonObjectRequest();

        gps = new GPSTracker(RegularServiceActivity.this);
        getLocation(gps.getLatitude(),gps.getLongitude());
        //makeJsonObjectRequest();
    }

    private void search(int kode) {
        try {
            //Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY).build(this);
            //startActivityForResult(intent, kode);
            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
            startActivityForResult(builder.build(this), kode);
        } catch (GooglePlayServicesRepairableException e) {

            GoogleApiAvailability.getInstance().getErrorDialog(this, e.getConnectionStatusCode(), 0).show();
        } catch (GooglePlayServicesNotAvailableException e) {
            String message = "Google Play Services is not available: " +
                    GoogleApiAvailability.getInstance().getErrorString(e.errorCode);
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            String nama = data.getStringExtra("nama_driver");
            String plat_nomor = data.getStringExtra("plat_nomor");
            final String telepon = data.getStringExtra("telepon");
            String avatar = data.getStringExtra("avatar");
            final String invoice = data.getStringExtra("invoice");
            final String urlAvatar = "http://ojekita.com/"+avatar;
            final String orderid = data.getStringExtra("order_id");
            if(nama!=null){
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
                //markerDriver.remove();
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
                tvInvoice.setText("No Order : "+invoice);
                Picasso.with(this).load(urlAvatar).into(imageView);

                /*int SPLASH_TIME_OUT = 180000;

                new Handler().postDelayed(new Thread() {
                    @Override
                    public void run() {
                        btCancel.setVisibility(View.GONE);
                    }
                }, SPLASH_TIME_OUT);*/

                btFinish.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
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
                                        cancel(orderid);
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
            }
            Place place = PlacePicker.getPlace(data, this);
            switch (requestCode) {
                case PICK:
                    try{
                        etJemput.setText(place.getAddress().toString());
                        jemput = place.getAddress().toString();
                        latJemput = place.getLatLng().latitude;
                        lngJemput = place.getLatLng().longitude;
                        alamatJemput = place.getAddress().toString();
                        if(markerJemput==null){
                            markerJemput = mGoogleMap.addMarker(new MarkerOptions().position(place.getLatLng()).title("Lokasi Jemput").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_dest)));
                        }else{
                            markerJemput.remove();
                            markerJemput = mGoogleMap.addMarker(new MarkerOptions().position(place.getLatLng()).title("Lokasi Jemput").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_dest)));
                        }
                        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(place.getLatLng()));

                        //panggil method  buat request harganya ke server, atau gk perhitungan lu
                        requestHarga();


                        // perintah ini digunain untuk ngambil data yang dibutuhin dari API (alamat, lat, lng)
                        Log.e("nama",place.getName().toString());
                        Log.e("alamat",place.getAddress().toString());
                        Log.e("latitude", String.valueOf(place.getLatLng().latitude));
                        Log.e("longitude", String.valueOf(place.getLatLng().longitude));
                    }catch(Exception e){
                        Toast.makeText(this, "Koneksi tidak stabil, periksa koneksi atau pilih dengan perlahan!", Toast.LENGTH_LONG).show();
                    }
                    break;
                case DESTINATION:
                    try{
                        etTujuan.setText(place.getAddress().toString());
                        tujuan = place.getAddress().toString();
                        latTujuan = place.getLatLng().latitude;
                        lngTujuan = place.getLatLng().longitude;
                        alamatTujuan = place.getAddress().toString();
                        if(markerTujuan==null){
                            markerTujuan = mGoogleMap.addMarker(new MarkerOptions().position(place.getLatLng()).title("Lokasi Tujuan").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_del)));
                        }else{
                            markerTujuan.remove();
                            markerTujuan = mGoogleMap.addMarker(new MarkerOptions().position(place.getLatLng()).title("Lokasi Tujuan").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_del)));
                        }
                        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(place.getLatLng()));

                        //panggil method  buat request harganya ke server, atau gk perhitungan lu
                        requestHarga();

                        // perintah ini digunain untuk ngambil data yang dibutuhin dari API (alamat, lat, lng)
                        Log.e("nama",place.getName().toString());
                        Log.e("alamat",place.getAddress().toString());
                        Log.e("latitude", String.valueOf(place.getLatLng().latitude));
                        Log.e("longitude", String.valueOf(place.getLatLng().longitude));
                    }catch(Exception e){
                        Toast.makeText(this, "Koneksi tidak stabil, periksa koneksi atau pilih dengan perlahan!", Toast.LENGTH_LONG).show();
                    }
                    break;
                case OUTPUT:

                    break;
            }
        }
    }

    public void requestHarga(){
        if(alamatJemput!=null&&alamatTujuan!=null) {
            if(markerDriver!=null) {
                markerList.clear();
                markerDriver.remove();
            }
            latLngJemput = new LatLng(latJemput, lngJemput);
            latLngTujuan = new LatLng(latTujuan, lngTujuan);

            String url = getDirectionsUrl(latLngJemput, latLngTujuan);
            RegularServiceActivity.DownloadTask downloadTask = new RegularServiceActivity.DownloadTask();
            downloadTask.execute(url);
            CalculationByDistance(latLngJemput, latLngTujuan);
            /*if(jarakKM>50){
                jarakKM = jarakKM - 10;
                harga = jarakKM * 2000;
                harga = harga + 25000;
            }else if(jarakKM>30){
                jarakKM = jarakKM - 10;
                harga = jarakKM * 3000;
                harga = harga + 25000;
            }else if(jarakKM>10){
                jarakKM = jarakKM - 10;
                harga = jarakKM * 4000;
                harga = harga + 25000;
            }else{
                harga = 25000;
            }*/
            /*if (kmInDec > 50) {
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
            } else {*/
                if (kmInDec > 5) {
                    jarakKM = kmInDec;
                    jarakKM = jarakKM - 5;
                    harga = jarakKM * 2000;
                    harga = harga + 10000;
                } else {
                    jarakKM = kmInDec;
                    harga = 10000;
                }
                tvHarga.setText("Rp. " + harga);
                tvJarak.setText(kmInDec + " KM");
                noteTujuan = etNoteTujuan.getText().toString();
                noteJemput = etNoteJemput.getText().toString();

                if (noteTujuan.length() == 0) {
                    noteTujuan = "Tidak ada Note";
                }
                if (noteJemput.length() == 0) {
                    noteJemput = "Tidak ada Note";
                }
                linLayOrderInfo.setVisibility(View.VISIBLE);
                btPesan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        makeOrder("" + harga, "" + latJemput, "" + lngJemput, "" + latTujuan, "" + lngTujuan, alamatJemput, alamatTujuan, noteJemput, noteTujuan, "" + kmInDec);
                        Log.i("pesan harga : ", "" + harga);
                        Log.i("pesan latAwal : ", "" + latJemput);
                        Log.i("pesan lngAwal : ", "" + lngJemput);
                        Log.i("pesan latAkhir : ", "" + latTujuan);
                        Log.i("pesan lngAkhir : ", "" + lngTujuan);
                    }
                });
            }
        //}
    }

    public double CalculationByDistance(LatLng StartP, LatLng EndP) {
        int Radius = 6371;
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
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
        kmInDec = Float.valueOf(newFormat.format(km));
        double meter = valueResult % 1000;
        int meterInDec = Integer.valueOf(newFormat.format(meter));
        Log.i("Radius Value", "" + valueResult + "   KM  " + kmInDec
                + " Meter   " + meterInDec);

        return Radius * c;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mGoogleMap = googleMap;

        /*//Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mGoogleMap.setMyLocationEnabled(true);
            }
        } else {
            buildGoogleApiClient();
            mGoogleMap.setMyLocationEnabled(true);
        }*/


        // Add a marker in Sydney and move the camera
        gps = new GPSTracker(RegularServiceActivity.this);
        LatLng sydney = new LatLng(gps.getLatitude(), gps.getLongitude());
        mGoogleMap.moveCamera(CameraUpdateFactory.zoomTo(14));
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        markerJemput = mGoogleMap.addMarker(new MarkerOptions().position(sydney).title("Lokasi Jemput").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_dest)));



        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        //mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
        mGoogleMap.getUiSettings().setAllGesturesEnabled(true);
        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
        mGoogleMap.getUiSettings().setCompassEnabled(true);
        mGoogleMap.getUiSettings().setMapToolbarEnabled(false);

        makeJsonObjectRequest();
    }

    public void getLocation(final double lat, final double lng){
        try {
            geocoder = new Geocoder(this.getApplicationContext(), Locale.getDefault());
            addresses = geocoder.getFromLocation(lat, lng, 1);
            etJemput.setText(addresses.get(0).getAddressLine(0) + ", " + addresses.get(0).getLocality() +", " + addresses.get(0).getAdminArea() + ", " + addresses.get(0).getCountryName());
            alamatJemput = etJemput.getText().toString();

            latJemput = lat;
            lngJemput = lng;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

   /*protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Places.GEO_DATA_API)
                //.addApi(Places.PLACE_DETECTION_API)
                //.enableAutoManage(this, this)
                .build();
        mGoogleApiClient.connect();
    }*/


    /*@Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }*/

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {}

    /*@Override
    public void onConnectionSuspended(int i) {}


    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mGoogleMap.moveCamera(CameraUpdateFactory.zoomTo(14));

        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }*/


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

    // Fetches data from url passed
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

            ParserTask parserTask = new ParserTask();

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

            // Drawing polyline in the Google Map for the i-th route
            if(polyline!=null){
                polyline.remove();
                polyline = mGoogleMap.addPolyline(lineOptions);
            }else{
                polyline = mGoogleMap.addPolyline(lineOptions);
            }
        }
    }

    private void makeJsonObjectRequest() {

        loading = ProgressDialog.show(this,"Mohon Tunggu","Memuat Driver...",false,false);

        for (Profil profil : valuesProfil){
            token = profil.getToken();
        }

        StringRequest jsonObjReq = new StringRequest(Request.Method.GET, APIConfig.API_GET_DRIVER, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                hideDialog();
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        JSONArray dataArray = jObj.getJSONArray("data");
                        try {
                            //looping untuk mendapatkan seluruh item yang di order oleh user
                            //Markers markers = new Markers();
                            for(int i = 0; i < dataArray.length();i++) {
                                JSONObject data = dataArray.getJSONObject(i);
                                try{
                                    String name = data.getString("name");
                                    String nopol = data.getString("plat_nomor");
                                    double latitude = data.getDouble("latitude");
                                    double longitude = data.getDouble("longitude");
                                    String avatar = data.getString("avatar");
                                    markerList.add(new Markers(String.valueOf(i),nopol,name,latitude, longitude,avatar));
                                }catch(Exception e){

                                }
                            }
                            for(int i =0;i<markerList.size();i++){
                                LatLng latLng = new LatLng(markerList.get(i).getLat(),markerList.get(i).getLong());
                                markerDriver = mGoogleMap.addMarker(new MarkerOptions()
                                        .position(latLng)
                                        .title(markerList.get(i).getNama_mitra())
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_v)));
                            }
                        } catch (JSONException e) {
                            Toast.makeText(RegularServiceActivity.this, e.toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                } catch (JSONException e) {
                    Toast.makeText(RegularServiceActivity.this, ""+e, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                hideDialog();
                Toast.makeText(RegularServiceActivity.this, ""+error, Toast.LENGTH_SHORT).show();
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

    private void makeOrder( final String harga, final String startLat, final String startLng, final String endLat, final String endLng, final String Saddress, final String Eaddress, final String Snote, final String Enote, final String distance) {

        loading = ProgressDialog.show(this,"Mohon Tunggu","Sedang memesan...",false,false);

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
                        Intent intent = new Intent(RegularServiceActivity.this, LoadingScreenActivity.class);
                        intent.putExtra("order_id",order_id);
                        startActivityForResult(intent,OUTPUT);
                    }else{
                        if (jObj.has("msg")){
                            message = jObj.getString("msg");
                        }else if(jObj.has("message")){
                            message = jObj.getString("message");
                        }
                        final AlertDialog.Builder alert = new AlertDialog.Builder(RegularServiceActivity.this);
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
                    Toast.makeText(RegularServiceActivity.this, ""+e, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                hideDialog();
                Toast.makeText(RegularServiceActivity.this, ""+error, Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("type", "1");
                params.put("start_latitude",startLat);
                params.put("start_longitude",startLng);
                params.put("end_latitude",endLat);
                params.put("end_longitude",endLng);
                params.put("notes",Snote);
                params.put("endnotes",Enote);
                params.put("total_price",harga);
                params.put("distance", distance);
                params.put("address",Saddress);
                params.put("endaddress",Eaddress);
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
                        Intent intent = new Intent(RegularServiceActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                } catch (JSONException e) {
                    Toast.makeText(RegularServiceActivity.this, ""+e, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                hideDialog();
                Toast.makeText(RegularServiceActivity.this, ""+error, Toast.LENGTH_SHORT).show();
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
