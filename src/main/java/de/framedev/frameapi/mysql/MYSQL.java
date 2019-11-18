/**
 * Dies ist ein Plugin von FrameDev
 * Bitte nichts ?ndern, @Copyright by FrameDev 
 */
package de.framedev.frameapi.mysql;

import com.mysql.jdbc.MySQLConnection;
import de.framedev.frameapi.main.FrameMain;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author Darryl
 *
 */
public class MYSQL {

    public static String host = FrameMain.getInstance().getConfig().getString("MYSQL.Host");
    public static String user = FrameMain.getInstance().getConfig().getString("MYSQL.User");
    public static String password = FrameMain.getInstance().getConfig().getString("MYSQL.Password");
    public static String database = FrameMain.getInstance().getConfig().getString("MYSQL.Database");
    public static String port = FrameMain.getInstance().getConfig().getString("MYSQL.Port");
    public static Connection con;
    static ConsoleCommandSender console = Bukkit.getConsoleSender();
    public MYSQL() {
    }
    public static Connection getConnection() {
        try {
            con = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, user, password);
            con.setAutoCommit(true);
        } catch (SQLException e) {
            Bukkit.getConsoleSender().sendMessage("[MySQL] §cEin Fehler ist aufgetreten: §f" + e.getMessage());
        }
        return con;
    }
    // connect
    public static void connect() {
        if(con == null) {
            try {
                con = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, user, password);
                con.setAutoCommit(true);
                Bukkit.getConsoleSender().sendMessage("§bMySQL-Verbindung wurde aufgebaut!");
            } catch (SQLException e) {
                Bukkit.getConsoleSender().sendMessage("[MySQL] §cEin Fehler ist aufgetreten: §a" + e.getMessage());
            }
        }
    }
    public static void close() {
        if(con != null)
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
    }
}
