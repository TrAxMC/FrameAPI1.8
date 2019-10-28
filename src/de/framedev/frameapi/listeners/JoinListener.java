package de.framedev.frameapi.listeners;

import com.mysql.jdbc.PreparedStatement;
import de.framedev.frameapi.api.API;
import de.framedev.frameapi.main.FrameMain;
import de.framedev.frameapi.mysql.IsTableExist;
import de.framedev.frameapi.mysql.MYSQL;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JoinListener implements Listener {

    public FrameMain main = FrameMain.getInstance();
    public API api = new API();
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        new BukkitRunnable() {

            @Override
            public void run() {
                if(main.getConfig().getBoolean("MYSQL.Boolean")) {
                    MYSQL.connect();
                    if(IsTableExist.isExist("offline")) {
                        try {
                            Statement statement = MYSQL.getConnection().createStatement();
                            ResultSet res = statement.executeQuery("SELECT * FROM offline WHERE PlayerName = '" + e.getPlayer().getName() + "';");
                            if(res.next()) {
                                if(res.getString("PlayerName") == null) {
                                    statement.executeUpdate("INSERT INTO offline (PlayerName,boolean) VALUES ('"+e.getPlayer().getName()+ "','no');");
                                    Bukkit.getConsoleSender().sendMessage(FrameMain.FrameMainGet.getPrefix() + "§b §cInserted");
                                    return;
                                } else {
                                    String sql2 = "UPDATE offline SET boolean = 'no' WHERE PlayerName = '" + e.getPlayer().getName() + "'";
                                    statement.executeUpdate(sql2);
                                    Bukkit.getConsoleSender().sendMessage(FrameMain.FrameMainGet.getPrefix() + "§b Updated info");
                                    return;
                                }
                            } else {
                                statement.executeUpdate("INSERT INTO offline (PlayerName,boolean) VALUES ('"+e.getPlayer().getName()+ "','no');");
                            }

                        } catch (SQLException e1) {
                            e1.printStackTrace();
                        }
                        finally {
                        }


                    } else {
                        String sql = "CREATE TABLE IF NOT EXISTS offline(PlayerName TEXT(11),boolean TEXT);";
                        try {
                            PreparedStatement stmt = (PreparedStatement) MYSQL.getConnection().prepareStatement(sql);
                            stmt.executeUpdate();
                        } catch (SQLException e1) {
                            e1.printStackTrace();
                        }
                    }

                }
            }
        }.runTaskLater(FrameMain.getInstance(), 60);
        new BukkitRunnable() {
            @Override
            public void run() {
                for(Player online : Bukkit.getOnlinePlayers()) {
                    MYSQL.connect();
                    try {
                        Statement statement = MYSQL.getConnection().createStatement();
                        ResultSet res = statement.executeQuery("SELECT * FROM pays WHERE PlayerTo = '" + online.getName() + "';");
                        if(res.next()) {
                            int amounts = api.getPays(online);
                            main.j = amounts;
                            if(main.j == 0) {
                            } else {
                                online.sendMessage("§aYou get a Pay use /paythebill to sale it of §b" + main.j);
                                main.pays.add(online.getName());
                                main.j = 0;
                            }
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }

            }
        }.runTaskLater(FrameMain.getInstance(), 120);

    }
    /**
     * @param e {@link PlayerJoinEvent}
     */
    @EventHandler
    public void onjoin(PlayerJoinEvent e) {

        if(FrameMain.getInstance().getConfig().getBoolean("Join.Message.Boolean")) {
            String message = api.getCustomConfig().getString("JoinMessage");
            message = message.replace("[Player]", e.getPlayer().getDisplayName());
            message = message.replace('&', '§');
            e.setJoinMessage(message);
            Bukkit.getConsoleSender().sendMessage(message);
        }
    }

}