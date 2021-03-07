

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.database.DataScource;

public class LogOut extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	
	
	
 	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

 		Connection connection= DataScource.getConnection();
 		
 		try {
			connection.close();
			HttpSession seassion = request.getSession(false);	
			seassion.invalidate();
			response.sendRedirect("LoginHTML.html");
		} catch (SQLException e) {
			e.printStackTrace();
		}
 		
 	}
	
}
