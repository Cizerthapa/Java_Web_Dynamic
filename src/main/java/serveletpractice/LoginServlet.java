package serveletpractice;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import controller.DatabaseController;

/**
 *  implementation class LoginServlet - cizer
 */
@WebServlet(asyncSupported = true, urlPatterns = { "/LoginServlet" })
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	DatabaseController dbController = new DatabaseController();
	
    public LoginServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String userName = request.getParameter("username");
		String password = request.getParameter("password");
		
		int loginResult = dbController.getStudentLoginInfo(userName,password);
		
		if(loginResult == 1) {
			//successful
			//response.sendRedirect(request.getContextPath() + "/pagesHtml/welcome.html");
			  // Retrieve the first name from the database based on 
	        String usernm = dbController.getStudentUserName(userName, password);

	        // Set the first name as a request attribute
	        try{
	        request.setAttribute("userName", usernm);

	        // Forward the request to the welcome.jsp page
	        RequestDispatcher dispatcher = request.getRequestDispatcher("/pagesHtml/welcome.jsp");
	        dispatcher.forward(request, response);
	        } catch(Exception e) {
	        	e.getMessage();
	        	e.printStackTrace();
	        }
		} else if(loginResult == 0){
			System.out.println("Credentails didnt meet");
		} else {
			System.out.println("!");
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
