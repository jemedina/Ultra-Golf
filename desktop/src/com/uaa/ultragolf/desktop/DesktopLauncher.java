package com.uaa.ultragolf.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.uaa.ultragolf.UltraGolf;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1024;
		config.height = 600;
		config.resizable = false;
		config.vSyncEnabled=true;
		config.useHDPI=true;
		config.fullscreen = true;
		new LwjglApplication(new UltraGolf(), config);


	//TEST
	}
}
