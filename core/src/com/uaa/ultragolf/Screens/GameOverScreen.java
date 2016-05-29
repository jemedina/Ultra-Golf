package com.uaa.ultragolf.Screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;

public class GameOverScreen extends ScreenAdapter {
    protected Game game;

    public GameOverScreen(Game game) {
        this.game = game;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 0, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        checkInputUser();
    }

    private void checkInputUser() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            game.setScreen(new GameScreen(game));
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
