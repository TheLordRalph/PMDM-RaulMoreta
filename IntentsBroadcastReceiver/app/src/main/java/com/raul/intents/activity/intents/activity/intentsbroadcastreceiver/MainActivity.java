package com.raul.intents.activity.intents.activity.intentsbroadcastreceiver;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    BroadcastReceiver tickreceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v("MainActivity", "OnCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView hourTextView = (TextView) findViewById(R.id.hour);
        TextView minuteTextView = (TextView) findViewById(R.id.minute);

        hourTextView.setText(String.valueOf(Calendar.getInstance().get(Calendar.HOUR_OF_DAY)));
        minuteTextView.setText(String.valueOf(Calendar.getInstance().get(Calendar.MINUTE)));

        tickreceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(intent.getAction().equals(Intent.ACTION_TIME_TICK)) {
                    hourTextView.setText(String.valueOf(Calendar.getInstance().get(Calendar.HOUR_OF_DAY)));
                    minuteTextView.setText(String.valueOf(Calendar.getInstance().get(Calendar.MINUTE)));
                }
            }
        };

        registerReceiver(tickreceiver, new IntentFilter(Intent.ACTION_TIME_TICK));
    }

    @Override
    protected void onStop() {
        Log.v("MainActivity", "OnStop()");
        super.onStop();
        unregisterReceiver(tickreceiver);
    }

    @Override
    protected void onStart() {
        Log.v("MainActivity", "OnStart()");
        super.onStart();
    }

    @Override
    protected void onResume() {
        Log.v("MainActivity", "OnResume()");
        super.onResume();
    }

    @Override
    protected void onRestart() {
        Log.v("MainActivity", "OnRestart()");
        super.onRestart();
    }

    @Override
    protected void onPause() {
        Log.v("MainActivity", "OnPtop()");
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        Log.v("MainActivity", "OnDestroy()");
        super.onDestroy();
    }
}