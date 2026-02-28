package henrique.igor.mini_erp.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import henrique.igor.mini_erp.util.ConfigLoader;

public class ConnectionFactory {
	
	private static volatile ConnectionFactory instance;
	
	private static final String URL = ConfigLoader.get("db.url");
	private static final String USER = ConfigLoader.get("db.user");
	private static final String PASSWORD = ConfigLoader.get("db.password");
	
	private ConnectionFactory() {}
	
	public static ConnectionFactory getInstance() {
		if (instance == null) {
			synchronized (ConnectionFactory.class) {
				if (instance == null) {
			instance = new ConnectionFactory();
				}
			}
		}
		return instance;
	}
	
	public Connection getConnection() throws SQLException{
		return DriverManager.getConnection(URL, USER, PASSWORD);
	}
}
