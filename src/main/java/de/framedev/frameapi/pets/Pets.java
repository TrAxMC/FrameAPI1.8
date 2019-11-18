/**
 * Dies ist ein Plugin von FrameDev
 * Bitte nichts ?ndern, @Copyright by FrameDev 
 */
package de.framedev.frameapi.pets;

import de.framedev.frameapi.main.FrameMain;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftCreature;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Random;

/**
 * @author Darryl
 *
 */
public class Pets implements Listener {
	private static double speed;
	/**
	 * removePet
	 * @param player Player
	 */
	/**
	 * 
	 */
	public Pets() {
	}
	public void RemovePet(Player player) {
		
	if(Pet.containsKey(player.getName())) {
			Pet.get(player.getName()).remove();
		}
	}
	public static HashMap<String, Entity> Pet = new HashMap<String, Entity>();
		/**
		 * @param player Player
		 * @param type Entity Type
		 * @param name EntityName
		 * @param speed EntityWalk Speed
		 * @param cangetBabies Babies yes or no?
		 */
	public void createPet(Player player, EntityType type, String name, double speed, boolean cangetBabies) {
			
		Entity entity = player.getWorld().spawnEntity(player.getLocation(), type);
		entity.setCustomName(name);
		entity.setCustomNameVisible(true);
		Pet.put(player.getName(), entity);
		Pets.speed = speed;
		getBabies(player, cangetBabies);
		
	}
		/**
		 * @param creature Creature
		 * @param player Player
		 * @param speed pet speed
		 */
	@SuppressWarnings( {"DuplicateBranchesInSwitch", "deprecation"} )
	public void walktoLocation(Creature creature, Player player, double speed) {
			
		Location location = player.getLocation();
		Pets.speed = speed;
		Random rnd = new Random();
		int zufall = rnd.nextInt(6);
		switch(zufall) {
		case 0:
			location.add(1,0,1);
			break;
		case 1:
				location.add(0,0,1);
				break;
			case 2:
				location.add(1,0,0);
				break;
			case 3:
				location.subtract(1,0,1);
				break;
			case 4:
				location.subtract(0,0,1);
				break;
			case 5:
				location.subtract(1,0,1);
				break;
			}
			if(location.distanceSquared(creature.getLocation()) > 100) {
				if(!player.isOnGround()) {
					return;
				}
				creature.teleport(location);
			} else {
		((CraftCreature)creature).getHandle().getNavigation().a(location.getX(), location.getY(), location.getZ(), speed);
		}
	}
	/**
	 * @param e PlayerQuitEvent
	 */
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		
		Player player = (Player) e.getPlayer();
		if(Pet.containsKey(player.getName())) {
			Pet.get(player.getName()).remove();
		}
	}
	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		if(Pet.containsKey(e.getPlayer().getName())) {
			double speed;
			speed = Pets.speed;
			walktoLocation((Creature)Pet.get(e.getPlayer().getName()), e.getPlayer(), speed);
		}
	}
	@EventHandler
	public void EntityDamageEvent(EntityDamageByEntityEvent e) {
		Entity entity = e.getEntity();
		if(Pet.containsValue(entity)) {
			e.setCancelled(true);
		}
	}
	public void getBabies(Player player, Boolean baby) {
		if(baby == true) {
			new BukkitRunnable() {
				
				@Override
				public void run() {
					World world = player.getWorld();
					world.spawnEntity(player.getLocation(), Pet.get(player.getName()).getType());
					player.sendMessage("?aYou get a Baby");
					
				}
			}.runTaskTimer(FrameMain.getInstance(), 600, 600);
		}
		
	}

}
