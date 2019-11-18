/**
 * Dies ist ein Plugin von FrameDev
 * Bitte nichts ?ndern, @Copyright by FrameDev 
 */
package de.framedev.frameapi.warps;

import de.framedev.frameapi.api.API;
import de.framedev.frameapi.main.FrameMain.FrameMainGet;
import org.bukkit.Location;
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
public class WarpSigns implements Listener {
	

	@EventHandler
	public void signChange(SignChangeEvent e) {
			if(e.getLine(0).equalsIgnoreCase("warp")) {
				if(e.getPlayer().hasPermission("frameapi.signs.create")) {
				String[] args = e.getLines();
				String name = args[1];
				e.setLine(0, "?1[Warp]");
				e.setLine(1, name);
				} else {
					e.getPlayer().sendMessage(FrameMainGet.getPrefix() + " " + FrameMainGet.getNoPerm());
				}
			
		}
	}
	@EventHandler
	public void onInteractFree(PlayerInteractEvent e) {
			if(e.getAction() == Action.RIGHT_CLICK_BLOCK) {
				if(e.getClickedBlock().getState() instanceof Sign) {
					Sign s = (Sign) e.getClickedBlock().getState();
					if(s.getLine(0).equalsIgnoreCase("?1[Warp]")) {
						if(e.getPlayer().hasPermission("frameapi.signs.use")) {
						String[] args = s.getLines();
						String name = args[1];
						Location loc = API.Warp.getWarpLocation(name);
						if(s.getLine(1).equalsIgnoreCase(name)) {
								e.getPlayer().teleport(loc);
						}
						} else {
							e.getPlayer().sendMessage(FrameMainGet.getPrefix() + " " + FrameMainGet.getNoPerm());
						}
					
				}
			}
		}
	}
}
