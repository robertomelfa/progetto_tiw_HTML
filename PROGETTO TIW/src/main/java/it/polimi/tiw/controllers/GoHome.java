package it.polimi.tiw.controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
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
 * Servlet implementation class GoHome
 */
@WebServlet("/GoHome")
public class GoHome extends HttpServlet{
	
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
	private Connection connection = null;
	
	/**
	 * Method that initialize the connection with the DB
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
	 * Mwthod doGet: return to the home with the updated tree
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String errorMsg = request.getParameter("errorMsg");
		
		CategoriesDAO categoriesDAO = new CategoriesDAO(connection);
		List<Categories> categories = new ArrayList<Categories>();
		
		// check if the session is active
		HttpSession session = request.getSession();
		if (session.getAttribute("user") == null) {
			response.sendRedirect("index.html");
			return;
		}
		
		// set the tree from the categories in the DB
		try {
			categories = categoriesDAO.findCategory();
		}catch (SQLException e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Issue when reading categories from db");
			return;
		}
		
		Branch tree = new Branch();
		tree.setTree(categories);
		
		// return to home with the tree updated
		
		String path = "/WEB-INF/home.html";
		ServletContext servletContext = getServletContext();
		final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
		ctx.setVariable("content", false);
		ctx.setVariable("categories", tree.getRoot());
		if(errorMsg != null) {
			ctx.setVariable("errorMsg", errorMsg);
		}
		templateEngine.process(path, ctx, response.getWriter());
		
	}
	
	/**
	 * Method doPost: call doGet
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
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