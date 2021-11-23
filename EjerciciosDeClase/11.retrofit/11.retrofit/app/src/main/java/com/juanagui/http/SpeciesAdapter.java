package com.juanagui.http;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.juanagui.http.pokemon.Species;

import java.util.List;

public class SpeciesAdapter extends RecyclerView.Adapter<SpeciesViewHolder> {

    public interface OnSpeciesClickListener {
        void onSpeciesClick(Species species);
    }

    private final List<Species> _speciesList;
    private final OnSpeciesClickListener _onSpeciesClickListener;

    public SpeciesAdapter(List<Species> speciesList, OnSpeciesClickListener onSpeciesClickListener) {

        _speciesList = speciesList;
        _onSpeciesClickListener = onSpeciesClickListener;
    }

    @NonNull
    @Override
    public SpeciesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_species_item, parent, false);
        SpeciesViewHolder speciesViewHolder = new SpeciesViewHolder(view, _onSpeciesClickListener);
        return speciesViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SpeciesViewHolder holder, int position) {
        holder.bind(_speciesList.get(position));
    }

    @Override
    public int getItemCount() {
        return _speciesList.size();
    }
}