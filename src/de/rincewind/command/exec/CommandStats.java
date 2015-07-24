package de.rincewind.command.exec;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.rincewind.command.dyn.Argument;
import de.rincewind.command.dyn.BasicCommand;
import de.rincewind.command.dyn.BasicCommand.DefaultExecutor;
import de.rincewind.command.dyn.SimpleCommandSettings;
import de.rincewind.plugin.InfoLayout;
import de.rincewind.plugin.Main;

public class CommandStats extends BasicCommand implements DefaultExecutor {

	private InfoLayout layout;
	
	public CommandStats() {
		super("stats", new SimpleCommandSettings(), Main.getInstance());
		this.layout = Main.getLayout();
		
		super.setDescription(this.layout.clSec + "Command to show and edit stats!");
		super.setDefaultExecutor(this);
		super.setPermission(null);
		super.registerArgument("show", new ArgumentShow());
		super.registerArgument("setkills", new ArgumentSetkills());
		super.registerArgument("setdeaths", new ArgumentSetdeaths());
	}

	@Override
	public boolean onExecute(CommandSender sender, Command cmd, String label, String[] args) {
		InfoLayout layout = this.layout.clone();
		
		layout.newCategory("CommandInfo");
		
		for (Argument<?> arg : this.getArguments().values()) {
			layout.addComent(arg.getSyntax(), false);
		}
		
		if (sender instanceof Player) {
			layout.newCategory("Stats");
			
			Main.getInstance().getStats().getKills((Player) sender, (kills) -> {
				layout.addComent("Kills: " + kills, false);
				
				Main.getInstance().getStats().getDeaths((Player) sender, (deaths) -> {
					layout.addComent("Deaths: " + deaths, false);
					layout.newBarrier();
					layout.send(sender);
				});
			});
		} else {
			layout.newBarrier();
			layout.send(sender);
		}
		
		return true;
	}

}
