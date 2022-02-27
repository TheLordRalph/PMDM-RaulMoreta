package com.raul.dam;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class ThrustcopterGame extends ScreenAdapter implements InputProcessor {
    HelloWorldGame game;


    private float start;
    private static final int TIEMPO_START = 3;

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

    // CazaTie
    private Vector2 lastCazaTie;

    // TextureRegion
    private TextureRegion backgroundTextureRegion;
    private TextureRegion belowTextureRegion;
    private TextureRegion aboveTextureRegion;

    private TextureRegion pillarUp;
    private TextureRegion pillarDown;

    private TextureRegion cazaTie;

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
    private final Rectangle puntosBoundingBox = new Rectangle();
    private final Rectangle cazaTieBoundingBox = new Rectangle();

    // Text
    GlyphLayout time;
    GlyphLayout points;

    private float timeSeconds = 0f;
    private float period = 1f;
    private int second = 0;
    private int minute = 0;

    private int puntos = 0;

    public ThrustcopterGame(HelloWorldGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        // Camara
        camera = new OrthographicCamera();
        camera.position.set(game.WIDTH/2, game.HEIGHT/2, 0);
        fitViewport = new FitViewport(game.WIDTH, game.HEIGHT, camera);

        // TextureRegion
        textureAtlas = new TextureAtlas(Gdx.files.internal("assetsSW.atlas"));
        backgroundTextureRegion = textureAtlas.findRegion("backgrounds");

        belowTextureRegion = textureAtlas.findRegion("terrain1");
        aboveTextureRegion = new TextureRegion(belowTextureRegion);
        aboveTextureRegion.flip(true, true);

        pillarUp = textureAtlas.findRegion("turbolaser");
        pillarDown = new TextureRegion(pillarUp);
        pillarDown.flip(true, true);

        cazaTie = textureAtlas.findRegion("cazaTie");

        planeTexture1 = textureAtlas.findRegion("XWing1");
        planeTexture2 = textureAtlas.findRegion("XWing2");
        planeTexture3 = textureAtlas.findRegion("XWing3");

        // Animacion
        planeAnimation = new Animation(0.05f, planeTexture1, planeTexture2, planeTexture3, planeTexture2);
        planeAnimation.setPlayMode(Animation.PlayMode.LOOP);

        // PlanePosition
        defaultPlanePosition = new Vector2(planeTexture1.getRegionWidth() / 2, game.HEIGHT / 2 - planeTexture1.getRegionHeight() / 2);
        planePosition = new Vector2(defaultPlanePosition);
        gravity = new Vector2(0, -10f);
        planeVelocity = new Vector2();
        Gdx.input.setInputProcessor(this);

        game.muerte = Gdx.audio.newMusic(Gdx.files.internal("sound/muerte.mp3"));
        game.musicGame = Gdx.audio.newMusic(Gdx.files.internal("sound/MusicGame.mp3"));
        game.musicGame.play();
        game.musicGame.setLooping(true);

        addCazaTie();
        addPillar();
    }

    @Override
    public void resize(int width, int height) {
        fitViewport.update(width, height);
    }

    @Override
    public void render(float delta) {
            timeSeconds += Gdx.graphics.getDeltaTime();
            if (timeSeconds > period) {
                second += timeSeconds;
                game.fileTime.writeString(String.format("%02d : %02d", minute, second), false);
                if (second >= 60) {
                    second = 0;
                    minute++;
                }
                timeSeconds -= period;
            }
            updateScene();
            drawScene();
    }

    private void updateScene() {
        terrainOffest -= game.TERREIN_SPEED_PPS * Gdx.graphics.getDeltaTime();
        if (terrainOffest <= -belowTextureRegion.getRegionWidth()) {
            terrainOffest = 0f;
        }
        backgroundOffest -= game.BACKGROUND_SPEED_PPS * Gdx.graphics.getDeltaTime();
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
        if (planePosition.y > game.HEIGHT-game.HEIGHT/3 || planePosition.y < game.HEIGHT/7) {
            gameOver();
        }

        for (Vector2 pillar : pillars) {
            pillar.x -= game.TERREIN_SPEED_PPS * Gdx.graphics.getDeltaTime();
            if (pillar.y ==1){
                pillarBoundingBox.set(pillar.x + 10, 0, pillarUp.getRegionWidth() - 20, pillarUp.getRegionHeight() - 10);
            } else {
                pillarBoundingBox.set(pillar.x + 10,game.HEIGHT - pillarDown.getRegionHeight() + 10, pillarUp.getRegionWidth() - 20, pillarUp.getRegionHeight());
            }

            puntosBoundingBox.set(pillar.x + pillarUp.getRegionWidth(), 0, 0, game.HEIGHT);


            if (planeBoundingBox.overlaps(pillarBoundingBox)) {
                gameOver();
            } else if (planeBoundingBox.overlaps(puntosBoundingBox)) {
                puntos++;
            }

            if (pillar.x<0-pillarUp.getRegionWidth()) {
                pillars.removeValue(pillar, false);
            }
        }

        lastCazaTie.x -= 4;
        cazaTieBoundingBox.set(lastCazaTie.x+cazaTie.getRegionWidth()/4, lastCazaTie.y+cazaTie.getRegionHeight()/4, cazaTie.getRegionWidth()-cazaTie.getRegionWidth()/2, cazaTie.getRegionHeight()-cazaTie.getRegionHeight()/2);

        if (planeBoundingBox.overlaps(cazaTieBoundingBox)) {
            gameOver();
        }

        if (MathUtils.randomBoolean()) {
            if (lastPillarPosition.x < game.NEW_PILLAR_POSITION_THRESHOLD) {
                    addPillar();
                }
            } else {
            if (lastCazaTie.x < 0) {
                addCazaTie();
            }
        }


        // Text
        time = new GlyphLayout(game.fontGame, String.format("%02d : %02d", minute, second), game.fontGame.getColor(), Gdx.graphics.getWidth()/2, Align.center, true);
        points = new GlyphLayout(game.fontGame, String.format("Puntos: %d", puntos), game.fontGame.getColor(), Gdx.graphics.getWidth()/2, Align.center, true);
    }

    private void drawScene() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();

        game.batch.draw(backgroundTextureRegion, backgroundOffest, 0);
        game.batch.draw(backgroundTextureRegion, backgroundOffest + backgroundTextureRegion.getRegionWidth(), 0);

        game.batch.draw(belowTextureRegion, terrainOffest, 0 - game.HEIGHT/3);
        game.batch.draw(belowTextureRegion, terrainOffest + belowTextureRegion.getRegionWidth(), 0 - game.HEIGHT/3);

        game.batch.draw(aboveTextureRegion, terrainOffest, game.HEIGHT - game.HEIGHT/5);
        game.batch.draw(aboveTextureRegion, terrainOffest + aboveTextureRegion.getRegionWidth(), game.HEIGHT - game.HEIGHT/5);


        for (Vector2 pillar : pillars) {
            if (pillar.y == 1) {
                game.batch.draw(pillarUp,pillar.x,0);
            } else {
                game.batch.draw(pillarDown,pillar.x,game.HEIGHT-pillarDown.getRegionHeight());
            }
        }

        game.batch.draw(cazaTie, lastCazaTie.x, lastCazaTie.y);

        /*game.batch.draw(escudo, posicionEscudo.x, posicionEscudo.y);

        if (escudoActivado) {
            game.batch.draw(escudo1, planePosition.x-planeTexture1.getRegionWidth()/5, planePosition.y-planeTexture1.getRegionHeight()/5);
        }*/

        // FONTS
        // time
        game.fontGame.draw(game.batch, time, game.WIDTH/2-game.WIDTH/2+time.width,  game.HEIGHT);
        // points
        game.fontGame.draw(game.batch, points, game.WIDTH/2,  game.HEIGHT);


        // Animacion

        TextureRegion keyFrame = planeAnimation.getKeyFrame(planeAnimTime);
        int rotation = 270;
        if (planeVelocity.y > 30) {
            rotation = 280;
            game.batch.draw(keyFrame, planePosition.x, planePosition.y,
                    keyFrame.getRegionWidth() / 2.0f,
                    keyFrame.getRegionHeight() / 2.0f, keyFrame.getRegionWidth(),
                    keyFrame.getRegionHeight(), 1f, 1f, rotation, false);
        } else if (planeVelocity.y < -30) {
            rotation = 260;
            game.batch.draw(keyFrame, planePosition.x, planePosition.y,
                    keyFrame.getRegionWidth() / 2.0f,
                    keyFrame.getRegionHeight() / 2.0f, keyFrame.getRegionWidth(),
                    keyFrame.getRegionHeight(), 1f, 1f, rotation, false);
        } else {
            game.batch.draw(keyFrame, planePosition.x, planePosition.y,
                    keyFrame.getRegionWidth() / 2.0f,
                    keyFrame.getRegionHeight() / 2.0f, keyFrame.getRegionWidth(),
                    keyFrame.getRegionHeight(), 1f, 1f, rotation, false);
        }

        game.batch.end();
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }


    private void addPillar() {

        Vector2 tmPosition = new Vector2();
        if (pillars.size == 0) {
            tmPosition.x = (game.MIN_PILLAR_DISTANCE + (float) (game.PILLAR_DISTANCE_RANGE * Math.random()));
        } else {
            tmPosition.x = lastPillarPosition.x + game.MIN_PILLAR_DISTANCE + (float) (game.PILLAR_DISTANCE_RANGE * Math.random());
        }
        if (MathUtils.randomBoolean()) {
            tmPosition.y = 1;
        } else {
            tmPosition.y = -1;
        }
        lastPillarPosition = tmPosition;
        pillars.add(tmPosition);
    }

    private void addCazaTie() {

        Vector2 tmPosition = new Vector2();

        tmPosition.x = game.WIDTH;
        tmPosition.y = game.HEIGHT/2 - cazaTie.getRegionHeight()/2;

        lastCazaTie = tmPosition;
    }


    private void gameOver() {
        game.musicGame.stop();
        game.muerte.play();
        game.setScreen(new EndScreen(game));
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
        planeVelocity.add(new Vector2(0, game.PLANE_TAP_VELOCITY));
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