package com.raul.bsquedaweb;

import androidx.appcompat.app.AppCompatActivity;

import android.app.SearchManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import java.net.URI;

public class MainActivity extends AppCompatActivity {

    private static final String _WEB = "http://www.google.com/search?q=";
    private static EditText _editTextUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn = findViewById(R.id.Button);
        btn.setOnClickListener(view -> {

            _editTextUri = findViewById(R.id.URI);
            String address = Uri.encode(_editTextUri.getText().toString(), "UTF-8");

            Uri uri = Uri.parse(_WEB + address);
            Intent intentUri = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intentUri);
        });
    }
}