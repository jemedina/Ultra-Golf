package com.uaa.ultragolf.Actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.uaa.ultragolf.Global.Constantes;

/**
 * Created by Eduardo on 19/06/2016.
 */
public class FondoMobil {
    private Texture textura;
    private Sprite sprite;
    public FondoMobil() {
        textura = new Texture("sprites/fondoMobil.png");
        sprite = new Sprite(textura);
        //Transparencia de la textura
        sprite.setAlpha(0.2f);
        //Origen en el centro
        sprite.setOriginCenter();
        sprite.scale(0.7f);
        //Se posicionia al centro de la pantalla
        sprite.setPosition(Gdx.graphics.getWidth()/2 - sprite.getWidth()/2,Gdx.graphics.getHeight()/2-sprite.getHeight()/2);
    }

    public void draw(SpriteBatch sb, float delta) {
        //Rotacion de la imagen
        sprite.rotate(45*delta);
        sprite.draw(sb);
    }
}
