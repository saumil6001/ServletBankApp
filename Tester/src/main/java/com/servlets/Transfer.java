package com.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.classes.Customer;
import com.database.DataScource;
public class Transfer extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private HttpServletRequest request = null;
	private HttpServletResponse response = null;
	private HttpSession session = null;
	private Customer customer = null;
	private RequestDispatcher rd = null;
	private PrintWriter out = null;
	private Connection connection = null;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html");
		this.request = request;
		this.response = response;

		session = request.getSession();
		connection = DataScource.getConnection();
		customer = (Customer) session.getAttribute("customer");

		String transfer_amount = request.getParameter("amount");
		String deposit_accountNumber = request.getParameter("transferAccountNumber");

		out = response.getWriter();

		if (session == null || customer == null || transfer_amount == null || connection == null) {

			out.println("Please Login ");
			rd = request.getRequestDispatcher("LoginHTML.html");
			rd.include(request, response);

		} else {

			double amount = Double.parseDouble(transfer_amount);
			int accountNumber = Integer.parseInt(deposit_accountNumber);
			transfer(accountNumber, amount);

		}

	}

	private boolean transfer(int deposit_account_number, double amount) {

		if (customer.getBankAccount().transferMoney(deposit_account_number, amount)) {
			try {
				request.getRequestDispatcher("HomePage").include(request, response);
				out.println("Amount Transfer Successful<br>");
				out.println("New Balance : " + customer.getBankAccount().getBalance());
				return true;
			} catch (ServletException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {

			try {
				request.getRequestDispatcher("TransferHTML.html").include(request, response);
				out.println("Something Went wrong: Transection can not be Completed. <br>Please check the details.");
			} catch (ServletException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		return false;
	}
}
