package it.polimi.tiw.controllers;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import it.polimi.tiw.dao.UsersDAO;
import it.polimi.tiw.data.userData;
import it.polimi.tiw.utils.DBHandler;

/**
 * Servlet implementation class CheckLogin
 */
@WebServlet("/CheckLogin")
public class CheckLogin extends HttpServlet {
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

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String name = request.getParameter("username");
		String password = request.getParameter("password");
		
		UsersDAO us = new UsersDAO(connection);
		List<userData> users = new ArrayList<userData>();
		
		if(name == null || name.isEmpty() || password == null || password.isEmpty()) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Missing parameter");
			return;
		}
		
		try {
			users = us.findUser();
		} catch (SQLException e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Issue when reading courses from db");
			return;
		}
	
		boolean check = false;
		
		for(userData user : users) {
			if(user.getName().equals(name) && user.getPassword().equals(password)){
				check = true;
			}
		}
		
		if(check == false) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "credentials does not exist");
			return;
		}
		
		response.sendRedirect("GoHome");
	}
	
	public void destroy() {
		try {
			DBHandler.closeConnection(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
