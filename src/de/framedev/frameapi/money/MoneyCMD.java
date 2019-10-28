/**
 * Dies ist ein Plugin von FrameDev
 * Copyright by FrameDev 
 */
package de.framedev.frameapi.money;

import de.framedev.frameapi.api.Money;
import de.framedev.frameapi.main.FrameMain;
import de.framedev.frameapi.main.FrameMain.FrameMainGet;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


/**
 * @author Darryl
 *
 */
public class MoneyCMD implements CommandExecutor{
    private static Money eco = new Money();
    @SuppressWarnings("deprecation")
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(sender instanceof Player) {
            Player p = (Player) sender;
            if(cmd.getName().equalsIgnoreCase("balance")) {
                if(p.hasPermission("frameapi.balance")) {
                    if(args.length == 0) {
                        String money = String.valueOf(eco.getMoney(p));
                        eco.createAccount(p);
                        String text = FrameMain.getInstance().getConfig().getString("Money.MSG.Balance");
                        text = text.replace("[Money]", money);
                        text = text.replace('&', '§');
                        p.sendMessage(text);
                        return true;
                    } else if(args.length == 1) {
                        if(p.hasPermission("frameapi.balance.other")) {
                            Player target = Bukkit.getPlayer(args[0]);
                            if(target != null) {
                                String money = String.valueOf(eco.getMoney(target));
                                String text = FrameMain.getInstance().getConfig().getString("MoneyBalance.Other.MSG");
                                text = text.replace("[Money]", money);
                                text = text.replace('&', '§');
                                text = text.replace("[Target]", target.getName());
                                p.sendMessage(text);
                                return true;
                            } else {
                                OfflinePlayer Offline = Bukkit.getOfflinePlayer(args[0]);
                                String money = String.valueOf(eco.getMoney(Offline));
                                String text = FrameMain.getInstance().getConfig().getString("MoneyBalance.Other.MSG");
                                text = text.replace("[Money]", money);
                                text = text.replace('&', '§');
                                text = text.replace("[Target]", Offline.getName());
                                p.sendMessage(text);
                            }
                        } else {
                            p.sendMessage(FrameMainGet.getPrefix() + " " + FrameMainGet.getNoPerm());
                            return true;
                        }
                    }
                } else {
                    p.sendMessage(FrameMainGet.getPrefix() + " " + FrameMainGet.getNoPerm());
                    return true;
                }
            }
            if(cmd.getName().equalsIgnoreCase("pay")) {
                try {
                    double amount = Double.parseDouble(args[1]);
                    Player target = Bukkit.getPlayer(args[0]);
                    if(target == null) {
                        OfflinePlayer Offline = Bukkit.getOfflinePlayer(args[0]);
                        eco.addMoney(Offline, amount);
                        eco.removeMoney(p, amount);
                        String money = String.valueOf(amount);
                        String Text = FrameMain.getInstance().getConfig().getString("Money.MSG.Pay");
                        Text = Text.replace('&', '§');
                        Text = Text.replace("[Target]", Offline.getName());
                        Text = Text.replace("[Money]", money);
                        p.sendMessage(Text);
                        return true;
                    } else {
                        if(eco.getMoney(p) < amount) {
                            p.sendMessage("§4Not enought Money");
                            return true;
                        } else {
                            eco.addMoney(target, amount);
                            eco.removeMoney(p, amount);
                            String money = String.valueOf(amount);
                            String Text = FrameMain.getInstance().getConfig().getString("Money.MSG.Pay");
                            Text = Text.replace('&', '§');
                            Text = Text.replace("[Target]", target.getName());
                            Text = Text.replace("[Money]", money);
                            p.sendMessage(Text);
                            target.sendMessage("§aYou got §b" + money + " §afrom §b" + p.getName());
                            return true;
                        }
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    p.sendMessage("§cPlease use /pay (target) (amount)");
                }
            }
            if(cmd.getName().equalsIgnoreCase("set")) {
                if(p.hasPermission("frameapi.set")) {
                    if(args.length == 1) {
                        double amount = Double.parseDouble(args[0]);
                        eco.setMoney(p, amount);
                        String Text = FrameMain.getInstance().getConfig().getString("Money.MSG.Set");
                        String money = String.valueOf(amount);
                        Text = Text.replace('&', '§');
                        Text = Text.replace("[Money]", money);
                        p.sendMessage(Text);
                        return true;
                        //set 1000
                        //0		1
                    } else if(args.length == 2) {
                        if(p.hasPermission("frameapi.set.other")) {
                            double amount = Double.parseDouble(args[1]);
                            Player target = Bukkit.getPlayer(args[0]);
                            if(target != null) {
                                String money = String.valueOf(amount);
                                eco.setMoney(target, amount);
                                String Text = FrameMain.getInstance().getConfig().getString("MoneySet.Other.MSG");
                                String Text1 = FrameMain.getInstance().getConfig().getString("Money.MSG.Set");
                                Text1 = Text1.replace('&', '§');
                                Text1 = Text1.replace("[Money]", String.valueOf(amount));
                                Text = Text.replace('&', '§');
                                Text = Text.replace("[Target]", target.getName());
                                Text = Text.replace("[Money]", money);
                                target.sendMessage(Text1);
                                p.sendMessage(Text);
                                return true;
                            } else {
                                OfflinePlayer Offline = Bukkit.getOfflinePlayer(args[0]);
                                String money = String.valueOf(amount);
                                eco.setMoney(Offline, amount);
                                String Text = FrameMain.getInstance().getConfig().getString("MoneySet.Other.MSG");
                                Text = Text.replace('&', '§');
                                Text = Text.replace("[Target]", Offline.getName());
                                Text = Text.replace("[Money]", money);
                                p.sendMessage(Text);
                                return true;

                            }
                        } else {
                            p.sendMessage(FrameMainGet.getPrefix() + " " + FrameMainGet.getNoPerm());
                            return true;
                        }
                    }
                } else {
                    p.sendMessage(FrameMainGet.getPrefix() + " " + FrameMainGet.getNoPerm());
                    return true;
                }
            }
        } else {
            if(cmd.getName().equalsIgnoreCase("set")) {
                if(args.length == 2) {
                    try {
                        Player target = Bukkit.getPlayer(args[0]);
                        if(target != null) {
                            double amount = Double.parseDouble(args[1]);
                            eco.setMoney(target, amount);
                            String Text = FrameMain.getInstance().getConfig().getString("Money.MSG.Set");
                            String money = String.valueOf(amount);
                            Text = Text.replace('&', '§');
                            Text = Text.replace("[Money]", money);
                            sender.sendMessage(Text);
                            return true;
                        } else {
                            double amount = Double.parseDouble(args[1]);
                            OfflinePlayer Offline = Bukkit.getOfflinePlayer(args[0]);
                            String money = String.valueOf(amount);
                            eco.setMoney(Offline, amount);
                            String Text = FrameMain.getInstance().getConfig().getString("MoneySet.Other.MSG");
                            Text = Text.replace('&', '§');
                            Text = Text.replace("[Target]", Offline.getName());
                            Text = Text.replace("[Money]", money);
                            sender.sendMessage(Text);
                            return true;
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {
                        sender.sendMessage("§cuse /set (target) (amount)");
                    }
                }
            }
            if(cmd.getName().equalsIgnoreCase("balance")) {
                if(args.length == 1) {
                    try {
                        Player target = Bukkit.getPlayer(args[0]);
                        if(target != null) {
                            double amount = eco.getMoney(target);
                            String Text = FrameMain.getInstance().getConfig().getString("MoneySet.Other.MSG");
                            Text = Text.replace('&', '§');
                            Text = Text.replace("[Target]", target.getName());
                            Text = Text.replace("[Money]", String.valueOf(amount));
                            sender.sendMessage(Text);
                            return true;
                        } else {
                            OfflinePlayer Offline = Bukkit.getOfflinePlayer(args[0]);
                            String money = String.valueOf(eco.getMoney(Offline));
                            String text = FrameMain.getInstance().getConfig().getString("MoneyBalance.Other.MSG");
                            text = text.replace("[Money]", money);
                            text = text.replace('&', '§');
                            text = text.replace("[Target]", Offline.getName());
                            sender.sendMessage(text);
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {
                        sender.sendMessage("§cuse /balance (target)");
                    }
                }
            }
        }
        return false;
    }

}
