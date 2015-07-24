package de.rincewind.listener.exec;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;

import de.rincewind.listener.dyn.DefaultListener;
import de.rincewind.listener.dyn.ListenerBundle;
import de.rincewind.plugin.Main;
import de.rincewind.plugin.db.StatsStorable;

public class ListenerDeath extends DefaultListener {

	public ListenerDeath() {
		super(ListenerDeath.class, PlayerDeathEvent.getHandlerList());
	}

	@ListenerBundle(name = "bundle.game")
	private static void onDeath(PlayerDeathEvent event) {
		Player player = event.getEntity();
		StatsStorable stats = Main.getInstance().getStats();
		
		stats.getDeaths(player, (deaths) -> {
			stats.setDeaths(player, deaths + 1);
		});
		
		if (player.getKiller() != null) {
			Player killer = player.getKiller();
			
			stats.getKills(killer, (kills) -> {
				stats.setKills(killer, kills + 1);
			});
		}
	}
	
}
