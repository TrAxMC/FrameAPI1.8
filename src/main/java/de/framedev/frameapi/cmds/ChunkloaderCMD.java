package de.framedev.frameapi.cmds;

import de.framedev.frameapi.main.FrameMain;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

public class ChunkloaderCMD implements CommandExecutor {
    private final FrameMain plugin;

    public ChunkloaderCMD(FrameMain plugin) {
        this.plugin = plugin;
        plugin.getCommand("loader").setExecutor(this);
    }

    public static File file = new File("plugins/FrameAPI/chunkloader.yml");
    public static FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {
            Player player = (Player) sender;
            if (command.getName().equalsIgnoreCase("loader")) {
                if (player.hasPermission("frameapi.chunkloader")) {
                    if (cfg.get(args[0] + ".boolean") != null) {
                        if (player.getWorld().getChunkAt(player.getLocation()).isLoaded() == false) {
                            if (cfg.getBoolean(args[0] + ".boolean") == false) {
                                cfg.set(args[0] + ".boolean", true);
                                cfg.set(args[0] + ".world", player.getWorld().getName());
                                cfg.set(args[0] + ".x", player.getLocation().getBlockX());
                                cfg.set(args[0] + ".y", player.getLocation().getBlockY());
                                cfg.set(args[0] + ".z", player.getLocation().getBlockZ());
                                try {
                                    cfg.save(file);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                if (!file.exists()) {
                                    try {
                                        file.createNewFile();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                                player.sendMessage("§b" + args[0] + " §aAdded to ChunkLoader");
                                player.getWorld().getChunkAt(player.getLocation()).load(true);
                            }
                        } else {
                            if (cfg.getBoolean(args[0] + ".boolean")) {
                                player.getWorld().getChunkAt(player.getLocation()).load(false);
                                cfg.set(args[0] + ".boolean", false);
                                try {
                                    cfg.save(file);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                if (!file.exists()) {
                                    try {
                                        file.createNewFile();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                                player.sendMessage("§b" + args[0] + " §aRemoved to ChunkLoader");
                            }
                        }
                    } else if (player.getWorld().getChunkAt(player.getLocation()).isLoaded() == false) {
                        cfg.set(args[0] + ".boolean", true);
                        cfg.set(args[0] + ".world", player.getWorld().getName());
                        cfg.set(args[0] + ".x", player.getLocation().getBlockX());
                        cfg.set(args[0] + ".y", player.getLocation().getBlockY());
                        cfg.set(args[0] + ".z", player.getLocation().getBlockZ());
                        try {
                            cfg.save(file);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (!file.exists()) {
                            try {
                                file.createNewFile();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        player.sendMessage("§b" + args[0] + " §aAdded to ChunkLoader");
                        player.getWorld().getChunkAt(player.getLocation()).load(true);
                    } else {
                        player.getWorld().getChunkAt(player.getLocation()).load(false);
                        cfg.set(args[0] + ".boolean", false);
                        try {
                            cfg.save(file);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (!file.exists()) {
                            try {
                                file.createNewFile();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        player.sendMessage("§b" + args[0] + " §aRemoved to ChunkLoader");
                    }
                } else{
                    player.sendMessage(FrameMain.FrameMainGet.getPrefix() + " " + FrameMain.FrameMainGet.getNoPerm());
                }
            }
        }
        return false;
    }
}