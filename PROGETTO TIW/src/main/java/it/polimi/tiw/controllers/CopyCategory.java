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
import it.polimi.tiw.data.Branch;
import it.polimi.tiw.data.Categories;
import it.polimi.tiw.utils.DBHandler;

/**
 * Servlet implementation class CopyCategory
 */
@WebServlet("/CopyCategory")
public class CopyCategory extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
	private Connection connection = null;

	/**
	 * Initialize the connection with the BD
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
    public CopyCategory() {
        super();
    }
    
    /**
     * Method that copy the category and her children
     */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String cat = request.getParameter("category");
		
		CategoriesDAO categoriesDAO = new CategoriesDAO(connection);
		List<Categories> categories = new ArrayList<Categories>();
		
		// check if the session is active
		HttpSession session = request.getSession();
		if (session.getAttribute("user") == null) {
			response.sendRedirect("index.html");
			return;
		}
		
		// find the categories to copy starting from the "category" of the father
		
		try {
			categories = categoriesDAO.findCategory();
		}catch (SQLException e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Issue when reading categories from db");
			return;
		}
		
		boolean check_cat = false;
		for(Categories category : categories) {
			if(category.getCategory().equals(cat)) {
				check_cat = true;
			}
		}
		
		if(check_cat == false) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Category not found");
			return;
		}
		
		boolean check = false;
		
		for(Categories category : categories) {
			check = false;
			if(category.getCategory().equals(cat)) {
				category.setCopy();
			}else if(category.getCategory().length()>cat.length()) {
				for(int i = 0; i < cat.length(); i++) {
					if(category.getCategory().charAt(i) != cat.charAt(i)){
						check = true;
						break;
					}
				}
				if(!check) {
					category.setCopy();
				}
			}
		}
		
		
		// return the tree
		Branch tree = new Branch();
		tree.setTree(categories);
		
		String path = "/WEB-INF/home.html";
		ServletContext servletContext = getServletContext();
		final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
		ctx.setVariable("content", true);
		ctx.setVariable("categories", tree.getRoot());
		ctx.setVariable("categoriesCopied", cat);
		templateEngine.process(path, ctx, response.getWriter());
	}
	
	/**
	 * doPost method call doGet
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
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
