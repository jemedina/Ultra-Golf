package com.uaa.ultragolf;

import com.badlogic.gdx.Game;
import com.uaa.ultragolf.Screens.GameScreen;

public class UltraGolf extends Game {
    @Override
    public void create() {
        setScreen(new GameScreen(this));
    }
}
