package com.uaa.ultragolf;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.uaa.ultragolf.Screens.GameScreen;
import com.uaa.ultragolf.Screens.Menu;

public class UltraGolf extends Game {

    private Music music;

    @Override
    public void create() {
        music = Gdx.audio.newMusic(Gdx.files.getFileHandle("sounds/misfits.mp3", Files.FileType.Local));
        music.setLooping(true);
        music.setVolume(0.2f);
        music.play();
        setScreen(new Menu(this));
    }
}
