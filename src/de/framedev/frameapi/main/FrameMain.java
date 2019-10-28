/**
 * Dies ist ein Plugin von FrameDev
 * Bitte nichts §ndern, @Copyright by FrameDev 
 */
package de.framedev.frameapi.main;

import de.framedev.frameapi.api.API;
import de.framedev.frameapi.api.Money;
import de.framedev.frameapi.kits.GetKits;
import de.framedev.frameapi.listeners.JoinListener;
import de.framedev.frameapi.listeners.LeaveListener;
import de.framedev.frameapi.money.GetMoneyInTime;
import de.framedev.frameapi.money.MoneyBankSigns;
import de.framedev.frameapi.money.MoneySigns;
import de.framedev.frameapi.mysql.IsTableExist;
import de.framedev.frameapi.mysql.MYSQL;
import de.framedev.frameapi.pets.Pets;
import de.framedev.frameapi.utils.Config;
import de.framedev.frameapi.utils.Lags;
import de.framedev.frameapi.warps.WarpSigns;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerListHeaderFooter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * @author Darryl
 *
 */
public class FrameMain extends JavaPlugin implements Listener {

	private static GetKits kit = new GetKits();
	private static API api = new API();
	public static Money eco = new Money();
	private static Pets pet = new Pets();
	private static FrameMain mi;
	private GetMoneyInTime timeMoney = new GetMoneyInTime(this);
	public ArrayList<String> pays = new ArrayList<String>();
	@Override
	public void onEnable() {
		mi = this;
		Config.loadConfig();
		Config.updateConfig();
        new BukkitRunnable() {
            @Override
            public void run() {
                MYSQL.close();
            }
        }.runTaskTimer(FrameMain.getInstance(),0,500);
		if(getConfig().getString("language").equalsIgnoreCase("en_EN")) {
            //Register Pets Class
            pet.getClass();

            //Create Config = messages.yml
            api.createCustomConfig();

            //Start the Timer for the NoNight Table
            api.NoNight();

            //Sends Messages in Console
            api.init();

            //if get the Boolean it sends a Messages for each Player
            api.sendMessageofReload();

            //Create .yml File = kits
            kit.createCustomConfig();

            //Starts the Animation

            startAnimation();
            if (this.getConfig().getBoolean("MYSQL.Boolean")) {
                timeMoney.getOfflinePlayerMoney();
                checkPlayerAccount();
            } else {
                Bukkit.getConsoleSender().sendMessage("§cNo MySQL not Activated, §aFuctions §care Disabled");
            }
            api.onUpdate(FrameMainGet.getPrefix() + " §b4.1.6 = §aBug Fixes");
            timeMoney.getMoneyinTime();
            try {
                Bukkit.getConsoleSender().sendMessage(getPlayerMoneys() + "");
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (this.getConfig().getBoolean("MYSQL.Boolean")) {
                MYSQL.connect();
            } else {
                Bukkit.getConsoleSender().sendMessage("§cNo MySQL not Activated, §aFuctions §care Disabled");
            }
        } else if(getConfig().getString("language").equalsIgnoreCase("de_DE")) {
            //Register Pets Class
            pet.getClass();

            //Create Config = messages.yml
            api.createCustomConfig();

            //Load Config.yml
            Config.loadConfig();
            Config.updateConfig();

            //Start the Timer for the NoNight Table
            api.NoNight();

            //Sends Messages in Console
            api.init();

            //if get the Boolean it sends a Messages for each Player
            api.sendMessageofReload();

            //Create .yml File = kits
            kit.createCustomConfig();

            //Starts the Animation

            startAnimation();
            if (this.getConfig().getBoolean("MYSQL.Boolean")) {
                timeMoney.getOfflinePlayerMoney();
                checkPlayerAccount();
            } else {
                Bukkit.getConsoleSender().sendMessage("§cMYSQL ist nicht aktiviert");
            }
            api.onUpdate(FrameMainGet.getPrefix() + " §b4.1.6 = §aBug Fixes");
            timeMoney.getMoneyinTime();
            try {
                Bukkit.getConsoleSender().sendMessage(getPlayerMoneys() + "");
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (this.getConfig().getBoolean("MYSQL.Boolean")) {
                MYSQL.connect();
            } else {
                Bukkit.getConsoleSender().sendMessage("§cMYSQL ist nicht aktiviert");
            }
        } else {
		    Bukkit.getConsoleSender().sendMessage(FrameMainGet.getPrefix() + " §4Please use en_EN or de_DE!!!");
        }
		
	}
	public static FrameMain getInstance() {
		return mi;
		
	}
	ArrayList<Integer> lol = new ArrayList<Integer>();
	public ArrayList<Integer> getPlayerMoneys() throws SQLException {
		for(Player p : Bukkit.getOnlinePlayers()) {
			MYSQL.connect();
			if(IsTableExist.isExist("pays")) {
				Statement statement = MYSQL.getConnection().createStatement();
				ResultSet res = statement.executeQuery("SELECT * FROM money WHERE PlayerName = '" + p.getName() + "';");
				if(res.next()) {
					lol.add(res.getInt("balance_money"));
					return lol;
				} else {
					return lol;
				}
			} else {
				String sql = "CREATE TABLE IF NOT EXISTS pays(PlayerTo TEXT(11),payAmount INT, PlayerFrom TEXT);";
			    try {
			     PreparedStatement stmt = (PreparedStatement) MYSQL.getConnection().prepareStatement(sql);
			     	stmt.executeUpdate();
			    } catch (SQLException e1) {
			    	e1.printStackTrace();
			    }
			}
		}
		return lol;
		
	}
	public int j = 0;
	/**
	 * Check the Player Account in MYSQL for Pays
	 */
	public void checkPlayerAccount() {
		
		new BukkitRunnable() {
			@Override
			public void run() {
				for(Player online : Bukkit.getOnlinePlayers()) {
					MYSQL.connect();
					if(IsTableExist.isExist(""))
					try {
					Statement statement = MYSQL.getConnection().createStatement();
					ResultSet res = statement.executeQuery("SELECT * FROM pays WHERE PlayerTo = '" + online.getName() + "';");
					if(res.next()) {
						if(res.getString("PlayerTo") != null) {
							int amount = api.getPays(online);
								j = amount;
							
								if(j == 0) {
							
								} else {
									online.sendMessage("§aYou get a Pay use /paythebill to sale it of §b" + j);
									pays.add(online.getName());
									j = 0;
								}
						}
					}
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				
			}
		}.runTaskLater(this, 120);
	}
	@Override
	public void onDisable() {
		for(String name : Pets.Pet.keySet()) {
			Pets.Pet.get(name).remove();
		}
		MYSQL.close();
		Bukkit.getConsoleSender().sendMessage(FrameMainGet.getPrefix() + " §aDeactivated!");
		api.onUpdate(FrameMainGet.getPrefix() + " §b4.1.6 = §aBug Fixes");
	}
	/**
	 * Register The Listeners
	 * @param plugin this
	 */
	public void registerListener(FrameMain plugin) {
		
		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(new Money(), plugin);
		pm.registerEvents(new API(), plugin);
		pm.registerEvents(new MoneySigns(), plugin);
		pm.registerEvents(new WarpSigns(), plugin);
		if(this.getConfig().getBoolean("MYSQL.Boolean")) {
			pm.registerEvents(this, plugin);
			pm.registerEvents(new LeaveListener(), this);
			pm.registerEvents(new JoinListener(), this);
			pm.registerEvents(new MoneyBankSigns(), plugin);
		} else {
			Bukkit.getConsoleSender().sendMessage("§cNo MySQL Found, Fuctions are Disabled");
		}
	}
	private static String noperm;
	private static String prefix;
	/**
	 * @author Darryl
	 *Get Prefix
	 */
	public static class FrameMainGet {
		/**
	 	* @return the prefix
	 	*/
		public static String getPrefix() {
			prefix = FrameMain.getInstance().getConfig().getString("Prefix");
			prefix = prefix.replace('&', '§');
			return prefix;
		}
		/**
		 * @param prefix the prefix to set
		 */
		public static void setPrefix(String prefix) {
			prefix = FrameMain.getInstance().getConfig().getString("Prefix");
			prefix = prefix.replace('&', '§');
			FrameMain.getInstance();
			FrameMain.prefix = prefix;
		}
		public static void setNoPerm(String noperm) {
			noperm = FrameMain.getInstance().getConfig().getString("NoPerm");
			noperm = noperm.replace('&', '§');
			FrameMain.getInstance();
			FrameMain.noperm = noperm;
		}
		public static String getNoPerm() {
			noperm = FrameMain.getInstance().getConfig().getString("NoPerm");
			noperm = noperm.replace('&', '§');
			return noperm;
		}
	}
	private String namethis = "";
	public String getNameThis() {
		namethis = this.getConfig().getString("Tablist.Header");
		return namethis;
	}
	public void setname(String namethis) {
		namethis = this.getConfig().getString("Tablist.Header");
		this.namethis = namethis;
	}
	public String[] animation = new String[] {
			"§b§l" +getNameThis()+ "",
			"§3§l" +getNameThis()+ "",
			"§9§l" +getNameThis()+ "",
			"§6§l" + getNameThis()+ "",
			"§7§l"+getNameThis()+"",
			"§1§l"+getNameThis()+"",
			"§f§l"+getNameThis()+"",
			"§e§l"+getNameThis()+"",
			"§d§l"+getNameThis()+""
	};
	public Integer AnimationCount;
	/**
	 * Starts the Animation from the TabList only the Header
	 */
	public void startAnimation() {
		
		AnimationCount = 0;
		Bukkit.getScheduler().runTaskTimer(this, new Runnable() {
			
			@Override
			public void run() {
				Bukkit.getOnlinePlayers().forEach(current -> {
				
			});
			AnimationCount++;
			if(AnimationCount == animation.length) {
				AnimationCount = 0;
			}
				}
		}, 0, 20);
	}
	@SuppressWarnings({ "deprecation"})
	public ArrayList<OfflinePlayer> getPlayers() {
		ArrayList<OfflinePlayer> playertop = new ArrayList<OfflinePlayer>();
		if(getConfig().getBoolean("MYSQL.Boolean")) {
			MYSQL.connect();
			try {
				Statement statement = MYSQL.getConnection().createStatement();
				ResultSet res = statement.executeQuery("SELECT * FROM offline WHERE boolean = 'yes';");
				int spalten = res.getMetaData().getColumnCount();
	            
	            while (res.next()) {
	                String[] str = new String[8];
	                for (int k = 1; k <=spalten; k++) {
	                    str[k - 1] = res.getString(k);
	                    OfflinePlayer p = Bukkit.getOfflinePlayer(res.getString(k));
	                	if(p != null) {
	                		if(playertop.contains(p)) {
	                		} else {
	                			playertop.add(p);
	                		}
	                	}
	                }
	            }
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return playertop;
	}
	@EventHandler
	public void onColorChatMessage(AsyncPlayerChatEvent e) {
		String msg = e.getMessage();
		msg = msg.replace('&', '§');
		e.setMessage(msg);
	}
	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(command.getName().equalsIgnoreCase("tps")){
            if(sender.hasPermission("frameapi.tps")){
                    if(args.length == 0){
                            double TPS = Lags.getTPS();
                            DecimalFormat TpsFormat = new DecimalFormat("#.##");
                           
                            if(TPS > 20){
                                    sender.sendMessage(ChatColor.DARK_GREEN+"TPS : " + TpsFormat.format(TPS));
                                    return true;
                            }
                           
                            if(TPS > 19){
                                    sender.sendMessage(ChatColor.GREEN+"TPS : " + TpsFormat.format(TPS));
                                    return true;
                            }
                           
                            if(TPS > 14){
                                    sender.sendMessage(ChatColor.YELLOW+"TPS : " + TpsFormat.format(TPS));
                                    return true;
                            }
                           
                            if(TPS > 9){
                                    sender.sendMessage(ChatColor.RED+"TPS : " + TpsFormat.format(TPS));
                                    return true;
                            }
                           
                            if(TPS < 9){
                                    sender.sendMessage(ChatColor.DARK_RED+"TPS : " + TpsFormat.format(TPS));
                                    return true;
                            }
                           
                    }
            }
            /*} else {
            	if(command.getName().equalsIgnoreCase("cleartps")) {
            		if(sender.hasPermission("frameapi.cleartps")) {
            			World world = Bukkit.getWorld("world");
            			for(Entity ent : world.getEntities()) {
            				if(ent instanceof LivingEntity) {
            					ent.remove();
            				}
            			}
            		}
            	}
            	*/
            }
		if(command.getName().equalsIgnoreCase("health")) {
        	if(sender instanceof Player) {
        		Player p = (Player) sender;
        		if(p.getHealth() == 6) {
        			p.resetMaxHealth();
        			p.setHealth(20);
        			p.setSaturation(20);
        			p.setFoodLevel(20);
        			p.sendTitle("§aAlle Herzen erstellt", "");
        			Bukkit.getScheduler().cancelTasks(this);
        			startAnimation();
        			api.onUpdate(FrameMainGet.getPrefix() + " §b3.0.0 Release §a= Bug Fixes, Permission Fixes");
        			timeMoney.getMoneyinTime();
        			checkPlayerAccount();
        		} else {
        			runTask(p);
        			p.setMaxHealth(6);
        			p.sendTitle("§aDrei Herzen erstellt", "");
        		}
        	}
        }
		if(command.getName().equalsIgnoreCase("sethealth")) {
			if(sender instanceof  Player) {
				Player p = (Player) sender;
				if(args.length == 1) {
					double x = Double.parseDouble(args[0]);
					p.setMaxHealth(x*2);
					return true;
				} else if(args.length == 2) {
					Player target = Bukkit.getPlayer(args[0]);
					double x = Double.parseDouble(args[1]);
					if (target != null) {
						target.setMaxHealth(x*2);
						p.sendMessage(args[0] + " §a sind seine herzen nun auf " + x);
						//sethealth TrAxMC 100
						//0			1		2
					} else {
						p.sendMessage(args[0] + " §aist nicht Online");
					}
				}
			}
		}
		return super.onCommand(sender, command, label, args);
		
	}
		public void runTask(Player p) {
			Bukkit.getScheduler().runTaskTimer(this, new Runnable() {
				
				@SuppressWarnings("deprecation")
				@Override
				public void run() {
					if(p.getLevel() == 3) {
						p.setMaxHealth(7);
						} else if(p.getLevel() == 6) {
						p.setMaxHealth(8);
						} else if(p.getLevel() == 8) {
						p.setMaxHealth(10);
						} else if(p.getLevel() == 10) {
						p.setMaxHealth(12);
						} else if(p.getLevel() == 12) {
						p.setMaxHealth(14);
						} else if(p.getLevel() == 14) {
						p.setMaxHealth(16);
						} else if(p.getLevel() == 16) {
						p.setMaxHealth(18);
						} else if(p.getLevel() == 18) {
						p.setMaxHealth(20);
						} else if(p.getLevel() == 20) {
						p.setMaxHealth(22);
						} else if(p.getLevel() == 22) {
						p.setMaxHealth(24);
						}
					
					}
				}, 0, 20);
		}

}
