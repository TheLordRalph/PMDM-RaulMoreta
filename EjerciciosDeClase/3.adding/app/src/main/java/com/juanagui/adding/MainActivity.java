package com.juanagui.adding;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = (Button) findViewById(R.id.addButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText firstInput = (EditText) findViewById(R.id.firstNumberInut);
                EditText secondInput = (EditText) findViewById(R.id.secondNumberInput);
                int firstNumber = Integer.parseInt(firstInput.getText().toString());
                int secondNumber = Integer.parseInt(secondInput.getText().toString());
                int sum = firstNumber + secondNumber;
                TextView addOutput = (TextView) findViewById(R.id.addOutput);
                addOutput.setText(String.format("%d", sum));
                //addOutput.setText(String.valueOf(sum));
            }
        });
    }

}