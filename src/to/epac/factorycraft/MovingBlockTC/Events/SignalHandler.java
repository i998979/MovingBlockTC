package to.epac.factorycraft.MovingBlockTC.Events;

import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.Plugin;

import com.bergerkiller.bukkit.tc.controller.MinecartGroup;
import com.bergerkiller.bukkit.tc.controller.MinecartGroupStore;
import com.bergerkiller.bukkit.tc.controller.MinecartMember;
import com.bergerkiller.bukkit.tc.controller.MinecartMemberStore;
import com.bergerkiller.bukkit.tc.properties.TrainProperties;
import com.bergerkiller.bukkit.tc.utils.TrackIterator;

import to.epac.factorycraft.MovingBlockTC.Main;

public class SignalHandler {
	public static int DEFAULT_CHECK_DISTANCE = 10;
	
	
	private static Plugin plugin = Main.instance;
	
	public static void check() {
	    Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
	        public void run() {
	        	handleSignal();
	        }
	    }, 0L, 10L);
	}
	
	public static void handleSignal() {
    	for(World world : Bukkit.getServer().getWorlds()){
            for(Entity entity : world.getEntities()){
                if (entity.getType() == EntityType.MINECART ||
                		entity.getType() == EntityType.MINECART_CHEST ||
                		entity.getType() == EntityType.MINECART_COMMAND ||
                		entity.getType() == EntityType.MINECART_FURNACE ||
                		entity.getType() == EntityType.MINECART_HOPPER ||
                		entity.getType() == EntityType.MINECART_MOB_SPAWNER) {
                	
                	if (hasMovingBlockSupport(entity) == true) {
                		//Bukkit.broadcastMessage("SUPPORT!");
                		MinecartGroup group = MinecartGroupStore.get(entity);
                		MinecartMember<?> head = group.head();
            			BlockFace dir = group.head().getDirection();
            			int distance = DEFAULT_CHECK_DISTANCE;
            			TrainProperties prop = group.getProperties();
            			
            			Double launchDistance = 1.0;
            			BlockFace launchDirection = group.head().getDirection();
            			
                        double speedUnclipped = prop.getHolder().getAverageForce();
                        
                        // This is the real current train speed
                        double speedClipped = Math.min(speedUnclipped, prop.getSpeedLimit());
            			
            			//Double launchVelocity = prop.getSpeedLimit();
            			//Double launchVelocity = 0.3;
            			Double launchVelocity = speedClipped;
            			if (launchVelocity == 0) launchVelocity = prop.getSpeedLimit();
            			
            			Long delay = 2L * 20;
                		
                        if (handleOccupied(head.getBlock(), dir, head, distance)) {
                        	group.getActions().clear();
                        	group.head().getActions().addActionWaitOccupied(distance, delay, launchDistance, launchDirection, launchVelocity);
                        }
                	}

                }
            }
    	}
	}
	public static boolean hasMovingBlockSupport(Entity entity) {
    	MinecartGroup group = MinecartGroupStore.get(entity);
    	Collection<String> prop = group.getProperties().getTags();
    	//Bukkit.broadcastMessage("" + prop.toString());
    	
    	if (prop.toString().contains("MovingBlock")) {
    		//Location loc = entity.getLocation();
    		//Bukkit.broadcastMessage("Train at " + loc + "has Seltrac Moving Block support!");
    		return true;
    	}
    	return false;
	}
	
    public static boolean handleOccupied(Block start, BlockFace direction, MinecartMember<?> ignore, int maxdistance) {
    	
    	//Bukkit.broadcastMessage(ChatColor.AQUA + "0 xxxxx. ");
        TrackIterator iter = new TrackIterator(start, direction);
        
        while (iter.hasNext() && --maxdistance >= 0) {
        	
            //Bukkit.broadcastMessage(ChatColor.AQUA + "1 start. " + start);
            //Bukkit.broadcastMessage(ChatColor.YELLOW + "2 dir. " + direction);
            //Bukkit.broadcastMessage(ChatColor.RED + "3 ignore. " + ignore);
            //Bukkit.broadcastMessage(ChatColor.GREEN + "4 maxdis. " + maxdistance);
            //Bukkit.broadcastMessage(ChatColor.GOLD + "5 iter. " + iter);
            //Bukkit.broadcastMessage(ChatColor.BLUE + "5A iter.getNext. " + iter.next());
            
            MinecartMember<?> mm = MinecartMemberStore.getAt(iter.next());
            //Bukkit.broadcastMessage(ChatColor.DARK_PURPLE + "6 mm. . " + mm);
            
            if (mm != null && mm.getGroup() != ignore.getGroup()) {
                ignore.setIgnoreCollisions(true);
                return true;
            }
        }
        ignore.setIgnoreCollisions(false);
        return false;
    }
}
