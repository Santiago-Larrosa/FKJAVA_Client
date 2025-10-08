
package com.FK.game.core;

public class UpgradeManager {

    public int getDamageUpgradeCost(PlayerData data) {
        return 20 + (data.attackDamageLevel * 15); 
    }

    public boolean canAffordDamageUpgrade(PlayerData data) {
        return data.coinCount >= getDamageUpgradeCost(data);
    }

    public void purchaseDamageUpgrade(PlayerData data) {
        if (canAffordDamageUpgrade(data)) {
            data.coinCount -= getDamageUpgradeCost(data);
            data.attackDamageLevel++;
            System.out.println("Daño mejorado a nivel " + data.attackDamageLevel);
        }
    }

    public int getHealthUpgradeCost(PlayerData data) {
        return 30 + (data.healthLevel * 25);
    }

    public boolean canAffordHealthUpgrade(PlayerData data) {
        return data.coinCount >= getHealthUpgradeCost(data);
    }

    public void purchaseHealthUpgrade(PlayerData data) {
        if (canAffordHealthUpgrade(data)) {
            data.coinCount -= getHealthUpgradeCost(data);
            data.healthLevel++;
            data.currentHealth = data.getMaxHealth();
            System.out.println("Vida mejorada a nivel " + data.healthLevel);
        }
    }
}