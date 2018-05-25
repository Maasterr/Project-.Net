package com.study.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.study.game.GameScreen;
import com.study.game.MainMenuScreen;
import com.study.game.MyGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "P0001";
		config.width = 800;
		config.height = 480;
		new LwjglApplication(new MyGame(), config);
	}
}
