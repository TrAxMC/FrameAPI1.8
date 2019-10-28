/**
 * Dies ist ein Plugin von FrameDev
 * @Copyright by FrameDev
 */
package de.framedev.frameapi.api;

import com.mysql.jdbc.PreparedStatement;
import de.framedev.frameapi.main.FrameMain;
import de.framedev.frameapi.main.FrameMain.FrameMainGet;
import de.framedev.frameapi.mysql.IsTableExist;
import de.framedev.frameapi.mysql.MYSQL;
import de.framedev.frameapi.mysql.SQL;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author Darryl
 *this class is free to use
 *
 */
public class Money implements Listener {

    private static boolean registered;
    /**
     * This is the Constructor to use this Class
     */
    public Money() {

    }
    /**
     * Create an Account it do it automatic if you use this class
     * @param player Player
     */
    public void createAccount(OfflinePlayer player) {
        setRegistered(false);
        if(cfgMoney.getBoolean(player.getUniqueId() + "." + player.getName()+".registered")) {
            saveMoneyFile();
            loadFile();
        } else {
            cfgMoney.set(player.getUniqueId() + "." + player.getName() +".registered", true);
            cfgMoney.set(player.getUniqueId() + "."+ player.getName() + ".Money", 0);
            setRegistered(true);
            saveMoneyFile();
            loadFile();
        }
    }
    /**
     * Money File!!
     */
    private void saveMoneyFile() {
        try {
            cfgMoney.save(fileMoney);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void loadFile() {
        try {
            cfgMoney.load(fileMoney);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }
    private File fileMoney = new File("plugins/FrameAPI/MoneyFile", "money.yml");
    private FileConfiguration cfgMoney = YamlConfiguration.loadConfiguration(fileMoney);

    /**
     * Get the Money from the Config
     * @param player Player
     * @return Integer Money in Config
     */
    public Double getMoney(OfflinePlayer player) {
        if(FrameMain.getInstance().getConfig().getBoolean("MYSQL.Boolean")) {
            try {
                if(cfgMoney.getBoolean(player.getUniqueId() + "." + player.getName()+".registered")) {
                    loadFile();
                    double money =getMoneyMySql(player);

                    return money;
                } else {
                    createAccount(player);
                    saveMoneyFile();
                    loadFile();
                    double money = getMoneyMySql(player);
                    return money;
                }
            } catch (NullPointerException e) {
                return cfgMoney.getDouble(player.getUniqueId() + "." + player.getName() + ".money");
            }
        } else {
            return cfgMoney.getDouble(player.getUniqueId() + "." + player.getName() + ".money");
        }

    }
    /**
     * Add Money to Player and in Config
     * @param player Player
     * @param amount the amount from money
     */
    public void addMoney(OfflinePlayer player, double amount) {
        if(FrameMain.getInstance().getConfig().getBoolean("MYSQL.Boolean")) {
            if(cfgMoney.getBoolean(player.getUniqueId() + "." + player.getName() +".registered")) {
                MYSQL.connect();
                if(IsTableExist.isExist("money")) { //Look if the table is exists
                    double money = getMoneyMySql(player);
                    money = money + amount;
                    saveMoneyInSQL(player, money);
                    saveMoneyFile();
                    loadFile();
                } else {
                        SQL.createTable("money","PlayerName TEXT(64),balance_money INT");
                        SQL.InsertData("PlayerName,balance_money","'" + player.getName() + "','" + 0 + "'","money");
                }
            } else {
                createAccount(player);
                if(IsTableExist.isExist("money")) { //Look if the table is exists
                    double money = getMoneyMySql(player);
                    money = money + amount;
                    saveMoneyInSQL(player, money);
                    saveMoneyFile();
                    loadFile();
                } else {
                        SQL.createTable("money","PlayerName TEXT(64),balance_money");
                        SQL.InsertData("PlayerName,balance_money","'" + player.getName() + "','" + 0 + "'","money");
                }
            }
        } else {
            if(cfgMoney.getBoolean(player.getUniqueId() + "." + player.getName() +".registered")) {
                double money = getMoney(player);
                money = money + amount;
                cfgMoney.set(player.getUniqueId() + "." + player.getName() + ".money", money);
                saveMoneyFile();
                loadFile();
            } else {
                createAccount(player);
                double money = getMoney(player);
                money = money + amount;
                saveMoneyInSQL(player, money);
                saveMoneyFile();
                loadFile();
            }
        }
    }
    /**
     * Remove Money
     * @param player Player
     * @param amount the amount from money
     */
    public void removeMoney(Player player,double amount) {
        if(cfgMoney.getBoolean(player.getUniqueId() + "." + player.getName() +".registered")) {
            if(FrameMain.getInstance().getConfig().getBoolean("MYSQL.Boolean")) {
                double money = getMoneyMySql(player);

                money = money - amount;
                saveMoneyInSQL(player, money);
                saveMoneyFile();
                loadFile();
            } else {
                double money = getMoney(player);
                money = money - amount;
                cfgMoney.set(player.getUniqueId() + "." + player.getName() + ".money", money);
                saveMoneyFile();
                loadFile();
            }
        } else {
            if(FrameMain.getInstance().getConfig().getBoolean("MYSQL.Boolean")) {
                createAccount(player);
                double money = getMoneyMySql(player);
                money = money - amount;
                saveMoneyInSQL(player, money);
                saveMoneyFile();
                loadFile();
                player.sendMessage("§aYou are Successfully Registered and now you can show your Balance!");
            } else {
                createAccount(player);
                double money = getMoney(player);
                money = money - amount;
                cfgMoney.set(player.getUniqueId() + "." + player.getName() + ".money", money);
                saveMoneyFile();
                loadFile();
            }
        }
    }
    /**
     *Set amount for the Money
     * @param offline Player
     * @param amount the amount from money
     */
    public void setMoney(OfflinePlayer offline, double amount) {
        if(FrameMain.getInstance().getConfig().getBoolean("MYSQL.Boolean")) {
            saveMoneyInSQL(offline, amount);
        } else {
            cfgMoney.set(offline.getUniqueId() + "." + offline.getName() + ".money", amount);
            saveMoneyFile();
            loadFile();
        }
    }
    /**
     * @return the registered
     */
    public boolean isRegistered() {
        return registered;
    }
    /**
     * @param registered the registered to set
     */
    public void setRegistered(boolean registered) {
        Money.registered = registered;
    }
    /**
     * @param player Player
     * @param amount Amount of the Money
     */
    private void saveMoneyInSQL(OfflinePlayer player, double amount) {
        MYSQL.connect();
        if (IsTableExist.isExist("money")) {
            if (SQL.exists("PlayerName", player.getName(), "money")) {
                SQL.UpdateData("balance_money", "'" + amount + "'", "money", "PlayerName ='" + player.getName() + "'");
            } else {
                SQL.InsertData("PlayerName,balance_money", "'" + player.getName() + "','" + amount + "'", "money");
            }
        } else {
            String sql2 = "CREATE TABLE IF NOT EXISTS money(PlayerName TEXT(11),balance_money INT);";
            try {
                PreparedStatement stmt = (PreparedStatement) MYSQL.getConnection().prepareStatement(sql2);
                stmt.executeUpdate();
                stmt.executeUpdate("INSERT INTO money (PlayerName,balance_money) VALUES ('" + player.getName() + "','" + amount + "');");
                stmt.executeUpdate();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
    }
    /**
     * @param player the Players how  is in MYSQL
     * @return Money of MYSQL
     */
    private Double getMoneyMySql(OfflinePlayer player) {

        MYSQL.connect();
        if(IsTableExist.isExist("money")) {
            if(SQL.exists("PlayerName",player.getName(),"money")) {
                int x = (int) SQL.get("balance_money","PlayerName",player.getName(),"money");
                return Double.valueOf(x);
            } else {
                SQL.InsertData("PlayerName,balance_money","'" + player.getName() + "','" + 0 + "'","money");
                return 0.0;
            }
        } else {
            String sql2 = "CREATE TABLE IF NOT EXISTS money(PlayerName TEXT(11),balance_money INT);";
            try {
                PreparedStatement stmt = (PreparedStatement) MYSQL.getConnection().prepareStatement(sql2);
                stmt.executeUpdate();
                stmt.executeUpdate("INSERT INTO money (PlayerName,balance_money) VALUES ('"+player.getName()+ "','"+0+"');");
                stmt.executeUpdate();
                return 0.0;
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
        return 0.0;
    }
    /**
     * @param player Player
     * @param amount Amount of the Money how will save in MYSQL Table BankMoney
     */
    public void SaveMoneyInBank(OfflinePlayer player, double amount) {

        try {
            MYSQL.connect();
            if(IsTableExist.isExist("BankMoney")) {
                Statement statement = MYSQL.getConnection().createStatement();
                ResultSet res = statement.executeQuery("SELECT * FROM BankMoney WHERE PlayerName = '" + player.getName() + "';");
                if(res.next()) {
                    if(res.getString("PlayerName") == null) {
                        statement.executeUpdate("INSERT INTO BankMoney (PlayerName,balance_money) VALUES ('"+player.getName()+ "','" + amount + "');");
                    } else {
                        String sql = "UPDATE BankMoney SET balance_money = '" + amount +"' WHERE PlayerName = '" + player.getName() + "'";
                        statement.executeUpdate(sql);
                        Bukkit.getConsoleSender().sendMessage(FrameMainGet.getPrefix() + "§b Updated info");
                    }
                } else {
                    statement.executeUpdate("INSERT INTO BankMoney (PlayerName,balance_money) VALUES ('"+player.getName()+ "','" + amount + "');");
                }
            } else {
                String sql = "CREATE TABLE IF NOT EXISTS BankMoney(PlayerName TEXT(11),balance_money INT);";
                // prepare the statement to be executed
                try {
                    Statement stmt = MYSQL.getConnection().createStatement();
                    SQL.createTable("BankMoney", "PlayerName TEXT(64),balance_money INT");
                    stmt.executeUpdate("INSERT INTO BankMoney (PlayerName,balance_money) VALUES ('"+player.getName()+ "','" + amount + "');");
                } catch (SQLException e3) {
                    e3.printStackTrace();
                }
            }

        } catch (SQLException e2) {
            e2.printStackTrace();
        }
    }
    /**
     * @param player Player
     * @return Money of MYSQL from Player
     */
    public Double getMoneyFromBankMySQL(OfflinePlayer player) {

        MYSQL.connect();
        if(IsTableExist.isExist("BankMoney")) {
            Statement statement = null;
            try {
                statement = MYSQL.getConnection().createStatement();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            ResultSet res = null;
            try {
                res = statement.executeQuery("SELECT * FROM BankMoney WHERE PlayerName = '" + player.getName() + "';");
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            try {
                if(!res.next()) {
                    statement.executeUpdate("INSERT INTO BankMoney (PlayerName,balance_money) VALUES ('"+player.getName()+ "','" + 0 + "');");
                } else {
                    return res.getDouble("balance_money");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            try {
                Statement stmt = MYSQL.getConnection().createStatement();
                SQL.createTable("BankMoney", "PlayerName TEXT(64),balance_money INT");
                stmt.executeUpdate("INSERT INTO BankMoney (PlayerName,balance_money) VALUES ('"+player.getName()+ "','" + 0 + "');");
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
        return 0.0;
    }
    public void RemoveMoneyFromBank(OfflinePlayer player, double amount) {
        double money = getMoneyFromBankMySQL(player);
        money = money - amount;
        SaveMoneyInBank(player, money);
    }
    public void AddMoneyFromBank(OfflinePlayer player, double amount) {
        double money = getMoneyFromBankMySQL(player);
        money = money + amount;
        SaveMoneyInBank(player, money);
    }
    public boolean hasPlayerAmount(OfflinePlayer player ,int amount) {
        if(getMoney(player) >= amount) {
            return true;
        } else {
            return false;
        }
     }
}
