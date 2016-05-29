package com.uaa.ultragolf.Animations;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

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
        img = new Texture("sprites/golfista.png");
        TextureRegion[][] tmpFrames = TextureRegion.split(img,64,64);
        animationFrames = new TextureRegion[8];
        for(int i = 0 ; i < 8 ; i++) {
            animationFrames[i] = tmpFrames[0][i];
        }
        animation = new Animation(1/10f,animationFrames);
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
                if(inverted){
                    if(!tempFrame.isFlipX())
                        tempFrame.flip(true,false);
                } else {
                    if(tempFrame.isFlipX())
                        tempFrame.flip(true,false);
                }
                if(inverted) x+=14;
                batch.draw(tempFrame, x, y);
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
