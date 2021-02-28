
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class Deposit extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private HttpSession session = null;
	private int account_number = -1;
	private double balance = -1;
	private Connection connnection = null;
	private PrintWriter out = null;
	private Customer bank_customer = null;
	private double amount = 0;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html");
		session = request.getSession(false);
		out = response.getWriter();
		bank_customer = (Customer) session.getAttribute("customer");
		account_number = bank_customer.getBank_account().getAccount_number();
		balance = bank_customer.getBank_account().getBalance();
		connnection = bank_customer.getConnnection();
		amount = Double.parseDouble(request.getParameter("deposit"));

		deposite(amount);

	}

	private void deposite(double amount) {

		if (bank_customer.getBank_account().addDeposit(amount)) {

			balance = bank_customer.getBank_account().getBalance();

			try (Statement st = connnection.createStatement();) {

				int rt = st.executeUpdate(
						"UPDATE accounts SET balance = '" + balance + "'WHERE account_id='" + account_number + "';");

				if (rt != 0) {
					out.println("amount deposited<br>");
					out.println("Balance : " + balance);
					
				} else {
					out.println("Something Went wrong transection can not be completed ");
				}

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {
			out.println("Something Went wrong: Transection can not be Completed ");
		}

	}

}
