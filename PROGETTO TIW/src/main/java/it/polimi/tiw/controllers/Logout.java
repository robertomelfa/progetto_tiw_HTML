package it.polimi.tiw.controllers;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class Logout
 */
@WebServlet("/Logout")
public class Logout extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructor of the class
	 */
	public Logout() {
		super();
	}
	
	/**
	 * Method do get: logout
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		// set session false
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.setAttribute("username", null);
			session.invalidate();
		}
		String path = getServletContext().getContextPath() + "/index.html";
		response.sendRedirect(path);
	}
	
	/**
	 * Method doPost: call doGet
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
