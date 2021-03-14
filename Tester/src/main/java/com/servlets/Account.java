package com.servlets;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class Account extends HttpServlet {
	private static final long serialVersionUID = 1L;

	
	private HttpServletRequest request=null;
	private HttpServletResponse response= null;
	private HttpSession session = null;
	private RequestDispatcher rd = null;
	private PrintWriter out = null;

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		
		this.request=request;
		this.response=response;
		response.setContentType("text/html");
		session = request.getSession();
		out = response.getWriter();
		String botton = request.getParameter("botton");

		
		if (session == null) {
			out.println("Please Login First");
			rd = request.getRequestDispatcher("LoginHTML.html");
			rd.include(request, response);
		} else {
			buttonClick(botton);
		}

	}
	
	
	private void buttonClick(String botton) throws ServletException, IOException {

		switch (botton) {

		case "checkbalance":
			rd = request.getRequestDispatcher("BalanceInquiry");
			rd.forward(request, response);
			break;
			
		case "deposit":
			rd = request.getRequestDispatcher("DepositHTML.html");
			rd.forward(request, response);
			break;

		case "withdraw":
			rd = request.getRequestDispatcher("WithdrawHTML.html");
			rd.forward(request, response);
			break;

		case "transfer":
			rd = request.getRequestDispatcher("TransferHTML.html");
			rd.forward(request, response);

		default:
			out.println("Something Went Wrong With Button");
		}
	}

}