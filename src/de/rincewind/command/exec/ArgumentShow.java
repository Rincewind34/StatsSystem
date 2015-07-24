package de.rincewind.command.exec;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.rincewind.command.dyn.Argument;
import de.rincewind.plugin.InfoLayout;
import de.rincewind.plugin.Main;

public class ArgumentShow extends Argument<Main> {

	public ArgumentShow() {
		super(Main.getInstance());
	}

	@Override
	public String getSyntax() {
		return "/stats show [player]";
	}

	@Override
	public String getPermission() {
		return "stats.show";
	}

	@Override
	public boolean isOnlyForPlayer() {
		return true;
	}

	@Override
	public boolean execute(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length == 1) {
			ArgumentShow.sendStats((Player) sender);
			return true;
		} else if (args.length == 2) {
			if (Bukkit.getPlayerExact(args[1]) != null) {
				Player from = Bukkit.getPlayerExact(args[1]);
				ArgumentShow.sendStats((Player) sender, from);
				return true;
			} else {
				Main.getLayout().message(sender, Main.getLayout().clNeg + "This player is not online!");
				return true;
			}
		} else {
			return false;
		}
	}
	
	public static void sendStats(Player player) {
		ArgumentShow.sendStats(player, player);
	}
	
	public static void sendStats(Player player, Player from) {
		InfoLayout layout = Main.getLayout().clone();
		layout.newCategory("Stats");
		
		Main.getInstance().getStats().getKills(from, (kills) -> {
			layout.addComent("Kills: " + kills, false);
			
			Main.getInstance().getStats().getDeaths(from, (deaths) -> {
				layout.addComent("Deaths: " + deaths, false);
				layout.newBarrier();
				layout.send(player);
			});
		});
	}
	
}
