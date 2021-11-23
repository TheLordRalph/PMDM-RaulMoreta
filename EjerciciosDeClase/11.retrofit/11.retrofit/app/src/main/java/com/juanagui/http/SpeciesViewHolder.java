package com.juanagui.http;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.juanagui.http.pokemon.Species;

public class SpeciesViewHolder extends RecyclerView.ViewHolder {

    private final TextView _speciesNameTextView;
    private final SpeciesAdapter.OnSpeciesClickListener _onSpeciesClickListener;
    private Species _species;

    public SpeciesViewHolder(@NonNull View itemView, SpeciesAdapter.OnSpeciesClickListener _onSpeciesClickListener) {
        super(itemView);
        _speciesNameTextView = itemView.findViewById(R.id.species_name);
        this._onSpeciesClickListener = _onSpeciesClickListener;
        _speciesNameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (_species != null) {
                    _onSpeciesClickListener.onSpeciesClick(_species);
                }
            }
        });
    }

    public void bind(Species species) {
        _species = species;
        _speciesNameTextView.setText(species.name);
    }
}