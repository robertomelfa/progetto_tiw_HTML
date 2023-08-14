package it.polimi.tiw.controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import it.polimi.tiw.dao.CategoriesDAO;
import it.polimi.tiw.data.Categories;
import it.polimi.tiw.utils.DBHandler;

/**
 * Servlet implementation class PasteCategory
 */
@WebServlet("/PasteCategory")
public class PasteCategory extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
	private Connection connection = null;
	
	/**
	 * Initialize the connection with the DB and the template for thymeleaf
	 */
	public void init() throws ServletException {
		
		ServletContext servletContext = getServletContext();
		ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(servletContext);
		templateResolver.setTemplateMode(TemplateMode.HTML);
		this.templateEngine = new TemplateEngine();
		this.templateEngine.setTemplateResolver(templateResolver);
		templateResolver.setSuffix(".html");
		
		connection = DBHandler.getConnection(getServletContext());
	}
	
	/**
	 * Constructor of the class
	 */
    public PasteCategory() {
        super();
    }

	/**
	 * Method doGet: paste the categories copied in the DB
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		Integer id  = Integer.parseInt(request.getParameter("id"));
		String cat = request.getParameter("category");
		
		
		CategoriesDAO categoriesDAO = new CategoriesDAO(connection);
		List<Categories> categories = new ArrayList<Categories>();
		
		// check if session is active
		HttpSession session = request.getSession();
		if (session.getAttribute("user") == null) {
			response.sendRedirect("index.html");
			return;
		}
		
		// get catagories from DB
		try {
			categories = categoriesDAO.findCategory();
		}catch (SQLException e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Issue when reading categories from db");
			return;
		}
		
		boolean check;
		
		List<Categories> categoriesCopied = new ArrayList<Categories>();
		Categories father = new Categories();
		
		// search where to copy the list of categories
		
		boolean check_cat = false;
		boolean check_father = false;
		
		if(id == 0) {
			check_father = true;
		}
		for(Categories category : categories) {
			if(category.getID() == id) {
				father = category;
				check_father = true;
			}
			if(category.getCategory().equals(cat)) {
				check_cat = true;
			}
		}
		
		if(check_cat == false || check_father == false) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Category not found");
			return;
		}
		
		
		// if father has 9 children -> error
		
		try {
			if(categoriesDAO.index(father.getID()) >= 9) {
				response.sendRedirect("GoHome?errorMsg=Can't paste here. Father has already 9 categories");
				return;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		// calculate new id, category for the categories to copy, then insert in the DB
		int j = 1;
		
		for(Categories category : categories) {
			
			if(category.getCategory().equals(cat)) {
				try {
					String c = "";
					if(father.getCategory() == null) {
						c = "" + categoriesDAO.firstIndexAvaible(0);
					}else {
						c = father.getCategory() + (categoriesDAO.index(father.getID())+1);
					}
					String name = category.getName();
					categoriesDAO.insertCategories(name, c);
					categoriesDAO.insertRelation(categories.size()+j, id);
					father.setCategory(c);
					father.setName(name);
					father.setId(categories.size()+j);
					father.setFather(id);
					j++;
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
				break;
			}
		}
		
		for(Categories category : categories) {
			
			check = false;
			if(category.getCategory().length()>cat.length()) {
				for(int i = 0; i < cat.length(); i++) {
					if(category.getCategory().charAt(i) != cat.charAt(i)){
						check = true;
						break;
					}
				}
				
				if(!check) {
					String caracter = "";
					for(int i = cat.length(); i < category.getCategory().length(); i++) {
						caracter = caracter + category.getCategory().charAt(i);
					}
					category.setCategory(father.getCategory()+caracter);
					try {
						category.setFather(categoriesDAO.getFatherByCategory(category.getCategory()));
					} catch (SQLException e) {
						e.printStackTrace();
					}
					
					try {
						categoriesDAO.insertCategories(category.getName(), father.getCategory()+caracter);
						categoriesDAO.insertRelation(categories.size()+j, (categoriesDAO.getFatherByCategory(category.getCategory())));
						j++;
					} catch (SQLException e) {
						e.printStackTrace();
					}
					categoriesCopied.add(category);
				}
			}
		}
	
		// redirect to home
		response.sendRedirect("GoHome");
	}

	/**
	 * Method doPost: call doGet
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		doGet(request, response);
	}
	
	/**
	 * Close connection with DB
	 */
	public void destroy() {
		try {
			DBHandler.closeConnection(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
