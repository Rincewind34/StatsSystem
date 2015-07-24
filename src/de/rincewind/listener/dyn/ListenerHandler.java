package de.rincewind.listener.dyn;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

import de.rincewind.plugin.Main;

public class ListenerHandler {

	private List<Listener> listeners;
	
	public ListenerHandler() {
		this.listeners = new ArrayList<>();
	}
	
	public void register(String name) {
		for (Listener listener : this.listeners) {
			if (listener instanceof DefaultListener) {
				((DefaultListener) listener).enableBundle(name);
			}
		}
	}
	
	public void unregister(String name) {
		for (Listener listener : this.listeners) {
			if (listener instanceof DefaultListener) {
				((DefaultListener) listener).disableBundle(name);
			}
		}
	}
	
	public void create() {
		for (Listener listener : this.listeners) {
			Bukkit.getPluginManager().registerEvents(listener, Main.getInstance());
		}
	}
	
	public void destroy() {
		for (Listener listener : this.listeners) {
			HandlerList.unregisterAll(listener);
		}
	}
	
	public void add(Listener listener) {
		this.listeners.add(listener);
	}
	
	@Override
	public String toString() {
		String result = "[";
		
		for (Listener listener : this.listeners) {
			result = result + listener.getClass().getName() + (listener instanceof DefaultListener ? ":" + ((DefaultListener) listener).getEnabledBundles().toString() : "") + ",";
		}
		
		return result.substring(0, result.length() - 1) + "]";
	}
	
}
