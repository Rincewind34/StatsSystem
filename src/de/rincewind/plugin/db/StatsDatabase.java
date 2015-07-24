package de.rincewind.plugin.db;

import java.sql.PreparedStatement;
import java.util.function.Consumer;

import org.bukkit.entity.Player;

public class StatsDatabase extends SQLiteDatabase implements StatsStorable {

	private String tablePrefix;
	private String tableStats;
	
	/*
	 * Database structure:
	 * username VARCHAR(16), kills INTEGER, deaths INTEGER
	 */
	
	public StatsDatabase(String dbName, String tablePrefix) {
		super(dbName);
		this.tablePrefix = tablePrefix;
		this.tableStats = this.tablePrefix + "stats";
	}
	
	@Override
	public void setKills(Player p, int value) {
		this.setupPlayer(p);
		
		PreparedStatement stmt = super.prepare("UPDATE " + this.tableStats + " SET kills = ? WHERE username = ?");
		
		Database.setInt(stmt, 1, value);
		Database.setString(stmt, 2, p.getName());
		
		super.update(stmt);
	}
	
	@Override
	public void setDeaths(Player p, int value) {
		this.setupPlayer(p);
		
		PreparedStatement stmt = super.prepare("UPDATE " + this.tableStats + " SET deaths = ? WHERE username = ?");
		
		Database.setInt(stmt, 1, value);
		Database.setString(stmt, 2, p.getName());
		
		super.update(stmt);
	}

	@Override
	public void getKills(Player p, Consumer<Integer> action) {
		this.setupPlayer(p);
		
		PreparedStatement stmt = super.prepare("SELECT kills FROM " + this.tableStats + " WHERE username = ?");
		
		Database.setString(stmt, 1, p.getName());
		
		super.query(stmt, (rs) -> {
			try {
				action.accept(rs.getInt("kills"));
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}
	
	@Override
	public void getDeaths(Player p, Consumer<Integer> action) {
		this.setupPlayer(p);
		
		PreparedStatement stmt = super.prepare("SELECT deaths FROM " + this.tableStats + " WHERE username = ?");
		
		Database.setString(stmt, 1, p.getName());
		
		super.query(stmt, (rs) -> {
			try {
				action.accept(rs.getInt("deaths"));
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	@Override
	public void setupPlayer(Player p) {
		PreparedStatement stmt = super.prepare("SELECT COUNT(*) AS `total` FROM " + this.tableStats + " WHERE username = ?");
		
		Database.setString(stmt, 1, p.getName());

		super.query(stmt, (rs) -> {
			try {
				if (rs.getInt("total") == 0) {
					this.createPlayer(p);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}
	
	public void setup() {
		PreparedStatement stmt = super.prepare("CREATE TABLE IF NOT EXISTS " + this.tableStats + " (username VARCHAR(16), kills INTEGER, deaths INTEGER)");
		
		super.update(stmt);
	}
	
	private void createPlayer(Player p) {
		PreparedStatement stmt = super.prepare("INSERT INTO " + this.tableStats + " (username, kills, deaths) VALUES (?, ?, ?)");
		
		Database.setString(stmt, 1, p.getName());
		Database.setInt(stmt, 2, 0);
		Database.setInt(stmt, 3, 0);
		
		super.update(stmt);
	}
}