
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connnection = null;
	private String driver = null;
	private String url = null;
	private String database = null;
	private String pwd = null;
	private String user_name = null;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {

			connnection = connect(request, response);

			if (connnection != null) {

				validate(request, response);

			}

		} catch (ClassNotFoundException | SQLException e1) {
			e1.printStackTrace();
		}

	}

	

	public Connection connect(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, ClassNotFoundException {

		ServletContext sc = request.getServletContext();
		driver = sc.getInitParameter("driver_name");
		url = sc.getInitParameter("url");
		database = sc.getInitParameter("database");
		pwd = sc.getInitParameter("password");
		user_name = sc.getInitParameter("user_name");
		response.setContentType("text/html");

		Class.forName(driver);
		return connnection = DriverManager.getConnection(url + database, user_name, pwd);

	}
	
	
	public void close_connection() throws SQLException, ClassNotFoundException {

		if (connnection != null) {
			connnection.close();

		}

	}
	
	
	
	
	private boolean validate(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		String name = request.getParameter("q1");
		int id = Integer.parseInt(request.getParameter("q2"));

		try (PrintWriter out = response.getWriter();
				Statement st = connnection.createStatement();
				ResultSet rt = st.executeQuery("select AccountNum,UserName,user_id from UserName where UserName='"
						+ name + "' && AccountNum='" + id + "';")) {

			if (rt.next()) {
				String user_name= rt.getString(2);
				int user_id = rt.getInt(3);		
				
				BankAccount bankAccount=activateBankAccount(user_name, out);

				if (bankAccount !=null) {
					
					Customer customer = new Customer(user_name,user_id,connnection,bankAccount);
					HttpSession seassion = request.getSession();
					seassion.setAttribute("customer", customer); 
					welcomepage(user_id, out);
					RequestDispatcher rd =request.getRequestDispatcher("AccountHTML.html"); 
					rd.include(request,response);
					
				}
								
				
			} else {

				response.getWriter().println("Check User Name Or Passaward ");

				RequestDispatcher rd = request.getRequestDispatcher("LoginHTML.html");

				rd.include(request, response);

			}

		} catch (SQLException e) {
			response.getWriter().println(e.getMessage());
			e.printStackTrace();
		}

		return false;
	}

	private void welcomepage(int user_id, PrintWriter out) {

		try (Statement st = connnection.createStatement();
				ResultSet rt = st
						.executeQuery("select customer_name,email_id,phone_number from customer where user_id ='"
								+ user_id + "';");) {

			while (rt.next()) {

				String customer_name = rt.getString(1);
				String email_id = rt.getString(2);
				String phone_number = rt.getString(3);

				out.println("<html><h1 align='center'>" + "TD Bank" + "</h1></html>");
				out.println("<html><body>" + "Customer Name :" + customer_name + "<br></body></html>");
				out.println("<html><body>" + "Email :" + email_id + "<br></body></html>");
				out.println("<html><body>" + "Phone Number :" + phone_number + "<br></body></html>");

			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
	

	private BankAccount activateBankAccount(String user_name, PrintWriter out) {

		BankAccount bankAccount = null;

		try (Statement st = connnection.createStatement();
				ResultSet rt = st
						.executeQuery("select customer_name,balance, account_id from bank_account where username='"
								+ user_name + "';");) {

			while (rt.next()) {

				String customer_name = rt.getString(1);
				double balance = rt.getDouble(2);
				int account_number = rt.getInt(3);

				bankAccount = new BankAccount(account_number, balance, customer_name);

			}

		} catch (SQLException e) {

			e.printStackTrace();
		}

		return bankAccount;
	}


	
	
	

}

// create db for balance, acoount name, email, phone etc.
// check accouunt class give butons for balance inquiry. deposite, withdraw and transfer.
