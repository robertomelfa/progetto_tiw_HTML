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

import org.thymeleaf.TemplateEngine;
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

	public void init() throws ServletException {
		
		ServletContext servletContext = getServletContext();
		ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(servletContext);
		templateResolver.setTemplateMode(TemplateMode.HTML);
		this.templateEngine = new TemplateEngine();
		this.templateEngine.setTemplateResolver(templateResolver);
		templateResolver.setSuffix(".html");
		
		connection = DBHandler.getConnection(getServletContext());
	}
	
    public AddCategory() {
        super();
    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String name = request.getParameter("name");
		String father = request.getParameter("father");
		
		
		if(name == null || name.isEmpty() || father == null || father.isEmpty()) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Missing parameter");
			return;
		}
		
		CategoriesDAO categoriesDAO = new CategoriesDAO(connection);
		List<Categories> categories = new ArrayList<Categories>();
		
		try {
			categories = categoriesDAO.findCategory();
		}catch (SQLException e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Issue when reading courses from db");
			return;
		}
		
		boolean check = false;
		Categories fatherCategory = new Categories();
		
		for(Categories category : categories) {
			if(category.getName().equals(name)) {
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "category already exists");
				return;
			}
			if(category.getName().equals(father)) {
				check = true;
				fatherCategory = category;
			}
		}
		
		if(!check) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "father does not exist");
			return;
		}
		
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
		
		response.sendRedirect("GoHome");
	}

}
