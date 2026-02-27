package henrique.igor.mini_erp.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
	
	private static ConnectionFactory instance;
	
	private static final String URL = "jdbc:oracle:thin:@//localhost:1521/XEPDB1";
	private static final String USER = "SALES_ORDER_SYSTEM";
	private static final String PASSWORD = "admin";
	
	private ConnectionFactory() {
		try {
			Class.forName("oracle.jdbc.OracleDriver");
		} catch (ClassNotFoundException ex) {
			throw new RuntimeException("Drive Oracle não encontrado", ex);
		}
	}
	
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
