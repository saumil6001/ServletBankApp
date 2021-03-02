
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class Transfer
 */
public class Transfer extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private HttpSession session = null;
	private int account_number = -1;
	private double balance = -1;
	private Connection connnection = null;
	private PrintWriter out = null;
	private Customer bank_customer = null;
	private double amount = 0;
	private int deposit_account_number = -1;
	private ReentrantLock lock;
	
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html");
		session = request.getSession(false);
		out = response.getWriter();
		bank_customer = (Customer) session.getAttribute("customer");
		account_number = bank_customer.getBank_account().getAccount_number();
		balance = bank_customer.getBank_account().getBalance();
		connnection = bank_customer.getConnnection();
		lock=new ReentrantLock();
		deposit_account_number = Integer.parseInt(request.getParameter("transferAccountNumber"));
		amount = Double.parseDouble(request.getParameter("amount"));

		transfer(deposit_account_number, amount);

	}

	private void transfer(int deposit_account_number, double amount) {

		
		
		try {
		lock.tryLock(1000,TimeUnit.MILLISECONDS);

		if (withdraw(amount)) {

			if (deposit(deposit_account_number, amount)) {

				out.println("Transfer successful <br>");
				out.println("Balance : " + balance);

			} else {
				
				deposit(amount);
				out.println("Transfer Error : <br>");
				out.println("Balance : " + balance);

			}

		} else {
			out.println("Transcection Error: Somthing Went Wrong");
		}

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			lock.unlock();
		}
		
		
		
		
	}

	private boolean withdraw(double amount) {

		if (bank_customer.getBank_account().withdraw(amount)) {

			balance = bank_customer.getBank_account().getBalance();

			try (Statement st = connnection.createStatement();) {

				int rt = st.executeUpdate(
						"UPDATE accounts SET balance = '" + balance + "'WHERE account_id='" + account_number + "';");

				if (rt != 0) {
					return true;
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

		return false;
	}

	public boolean deposit(int deposte_account_number, double amount) {

		String sql = "select account_id, balance from accounts where account_id= '" + deposte_account_number + "';";

		try (Statement st = connnection.createStatement(); ResultSet rt = st.executeQuery(sql);) {

			if (rt.next()) {
				int deposit_account_number = rt.getInt(1);
				double deposit_balance = rt.getDouble(2);
				deposit_balance += amount;

				try (Statement st1 = connnection.createStatement();) {

					int rt1 = st.executeUpdate("UPDATE accounts SET balance = '" + deposit_balance
							+ "'WHERE account_id='" + deposit_account_number + "';");

					if (rt1 != 0) {
						return true;
					} else {
						out.println("Something Went wrong transection can not be completed ");
					}

				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;
	}

	private boolean deposit(double amount) {

		if (bank_customer.getBank_account().addDeposit(amount)) {

			balance = bank_customer.getBank_account().getBalance();

			try (Statement st = connnection.createStatement();) {

				int rt = st.executeUpdate(
						"UPDATE accounts SET balance = '" + balance + "'WHERE account_id='" + account_number + "';");

				if (rt != 0) {

					return true;
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

		return false;
	}

}
