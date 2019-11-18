package de.framedev.frameapi.interfaces;/*
 * This Plugin was Created by FrameDev
 * Copyrighted by FrameDev
 * 13.11.2019, 18:01
 */

import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public interface EnergyListenerInterface {

    void run(Player player, Block block);
    void updateWorld(World world, Player player);
}
