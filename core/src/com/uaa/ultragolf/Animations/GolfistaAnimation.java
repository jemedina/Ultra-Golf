package com.uaa.ultragolf.Animations;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.uaa.ultragolf.Global.Constantes;

public class GolfistaAnimation {
    Texture img;
    TextureRegion[] animationFrames;
    Animation animation;
    float sumTime;
    boolean animate;
    boolean inverted;
    public GolfistaAnimation() {
        /*LOAD GLOFISTA ANIMATION*/

        animate = false;
        sumTime = 0f;
        img = new Texture("sprites/golfista2.png");
        TextureRegion[][] tmpFrames = TextureRegion.split(img,128,128);
        animationFrames = new TextureRegion[15];
        for(int i = 0 ; i < 15 ; i++) {
            animationFrames[i] = tmpFrames[0][i];
        }
        animation = new Animation(1/20f,animationFrames);
    }

    public float getWidth() {
        return img.getWidth();
    }
    public float getHeight() {
        return img.getHeight();
    }
    public void animate() {
        animate = true;
    }
    public void setInverted(boolean invert){
        inverted = invert;
    }

    public void draw(float dt,SpriteBatch batch,float x, float y){

        if(animate) {
            if(!animation.isAnimationFinished(sumTime)) {
                TextureRegion tempFrame = animation.getKeyFrame(sumTime, true);
                float w = (tempFrame.getRegionWidth()/ Constantes.PPM)/1.5f;
                float h = (tempFrame.getRegionHeight()/ Constantes.PPM)/1.5f;
                if(inverted){
                    if(!tempFrame.isFlipX())
                        tempFrame.flip(true,false);
                } else {
                    if(tempFrame.isFlipX())
                        tempFrame.flip(true,false);
                }
                if(inverted) x+=45/Constantes.PPM;
                batch.draw(tempFrame, x-(64/Constantes.PPM), y-(20/Constantes.PPM),w,h);
                sumTime += dt;
            } else {
                animate = false;
                sumTime = 0f;
            }
        }
    }

    public boolean isAnimating() {
        return animate;
    }
}
