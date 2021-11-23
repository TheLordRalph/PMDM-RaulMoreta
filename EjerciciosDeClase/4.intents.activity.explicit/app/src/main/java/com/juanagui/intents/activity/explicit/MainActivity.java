package com.juanagui.intents.activity.explicit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText firstNum = findViewById(R.id.first_number);
        final EditText secondNum = findViewById(R.id.second_number);
        Button btn = findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int num1 = Integer.parseInt(firstNum.getText().toString());
                int num2 = Integer.parseInt(secondNum.getText().toString());
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, ResultActivity.class);
                intent.putExtra("NUM1",num1);
                intent.putExtra("NUM2",num2);
                startActivity(intent);
            }
        });

    }
}

