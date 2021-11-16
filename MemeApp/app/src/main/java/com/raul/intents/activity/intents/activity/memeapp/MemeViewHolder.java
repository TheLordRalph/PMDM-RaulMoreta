package com.raul.intents.activity.intents.activity.memeapp;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MemeViewHolder extends RecyclerView.ViewHolder {

    private final View _view;

    public MemeViewHolder(@NonNull View itemView) {
        super(itemView);
        _view = itemView;
    }

    public void bind(Meme meme) {
        TextView nameTextView = (TextView) _view.findViewById(R.id.name);
        nameTextView.setText(meme.name);

        TextView topTextView = (TextView) _view.findViewById(R.id.top_text);
        topTextView.setText(meme.topText);

        TextView bottomTextView = (TextView) _view.findViewById(R.id.bottom_text);
        bottomTextView.setText(meme.bottomText);

        ImageView imageView = (ImageView) _view.findViewById(R.id.imageview);
        imageView.setImageResource(meme.imageResId);
    }
}
