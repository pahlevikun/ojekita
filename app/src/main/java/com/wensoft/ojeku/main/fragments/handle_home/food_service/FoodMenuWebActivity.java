package com.wensoft.ojeku.main.fragments.handle_home.food_service;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.wensoft.ojeku.R;
import com.wensoft.ojeku.config.GPSTracker;
import com.wensoft.ojeku.pojo.Cart;
import com.wensoft.ojeku.pojo.FoodBanner;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class FoodMenuWebActivity extends AppCompatActivity {

    private String url,id,name, idMenu, menu,jumlah,harga,total_harga,alamat;
    private double lat,lng;
    private WebView webView;
    private ArrayList<Cart> foodCart = new ArrayList<Cart>();
    private ProgressDialog loading;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_menu_web);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar(). setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        webView = (WebView) findViewById(R.id.webViewMakanan);

        Intent ambil = getIntent();
        id = ambil.getStringExtra("id");
        name = ambil.getStringExtra("nama");
        lat = ambil.getDoubleExtra("latitude",0);
        lng = ambil.getDoubleExtra("longitude",0);
        alamat = ambil.getStringExtra("alamat");
        Log.d("HASIL",""+id);
        setTitle("Menu "+name);

        loading = ProgressDialog.show(this,"Mohon Tunggu","Sedang memuat...",false,false);
        loading.setCancelable(true);
        loading.setOnCancelListener(new DialogInterface.OnCancelListener(){
            @Override
            public void onCancel(DialogInterface dialog){
                hideDialog();
                finish();
            }});

        url = "http://ojekita.com/menu/"+id;
        webView.loadUrl(url);
        webView.clearCache(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new LoadListener(), "HTMLOUT");
        webView.setWebViewClient(new WebViewClient(){

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            public void onPageFinished(WebView view, String url) {
                hideDialog();
                if(url.contains("generatemenu")){
                    view.setVisibility(View.INVISIBLE);
                    view.loadUrl("javascript:window.HTMLOUT.processHTML(document.getElementsByTagName('pre')[0].innerHTML);");
                }
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                hideDialog();
                finish();
            }
        });

        //destroyActivity();

    }

    class LoadListener{
        @JavascriptInterface
        public void processHTML(String html) {
            Log.d("result",html);
            String myJSONString = html;
            try {
                JSONObject jsonObj = new JSONObject(myJSONString);
                boolean error = jsonObj.getBoolean("error");
                if (!error) {
                    JSONArray array = jsonObj.getJSONArray("data");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject data = array.getJSONObject(i);
                        idMenu = data.getString("id");
                        menu = data.getString("menu");
                        harga = data.getString("harga");
                        jumlah = data.getString("jumlah");
                        total_harga = data.getString("total_harga");
                        foodCart.add(new Cart(String.valueOf(i),idMenu, menu, harga, jumlah, total_harga));
                    }
                    Intent intent = new Intent(FoodMenuWebActivity.this, FoodOrderDetailActivity.class);
                    intent.putExtra("id",id);
                    intent.putExtra("nama",name);
                    intent.putExtra("latitude",lat);
                    intent.putExtra("longitude",lng);
                    intent.putExtra("order",foodCart);
                    intent.putExtra("alamat",alamat);
                    startActivity(intent);
                    finish();
                }
            }catch (Exception e){
                Toast.makeText(FoodMenuWebActivity.this, "Error : "+e, Toast.LENGTH_SHORT).show();
                Log.d("ERROR",""+e);
                finish();
            }
        }
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
        hideDialog();
        finish();
    }

    private void hideDialog() {
        if (loading.isShowing())
            loading.dismiss();
    }

    public void destroyActivity(){
        BroadcastReceiver broadcast_receiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context arg0, Intent intent) {
                String action = intent.getAction();
                if (action.equals("finish_activity")) {
                    finish();
                }
            }
        };
        registerReceiver(broadcast_receiver, new IntentFilter("finish_activity"));
    }
}
