
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class BalanceInquiry extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private HttpSession session = null;
	private Customer customer = null;
	private RequestDispatcher rd = null;
	private PrintWriter out = null;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		session = request.getSession();

		customer = (Customer) session.getAttribute("customer");

		out = response.getWriter();

		if (session == null || customer == null) {

			out.println("Please Login First");
			rd = request.getRequestDispatcher("LoginHTML.html");
			rd.include(request, response);

		} else {

			balanceInquiry();

		}

	}

	private void balanceInquiry() {

		out.println("<html><h1 align='center'>" + "TD Bank" + "</h1></html>");
		out.println("<html> <form action=\"HomePage\" method=\"get\">"
				+ " <button style=\"position: absolute; right: 25;\"> Home </button> "
				+ "</form></html>");
		  
		out.println("<html><body>" + "Customer Name :" + customer.getName() + "<br></body></html>");
		out.println("<html><body>" + "Account Number :" + customer.getBank_account().getAccount_number()
				+ "<br></body></html>");
		out.println("<html><body>" + "Balance :" + customer.getBank_account().getBalance() + "<br></body></html>");

	}

}
