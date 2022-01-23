package com.raul.fonts;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Fonts extends ApplicationAdapter {
	private SpriteBatch batch;
	private BitmapFont font;
	private GlyphLayout glyphLayout;
	private GlyphLayout multilineGlyphLayout;
	private GlyphLayout longGlyphLayout;
	private GlyphLayout topLeftGlyphLayout;
	private GlyphLayout topRightGlyphLayout;
	private GlyphLayout downLeftGlyphLayout;
	private GlyphLayout downRightGlyphLayout;

	@Override
	public void create() {
		batch = new SpriteBatch();
		font = new BitmapFont(Gdx.files.internal("a.fnt"));
		font.setColor(255f,0f,0f,255f);
		glyphLayout = new GlyphLayout();
		glyphLayout.setText(font, "Hola Mundo");

		//multilineGlyphLayout = new GlyphLayout(font, "Progrmación Multimedia");
		//longGlyphLayout = new GlyphLayout(font, "programacion multimedia y dispositvos moviles", font.getColor(), Gdx.graphics.getWidth()/4, Aling.topLeft());

		topLeftGlyphLayout = new GlyphLayout(font, "programacion multimedia", font.getColor(), Gdx.graphics.getWidth()/2, Align.topLeft, true);
		topRightGlyphLayout = new GlyphLayout(font, "programacion multimedia", font.getColor(), Gdx.graphics.getWidth()/2, Align.topLeft, true);
		downLeftGlyphLayout = new GlyphLayout(font, "programacion multimedia", font.getColor(), Gdx.graphics.getWidth()/2, Align.topLeft, true);
		downRightGlyphLayout = new GlyphLayout(font, "programacion multimedia", font.getColor(), Gdx.graphics.getWidth()/2, Align.topLeft, true);

	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0.15f, 0.15f, 0.2f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		font.draw(batch, topLeftGlyphLayout, 0, Gdx.graphics.getHeight());
		font.draw(batch, topRightGlyphLayout, 1, 1);
		font.draw(batch, downLeftGlyphLayout, 5, 5);
		font.draw(batch, downRightGlyphLayout, 8, 8);
		batch.end();
	}

	@Override
	public void dispose() {
		batch.dispose();
		font.dispose();
	}
}