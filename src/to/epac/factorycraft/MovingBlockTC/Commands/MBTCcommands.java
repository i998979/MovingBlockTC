package to.epac.factorycraft.MovingBlockTC.Commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.bergerkiller.bukkit.tc.controller.MinecartGroup;
import com.bergerkiller.bukkit.tc.controller.MinecartMember;
import com.bergerkiller.bukkit.tc.properties.CartProperties;
import com.bergerkiller.bukkit.tc.properties.TrainProperties;

import to.epac.factorycraft.MovingBlockTC.Events.SignalHandler;

public class MBTCcommands implements CommandExecutor{	
	Player player;

	CartProperties cprop;
	TrainProperties prop;
	MinecartGroup group;
	
	double speedUnclipped;
	double speedClipped;
	double speedMomentum;
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
			player = (Player) sender;
		} else return false;
		
		if (args.length == 0) {
			sender.sendMessage("Help page will be displayed here.");
			sender.sendMessage("/mbtc setcurrentspeed [distance] [velocity]");
			sender.sendMessage("distance: How many blocks the train have to walk before it reach the target speed");
			sender.sendMessage("Same as Launch sign line 2 (launch 5)");
			sender.sendMessage("Which means that the train have to walk 5 block to reach the target speed");
			sender.sendMessage("Velocity: Target speed");
			sender.sendMessage("Same as Launcher sign line 3");
		}
		
		else if (args[0].equalsIgnoreCase("getproperties")) {
			prop = CartProperties.getEditing(player).getTrainProperties();
			Bukkit.broadcastMessage(ChatColor.AQUA + "Train properties of the currently riding train: " + prop);
		}
		
		else if (args[0].equalsIgnoreCase("setcurrentspeed")) {
			if (args[1] == null) {
				sender.sendMessage(ChatColor.RED + "Please enter the distance before reaching the target speed.");
				return false;
			} else if (args[2] == null) {
				sender.sendMessage(ChatColor.RED + "Please enter the target speed.");
				return false;
			}
			
			prop = CartProperties.getEditing(player).getTrainProperties();
			group = prop.getHolder();
			
            Double distance = Double.valueOf(args[1]);
            Double velocity = Double.valueOf(args[2]);
			
            speedUnclipped = prop.getHolder().getAverageForce();
            // This is the real current train speed
            speedClipped = Math.min(speedUnclipped, prop.getSpeedLimit());
            speedMomentum = (speedUnclipped - speedClipped);
			
            player.sendMessage("speedUnclipped: " +speedUnclipped +
            		"# speedClipped: " + speedClipped +
            		"# speedMomentum: " + speedMomentum);
            
            group.head().getActions().addActionLaunch(group.head().getDirection(), distance, velocity);
		}

		else if (args[0].equalsIgnoreCase("getcurrentspeed")) {
			prop = CartProperties.getEditing(player).getTrainProperties();
			group = prop.getHolder();
			
			
            speedUnclipped = prop.getHolder().getAverageForce();
            // This is the real current train speed
            speedClipped = Math.min(speedUnclipped, prop.getSpeedLimit());
            speedMomentum = (speedUnclipped - speedClipped);
			
            player.sendMessage("speedUnclipped: " +speedUnclipped +
            		"# speedClipped: " + speedClipped +
            		"# speedMomentum: " + speedMomentum);
		}
		
		else if (args[0].equalsIgnoreCase("getmaxspeed")) {
			prop = CartProperties.getEditing(player).getTrainProperties();
			
			sender.sendMessage(ChatColor.YELLOW + "Train's max speed (Speed Limit): " + ChatColor.WHITE + prop.getSpeedLimit());
		}
		
		else if (args[0].equalsIgnoreCase("setmaxspeed")) {
			if (args[1] == null) {
				sender.sendMessage(ChatColor.RED + "Please enter the Speed Limit.");
				return false;
			}
			prop = CartProperties.getEditing(player).getTrainProperties();
			prop.setSpeedLimit(Double.valueOf(args[1]));
		}
		
		else if (args[0].equalsIgnoreCase("getlength")) {
			prop = CartProperties.getEditing(player).getTrainProperties();
			group = prop.getHolder();
			
			sender.sendMessage("Train length: " + group.size());
		}
		else if (args[0].equalsIgnoreCase("hasmovingblock")) {
			SignalHandler.handleSignal();
		}
		
		else if (args[0].equalsIgnoreCase("checkdistance")) {
			//CartProperties cprop = null;
			//TrainProperties prop;
			
			if (CartProperties.getEditing(player) == null) {
				CartProperties.setEditing(player, cprop);
				Bukkit.broadcastMessage(ChatColor.AQUA + "NULL" + cprop);
			} else {
				cprop = CartProperties.getEditing(player);
				Bukkit.broadcastMessage(ChatColor.YELLOW + "NOT NULL" + cprop);
				
			}
			
			Bukkit.broadcastMessage("cprop: " + cprop);
			
			prop = cprop.getTrainProperties();
			Bukkit.broadcastMessage("prop: " + prop);
			
			group = prop.getHolder();
			
			MinecartMember<?> head = group.head();
			
			BlockFace dir = group.head().getDirection();
			int distance = Integer.valueOf(args[1]);
			
			Double launchDistance = 1.0;
			BlockFace launchDirection = group.head().getDirection();
			
            speedUnclipped = prop.getHolder().getAverageForce();
            // This is the real current train speed
            speedClipped = Math.min(speedUnclipped, prop.getSpeedLimit());
			Double launchVelocity = speedClipped;
			
			if (launchVelocity == 0) launchVelocity = prop.getSpeedLimit();
			Long delay = 2L * 20;
			
            //distance
            if (SignalHandler.handleOccupied(head.getBlock(), dir, head, distance)) {
            	group.getActions().clear();
            	group.head().getActions().addActionWaitOccupied(distance, delay, launchDistance, launchDirection, launchVelocity);
            }
        }
		return false;
	}
}
