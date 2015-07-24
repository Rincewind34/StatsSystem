package de.rincewind.plugin;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import de.rincewind.command.exec.CommandStats;
import de.rincewind.listener.dyn.ListenerHandler;
import de.rincewind.listener.exec.ListenerDeath;
import de.rincewind.listener.exec.ListenerJoin;
import de.rincewind.plugin.db.StatsDatabase;
import de.rincewind.plugin.db.StatsStorable;

public class Main extends JavaPlugin {
	
	private static Main instance;
	private static InfoLayout layout;
	
	public static Main getInstance() {
		return Main.instance;
	}
	
	public static InfoLayout getLayout() {
		return Main.layout;
	}
	
	private StatsDatabase stats;
	
	private ListenerHandler handler;
	
	@Override
	public void onLoad() {
		Main.instance = this;
		Main.layout = new InfoLayout("Stats");
	}
	
	@Override
	public void onEnable() {
		ConsoleCommandSender sender = Bukkit.getConsoleSender();
		Main.layout.message(sender, Main.layout.clSec + "The plugin is enabled!");
		
		try {
			File dir = this.getDataFolder();
			
			if (!dir.exists()) {
				dir.mkdirs();
			}
			
			this.stats = new StatsDatabase(new File(dir, "stats.db").getAbsolutePath(), "minigame_");
			
			new CommandStats().create();
			
			this.handler = new ListenerHandler();
			this.handler.add(new ListenerJoin());
			this.handler.add(new ListenerDeath());
			this.handler.create();
			this.handler.register("bundle.game");
			
			Main.layout.message(sender, Main.layout.clSec + "Connecting to database...");
			this.stats.connect();
			Main.layout.message(sender, Main.layout.clSec + "Connected!");
			
			this.stats.setup();
			
			Main.layout.message(sender, Main.layout.clPos + "The plugin was enabled!");
		} catch (Exception ex) {
			ex.printStackTrace();
			Main.layout.message(sender, Main.layout.clNeg + "The plugin could not be enabled!");
		}
	}
	
	@Override
	public void onDisable() {
		ConsoleCommandSender sender = Bukkit.getConsoleSender();
		Main.layout.message(sender, Main.layout.clSec + "The plugin is disabled!");
		
		try {
			this.handler.destroy();
			
			Main.layout.message(sender, Main.layout.clSec + "Disconnecting from database...");
			this.stats.disconnect();
			Main.layout.message(sender, Main.layout.clSec + "Disconnected!");
			
			Main.layout.message(sender, Main.layout.clPos + "The plugin was disabled!");
		} catch (Exception ex) {
			Main.layout.message(sender, Main.layout.clNeg + "An error occured while disabling the plugin!");
		}
	}
	
	public StatsStorable getStats() {
		return this.stats;
	}
	
}
