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

	public List<Categories> findCategory() throws SQLException {

		List<Categories> categories = new ArrayList<Categories>();

		String query = "SELECT * FROM categories, categoriesRelation WHERE categories.id = categoriesRelation.id ORDER BY categoriesRelation.father, categories.id";
		
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
				category.setCategory(result.getString("category"));
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
	
	public int index(int idfather)throws SQLException {

		int size = 0;

		String query = "SELECT COUNT(*) FROM categoriesRelation WHERE father = " + idfather;
		
		// contiene risultati del database
		ResultSet result = null;
		
		// ottimizza i tempi di accesso e protezione e per non avere sequel injection
		PreparedStatement pstatement = null;
		
		try {
			pstatement = con.prepareStatement(query);

			result = pstatement.executeQuery();
			
			if(result.next()) {
				size = result.getInt(1);
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

		return size;
	}
	
	public void insertCategories(String name, String category) throws SQLException{
		String query = "INSERT INTO categories(category, name) VALUES (?, ?)";
		
		// contiene risultati del database
		ResultSet result = null;
		
		// ottimizza i tempi di accesso e protezione e per non avere sequel injection
		PreparedStatement pstatement = null;
		
		try {
			pstatement = con.prepareStatement(query);
			
			pstatement.setString(1, category);
			pstatement.setString(2, name);
			
			int rowsAffected = pstatement.executeUpdate();

            // Verifica se l'inserimento è stato eseguito con successo
            if (rowsAffected > 0) {
                System.out.println("Inserimento effettuato correttamente.");
            } else {
                System.out.println("Nessuna riga inserita.");
            }
		}catch(SQLException e){
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
	}
	
	public void insertRelation(Integer id, Integer father) throws SQLException{
		String query = "INSERT INTO categoriesRelation(id, father) VALUES (?, ?)";
		
		// contiene risultati del database
		ResultSet result = null;
		
		// ottimizza i tempi di accesso e protezione e per non avere sequel injection
		PreparedStatement pstatement = null;
		
		try {
			pstatement = con.prepareStatement(query);
			
			pstatement.setInt(1, id);
			pstatement.setInt(2, father);
			
			int rowsAffected = pstatement.executeUpdate();

            // Verifica se l'inserimento è stato eseguito con successo
            if (rowsAffected > 0) {
                System.out.println("Inserimento effettuato correttamente.");
            } else {
                System.out.println("Nessuna riga inserita.");
            }
		}catch(SQLException e){
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
	}
}