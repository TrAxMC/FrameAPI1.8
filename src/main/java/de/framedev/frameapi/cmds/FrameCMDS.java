package de.framedev.frameapi.cmds;

import de.framedev.frameapi.api.API;
import de.framedev.frameapi.main.FrameMain;
import de.framedev.frameapi.materials.InventoryManager;
import de.framedev.frameapi.utils.Config;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FrameCMDS implements CommandExecutor, TabCompleter, Listener {

    public static InventoryManager inventoryManager = new InventoryManager();
    private static API api = new API();
    private static FileConfiguration cfg = FrameMain.getInstance().getConfig();
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(cmd.getName().equalsIgnoreCase("frameapi")) {
            try {
                if(args[0].equalsIgnoreCase("getmysql")) {
                    if(sender instanceof Player) {
                        Player p = (Player) sender;
                        if(p.hasPermission("frameapi.mysql")) {
                            api.getInformationMYSQL(p);
                            Bukkit.getPluginManager().callEvent(new API.SendMessageEvent(p));
                            return true;
                        } else {
                            p.sendMessage(FrameMain.FrameMainGet.getPrefix() + " " + FrameMain.FrameMainGet.getNoPerm());
                            return true;
                        }
                    }
                }
                if(args[0].equalsIgnoreCase("info")) {
                    if(sender instanceof Player) {
                        Player p = (Player) sender;
                        inventoryManager.setName("§aFrameAPI Info").setSize(3).build();
                        inventoryManager.setItem(api.createWrittenBook("§aFrameAPI Info Book","This Book was Written by FrameDev \n" +
                                "Good Luck FrameDev"),10);
                        inventoryManager.FillNull();
                        inventoryManager.showInv(p);

                        api.getInformation(p);
                        return true;
                    }
                }
                if(args[0].equalsIgnoreCase("load")) {
                    API.CreateConfig.onloadedfalse();
                }
                if(args[0].equalsIgnoreCase("reload")) {
                    if(sender instanceof Player) {
                        Player p = (Player) sender;
                        if(p.hasPermission("frameapi.config")) {
                            FrameMain.getInstance().reloadConfig();
                            Config.updateConfig();
                            Config.loadConfig();
                            Bukkit.getPluginManager().disablePlugin(FrameMain.getInstance());
                            Bukkit.getPluginManager().enablePlugin(FrameMain.getInstance());
                            p.sendMessage("§a[§aFrameAPI]§b Reload Config successfully!");
                            return true;
                        } else {
                            p.sendMessage(FrameMain.FrameMainGet.getPrefix() + " " + FrameMain.FrameMainGet.getNoPerm());
                            return true;
                        }
                    }
                }
                if(args[0].equalsIgnoreCase("help")) {
                    if(sender instanceof Player) {
                        Player p = (Player) sender;
                        api.sendHelp(p);
                        return true;
                    }
                }
                if(args[0].equalsIgnoreCase("startbudget")) {
                    if(sender instanceof Player) {
                        Player p = (Player) sender;
                        if(p.hasPermission("frameapi.startbudget")) {
                            try {
                                if(args[1].equalsIgnoreCase("on")) {
                                    FrameMain.getInstance().getConfig().set("StartMoney", true);
                                    FrameMain.getInstance().saveConfig();
                                    p.sendMessage(FrameMain.FrameMainGet.getPrefix() + " §bStartBudget Enabled");
                                    return true;
                                } else if(args[1].equalsIgnoreCase("off")) {
                                    FrameMain.getInstance().getConfig().set("StartMoney", false);
                                    FrameMain.getInstance().saveConfig();
                                    p.sendMessage(FrameMain.FrameMainGet.getPrefix() + " §bStartBudget Disabled");
                                    return true;
                                } else if(args[1].equalsIgnoreCase("set")) {
                                    //frameapi startbudget amount
                                    //0			1			2
                                        double money = Double.parseDouble(args[2]);
                                        FrameMain.getInstance().getConfig().set("StartBudget", money);
                                        FrameMain.getInstance().saveConfig();
                                        String text = API.CreateConfig.getConfig().getString("Startbudget.Text.Set");
                                        String money1 = String.valueOf(money);
                                        text = text.replace("[Money]", money1);
                                        text = text.replace('&', '§');
                                        p.sendMessage(FrameMain.FrameMainGet.getPrefix() + " " +text);
                                        return true;
                                }
                            } catch (ArrayIndexOutOfBoundsException e) {
                                p.sendMessage("§cuse /frameapi startbudget (on/off) or (amount)");
                                return true;
                            }
                        } else {
                            p.sendMessage(FrameMain.FrameMainGet.getPrefix() + " " + FrameMain.FrameMainGet.getNoPerm());
                            return true;
                        }

                    }
                }
                if(args[0].equalsIgnoreCase("time")) {
                    if(sender instanceof Player) {
                        Player p = (Player) sender;
                        if(p.hasPermission("frameapi.time")) {
                            int time = (int) p.getWorld().getTime();
                            p.sendMessage("§aTicks = §b" + time);
                            p.sendMessage("§a13000 = §bNight");
                            p.sendMessage("§a0 = §bDay");
                            p.sendMessage(ChatColor.GOLD + " " + getDate());
                            return true;
                        }
                    } else {
                        if(sender instanceof Player) {
                            Player p = (Player) sender;
                            p.sendMessage(FrameMain.FrameMainGet.getPrefix() + " " + FrameMain.FrameMainGet.getNoPerm());
                            return true;
                        }
                    }
                } else {
                    if(sender instanceof Player) {
                        Player p = (Player) sender;
                        api.sendHelp(p);
                    }
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                if(sender instanceof Player) {
                    Player p = (Player) sender;
                    api.sendHelp(p);
                    return true;
                }
            }
        }
        return false;
    }
    private static SimpleDateFormat date2=new SimpleDateFormat("HH.mm.ss");
    private static String Uhrzeit2=date2.format(new Date());
    private static String getDate() {
        return Uhrzeit2;

    }
        @Override
        public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        ArrayList<String> cmdframeapi = new ArrayList<String>();
        cmdframeapi.add("info");
        cmdframeapi.add("time");
        cmdframeapi.add("getmysql");
        cmdframeapi.add("reload");
        cmdframeapi.add("startbudget");
        cmdframeapi.add("spawnmob");
        cmdframeapi.add("help");
        if(command.getName().equalsIgnoreCase("frameapi")) {
            if (args.length == 1) {
                return cmdframeapi;
            } else if (args[0].equalsIgnoreCase("startbudget")) {
                ArrayList<String> money = new ArrayList<String>();
                money.add("on");
                money.add("off");
                return money;
            }
        }
        return null;
    }
    @EventHandler
    public void onClickFrameAPIBook(InventoryClickEvent e) {
        if(e.getSlotType() == InventoryType.SlotType.OUTSIDE) return;
        if(e.getInventory().getTitle().equalsIgnoreCase("§aFrameAPI Info")) {
            if(e.getCurrentItem() != null) {
                if(e.getCurrentItem().hasItemMeta()) {
                    if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§aFrameAPI Info Book")) {
                        e.setCancelled(true);
                        e.getWhoClicked().getInventory().addItem(api.createWrittenBook("§aFrameAPI Info Book","This Book was Written by FrameDev \n" +
                                "Good Luck FrameDev"));
                        e.getWhoClicked().closeInventory();
                    } else {
                        e.setCancelled(true);
                    }
                } else {
                    e.setCancelled(true);
                }
            }
        }
    }
}