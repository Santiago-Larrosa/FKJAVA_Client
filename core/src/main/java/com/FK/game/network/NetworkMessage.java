package com.FK.game.network;
import java.io.IOException;

    public enum NetworkMessage {
        CONNECT,           
        DISCONNECT,       
        INPUT_LEFT,
        STOP_LEFT,
        INPUT_RIGHT,
        STOP_RIGHT,
        INPUT_JUMP,
        STOP_JUMP,
        INPUT_ATTACK,
        STOP_ATTACK,
        INPUT_FIRE_ATTACK,
        STOP_FIRE_ATTACK,
        INPUT_DOWN,
        STOP_DOWN,
        PING,             
        CONNECTED,        
        ENTITY_UPDATE,    
        COIN_UPDATE,       
        PORTAL_SPAWNED,   
        GAME_OVER,        
        PONG;            
    }
