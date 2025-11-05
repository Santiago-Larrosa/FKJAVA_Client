package com.FK.game.network;
import java.io.IOException;

    public enum NetworkMessage {
        // Cliente → Servidor
        CONNECT,           // Solicita conexión al servidor
        DISCONNECT,        // Cliente se desconecta
        INPUT_LEFT,
        STOP_LEFT,
        INPUT_RIGHT,
        STOP_RIGHT,
        INPUT_JUMP,
        STOP_JUMP,
        INPUT_ATTACK,
        STOP_ATTACK,
        INPUT_FIRE_ATTACK,
        INPUT_DOWN,// Ataque especial
        PING,              // Para test de latencia

        // Servidor → Cliente
        CONNECTED,         // Confirmación de conexión
        ENTITY_UPDATE,     // Posiciones/estados de jugadores y enemigos
        COIN_UPDATE,       // Actualización de monedas recogidas
        PORTAL_SPAWNED,    // Portal creado en la sala
        GAME_OVER,         // Cuando el jugador muere o termina la partida
        PONG;              // Respuesta a PING
    }
