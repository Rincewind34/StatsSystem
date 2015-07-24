package de.rincewind.plugin;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public class InfoLayout {
	
	public final List<String> lines;
	
	public final String clPri;
	public final String clSec;
	public final String clPos;
	public final String clNeg;
	public final String clHiLi;
	
	public final String prefix;
	
	private final String name;
	
	public InfoLayout(String name) {
		Validate.notNull(name, "The name cannot be null!");
		
		this.name = name;
		
		this.lines = new ArrayList<String>();
		this.clPri = "§8";
		this.clSec = "§3";
		this.clPos = "§f";
		this.clNeg = "§c";
		this.clHiLi = "§b";
		this.prefix = this.clPri + "[" + this.clSec + name + this.clPri + "] §r";
	}
	
	public List<String> getLines() {
		return this.lines;
	}
	
	public String getPrefix() {
		return this.prefix;
	}
	
	public void newCategory(String name) {
		this.newBarrier();
		this.lines.add(this.prefix + this.clHiLi + name);
	}
	
	public void newBarrier() {
		this.lines.add(this.prefix + this.clPri + "===============================================");
	}
	
	public void addInfo(String name, String info, boolean positiv) {
		this.lines.add(this.prefix + this.clSec + name + ": " + ((positiv) ? this.clPos : this.clNeg) + info);
	}
	
	public void addInfo(String name, boolean element) {
		this.lines.add(this.prefix + this.clSec + name + ": " + ((element) ? this.clPos : this.clNeg) + element);
	}
	
	public void addElement(String element, boolean positiv) {
		this.lines.add(this.prefix + ((positiv) ? this.clPos : this.clNeg) + element);
	}
	
	public void addComent(String element, boolean highlight) {
		this.lines.add(this.prefix + ((highlight) ? this.clHiLi : this.clSec) + element);
	}
	
	public void send(CommandSender sender) {
		for (String line : this.lines) {
			sender.sendMessage(line);
		}
	}
	
	public void message(CommandSender sender, String msg) {
		sender.sendMessage(this.prefix + msg);
	}
	
	public void broadcast(String msg) {
		Bukkit.broadcastMessage(this.prefix + msg);
	}
	
	public InfoLayout clone() {
		return new InfoLayout(this.name);
	}
	
}
