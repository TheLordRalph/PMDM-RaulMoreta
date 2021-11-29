package com.juanagui.recycler.view;

public class Meme {
    final String name;
    final int imageResId;
    final String topText;
    final String bottomText;

    public Meme(String name, int imageResId, String topText, String bottomText) {
        this.name = name;
        this.imageResId = imageResId;
        this.topText = topText;
        this.bottomText = bottomText;
    }
}