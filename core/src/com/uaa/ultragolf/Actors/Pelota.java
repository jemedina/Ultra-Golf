package com.uaa.ultragolf.Actors;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.uaa.ultragolf.Animations.FlechaAnimation;
import com.uaa.ultragolf.Animations.GolfistaAnimation;

public class Pelota extends Actor {
    private float dt;
    //Animacion del golfista
    private GolfistaAnimation golfistaAnimation;
    private boolean firstTime = true;//Bandera para saber si es el primer tiro
    private Texture textura;//Textura de la pelota
    private FlechaAnimation flechaAnimation;//Animacion de la flecha
    public boolean centered;
    public Body body;

    public void setFirstTime(boolean firstTime) {
        this.firstTime = firstTime;
    }

    public Pelota() {
        flechaAnimation = new FlechaAnimation();
        golfistaAnimation = new GolfistaAnimation();
        textura = new Texture("sprites/pelota.png");
    }
    public void setAngle(float angle) {
        flechaAnimation.setAngle(angle);
    }
    public boolean isMoving() {
        return body.isAwake();
    }//Saber si la pelota esta en movimiento
    //Ejecuta logica de la pelota
    public void act(float delta,OrthographicCamera cam) {
        this.setPosition(body.getPosition().x-0.3f,body.getPosition().y-0.3f);
        dt = delta;
    }
    public void animateGolfista() {
        golfistaAnimation.animate();
    }

    public boolean isGolfistaAnimating() {
        return golfistaAnimation.isAnimating();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(textura,body.getPosition().x-0.3f,body.getPosition().y-0.3f,0.6f,0.6f);
        //batch.draw(t,body.getPosition().x-0.3f,body.getPosition().y-0.3f,0.6f,0.6f);
        if(flechaAnimation.getAngle() > 90 && flechaAnimation.getAngle() < 270)
            golfistaAnimation.setInverted(true);
        else
            golfistaAnimation.setInverted(false);
        if(!body.isAwake() || firstTime && !golfistaAnimation.isAnimating())
            flechaAnimation.draw(dt, (SpriteBatch) batch,body.getPosition().x,body.getPosition().y);
            golfistaAnimation.draw(dt, (SpriteBatch) batch,body.getPosition().x,body.getPosition().y);

    }

    public boolean isFirstTime() {
        return firstTime;
    }

    public float getXForce(float i) {
        return (float) (i*Math.cos(Math.toRadians(flechaAnimation.getAngle())));
    }
    public float getYForce(float i) {
        return (float) (i*Math.sin(Math.toRadians(flechaAnimation.getAngle())));
    }

    public float getAngle() {
        return flechaAnimation.getAngle();
    }
}
