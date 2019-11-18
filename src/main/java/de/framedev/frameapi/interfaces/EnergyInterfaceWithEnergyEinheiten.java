package de.framedev.frameapi.interfaces;/*
 * This Plugin was Created by FrameDev
 * Copyrighted by FrameDev
 * 17.11.2019, 15:18
 */

import org.bukkit.OfflinePlayer;

public interface EnergyInterfaceWithEnergyEinheiten {
    void addEnergy(OfflinePlayer player, int amount, EnergyEinheiten energyEinheiten);
    void removeEnergy(OfflinePlayer player, int amount, EnergyEinheiten energyEinheiten);
}
