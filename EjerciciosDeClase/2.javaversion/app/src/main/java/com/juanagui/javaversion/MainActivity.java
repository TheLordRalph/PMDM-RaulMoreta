package com.juanagui.javaversion;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void sayIt() {
        var message = "I am a Java 10 inferred type running on Android";
        System.out.println(message);
    }
}