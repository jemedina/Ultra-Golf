package com.uaa.ultragolf.Animations;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.uaa.ultragolf.Global.Constantes;

public class FlechaAnimation {
    Texture img;
    TextureRegion[] animationFrames;
    Animation animation;
    float sumTime;
    private float angle;
    public void setAngle(float angle) {
        if(angle > 360)
            angle = angle-360;
        if(angle < 0)
            angle = 360+angle;
        this.angle = angle;
    }


    public float getAngle(){
        return angle;
    }

    public FlechaAnimation() {
        sumTime = 0f;
        setAngle(45);
        img = new Texture("sprites/flecha.png");
        TextureRegion[][] tmpFrames = TextureRegion.split(img,100,400);
        animationFrames = new TextureRegion[20];
        for(int i = 0 ; i < 20 ; i++) {
            animationFrames[i] = tmpFrames[0][i];
        }
        animation = new Animation(1/60f,animationFrames);
    }

    public float getWidth() {
        return img.getWidth();
    }
    public float getHeight() {
        return img.getHeight();
    }
    public void draw(float dt, SpriteBatch batch, float x, float y){
        TextureRegion tempFrame = animation.getKeyFrame(sumTime, true);
        float w = (tempFrame.getRegionWidth()/ Constantes.PPM)/3;
        float h = (tempFrame.getRegionHeight()/ Constantes.PPM)/3;
        batch.draw(tempFrame,x-(w/2),y,w/2,0,w,h,1,1,angle-90);
        sumTime += dt;
    }
}
