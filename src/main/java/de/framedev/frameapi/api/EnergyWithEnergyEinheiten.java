package de.framedev.frameapi.api;
/*
 * This Plugin was Created by FrameDev
 * Copyrighted by FrameDev
 * 17.11.2019, 15:19
 */

import de.framedev.frameapi.interfaces.EnergyEinheiten;
import de.framedev.frameapi.interfaces.EnergyInterfaceWithEnergyEinheiten;
import org.bukkit.OfflinePlayer;

public class EnergyWithEnergyEinheiten implements EnergyInterfaceWithEnergyEinheiten {
    public EnergyWithEnergyEinheiten() {
    }

    @Override
    public void addEnergy(OfflinePlayer player, int amount, EnergyEinheiten energyEinheiten) {
        int energy = new Energy().getEnergy(player);
        amount = amount + energyEinheiten.getAmount();
        energy = energy + amount;
        new Energy().setEnergy(player, energy);
    }

    @Override
    public void removeEnergy(OfflinePlayer player, int amount, EnergyEinheiten energyEinheiten) {
        int energy = new Energy().getEnergy(player);
        amount = amount + energyEinheiten.getAmount();
        energy = energy - amount;
        new Energy().setEnergy(player, energy);
    }
}
