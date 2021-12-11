package com.raul.random;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ResultGame extends AppCompatActivity {

    private static int numeroAleatorio;
    private static int numeroIntroducido;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.win_activity);

        Intent intent = getIntent();
        numeroAleatorio = (int) intent.getSerializableExtra("NumeroCorrecto");
        if ((boolean) intent.getSerializableExtra("Win")) {

            setContentView(R.layout.win_activity);
            TextView numeroCorrecto = findViewById(R.id.Numero_Correcto1);
            numeroCorrecto.setText(String.valueOf(numeroAleatorio));
        } else {

            setContentView(R.layout.fail_activity);
            numeroIntroducido = (int) intent.getSerializableExtra("NumeroErroneo");

            TextView numeroCorrecto = findViewById(R.id.Numero_Correcto2);
            numeroCorrecto.setText(String.valueOf(numeroAleatorio));

            TextView numeroIncorrecto = findViewById(R.id.Numero_Incorrecto);
            numeroIncorrecto.setText(String.valueOf(numeroIntroducido));

        }
    }
}
