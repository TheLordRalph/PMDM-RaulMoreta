package com.raul.intents.activity.intents.activity.a1sumaconstraintlayaout;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button addBtn = findViewById(R.id.calcular);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText firstNumberEditText = findViewById(R.id.primerNumero);
                EditText secondNumberEditText = findViewById(R.id.segundoNumero);
                Button suma = findViewById(R.id.suma);
                Button resta = findViewById(R.id.resta);
                Button mult = findViewById(R.id.multiplicacion);
                Button div = findViewById(R.id.division);

                int firsNumber = Integer.parseInt(firstNumberEditText.getText().toString());
                int secondNumber = Integer.parseInt(secondNumberEditText.getText().toString());

                if (suma.callOnClick() == true) {
                    TextView resultTextView = findViewById(R.id.resultado);
                    resultTextView.setText(String.format("%d", firsNumber + secondNumber));

                } else if (resta.callOnClick() == true) {
                    TextView resultTextView = findViewById(R.id.resultado);
                    resultTextView.setText(String.format("%d", firsNumber - secondNumber));

                }  else if (mult.callOnClick() == true) {
                    TextView resultTextView = findViewById(R.id.resultado);
                    resultTextView.setText(String.format("%d", firsNumber * secondNumber));

                }  else if (div.callOnClick() == true) {
                    TextView resultTextView = findViewById(R.id.resultado);
                    resultTextView.setText(String.format("%d", firsNumber / secondNumber));

                }


            }
        });



    }
}