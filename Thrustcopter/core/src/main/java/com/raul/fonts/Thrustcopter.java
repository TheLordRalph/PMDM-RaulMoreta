package com.raul.fonts;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Thrustcopter extends ApplicationAdapter {

	public static final int WIDTH = 800;
	public static final int HEIGHT = 480;

	private SpriteBatch batch;
	private Texture backGround;
	private Texture grassTexture;
	//private Sprite grassSprite;
	private FPSLogger fpsLogger;
	private OrthographicCamera camera;
	private TextureRegion backgroundTextureRegion;

	private TextureRegion belowTextureRegion;
	private TextureRegion aboveTextureRegion;

	private float terrainOffest = 0f;

	private Animation<Texture> planeAnimation;
	private Texture planeTexture1;
	private Texture planeTexture2;
	private Texture planeTexture3;
	private Sprite playerSprite;

	private float planeAnimTime;

	@Override
	public void create() {
		fpsLogger = new FPSLogger();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, WIDTH, HEIGHT);
		batch = new SpriteBatch();
		backGround = new Texture(Gdx.files.internal("background.png"));
		backgroundTextureRegion = new TextureRegion(backGround, backGround.getWidth(), backGround.getHeight());

		grassTexture = new Texture(Gdx.files.internal("groundGrass.png"));
		//grassSprite = new Sprite(grassTexture);
		//grassSprite.setPosition(0, 0);

		planeTexture1 = new Texture(Gdx.files.internal("planeRed1.png"));
		planeTexture2 = new Texture(Gdx.files.internal("planeRed2.png"));
		planeTexture3 = new Texture(Gdx.files.internal("planeRed3.png"));
		planeAnimation = new Animation(0.05f, planeTexture1, planeTexture2, planeTexture3, planeTexture2);
		planeAnimation.setPlayMode(Animation.PlayMode.LOOP);

		belowTextureRegion = new TextureRegion(grassTexture, grassTexture.getWidth(), grassTexture.getHeight());
		aboveTextureRegion = new TextureRegion(grassTexture, grassTexture.getWidth(), grassTexture.getHeight());
		aboveTextureRegion.flip(true, true);
	}

	@Override
	public void render() {
		fpsLogger.log();
		updateScene();
		drawScene();
	}

	private void updateScene() {
		terrainOffest -= 200 * Gdx.graphics.getDeltaTime();
		if (terrainOffest <= -belowTextureRegion.getRegionWidth()) {
			terrainOffest = 0f;
		}

		planeAnimTime += Gdx.graphics.getDeltaTime();
	}

	private void drawScene() {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		batch.draw(backgroundTextureRegion,0,0);
		//grassSprite.draw(batch);


		batch.draw(belowTextureRegion, terrainOffest, 0);
		batch.draw(belowTextureRegion, terrainOffest + belowTextureRegion.getRegionWidth(), 0);

		batch.draw(aboveTextureRegion, terrainOffest, HEIGHT-aboveTextureRegion.getRegionHeight());
		batch.draw(aboveTextureRegion, terrainOffest + aboveTextureRegion.getRegionWidth(), HEIGHT-aboveTextureRegion.getRegionHeight());

		batch.draw(planeAnimation.getKeyFrame(planeAnimTime), 1, backGround.getHeight() / 2);
		batch.end();
	}

	@Override
	public void dispose() {
		batch.dispose();
		backGround.dispose();
		grassTexture.dispose();
		planeTexture1.dispose();
		planeTexture2.dispose();
		planeTexture3.dispose();
	}

}