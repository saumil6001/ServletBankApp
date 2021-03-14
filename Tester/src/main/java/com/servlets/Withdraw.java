package com.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.classes.Customer;

public class Withdraw extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private HttpServletRequest request = null;
	private HttpServletResponse response = null;
	private HttpSession session = null;
	private Customer customer = null;
	private RequestDispatcher rd = null;
	private PrintWriter out = null;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html");
		this.request = request;
		this.response = response;
		session = request.getSession();
		customer = (Customer) session.getAttribute("customer");
		String withdraw_amount = request.getParameter("withdraw");
		out = response.getWriter();

		if (session == null || customer == null || withdraw_amount == null) {
			out.println("Please Login ");
			rd = request.getRequestDispatcher("LoginHTML.html");
			rd.include(request, response);

		} else {

			double amount = Double.parseDouble(withdraw_amount);
			withdraw(amount);

		}

	}

	private boolean withdraw(double amount) {

		if (customer.getBankAccount().withdraw(amount)) {
			try {

				request.getRequestDispatcher("HomePage").include(request, response);
				out.println("Amount Withdraw Successful<br>");
				out.println("New Balance : " + customer.getBankAccount().getBalance());
				return true;

			} catch (ServletException | IOException e) {
				e.printStackTrace();
			}
		} else {
			try {
				request.getRequestDispatcher("WithdrawHTML.html").include(request, response);
				out.println("Something Went wrong: Transection can not be Completed ");
			} catch (ServletException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return false;
	}

}
