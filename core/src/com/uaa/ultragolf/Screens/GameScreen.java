package com.uaa.ultragolf.Screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.uaa.ultragolf.Actors.BarraDePoder;
import com.uaa.ultragolf.Actors.Nube;
import com.uaa.ultragolf.Actors.Pelota;
import com.uaa.ultragolf.Global.Constantes;
import com.uaa.ultragolf.Global.Point;
import com.uaa.ultragolf.Levels.LevelLoader;

import static com.uaa.ultragolf.Global.Constantes.PPM;
import java.awt.Dimension;
import java.util.ArrayList;

public class GameScreen extends ScreenAdapter {
    protected Game game;
    private Dimension mapSize;
    private Point holeLocation;
    private SpriteBatch batch;
    private Pelota pelota;
    private ArrayList<Nube> nubes;
    private OrthographicCamera camera, staticCamera;
    private World world;
    private BarraDePoder barraDePoder;
    private Box2DDebugRenderer physicsRenderer;
    private boolean pelotaEnfocada = false, tirar;
    private Body holeBody;
    private OrthogonalTiledMapRenderer mapRenderer;
    private TiledMap map;
    private int cameraZoom;
    public GameScreen(Game game) {
        this.game = game;
    }
    @Override
    public void show() {
        cameraZoom = 0;
        tirar = false;
        batch = new SpriteBatch();
        pelota = new Pelota();
        world = new World(Constantes.GRAVITY, true);
        physicsRenderer = new Box2DDebugRenderer();
        LevelLoader levelLoader = new LevelLoader(world, pelota.body, holeBody, 7, map);
        levelLoader.loadLevel();
        pelota.body = levelLoader.getPelotaBody();
        holeBody = levelLoader.getHoleBody();
        mapRenderer = levelLoader.loadMapRenderer();
        mapSize = levelLoader.getMapDimension();
        holeLocation = levelLoader.getHoleLocation();
        camera = new OrthographicCamera(Constantes.WIDTH / PPM, Constantes.HEIGHT / PPM);
        staticCamera = new OrthographicCamera(Constantes.WIDTH / PPM, Constantes.HEIGHT / PPM);

        camera.position.set(holeLocation.x,holeLocation.y,0);
        limitCamera();
        barraDePoder = new BarraDePoder(pelota.body,staticCamera);
        int n = 9+(int) (Math.random() * 6);
        nubes = new ArrayList<Nube>();
        for (int i = 0; i < n; i++) {
            nubes.add(new Nube(camera,mapSize));
        }
    }
    @Override
    public void render(float delta) {
        checkInputUser();
        world.step(1/60f,6,3);
        update(delta);
        Gdx.gl.glClearColor(12/255f, 183/255f, 242/255f,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        for(Nube nube : nubes) {
            nube.draw(batch, 1);
        }
        batch.end();

        mapRenderer.setView(camera);
        mapRenderer.render();
        batch.begin();
        pelota.draw(batch,1);
        batch.end();
        batch.setProjectionMatrix(staticCamera.combined);
        if(tirar) {
            batch.begin();
            barraDePoder.draw(batch, 1);
            batch.end();
        }
        //physicsRenderer.render(world,camera.combined);
    }

    private void update(float delta) {
        if(pelotaEnfocada)
            pelota.act(delta, camera);
        if(!pelotaEnfocada)
            enfocarPelota(delta);
        for (Nube nube : nubes)
            nube.act(delta);
        if (pelota.isMoving())
            updateCamera(pelota.body.getPosition().x,pelota.body.getPosition().y);
        else{
            arrastrarCamara();
        }
    }
    private void arrastrarCamara() {
        if(Gdx.input.isTouched()) {
            camera.translate(-Gdx.input.getDeltaX()/PPM,Gdx.input.getDeltaY()/PPM);
            limitCamera();
        }
        if(Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            if(cameraZoom < Constantes.MAX_ZOOM && (camera.viewportWidth < mapSize.getWidth()/PPM)) {
                cameraZoom++;
                camera.viewportWidth += Constantes.WIDTH / PPM / Constantes.ZOOM_SPEED;
                camera.viewportHeight += Constantes.HEIGHT / PPM / Constantes.ZOOM_SPEED;
                limitCamera();
            }
        }
        if(Gdx.input.isKeyPressed(Input.Keys.UP)) {
            if(cameraZoom > Constantes.MIN_ZOOM) {
                cameraZoom--;
                camera.viewportWidth -= Constantes.WIDTH / PPM / Constantes.ZOOM_SPEED;
                camera.viewportHeight -= Constantes.HEIGHT / PPM / Constantes.ZOOM_SPEED;
                limitCamera();
            }
        }
    }

    private void limitCamera() {
        if(camera.position.x < camera.viewportWidth/2)
            camera.position.x = camera.viewportWidth/2;
        if(camera.position.x > (mapSize.getWidth()/PPM-camera.viewportWidth/2))
            camera.position.x = (float) (mapSize.getWidth()/PPM-camera.viewportWidth/2);
        if(camera.position.y < camera.viewportHeight/2)
            camera.position.y = camera.viewportHeight/2;
        if(camera.position.y > (mapSize.getHeight()/PPM-camera.viewportHeight/2))
            camera.position.y = (float) (mapSize.getHeight()/PPM-camera.viewportHeight/2);
        camera.update();
    }

    private void updateCamera(float _x , float _y) {
        float camX = _x;
        float camY = _y;

        if(camX-(camera.viewportWidth/2)>=0 && camX+(camera.viewportWidth/2)<=mapSize.getWidth()/PPM) {
            camera.position.set(camX, camera.position.y, 0);
        }
        if(camY-(camera.viewportHeight/2)>=0 && camY+(camera.viewportHeight/2)<=mapSize.getHeight()/PPM) {
            camera.position.set(camera.position.x,camY, 0);
        }
        if((camX-(camera.viewportWidth/2)>=0 && camX+(camera.viewportWidth/2)<=mapSize.getWidth()/PPM) && (camY-(camera.viewportHeight/2)>=0 && camY+(camera.viewportHeight/2)<=mapSize.getHeight()/PPM)) {
            pelota.centered = true;
        } else {
            pelota.centered = false;
        }
        camera.update();
    }
    public void enfocarPelota(float delta) {
        float x0 = camera.position.x;
        float y0 = camera.position.y;
        if(camera.position.x > pelota.body.getPosition().x) camera.position.x -= 3*delta;
        if(camera.position.x < pelota.body.getPosition().x) camera.position.x += 3*delta;
        if(camera.position.y > pelota.body.getPosition().y) camera.position.y -= 3*delta;
        if(camera.position.y < pelota.body.getPosition().y) camera.position.y += 3*delta;
        if(cameraZoom < Constantes.MAX_ZOOM  && (camera.viewportWidth < mapSize.getWidth()/PPM) ) {
            cameraZoom++;
            camera.viewportWidth += Constantes.WIDTH / PPM / (Constantes.ZOOM_SPEED*2);
            camera.viewportHeight += Constantes.HEIGHT / PPM / (Constantes.ZOOM_SPEED*2);
        }
        limitCamera();
        float x1 = camera.position.x;
        float y1 = camera.position.y;
        if(Math.abs(x1-x0) <= 0 && Math.abs(y1-y0) <= 0) pelotaEnfocada = true;
    }
    private void checkInputUser() {
        if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER) && pelotaEnfocada){
            if(!tirar) {
                tirar = true;
                barraDePoder.resetPower();
            } else {
                camera.position.x = pelota.body.getPosition().x;
                camera.position.y = pelota.body.getPosition().y;
                limitCamera();
                pelota.body.applyForceToCenter(pelota.getXForce(barraDePoder.getPower()), pelota.getYForce(barraDePoder.getPower()), true);
                if (pelota.isFirstTime()) {
                    pelota.setFirstTime(false);
                }
                tirar = false;
            }
        }

        if(!pelota.isMoving() || pelota.isFirstTime()){
            if(Gdx.input.isKeyPressed(Input.Keys.LEFT) && pelotaEnfocada){
                pelota.setAngle(pelota.getAngle()+2);
            }
            if(Gdx.input.isKeyPressed(Input.Keys.RIGHT) && pelotaEnfocada){
                pelota.setAngle(pelota.getAngle()-2);
            }
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }
}
