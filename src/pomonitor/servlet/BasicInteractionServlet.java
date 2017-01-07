package pomonitor.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.jms.Session;
import javax.json.Json;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


/**
 * 基本交互Servlet
 * @author zhouzhifeng
 * 因为html控件只能在当前页面交互,
 * 如果想更新例如session这些服务器属性的话,
 * 只能通过请求的方式来设置,所以我设置了基本交互Servlet来处理这些基本交互,这不属于任何特性的Servlet
 *
 */
public class BasicInteractionServlet extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public BasicInteractionServlet() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

           doPost(request, response);
	}

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		String method=request.getParameter("method");
	

		switch (method) {
		case "setTimeInteraction":
			  setTimeInteraction(request);
	
			  
			  break;
		default:
			break;
		}
        

	}

	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}
	
	public void setTimeInteraction(HttpServletRequest request)
	{
		HttpSession session=request.getSession();
		session.setAttribute("startTime", request.getParameter("startTime"));
		session.setAttribute("endTime", request.getParameter("endTime"));
	}

}
