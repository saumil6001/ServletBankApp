import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class Account extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private HttpSession session = null;
	private String username = null;
	private Connection connnection = null;
	private PrintWriter out = null;
	private Customer bank_customer = null;



	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		response.setContentType("text/html");
		session = request.getSession(false);
		out = response.getWriter();
		bank_customer = (Customer) session.getAttribute("customer");
		connnection = bank_customer.getConnnection();
		username = bank_customer.getUsername();

		if (session != null) {

			String botton = request.getParameter("botton");

			switch (botton) {

			case "checkbalance":
				checkbalnce(username);
				break;
		
			case "deposit":
				RequestDispatcher rd = request.getRequestDispatcher("DepositHTML.html");
				rd.forward(request, response);
				break;

			case "withdraw":
				rd = request.getRequestDispatcher("WithdrawHTML.html");
				rd.forward(request, response);
				break;
			
			case "transfer":
				rd=request.getRequestDispatcher("TransferHTML.html");
				rd.forward(request, response);
				
			default:
				out.println("Something Went Wrong With Button");
			}

		} else {
			out.println("Problem With Seassion");
		}

	}


	private void checkbalnce(String userName) {

		try (Statement st = connnection.createStatement();
				ResultSet rt = st
						.executeQuery("select customer_name,balance, account_id from bank_account where username='"
								+ userName + "';")) {

			while (rt.next()) {

				String customer_name = rt.getString(1);
				double balance = rt.getDouble(2);
				int account_number = rt.getInt(3);

				out.println("<html><h1 align='center'>" + "TD Bank" + "</h1></html>");
				out.println("<html><body>" + "Customer Name :" + customer_name + "<br></body></html>");
				out.println("<html><body>" + "Account Number :" + account_number + "<br></body></html>");
				out.println("<html><body>" + "Balance :" + balance + "<br></body></html>");

			}

		} catch (SQLException e) { // TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}