package com.uaa.ultragolf.Actors;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.uaa.ultragolf.Animations.GolfistaAnimation;

public class Pelota extends Actor {

    private GolfistaAnimation golfistaAnimation;
    private Texture textura;
    public boolean centered;
    public Body body;
    public Pelota() {
        golfistaAnimation = new GolfistaAnimation();
        textura = new Texture("sprites/pelota.png");
    }
    public boolean isMoving() {
        return body.isAwake();
    }
    public void act(float delta,OrthographicCamera cam) {
        this.setPosition(body.getPosition().x-0.3f,body.getPosition().y-0.3f);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(textura,body.getPosition().x-0.3f,body.getPosition().y-0.3f,0.6f,0.6f);
    }

}
