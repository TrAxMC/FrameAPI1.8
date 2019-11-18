package de.framedev.frameapi.interfaces;/*
 * This Plugin was Created by FrameDev
 * Copyrighted by FrameDev
 * 13.11.2019, 18:20
 */

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public interface EnergyInterface {

    void addEnergy(OfflinePlayer player, int amount);
    void removeEnergy(OfflinePlayer player, int amount);
    int getEnergy(OfflinePlayer player);
    void setEnergy(OfflinePlayer player, int amount);
    void adminEnergy(Player player);
}
