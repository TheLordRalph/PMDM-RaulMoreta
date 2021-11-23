package com.juanagui.intents.activity.intents.activity.implicit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    public static final String GEO_0_0_Q_S = "geo:0,0?q=%s";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText input = findViewById(R.id.address);
        input.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    String address = Uri.encode(textView.getText().toString());
                    Intent mapIntent = new Intent(android.content.Intent.ACTION_VIEW);
                    Uri uri= Uri.parse(String.format(GEO_0_0_Q_S, address));
                    mapIntent.setData(uri);
                    startActivity(mapIntent);
                    return true;
                }
                return false;
            }
        });
    }
}