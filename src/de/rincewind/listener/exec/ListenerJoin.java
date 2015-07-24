package de.rincewind.listener.exec;

import org.bukkit.event.player.PlayerJoinEvent;

import de.rincewind.listener.dyn.DefaultListener;
import de.rincewind.listener.dyn.ListenerBundle;
import de.rincewind.plugin.Main;
import de.rincewind.plugin.db.StatsStorable;

public class ListenerJoin extends DefaultListener {

	public ListenerJoin() {
		super(ListenerJoin.class, PlayerJoinEvent.getHandlerList());
	}
	
	@ListenerBundle(name = "bundle.game")
	private static void onJoin(PlayerJoinEvent event) {
		StatsStorable database = Main.getInstance().getStats();
		database.setupPlayer(event.getPlayer());
	}

}
