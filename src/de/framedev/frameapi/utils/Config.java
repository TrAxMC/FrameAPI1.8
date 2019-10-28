package de.framedev.frameapi.utils;

import de.framedev.frameapi.main.FrameMain;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class Config {
	// Load the Config! / Put in the onEnable
		public static void loadConfig() {
			FrameMain.getInstance().getConfig().options().copyDefaults(true);
			FrameMain.getInstance().saveDefaultConfig();
	    }
		// Update the Config put in the onEnable
		public static void updateConfig() {
	        try {
	            if(new File(FrameMain.getInstance().getDataFolder() + "/config.yml").exists()) {
	                boolean changesMade = false;
	                YamlConfiguration tmp = new YamlConfiguration();
	                tmp.load(FrameMain.getInstance().getDataFolder() + "/config.yml");
	                for(String str : FrameMain.getInstance().getConfig().getKeys(true)) {
	                    if(!tmp.getKeys(true).contains(str)) {
	                        tmp.set(str, FrameMain.getInstance().getConfig().get(str));
	                        changesMade = true;
	                    }
	                }
	                if(changesMade){

	                    tmp.save(FrameMain.getInstance().getDataFolder() + "/config.yml");
	                }
	            }
	        } catch (IOException | InvalidConfigurationException e) {
	            e.printStackTrace();
	        }
	    }

}
