package com.wensoft.ojeku.main.handle_information;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.wensoft.ojeku.R;
import com.wensoft.ojeku.main.fragments.InformationFragment;

public class InformationOjekActivity extends AppCompatActivity {

    private String textDetail;
    private TextView textView;
    private Toolbar toolbar;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        toolbar.setTitle("Kita Jemput");
        textView = (TextView) findViewById(R.id.textViewInformation);
        textView.setText(R.string.kitajemput);

        imageView = (ImageView) findViewById(R.id.ivInformation);
        imageView.setImageResource(R.drawable.ic_motor);
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
}
