package com.raul.intents.activity.intents.activity.memeapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MemeAdapter extends RecyclerView.Adapter<MemeViewHolder> {

    private final List<Meme> _memes;

    public MemeAdapter(List<Meme> memes) {
        this._memes = memes;
    }
    @Override
    @NonNull
    public MemeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_name_item, parent, false);
        return new MemeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MemeViewHolder holder, int position) {
        Meme meme = _memes.get(position);
        holder.bind(meme);
    }

    @Override
    public int getItemCount() {
        return _memes.size();
    }
}
