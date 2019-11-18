/**
 * Dies ist ein Plugin von FrameDev
 * Bitte nichts §ndern, @Copyright by FrameDev 
 */
package de.framedev.frameapi.cmds;

import de.framedev.frameapi.api.money.Money;
import de.framedev.frameapi.main.FrameMain;
import de.framedev.frameapi.main.FrameMain.FrameMainGet;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * @author Darryl
 *
 */
public class MoneyBankCMD implements CommandExecutor {
	private Money eco = new Money();
	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			if(cmd.getName().equalsIgnoreCase("deposit")) {
				Player p = (Player) sender;
				if(p.hasPermission("frameapi.deposit")) {
					if(args.length !=0) {
						double x = Double.parseDouble(args[0]);
						if(args.length == 1) {
							if(eco.getMoney(p) < x) {
								p.sendMessage("§cNot enought Money!");
							} else {
								p.sendMessage("§aTransaction is in progress");
								new BukkitRunnable() {
									
									@Override
									public void run() {
										eco.AddMoneyFromBank(p, x);
										eco.removeMoney(p, x);
										p.sendMessage("§aYou have been successful added §b" + x + " §ato your Bank Account");
							
									}
								}.runTaskLater(FrameMain.getInstance(), 300);
							}
						}
					} else {
						p.sendMessage("§cPlease use §a/deposit (amount");
					}
				} else {
					p.sendMessage(FrameMainGet.getPrefix() + " " + FrameMainGet.getNoPerm());
				}
			} else if(cmd.getName().equalsIgnoreCase("withdraw")) {
				Player p = (Player) sender;
				if(p.hasPermission("frameapi.withdraw")) {
					if(args.length != 0) {
						double x = Double.parseDouble(args[0]);
						if(args.length == 1) {
							if(eco.getMoneyFromBankMySQL(p) < x) {
								p.sendMessage("§cNot enought Money in your Bank Account");
							} else {
								p.sendMessage("§aTransaction is in progress");
								new BukkitRunnable() {
									
									@Override
									public void run() {
										eco.addMoney(p, x);
										eco.RemoveMoneyFromBank(p, x);
										p.sendMessage("§aYou have been successful removed §b" + x + " §afrom your Bank Account");
							
									}
								}.runTaskLater(FrameMain.getInstance(), 300);
							}
						} else {
							p.sendMessage("§cPlease use §a/withdraw (amount");
						}
					} else {
						p.sendMessage("§cPlease use §a/withdraw (amount");
					}
				} else {
					p.sendMessage(FrameMainGet.getPrefix() + " " + FrameMainGet.getNoPerm());
				}
			} else if(cmd.getName().equalsIgnoreCase("transfer")) {
				Player p = (Player) sender;
				if(p.hasPermission("frameapi.transfer")) {
					if(args.length !=0) {
						Player target = Bukkit.getPlayer(args[0]);
						double x = Double.parseDouble(args[1]);
						if(args.length == 2) {
							if(eco.getMoneyFromBankMySQL(p) < x) {
								p.sendMessage("§cNot enought Money in your Bank Account");
							} else {
								p.sendMessage("§aTransaction is in progress");
								new BukkitRunnable() {
					
									@Override
									public void run() {
										if(target != null) {
											eco.AddMoneyFromBank(target, x);
											eco.RemoveMoneyFromBank(p, x);
											target.sendMessage("§aYou got §b" + x + " §afrom §b" + p.getName() + " §ato your Bank Account");
											p.sendMessage("§aYou have been successful transfered §b" + x + " §ato §b" + target.getName() + "'s §aBank Account");
										} else {
											OfflinePlayer offline = Bukkit.getOfflinePlayer(args[0]);
											eco.AddMoneyFromBank(offline, x);
											eco.RemoveMoneyFromBank(p, x);
											p.sendMessage("§aYou have been successful transfered §b" + x + " §ato §b" + offline.getName() + "'s §aBank Account");
										}
									}
								}.runTaskLater(FrameMain.getInstance(), 300);
							}
						} else {
							p.sendMessage("§cPlease use §a/transfer (target) (amount");
						}
					} else {
						p.sendMessage("§cPlease use §a/transfer (target) (amount");
					}
				} else {
					p.sendMessage(FrameMainGet.getPrefix() + " " + FrameMainGet.getNoPerm());
				}
			} else if(cmd.getName().equalsIgnoreCase("setbank")) {
				if(args.length != 0) {
				if(args.length == 1) {
					Player p = (Player) sender;
					if(p.hasPermission("frameapi.setbank")) {
						double x = Double.parseDouble(args[0]);
						eco.SaveMoneyInBank(p, x);
						p.sendMessage("§aYour Bank Account have been Succssesfully set to §b" + x);
					} else {
						p.sendMessage(FrameMainGet.getPrefix() + " " + FrameMainGet.getNoPerm());
					}
				} else if(args.length == 2) {
					Player p = (Player) sender;
					if(p.hasPermission("frameapi.setbank.other")) {
						double x = Double.parseDouble(args[1]);
						Player target = Bukkit.getPlayer(args[0]);
						if(target != null) {
							eco.SaveMoneyInBank(target, x);
							if(FrameMain.getInstance().getConfig().getString("language").equalsIgnoreCase("en_EN")) {
								p.sendMessage("§aYour have been successful set the Bank Account from §b" + target.getName() + "§a to §b" + x);
								target.sendMessage("§aYour Bank account have been set to §b" + x);
							} else if(FrameMain.getInstance().getConfig().getString("language").equalsIgnoreCase("de_DE")) {
								p.sendMessage("§aYour have been successful set the Bank Account from §b" + target.getName() + "§a to §b" + x);
								target.sendMessage("§aYour Bank account have been set to §b" + x);
							}
						} else {
							OfflinePlayer offline = Bukkit.getOfflinePlayer(args[0]);
							eco.SaveMoneyInBank(offline, x);
							p.sendMessage("§aYour have been successful set the Bank Account from §b" + offline.getName() + "§a to §b" + x);
						}
					} else {
						p.sendMessage(FrameMainGet.getPrefix() + " " + FrameMainGet.getNoPerm());
					}
					} else {
						Player p = (Player) sender;
						p.sendMessage("§cPlease use §a/setbank (amount) §cor §a/setbank (target) (amount)");
					}
				}
			} else if(cmd.getName().equalsIgnoreCase("bankbalance")) {
				if(args.length == 0) {
					Player p = (Player) sender;
					if(p.hasPermission("frameapi.bankbalance")) {
						double x = eco.getMoneyFromBankMySQL(p);
						p.sendMessage("§aYour Bank Account have §b" + x);
					} else {
						p.sendMessage(FrameMainGet.getPrefix() + " " + FrameMainGet.getNoPerm());
					}
				} else if(args.length == 1) {
					Player target = Bukkit.getPlayer(args[0]);
					Player p = (Player) sender;
					if(p.hasPermission("frameapi.bankbalance.other")) {
						if(target != null) {
							double x = eco.getMoneyFromBankMySQL(target);
							p.sendMessage("§aThe Bank Account from §b" + target.getName() + " §a is §b" + x);
						} else {
							OfflinePlayer offline = Bukkit.getOfflinePlayer(args[0]);
							double x = eco.getMoneyFromBankMySQL(offline);
							p.sendMessage("§aThe Bank Account from §b" + offline.getName() + "§a is §b" + x);
						}
					} else {
						p.sendMessage(FrameMainGet.getPrefix() + " " + FrameMainGet.getNoPerm());
					}
				} else {
					Player p = (Player) sender;
					p.sendMessage("§cPlease use §a/bankbalance §cor §a/bankbalance (target)");
				}
			}
		}
		return false;
	}

}
