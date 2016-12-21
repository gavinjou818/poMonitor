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
 *  ��Servlet���ڽ���ǰ�˷����淢����png,��������������Ӧģ�屨��,
 *  ǰ��һ��Ҫÿ��һģ��λ�ö�Ҫ�ж�Ӧ������.��Ӧ���ݴ�����briefing.html��
 */
public class sImageAndcTemplateServlet extends HttpServlet 
{
    private static String filePath;
     
    //��ȡͼƬ����·��
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
	 * ���������ڻ�ȡ��Ӧrequest��ͼƬ����,
	 * Ȼ�������תΪpng ���浽filePath+fileName��
	 * �����ϴ��Ĳ���Ϊ"a=img/....&a1=img...";
	 * ��Ҫ�Ȼ�ȡa��ͼƬ
	 * ��ֻ����RequestParameter����"a"����
	 * ����SaveImage(request,"a0","D:/","a0.png");
	 * @author zhouzhifeng
	 * @param request ��ص�����ͷ,get,post������
	 * @param RequestParameter �����Ӧ��ͼƬkey
	 * @param filePath �ļ�·��
	 * @param fileName �ļ���
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
	 * ��Ϊ�Ǵ�ǰ�˴�������ͼ��ֻ�����ַ�������ʽ������,
	 * û�취�������,����ÿ��key�Ͳ�һ����,������ʱ���뷨
	 * �����ж��ٸ�ģ��,��Ҫ�ж��ٸ���Ӧ��ģ�庯��,׼ȷ��λÿ��λ��.
	 * ����ģ��1�ķ���, 
	 *
	 */
	private void CreateTemplate1()
	{
		
	}

	

}
