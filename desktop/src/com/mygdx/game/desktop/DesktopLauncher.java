package com.mygdx.game.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.WoodenSlug;

 public class DesktopLauncher {
	public static void main (String[] arg) {
	    	LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();              
		new LwjglApplication(new WoodenSlug(), config);
                config.title = "Wooden Slug";
                config.height = WoodenSlug.ALTEZZA; // regolazione dell'altezza della finestra
                config.width = WoodenSlug.LUNGHEZZA; // regolazione della lunghezza della finestra
                config.addIcon("logo_finestra.png", Files.FileType.Internal); // scelta dell'icona della finestra
                config.resizable = false; // la finestra non è ridimensionabile
                config.forceExit = false; // consente la corretta chiusura della finestra
                
          
	}
}
