package com.raul.fonts;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.particles.values.MeshSpawnShapeValue;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms.
 */
public class Thrustcopter extends ApplicationAdapter implements InputProcessor {


    public static final int WIDTH = 800;
    public static final int HEIGHT = 480;
    public static final float TERREIN_SPEED_PPS = 200f;
    public static final float BACKGROUND_SPEED_PPS = 20f;
    public static final float PLANE_TAP_VELOCITY = 300f;
    private static final int PILLAR_DISTANCE_RANGE = WIDTH/2;
    private static final int MIN_PILLAR_DISTANCE = WIDTH/2;
    private static final float NEW_PILLAR_POSITION_THRESHOLD = WIDTH/2f;

    private SpriteBatch batch;
    private TextureAtlas textureAtlas;
    private FPSLogger fpsLogger;
    private OrthographicCamera camera;
    //private FitViewport fitViewport;
    //private ExtendViewport extendViewport;
    private FillViewport fillViewport;
    //private ScreenViewport screenViewport;

    private TextureRegion backgroundTextureRegion;
    private TextureRegion belowTextureRegion;
    private TextureRegion aboveTextureRegion;

    private TextureRegion pillarUp;
    private TextureRegion pillarDown;
    private Array<Vector2> pillars = new Array<>();
    public Vector2 lastPillarPosition;

    private float terrainOffest = 0f;
    private float backgroundOffest = 0f;

    private Animation<TextureRegion> planeAnimation;
    private TextureRegion planeTexture1;
    private TextureRegion planeTexture2;
    private TextureRegion planeTexture3;

    private float planeAnimTime;
    private Vector2 defaultPlanePosition;
    private Vector2 planePosition;
    private Vector2 gravity;
    private Vector2 planeVelocity;
    private float damping = 0.99f;

    private final Rectangle planeBoundingBox = new Rectangle();
    private final Rectangle pillarBoundingBox = new Rectangle();

    private Sound creashSound;

    @Override
    public void create() {
        fpsLogger = new FPSLogger();
        camera = new OrthographicCamera();
        camera.position.set(WIDTH / 2, HEIGHT / 2, 0);
        fillViewport = new FillViewport(WIDTH, HEIGHT, camera);

        batch = new SpriteBatch();
        textureAtlas = new TextureAtlas(Gdx.files.internal("ThrustCopter.pack"));
        backgroundTextureRegion = textureAtlas.findRegion("background");

        belowTextureRegion = textureAtlas.findRegion("groundGrass");
        aboveTextureRegion = new TextureRegion(belowTextureRegion);
        aboveTextureRegion.flip(true, true);

        pillarUp = textureAtlas.findRegion("rockGrassUp");
        pillarDown = textureAtlas.findRegion("rockGrassDown");

        planeTexture1 = textureAtlas.findRegion("planeRed1");
        planeTexture2 = textureAtlas.findRegion("planeRed2");
        planeTexture3 = textureAtlas.findRegion("planeRed3");
        planeAnimation = new Animation(0.05f, planeTexture1, planeTexture2, planeTexture3, planeTexture2);
        planeAnimation.setPlayMode(Animation.PlayMode.LOOP);

        defaultPlanePosition = new Vector2(planeTexture1.getRegionWidth() / 2, HEIGHT / 2 - planeTexture1.getRegionHeight() / 2);
        planePosition = new Vector2(defaultPlanePosition);
        gravity = new Vector2(0, -10f);
        planeVelocity = new Vector2();
        Gdx.input.setInputProcessor(this);

        creashSound = Gdx.audio.newSound(Gdx.files.internal("crash.ogg"));

        addPillar();
    }

    @Override
    public void resize(int width, int height) {
        fillViewport.update(width, height);
    }

    @Override
    public void render() {
        fpsLogger.log();
        updateScene();
        drawScene();
    }

    private void updateScene() {
        terrainOffest -= TERREIN_SPEED_PPS * Gdx.graphics.getDeltaTime();
        if (terrainOffest <= -belowTextureRegion.getRegionWidth()) {
            terrainOffest = 0f;
        }
        backgroundOffest -= BACKGROUND_SPEED_PPS * Gdx.graphics.getDeltaTime();
        if (backgroundOffest <= -backgroundTextureRegion.getRegionWidth()) {
            backgroundOffest = 0f;
        }

        planeAnimTime += Gdx.graphics.getDeltaTime();
        planeVelocity.add(gravity);
        planeVelocity.scl(damping);
        planePosition.mulAdd(planeVelocity, Gdx.graphics.getDeltaTime());
        planePosition.x = defaultPlanePosition.x;
        planeBoundingBox.set(planePosition.x + planeTexture1.getRegionWidth()/4, planePosition.y + planeTexture1.getRegionHeight()/4, planeTexture1.getRegionWidth()-planeTexture1.getRegionWidth()/4, planeTexture1.getRegionHeight() -planeTexture1.getRegionHeight()/4);

        if (planePosition.y > HEIGHT-aboveTextureRegion.getRegionHeight()/2f || planePosition.y < belowTextureRegion.getRegionHeight()/2f) {
            gameOver();
        }

        for (Vector2 pillar : pillars) {
            pillar.x -= TERREIN_SPEED_PPS * Gdx.graphics.getDeltaTime();
            if (pillar.y ==1){
                pillarBoundingBox.set(pillar.x +30, 0, pillarUp.getRegionWidth()-60, pillarUp.getRegionHeight());
            } else {
                pillarBoundingBox.set(pillar.x +30, HEIGHT - pillarDown.getRegionHeight(), pillarDown.getRegionWidth() -60, pillarDown.getRegionHeight());
            }

            if (planeBoundingBox.overlaps(pillarBoundingBox)) {
                System.out.println("muerto");
                gameOver();
            }

            if (pillar.x<0-pillarUp.getRegionWidth()) {
                pillars.removeValue(pillar, false);
            }
        }

        if (lastPillarPosition.x < NEW_PILLAR_POSITION_THRESHOLD) {
            addPillar();
        }

        if (planePosition.y <= 0) {
            planePosition.y = 0;
        }

    }

    private void drawScene() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        batch.draw(backgroundTextureRegion, backgroundOffest, 0);
        batch.draw(backgroundTextureRegion, backgroundOffest + backgroundTextureRegion.getRegionWidth(), 0);

        for (Vector2 pillar : pillars) {
            if (pillar.y == 1) {
                batch.draw(pillarUp,pillar.x,0);
            } else {
                batch.draw(pillarDown,pillar.x,HEIGHT-pillarDown.getRegionHeight());
            }
        }


        batch.draw(belowTextureRegion, terrainOffest, 0);
        batch.draw(belowTextureRegion, terrainOffest + belowTextureRegion.getRegionWidth(), 0);

        batch.draw(aboveTextureRegion, terrainOffest, HEIGHT - aboveTextureRegion.getRegionHeight());
        batch.draw(aboveTextureRegion, terrainOffest + aboveTextureRegion.getRegionWidth(), HEIGHT - aboveTextureRegion.getRegionHeight());


        batch.draw(planeAnimation.getKeyFrame(planeAnimTime), planePosition.x, planePosition.y);

/*
        // Animacion
        TextureRegion keyFrame = planeAnimation.getKeyFrame(planeAnimTime);
        int rotation = 270;
        if (planeVelocity.y > 30) {
            rotation = 280;
        }
        if (planeVelocity.y < -30) {
            rotation = 260;
            batch.draw(keyFrame, planePosition.x, planePosition.y,
                    keyFrame.getRegionWidth() / 2.0f,
                    keyFrame.getRegionHeight() / 2.0f, keyFrame.getRegionWidth(),
                    keyFrame.getRegionHeight(), 1f, 1f, rotation, false);
        }
        batch.draw(keyFrame, planePosition.x, planePosition.y, keyFrame
                        .getRegionWidth() / 2.0f, keyFrame.getRegionHeight() / 2.0f,
                keyFrame.getRegionWidth(), keyFrame.getRegionHeight(),
                keyFrame.getRegionHeight() / (float) keyFrame.getRegionWidth(),
                2 - (keyFrame.getRegionHeight() / (float) keyFrame
                        .getRegionWidth()), rotation, false);
*/
        batch.end();
    }

    private void gameOver() {
        creashSound.play();
    }

    @Override
    public void dispose() {
        batch.dispose();
        textureAtlas.dispose();
        creashSound.dispose();
    }


    @Override
    public boolean keyDown(int i) {
        return false;
    }

    @Override
    public boolean keyUp(int i) {
        return false;
    }

    @Override
    public boolean keyTyped(char c) {
        return false;
    }

    @Override
    public boolean touchDown(int i, int i1, int i2, int i3) {
        planeVelocity.add(new Vector2(0, PLANE_TAP_VELOCITY));
        planeVelocity.add(new Vector2(0, 0));
        return true;
    }

    private void addPillar() {

        Vector2 tmPosition = new Vector2();
        if (pillars.size == 0) {
            tmPosition.x = (MIN_PILLAR_DISTANCE + (float) (PILLAR_DISTANCE_RANGE * Math.random()));
        } else {
            tmPosition.x = lastPillarPosition.x + MIN_PILLAR_DISTANCE + (float) (PILLAR_DISTANCE_RANGE * Math.random());
        }
        if (MathUtils.randomBoolean()) {
            tmPosition.y = 1;
        } else {
            tmPosition.y = -1;
        }
        lastPillarPosition = tmPosition;
        pillars.add(tmPosition);
    }

    @Override
    public boolean touchUp(int i, int i1, int i2, int i3) {
        return false;
    }

    @Override
    public boolean touchDragged(int i, int i1, int i2) {
        return false;
    }

    @Override
    public boolean mouseMoved(int i, int i1) {
        return false;
    }

    @Override
    public boolean scrolled(float v, float v1) {
        return false;
    }
}