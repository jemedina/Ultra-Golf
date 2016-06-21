package com.uaa.ultragolf.Actors;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.uaa.ultragolf.Global.Constantes;

import static com.uaa.ultragolf.Global.Constantes.PPM;

public class BarraDePoder {
    //2 sprites que son la flecha y la barra
    private Texture barra, indicadorTextura;
    //Sprite para manejo de la flecha (rotacion)
    private Sprite indicadorSprite;
    private Body p;
    private float speed = 3; //90 grados por segundo
    private float angle = 90;
    float x,y,w,h;
    OrthographicCamera cam;
    public BarraDePoder(Body c , OrthographicCamera cam) {
        p = c;
        //Cargar texturas
        barra = new Texture("sprites/power_bar.png");
        indicadorTextura = new Texture("sprites/indicador.png");
        this.cam = cam;
        indicadorSprite = new Sprite(indicadorTextura);
       //Dimensiones escaladas a los pixeles por metro
        w = barra.getWidth()/PPM/1.5f;
        h = barra.getHeight()/PPM/1.5f;
        x = cam.position.x+(Constantes.WIDTH/PPM/2)-w;
        y = -h-h/2.5f;
        //Eje de rotacion en el centro y abajo
        indicadorSprite.setOrigin(indicadorTextura.getWidth()/PPM/2,0);
        //Escalamos el indicador
        indicadorSprite.setBounds(x+(barra.getWidth()/PPM/1.8f),y,indicadorTextura.getWidth()/PPM,indicadorTextura.getHeight()/PPM);
    }
    //Dibuja la flecha con los calculos de velocidad y angulo
    public void draw(SpriteBatch batch, float delta) {
        update(delta);
        batch.draw(barra,x,y,w,h);
        indicadorSprite.setRotation(angle);
        indicadorSprite.draw(batch);
    }
    //Dibuja la flecha sin los calculos
    public void drawOnly(SpriteBatch batch, float delta) {
        batch.draw(barra,x,y,w,h);
        indicadorSprite.setRotation(angle);
        indicadorSprite.draw(batch);
    }
    //Da los angulos 0 y 90 para que no se salga y le da la velocidad
    private void update(float delta) {
        if(angle < 0 || angle > 90) speed=-speed;
        angle+=speed*delta;
    }

    //Poder inicial de tiro
    public void resetPower() {
        angle = 90;
    }

    //Poder conforme a lo de la flecha
    public float getPower() {
        return 10+Math.abs(90-angle)*4;
    }
}
