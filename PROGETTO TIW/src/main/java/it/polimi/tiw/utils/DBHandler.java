package it.polimi.tiw.utils;

import java.sql.*;
import javax.servlet.*;

public class DBHandler {
	
	/**
	 * 
	 * @param context Servlet context
	 * @return the connection with DB
	 * @throws UnavailableException
	 */
	public static Connection getConnection(ServletContext context) throws UnavailableException {
		Connection connection = null;
		try {
			String driver = context.getInitParameter("dbDriver");
			String url = context.getInitParameter("dbUrl");
			String user = context.getInitParameter("dbUser");
			String password = context.getInitParameter("dbPassword");
			Class.forName(driver);
			connection = DriverManager.getConnection(url, user, password);
		} catch (ClassNotFoundException e) {
			throw new UnavailableException("Can't load database driver");
		} catch (SQLException e) {
			throw new UnavailableException("Couldn't get db connection");
		}
		return connection;
	}
	 
	/**
	 * Close the connection
	 * @param connection
	 * @throws SQLException
	 */
	public static void closeConnection(Connection connection) throws SQLException {
		if (connection != null) {
			connection.close();
		}
	}
}