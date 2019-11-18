package de.framedev.frameapi.listeners;

import de.framedev.frameapi.api.API;
import de.framedev.frameapi.main.FrameMain;
import de.framedev.frameapi.mysql.IsTableExist;
import de.framedev.frameapi.mysql.MYSQL;
import de.framedev.frameapi.utils.ReplaceCharConfig;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class LeaveListener implements Listener {

    public API api = new API();
    public FrameMain main = FrameMain.getInstance();
    /**
     * @param e {@link PlayerKickEvent}
     */
    @EventHandler
    public void onKick(PlayerKickEvent e) {

        if(main.getConfig().getBoolean("MYSQL.Boolean")) {
            if(main.pays.contains(e.getPlayer().getName())) {
                main.pays.remove(e.getPlayer().getName());
            }
            MYSQL.connect();
            try {
                if(IsTableExist.isExist("offline")) {
                    Statement statement = MYSQL.getConnection().createStatement();
                    ResultSet res = statement.executeQuery("SELECT * FROM offline WHERE PlayerName = '" + e.getPlayer().getName() + "';");
                    if(res.next()) {
                        if(res.getString("PlayerName") == null) {
                            statement.executeUpdate("INSERT INTO offline (PlayerName,boolean) VALUES ('"+e.getPlayer().getName()+ "','yes');");
                            Bukkit.getConsoleSender().sendMessage(FrameMain.FrameMainGet.getPrefix() + "§b §cInserted");
                            MYSQL.close();
                        } else {
                            String sql2 = "UPDATE offline SET boolean = 'yes' WHERE PlayerName = '" + e.getPlayer().getName() + "'";
                            statement.executeUpdate(sql2);
                            Bukkit.getConsoleSender().sendMessage(FrameMain.FrameMainGet.getPrefix() + "§b Updated info");
                            MYSQL.close();
                        }
                    } else {
                        statement.executeUpdate("INSERT INTO offline (PlayerName,boolean) VALUES ('"+e.getPlayer().getName()+ "','yes');");
                    }
                } else {
                    String sql = "CREATE TABLE IF NOT EXISTS offline(PlayerName TEXT(11),boolean TEXT);";
                    try {
                        PreparedStatement stmt = (PreparedStatement) MYSQL.getConnection().prepareStatement(sql);
                        stmt.executeUpdate();
                        MYSQL.close();
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                }
            }catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
    }
    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        if(main.getConfig().getBoolean("MYSQL.Boolean")) {
            if(main.pays.contains(e.getPlayer().getName())) {
                main.pays.remove(e.getPlayer().getName());
            }
            MYSQL.connect();
            try {
                if(IsTableExist.isExist("offline")) {
                    Statement statement = MYSQL.getConnection().createStatement();
                    ResultSet res = statement.executeQuery("SELECT * FROM offline WHERE PlayerName = '" + e.getPlayer().getName() + "';");
                    if(res.next()) {
                        if(res.getString("PlayerName") == null) {
                            statement.executeUpdate("INSERT INTO offline (PlayerName,boolean) VALUES ('"+e.getPlayer().getName()+ "','yes');");
                            MYSQL.close();
                            Bukkit.getConsoleSender().sendMessage(FrameMain.FrameMainGet.getPrefix() + "§b §cInserted");
                        } else {
                            String sql2 = "UPDATE offline SET boolean = 'yes' WHERE PlayerName = '" + e.getPlayer().getName() + "'";
                            statement.executeUpdate(sql2);
                            Bukkit.getConsoleSender().sendMessage(FrameMain.FrameMainGet.getPrefix() + "§b Updated info");
                            MYSQL.close();
                        }
                    } else {
                        statement.executeUpdate("INSERT INTO offline (PlayerName,boolean) VALUES ('"+e.getPlayer().getName()+ "','yes');");
                    }
                } else {
                    String sql = "CREATE TABLE IF NOT EXISTS offline(PlayerName TEXT(11),boolean TEXT);";
                    try {
                        PreparedStatement stmt = (PreparedStatement) MYSQL.getConnection().prepareStatement(sql);
                        stmt.executeUpdate();
                        MYSQL.close();
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                }
            }catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
    }
    /**
     * @param e {@link PlayerQuitEvent}
     */
    @EventHandler
    public void onleave(PlayerQuitEvent e) {

        if(FrameMain.getInstance().getConfig().getBoolean("Leave.Message.Boolean")) {
            String message = api.getCustomConfig().getString("LeaveMessage");
            message = ReplaceCharConfig.replaceObjectWithData(message,"[Player]",e.getPlayer().getDisplayName());
            message = ReplaceCharConfig.replaceParagraph(message);
            e.setQuitMessage(message);
        }
    }

}