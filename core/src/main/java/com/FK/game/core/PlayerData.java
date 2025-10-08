
package com.FK.game.core; 

public class PlayerData {
    
    public int coinCount;
    public int healthLevel;
    public int attackDamageLevel;
    public int currentHealth;

    public PlayerData() {
        this.currentHealth = 5;
        this.attackDamageLevel = 1;
        this.currentHealth = getMaxHealth();
        this.coinCount = 0;
    }


    public int getAttackDamage() {
        return 5 + (attackDamageLevel - 1) * 2; 
    }

    public int getMaxHealth() {
        return 100 + (healthLevel - 1) * 20; 
    }

    public void resetOnDeath() {
        this.currentHealth = getMaxHealth();
    }
}