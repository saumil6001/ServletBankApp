
import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.database.DataScource;

public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private DataScource datascource;
	private RequestDispatcher rd;
	
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        datascource = new DataScource();
    }
	
    
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		
		response.setContentType("text/html");
		String name = request.getParameter("q1");
		String id = request.getParameter("q2");

		
		if(datascource.connect(name,id)) {
			rd= request.getRequestDispatcher("HomePage");
			HttpSession seassion = request.getSession();
			seassion.setAttribute("user_name", name);
			rd.forward(request, response);
				
		} else {
			response.getWriter().println("Check User Name Or Passaward ");
			rd = request.getRequestDispatcher("LoginHTML.html");
			rd.include(request, response);			
		} 		
	}

}
