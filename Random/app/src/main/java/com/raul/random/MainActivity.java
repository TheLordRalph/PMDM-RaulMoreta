package com.raul.random;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static int _numeroAleatorio;
    private static int _numeroIntroducido;
    private int _intentosRestantes;
    private Intent _intent = new Intent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void onResume() {
        super.onResume();
        setContentView(R.layout.activity_main);

        // Generamos el número aleatorio con el metodo "numeroAleatorio" y se lo pasamos a la variable _numeroAleatorio
        _numeroAleatorio = numeroAleatorio();
        System.out.println(_numeroAleatorio);

        _intentosRestantes = 3;
        TextView intentos = findViewById(R.id.Intentos);
        intentos.setText(String.format("Numero de intentos: %d", _intentosRestantes));

        Button btn = findViewById(R.id.resolver);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText numeroIntroducido = findViewById(R.id.Numero_Introducido);
                _numeroIntroducido = Integer.parseInt(numeroIntroducido.getText().toString());
                _intent.setClass(MainActivity.this, ResultGame.class);


                if (_numeroIntroducido == _numeroAleatorio) {

                    _intent.putExtra("NumeroCorrecto", _numeroAleatorio);
                    _intent.putExtra("Win", true);
                    startActivity(_intent);
                } else {
                    --_intentosRestantes;
                    intentos.setText(String.format("Numero de intentos: %d", _intentosRestantes));
                    if (_intentosRestantes == 0) {
                        _intent.putExtra("NumeroCorrecto", _numeroAleatorio);
                        _intent.putExtra("NumeroErroneo", _numeroIntroducido);
                        _intent.putExtra("Win", false);
                        startActivity(_intent);
                    }
                }
            }
        });

    }

    private int numeroAleatorio() {
        return (int) (Math.random() * 10);
    }
}