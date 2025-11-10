package com.FK.game.lwjgl3;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.FK.game.core.MainGame;
import com.FK.game.core.GameContext;
import com.FK.game.network.ClientThread;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3WindowAdapter;

public class Lwjgl3Launcher {
    public static void main(String[] args) {
        if (StartupHelper.startNewJvmIfRequired()) return; 
        createApplication();
    }

    private static Lwjgl3Application createApplication() {
        Lwjgl3ApplicationConfiguration config = getDefaultConfiguration();

        config.setWindowListener(new Lwjgl3WindowAdapter() {
            @Override
            public boolean closeRequested() {
                System.out.println("[HOOK] Ventana cerrada — ejecutando limpieza manual.");
                ClientThread client = GameContext.getConnectionScreen().getGame().client;
                if (client != null){
                    client.sendDisconnectMessage();
                    try {
                        Thread.sleep(150);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    client.stopClient();
                }
                MainGame.onWindowClosed();
                System.exit(0);
                return true; // permite el cierre
            }
        });
        return new Lwjgl3Application(new MainGame(), config);
    }

    private static Lwjgl3ApplicationConfiguration getDefaultConfiguration() {
        Lwjgl3ApplicationConfiguration configuration = new Lwjgl3ApplicationConfiguration();
        configuration.setTitle("FurnaceKnightDungeonJAVA");
        configuration.useVsync(true);
        configuration.setForegroundFPS(Lwjgl3ApplicationConfiguration.getDisplayMode().refreshRate + 1);
        configuration.setWindowedMode(640, 480);
        configuration.setWindowIcon("libgdx128.png", "libgdx64.png", "libgdx32.png", "libgdx16.png");
        return configuration;
    }
}