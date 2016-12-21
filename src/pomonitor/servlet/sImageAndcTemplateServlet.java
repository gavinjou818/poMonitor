package pomonitor.servlet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pomonitor.util.PropertiesReader;
import sun.misc.BASE64Decoder;


/**
 * 
 *  @author zhouzhifeng
 *  2016/12/19
 *  本Servlet用于接收前端发界面发来的png,并且用于生成相应模板报表,
 *  前提一定要每个一模板位置都要有对应的输入.相应数据传输在briefing.html中
 */
public class sImageAndcTemplateServlet extends HttpServlet 
{
    private static String filePath;
     
    //获取图片保存路径
    static
    {
    	PropertiesReader propertiesReader = new PropertiesReader();
	    filePath= propertiesReader.getPropertyByName("IMG_savePath");
    }
	
    /**
	 * Constructor of the object.
	 */
	public sImageAndcTemplateServlet() {
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
        
		
	}

	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}
	
	/**
	 * 
	 * 本功能用于获取相应request的图片参数,
	 * 然后将其解码转为png 保存到filePath+fileName中
	 * 例如上传的参数为"a=img/....&a1=img...";
	 * 若要先获取a的图片
	 * 则只需在RequestParameter输入"a"即可
	 * 例子SaveImage(request,"a0","D:/","a0.png");
	 * @author zhouzhifeng
	 * @param request 相关的请求头,get,post都可以
	 * @param RequestParameter 请求对应的图片key
	 * @param filePath 文件路径
	 * @param fileName 文件名
	 * @throws IOException 
	 */
	
	@SuppressWarnings("unused")
	private void SaveImage(HttpServletRequest request,String RequestParameter,String filePath,String fileName) throws IOException
	{
		
		String uu=request.getParameter(RequestParameter);
        String[] url=uu.split(",");
        String u=url[1];
   
        byte[] b=new BASE64Decoder().decodeBuffer(u);
        OutputStream out=new FileOutputStream(new File(filePath+fileName));
        out.write(b); 
        out.flush();
        out.close();
	}
	
	
	/*
	 * 因为是从前端传过来的图表只能用字符串的形式传过来,
	 * 没办法用数组存,对于每个key就不一样了,所以暂时的想法
	 * 就是有多少个模板,就要有多少个对应的模板函数,准确定位每个位置.
	 * 创造模板1的方法, 
	 *
	 */
	private void CreateTemplate1()
	{
		
	}

	

}
