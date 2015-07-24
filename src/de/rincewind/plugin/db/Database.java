package de.rincewind.plugin.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Consumer;

public abstract interface Database {

	public abstract void connect();
	
	public abstract void disconnect();
	
	public abstract void update(PreparedStatement stmt);
	
	public abstract void query(PreparedStatement stmt, Consumer<ResultSet> action);
	
	public abstract PreparedStatement prepare(String sql); //In MySQL async
	
	
	public static void setString(PreparedStatement stmt, int position, String param) {
		try {
			stmt.setString(position, param);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void setInt(PreparedStatement stmt, int position, int param) {
		try {
			stmt.setInt(position, param);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
