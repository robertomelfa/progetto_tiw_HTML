package it.polimi.tiw.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polimi.tiw.data.Categories;

public class CategoriesDAO {
	private Connection con;

	public CategoriesDAO(Connection connection) {
		this.con = connection;
	}

	public List<Categories> findUser() throws SQLException {

		List<Categories> categories = new ArrayList<Categories>();

		String query = "SELECT * FROM categories";
		
		// contiene risultati del database
		ResultSet result = null;
		
		// ottimizza i tempi di accesso e protezione e per non avere sequel injection
		PreparedStatement pstatement = null;
		try {
			pstatement = con.prepareStatement(query);

			result = pstatement.executeQuery();
			
			// muove puntatore avanti di uno
			while (result.next()) {
				Categories category = new Categories();
				category.setId(result.getInt("id"));
				category.setCategory(result.getInt("category"));
				category.setName(result.getString("name"));
				category.setFather(result.getInt("father"));
				categories.add(category);
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

		return categories;
	}
}