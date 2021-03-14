package com.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.classes.BankAccount;
import com.classes.Customer;
import com.database.DataScource;

public class HomePage extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private HttpSession session = null;
	private String username = null;
	private Connection connnection = null;
	private PrintWriter out = null;
	private RequestDispatcher rd = null;
	private Customer customer;
	private final String sql = "select account_id,balance,user_id from bank_account where username= ?";
	private final String sql1 = "select customer_name,email_id,phone_number from customer where user_id = ?";
	private BankAccount bankAccount = null;
	private String user_id = null;
	private AnnotationConfigApplicationContext context=null;
	
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		connnection = DataScource.getConnection();

		response.setContentType("text/html");
		request.getParameter("botton");
		session = request.getSession();
		out = response.getWriter();
		username = (String) session.getAttribute("user_name");

		if (session == null || connnection == null) {
			out.println("Please Login First");
			rd = request.getRequestDispatcher("LoginHTML.html");
			rd.include(request, response);

		} else {

			if (initBankAccount(username) && initCustomer()) {
				welcomepage();
				session.setAttribute("customer", customer);
				rd = request.getRequestDispatcher("AccountHTML.html");
				rd.include(request, response);
			}

		}

	}

	
	
	
	
	public boolean initBankAccount(String userName) {

		PreparedStatement statement = null;
		ResultSet rs = null;

		try {
			statement = connnection.prepareStatement(sql);
			statement.setString(1, userName);
			rs = statement.executeQuery();

			while (rs.next()) {

				int account_number = rs.getInt(1);
				double balance = rs.getDouble(2);
				int u_Id = rs.getInt(3);
				user_id = Integer.toString(u_Id);

				context = new AnnotationConfigApplicationContext(
						com.classes.SpringConfing.class);

				BankAccount bankAccount = context.getBean("bankAccount", BankAccount.class);
				bankAccount.setAccountNumber(account_number);
				bankAccount.setBalance(balance);
				
				return true;

			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		finally {

			if (statement != null) {

				try {
					statement.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		}

		return false;

	}

	
	
	
	
	private boolean initCustomer() {
		PreparedStatement statement = null;
		ResultSet rs = null;

		try {
			statement = connnection.prepareStatement(sql1);
			statement.setString(1, user_id);
			rs = statement.executeQuery();
//			private final String sql1 = "select customer_name,email_id,phone_number from customer where user_id = ?";

			while (rs.next()) {

				String name = rs.getString(1);
				String email_id = rs.getString(2);
				String phoneNumber = rs.getString(3);


				customer = context.getBean("customer", Customer.class);
				customer.setEmailId(email_id);
				customer.setName(name);
				customer.setPhoneNumber(phoneNumber);
				return true;

			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		finally {

			if (statement != null) {

				try {
					statement.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		}

		return false;

	}

	
	
	
	private void welcomepage() {
		out.println("<html><h1 align='center'>" + "TD Bank" + "</h1></html>");
		out.println("<html> <form action=\"LogOut\" method=\"get\">"
				+ " <button style=\"position: absolute; right: 25;\"> Logout </button> " + "</form></html>");

		out.println("<html><body>" + "Customer Name :" + customer.getName() + "<br></body></html>");
		out.println("<html><body>" + "Email :" + customer.getEmailId() + "<br></body></html>");
		out.println("<html><body>" + "Phone Number :" + customer.getPhoneNumber() + "<br></body></html>");

	}

}
