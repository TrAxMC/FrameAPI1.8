package de.framedev.frameapi.api;
/*This Plugin was Created by FrameDev
 * Copyrighted by FrameDev
 * 10.11.2019, 15:08
 */

import de.framedev.frameapi.interfaces.EnergyInterface;
import de.framedev.frameapi.main.FrameMain;
import de.framedev.frameapi.mysql.SQL;
import de.framedev.frameapi.utils.ReplaceCharConfig;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class Energy implements EnergyInterface {

    public static String energyprefix = "§a[§6Energy§a] §f";
    @Override
    public void setEnergy(OfflinePlayer player, int amount) {
        if(SQL.isTableExists("Energy")) {
            if(SQL.exists("PlayerUUID",player.getUniqueId().toString(),"Energy")) {
                SQL.UpdateData("Energy","'" + amount  + "'","Energy","PlayerUUID = '" + player.getUniqueId().toString() + "'");
            } else {
                SQL.InsertData("PlayerUUID,Energy","'" + player.getUniqueId().toString() + "','" + amount + "'","Energy");
            }
        } else {
            SQL.createTable("Energy","PlayerUUID VARCHAR(64),Energy INT");
            SQL.InsertData("PlayerUUID,Energy","'" + player.getUniqueId().toString() + "','" + amount + "'","Energy");
        }
    }

    @Override
    public void adminEnergy(Player player) {
        Bukkit.getOnlinePlayers().forEach(current ->{
            if(SQL.isTableExists("Energy")) {
                if(SQL.exists("PlayerUUID",current.getUniqueId().toString(),"Energy")) {
                    if(SQL.get("Energy","PlayerUUID",current.getUniqueId().toString(),"Energy") != null) {
                        Object energy = SQL.get("Energy","PlayerUUID",current.getUniqueId().toString(),"Energy");
                        player.sendMessage("§6[OnlinePlayers§6] §aEnergie von §b" + current.getName() + " §aBeträgt §b" + ReplaceCharConfig.convertObjectToInteger(energy));
                    }
                }
            } else {
                SQL.createTable("Energy","PlayerUUID VARCHAR(64),Energy INT");
            }
        });
        FrameMain.getInstance().getPlayers().forEach(current ->{
            if(SQL.isTableExists("Energy")) {
                if(SQL.exists("PlayerUUID",current.getUniqueId().toString(),"Energy")) {
                    if(SQL.get("Energy","PlayerUUID",current.getUniqueId().toString(),"Energy") != null) {
                        Object energy = SQL.get("Energy","PlayerUUID",current.getUniqueId().toString(),"Energy");
                        player.sendMessage("§6[OfflinePlayers§6] §aEnergie von §b" + current.getName() + " §aBeträgt §b" + ReplaceCharConfig.convertObjectToInteger(energy));
                    }
                }
            } else {
                SQL.createTable("Energy","PlayerUUID VARCHAR(64),Energy INT");
            }
        });
    }

    @Override
    public int getEnergy(OfflinePlayer player) {
        if(SQL.isTableExists("Energy")) {
            if(SQL.exists("PlayerUUID",player.getUniqueId().toString(),"Energy")) {
                if(SQL.get("Energy","PlayerUUID",player.getUniqueId().toString(),"Energy") != null) {
                    Object energy = SQL.get("Energy","PlayerUUID",player.getUniqueId().toString(),"Energy");
                    return ReplaceCharConfig.convertObjectToInteger(energy);
                } else {
                    return 0;
                }
            } else {
                return 0;
            }
        } else {
            SQL.createTable("Energy","PlayerUUID VARCHAR(64),Energy INT");
           return 0;
        }
    }
    @Override
    public void addEnergy(OfflinePlayer player, int amount) {
        int energy = getEnergy(player);
        energy = energy + amount;
        setEnergy(player, energy);
    }
    @Override
    public void removeEnergy(OfflinePlayer player, int amount) {
        int energy = getEnergy(player);
        energy = energy - amount;
        setEnergy(player, energy);
    }
}
