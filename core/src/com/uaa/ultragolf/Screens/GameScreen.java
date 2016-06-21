package com.uaa.ultragolf.Screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.physics.box2d.*;
import com.uaa.ultragolf.Actors.BarraDePoder;
import com.uaa.ultragolf.Actors.Nube;
import com.uaa.ultragolf.Actors.Pelota;
import com.uaa.ultragolf.Global.Constantes;
import com.uaa.ultragolf.Global.Info;
import com.uaa.ultragolf.Global.Point;
import com.uaa.ultragolf.Levels.LevelLoader;

import static com.uaa.ultragolf.Global.Constantes.PPM;
import java.awt.Dimension;
import java.util.ArrayList;

public class GameScreen extends ScreenAdapter {
    private Info gameInfo;//Informacion del estado del juego (tiros restantes y el hoyo en el que vamos)
    protected Game game; //Objeto game para cambiar de nivel
    private boolean bolaCaida;  //Bandera para saber si la pelota cayo al agua
    private BitmapFont font;    //Objeto para escribir texto en pantall
    private Point oldPosition;  //Punto donde estuvo por ultimo la pelota (por si se cae al agua)
    private Dimension mapSize;   //Tamaño del mapa en curso
    private Point holeLocation;  //Posicion del hoyo
    private SpriteBatch batch,batch2; //SB principal para pintar el mundo y la pelota, y SB2 para pintar la info del juego
    private boolean bolaActivada = false; //Saber si pelota esta estatica
    private Sound golpe, rebote, goal, ultra, dirt, agua; //Sonidos del juego
    private Pelota pelota;          //Pelota principal
    private ArrayList<Nube> nubes;  //Nubes
    private OrthographicCamera camera, staticCamera; //Camara completa, y la que se mueve
    private World world;    //Manjeador de la fisica
    private BarraDePoder barraDePoder;  //Actor barra de poder
    private boolean pelotaEnfocada = false, tirar,animarGolfista;
    private Body holeBody, watter; //Objetos fisicos para el hoyo y el agua
    boolean watterAlive;        //Saber si el nivel actual usa agua
    private OrthogonalTiledMapRenderer mapRenderer;     //Renderizador de mapa
    private TiledMap map;       //Objeto para leer el tiled map
    private float potenciaCapturada;    //Nivel de fierza extraida del la barra de poder
    int level;                      //Nivel actual
    private int cameraZoom;         //Zoom de la camara
    public GameScreen(Game game,int level) {
        this.game = game;
        this.level = level;
        gameInfo = new Info();
        bolaCaida = false;
    }

    public GameScreen(Game game, int level, int balls, Point pelotaLocation) {
        this(game,level);
        gameInfo.setBolas(balls);
        bolaCaida = true;
        oldPosition = new Point(pelotaLocation.x,pelotaLocation.y);
    }

    @Override
    public void show() {
        if(level >= Constantes.levels.length){ game.setScreen(new GameOverScreen(game,true));}else {

        golpe = Gdx.audio.newSound(Gdx.files.getFileHandle("sounds/golpe.wav", Files.FileType.Local));
        goal = Gdx.audio.newSound(Gdx.files.getFileHandle("sounds/goal.ogg", Files.FileType.Local));
        rebote = Gdx.audio.newSound(Gdx.files.getFileHandle("sounds/rebote1.wav", Files.FileType.Local));
        ultra = Gdx.audio.newSound(Gdx.files.getFileHandle("sounds/ultra.ogg", Files.FileType.Local));
        dirt = Gdx.audio.newSound(Gdx.files.getFileHandle("sounds/dirt.wav", Files.FileType.Local));
        agua = Gdx.audio.newSound(Gdx.files.getFileHandle("sounds/agua.wav", Files.FileType.Local));
            watterAlive = true;
            cameraZoom = 0;
            tirar = false;
            animarGolfista = false;
            batch = new SpriteBatch();
            batch2 = new SpriteBatch();
            pelota = new Pelota();
            world = new World(Constantes.GRAVITY, true);//True para evitar calculos de fisica inecesarios
            LevelLoader levelLoader = new LevelLoader(world, pelota.body, holeBody, Constantes.levels[level], map);
            if (bolaCaida)
                levelLoader.setPelotaRestore(oldPosition);
            levelLoader.loadLevel();
            pelota.body = levelLoader.getPelotaBody();
            holeBody = levelLoader.getHoleBody();
            watter = levelLoader.getWatterBody();
            if (watter == null) watterAlive = false;
            font = new BitmapFont();
            mapRenderer = levelLoader.loadMapRenderer();
            mapSize = levelLoader.getMapDimension();
            holeLocation = levelLoader.getHoleLocation();
            camera = new OrthographicCamera(Constantes.WIDTH / PPM, Constantes.HEIGHT / PPM);
            staticCamera = new OrthographicCamera(Constantes.WIDTH / PPM, Constantes.HEIGHT / PPM);
            camera.position.set(holeLocation.x, holeLocation.y, 0);
            limitCamera();
            barraDePoder = new BarraDePoder(pelota.body, staticCamera);
            int n = 9 + (int) (Math.random() * 6);
            nubes = new ArrayList<Nube>();
            world.setContactListener(new ContactListener() {
                @Override
                public void beginContact(Contact contact) {

                    if(getType(contact.getFixtureA() , contact.getFixtureB()).equals("common")) {
                        if(pelota.body.getLinearVelocity().x > 4 || pelota.body.getLinearVelocity().y > 4)
                         rebote.play();
                    }
                    if(getType(contact.getFixtureA() , contact.getFixtureB()).equals("tierra")){
                        pelota.body.setLinearVelocity(pelota.body.getLinearVelocity().x/70,pelota.body.getLinearVelocity().y/70);
                        dirt.play();
                    }
                    if(getType(contact.getFixtureA() , contact.getFixtureB()).equals("hole")) {
                        goal.play();
                    }
                    if ((contact.getFixtureA() == pelota.body.getFixtureList().first() && contact.getFixtureB() == holeBody.getFixtureList().first()) ||
                            (contact.getFixtureB() == pelota.body.getFixtureList().first() && contact.getFixtureA() == holeBody.getFixtureList().first())) {
                        game.setScreen(new GameScreen(game, level + 1));
                    }

                            if (watterAlive) {
                                if ((contact.getFixtureA() == pelota.body.getFixtureList().first() && contact.getFixtureB() == watter.getFixtureList().first()) ||
                                        (contact.getFixtureB() == pelota.body.getFixtureList().first() && contact.getFixtureA() == watter.getFixtureList().first())) {
                                    agua.play();
                                    gameInfo.decrementaBolas();
                                    if (!gameInfo.gameOver()) {
                                        game.setScreen(new GameScreen(game, level, gameInfo.getBolasRestantes(), oldPosition));
                                    } else {
                                        game.setScreen(new GameScreen(game, level));
                            }
                        }
                    }
                }

                @Override
                public void endContact(Contact contact) {
                }

                @Override
                public void preSolve(Contact contact, Manifold oldManifold) {

                }

                @Override
                public void postSolve(Contact contact, ContactImpulse impulse) {
                }
            });
            for (int i = 0; i < n; i++) {
                nubes.add(new Nube(camera, mapSize));
            }
        }
    }

    private String getType(Fixture A , Fixture B ){
        if(A.getUserData() != null){
            return (String) A.getUserData();
        }
        else if(B.getUserData() != null){
            return (String) B.getUserData();
        }
        return "";
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

        if(tirar && animarGolfista) {
            batch.begin();
            barraDePoder.draw(batch, 1);
            batch.end();
        } else if(tirar){
            batch.begin();
            barraDePoder.drawOnly(batch, 1);
            batch.end();
        }
        batch2.begin();
        font.draw(batch2,"Tiros restantes: "+gameInfo.getBolasRestantes(),Gdx.graphics.getWidth()-400,Gdx.graphics.getHeight()-font.getXHeight());
        font.draw(batch2,"Hoyo: "+(level+1),Gdx.graphics.getWidth()-200,Gdx.graphics.getHeight()-font.getXHeight());
        batch2.end();
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
        if(bolaActivada){
            if(!pelota.body.isAwake()){
                bolaActivada = false;
                gameInfo.decrementaBolas();
                if(gameInfo.gameOver()){
                    game.setScreen(new GameScreen(game,level));
                }
            }
        }
        checkTiro();
    }

    private void checkTiro() {
         if(tirar && !pelota.isGolfistaAnimating() && !animarGolfista){
            camera.position.x = pelota.body.getPosition().x;
            camera.position.y = pelota.body.getPosition().y;
            limitCamera();
            bolaActivada = true;
                pelota.body.applyForceToCenter(pelota.getXForce(potenciaCapturada), pelota.getYForce(potenciaCapturada), true);
                if(potenciaCapturada > 350)
                    ultra.play();
                else
                    golpe.play();
            if (pelota.isFirstTime()) {
                pelota.setFirstTime(false);
            }
            tirar = false;
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
        if(!bolaCaida){
            float x0 = camera.position.x;
            float y0 = camera.position.y;
            if(camera.position.x > pelota.body.getPosition().x) camera.position.x -= 6*delta;
            if(camera.position.x < pelota.body.getPosition().x) camera.position.x += 6*delta;
            if(camera.position.y > pelota.body.getPosition().y) camera.position.y -= 6*delta;
            if(camera.position.y < pelota.body.getPosition().y) camera.position.y += 6*delta;
            if(cameraZoom < Constantes.MAX_ZOOM  && (camera.viewportWidth < mapSize.getWidth()/PPM) ) {
                cameraZoom++;
                camera.viewportWidth += Constantes.WIDTH / PPM / (Constantes.ZOOM_SPEED*2);
                camera.viewportHeight += Constantes.HEIGHT / PPM / (Constantes.ZOOM_SPEED*2);
            }
            limitCamera();
            float x1 = camera.position.x;
            float y1 = camera.position.y;
            if(Math.abs(x1-x0) <= 0 && Math.abs(y1-y0) <= 0) pelotaEnfocada = true;
        }else {
            camera.position.set(oldPosition.x,oldPosition.y,0);
            while(cameraZoom < Constantes.MAX_ZOOM  && (camera.viewportWidth < mapSize.getWidth()/PPM) ) {
                cameraZoom++;
                camera.viewportWidth += Constantes.WIDTH / PPM / (Constantes.ZOOM_SPEED*2);
                camera.viewportHeight += Constantes.HEIGHT / PPM / (Constantes.ZOOM_SPEED*2);
            };
            limitCamera();
            pelotaEnfocada = true;
        }
    }
    private void checkInputUser() {
        if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER) && pelotaEnfocada && !pelota.isMoving()){
            if(!tirar) {
                oldPosition = new Point(pelota.body.getPosition().x,pelota.body.getPosition().y);
                tirar = true;
                animarGolfista = true;
                barraDePoder.resetPower();
            } else if(tirar && !pelota.isGolfistaAnimating()){
                pelota.animateGolfista();
                animarGolfista = false;
                potenciaCapturada = barraDePoder.getPower();
            }
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)){
            game.setScreen(new Menu(game));
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
