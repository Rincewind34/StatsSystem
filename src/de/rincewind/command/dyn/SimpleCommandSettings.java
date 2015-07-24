package de.rincewind.command.dyn;

import de.rincewind.plugin.InfoLayout;
import de.rincewind.plugin.Main;

public final class SimpleCommandSettings implements CommandSettings {

	private final String messagePrefix;
	private final String messageNoPermission;
	private final String messageSyntax;
	private final String messageOnlyPlayer;
	private final String messageDefault;
	
	private final InfoLayout layout;

	public SimpleCommandSettings() {
		this.layout = Main.getLayout();

		this.messagePrefix = this.layout.prefix;
		this.messageNoPermission = this.layout.clNeg + "You do not have the permission to do that!";
		this.messageSyntax = this.layout.clNeg + "Syntax: %s";
		this.messageOnlyPlayer = this.layout.clNeg + "You have to be a player";
		this.messageDefault = this.layout.clNeg + "Use /stats to get the help!";
	}
	
	@Override
	public String getMessagePrefix() {
		return this.messagePrefix;
	}

	@Override
	public String getMessageNoPermission() {
		return this.messageNoPermission;
	}

	@Override
	public String getMessageSyntax() {
		return this.messageSyntax;
	}

	@Override
	public String getMessageOnlyPlayer() {
		return this.messageOnlyPlayer;
	}

	@Override
	public String getMessageDefault() {
		return this.messageDefault;
	}
}
