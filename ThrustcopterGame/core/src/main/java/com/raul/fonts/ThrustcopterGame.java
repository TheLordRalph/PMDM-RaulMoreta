package com.raul.fonts;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.text.DecimalFormat;

import sun.font.CreatedFontTracker;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class ThrustcopterGame extends ScreenAdapter implements InputProcessor {
    HelloWorldGame game;

    // Statics Finals
    public static final int WIDTH = 800;
    public static final int HEIGHT = 480;
    public static final float TERREIN_SPEED_PPS = 200f;
    public static final float BACKGROUND_SPEED_PPS = 20f;
    public static final float PLANE_TAP_VELOCITY = 300f;
    private static final int PILLAR_DISTANCE_RANGE = WIDTH/2;
    private static final int MIN_PILLAR_DISTANCE = WIDTH/2;
    private static final float NEW_PILLAR_POSITION_THRESHOLD = WIDTH/2f;


    // Camara
    private OrthographicCamera camera;
    private FitViewport fitViewport;

    // Sprites
    TextureAtlas textureAtlas;

    // Pillars Terrain
    private Array<Vector2> pillars = new Array<>();
    public Vector2 lastPillarPosition;

    private float terrainOffest = 0f;
    private float backgroundOffest = 0f;

    // TextureRegion
    private TextureRegion backgroundTextureRegion;
    private TextureRegion belowTextureRegion;
    private TextureRegion aboveTextureRegion;

    private TextureRegion pillarUp;
    private TextureRegion pillarDown;

    private TextureRegion planeTexture1;
    private TextureRegion planeTexture2;
    private TextureRegion planeTexture3;

    // Animaciones
    private Animation<TextureRegion> planeAnimation;
    private float planeAnimTime;

    // Plane
    private Vector2 defaultPlanePosition;
    private Vector2 planePosition;
    private Vector2 gravity;
    private Vector2 planeVelocity;
    private float damping = 0.99f;

    // HitBox
    private final Rectangle planeBoundingBox = new Rectangle();
    private final Rectangle pillarBoundingBox = new Rectangle();

    // Sonido y musica
    //Sound creashSound;

    // Text
    GlyphLayout time;
    GlyphLayout points;

    private float timeSeconds = 0f;
    private float period = 1f;
    private int second = 0;
    private int minute = 0;

    public ThrustcopterGame(HelloWorldGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        // Camara
        camera = new OrthographicCamera();
        camera.position.set(WIDTH/2, HEIGHT/2, 0);
        fitViewport = new FitViewport(WIDTH, HEIGHT, camera);

        // TextureRegion
        textureAtlas = new TextureAtlas(Gdx.files.internal("pack.atlas"));
        backgroundTextureRegion = textureAtlas.findRegion("backgrounds");

        belowTextureRegion = textureAtlas.findRegion("terrain1");
        aboveTextureRegion = new TextureRegion(belowTextureRegion);
        aboveTextureRegion.flip(true, true);

        pillarUp = textureAtlas.findRegion("turbolaser");
        pillarDown = new TextureRegion(pillarUp);
        pillarDown.flip(true, true);

        planeTexture1 = textureAtlas.findRegion("XWing1");
        planeTexture2 = textureAtlas.findRegion("XWing2");
        planeTexture3 = textureAtlas.findRegion("XWing3");

        // Animacion
        planeAnimation = new Animation(0.05f, planeTexture1, planeTexture2, planeTexture3, planeTexture2);
        planeAnimation.setPlayMode(Animation.PlayMode.LOOP);

        // PlanePosition
        defaultPlanePosition = new Vector2(planeTexture1.getRegionWidth() / 2, HEIGHT / 2 - planeTexture1.getRegionHeight() / 2);
        planePosition = new Vector2(defaultPlanePosition);
        gravity = new Vector2(0, -10f);
        planeVelocity = new Vector2();
        Gdx.input.setInputProcessor(this);

        // Sonido Musica
        //creashSound = Gdx.audio.newSound(Gdx.files.internal("crash.ogg"));

        // Text
        time = new GlyphLayout(game.font, String.format("%02d : %02d", minute, second), game.font.getColor(), Gdx.graphics.getWidth()/2, Align.topRight, true);
        points = new GlyphLayout(game.font, String.format("Puntos: 0"), game.font.getColor(), Gdx.graphics.getWidth()/2, Align.topRight, true);


        addPillar();
    }

    @Override
    public void resize(int width, int height) {
        fitViewport.update(width, height);
    }

    @Override
    public void render(float delta) {
        timeSeconds += Gdx.graphics.getRawDeltaTime();
        if(timeSeconds > period){
            second += timeSeconds;
            if (second >= 60) {
                second = 0;
                minute++;
            }
            timeSeconds-=period;
            handleEvent();
        }

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
        planeBoundingBox.set(planePosition.x + planeTexture1.getRegionWidth()/4, planePosition.y + planeTexture1.getRegionHeight()/4, planeTexture1.getRegionWidth()-planeTexture1.getRegionWidth()/2, planeTexture1.getRegionHeight()-planeTexture1.getRegionHeight()/2);
        
        // COMPROBACION MUERTE POR SUELO O TECHO
        if (planePosition.y > HEIGHT-HEIGHT/3 || planePosition.y < HEIGHT/7) {
            game.setScreen(new EndScreen(game));
        }

        for (Vector2 pillar : pillars) {
            pillar.x -= TERREIN_SPEED_PPS * Gdx.graphics.getDeltaTime();
            if (pillar.y ==1){
                pillarBoundingBox.set(pillar.x + 30, 0, pillarUp.getRegionWidth() - 60, pillarUp.getRegionHeight() - 20);
            } else {
                pillarBoundingBox.set(pillar.x + 30, pillar.y, pillarDown.getRegionWidth() - 60, pillarDown.getRegionHeight());
            }

            if (planeBoundingBox.overlaps(pillarBoundingBox)) {
                System.out.println("muerto");
                game.setScreen(new EndScreen(game));
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
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();

        game.batch.draw(backgroundTextureRegion, backgroundOffest, 0);
        game.batch.draw(backgroundTextureRegion, backgroundOffest + backgroundTextureRegion.getRegionWidth(), 0);

        game.batch.draw(belowTextureRegion, terrainOffest, 0 - HEIGHT/3);
        game.batch.draw(belowTextureRegion, terrainOffest + belowTextureRegion.getRegionWidth(), 0 - HEIGHT/3);

        game.batch.draw(aboveTextureRegion, terrainOffest, HEIGHT - HEIGHT/5);
        game.batch.draw(aboveTextureRegion, terrainOffest + aboveTextureRegion.getRegionWidth(), HEIGHT - HEIGHT/5);


        for (Vector2 pillar : pillars) {
            if (pillar.y == 1) {
                game.batch.draw(pillarUp,pillar.x,0);
            } else {
                game.batch.draw(pillarDown,pillar.x,HEIGHT-pillarDown.getRegionHeight());
            }
        }


        game.batch.draw(planeAnimation.getKeyFrame(planeAnimTime), planePosition.x, planePosition.y);

        // FONTS
        // time
        game.font.draw(game.batch, time, WIDTH/2,  HEIGHT);
        // points
        game.font.draw(game.batch, points, WIDTH/2,  HEIGHT-20);

        game.batch.end();
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
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


    public void handleEvent() {

    }


    // InputProcessor

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