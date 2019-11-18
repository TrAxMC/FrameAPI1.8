package de.framedev.frameapi.listeners.energy;
/*This Plugin was Created by FrameDev
 * Copyrighted by FrameDev
 * 10.11.2019, 15:16
 */

import de.framedev.frameapi.api.Energy;
import de.framedev.frameapi.interfaces.EnergyListenerInterface;
import de.framedev.frameapi.main.FrameMain;
import de.framedev.frameapi.materials.InventoryManager;
import de.framedev.frameapi.materials.ItemBuilder;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class CapacitorListener implements Listener, EnergyListenerInterface {

    public ArrayList<BukkitRunnable> runtime = new ArrayList<>();
    public ArrayList<UUID> playeruuid = new ArrayList<>();
    @EventHandler
    public void getCapacator(PlayerInteractEvent event) {
        if(event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if(event.getClickedBlock().getType() == Material.CAULDRON) {
                event.setCancelled(true);
                InventoryManager manager = new InventoryManager();
                manager.setName("§aEnergy");
                manager.setSize(3);
                manager.build();
                manager.setItem(new ItemBuilder(Material.NETHER_STAR).setName("§aYour Energy").setLore("§aEnergy = " + new Energy().getEnergy(event.getPlayer())).build(),10);
                manager.setItem(new ItemBuilder(Material.STAINED_GLASS_PANE).setName("§b§lVerkaufen").build(),12);
                manager.FillNull();
                manager.showInv(event.getPlayer());
            }
        }
    }
    @SuppressWarnings( "deprecation" )
    @EventHandler
    public void onClickEnergy(InventoryClickEvent event) {
        if(event.getInventory() != null) {
            if(event.getView().getTitle().equalsIgnoreCase("§aEnergy")) {
                if(event.getCurrentItem() != null){
                    if(event.getCurrentItem().hasItemMeta()) {
                        if (event.getCurrentItem().getType() == Material.NETHER_STAR && event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§aYour Energy")) {
                            event.setCancelled(true);
                        } else if(event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§b§lVerkaufen")) {
                            event.setCancelled(true);
                            InventoryManager verkaufen = new InventoryManager();
                            verkaufen.setName("§b§lVerkaufen Lege ein Item in den Mittleren Slot");
                            verkaufen.setSize(1);
                            verkaufen.build();
                            verkaufen.showInv((Player) event.getWhoClicked());
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    for (int i = 0; i < verkaufen.getInventory().getSize(); i++) {
                                        if (verkaufen.getInventory().getItem(i) != null) {
                                            int durability = 100;
                                            new Energy().addEnergy((OfflinePlayer) event.getWhoClicked(), durability);
                                            int amount = durability;
                                            verkaufen.setItem(new ItemStack(Material.AIR), i);
                                            verkaufen.setName("§a§lErfolgreich Verkauft fuer §c§l" + amount + " §a§lEnergy!");
                                            verkaufen.build();
                                            verkaufen.showInv((Player) event.getWhoClicked());
                                            new BukkitRunnable() {
                                                @Override
                                                public void run() {
                                                    event.getWhoClicked().closeInventory();
                                                    cancel();
                                                }
                                            }.runTaskLater(FrameMain.getInstance(), 40);
                                        }
                                    }
                                }
                            }.runTaskTimer(FrameMain.getInstance(),0,20);
                        } else {
                            event.setCancelled(true);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void run(Player player, Block block) {
        if(block.getType() == Material.PISTON_BASE) {
            int amount = 1000;
            if(new Energy().getEnergy(player) >= amount) {
                if(playeruuid.contains(player.getUniqueId())) {
                    player.sendMessage("§cCapacität Voll!");
                    playeruuid.remove(player.getUniqueId());
                    return;
                }
            } else {
                if(!playeruuid.contains(player.getUniqueId())) {
                    playeruuid.add(player.getUniqueId());
                    if(block.getBlockPower() == 15) {
                        new Energy().addEnergy(player,15);
                    } else if(block.getBlockPower() < 15) {
                        new Energy().addEnergy(player,5);
                    }
                } else {
                    if(block.getBlockPower() == 15) {
                        new Energy().addEnergy(player,15);
                    } else if(block.getBlockPower() < 15) {
                        new Energy().addEnergy(player,5);
                    }
                }
            }

        } else if(block.getType() == Material.PISTON_STICKY_BASE) {
            int amount = 2500;
            if(new Energy().getEnergy(player) >= amount) {
                player.sendMessage("§cCapacität Voll!");
                playeruuid.remove(player.getUniqueId());
                return;
            }
            if(block.getBlockPower() == 15) {
                new Energy().addEnergy(player,25);
            } else if(block.getBlockPower() < 15) {
                new Energy().addEnergy(player,10);
            }
        }

    }
    public void runOnBeacon(Player player, Block block) {
        Location block1 = new Location(player.getWorld(),block.getX(),block.getY(),block.getZ() + 1);
        Location block2 = new Location(player.getWorld(),block.getX(),block.getY(),block.getZ() - 1);
        Location block3 = new Location(player.getWorld(),block.getX() + 1,block.getY(),block.getZ());
        Location block4 = new Location(player.getWorld(),block.getX() - 1,block.getY(),block.getZ());
        if(block1.getBlock().getType() == Material.OBSIDIAN && block2.getBlock().getType() == Material.OBSIDIAN &&
                block3.getBlock().getType() == Material.OBSIDIAN && block4.getBlock().getType() == Material.OBSIDIAN) {
            if (block.getType() == Material.BEACON) {
                int amount = 100000;
                if (new Energy().getEnergy(player) >= amount) {
                    if(playeruuid.contains(player.getUniqueId())) {
                        player.sendMessage("§cCapacität Voll!");
                        playeruuid.remove(player.getUniqueId());
                        return;
                    }
                } else {
                    if(playeruuid.contains(player.getUniqueId())) {
                        new Energy().addEnergy(player, 100);
                    } else {
                        new Energy().addEnergy(player, 100);
                        playeruuid.add(player.getUniqueId());
                    }
                }
            }
        }
    }
    @Override
    public void updateWorld(World world,Player player) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if(!runtime.contains(this)) {
                    runtime.add(this);
                }
                Location loc1 = new Location(world,
                        cfg.getInt("Piston." + player.getName() + ".x"),
                        cfg.getInt("Piston." + player.getName() + ".y"),
                        cfg.getInt("Piston." + player.getName() + ".z"));
                if (world.getBlockAt(loc1).getType() == Material.PISTON_BASE) {
                    CapacitorListener.this.run(player, world.getBlockAt(loc1));
                }
                Location loc2 = new Location(world,
                        cfg.getInt("Beacon." + player.getName() + ".x"),
                        cfg.getInt("Beacon." + player.getName() + ".y"),
                        cfg.getInt("Beacon." + player.getName() + ".z"));
                Block block5 = loc2.getBlock();
                Location block6 = new Location(player.getWorld(), block5.getX(), block5.getY(), block5.getZ() + 1);
                Location block7 = new Location(player.getWorld(), block5.getX(), block5.getY(), block5.getZ() - 1);
                Location block8 = new Location(player.getWorld(), block5.getX() + 1, block5.getY(), block5.getZ());
                Location block9 = new Location(player.getWorld(), block5.getX() - 1, block5.getY(), block5.getZ());
                if (block6.getBlock().getType() == Material.OBSIDIAN && block7.getBlock().getType() == Material.OBSIDIAN &&
                        block8.getBlock().getType() == Material.OBSIDIAN && block9.getBlock().getType() == Material.OBSIDIAN) {
                    if (block5.getType() == Material.BEACON) {
                        CapacitorListener.this.runOnBeacon(player, world.getBlockAt(loc2));
                    }
                }

                if (playeruuid.contains(player.getUniqueId())) {
                    try {
                        Location loc = new Location(world,
                                cfg.getInt("Beacon." + player.getName() + ".x"),
                                cfg.getInt("Beacon." + player.getName() + ".y"),
                                cfg.getInt("Beacon." + player.getName() + ".z"));
                        Block block = loc.getBlock();
                        Location block1 = new Location(player.getWorld(), block.getX(), block.getY(), block.getZ() + 1);
                        Location block2 = new Location(player.getWorld(), block.getX(), block.getY(), block.getZ() - 1);
                        Location block3 = new Location(player.getWorld(), block.getX() + 1, block.getY(), block.getZ());
                        Location block4 = new Location(player.getWorld(), block.getX() - 1, block.getY(), block.getZ());
                        if (block1.getBlock().getType() == Material.OBSIDIAN && block2.getBlock().getType() == Material.OBSIDIAN &&
                                block3.getBlock().getType() == Material.OBSIDIAN && block4.getBlock().getType() == Material.OBSIDIAN) {
                            if (block.getType() == Material.BEACON) {
                                CapacitorListener.this.runOnBeacon(player, world.getBlockAt(loc));
                                if (new Energy().getEnergy(player) >= 10000) {
                                    //player.getInventory().addItem(new ItemStack(Material.DIAMOND));
                                    //new Energy().removeEnergy(player, 10000);
                                }
                            }
                        }
                    } catch (NullPointerException ex) {
                    }
                    try {
                        Location loc = new Location(world,
                                cfg.getInt("Piston." + player.getName() + ".x"),
                                cfg.getInt("Piston." + player.getName() + ".y"),
                                cfg.getInt("Piston." + player.getName() + ".z"));
                        if (world.getBlockAt(loc).getType() == Material.PISTON_BASE) {
                            CapacitorListener.this.run(player, world.getBlockAt(loc));
                            if (new Energy().getEnergy(player) >= 500) {
                                // player.getInventory().addItem(new ItemStack(Material.IRON_INGOT));
                                //new Energy().removeEnergy(player, 500);
                            }
                        }
                    } catch (NullPointerException ex) {
                    }
                    try {
                        Location loc = new Location(world,
                                cfg.getInt("Glass." + player.getName() + ".x"),
                                cfg.getInt("Glass." + player.getName() + ".y"),
                                cfg.getInt("Glass." + player.getName() + ".z"));
                        Block block = loc.getBlock();
                        Location block1 = new Location(player.getWorld(), block.getX(), block.getY(), block.getZ() + 1);
                        Location block2 = new Location(player.getWorld(), block.getX(), block.getY(), block.getZ() - 1);
                        Location block3 = new Location(player.getWorld(), block.getX() + 1, block.getY(), block.getZ());
                        Location block4 = new Location(player.getWorld(), block.getX() - 1, block.getY(), block.getZ());
                        if (block1.getBlock().getType() == Material.OBSIDIAN && block2.getBlock().getType() == Material.OBSIDIAN &&
                                block3.getBlock().getType() == Material.OBSIDIAN && block4.getBlock().getType() == Material.OBSIDIAN) {
                            if (block.getType() == Material.GLASS) {
                                CapacitorListener.this.runOnBeacon(player, world.getBlockAt(loc));
                                if (new Energy().getEnergy(player) >= 10000) {
                                    //player.getInventory().addItem(new ItemStack(Material.DIAMOND));
                                    //new Energy().removeEnergy(player, 10000);
                                }
                            }
                        }
                    } catch (NullPointerException ex) {
                    }
                }
            }
        }.runTaskTimer(FrameMain.getInstance(), 0, 200);
    }
    private int getInt(Player player) {
        return player.getEntityId();
    }
    public void runOnSolar(Player player, Block block) {
        Location block1 = new Location(player.getWorld(),block.getX(),block.getY(),block.getZ() + 1);
        Location block2 = new Location(player.getWorld(),block.getX(),block.getY(),block.getZ() - 1);
        Location block3 = new Location(player.getWorld(),block.getX() + 1,block.getY(),block.getZ());
        Location block4 = new Location(player.getWorld(),block.getX() - 1,block.getY(),block.getZ());
        if(block1.getBlock().getType() == Material.OBSIDIAN && block2.getBlock().getType() == Material.OBSIDIAN &&
                block3.getBlock().getType() == Material.OBSIDIAN && block4.getBlock().getType() == Material.OBSIDIAN) {
            if (block.getType() == Material.GLASS) {
                int amount = 2000000;
                if (new Energy().getEnergy(player) >= amount) {
                    if(playeruuid.contains(player.getUniqueId())) {
                        player.sendMessage("§cCapacität Voll!");
                        playeruuid.remove(player.getUniqueId());
                        return;
                    }
                } else {
                    if(playeruuid.contains(player.getUniqueId())) {
                        new Energy().addEnergy(player, 5);
                    } else {
                        new Energy().addEnergy(player, 5);
                        playeruuid.add(player.getUniqueId());
                    }
                }
            }
        }
    }
    public void saveCfg() {
        try {
            cfg.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    File file = new File("plugins/FrameAPI/Pistons.yml");
    FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
    @EventHandler
    public void blockUpdate(BlockPlaceEvent event) {
        if(event.getBlock().getType() == Material.BEACON) {
            if(cfg.getBoolean("Beacon." + event.getPlayer().getName() + ".boolean") == false) {
                cfg.set("Beacon." + event.getPlayer().getName() + ".x", event.getBlock().getX());
                cfg.set("Beacon." + event.getPlayer().getName() + ".y", event.getBlock().getY());
                cfg.set("Beacon." + event.getPlayer().getName() + ".z", event.getBlock().getZ());
                cfg.set("Beacon." + event.getPlayer().getName() + ".boolean", true);

                saveCfg();
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        runtime.add(this);
                        if(event.getPlayer().isOnline()) {
                            CapacitorListener.this.updateWorld(event.getPlayer().getWorld(),event.getPlayer());
                        }
                    }
                }.runTaskLater(FrameMain.getInstance(),  120);
            } else {
                event.getPlayer().sendMessage("§a[§6Energy§a] §fNur eine Speicher Kapazität dieser grösse kann verwendet werden!");
            }
        }
        if(event.getBlock().getType() == Material.GLASS) {
            Player player = event.getPlayer();
            Block block = event.getBlock();
            Location block1 = new Location(player.getWorld(),block.getX(),block.getY(),block.getZ() + 1);
            Location block2 = new Location(player.getWorld(),block.getX(),block.getY(),block.getZ() - 1);
            Location block3 = new Location(player.getWorld(),block.getX() + 1,block.getY(),block.getZ());
            Location block4 = new Location(player.getWorld(),block.getX() - 1,block.getY(),block.getZ());
            if(block1.getBlock().getType() == Material.OBSIDIAN && block2.getBlock().getType() == Material.OBSIDIAN &&
                    block3.getBlock().getType() == Material.OBSIDIAN && block4.getBlock().getType() == Material.OBSIDIAN) {
                if (block.getType() == Material.GLASS) {
                    if (cfg.getBoolean("Glass." + event.getPlayer().getName() + ".boolean") == false) {
                        cfg.set("Glass." + event.getPlayer().getName() + ".x", event.getBlock().getX());
                        cfg.set("Glass." + event.getPlayer().getName() + ".y", event.getBlock().getY());
                        cfg.set("Glass." + event.getPlayer().getName() + ".z", event.getBlock().getZ());
                        cfg.set("Glass." + event.getPlayer().getName() + ".boolean", true);

                        saveCfg();
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                runtime.add(this);
                                if (event.getPlayer().isOnline()) {
                                    CapacitorListener.this.updateWorld(event.getPlayer().getWorld(), event.getPlayer());
                                }
                            }
                        }.runTaskLater(FrameMain.getInstance(), 120);
                    } else {
                        event.getPlayer().sendMessage("§a[§6Energy§a] §fNur eine Speicher Kapazität dieser grösse kann verwendet werden!");
                    }
                }
            }
        }
        if (event.getBlock().getType() == Material.PISTON_BASE) {
            if(cfg.getBoolean("Piston." + event.getPlayer().getName() + ".boolean") == false) {
                cfg.set("Piston." + event.getPlayer().getName() + ".x", event.getBlock().getX());
                cfg.set("Piston." + event.getPlayer().getName() + ".y", event.getBlock().getY());
                cfg.set("Piston." + event.getPlayer().getName() + ".z", event.getBlock().getZ());
                cfg.set("Piston." + event.getPlayer().getName() + ".boolean", true);
                saveCfg();
                if (!file.exists()) {
                    try {
                        file.mkdir();
                    } catch (Exception e) {
                        Bukkit.getConsoleSender().sendMessage("§cAn Issue while Create File §f" + e.getMessage());
                    }
                }
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        runtime.add(this);
                        if(event.getPlayer().isOnline()) {
                            CapacitorListener.this.updateWorld(event.getPlayer().getWorld(),event.getPlayer());
                        }
                    }
                }.runTaskLater(FrameMain.getInstance(),  120);
            } else {
                event.getPlayer().sendMessage("§a[§6Energy§a] §fNur eine Speicher Kapazität dieser grösse kann verwendet werden!");
            }
        }
    }
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if(event.getBlock().getType() == Material.PISTON_BASE) {
            Location loc = new Location(event.getPlayer().getWorld(),
                    cfg.getInt("Piston." + event.getPlayer().getName() + ".x"),
                    cfg.getInt("Piston." + event.getPlayer().getName() + ".y"),
                    cfg.getInt("Piston." + event.getPlayer().getName() + ".z"));
            if (event.getBlock().getLocation().equals(loc)) {
                cfg.set("Piston." + event.getPlayer().getName() + ".boolean", false);
                saveCfg();
                event.getPlayer().sendMessage("§aCapacitor Piston entfernt!");
            } else if (event.getBlock().getType() == Material.BEACON) {
                Location loc1 = new Location(event.getPlayer().getWorld(),
                        cfg.getInt("Beacon." + event.getPlayer().getName() + ".x"),
                        cfg.getInt("Beacon." + event.getPlayer().getName() + ".y"),
                        cfg.getInt("Beacon." + event.getPlayer().getName() + ".z"));
                if (event.getBlock().getLocation().equals(loc1)) {
                    cfg.set("Beacon." + event.getPlayer().getName() + ".boolean", false);
                    saveCfg();
                    event.getPlayer().sendMessage("§aCapacitor Beacon entfernt!");
                }
            } else if (event.getBlock().getType() == Material.GLASS) {
                Player player = event.getPlayer();
                Block block = event.getBlock();
                Location block1 = new Location(player.getWorld(),block.getX(),block.getY(),block.getZ() + 1);
                Location block2 = new Location(player.getWorld(),block.getX(),block.getY(),block.getZ() - 1);
                Location block3 = new Location(player.getWorld(),block.getX() + 1,block.getY(),block.getZ());
                Location block4 = new Location(player.getWorld(),block.getX() - 1,block.getY(),block.getZ());
                if(block1.getBlock().getType() == Material.OBSIDIAN && block2.getBlock().getType() == Material.OBSIDIAN &&
                        block3.getBlock().getType() == Material.OBSIDIAN && block4.getBlock().getType() == Material.OBSIDIAN) {
                    if (block.getType() == Material.GLASS) {
                        Location loc1 = new Location(event.getPlayer().getWorld(),
                                cfg.getInt("Glass." + event.getPlayer().getName() + ".x"),
                                cfg.getInt("Glass." + event.getPlayer().getName() + ".y"),
                                cfg.getInt("Glass." + event.getPlayer().getName() + ".z"));
                        if (event.getBlock().getLocation().equals(loc1)) {
                            cfg.set("Glass." + event.getPlayer().getName() + ".boolean", false);
                            saveCfg();
                            event.getPlayer().sendMessage("§aCapacitor Solar entfernt!");
                        }
                    }
                }
            }
        }
    }

}
