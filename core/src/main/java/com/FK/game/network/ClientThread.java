package com.FK.game.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayDeque;
import java.util.Queue;
import com.badlogic.gdx.Gdx;

import com.FK.game.core.MainGame;
import com.FK.game.core.GameContext;
import com.FK.game.network.NetworkMessage;
import com.FK.game.screens.GameScreen;
import com.FK.game.screens.InterlevelLoadingScreen;
import com.FK.game.screens.FireAttackHUD;
import java.net.SocketException;
import com.FK.game.screens.ClientConnectionScreen;

public class ClientThread extends Thread {
    private static final int SERVER_PORT = 54555;
    private boolean running = true;
    private DatagramSocket socket;
    private InetAddress serverAddress;
    private int playerId = -1;
    private final Queue<String> pendingMessages = new ArrayDeque<>();
    private final Object pendingLock = new Object();
    private volatile Integer pendingPlayerId = null;
    private boolean connected = false;
    private boolean isServerClosed = true;

    public ClientThread(String serverIp) {
        try {
            serverAddress = InetAddress.getByName(serverIp);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
        if (isServerClosed != true){
            try {
                sendDisconnectMessage();
                System.out.println("[CLIENT] Shutdown Hook ejecutado — mensaje de desconexión enviado.");
            } catch (Exception e) {
                System.err.println("[CLIENT] Error al enviar desconexión en shutdown hook: " + e.getMessage());
            }
        }
    }));
    }

@Override
public void run() {
    
    try {
        socket = new DatagramSocket();
        System.out.println("Cliente UDP iniciado. Enviando mensaje de conexión...");
        if (!connected) {
            serverAddress = discoverServer();
            if (serverAddress == null) {
                System.out.println("[CLIENT] No se encontró servidor. Intentando con localhost...");
                serverAddress = InetAddress.getByName("127.0.0.1");
            }
            sendMessage("CONNECT");
            connected = true;
        }
        byte[] buffer = new byte[1024];

        while (running) {
            try {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                String message = new String(packet.getData(), 0, packet.getLength());
                handleServerMessage(message);

            } catch (SocketException e) {
                if (!running || socket.isClosed()) {
                    System.out.println("[CLIENT] Socket cerrado — hilo finalizado correctamente.");
                    break;
                } else {
                    System.err.println("[CLIENT] Error de socket: " + e.getMessage());
                }
            } catch (IOException e) {
                if (!running) {
                    System.out.println("[CLIENT] Socket cerrado, deteniendo el hilo del cliente.");
                    break;
                }
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    } catch (Exception e) {
        e.printStackTrace();
        System.err.println("[CLIENT] Error inesperado: " + e.getMessage());
        sendDisconnectMessage();
        try { Thread.sleep(200); } catch (InterruptedException ignored) {}
    } finally {
        if (socket != null && !socket.isClosed()) {
            socket.close();
        }
        System.out.println("Hilo del cliente detenido.");
    }

    
}
    public void sendMessage(String msg) {
        try {
            byte[] data = msg.getBytes();
            DatagramPacket packet = new DatagramPacket(data, data.length, serverAddress, SERVER_PORT);
            socket.send(packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
private InetAddress discoverServer() {
    try (DatagramSocket socket = new DatagramSocket()) {
        socket.setBroadcast(true);
        socket.setSoTimeout(2000); // espera 2 segundos máximo

        String discoveryMessage = "DISCOVER_FK_SERVER";
        byte[] data = discoveryMessage.getBytes();
        DatagramPacket packet = new DatagramPacket(
            data, data.length,
            InetAddress.getByName("255.255.255.255"), 54556 // 👈 mismo puerto del broadcast del server
        );

        System.out.println("[CLIENT] Buscando servidor en la red local...");
        socket.send(packet);

        byte[] recvBuf = new byte[256];
        DatagramPacket receivePacket = new DatagramPacket(recvBuf, recvBuf.length);

        socket.receive(receivePacket);
        String msg = new String(receivePacket.getData(), 0, receivePacket.getLength());

        if (msg.startsWith("FK_SERVER_RESPONSE:")) {
            String ip = msg.split(":")[1];
            System.out.println("[CLIENT] Servidor detectado en: " + ip);
            isServerClosed = false;
            return InetAddress.getByName(ip); // 🔸 devuelve InetAddress
        }

    } catch (IOException e) {
        System.out.println("[CLIENT] No se detectó servidor por broadcast.");
    }
    return null;
}


public void sendDisconnectMessage() {
    try {
        if (socket != null && serverAddress != null) {
            String msg = "DISCONNECT:" + playerId;
            byte[] data = msg.getBytes();
            DatagramPacket packet = new DatagramPacket(data, data.length, serverAddress, 54555);
            socket.send(packet);
            System.out.println("[CLIENT] Enviando mensaje de desconexión...");
        }
    } catch (Exception e) {
        System.err.println("[CLIENT] No se pudo enviar mensaje de desconexión: " + e.getMessage());
    }
}


    public void sendInput(String input) {
        if (playerId == -1) return; 
        String msg = "INPUT:" + playerId + ":" + input;
        byte[] data = msg.getBytes();
        DatagramPacket packet = new DatagramPacket(data, data.length, serverAddress, SERVER_PORT);
        try {
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
        }
    public int getPlayerId() {
        return playerId;
    }

    private void handleServerMessage(String message) {

        if (message.startsWith("ASSIGN_ID:")) {
            int id = Integer.parseInt(message.substring(10));
            System.out.println("Este cliente es Jugador " + id);
            this.playerId = id;
            isServerClosed = false;
            GameScreen screen = GameContext.getScreen();
            if (screen != null) {
                screen.setPlayerId(id);
                flushPendingMessages(screen);
            } else {
                pendingPlayerId = id;
            }
            return;
        }

        GameScreen screen = GameContext.getScreen();
        if (screen == null) {
            synchronized (pendingLock) {
                pendingMessages.add(message);
            }
            return;
        }

        if (pendingPlayerId != null) {
            screen.setPlayerId(pendingPlayerId);
            pendingPlayerId = null;
        }

        boolean hadPending;
        synchronized (pendingLock) {
            hadPending = !pendingMessages.isEmpty();
            if (hadPending) {
                pendingMessages.add(message);
            }
        }

        if (hadPending) {
            flushPendingMessages(screen);
            return;
        }

        processMessage(screen, message);
    }

    private void flushPendingMessages(GameScreen screen) {
        while (true) {
            String pending;
            synchronized (pendingLock) {
                pending = pendingMessages.poll();
            }
            if (pending == null) {
                break;
            }
            processMessage(screen, pending);
        }
    }

    private void processMessage(GameScreen screen, String message) {
        if (message.startsWith("NEW_PLAYER:")) {
            try {
                int newId = Integer.parseInt(message.split(":")[1]);
                System.out.println("Nuevo jugador en el mundo: " + newId);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }

        if (message.startsWith("REMOVE_PLAYER:")) {
            try {
                int remId = Integer.parseInt(message.split(":")[1]);
                System.out.println("Jugador desconectado: " + remId);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }
        if (message.equals("RESET_MAP")) {
            System.out.println("[CLIENT] Recibido comando RESET_MAP — limpiando mapa");
            Gdx.app.postRunnable(() -> {
                if (screen != null) {
                    screen.cleanUpCurrentMap();
                }
            });
        }

        if (message.equals("CHANGE_LEVEL")) {
            System.out.println("[CLIENT] Cambio de nivel recibido desde el servidor");

            Gdx.app.postRunnable(() -> {
                GameScreen gs = GameContext.getScreen();
                if (gs != null) {
                    gs.notifyLevelChange();
                }
            });
            return;
        }
        if (message.equals("OPEN_UPGRADE_MENU")) {
            Gdx.app.postRunnable(() -> {
            GameContext.getScreen().openUpgradeMenu();
            });
        }
        if (message.startsWith("UPGRADE_DATA:")) {
            String[] p = message.split(":");
            int coins = Integer.parseInt(p[1]);
            int dmgLvl = Integer.parseInt(p[2]);
            int hpLvl = Integer.parseInt(p[3]);
            int dmgCost = Integer.parseInt(p[4]);
            int hpCost = Integer.parseInt(p[5]);

            screen.getGame().playerData.coinCount = coins;
            screen.getGame().playerData.attackDamageLevel = dmgLvl;
            screen.getGame().playerData.healthLevel = hpLvl;
            System.out.println("[CLIENT] Datos de mejora recibidos: Monedas=" + coins +
                               ", DañoNivel=" + dmgLvl + ", VidaNivel=" + hpLvl + ", DañoCosto=" + dmgCost + ", VidaCosto=" + hpCost);
            
            if (screen.getUpgradeWindow() != null) {
                screen.getUpgradeWindow().updateUIFromNetwork(coins, dmgLvl, hpLvl, dmgCost, hpCost);
            }
        }
        if (message.startsWith("BOSS_LASER:")) {
            String[] parts = message.split(":");
            String phase = parts[1];
            
            Gdx.app.postRunnable(() -> {
                if (screen != null && screen.getBoss() != null) {
                    float angle = 0f;
                    switch (phase) {
                        case "WARNING":
                            angle = Float.parseFloat(parts[2]);
                            screen.getBoss().startLaserCharging(angle);
                            System.out.println("Boss laser charging started");
                            break;
                        case "FIRING":
                            angle = Float.parseFloat(parts[2]);
                            System.out.println("Boss laser firing at angle: " + angle);
                            screen.getBoss().fireLaser(angle);
                            break;
                        case "COOLDOWN":
                            System.out.println("Boss laser cooldown started");
                            screen.getBoss().laserCooldown();
                            break;
                        case "END":
                            System.out.println("Boss laser attack ended");
                            screen.getBoss().endLaserAttack();
                            break;
                    }
                }
            });
            return;
        }

        if (message.startsWith("PLAYER_DIED:")) {
            String[] parts = message.split(":");
            int deadId = Integer.parseInt(parts[1]);
            System.out.println("[CLIENT] Jugador muerto con ID: " + deadId);

            Gdx.app.postRunnable(() -> {
                MainGame game = screen.getGame();
                game.playerData.resetOnDeath();
                game.setScreen(new GameScreen(game));
                screen.cleanUpCurrentMap();
            });
        }
if (message.equals("SERVER_SHUTDOWN")) {
    System.out.println("[CLIENT] El servidor se ha desconectado.");
    isServerClosed = true;

    // Resetear estado interno
    playerId = -1;
    connected = false;

    // Cerrar el socket de manera segura
    try {
        if (socket != null && !socket.isClosed()) {
            socket.close();
            System.out.println("[CLIENT] Socket cerrado tras apagado del servidor.");
        }
    } catch (Exception e) {
        System.err.println("[CLIENT] Error al cerrar socket tras apagado del servidor: " + e.getMessage());
    }

    // Volver a la pantalla inicial de conexión
    Gdx.app.postRunnable(() -> {
        MainGame game = GameContext.getScreen().getGame();
        if (game != null) {
            System.out.println("[CLIENT] Volviendo a pantalla de conexión...");
            game.setScreen(new ClientConnectionScreen(game));
        } else {
            System.err.println("[CLIENT] No se pudo acceder al contexto del juego para reiniciar pantalla.");
        }
    });

    return;
}

        if (message.startsWith("HUD_FIRE_STATE:")) {
            String[] p = message.split(":");
            String stateName = p[1];
            float time = Float.parseFloat(p[2]);

            Gdx.app.postRunnable(() -> {
                FireAttackHUD hud = screen.getMyPlayer().getFireAttackHUD();
                if (hud != null){
                    hud.setNetworkState(stateName, time);
                }
            });
        }


        if (message.startsWith("FIRE_READY")) {
                System.out.println("[CLIENT] Fuego listo para jugador ");
                Gdx.app.postRunnable(() -> {
                    if (screen.getMyPlayer() != null && screen.getMyPlayer().getFireAttackHUD() != null) {
                        screen.getMyPlayer().updateFireCondition(true);
                        screen.getMyPlayer().getFireAttackHUD().setFireReady(true);
                    }
                });
        }

        if (message.startsWith("FIRE_COOLDOWN")) {
                System.out.println("[CLIENT] Fuego en cooldown para jugador ");
                Gdx.app.postRunnable(() -> {
                    if (screen.getMyPlayer() != null && screen.getMyPlayer().getFireAttackHUD() != null) {
                        screen.getMyPlayer().updateFireCondition(false);
                        screen.getMyPlayer().getFireAttackHUD().setFireReady(false);
                    }
                });
        }


        if (message.startsWith("LEVEL_READY:")) {
            String mapName = message.split(":")[1];

            Gdx.app.postRunnable(() -> {
                GameScreen scr = GameContext.getScreen();
                System.out.println("[CLIENT] LEVEL_READY received: " + mapName);

                scr.loadSpecificMap(mapName); 
            });
        }
        if (message.startsWith("REMOVE_ENTITY:")) {
            int id = Integer.parseInt(message.split(":")[1]);
            screen.removeEntityById(id);
            return;
        }




        if (message.startsWith("UPDATE_PLAYER:")) {
            try {
                String[] parts = message.split(":");
                if (parts.length < 6) {
                    System.err.println("UPDATE_PLAYER mal formado: " + message);
                    return;
                }

                int id = Integer.parseInt(parts[1]);
                float x = Float.parseFloat(parts[2]);
                float y = Float.parseFloat(parts[3]);
                String state = parts[4];
                String facing = parts[5];

                if (screen.getMyPlayerId() == -1) {
                    synchronized (pendingLock) {
                        pendingMessages.add(message);
                    }
                    return;
                }

                screen.updatePlayerState(id, x, y, state, facing);

            } catch (Exception e) {
                System.err.println("Error al procesar el paquete UPDATE_PLAYER: " + message);
                e.printStackTrace();
            }
        }

        if (message.startsWith("UPDATE_ENTITY:") || message.startsWith("CREATE_ENTITY:")) {
            String[] p = message.split(":");
            int id = Integer.parseInt(p[1]);
            String type = p[2];
            float x = Float.parseFloat(p[3]);
            float y = Float.parseFloat(p[4]);
            String state = p[5];
            String facing = p[6];
            float rotation = (p.length > 7) ? Float.parseFloat(p[7]) : 0f;

            screen.updateEntityState(id, type, x, y, state, facing, rotation);
            
        }
        if (message.startsWith("COIN_PICKED:")) {
            int count = Integer.parseInt(message.split(":")[1]);
            screen.getGame().playerData.coinCount = count;
        }
        if (message.startsWith("UPGRADE_CONFIRM:")) {
            String[] parts = message.split(":");
            int newCoins = Integer.parseInt(parts[1]);
            screen.getGame().playerData.coinCount = newCoins;
            Gdx.app.postRunnable(() -> {
                screen.refreshUpgradeWindow();
            });
        }
    

        if (message.startsWith("UPDATE_ENTITY:")) {
            
        }

    }

    public void notifyScreenReady(GameScreen screen) {
        if (screen == null) {
            return;
        }

        if (pendingPlayerId != null) {
            screen.setPlayerId(pendingPlayerId);
            pendingPlayerId = null;
        }

        flushPendingMessages(screen);
    }

public boolean isConnected() { return (playerId != -1); }


    public void sendNetworkMessage(NetworkMessage msg) {
    sendMessage(msg.name());
}

    public void sendNetworkInput(NetworkMessage msg) {
    sendInput(msg.name());
}

    public void stopClient() {
        running = false;
        if (socket != null && !socket.isClosed()) socket.close();
    }
}
