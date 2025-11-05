package com.FK.game.core;

import com.badlogic.gdx.InputAdapter;
import com.FK.game.network.NetworkMessage;
import com.FK.game.network.ClientThread;


public class NetworkInputAdapter extends InputAdapter {

    private final ClientThread client;
    private final int keyLeft;
    private final int keyRight;
    private final int keyJump;
    private final int keyAttack;
    private final int keyFireAttack;
    private final int keyDown;

    public NetworkInputAdapter(ClientThread client,
                               int keyLeft, int keyRight, int keyJump,
                               int keyAttack, int keyFireAttack, int keyDown) {
        this.client = client;
        this.keyLeft = keyLeft;
        this.keyRight = keyRight;
        this.keyJump = keyJump;
        this.keyAttack = keyAttack;
        this.keyFireAttack = keyFireAttack;
        this.keyDown = keyDown;
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == keyLeft) {
            client.sendNetworkInput(NetworkMessage.INPUT_LEFT);
            return true;
        } else if (keycode == keyRight) {
            client.sendNetworkInput(NetworkMessage.INPUT_RIGHT);
            return true;
        } else if (keycode == keyJump) {
            client.sendNetworkInput(NetworkMessage.INPUT_JUMP);
            return true;
        } else if (keycode == keyAttack) {
            client.sendNetworkInput(NetworkMessage.INPUT_ATTACK);
            return true;
        } else if (keycode == keyFireAttack) {
            client.sendNetworkInput(NetworkMessage.INPUT_FIRE_ATTACK);
            return true;
        } else if (keycode == keyDown) {
            client.sendNetworkInput(NetworkMessage.INPUT_DOWN);
            return true;
        }
        
        return false; 
    }

 
    @Override
    public boolean keyUp(int keycode) {
        if (keycode == keyLeft) {
            client.sendNetworkInput(NetworkMessage.STOP_LEFT);
            return true;
        } else if (keycode == keyRight) {
            client.sendNetworkInput(NetworkMessage.STOP_RIGHT);
            return true;
        }else if (keycode == keyJump) {
            client.sendNetworkInput(NetworkMessage.STOP_JUMP);
            return true;
        }else if (keycode == keyAttack) {
            client.sendNetworkInput(NetworkMessage.STOP_ATTACK);
            return true;
        }

        return false; 
    }
}