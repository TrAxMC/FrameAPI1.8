/**
 * Dies ist ein Plugin von FrameDev
 * @Copyright by FrameDev
 */
package de.framedev.frameapi.api;

import de.framedev.frameapi.api.money.Money;
import de.framedev.frameapi.cmds.*;
import de.framedev.frameapi.main.FrameMain;
import de.framedev.frameapi.main.FrameMain.FrameMainGet;
import de.framedev.frameapi.materials.ItemBuilder;
import de.framedev.frameapi.mysql.IsTableExist;
import de.framedev.frameapi.mysql.MYSQL;
import de.framedev.frameapi.mysql.SQL;
import de.framedev.frameapi.pets.Pets;
import de.framedev.frameapi.utils.Config;
import de.framedev.frameapi.utils.Information;
import org.bukkit.*;
import org.bukkit.Color;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.Event;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.permissions.ServerOperator;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

/**
 * @author Darryl
 *This API is Free to use
 *it has a much of cool stuff
 *
 */
@SuppressWarnings("deprecation")
public class API implements Listener {
    public static HashMap<String, Location> oldloc = new HashMap<String, Location>();
    private static String version = Information.getVersion();
    private static String Author = Information.getAuthor();
    private static String name = Information.getName();
    private static String apiversion = Information.getApiversion();
    private static File file = new File("plugins/FrameAPI/locations.yml");
    public static FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
    private static FileConfiguration cfglocation;
    private static File filelocation;
    private static String messageOfreload;
    private static Money eco = new Money();
    private String materialname;

    /**
     *Use this Constructor for the API sections!
     */
    public API() {

    }
    /**
     * Register the Commands
     * @param plugin this Plugin
     */
    public void registerCommands(FrameMain plugin) {
        plugin.getCommand("frameapi").setExecutor(new FrameCMDS());
        plugin.getCommand("back").setExecutor(new FrameAPICMDS());
        plugin.getCommand("spawnmob").setExecutor(new FrameAPICMDS());
        plugin.getCommand("denied").setExecutor(new FrameAPICMDS());
        plugin.getCommand("getitem").setExecutor(new FrameAPICMDS());
        plugin.getCommand("heal").setExecutor(new FrameAPICMDS());
        plugin.getCommand("getmysql").setExecutor(new FrameAPICMDS());
        plugin.getCommand("frameapi").setTabCompleter(new FrameCMDS());
        plugin.getCommand("teleporter").setExecutor(new FrameAPICMDS());
        plugin.getCommand("setwarp").setExecutor(new FrameAPICMDS());
        plugin.getCommand("warp").setExecutor(new FrameAPICMDS());
        plugin.getCommand("removewarp").setExecutor(new FrameAPICMDS());
        plugin.getCommand("warp").setTabCompleter(new FrameAPICMDS());
        plugin.getCommand("removewarp").setTabCompleter(new FrameAPICMDS());
        plugin.getCommand("setwarp").setTabCompleter(new FrameAPICMDS());
        plugin.getCommand("kits").setExecutor(new FrameAPICMDS());
        plugin.getCommand("msg").setExecutor(new FrameAPICMDS());
        plugin.getCommand("god").setExecutor(new FrameAPICMDS());
        plugin.getCommand("gm").setExecutor(new FrameAPICMDS());
        plugin.getCommand("ultrahardcore").setExecutor(new FrameAPICMDS());
        new ChunkloaderCMD(plugin);
        Bukkit.getPluginManager().registerEvents(new FrameAPICMDS(),plugin);
        if(FrameMain.getInstance().getConfig().getBoolean("Money.CMDS")) {
            plugin.getCommand("set").setExecutor(new MoneyCMD());
            plugin.getCommand("balance").setExecutor(new MoneyCMD());
            plugin.getCommand("pay").setExecutor(new MoneyCMD());
        } else {
            if(FrameMain.getInstance().getConfig().getString("language").equalsIgnoreCase("en_EN")) {
                Bukkit.getConsoleSender().sendMessage(FrameMainGet.getPrefix() + " §4Money Commands are Disabled");
            } else if(FrameMain.getInstance().getConfig().getString("language").equalsIgnoreCase("de_DE")) {
                Bukkit.getConsoleSender().sendMessage(FrameMainGet.getPrefix() + " §4Money Commands sind Deaktiviert");
            }
        }
        if(FrameMain.getInstance().getConfig().getBoolean("MYSQL.Boolean")) {
            plugin.getCommand("deposit").setExecutor(new MoneyBankCMD());
            plugin.getCommand("withdraw").setExecutor(new MoneyBankCMD());
            plugin.getCommand("transfer").setExecutor(new MoneyBankCMD());
            plugin.getCommand("setbank").setExecutor(new MoneyBankCMD());
            plugin.getCommand("bankbalance").setExecutor(new MoneyBankCMD());
            plugin.getCommand("createpay").setExecutor(new FrameAPICMDS());
            plugin.getCommand("paythebill").setExecutor(new FrameAPICMDS());
        }
    }

    public void init() {
        registerCommands(FrameMain.getInstance());
        StartUp(FrameMain.getInstance());
        FrameMain.getInstance().registerListener(FrameMain.getInstance());
    }
    /**
     * Get Information from FrameAPI
     * @author FrameDev
     * @param p PlayerName
     */
    public void getInformation(Player p) {
        ArrayList<String> string = new ArrayList<String>();
        string.add(0, FrameMainGet.getPrefix() + " §bPlugin Name = §a"+ name);
        string.add(1 ,FrameMainGet.getPrefix() + " §bAuthor = §a" + Author);
        string.add(2, FrameMainGet.getPrefix() + " §bVersion = §a" + version);
        string.add(3, FrameMainGet.getPrefix() + " §bApi Version = §a" + apiversion);
        for(String s : string) {
            p.sendMessage(s);
        }
        ArrayList<String> issues = new ArrayList<String>();
        issues.add("");
        p.sendMessage(issues.get(0));

    }
    public void InformationStartup() {
        ArrayList<String> string = new ArrayList<String>();
        string.add(0, FrameMainGet.getPrefix() + " §bPlugin Name = §a"+ name);
        string.add(1 ,FrameMainGet.getPrefix() + " §bAuthor = §a" + Author);
        string.add(2, FrameMainGet.getPrefix() + " §bVersion = §a" + version);
        string.add(3, FrameMainGet.getPrefix() + " §bApi Version = §a" + apiversion);
        ArrayList<String> issues = new ArrayList<String>();
        issues.add(0, FrameMainGet.getPrefix() + "");
        for(String s : string) {
            Bukkit.getConsoleSender().sendMessage(s);
        }
        Bukkit.getConsoleSender().sendMessage(issues.get(0));
    }
    /**
     * Send Messages pls dont change
     * @param plugin use for register all Listeners
     */
    public void StartUp(FrameMain plugin) {
        if(FrameMain.getInstance().getConfig().getString("language").equalsIgnoreCase("en_EN")) {
            Bukkit.getConsoleSender().sendMessage(FrameMainGet.getPrefix() + " §4if you want a specific version then write me in the forum!");
            Bukkit.getConsoleSender().sendMessage("§b==========================================");
            Bukkit.getConsoleSender().sendMessage("§b                                          ");
            Bukkit.getConsoleSender().sendMessage("§b=         §a[§aFrameAPI]§b                     =");
            Bukkit.getConsoleSender().sendMessage("§b                                          ");
            Bukkit.getConsoleSender().sendMessage("§b                                          ");
            Bukkit.getConsoleSender().sendMessage("§b==========================================");
            ConfigStartup();
            Bukkit.getConsoleSender().sendMessage(FrameMainGet.getPrefix() + " §bLoaded");
        } else if(FrameMain.getInstance().getConfig().getString("language").equalsIgnoreCase("de_DE")) {
            Bukkit.getConsoleSender().sendMessage(FrameMainGet.getPrefix() + " §4Wenn du eine eigene Version haben moechtest Kontaktiere mich im Forum");
            Bukkit.getConsoleSender().sendMessage("§b==========================================");
            Bukkit.getConsoleSender().sendMessage("§b                                          ");
            Bukkit.getConsoleSender().sendMessage("§b=         §a[§aFrameAPI]§b                     =");
            Bukkit.getConsoleSender().sendMessage("§b                                          ");
            Bukkit.getConsoleSender().sendMessage("§b                                          ");
            Bukkit.getConsoleSender().sendMessage("§b==========================================");
            ConfigStartup();
            Bukkit.getConsoleSender().sendMessage(FrameMainGet.getPrefix() + " §bgeladen");
        }
    }
    /**
     * Send a Custom Messages to Player by send to console /Reload
     */
    public void sendMessageofReload() {

        Bukkit.getOnlinePlayers().forEach(current -> {
            messageOfreload = FrameMain.getInstance().getConfig().getString("MessageOfReload.Text");
            messageOfreload = messageOfreload.replace("[Player]", current.getName());
            messageOfreload = messageOfreload.replace('&', '§');
            current.sendMessage(messageOfreload);
        });
    }


    /**
     * @author Darryl
     *Locations Class
     */
    public static class Locations {

        /**
         * Save Location File
         */
        private static void saveCfgLocation() {
            try {
                cfg.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        /**
         * Save a Location with String and PlayerLocation
         * @param locationname Location Name
         * @param loc save Location
         */
        public static void setLocation(String locationname, Location loc) {

            cfg.set(locationname+ ".world", loc.getWorld().getName());
            cfg.set(locationname+".x", loc.getX());
            cfg.set(locationname+".y", loc.getY());
            cfg.set(locationname+".z", loc.getZ());
            cfg.set(locationname+".yaw", loc.getYaw());
            cfg.set(locationname+".pitch", loc.getPitch());
            saveCfgLocation();
            if(!file.exists()) {
                try {
                    file.mkdir();
                } catch (Exception e) {

                }

            }
        }
        private static Location loc;
        /**
         * @param locationname String Location name in File
         * @return Location
         */
        public static Location getLocation(String locationname) {

            try {
                World world = Bukkit.getWorld(cfg.getString(locationname+".world"));
                double x = cfg.getDouble(locationname+".x");
                double y = cfg.getDouble(locationname+".y");
                double z = cfg.getDouble(locationname+".z");
                float yaw = (float) cfg.get(locationname+".yaw");
                float pitch = (float) cfg.get(locationname+".pitch");
                loc = new Location(world, x, y, z, yaw, pitch);
            } catch (NullPointerException e) {
                Bukkit.getConsoleSender().sendMessage("§4This Location can't found in File!");
            }
            return loc;

        }
    }
    @EventHandler
    public void onClickPlayerSetPassenger(PlayerInteractAtEntityEvent e) {
        if(FrameMain.getInstance().getConfig().getBoolean("SetPlayerPassenger")) {
            if(e.getRightClicked() instanceof Player) {
                Player target = (Player) e.getRightClicked();
                Player p = e.getPlayer();
                p.setPassenger(target);
            }
        }
    }
    public ItemStack getWrittenBook(Player p) {
        ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta meta = (BookMeta) book.getItemMeta();
        meta.setAuthor(API.Author);
        meta.setDisplayName("§aFrameAPI Book Info");
        if(FrameMain.getInstance().getConfig().getString("language").equalsIgnoreCase("en_EN")) {
            meta.addPage("§aWelcome §b" + p.getName() + "\n"
                    + "§aThis Book was Written by §b\n"
                    + "" + API.Author + "\n"
                    + "§aif you have any Questions contact me in the Forum");
            book.setItemMeta(meta);
            return book;
        } else  if(FrameMain.getInstance().getConfig().getString("language").equalsIgnoreCase("de_DE")) {
            meta.addPage("§aWillkommen §b" + p.getName() + "\n"
                    + "§aDieses Buch wurde geschrieben von §b\n"
                    + "" + API.Author + "\n"
                    + "§aWenn du Fragen hast Kontaktiere mich im Forum!");
            book.setItemMeta(meta);
            return book;
        }
        return book;

    }
    /**
     * get the Onlineplayers in Int.
     * @return Online Size of Players
     */
    public Integer getAllOnlinePlayersInInt() {
        return Bukkit.getOnlinePlayers().size();

    }
    /**
     * @param money Money in Int to get all Players
     */
    public void getAllPlayerMoney(double money) {
        Bukkit.getOnlinePlayers().forEach(current -> {
            eco.addMoney(current, money);
        });
    }

    /**
     * @param Displayname displayname
     * @param pages pages
     * @return WrittenBook
     */
    public ItemStack createWrittenBook(String Displayname, String pages) {
        ItemStack item = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta meta = (BookMeta) item.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&',Displayname));
        meta.addPage(pages);
        meta.setAuthor("FrameDev");
        item.setItemMeta(meta);
        return item;
    }
    /**
     * @deprecated
     * This Method is @deprecated
     * @param name PlayerName SkullName
     * @param p Who get the Skull
     */
    public void getSkull(String name, Player p) {

        ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());
        SkullMeta meta = (SkullMeta) skull.getItemMeta();
        meta.setOwner(name);
        meta.setDisplayName(ChatColor.GREEN + name + "'s Head!");
        skull.setItemMeta(meta);

        p.getInventory().addItem(skull);

    }
    /**@deprecated
     * This Method is @deprecated
     * @param name PlayerName
     * @return ItemStack Skull
     */
    public static ItemStack ItemStackSkull(String name) {

        ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());

        SkullMeta meta = (SkullMeta) skull.getItemMeta();
        meta.setDisplayName("§a" + name);
        skull.setItemMeta(meta);
        return skull;
    }
    /**
     * Only for Time Change!
     * @param time in Ticks
     */
    public void SetTime(long time) {
        for (World world : Bukkit.getWorlds()) {
            world.setTime(time);
        }
    }
    public void SetTimeMessage(long time, Player p) {
        for (World world : Bukkit.getWorlds()) {
            world.setTime(time);
            if(FrameMain.getInstance().getConfig().getBoolean("SetTime.Message")) {
                String msg = FrameMain.getInstance().getConfig().getString("SetTime.Text");
                msg = msg.replace("[Time]", time + "");
                msg = msg.replace('&', '§');
                p.sendMessage(msg);
            }
        }
    }
    /**
     * Shoot a Arrow to the Target!
     * @param shoot the Name from Player
     * @param target the name from target
     */
    public void shootArrow(Player shoot, Player target) {
        if(target.isOnline()) {
            target.launchProjectile(Arrow.class);
        } else {
            shoot.sendMessage(target.getDisplayName() + " i'snt Online");
        }
    }
    public void CreateFireWorktoSpawn(Player player,Type type, boolean flicker, Color color) {
        Firework firework = (Firework) player.getWorld().spawnEntity(player.getLocation(), EntityType.FIREWORK);
        FireworkEffect eff = FireworkEffect.builder()
                .with(type)
                .flicker(flicker)
                .withColor(color)
                .build();
        FireworkMeta meta = firework.getFireworkMeta();
        meta.addEffect(eff);
        meta.setPower(3);
        firework.setFireworkMeta(meta);

    }
    private ArrayList<String> hide = new ArrayList<String>();
    /** Hide Online Players for the player
     * @param p Player
     */
    public void HidePlayers(Player p) {

        for(Player players : Bukkit.getOnlinePlayers()) {
            if(hide.contains(p.getName())) {
                p.showPlayer(players);
            } else {
                p.hidePlayer(players);
            }
        }
    }
    private SimpleDateFormat date2=new SimpleDateFormat("HH.mm.ss");
    private String Uhrzeit2=date2.format(new Date());
    private String getDate() {
        return Uhrzeit2;
    }
    public void sendHelp(Player p) {
        if(FrameMain.getInstance().getConfig().getString("language").equalsIgnoreCase("en_EN")) {
            if (p.hasPermission("frameapi.help")) {
                p.sendMessage("§6Please use  /frameapi time || /frameapi getmysql");
                p.sendMessage("§6or use /frameapi startbudget || /frameapi info ");
                p.sendMessage("§6or use /frameapi help || /frameapi reload");
                p.sendMessage("§6or use /back");
                p.sendMessage("§6or use /spawnmob (entitytype)|/spawnmob (entitytype) count");
                p.sendMessage("§6 use /getitem itemname name lore unbreakable true/false");
                p.sendMessage("§6 use /heal or /heal (PlayerName)");
                p.sendMessage("§6 use /denied add (word)|/denied remove (word)");
            }
        } else if(FrameMain.getInstance().getConfig().getString("language").equalsIgnoreCase("de_DE")) {
            if (p.hasPermission("frameapi.help")) {
                p.sendMessage("§6Bitte Benutze  /frameapi time || /frameapi getmysql");
                p.sendMessage("§6oder /frameapi startbudget || /frameapi info ");
                p.sendMessage("§6oder /frameapi help || /frameapi reload");
                p.sendMessage("§6oder /back");
                p.sendMessage("§6oder /spawnmob (entitytype)|/spawnmob (entitytype) count");
                p.sendMessage("§6oder /getitem itemname name lore unbreakable true/false");
                p.sendMessage("§6oder /heal or /heal (PlayerName)");
                p.sendMessage("§6oder /denied add (word)|/denied remove (word)");
            }
        }
    }
    /**
     *
     * Set a Player to ServerOperator
     * @param p Player to OP
     *
     */
    public void setOP(Player p) {

        ServerOperator op = p;
        if(p.isOp()) {
        } else {
            p.setOp(true);
        }
    }
    /**
     *
     * Give all PlayerNames
     * @return names1 = PlayerNames
     *
     */
    public String playerNames() {
        String names1 = "";
        for(Player all : Bukkit.getOnlinePlayers()) {
            String names = all.getDisplayName();
            names = names + names1;
        }
        return names1;
    }
    /**
     * @param player Player
     */
    public void openEnderChest(Player player) {

        player.openInventory(player.getEnderChest());
    }
    /**
     * @param player Player
     * @param target Player target
     */
    public void openOtherEnderChest(Player player,Player target) {

        player.openInventory(target.getEnderChest());
    }
    /**
     * @param player Player
     */
    public void openWorkBench(Player player) {

        player.openWorkbench(player.getLocation(), true);
    }
    /**
     * @param player Player
     * @param target Player target
     */
    public void openInventoryOther(Player player, Player target) {

        player.openInventory(target.getInventory());
    }
    /**
     * @deprecated
     * @param target set passenger to target
     * @param entity what Type
     */
    public void SetPassengetToTarget(Player target, Entity entity) {
        target.setPassenger(entity);
    }
    /**
     * set GodMode to Player
     * @param player Player
     */
    public void godMode(Player player) {

        if(player.spigot().isInvulnerable() == true) {
            player.spigot().setCollidesWithEntities(false);
        } else {
            player.spigot().setCollidesWithEntities(true);
        }
    }
    /**
     *Save the Location from the player
     * @param player Player
     */
    public void savePlayerlocation(Player player) {

        Location loc = player.getLocation();
        filelocation = new File("plugins/FrameAPI/PlayerLocations.yml");
        cfglocation = YamlConfiguration.loadConfiguration(filelocation);
        cfglocation.set(player.getName()+ ".world", loc.getWorld().getName());
        cfglocation.set(player.getName()+".x", loc.getX());
        cfglocation.set(player.getName()+".y", loc.getY());
        cfglocation.set(player.getName()+".z", loc.getZ());
        cfglocation.set(player.getName()+".yaw", loc.getYaw());
        cfglocation.set(player.getName()+".pitch", loc.getPitch());
        saveCfgLocationplayer();

    }
    /**
     * Saved the PlayerLocation
     */
    private void saveCfgLocationplayer() {

        try {
            cfglocation.save(filelocation);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public Location getPlayerLocation(Player player) {
        World world = Bukkit.getWorld(cfglocation.getString(player.getName()+".world"));
        double x = cfglocation.getDouble(player.getName()+".x");
        double y = cfglocation.getDouble(player.getName()+".y");
        double z = cfglocation.getDouble(player.getName()+".z");
        float yaw = cfglocation.getInt(player.getName() + ".yaw");
        float pitch = cfglocation.getInt(player.getName() + ".pitch");
        Location loc = new Location(world, x, y, z,yaw,pitch);
        return loc;
    }
    @SuppressWarnings("unused")
    private void giveDateinChat(Player p) {
        if(p.isOnline() == true) {
            p.sendMessage("Date = " + getDate());
        }
    }
    @EventHandler
    public void onPlayerDeathEvent(PlayerDeathEvent e) {
        Player p = (Player)e.getEntity();
        oldloc.put(p.getName(), p.getLocation());
        if(FrameMain.getInstance().getConfig().getBoolean("OldLoc.Teleport")) {
            if(FrameMain.getInstance().getConfig().getString("language").equalsIgnoreCase("en_EN")) {
                p.sendMessage("§a/back §cto the DeathLocation");
            } else if(FrameMain.getInstance().getConfig().getString("language").equalsIgnoreCase("de_DE")) {
                p.sendMessage("§a/back §czu deiner DeathLocation");
            }
        }
    }
    public void createPet(Player player, EntityType type, String name, double speed, boolean cangetBabies) {
        Pets pet = new Pets();
        pet.createPet(player, type, name, speed, cangetBabies);
    }
    private File deniedFile = new File("plugins/FrameAPI","DeniedWords.yml");
    private FileConfiguration deniedcfg = YamlConfiguration.loadConfiguration(deniedFile);
    /**
     * Use for Denied words
     * @param e {@link AsyncPlayerChatEvent}
     */
    @EventHandler
    public void AsyncChat(AsyncPlayerChatEvent e) {
        if (FrameMain.getInstance().getConfig().getBoolean("Chat.Denied")) {
            Player p = e.getPlayer();
            String msg = e.getMessage();
            for (String words : deniedcfg.getStringList("deniedwords")) {
                if (msg.equalsIgnoreCase(words)) {
                    e.setCancelled(true);
                    Bukkit.getScheduler().runTask(FrameMain.getInstance(), new Runnable() {
                        public void run() {
                            p.kickPlayer("§aDon't use §4" + msg);
                        }
                    });
                    p.kickPlayer("Don't use " + msg);
                    for (Player op : Bukkit.getOnlinePlayers()) {
                        if (op.isOp() == true) {
                            op.sendMessage("§a" + p.getName() + " §fwrite §4" + words);
                        }
                    }
                }
            }
        }
    }
    /*
     * Use for show Updates
     */
    public void onUpdate(String update) {
        Bukkit.getConsoleSender().sendMessage(update);

    }

    /**
     * @param p Player
     * @return is op or not
     */
    public boolean showOpPlayer(Player p) {


        if(p.isOp()) {
            return true;
        } else {
            return false;
        }
    }
    private void ConfigStartup() {
        Config.loadConfig();
        Config.updateConfig();
        Bukkit.getConsoleSender().sendMessage(FrameMainGet.getPrefix() + " §bConfig Updated");
    }

    /**
     * @param mat ItemStack
     * @param itemamount how much items
     * @param displayname Display name from the Item
     * @return The Item how created
     */
    public ItemStack getItem(ItemStack mat, int itemamount,String displayname) {

        ItemStack item = mat;
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(displayname);
        item.setAmount(itemamount);
        item.setItemMeta(meta);
        return item;
    }
    /**
     * @param player player
     * @param amount of Health
     */
    public void HealPlayer(Player player, int amount) {

        player.setHealth(amount);
        player.setFoodLevel(amount);
        player.setSaturation(amount);
        player.setFireTicks(0);
        player.setLastDamage(0);
    }
    /**
     * send information about connecting to MYSQl
     * @param p Player
     */
    public void getInformationMYSQL(Player p) {

        ArrayList<String> issues = new ArrayList<String>();
        issues.add(0, FrameMainGet.getPrefix() + " " + MYSQL.getConnection().toString());
        p.sendMessage(issues.get(0));
    }
    public static class SendMessageEvent extends Event {

        private static HandlerList handlers = new HandlerList();
        private Player player;
        private String message;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public SendMessageEvent(Player player) {
            this.player = player;
        }

        public static HandlerList getHandlerList() {
            return handlers;
        }
        @Override
        public HandlerList getHandlers() {
            return handlers;
        }
        public static void setHandlers(HandlerList handlers) {
            SendMessageEvent.handlers = handlers;
        }
        public void setPlayer(Player player) {
            this.player = player;
        }
        public Player getPlayer() {
            return player;
        }
    }
    /**
     * @param item item
     * @param displayname displayname
     * @param itemlore itemlore
     * @param unbreakable unbreakable
     * @param enchantment enchantment
     * @param enchantmentlevel enchantmentlevel
     * @return ItemStack
     */
    public ItemStack CreateItem(ItemStack item, String displayname, boolean unbreakable, Enchantment enchantment, int enchantmentlevel, String... itemlore) {

        ItemMeta meta = item.getItemMeta();
        meta.addEnchant(enchantment, enchantmentlevel, true);
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', displayname));
        meta.setLore(Arrays.asList(itemlore));
        meta.spigot().setUnbreakable(unbreakable);
        item.setItemMeta(meta);
        return item;
    }
    /**
     * @param world World
     * @return the World time
     */
    public Long getGameTicks(World world) {

        long time = world.getTime();
        return time;
    }
    /**
     * Set the day Time
     * NoNight!
     */
    public void NoNight() {

        if(FrameMain.getInstance().getConfig().getBoolean("NoNight")) {
            new BukkitRunnable() {

                @Override
                public void run() {
                    for(World world : Bukkit.getWorlds()) {
                        if(getGameTicks(world) == 10000) {
                            world.setTime(0);
                        }
                    }

                }
            }.runTaskTimer(FrameMain.getInstance(), 0, 120);
        }
    }
    private static Inventory inv = Bukkit.createInventory(null, 3*9, "§aTeleporter");
    static {
        for(Player all :Bukkit.getOnlinePlayers()) {
            ItemStack skull = ItemStackSkull(all.getName());
            inv.addItem(skull);
        }
    }
    /**Teleport with Inventory
     * @param p Player
     */
    public void TeleporterWithHeads(Player p) {

        if(FrameMain.getInstance().getConfig().getBoolean("Teleporter.Inventory")) {
            p.openInventory(inv);
        } else {
            String text = CreateConfig.getConfig().getString("message.notactivated");
            text = text.replace('&', '§');
            p.sendMessage(FrameMainGet.getPrefix() + " " + text);
        }
    }
    @EventHandler
    public void onClickSkull(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if(e.getInventory().equals(inv)) {
            if(e.getSlotType() == InventoryType.SlotType.OUTSIDE) return;
            if(e.getSlot() == e.getRawSlot()) {
                if(e.getClick() == ClickType.LEFT) {
                    if (e.getCurrentItem() != null && e.getCurrentItem().hasItemMeta()) {
                        String playername = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName());
                        Player player_ = Bukkit.getPlayerExact(playername);
                        if (player_ != null) {
                            e.setCancelled(true);
                            p.closeInventory();
                            p.teleport(player_);
                        }
                    }
                } else if(e.getClick() == ClickType.RIGHT) {
                    if (e.getCurrentItem() != null && e.getCurrentItem().hasItemMeta()) {
                        String playername = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName());
                        Player player_ = Bukkit.getPlayerExact(playername);
                        if (player_ != null) {
                            e.setCancelled(true);
                            p.closeInventory();
                            p.sendMessage(player_.getName());
                        }
                    }
                }
            }
        }
    }
    @EventHandler (priority = EventPriority.HIGH)
    public void firstJoinAsOP(PlayerJoinEvent e) {
        if(e.getPlayer().isOp()) {
            if(e.getPlayer().hasPlayedBefore() == false) {
                Player player = e.getPlayer();
                if (FrameMain.getInstance().getConfig().getString("language").equalsIgnoreCase("en_EN")) {
                    player.sendMessage(FrameMainGet.getPrefix() + " §aVisit the Config.yml");
                    player.sendMessage(FrameMainGet.getPrefix() + " §aLocation = §bplugins/FrameAPI/config.yml");
                } else if(FrameMain.getInstance().getConfig().getString("language").equalsIgnoreCase("de_DE")) {
                    player.sendMessage(FrameMainGet.getPrefix() + " §aBesuche die Config.yml");
                    player.sendMessage(FrameMainGet.getPrefix() + " §aPfad zur Datei = §bplugins/FrameAPI/config.yml");
                }
            }
        }
    }
    public static class Warp {
        private static File warpfile = new File("plugins/FrameAPI/WarpFile/warps.yml");
        private static FileConfiguration cfgwarp = YamlConfiguration.loadConfiguration(warpfile);
        /**
         * @param name Location name
         * @param loc Location that will be saved
         */
        public static void setWarpLocation(String name, Location loc) {
            cfgwarp.set("warps." + name + ".boolean", true);
            cfgwarp.set("warps." + name+ ".world", loc.getWorld().getName());
            cfgwarp.set("warps." +name+".x", loc.getX());
            cfgwarp.set("warps." +name+".y", loc.getY());
            cfgwarp.set("warps." +name+".z", loc.getZ());
            cfgwarp.set("warps." +name+".yaw", loc.getYaw());
            cfgwarp.set("warps." +name+".pitch", loc.getPitch());
            try {
                cfgwarp.save(warpfile);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            if(!warpfile.exists()) {
                try {
                    warpfile.mkdir();
                } catch (Exception e) {

                }

            }
        }
        static Location locwarp;
        /**
         * @param name Location name in File
         * @return Location
         */
        public static Location getWarpLocation(String name) {
            try {
                cfgwarp.getBoolean("warps." + name + ".boolean");
                World world = Bukkit.getWorld(cfgwarp.getString("warps." + name+".world"));
                double x = cfgwarp.getDouble("warps." +name+".x");
                double y = cfgwarp.getDouble("warps." +name+".y");
                double z = cfgwarp.getDouble("warps." +name+".z");
                float yaw = (float) cfgwarp.get("warps." +name+".yaw");
                float pitch = (float) cfgwarp.get("warps." +name+".pitch");
                locwarp = new Location(world, x, y, z,yaw,pitch);
            } catch (NullPointerException e) {
                Bukkit.getConsoleSender().sendMessage("§4Can't find this Location");
            }
            return locwarp;

        }
    }
    /**
     * Get the MOTD from The Config and set the new MOTD While ServerPinging
     * @return MOTD
     */
    private String createNewMotd() {

        String name = FrameMain.getInstance().getConfig().getString("Motd.Name");
        name = name.replace('&', '§');
        return name;
    }
    /**
     * @param e ServerListPingEvent
     */
    @EventHandler
    public void setMotd(ServerListPingEvent e) {

        if(FrameMain.getInstance().getConfig().getBoolean("Motd.Boolean")) {
            e.setMotd(createNewMotd());
        }
    }
    /**
     * @author Darryl
     *
     *Creates the Custom Config for Messages.yml
     */
    public static class CreateConfig {

        // Variables
        private static FileConfiguration myConfig;
        private static File configFile;
        private static boolean loaded = false;

        /**
         * Gets the configuration file.
         *
         * @return the myConfig
         */
        public static FileConfiguration getConfig() {
            if (!loaded) {
                loadConfig();
            }
            return myConfig;
        }

        /**
         * Gets the configuration file.
         *
         * @return Configuration file
         */
        public static File getConfigFile() {

            return configFile;
        }

        /**
         * Loads the configuration file from the .jar.
         */
        public static void loadConfig() {
            configFile = new File(Bukkit.getServer().getPluginManager().getPlugin("FrameAPI").getDataFolder(), "messages.yml");
            if (configFile.exists()) {
                new YamlConfiguration();
                myConfig = YamlConfiguration.loadConfiguration(configFile);
                try {
                    myConfig.load(configFile);
                } catch (IOException | InvalidConfigurationException e1) {
                    e1.printStackTrace();
                }
                try {
                    if(new File(FrameMain.getInstance().getDataFolder() + "/messages.yml").exists()) {
                        boolean changesMade = false;
                        YamlConfiguration tmp = new YamlConfiguration();
                        tmp.load(FrameMain.getInstance().getDataFolder() + "/messages.yml");
                        for(String str : CreateConfig.myConfig.getKeys(true)) {
                            if(!tmp.getKeys(true).contains(str)) {
                                tmp.set(str, CreateConfig.myConfig.get(str));
                                changesMade = true;
                            }
                        }
                        if(changesMade){

                            tmp.save(FrameMain.getInstance().getDataFolder() + "/messages.yml");
                        }
                    }
                } catch (IOException | InvalidConfigurationException e) {
                    e.printStackTrace();
                }
                loaded = true;
            } else {
                try {
                    Bukkit.getServer().getPluginManager().getPlugin("FrameAPI").getDataFolder().mkdir();
                    InputStream jarURL = Config.class.getResourceAsStream("/messages.yml");
                    copyFile(jarURL, configFile);
                    new YamlConfiguration();
                    myConfig = YamlConfiguration.loadConfiguration(configFile);
                    myConfig.save(configFile);
                    myConfig.options().copyDefaults(true);
                    FrameMain.getInstance().getResource("messages.yml");
                    myConfig.load(configFile);
                    loaded = true;
                    try {
                        if(new File(FrameMain.getInstance().getDataFolder() + "/messages.yml").exists()) {
                            boolean changesMade = false;
                            YamlConfiguration tmp = new YamlConfiguration();
                            tmp.load(FrameMain.getInstance().getDataFolder() + "/messages.yml");
                            for(String str : CreateConfig.myConfig.getKeys(true)) {
                                if(!tmp.getKeys(true).contains(str)) {
                                    tmp.set(str, CreateConfig.myConfig.get(str));
                                    changesMade = true;
                                }
                            }
                            if(changesMade){

                                tmp.save(FrameMain.getInstance().getDataFolder() + "/messages.yml");
                            }
                        }
                    } catch (IOException | InvalidConfigurationException e) {
                        e.printStackTrace();
                    }
                    myConfig.load(configFile);
                } catch (Exception e) {
                }
            }
        }

        /**
         * Copies a file to a new location.
         *
         * @param in InputStream
         * @param out File
         *
         * @throws Exception e
         */
        static private void copyFile(InputStream in, File out) throws Exception {
            InputStream fis = in;
            FileOutputStream fos = new FileOutputStream(out);
            try {
                byte[] buf = new byte[1024];
                int i = 0;
                while ((i = fis.read(buf)) != -1) {
                    fos.write(buf, 0, i);
                }
            } catch (Exception e) {
                throw e;
            } finally {
                if (fis != null) {
                    fis.close();
                }
                if (fos != null) {
                    fos.close();
                }
            }
        }
        public static void onloadedfalse() {
            loaded = false;
        }
    }
    /**
     * Reload CustomConfig == Messages.yml
     * @throws UnsupportedEncodingException fault fiel type
     */
    public void reloadCustomConfig() throws UnsupportedEncodingException {

        if (CreateConfig.getConfig() == null) {
        }
        CreateConfig.myConfig = YamlConfiguration.loadConfiguration(CreateConfig.configFile);

        // Look for defaults in the jar
        Reader defConfigStream = new InputStreamReader(FrameMain.getInstance().getResource("messages.yml"), "UTF8");
        if (defConfigStream != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
            CreateConfig.myConfig.setDefaults(defConfig);
        }
    }
    private File fileplayer = new File("plugins/FrameAPI/playerlist.yml");
    private FileConfiguration cfgplayer = YamlConfiguration.loadConfiguration(fileplayer);
    private ArrayList<String> names = new ArrayList<String>();

    /**
     * @param player Player
     */
    public void addPlayerToPlayerList(Player player) {
        names = (ArrayList<String>) cfgplayer.getStringList("Players");
        if(names.contains(player.getName())) {
            return;
        } else {
            names.add(player.getName());
            cfgplayer.set("Players", names);
            try {
                cfgplayer.save(fileplayer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * Creates a New File with the Players for Each!
     */
    public void CreateNewFilePlayerList() {
        names = (ArrayList<String>) cfgplayer.getStringList("Players");
        for(Player player : Bukkit.getOnlinePlayers()) {
            if(cfgplayer.getStringList("Players").contains(player.getName())) {
                break;
            } else {
                names.add(player.getName());
                cfgplayer.set("Players", names);
                try {
                    cfgplayer.save(fileplayer);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    /**
     *
     * @return Players in Int Online Players
     */
    public Integer showPlayersSize() {

        return Bukkit.getOnlinePlayers().size();
    }
    private static File customConfigFile;
    private static FileConfiguration customConfig;
    /**
     * @return the Custom Config == messages.yml
     */
    public FileConfiguration getCustomConfig() {

        return customConfig;
    }

    /**
     * in use for creating the file messages.yml
     */
    public void createCustomConfig() {

        customConfigFile = new File(FrameMain.getInstance().getDataFolder(), "messages.yml");
        if (!customConfigFile.exists()) {
            customConfigFile.getParentFile().mkdirs();
            FrameMain.getInstance().saveResource("messages.yml", false);
        }

        customConfig= new YamlConfiguration();
        try {
            customConfig.load(customConfigFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }
    public GameMode getGamemodeInInt(int gamemode) {
        if(gamemode == 0) {
            return GameMode.SURVIVAL;
        } else if(gamemode == 1) {
            return GameMode.CREATIVE;
        } else if(gamemode == 2) {
            return GameMode.ADVENTURE;
        } else if(gamemode == 3) {
            return GameMode.SPECTATOR;
        } else {
            return null;
        }
    }
    /**
     * @param target the Target how get the Bill
     * @param amount amount of the Bill
     * @param from how send the Bill
     */
    public void createPay(OfflinePlayer target, int amount,Player from) {


        if(FrameMain.getInstance().getConfig().getBoolean("MYSQL.Boolean")) {
            MYSQL.connect();
            try {
                if(SQL.isTableExists("pays")) {
                    Statement statement = MYSQL.getConnection().createStatement();
                    ResultSet res = statement.executeQuery("SELECT * FROM pays WHERE PlayerTo = '" +target.getName() + "';");
                    if(res.next()) {
                        if(res.getString("PlayerTo") == null) {
                            statement.executeUpdate("INSERT INTO pays (PlayerTo,payAmount,PlayerFrom) VALUES ('"+target.getName()+ "','" + amount + "','" + from.getName() + "');");
                            MYSQL.close();
                        } else {
                            int x = res.getInt("payAmount");
                            x = x + amount;
                            statement.executeUpdate("UPDATE pays SET payAmount = '" + x +"' WHERE PlayerTo = '" + target.getName() + "'");
                            MYSQL.close();
                        }
                    } else {
                        statement.executeUpdate("INSERT INTO pays (PlayerTo,payAmount,PlayerFrom) VALUES ('"+target.getName()+ "','" + amount + "','" + from.getName() + "');");
                        MYSQL.close();
                    }
                } else {
                        SQL.createTable("pays","PlayerTo TEXT(11),payAmount INT,PlayerFrom TEXT");
                        Bukkit.getConsoleSender().sendMessage("§a[FrameAPI] §bBill was Createt to §a" + target.getName() + " §bin amount of §a" +amount);
                    MYSQL.close();
                }
            }catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public ArrayList<OfflinePlayer> getPaysTo(OfflinePlayer to) {
        MYSQL.connect();
        ArrayList<OfflinePlayer> playertop = new ArrayList<OfflinePlayer>();
        if(FrameMain.getInstance().getConfig().getBoolean("MYSQL.Boolean")) {
            if(IsTableExist.isExist("pays")) {
                try {
                    Statement statement = MYSQL.getConnection().createStatement();
                    ResultSet res = statement.executeQuery("SELECT * FROM pays WHERE PlayerTo = '" + to.getName() + "';");
                    while(res.next()){
                        String uuid = res.getString("PlayerFrom");
                        playertop.add(Bukkit.getOfflinePlayer(uuid));
                        MYSQL.close();
                        Bukkit.getConsoleSender().sendMessage(playertop.toString());
                        return playertop;
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                String sql = "CREATE TABLE IF NOT EXISTS pays(PlayerTo TEXT(11),payAmount INT, PlayerFrom TEXT);";
                try {
                    PreparedStatement stmt = (PreparedStatement) MYSQL.getConnection().prepareStatement(sql);
                    stmt.executeUpdate();
                    MYSQL.close();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return playertop;
    }
    /**
     * @param offline The Player ho get the Bill
     * @return ArrayList of Amounts
     */
    public Integer getPays(OfflinePlayer offline) {

        if(FrameMain.getInstance().getConfig().getBoolean("MYSQL.Boolean")) {
            MYSQL.connect();
            try {
                if(IsTableExist.isExist("pays")) {
                    Statement statement = MYSQL.getConnection().createStatement();
                    ResultSet res = statement.executeQuery("SELECT * FROM pays WHERE PlayerTo = '" + offline.getName() + "';");
                    if(res.next()) {
                        if(res.getInt("payAmount") == 0) {
                            MYSQL.close();
                            return null;
                        } else {
                            int x = res.getInt("payAmount");
                            MYSQL.close();
                            return x;
                        }
                    } else {
                        MYSQL.close();
                        return null;
                    }
                } else {
                    String sql = "CREATE TABLE IF NOT EXISTS pays(PlayerTo TEXT(11),payAmount INT, PlayerFrom TEXT);";
                    try {
                        PreparedStatement stmt = (PreparedStatement) MYSQL.getConnection().prepareStatement(sql);
                        stmt.executeUpdate();
                        MYSQL.close();
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                }
            } catch (SQLException e) {
            }
        }
        return null;
    }
    /**
     * @param from Player
     * @return From the Player
     */
    public ArrayList<OfflinePlayer> getPaysFrom(OfflinePlayer from) {
        MYSQL.connect();
        ArrayList<OfflinePlayer> playertop = new ArrayList<OfflinePlayer>();
        if(FrameMain.getInstance().getConfig().getBoolean("MYSQL.Boolean")) {
            if(IsTableExist.isExist("pays")) {
                try {
                    Statement statement = MYSQL.getConnection().createStatement();
                    ResultSet res = statement.executeQuery("SELECT * FROM pays WHERE PlayerFrom = '" + from.getName() + "';");
                    while(res.next()){
                        String uuid = res.getString("PlayerTo");
                        playertop.add(Bukkit.getOfflinePlayer(uuid));
                        MYSQL.close();
                        return playertop;
                    }
                } catch (SQLException e) {
                }
            } else {
                String sql = "CREATE TABLE IF NOT EXISTS pays(PlayerTo TEXT(11),payAmount INT, PlayerFrom TEXT);";
                try {
                    PreparedStatement stmt = (PreparedStatement) MYSQL.getConnection().prepareStatement(sql);
                    stmt.executeUpdate();
                    MYSQL.close();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return playertop;
    }
    /**
     * @param target how get the Bill
     * @param from from the Target how send the bill
     */
    public void getPaysDelete(Player target, OfflinePlayer from) {
        MYSQL.connect();
        if(FrameMain.getInstance().getConfig().getBoolean("MYSQL.Boolean")) {
            try {
                if(IsTableExist.isExist("pays")) {
                    Statement statement = MYSQL.getConnection().createStatement();
                    ResultSet res = statement.executeQuery("SELECT * FROM pays WHERE PlayerFrom = '" + from.getName() + "';");
                    res.next();
                    if(res.getString("PlayerFrom") == null) {
                    } else {
                        statement.execute("DELETE FROM pays WHERE PlayerTo = '" + target.getName() + "'");
                        MYSQL.close();
                    }
                } else {
                    String sql = "CREATE TABLE IF NOT EXISTS pays(PlayerTo TEXT(11),payAmount INT, PlayerFrom TEXT);";
                    try {
                        PreparedStatement stmt = (PreparedStatement) MYSQL.getConnection().prepareStatement(sql);
                        stmt.executeUpdate();
                        MYSQL.close();
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                }
            }catch (SQLException e) {
                e.printStackTrace();
            } finally {

            }
        }
    }

    /**
     *
     * @param materialname materialname
     * @return ItemStack
     */
    public ItemStack getItem(String materialname) {
        ItemStack item = new ItemStack(Material.getMaterial(materialname));
        return item;
    }
    public static ItemStack createItemBuilder(Material material,int amount,String displayname,boolean unbreakable) {
        return new ItemBuilder(material).setAmount(amount).setName(displayname).setUnbreakable(unbreakable).build();
    }
}
