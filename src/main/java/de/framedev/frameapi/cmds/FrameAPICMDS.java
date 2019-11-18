/**
 * Dies ist ein Plugin von FrameDev
 * Bitte nichts §ndern, @Copyright by FrameDev 
 */
package de.framedev.frameapi.cmds;

import de.framedev.frameapi.api.API;
import de.framedev.frameapi.api.API.CreateConfig;
import de.framedev.frameapi.api.money.Money;
import de.framedev.frameapi.kits.GetKits;
import de.framedev.frameapi.main.FrameMain;
import de.framedev.frameapi.main.FrameMain.FrameMainGet;
import de.framedev.frameapi.utils.ReplaceCharConfig;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Darryl
 *
 */
public class FrameAPICMDS implements CommandExecutor, TabCompleter, Listener {
	private static File warpfile = new File("plugins/FrameAPI/WarpFile/warps.yml");
	private static FileConfiguration cfgwarp = YamlConfiguration.loadConfiguration(warpfile);
	private static File deniedFile = new File("plugins/FrameAPI","DeniedWords.yml");
	private static FileConfiguration deniedcfg = YamlConfiguration.loadConfiguration(deniedFile);
	private static API api = new API();
	private static FileConfiguration cfg = FrameMain.getInstance().getConfig();
	private Money eco = new Money();
	@SuppressWarnings( {"deprecation", "ConstantConditions"} )
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(cmd.getName().equalsIgnoreCase("teleporter")) {
			if(sender instanceof Player) {
				Player p = (Player) sender;
				if(p.hasPermission("frameapi.teleporter")) {
					api.TeleporterWithHeads(p);
					return true;
				} else {
					p.sendMessage(FrameMainGet.getPrefix() + " " + FrameMainGet.getNoPerm());
					return true;
				}
			}
		}
		if(cmd.getName().equalsIgnoreCase("back")) {
			if(sender instanceof Player) {
				Player p = (Player) sender;
				if(FrameMain.getInstance().getConfig().getBoolean("OldLoc.Teleport")) {
					if(API.oldloc.containsKey(p.getName())) {
						if(p.hasPermission("frameapi.backtp")) {
							Location loc = API.oldloc.get(p.getName());
							p.teleport(loc);
							String message = CreateConfig.getConfig().getString("message.TeleportBack");
							message = message.replace('&', '§');
							p.sendMessage(message);
							return true;
						} else {
							p.sendMessage(FrameMainGet.getPrefix() + " " + FrameMainGet.getNoPerm());
							return true;
						}	
					} else {
						String message = CreateConfig.getConfig().getString("message.NoLocation");
						message = message.replace('&', '§');
						p.sendMessage(message);
						return true;
					}
				} else {
					String message = CreateConfig.getConfig().getString("message.notactivated");
					message = message.replace('&', '§');
					p.sendMessage(message);
					return true;
				}
			}
		}
		if(cmd.getName().equalsIgnoreCase("spawnmob")) {
			if(sender instanceof Player) {
				Player p = (Player) sender;
				if(args.length == 0) {
					p.sendMessage(FrameMainGet.getPrefix() + " §aPlease use §b/spawnmob (type) §aor §b/spawnmob (type) (amount)");
					return true;
				} else if(args.length == 1) {
					if(p.hasPermission("frameapi.spawnmob")) {
						EntityType type = EntityType.fromName(args[0]);
						World world = p.getWorld();
						Block block = p.getTargetBlock((HashSet<Byte>) null, 100);
						Location bl = block.getLocation();
						bl.setY(block.getLocation().getY() + 1);
						world.spawnEntity(bl, type);
					}
				} else if (args.length == 2) {
					for(int i = 1; i <= Integer.parseInt(args[1]); i++) {
						EntityType type = EntityType.fromName(args[0]);
						World world = p.getWorld();
						Block block = p.getTargetBlock((HashSet<Byte>) null, 100);
						Location bl = block.getLocation();
						bl.setY(block.getLocation().getY() + 1);
						world.spawnEntity(bl, type);
					}
					//spawnmob type amount
					//0			1		2
				}
			}
		}
		if(cmd.getName().equalsIgnoreCase("denied")) {
			if(sender instanceof Player) {
				Player p = (Player) sender;
				if(FrameMain.getInstance().getConfig().getBoolean("Chat.Denied")) {
					if(args[0].equalsIgnoreCase("remove")) {
						if(p.hasPermission("frameapi.denied")) {
						List<String> words = deniedcfg.getStringList("deniedwords");
							if(words.contains(args[1])) {
								words.remove(args[1]);
								p.sendMessage(args[1] + " wurde entfernt!");
								deniedcfg.set("deniedwords", words);
								try {
									deniedcfg.save(deniedFile);
								} catch (IOException e) {
									e.printStackTrace();
								}	
								return true;
							} else {
								p.sendMessage(args[1] + " ist nicht in der Liste!");
								return true;
							}
						} else {
							p.sendMessage(FrameMainGet.getPrefix() + " " + FrameMainGet.getNoPerm());
							return true;
						}
					}
					else if(args[0].equalsIgnoreCase("add")) {
						List<String> words = deniedcfg.getStringList("deniedwords");
						words.add(args[1]);
						p.sendMessage(args[1] + " wurde gesperrt!");
						deniedcfg.set("deniedwords", words);
						try {
							deniedcfg.save(deniedFile);
						} catch (IOException e) {
							e.printStackTrace();
						}
						return true;
					} else {
						p.sendMessage(FrameMainGet.getPrefix() + " §aPlease use §b/denied add (word) §aor §b/denied remove (word)");
					}
				} else {
					String text = CreateConfig.getConfig().getString("message.notactivated");
					text = text.replace('&', '§');
					p.sendMessage(FrameMainGet.getPrefix() + " " + text);
				}
			}
				
		}
		if(cmd.getName().equalsIgnoreCase("getitem")) {
			if(sender instanceof Player) {
				Player p = (Player) sender;
				if(p.hasPermission("frameapi.getitem")) {
					if(args.length == 0) {
						p.sendMessage("§cPlease use /getitem (Material) (DisplayName) (ItemLore) (unbreakable) (enchantment) (enchantmentlevel)");
					} else if(args.length == 6) {
					try {
						String[] lore = {args[2]};
						ItemStack mat = new ItemStack(Material.getMaterial(args[0].toUpperCase()));
						String name = args[1];
						ItemStack item2 = api.CreateItem(mat, name, Boolean.parseBoolean(args[3]),getEnchantment(args[4]), Integer.parseInt(args[5]), lore);
						p.getInventory().addItem(item2);
						return true;
					} catch (ArrayIndexOutOfBoundsException ex) {
						p.sendMessage("§cPlease use /getitem (Material) (DisplayName) (ItemLore) (unbreakable) (enchantment) (enchantmentlevel)");
					}
					} else if(args.length == 2) {
						int x = Integer.parseInt(args[1]);
						ItemStack item = new ItemStack(Material.getMaterial(args[0]));
						item.setAmount(x);
						p.getInventory().addItem(new ItemStack(item));
					} else {
						p.sendMessage("§cPlease use /getitem (Material) (DisplayName) (ItemLore) (unbreakable) (enchantment) (enchantmentlevel)");
					}
					//getitem test 1 test smite 1 true
					// 0       1   2  3    4    5  6
				} else {
					p.sendMessage(FrameMainGet.getPrefix() + " " + FrameMainGet.getNoPerm());
					return true;
				}
			}
		
		}
		if(cmd.getName().equalsIgnoreCase("setwarp")) {
			if(sender instanceof Player) {
				Player p= (Player) sender;
				if(p.hasPermission("frameapi.setwarp")) {
					API.Warp.setWarpLocation(args[0], p.getLocation());
					p.sendMessage(FrameMainGet.getPrefix() + " " + args[0] + " §aWarp has been set");
					try {
						cfgwarp.load(warpfile);
					} catch (IOException | InvalidConfigurationException e) {
						e.printStackTrace();
					}
				}
				return true;
			} else {
			    sender.sendMessage("§cYou must be a Player!");
            }
		}
		if(cmd.getName().equalsIgnoreCase("warp")) {
			if(sender instanceof Player) {
				Player p = ( Player) sender;
				if(FrameMain.getInstance().getConfig().getBoolean("Use.Warps")) {
					if(p.hasPermission("frameapi.warp")) {
						if(args.length == 1) {
							ConfigurationSection cs = cfgwarp.getConfigurationSection("warps");
							try {
								for(String s : cs.getKeys(false)) {
									if(s.equalsIgnoreCase(args[0])) {
										if(cfgwarp.getBoolean("warps." + s + ".boolean")) {
											try {
												Location loc =API.Warp.getWarpLocation(args[0]);
												String name = CreateConfig.getConfig().getString("Warp.Warp");
												name = name.replace("[warp]",args[0]);
												name = name.replace('&', '§');
												p.teleport(loc);
												p.sendMessage(name);
											} catch (NullPointerException e) {
												String message = CreateConfig.getConfig().getString("Warp.warpdoesntexist");
												message = message.replace('&', '§');
												p.sendMessage(message);
											}
										} else {
											String message = CreateConfig.getConfig().getString("Warp.warpdoesntexist");
											message = message.replace('&', '§');
											p.sendMessage(message);
										}
									}
								}
							} catch (NullPointerException e) {
								p.sendMessage("§cNo Warps Found!");
							}
						} else {
							p.sendMessage("§cPlease use §a/warp (warpname)");
					}
					} else {
						p.sendMessage(FrameMainGet.getPrefix() + " " + FrameMainGet.getNoPerm());
						return true;
					}
				} else {
				String text = CreateConfig.getConfig().getString("message.notactivated");
				text = text.replace('&', '§');
				p.sendMessage(FrameMainGet.getPrefix() + " " + text);
				}
			} else {
				sender.sendMessage("§cThis Command is only for Players");
			}
		}
		if(cmd.getName().equalsIgnoreCase("removewarp")) {
			if(sender.hasPermission("frameapi.warpremove")) {
				ConfigurationSection cs = cfgwarp.getConfigurationSection("warps");
				for(String s : cs.getKeys(false)) {
					if(s.equalsIgnoreCase(args[0])) {
						cfgwarp.set("warps."+ s + ".boolean", false);
						String message = CreateConfig.getConfig().getString("Warp.warpremoved");
						message = message.replace("[Prefix]", FrameMainGet.getPrefix());
						message = message.replace("[Warpname]", args[0]);
						message = message.replace('&', '§');
						sender.sendMessage(message);
						try {
							cfgwarp.save(warpfile);
						} catch (IOException e) {
							e.printStackTrace();
						}
					} else {
						try {
							if(s.equalsIgnoreCase(args[0])) {
								
							}
						} catch (IllegalArgumentException e) {
							sender.sendMessage("§c" + args[0] + " §ais not in warps.yml");
						}
					}
				}
			} else {
				sender.sendMessage(FrameMainGet.getPrefix() + " " + FrameMainGet.getNoPerm());
				return true;
			}
		}
		if(cmd.getName().equalsIgnoreCase("heal")) {
			if(sender instanceof Player) {
				Player p = (Player) sender;
				if(p.hasPermission("frameapi.heal")) {
					if(args.length == 0) {
						api.HealPlayer(p, 20);
						String text1 = CreateConfig.getConfig().getString("message.heal");
						text1 = text1.replace('&', '§');
						p.sendMessage(text1);
				
					} else if(args.length == 1) {
						try {
							Player target = Bukkit.getPlayer(args[0]);
							api.HealPlayer(target, 20);
							String text1 = CreateConfig.getConfig().getString("message.targetheal");
							text1 = text1.replace('&', '§');
							text1 = text1.replace("[player]", p.getDisplayName());
							String text2 = CreateConfig.getConfig().getString("message.targethealplayer");
							text2 = text2.replace('&', '§');
							text2 = text2.replace("[target]", target.getDisplayName());
							target.sendMessage(text1);
							p.sendMessage(text2);
						} catch (ArrayIndexOutOfBoundsException e) {
							p.sendMessage(args[0] + " §cis not a Player");
						}
					} else {
						p.sendMessage("§aPlease use §b/heal §aor §b/heal (player)");
					}
				} else {
					p.sendMessage(FrameMainGet.getPrefix() + " " + FrameMainGet.getNoPerm());
					return true;
				}
			} else {
			    sender.sendMessage("§cYou must be a Player");
            }
		}
		if(cmd.getName().equalsIgnoreCase("kits")) {
			if(sender instanceof Player) {
				Player p = (Player) sender;
				if(args.length == 1) {
				    String name = args[0];
				    if(p.hasPermission("frameapi." + name)) {
                        new GetKits().loadKits(name, p);
                    }
                }
			}
		}
		if(cmd.getName().equalsIgnoreCase("msg")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                String message = "";
                if (p.hasPermission("frameapi.msg")) {
                    if (args.length >= 2) {
                        Player target = Bukkit.getPlayer(args[0]);
                        if (target != null) {
                            for (int i = 1; i < args.length; i++) {
                                message = message + args[i] + " ";
                            }
                            p.sendMessage("§cme §r-> §a " + target.getName() + " §f-> " + message);
                            target.sendMessage("§c" + p.getName() + " §r-> §cme  §f-> " + message);
                            message = "";
                        }
                    }
                } else {
                }
            } else {
                sender.sendMessage("§cYou must be a Player");
            }
        }
		if(cmd.getName().equalsIgnoreCase("createpay")) {
			if(cfg.getBoolean("MYSQL.Boolean")) {
				if(args.length == 2) {
					if(sender instanceof Player) {
						Player p = (Player) sender;
						Player target = Bukkit.getPlayer(args[0]);
						if(target != null) {
							int amount = Integer.parseInt(args[1]);
							api.createPay(target, amount, p);
							String message = api.getCustomConfig().getString("Bill.Create");
							message = message.replace("[Target]", target.getName());
							message = message.replace('&', '§');
							p.sendMessage(message);
							if(api.getPays(target) != null) {
								FrameMain.getInstance().pays.add(target.getName());
								String message2 = api.getCustomConfig().getString("Bill.Get");
								message2 = message2.replace("[Target]", p.getName());
								message2 = message2.replace("[Amount]", String.valueOf(amount));
								message2 = message2.replace('&', '§');
								target.sendMessage("§aYou get a Pay use /paythebill to sale it of §b" +amount);
							}
						} else {
							OfflinePlayer offline = Bukkit.getOfflinePlayer(args[0]);
							int amount = Integer.parseInt(args[1]);
							api.createPay(offline, amount, p);
							String message = api.getCustomConfig().getString("Bill.Create");
							message = message.replace("[Target]", args[0]);
							message = message.replace('&', '§');
							p.sendMessage(message);
						}
					} else {
					    sender.sendMessage("§cYou must be a Player!");
                    }
				}
			}
		}
		if(cmd.getName().equalsIgnoreCase("paythebill")) {
			if(cfg.getBoolean("MYSQL.Boolean")) {
                if (sender instanceof Player) {
                    Player p = (Player) sender;
                    if (FrameMain.getInstance().pays.contains(p.getName())) {
                        if (api.getPays(p) != null) {
                            int amounts = api.getPays(p);
                            if (eco.getMoney(p) < amounts) {
                                double money = amounts - eco.getMoney(p);
                                p.sendMessage("§cNot enought Money. §aIf you have Money in your Bank please withdraw it and Pay it!");
                                p.sendMessage("§cYou need = §a" + money);
                            } else {
                                for (Player from : Bukkit.getOnlinePlayers()) {
                                    if (from != null) {
                                        if (api.getPaysFrom(from) != null) {
                                            if (from != p) {
                                                eco.addMoney(from, amounts);
                                                String message2 = api.getCustomConfig().getString("Bill.Pay");
												message2 = ReplaceCharConfig.replaceObjectWithData(message2,"[Target]",from.getName());
												message2 = ReplaceCharConfig.replaceObjectWithData(message2,"[Amount]",String.valueOf(amounts));
												message2 = ReplaceCharConfig.replaceParagraph(message2);
                                                from.sendMessage("§aYou get the Pay from §b" + p.getName() + " §ain Amount of §b" + amounts);
                                                p.sendMessage(message2);
                                                api.getPaysDelete(p, from);
                                                eco.removeMoney(p, amounts);

                                            } else {
												for (OfflinePlayer offline : Bukkit.getOfflinePlayers()) {
													api.getPaysTo(p);
													if(api.getPaysTo(p).contains(offline)) {
														String message2 = api.getCustomConfig().getString("Bill.Pay");
														message2 = ReplaceCharConfig.replaceObjectWithData(message2, "[Target]", offline.getName());
														message2 = ReplaceCharConfig.replaceObjectWithData(message2, "[Amount]", String.valueOf(amounts));
														message2 = ReplaceCharConfig.replaceParagraph(message2);
														eco.addMoney(offline, amounts);
														p.sendMessage(message2);
														api.getPaysDelete(p, offline);
														eco.removeMoney(p, amounts);
													}
												}
											}
                                        }
                                    } else {
                                        for (OfflinePlayer offline : Bukkit.getOfflinePlayers()) {
                                        	api.getPaysTo(p);
                                        	if(api.getPaysTo(p).contains(offline)) {
												String message2 = api.getCustomConfig().getString("Bill.Pay");
												message2 = ReplaceCharConfig.replaceObjectWithData(message2, "[Target]", offline.getName());
												message2 = ReplaceCharConfig.replaceObjectWithData(message2, "[Amount]", String.valueOf(amounts));
												message2 = ReplaceCharConfig.replaceParagraph(message2);
												eco.addMoney(offline, amounts);
												p.sendMessage(message2);
												api.getPaysDelete(p, offline);
												eco.removeMoney(p, amounts);
											}
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        p.sendMessage("§cYou have no Bill to Pay!");
                    }
                }
            }
		}
		if(cmd.getName().equalsIgnoreCase("god")) {
			if(sender instanceof Player) {
				Player p = (Player) sender;
				api.godMode(p);
				p.sendMessage("§cGodMode Changed");
			}
		}
		if(cmd.getName().equalsIgnoreCase("gm")) {
			if(sender instanceof Player) {
				Player player = (Player) sender;
				if (args.length == 1) {
					player.setGameMode(api.getGamemodeInInt(Integer.parseInt(args[0])));
				} else if (args.length == 2) {
					Player target = Bukkit.getPlayer(args[1]);
					if (target != null) {
						target.setGameMode(api.getGamemodeInInt(Integer.parseInt(args[1])));
					}
				}
			}
		}
		return false;
	}
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		if(command.getName().equalsIgnoreCase("frameapi")) {

			if(args.length == 1) {
                ArrayList<String> cmdframeapi = new ArrayList<String>();
                cmdframeapi.add("startbudget");
                cmdframeapi.add("reload");
                cmdframeapi.add("getmysql");
                cmdframeapi.add("help");
                cmdframeapi.add("info");
                cmdframeapi.add("time");
			    if(!sender.hasPermission("frameapi.startbudget")) {
                    cmdframeapi.remove("startbudget");
                }
			    if(!sender.hasPermission("frameapi.reload")) {
                    cmdframeapi.remove("reload");
                }
			    if(!sender.hasPermission("frameapi.mysql")) {
                    cmdframeapi.remove("getmysql");
                }
			    if(!sender.hasPermission("frameapi.help")) {
                    cmdframeapi.remove("help");
			    }
			    if(!sender.hasPermission("frameapi.info")) {
                    cmdframeapi.remove("info");
                }
			    if(!sender.hasPermission("frameapi.time")) {
                    cmdframeapi.remove("time");

                }
                return cmdframeapi;
			}
			else if(args[0].equalsIgnoreCase("startbudget")) {
				ArrayList<String> money = new ArrayList<String>();
				money.add("on");
				money.add("off");
				money.add("set");
				return money;
			}
		} else if(command.getName().equalsIgnoreCase("warp")) {
			if(args.length == 1) {
				if(sender.hasPermission("frameapi.warp")) {
					ArrayList<String> warps = new ArrayList<String>();
					ConfigurationSection cs = cfgwarp.getConfigurationSection("warps");
					if(!warpfile.exists()) {
						try {
							warpfile.mkdir();
							sender.sendMessage("§4No Warps found");
							return warps;
						} catch (NullPointerException e) {
							sender.sendMessage("§4No Warps found");
						}
					} else {
						for(String s : cs.getKeys(false)) {
							if(cfgwarp.getBoolean("warps." + s + ".boolean")) {
								try {
									if(cfgwarp.getBoolean("warps." + s + ".boolean")) {
									warps.add(s);
									}
								} catch (NullPointerException e) {
									String message = CreateConfig.getConfig().getString("Warp.warpdoesntexist");
									message = message.replace('&', '§');
									sender.sendMessage(message);
								}
							}
						}
						return warps;
					}
				}
			}
		} else if(command.getName().equalsIgnoreCase("removewarp")) {
			if(args.length == 1) {
				if(sender.hasPermission("frameapi.warpremove")) {
					ArrayList<String> warps = new ArrayList<String>();
					ConfigurationSection cs = cfgwarp.getConfigurationSection("warps");
					for(String s : cs.getKeys(false)) {
						if(cfgwarp.getBoolean("warps." + s + ".boolean")) {
							try {
								if(cfgwarp.getBoolean("warps." + s + ".boolean")) {
									warps.add(s);
								}
							} catch (NullPointerException e) {
								String message = CreateConfig.getConfig().getString("Warp.warpdoesntexist");
								message = message.replace('&', '§');
								sender.sendMessage(message);
							}
						}
					}
					return warps;
				}
			}
		} else if(command.getName().equalsIgnoreCase("kits")) {
			if(args.length == 1) {
				ArrayList<String> list = new ArrayList<String>();
				ConfigurationSection cs = GetKits.getCustomConfig().getConfigurationSection("Items");
				for(String s : cs.getKeys(false)) {
					if(sender.hasPermission("frameapi." + s)) {
					    list.add(s);
                    }
				}
				return list;
			}
		}
		return null;
	}
	private static void sendHelp(Player p) {
		try {
			if(p.hasPermission("frameapi.help")) {
				p.sendMessage("§6Please use  /frameapi time || /frameapi getmysql");
				p.sendMessage("§6or use /frameapi startbudget || /frameapi info ");
				p.sendMessage("§6or use /frameapi help || /frameapi reload");
				p.sendMessage("§6or use /back");
				p.sendMessage("§6or use /spawnmob (entitytype)|/spawnmob (entitytype) count");
				p.sendMessage("§6 use /getitem itemname name lore unbreakable true/false");
				p.sendMessage("§6 use /heal or /heal (PlayerName)");
				p.sendMessage("§6 use /denied add (word)|/denied remove (word)");
			}
		} catch (Exception e) {
			p.sendMessage("§6you don't have Permission to use /frameapi help");
		}
	}
	private static SimpleDateFormat date2=new SimpleDateFormat("HH:mm:ss");
	private static String Uhrzeit2=date2.format(new Date());
	private static String getDate() {
		return Uhrzeit2;

	}
	@SuppressWarnings("deprecation")
	private static Enchantment getEnchantment(String enchString) {
        // Clean up string - make lowercase and strip space/dash/underscore
        enchString = enchString.toLowerCase().replaceAll("[ _-]", "");
 
        // Set up aliases (this could probably be done outside the function so
        // we only do it once (eg. in a support class init or read from a file)
        Map <String, String> aliases = new HashMap<String, String>();
        aliases.put("aspectfire", "fireaspect");
        aliases.put("sharpness", "damageall");
        aliases.put("smite", "damageundead");
        aliases.put("punch", "arrowknockback");
        aliases.put("looting", "lootbonusmobs");
        aliases.put("fortune", "lootbonusblocks");
        aliases.put("baneofarthropods", "damageundead");
        aliases.put("power", "arrowdamage");
        aliases.put("flame", "arrowfire");
        aliases.put("infinity", "arrowinfinite");
        aliases.put("unbreaking", "durability");
        aliases.put("efficiency", "digspeed");
        aliases.put("smite", "damageundead");
 
        // If an alias exists, use it
        String alias = aliases.get(enchString);
        if (alias != null)
            enchString = alias;
 
        // Loop through all enchantments and match (case insensitive and ignoring space,
        // underscore and dashes
        for (Enchantment value : Enchantment.values()) {
            if (enchString.equalsIgnoreCase(value.getName().replaceAll("[ _-]", ""))) {
                return value;
            }
        }
       
        return null; // nothing found.
    }

}
