package pomonitor.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pomonitor.statistics.Summarize;

public class IndexServlet extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public IndexServlet() {
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
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * The doPost method of the servlet. <br>
	 * 
	 * This method is called when a form has its tag value method equals to
	 * post.
	 * 
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		String method = request.getParameter("method");
		String resJSON = "";
		System.out.println(method);
		switch (method) {
		case "getTendency":
			try {System.out.println("**");
				resJSON = getTendency(startTime, endTime);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case "checkStatus":
			try {
				resJSON = checkStatus();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
			
		case "getLatestMessage":
			try {
				System.out.println("****");
				resJSON = getLatestMessage();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		default:
			break;
		}
		System.out.println("***" + resJSON);
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(resJSON);
	}

	/**
	 * Initialization of the servlet. <br>
	 * 
	 * @throws ServletException
	 *             if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}

	private String getTendency(String startTime, String endTime)
			throws ParseException {
		Summarize summarize = new Summarize();
		return summarize.getTendency(startTime, endTime);
	}

	private String checkStatus() throws ParseException {
		Summarize summarize = new Summarize();
		return summarize.checkStatus();
	}
	private String getLatestMessage() throws ParseException {
		Summarize summarize = new Summarize();
		return summarize.getLatestMessage();
		
	}
}
