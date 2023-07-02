package it.polimi.tiw.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polimi.tiw.data.userData;

public class UsersDAO {
	private Connection con;

	public UsersDAO(Connection connection) {
		this.con = connection;
	}

	public List<userData> findUser() throws SQLException {

		List<userData> users = new ArrayList<userData>();

		String query = "SELECT * FROM users";
		
		// contiene risultati del database
		ResultSet result = null;
		
		// ottimizza i tempi di accesso e protezione e per non avere sequel injection
		PreparedStatement pstatement = null;
		try {
			pstatement = con.prepareStatement(query);

			result = pstatement.executeQuery();
			
			// muove puntatore avanti di uno
			while (result.next()) {
				userData user = new userData();
				user.setId(result.getInt("id"));
				user.setUsername(result.getString("username"));
				user.setName(result.getString("name"));
				user.setSurname(result.getString("surname"));
				user.setRegion(result.getString("region"));
				user.setPassword(result.getString("password"));
				users.add(user);
			}
		} catch (SQLException e) {
			throw e;

		} finally {
			try {
				if (result != null)
					result.close();
			} catch (SQLException e1) {
				throw e1;
			}
			try {
				if (pstatement != null)
					pstatement.close();
			} catch (SQLException e2) {
				throw e2;
			}
		}

		return users;
	}

}