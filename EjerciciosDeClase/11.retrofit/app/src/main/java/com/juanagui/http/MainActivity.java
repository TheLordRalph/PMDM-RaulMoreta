package com.juanagui.http;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.juanagui.http.pokemon.PokemonService;
import com.juanagui.http.pokemon.Species;
import com.juanagui.http.pokemon.SpeciesChunk;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName();
    private PokemonService _service;
    private int _offset = 0;
    private static final int LIMIT = 20;
    private List<Species> _species = new ArrayList<Species>();
    private SpeciesAdapter _adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://pokeapi.co/api/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        _service = retrofit.create(PokemonService.class);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        _adapter = new SpeciesAdapter(_species, new SpeciesAdapter.OnSpeciesClickListener() {
            @Override
            public void onSpeciesClick(Species species) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, SpeciesDetailsActivity.class);
                intent.putExtra(SpeciesDetailsActivity.NAME_KEY, species.name);
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(_adapter);

        Button loadMoreButton = findViewById(R.id.load_more_button);
        loadMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadChunk(_offset, LIMIT);
            }
        });

        loadChunk(_offset, LIMIT);
    }

    private void loadChunk(int offset, int limit) {
        Call<SpeciesChunk> call = _service.listSpecies(offset, limit);
        call.enqueue(new Callback<SpeciesChunk>() {
            @Override
            public void onResponse(Call<SpeciesChunk> call, Response<SpeciesChunk> response) {
                if (response.isSuccessful()) {
                    List<Species> species = response.body().results;
                    _species.addAll(species);
                    for (Species specie : species) {
                        Log.i(TAG, specie.toString());
                    }
                    _adapter.notifyItemRangeInserted(offset, LIMIT);
                    _offset += LIMIT;
                } else {
                    Log.e(TAG, response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<SpeciesChunk> call, Throwable t) {
                Log.e(TAG, t.getLocalizedMessage());
            }
        });
    }
}