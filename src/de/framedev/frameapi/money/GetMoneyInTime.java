/**
 * Dies ist ein Plugin von FrameDev
 * Bitte nichts§ndern, @Copyright by FrameDev 
 */
package de.framedev.frameapi.money;

import de.framedev.frameapi.api.API;
import de.framedev.frameapi.api.Money;
import de.framedev.frameapi.main.FrameMain;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * @author Darryl
 *
 */
public class GetMoneyInTime {
	private Money eco = new Money();
	private FrameMain plugin;
	public GetMoneyInTime(FrameMain plugin) {
		this.plugin = plugin;
	}
	/**
	 * GetMoney for OnlinePlayers in Time
	 */
	public void getMoneyinTime() {
		
		double money = plugin.getConfig().getDouble("Money.Amount");
		if(plugin.getConfig().getBoolean("Money.Boolean")){
			int time = plugin.getConfig().getInt("Money.Time");
			new BukkitRunnable() {
				
				@Override
				public void run() {
					Bukkit.getOnlinePlayers().forEach(current ->{
							if(API.CreateConfig.getConfig().getBoolean("Payload.Boolean")) {
								try {
									String message = API.CreateConfig.getConfig().getString("Payload.Text");
									message = message.replace("[Money]", String.valueOf(money));
									message = message.replace('&', '§');
									current.sendMessage(message);
									Bukkit.getConsoleSender().sendMessage("§aMoney was giving to the Players");
									eco.addMoney(current, money);
									current.playSound(current.getLocation(), Sound.LEVEL_UP, 1, 1);
								} catch (NullPointerException e) {
									if(current.isOp()) {
										current.sendMessage("§cThere was a mistake in forgiving his money");
									}
								}
							} else {
								eco.addMoney(current, money);
							}
					});
					
				}
			}.runTaskTimer(plugin, 0, time*20);
		}
	}
	/**
	 * GetMoney for OfflinePlayers in Time
	 */
	public void getOfflinePlayerMoney() {
		
		double money = plugin.getConfig().getDouble("OfflinePlayers.Money.Amount");
		if(plugin.getConfig().getBoolean("OfflinePlayers.Money.Boolean")){
			int time = plugin.getConfig().getInt("OfflinePlayers.Money.Time");
			try {
			new BukkitRunnable() {
				@Override
				public void run() {
					if(plugin.getConfig().getBoolean("MYSQL.Boolean")) {
					for(OfflinePlayer player : FrameMain.getInstance().getPlayers()) {
							if(player.isOnline() == false) {
								if(API.CreateConfig.getConfig().getBoolean("Payload.Boolean")) {
									try {
										String message = API.CreateConfig.getConfig().getString("Payload.Text");
										message = message.replace("[Money]", String.valueOf(money));
										message = message.replace('&', '§');
										Bukkit.getConsoleSender().sendMessage("§aMoney was giving to the Players");
										eco.addMoney(player, money);
									} catch (NullPointerException e) {
										if(player.isOp()) {
										}
									}
								} else {
									eco.addMoney(player, money);
								}
						}
					}
				}
				}
			}.runTaskTimer(plugin, 0, time*20);
			} catch (Exception e) {
				Bukkit.getConsoleSender().sendMessage("§cNo Offline Players");
			}
		}
	}
}
