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
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import it.polimi.tiw.dao.CategoriesDAO;
import it.polimi.tiw.dao.UsersDAO;
import it.polimi.tiw.data.Branch;
import it.polimi.tiw.data.Categories;
import it.polimi.tiw.data.userData;
import it.polimi.tiw.utils.DBHandler;

/**
 * Servlet implementation class AddCategory
 */
@WebServlet("/AddCategory")
public class AddCategory extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
	private Connection connection = null;
	
	/**
	 * Initialize the connection with DB and Template thymeleaf
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
    public AddCategory() {
        super();
    }
    
    /**
     * What the class do after the submit of the form
     */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String name = request.getParameter("name");
		String father = request.getParameter("father");
		
		// check if the session is active
		HttpSession session = request.getSession();
		if (session.getAttribute("user") == null) {
			response.sendRedirect("index.html");
			return;
		}
		
		String path;
		ServletContext servletContext = getServletContext();
		final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
		
		// check if parameter are not null
		if(name == null || name.isEmpty() || father == null || father.isEmpty()) {
			response.sendRedirect("GoHome?errorMsg="+"missing parameters");
			return;
		}
		
		CategoriesDAO categoriesDAO = new CategoriesDAO(connection);
		List<Categories> categories = new ArrayList<Categories>();
		
		// get all the categories of the database
		
		try {
			categories = categoriesDAO.findCategory();
		}catch (SQLException e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Issue when reading categories from db");
			return;
		}
		
		boolean check = false;
		int i = 0;
		Categories fatherCategory = new Categories();
		
		// check if the category already exists (error) and if the father exists
		
		for(Categories category : categories) {
			if(category.getName().equals(name)) {
				response.sendRedirect("GoHome?errorMsg=category already exists");
				return;
			}
			if(category.getCategory().equals(father)) {
				check = true;
				fatherCategory = category;
			}
		}
		
		// check if the father exists
		if(!check) {
			response.sendRedirect("GoHome?errorMsg=father does not exist");
			return;
		}
		
		// check number of children
		
		try {
			if(categoriesDAO.index(fatherCategory.getID()) >= 9) {
				response.sendRedirect("GoHome?errorMsg=Can't paste here. Father has already 9 categories");
				return;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		// add the category in the database
		
		int size = 0;
		
		try {
			size = categoriesDAO.index(fatherCategory.getID());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		try {
			categoriesDAO.insertCategories(name, fatherCategory.getCategory() + (size+1));
			categoriesDAO.insertRelation(categories.size()+1, fatherCategory.getID());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	
		// return to home
		response.sendRedirect("GoHome");
	}
	
	/**
	 * Close connection with the DB
	 */
	public void destroy() {
		try {
			DBHandler.closeConnection(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
