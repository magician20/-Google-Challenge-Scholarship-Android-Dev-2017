package com.example.android.sunshine;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {

    private static final String FORECAST_SHARE_HASHTAG = " #SunshineApp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        TextView textDay = (TextView) findViewById(R.id.tv_day_out);
        // TODO (2) Display the weather forecast that was passed from MainActivity
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if(extras.getString(Intent.ACTION_SEND)!=null) {
            String stringDay = extras.getString(Intent.ACTION_SEND);
            textDay.setText(stringDay);
        }else{
            textDay.setText("No Data");
        }
    }
}