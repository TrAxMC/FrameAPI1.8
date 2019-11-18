/**
 * Dies ist ein Plugin von FrameDev
 * Bitte nichts §ndern, @Copyright by FrameDev 
 */
package de.framedev.frameapi.kits;

import de.framedev.frameapi.main.FrameMain;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;

/**
 * @author Darryl
 *
 */
public class GetKits {
	private static File customConfigFile;
    private static FileConfiguration customConfig;
	/**
	 * @return the Custom Config == kits.yml
	 */
	public static FileConfiguration getCustomConfig() {
	
        return customConfig;
    }

    /**
     * in use for creating the file messages.yml
     */
    public void createCustomConfig() {
    	
        customConfigFile = new File(FrameMain.getInstance().getDataFolder(), "kits.yml");
        if (!customConfigFile.exists()) {
            customConfigFile.getParentFile().mkdirs();
            FrameMain.getInstance().saveResource("kits.yml", false);
         }

        customConfig= new YamlConfiguration();
        try {
            customConfig.load(customConfigFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }
	/**
	 * 
	 */
	public GetKits() {
		
	}
	/**
	 * Load Kit from kits.yml
	 * @param name KitName
	 * @param player Player
	 */
	public void loadKits(String name, Player player) {
		
		for(String s : getCustomConfig().getStringList("Items." + name)) {
			String[] x = s.split(",");
			ItemStack item = new ItemStack(Material.getMaterial(x[0]), Integer.valueOf(x[1]));
			kitname.addItem(item);
		}
		for(ItemStack items : kitname.getContents()) {
			if(items != null) {
				player.getInventory().addItem(items);
				VoidClearInventory();
			} else {
				return;
			}
		}
	}
	public Inventory kitname = Bukkit.createInventory(null, 4*9);
	/**
	 * Clear KitsInventory
	 */
	public void VoidClearInventory() {
		
		kitname.clear();
	}

}
