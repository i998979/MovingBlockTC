package to.epac.factorycraft.MovingBlockTC;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import to.epac.factorycraft.MovingBlockTC.Commands.MBTCcommands;
import to.epac.factorycraft.MovingBlockTC.Events.SignalHandler;

public class Main extends JavaPlugin{
	public static Main instance;
	
	public void onEnable() {
		instance = this;
		
		PluginManager pm = getServer().getPluginManager();
		
		getCommand("mbtc").setExecutor(new MBTCcommands());
	
		
		SignalHandler.check();
	}

	public void onDisable() {
		instance = null;
	}

}
