package com.juanagui.activity.configurationchanges;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null)
            for (String key : savedInstanceState.keySet()) {
                Log.i(MainActivity.class.getName(), savedInstanceState.get(key).toString());
            }
        setContentView(R.layout.activity_main);
    }
}