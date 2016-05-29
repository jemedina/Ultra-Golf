package com.uaa.ultragolf.Actors;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.uaa.ultragolf.Global.Constantes;

import java.awt.*;

import static com.uaa.ultragolf.Global.Constantes.PPM;

public class Nube extends Actor {
    private Texture textura;
    private OrthographicCamera camera;
    private float yCam0;
    private Dimension mapSize;
    private float x, y;
    private float velosidad;
    public Nube(OrthographicCamera camera,Dimension mapSize) {
        this.camera = camera;
        velosidad = (float) (0.5f+(Math.random()*0.8f));
        yCam0 = camera.position.y;
        this.mapSize = mapSize;
        textura = new Texture("sprites/Nube.png");
        x = (float) (Math.random()*((mapSize.getWidth()/PPM)+(textura.getWidth()*3f/PPM)));
        y = (float) ((Math.random()* ((mapSize.getHeight()/5)/PPM)) + (4*mapSize.getHeight()/5)/PPM);
    }

    @Override
    public void act(float delta) {
        x -= velosidad*delta;
        float dy = (yCam0-camera.position.y);
        yCam0 = camera.position.y;
        System.out.println(dy);
        y += dy*10/(mapSize.getHeight()/PPM);
        if(x < -(textura.getWidth()*1.4f/PPM)) {
            x = (float) (mapSize.getWidth()/ PPM);
            y = (float) ((Math.random()* ((mapSize.getHeight()/4)/PPM)) + (4*mapSize.getHeight()/5)/PPM);
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(textura,x,y,textura.getWidth()*1.4f/PPM,textura.getHeight()*1.4f/PPM);
    }
}
