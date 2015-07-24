package de.rincewind.plugin.db;

import java.util.function.Consumer;

import org.bukkit.entity.Player;

public interface StatsStorable {

	public abstract void setKills(Player player, int value);
	
	public abstract void setDeaths(Player player, int value);
	
	public abstract void getKills(Player player, Consumer<Integer> action);
	
	public abstract void getDeaths(Player player, Consumer<Integer> action);
	
	public abstract void setupPlayer(Player player);
	
}