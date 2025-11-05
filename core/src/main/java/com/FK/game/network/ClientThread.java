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

public class ClientThread extends Thread {
    private static final int SERVER_PORT = 54555;
    private boolean running = true;
    private DatagramSocket socket;
    private InetAddress serverAddress;
    private int playerId = -1;
    private final Queue<String> pendingMessages = new ArrayDeque<>();
    private final Object pendingLock = new Object();
    private volatile Integer pendingPlayerId = null;

    public ClientThread(String serverIp) {
        try {
            serverAddress = InetAddress.getByName(serverIp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

@Override
public void run() {
    try {
        socket = new DatagramSocket();
        System.out.println("Cliente UDP iniciado. Enviando mensaje de conexión...");
        sendMessage("CONNECT");

        byte[] buffer = new byte[1024];

        while (running) {
            try {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                String message = new String(packet.getData(), 0, packet.getLength());
                handleServerMessage(message);

            } catch (IOException e) {
                if (!running) {
                    System.out.println("Socket cerrado, deteniendo el hilo del cliente.");
                    break;
                }
                e.printStackTrace(); 
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    } catch (Exception e) {
        e.printStackTrace();
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
        if (message.startsWith("LEVEL_READY:")) {
            String mapName = message.split(":")[1];

            Gdx.app.postRunnable(() -> {
                GameScreen scr = GameContext.getScreen();
                System.out.println("[CLIENT] LEVEL_READY received: " + mapName);

                scr.loadSpecificMap(mapName); 
            });
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
