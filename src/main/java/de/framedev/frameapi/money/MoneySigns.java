/**
 * Dies ist ein Plugin von FrameDev
 * Bitte nichts §ndern, @Copyright by FrameDev 
 */
package de.framedev.frameapi.money;

import de.framedev.frameapi.api.money.Money;
import de.framedev.frameapi.main.FrameMain;
import de.framedev.frameapi.main.FrameMain.FrameMainGet;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

/**
 * @author Darryl
 *
 */
public class MoneySigns implements Listener {
	private static Money eco = new Money();
	@EventHandler
	public void onclickSignBalance(SignChangeEvent e) {
			if(e.getLine(0).equalsIgnoreCase("[balance]")) {
				if(e.getPlayer().hasPermission("frameapi.signs.create")) {
				String signName = FrameMain.getInstance().getConfig().getString("MoneySign.Balance");
				signName = signName.replace('&', '§');
				e.setLine(0, signName);
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
					String signName = FrameMain.getInstance().getConfig().getString("MoneySign.Balance");
					signName = signName.replace('&', '§');
					if(s.getLine(0).equalsIgnoreCase(signName)) {
						if(e.getPlayer().hasPermission("frameapi.signs.use")) {
						String money = String.valueOf(eco.getMoney(e.getPlayer()));
						String text = FrameMain.getInstance().getConfig().getString("Money.MSG.Balance");
						text = text.replace("[Money]", money);
						text = text.replace('&', '§');
						e.getPlayer().sendMessage(text);
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
			if(e.getLine(0).equalsIgnoreCase("buy")) {
				String[] args = e.getLines();
				Material name = Material.getMaterial(args[1].toUpperCase());
				int amount = Integer.parseInt(args[2]);
				int money = Integer.parseInt(args[3]);
				if(e.getPlayer().hasPermission("frameapi.signs.create")) {
				if(e.getLine(1).equalsIgnoreCase(name.name())) {
					if(e.getLine(2).equalsIgnoreCase(amount + "")) {
						if(e.getLine(3).equalsIgnoreCase(money + "")) {
							e.setLine(0, signName);
							e.setLine(1, name.name());
							e.setLine(2, amount + "");
							e.setLine(3, money + "");
						}
					}
				}
				} else {
					e.getPlayer().sendMessage(FrameMainGet.getPrefix() + " " + FrameMainGet.getNoPerm());
			}
		}
	}
	@EventHandler
	public void SignChangeFree(SignChangeEvent e) {
			String signName = FrameMain.getInstance().getConfig().getString("MoneySign.Free");
			signName = signName.replace('&', '§');
			if(e.getLine(0).equalsIgnoreCase("free")) {
				if(e.getPlayer().hasPermission("frameapi.signs.create")) {
				String[] args = e.getLines();
				Material name = Material.getMaterial(args[1].toUpperCase());
				int amount = Integer.parseInt(args[2]);
				if(e.getLine(1).equalsIgnoreCase(name.name())) {
					if(e.getLine(2).equalsIgnoreCase(amount + "")) {
						e.setLine(0, signName);
						e.setLine(1, name.name());
						e.setLine(2, amount + "");
					}
				
				}
				} else {
					e.getPlayer().sendMessage(FrameMainGet.getPrefix() + " " + FrameMainGet.getNoPerm());
				}
		}
	}
	@EventHandler
	public void onInteractFree(PlayerInteractEvent e) {
			if(e.getAction() == Action.RIGHT_CLICK_BLOCK) {
				String signName = FrameMain.getInstance().getConfig().getString("MoneySign.Free");
				signName = signName.replace('&', '§');
				if(e.getClickedBlock().getState() instanceof Sign) {
					Sign s = (Sign) e.getClickedBlock().getState();
					if(s.getLine(0).equalsIgnoreCase(signName)) {
						if(e.getPlayer().hasPermission("frameapi.signs.use")) {
						String[] args = s.getLines();
						Material name = Material.getMaterial(args[1].toUpperCase());
						if(s.getLine(1).equalsIgnoreCase(name.name())) {
							int amount = Integer.parseInt(args[2]);
							if(s.getLine(2).equalsIgnoreCase(amount + "")) {
								e.getPlayer().getInventory().addItem(new ItemStack(name , amount));
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
	public void onInteract(PlayerInteractEvent e) {
			if(e.getAction() == Action.RIGHT_CLICK_BLOCK) {
				if(e.getClickedBlock().getState() instanceof Sign) {
					String signName = FrameMain.getInstance().getConfig().getString("MoneySign.Buy");
					signName = signName.replace('&', '§');
					Sign s = (Sign) e.getClickedBlock().getState();
					if(s.getLine(0).equalsIgnoreCase(signName)) {
						if(e.getPlayer().hasPermission("frameapi.signs.use")) {
						String[] args = s.getLines();
						Material name = Material.getMaterial(args[1].toUpperCase());
						if(s.getLine(1).equalsIgnoreCase(name.name())) {
							int amount = Integer.parseInt(args[2]);
							if(s.getLine(2).equalsIgnoreCase(amount + "")) {
								int money = Integer.parseInt(args[3]);
								if(s.getLine(3).equalsIgnoreCase(money + "")) {
									if(eco.getMoney(e.getPlayer()) < money) {
										e.getPlayer().sendMessage("Not enought Money");
									} else {
										e.getPlayer().getInventory().addItem(new ItemStack(name, amount));
										eco.removeMoney(e.getPlayer(), money);
									}
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
			String signName = FrameMain.getInstance().getConfig().getString("MoneySign.Sell");
			signName = signName.replace('&', '§');
			if(e.getLine(0).equalsIgnoreCase("sell")) {
				if(e.getPlayer().hasPermission("frameapi.signs.create")) {
				String[] args = e.getLines();
				Material name = Material.getMaterial(args[1].toUpperCase());
				int amount = Integer.parseInt(args[2]);
				int money = Integer.parseInt(args[3]);
				if(e.getLine(1).equalsIgnoreCase(name.name())) {
					if(e.getLine(2).equalsIgnoreCase(amount + "")) {
						if(e.getLine(3).equalsIgnoreCase(money + "")) {
							e.setLine(0, signName);
							e.setLine(1, name.name());
							e.setLine(2, amount + "");
							e.setLine(3, money + "");
						}
					
					}
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
					String signName = FrameMain.getInstance().getConfig().getString("MoneySign.Sell");
					signName = signName.replace('&', '§');
					Sign s = (Sign) e.getClickedBlock().getState();
					if(s.getLine(0).equalsIgnoreCase(signName)) {
						if(e.getPlayer().hasPermission("frameapi.signs.use")) {
						String[] args = s.getLines();
						Material name = Material.getMaterial(args[1].toUpperCase());
						if(s.getLine(1).equalsIgnoreCase(name.name())) {
							int amount = Integer.parseInt(args[2]);
							if(s.getLine(2).equalsIgnoreCase(amount + "")) {
								int money = Integer.parseInt(args[3]);
								if(s.getLine(3).equalsIgnoreCase(money + "")) {
									if(e.getPlayer().getInventory().contains(name, amount)) {
										e.getPlayer().getInventory().removeItem(new ItemStack(name, amount));
										eco.addMoney(e.getPlayer(), money);
									} else {
										e.getPlayer().sendMessage("Not enought " + name.name());
									}
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
