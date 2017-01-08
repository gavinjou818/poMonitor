package pomonitor.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.jms.Session;
import javax.json.Json;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.alibaba.fastjson.JSON;

import pomonitor.entity.User;
import pomonitor.entity.UserDAO;


/**
 * ��������Servlet
 * @author zhouzhifeng
 * ��Ϊhtml�ؼ�ֻ���ڵ�ǰҳ�潻��,
 * ������������session��Щ���������ԵĻ�,
 * ֻ��ͨ������ķ�ʽ������,�����������˻�������Servlet��������Щ��������,�ⲻ�����κ����Ե�Servlet
 *
 */


/**
 * 
 * @author Gavinjou
 *
 */
public class BasicInteractionServlet extends HttpServlet 
{

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
			throws ServletException, IOException 
	{
		
		String method=request.getParameter("method");
		boolean VerificationFlag=false;

		switch (method) 
		{
		//����ʱ�佻��
		case "setTimeInteraction":
			  setTimeInteraction(request);
			  break;
	    //�û���¼��֤����
		case "verifyUserInteraction":
			VerificationFlag=verifyUserInteraction(request);
			  break;			  
		default:
			break;
		}
        
		if(method.equals("verifyUserInteraction"))
		{   
			
			String basePath = request.getScheme()+"://"+request.getServerName()+":"+
	                request.getServerPort()+request.getContextPath()+"/";
		
			if(VerificationFlag==true)
			{   
				response.sendRedirect(basePath+"SY.jsp");
			}
			else 
			{    
				HttpSession session=request.getSession();
				session.setAttribute("errorMessage", "�˺Ż����������");
				response.sendRedirect(basePath+"index.jsp");	
			}
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
    /**
     * �򵥵ĵ�¼��֤,û��������ʩ,���޸�.
     * @author zhouzhifeng
     * @param request
     * @return
     */
	public boolean verifyUserInteraction(HttpServletRequest request)
	{  
		HttpSession session=request.getSession();
		String username=request.getParameter("username");
		String userpwd=request.getParameter("userpwd");
		
	
		UserDAO userDAO=new UserDAO();
		List<User> users=userDAO.findByUsername(username);
		
		
		if(users.size()==0){return false;}
	    
		User user=users.get(0);
		if(!user.getUserpwd().equals(userpwd))
		{    
			return false;
		}
		
		session.setAttribute("username",user.getUsername());
		session.setAttribute("userlevel",user.getUserlevel());
		session.setAttribute("userId", user.getUserid());

		return true;
	}
}
