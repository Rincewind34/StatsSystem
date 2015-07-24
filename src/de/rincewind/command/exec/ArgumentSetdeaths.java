package de.rincewind.command.exec;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.rincewind.command.dyn.Argument;
import de.rincewind.plugin.Main;

public class ArgumentSetdeaths extends Argument<Main> {

	public ArgumentSetdeaths() {
		super(Main.getInstance());
	}

	@Override
	public String getSyntax() {
		return "/stats setdeaths <count> [player]";
	}

	@Override
	public String getPermission() {
		return "stats.set.deaths";
	}

	@Override
	public boolean isOnlyForPlayer() {
		return true;
	}

	@Override
	public boolean execute(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length == 2) {
			int i;
			
			try {
				i = Integer.parseInt(args[1]);
			} catch (Exception ex) {
				Main.getLayout().message(sender, Main.getLayout().clNeg + "Invalid count!");
				return true;
			}
			
			Main.getInstance().getStats().setDeaths((Player) sender, i);
			Main.getLayout().message(sender, Main.getLayout().clPos + "The deaths will be set!");
			return true;
		} else if (args.length == 3) {
			if (Bukkit.getPlayerExact(args[2]) != null) {
				Player from = Bukkit.getPlayerExact(args[2]);
				int i;
				
				try {
					i = Integer.parseInt(args[1]);
				} catch (Exception ex) {
					Main.getLayout().message(sender, Main.getLayout().clNeg + "Invalid count!");
					return true;
				}
				
				Main.getInstance().getStats().setDeaths(from, i);
				Main.getLayout().message(sender, Main.getLayout().clPos + "The deaths will be set!");
				return true;
			} else {
				Main.getLayout().message(sender, Main.getLayout().clNeg + "This player is not online!");
				return true;
			}
		} else {
			return false;
		}
	}
	
}
