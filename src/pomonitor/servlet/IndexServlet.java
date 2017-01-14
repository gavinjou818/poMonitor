package pomonitor.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pomonitor.statistics.Summarize;

public class IndexServlet extends HttpServlet 
{

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
		
		
		switch (method) 
		{
		case "getTendency":
			try 
			{
				System.out.println("**");
				resJSON = getTendency(startTime, endTime);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case "checkStatus":
			try 
			{    
				
				resJSON = checkStatus(endTime);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
			
		case "getLatestMessage":
			try 
			{
				System.out.println("new****");
				resJSON = getLatestMessage();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		
		
			
	    //@author  zhouzhifeng------>
		case "getAllMessage_Briefing"://得到简报所需要的所有新闻 对应页面AllPublicOpinion.jsp
			try
			{
				     int max=Integer.parseInt(request.getParameter("max"));//获取显示条数
				     int index=Integer.parseInt(request.getParameter("index"));//当前第几页，开头为0
		             System.out.println("getAllMessage_Briefing***");
				resJSON=getAllMessage_Briefing(startTime, endTime, max, index);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
  			break;
		case "getMediaAnalysis"://得到媒体分析
			    resJSON=getMediaAnalysis(startTime,endTime);
			break;
  			
  	    //zhouzhifeng------>
		default:
			break;
		}
		System.out.println("---->***" + resJSON);
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
			throws ParseException 
	{
		Summarize summarize = new Summarize();
		return summarize.getTendency(startTime, endTime);
	}

	private String checkStatus(String endTime) throws ParseException {
		Summarize summarize = new Summarize();
		return summarize.checkStatus(endTime);
	}
	private String getLatestMessage() throws ParseException {
		Summarize summarize = new Summarize();
		return summarize.getLatestMessage();
		
	}
	
	
	//@author zhouzhifeng----->
	/**
	 * 该方法主要用于获取news和newstend的联结关系的数据库表
	 * 对应MGYQ.jsp的今日舆情,提供了一个显示接口,
	 * @author zhouzhifeng
	 * @param startTime  开始时间
	 * @param endTime    结束时间
	 * @param max        每页的数量
	 * @param index      开始的索引=(每页的数量*当前是第几页) 索引是从0开始的。
	 * @return
	 * @throws Exception
	 */
	private String  getAllMessage_Briefing(String startTime,String endTime,int max, int index) throws Exception 
	{
		Summarize summarize = new Summarize();
		return summarize.getAllMessage_Briefing(startTime,endTime,max,index);
	}
	
	
	private String getMediaAnalysis(String startTime,String endTime)
	{
		Summarize summarize = new Summarize();
		return summarize.getMediaAnalysis(startTime,endTime);
	}
	
	
	//zhouzhifeng----->
}
