package com.raul.intents.activity.intents.activity.memeapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<Meme> memes = Arrays.asList(
                new Meme("Grumpy Cat", R.drawable.grumpy_cat, "", "Good!"),
                new Meme("Brace Yourselves", R.drawable.brace_yourselves_x_is_coming, "Brace Yourselves", "Winter is Coming!"),
                new Meme("Futurama Fry", R.drawable.futurama_fry, "Not sure if ___", "Or just ___"),
                new Meme("One Does Not Simply", R.drawable.one_does_not_simply, "One does not simply", "Walk into mordor"),
                new Meme("Bad Luck Brian", R.drawable.bad_luck_brian, "", "Bad Luck Brian!"),
                new Meme("First World Problems", R.drawable.first_world_problems, "", ""),
                new Meme("Am I The Only One Around Here", R.drawable.am_i_the_only_one_around_here, "Am I The Only One Around Here", "Who ___"));

        RecyclerView recyclerviwe = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerviwe.setLayoutManager(new LinearLayoutManager(this));
        recyclerviwe.setAdapter(new MemeAdapter(memes));
    }
}