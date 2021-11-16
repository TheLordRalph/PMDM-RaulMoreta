package com.juanagui.recycler.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MemeAdapter extends RecyclerView.Adapter<MemeAdapter.MemeViewHolder> {

    public interface OnMemeClickListener {
        void onMemeClick(int position);
    }

    public class MemeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView _nameTextView;
        private final TextView _topTextView;
        private final ImageView _imageView;
        private final TextView _bottomTextView;
        private final OnMemeClickListener _onMemeClickListener;

        public MemeViewHolder(@NonNull View itemView, OnMemeClickListener onMemeClickListener) {
            super(itemView);
            _nameTextView = (TextView) itemView.findViewById(R.id.name);
            _topTextView = (TextView) itemView.findViewById(R.id.top_text);
            _imageView = (ImageView) itemView.findViewById(R.id.image);
            _bottomTextView = (TextView) itemView.findViewById(R.id.bottom_text);
            _onMemeClickListener = onMemeClickListener;
            itemView.setOnClickListener(this);
        }

        public void setName(String text) {
            _nameTextView.setText(text);
        }

        public void setTopText(String text) {
            _topTextView.setText(text);
        }

        public void setImageResource(int imageResId) {
            _imageView.setImageResource(imageResId);
        }

        public void setBottomText(String text) {
            _bottomTextView.setText(text);
        }

        @Override
        public void onClick(View view) {
            _onMemeClickListener.onMemeClick(getAdapterPosition());
        }
    }

    private List<Meme> _memes;
    private final OnMemeClickListener _onMemeClickListener;

    public MemeAdapter(List<Meme> memes, OnMemeClickListener onMemeClickListener) {
        _memes = memes;
        _onMemeClickListener = onMemeClickListener;
    }

    @NonNull
    @Override
    public MemeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_meme_item, parent, false);
        return new MemeViewHolder(view, _onMemeClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MemeViewHolder holder, int position) {
        Meme meme = _memes.get(position);
        holder.setName(meme.name);
        holder.setTopText(meme.topText);
        holder.setImageResource(meme.imageResId);
        holder.setBottomText(meme.bottomText);
    }

    @Override
    public int getItemCount() {
        return _memes.size();
    }

}