package de.rincewind.plugin.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

import de.rincewind.plugin.StatsException;

public class SQLiteDatabase implements Database {

	private static final ExecutorService POOL;
	
	static {
		POOL = Executors.newCachedThreadPool();
	}
	
	private String dbName;
	private Connection connection;
	
	public SQLiteDatabase(String dbName) {
		this.dbName = dbName;
	}

	@Override
	public void connect() {
		try {
			// ================= MySQL =================
			// Class.forName("com.mysql.jdbc.Driver");
			// DriverManager.getConnection("jdbc:mysql://%host%:%port%/%db%", "%user%", "%password%");
			// ================= MySQL =================
			
			Class.forName("org.sqlite.JDBC");
			this.connection = DriverManager.getConnection("jdbc:sqlite:" + this.dbName);
		} catch (SQLException e) {
			throw new StatsException(e.getMessage());
		} catch (ClassNotFoundException e) {
			throw new StatsException("SQLite-Driver not found at runtime!");
		}
	}

	@Override
	public void disconnect() {
		if (this.connection != null) {
			try {
				this.connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void update(PreparedStatement stmt) {
		SQLiteDatabase.POOL.execute(() -> {
			try {
				stmt.executeUpdate();
				stmt.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	@Override
	public void query(PreparedStatement stmt, Consumer<ResultSet> action) {
		SQLiteDatabase.POOL.execute(() -> {
			try {
				ResultSet rs = stmt.executeQuery();
				action.accept(rs);
				stmt.close();
				rs.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	@Override
	public PreparedStatement prepare(String sql) {
		try {
			return this.connection.prepareStatement(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}
}