package com.raul.intents.activity.intents.activity.memeapp;

public class Meme {

    public final String name;
    public final String topText;
    public final String bottomText;
    public final int imageResId;

    public Meme(String name, int imageResId, String topText, String bottomText) {
        this.name = name;
        this.topText = topText;
        this.bottomText = bottomText;
        this.imageResId = imageResId;
    }
}
