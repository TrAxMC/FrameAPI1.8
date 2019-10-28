/**
 * Dies ist ein Plugin von FrameDev
 * Bitte nichts §ndern, @Copyright by FrameDev 
 */
package de.framedev.frameapi.money;

import de.framedev.frameapi.api.Money;
import de.framedev.frameapi.main.FrameMain;
import de.framedev.frameapi.main.FrameMain.FrameMainGet;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * @author Darryl
 *
 */
public class MoneyBankSigns implements Listener {
	private static Money eco = new Money();
	@EventHandler
	public void onclickSignBalance(SignChangeEvent e) {
			if(e.getLine(0).equalsIgnoreCase("[balancebank]")) {
				if(e.getPlayer().hasPermission("frameapi.signs.create")) {
				e.setLine(0, "§bBalanceBank");
				} else {
					e.getPlayer().sendMessage(FrameMainGet.getPrefix() + " " + FrameMainGet.getNoPerm());
				}
			
		}
	}
	@EventHandler
	public void onClickBalance(PlayerInteractEvent e) {
			if(e.getAction() == Action.RIGHT_CLICK_BLOCK) {
				if(e.getClickedBlock().getState() instanceof Sign) {
					Sign s =(Sign) e.getClickedBlock().getState();
					if(s.getLine(0).equalsIgnoreCase("§bBalanceBank")) {
						if (e.getPlayer().hasPermission("frameapi.signs.use")) {
							if (FrameMain.getInstance().getConfig().getString("language").equalsIgnoreCase("en_EN")) {
								e.getPlayer().sendMessage("§aYou have §b" + eco.getMoneyFromBankMySQL(e.getPlayer()) + " §aon your Bank Account");
							} else if (FrameMain.getInstance().getConfig().getString("language").equalsIgnoreCase("de_DE")) {
								e.getPlayer().sendMessage("§aDu hast auf deinem Bank Account §b" + eco.getMoneyFromBankMySQL(e.getPlayer()));
							}
						} else {
							e.getPlayer().sendMessage(FrameMainGet.getPrefix() + " " + FrameMainGet.getNoPerm());
						}
					}
			}
		}
	}
	@EventHandler
	public void signChange(SignChangeEvent e) {
			String signName = FrameMain.getInstance().getConfig().getString("MoneySign.Buy");
			signName = signName.replace('&', '§');
			if(e.getLine(0).equalsIgnoreCase("deposit")) {
				if(e.getPlayer().hasPermission("frameapi.signs.create")) {
				String[] args = e.getLines();
				int money = Integer.parseInt(args[1]);
					if(e.getLine(1).equalsIgnoreCase(money + "")) {
							e.setLine(0, "§aDeposit");
							e.setLine(1, money + "");
				}
				} else {
					e.getPlayer().sendMessage(FrameMainGet.getPrefix() + " " + FrameMainGet.getNoPerm());
				}
			
		}
	}
	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
			if(e.getAction() == Action.RIGHT_CLICK_BLOCK) {
				if(e.getClickedBlock().getState() instanceof Sign) {
					Sign s = (Sign) e.getClickedBlock().getState();
					if(s.getLine(0).equalsIgnoreCase("§aDeposit")) {
						if(e.getPlayer().hasPermission("frameapi.signs.use")) {
						String[] args = s.getLines();
								int money = Integer.parseInt(args[1]);
								if(s.getLine(1).equalsIgnoreCase(money + "")) {
									if(eco.getMoney(e.getPlayer()) < money) {
										if(FrameMain.getInstance().getConfig().getString("language").equalsIgnoreCase("en_EN")) {
											e.getPlayer().sendMessage("§cNot enought Money");
										} else if(FrameMain.getInstance().getConfig().getString("language").equalsIgnoreCase("de_DE")) {
											e.getPlayer().sendMessage("§cNicht genug Geld");
										}
									} else {
										eco.removeMoney(e.getPlayer(), money);
										eco.AddMoneyFromBank(e.getPlayer(), money);
										if(FrameMain.getInstance().getConfig().getString("language").equalsIgnoreCase("en_EN")) {
											e.getPlayer().sendMessage("§aYou have been successfuly added §b" + money + " §ato your Bank Account");
										} else if(FrameMain.getInstance().getConfig().getString("language").equalsIgnoreCase("de_DE")) {
											e.getPlayer().sendMessage("§aDu hast erfolgreich §b" + money + " §aauf dein Bank Account §bertragen!");
										}
							}
						}
						} else {
							e.getPlayer().sendMessage(FrameMainGet.getPrefix() + " " + FrameMainGet.getNoPerm());
						}
					
				}
			}
		}
	}
	@EventHandler
	public void signChangeSell(SignChangeEvent e) {
			if(e.getLine(0).equalsIgnoreCase("Withdraw")) {
				if(e.getPlayer().hasPermission("frameapi.signs.create")) {
				String[] args = e.getLines();
				int money = Integer.parseInt(args[1]);
						if(e.getLine(1).equalsIgnoreCase(money + "")) {
							e.setLine(0, "§aWithdraw");
							e.setLine(1, money + "");
					
				}
				} else {
					e.getPlayer().sendMessage(FrameMainGet.getPrefix() + " " + FrameMainGet.getNoPerm());
				}
			
		}
	}
	@EventHandler
	public void onInteractSell(PlayerInteractEvent e) {
			if(e.getAction() == Action.RIGHT_CLICK_BLOCK) {
				if(e.getClickedBlock().getState() instanceof Sign) {
					Sign s = (Sign) e.getClickedBlock().getState();
					if(s.getLine(0).equalsIgnoreCase("§aWithdraw")) {
						if(e.getPlayer().hasPermission("frameapi.signs.use")) {
						String[] args = s.getLines();
								int money = Integer.parseInt(args[1]);
								if(s.getLine(1).equalsIgnoreCase(money + "")) {
									if(eco.getMoneyFromBankMySQL(e.getPlayer()) < money ) {
										if(FrameMain.getInstance().getConfig().getString("language").equalsIgnoreCase("en_EN")) {
											e.getPlayer().sendMessage("§cNot enought Money in your Bank!");
										} else if(FrameMain.getInstance().getConfig().getString("language").equalsIgnoreCase("de_DE")) {
											e.getPlayer().sendMessage("§cNicht genug Geld auf deinem Bank Account!");
										}
									} else {
										eco.addMoney(e.getPlayer(), money);
										eco.RemoveMoneyFromBank(e.getPlayer(), money);
										if(FrameMain.getInstance().getConfig().getString("language").equalsIgnoreCase("en_EN")) {
											e.getPlayer().sendMessage("§aYou have been successfuly Removed §b" + money + " §afrom your Bank Account");
										} else if(FrameMain.getInstance().getConfig().getString("language").equalsIgnoreCase("de_DE")) {
											e.getPlayer().sendMessage("§aDu hast erfolgreich §b" + money + " §avon deinem Bank Account abgehoben");
										}
									}
								}
						} else {
							e.getPlayer().sendMessage(FrameMainGet.getPrefix() + " " + FrameMainGet.getNoPerm());
						}
					
				}
			}
		}
	}
	

}
