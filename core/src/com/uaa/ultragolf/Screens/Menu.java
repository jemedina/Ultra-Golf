package com.uaa.ultragolf.Screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.uaa.ultragolf.Actors.FondoMobil;
import com.uaa.ultragolf.Global.Constantes;

public class Menu extends ScreenAdapter {
    protected Game game;
    private FondoMobil fondoMobil;
    public static EstadoDeJuego es;
    Skin skin;
    Stage stage;
    Sound click;
    SpriteBatch batch, n1,n2;
    Texture img, imagen1, imagen2,imagen3, imagen4,imagen5, imagen6, imagen7, imagen8, nivel;
    public Menu(Game game){
        this.game = game;
    }
    public enum EstadoDeJuego {

        MENU, SALIR, JUGAR;
    }

    @Override
    public void show() {
       click = Gdx.audio.newSound(Gdx.files.getFileHandle("sounds/click.wav", Files.FileType.Local));
        es = EstadoDeJuego.MENU;
        batch = new SpriteBatch();
        fondoMobil = new FondoMobil();
        n1 = new SpriteBatch();
        img = new Texture(Gdx.files.internal("sprites/UltraGolfLogo.png"));
        imagen1 = new Texture(Gdx.files.internal("ui/Niveles1.png"));
        imagen2 = new Texture(Gdx.files.internal("ui/Niveles2.png"));
        imagen3 = new Texture(Gdx.files.internal("ui/Niveles3.png"));
        imagen4 = new Texture(Gdx.files.internal("ui/Niveles4.png"));
        imagen5 = new Texture(Gdx.files.internal("ui/Niveles5.png"));
        imagen6 = new Texture(Gdx.files.internal("ui/Niveles6.png"));
        imagen7 = new Texture(Gdx.files.internal("ui/Niveles7.png"));
        imagen8 = new Texture(Gdx.files.internal("ui/Niveles8.png"));
        nivel = new Texture(Gdx.files.internal("sprites/textoNiveles.png"));

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        //batch.draw(img, -100, 0);
        fondoMobil.draw(batch,delta);
        batch.draw(img,Gdx.graphics.getWidth()/2 - img.getWidth()/2,Gdx.graphics.getHeight()/2 - img.getHeight()/2);
        batch.end();

        switch (es) {
            case MENU:
                stage = new Stage();
                Table tabla = new Table();
                tabla.setPosition(1024 / 3, 450);
                tabla.setFillParent(true);
                tabla.setHeight(600);
                stage.addActor(tabla);

                Label label = new Label("SSG2", getSkin());
                
                label.setPosition((int) tabla.getWidth()/2,0);
                //tabla.addActor(label);

                TextButton juega = new TextButton("Juega", getSkin());
                juega.setPosition(245, label.getOriginY() - 120);
                juega.setWidth(200);
                juega.setHeight(40);
                juega.setColor(Color.GREEN);
                juega.addListener(new InputListener() {
                    public boolean touchDown(InputEvent e, float x, float y, int punto, int boton) {
                        click.play();
                        es = EstadoDeJuego.JUGAR;
                        return false;
                    }
                });
                tabla.addActor(juega);

                TextButton Salir = new TextButton("Salir", getSkin());
                Salir.setPosition(245, label.getOriginY() - 220);
                Salir.setWidth(200);
                Salir.setHeight(40);
                Salir.setColor(Color.PURPLE);
                Salir.addListener(new InputListener() {
                    public boolean touchDown(InputEvent e, float x, float y, int punto, int boton) {
                        click.play();
                        Gdx.app.exit();
                        return false;
                    }
                });
                tabla.addActor(Salir);
                stage.act(Gdx.graphics.getDeltaTime());
                stage.draw();
                Gdx.input.setInputProcessor(stage);

                break;
                
            case JUGAR:
                stage = new Stage();

                Table tablan = new Table();
                tablan.setPosition(1024 / 4, 450);
                tablan.setFillParent(true);
                tablan.setHeight(500);
                stage.addActor(tablan);

                Label labeln = new Label("Niveles", getSkin());
                //tablan.addActor(labeln);

                TextButton[] b = new TextButton[8];
                for (int i = 0; i < b.length; i++) {
                    b[i] = new TextButton(" " + (i + 1), getSkin());
                    b[i].setPosition((i * 150) - 100, -130);
                    b[i].setWidth(50);
                    b[i].setHeight(50);
                    tablan.addActor(b[i]);
                }

                b[0].addListener(new InputListener() {
                    public boolean touchDown(InputEvent e, float x, float y, int punto, int boton) {
                        click.play();
                        game.setScreen(new GameScreen(game, 0));

                        return false;
                    }
                });
                b[1].addListener(new InputListener() {
                    public boolean touchDown(InputEvent e, float x, float y, int punto, int boton) {
                        click.play();
                        game.setScreen(new GameScreen(game,1));
                        return false;
                    }
                });
                b[2].addListener(new InputListener() {
                    public boolean touchDown(InputEvent e, float x, float y, int punto, int boton) {
                        click.play();
                        game.setScreen(new GameScreen(game,2));
                        return false;
                    }
                });
                b[3].addListener(new InputListener() {
                    public boolean touchDown(InputEvent e, float x, float y, int punto, int boton) {
                        click.play();
                        game.setScreen(new GameScreen(game,3));
                        return false;
                    }
                });
                b[4].addListener(new InputListener() {
                    public boolean touchDown(InputEvent e, float x, float y, int punto, int boton) {
                        click.play();
                        game.setScreen(new GameScreen(game,4));
                        return false;
                    }
                });
                b[5].addListener(new InputListener() {
                    public boolean touchDown(InputEvent e, float x, float y, int punto, int boton) {
                        click.play();
                        game.setScreen(new GameScreen(game,5));
                        return false;
                    }
                });
                b[6].addListener(new InputListener() {
                    public boolean touchDown(InputEvent e, float x, float y, int punto, int boton) {
                        click.play();
                        game.setScreen(new GameScreen(game,6));
                        return false;
                    }
                });
                b[7].addListener(new InputListener() {
                    public boolean touchDown(InputEvent e, float x, float y, int punto, int boton) {
                        click.play();
                        game.setScreen(new GameScreen(game,7));
                        return false;
                    }
                });

                TextButton rgresar = new TextButton("Regresar", getSkin());
                rgresar.setPosition(labeln.getOriginX() - 250, labeln.getOriginY() - 400);
                rgresar.setWidth(200);
                rgresar.setHeight(30);
                rgresar.setColor(Color.YELLOW);
                rgresar.addListener(new InputListener() {
                    public boolean touchDown(InputEvent e, float x, float y, int punto, int boton) {
                        es = EstadoDeJuego.MENU;
                        return false;
                    }
                });
                tablan.addActor(rgresar);
                stage.act(Gdx.graphics.getDeltaTime());
                stage.draw();
                Gdx.input.setInputProcessor(stage);
                n1.begin();
                n1.draw(imagen1, b[0].getX()+240, 310);
                n1.draw(imagen2, b[1].getX()+240, 310);
                n1.draw(imagen3, b[2].getX()+240, 310);
                n1.draw(imagen4, b[3].getX()+240, 310);
                n1.draw(imagen5, b[4].getX()+240, 310);
                n1.draw(imagen6, b[5].getX()+240, 310);
                n1.draw(imagen7, b[6].getX()+240, 310);
                n1.draw(imagen8, b[7].getX()+240, 310);
                n1.draw(nivel,100,Gdx.graphics.getHeight()-(nivel.getHeight()*1.2f));
                n1.end();

                break;
        }
    }

    protected Skin getSkin() {

        if (skin == null) {
            try {
                skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
            } catch (Exception e) {
                System.err.println("NOOOOOOOOOOOOOOOOOOOOOOO");
            }
        }
        return skin;
    }
}
