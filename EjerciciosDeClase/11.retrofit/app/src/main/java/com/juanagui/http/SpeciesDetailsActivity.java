package com.juanagui.http;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.juanagui.http.pokemon.PokemonService;
import com.juanagui.http.pokemon.SpeciesDetails;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SpeciesDetailsActivity extends AppCompatActivity {

    private static final String TAG = SpeciesDetailsActivity.class.getName();
    public final static String NAME_KEY = "NAME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_species_details);

        Intent intent = getIntent();
        String speciesName = intent.getStringExtra(NAME_KEY);

        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://pokeapi.co/api/v2/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        PokemonService _service = retrofit.create(PokemonService.class);
        Call<SpeciesDetails> speciesDetailsCall = _service.speciesDetails(speciesName);
        speciesDetailsCall.enqueue(new Callback<SpeciesDetails>() {
            @Override
            public void onResponse(Call<SpeciesDetails> call, Response<SpeciesDetails> response) {
                if (response.isSuccessful()) {
                    SpeciesDetails speciesDetails = response.body();
                    TextView speciesDetailNameTextView = findViewById(R.id.species_detail_name);
                    speciesDetailNameTextView.setText(speciesDetails.name);
                    if (speciesDetails.flavorTextEntries != null && !speciesDetails.flavorTextEntries.isEmpty()) {
                        TextView speciesDetailDescriptionTextView = findViewById(R.id.species_detail_description);
                        speciesDetailDescriptionTextView.setText(speciesDetails.flavorTextEntries.get(0).flavorText);
                    }
                } else {
                    Log.e(TAG, response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<SpeciesDetails> call, Throwable t) {
                Log.e(TAG, t.getLocalizedMessage());
            }
        });
    }
}