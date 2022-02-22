package com.raul.fonts;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FillViewport;

public class HelloWorldGame extends Game {

    SpriteBatch batch;
    BitmapFont font;

    @Override
    public void create () {
        // Batch
        batch = new SpriteBatch();
        font = new BitmapFont();
        setScreen(new TitleScreen(this));
    }

    @Override
    public void dispose () {
        batch.dispose();
        font.dispose();
        //creashSound.dispose();
    }
}
