package pomonitor.servlet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.pdfbox.pdfparser.BaseParser;
import org.omg.CORBA.PRIVATE_MEMBER;

import pomonitor.briefing.BriefingData;
import pomonitor.briefing.DocumentHandler;
import pomonitor.entity.Briefing;
import pomonitor.entity.BriefingDAO;
import pomonitor.entity.EntityManagerHelper;
import pomonitor.entity.News;
import pomonitor.entity.NewsDAO;
import pomonitor.entity.NewsTend;
import pomonitor.entity.NewsTendDAO;
import pomonitor.statistics.BriefingSummarize;
import pomonitor.util.ConsoleLog;
import pomonitor.util.PropertiesReader;
import sun.misc.BASE64Decoder;

/**
 * ���ǰ��Ҫ���freemarket+echarts
 * @author zhouzhifeng 2016/12/19 ��Servlet���ڽ���ǰ�˷����淢����png,��������������Ӧģ�屨��,
 *         ǰ��һ��Ҫÿ��һģ��λ�ö�Ҫ�ж�Ӧ������.��Ӧ���ݴ�����briefing.html��
 */
public class sImageAndcTemplateServlet extends HttpServlet
{
	private static String filePath;//�ļ���ϵͳ�е���ʵ·��
	private static String basePath;//�ļ��ڷ��ʵ�ַ�е����·��
	private static String BEF_savePath;//���ڱ���İ�
	
	private  int year; 
	private  int month;
	private  int day;
	private  int hour;
	private  int minute;
	private  int second;
	private  int userid;
	
	
	

	

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
			throws ServletException, IOException 
	{
		
		
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		
		int max;
		int index;
		String method = request.getParameter("method");
		String requestString = request.getParameter("requestString");
		String resJSON = "";
	    basePath = request.getScheme()+"://"+request.getServerName()+":"+
                request.getServerPort()+request.getContextPath()+"/";
	    HttpSession session=request.getSession();
	    session.setAttribute("BEF_savePath", BEF_savePath);
	    String tmp=session.getAttribute("userId").toString();
	    userid = Integer.parseInt(tmp);
	   


		switch (method) 
		{
		case "getPreviewMessage"://��ȡԤ����ѡ��Ĵ�����Ϣ

			// ��ȡ�����������
			max = Integer.parseInt(request.getParameter("max"));
			// ��ȡ��ʼ���� index=(max*�ڼ�ҳ) ��ʼ����Ϊ0
			index = Integer.parseInt(request.getParameter("index"));
			resJSON = getPreviewMessage(requestString, max, index);
			break;
		case "getPreviewChart"://������ѡ��Ĵ�����Ϣ,��������Ҫ��ͼ��
			// ��ȡ��ͼ��
			resJSON = getPreviewChart(requestString,request.getParameter("templateName"));
			break;
		case "createTemplate1":
			resJSON = "{\"message\":\"�ɹ����ɱ���\"}";
			createTemplate1(request,response);
			break;
		case "getIntimateBriefing":	//�õ�˽�����ɵı���,���ǲ�������,�Լ����ɵı����Լ��ı���.
			// ��ȡ�������
			max = Integer.parseInt(request.getParameter("max"));
			// ��ȡ��ʼ����
			index = Integer.parseInt(request.getParameter("index"));
			resJSON=getIntimateBriefing(max,index,userid,basePath);
			break;
		case "sendEmail":
		    DocumentHandler documentHandler=new DocumentHandler();
		    String recipientString=request.getParameter("recipientString");
		    try 
		    {
		    	resJSON=documentHandler.sendEmail(recipientString, requestString);
			} catch (MessagingException e)
			{	
				e.printStackTrace();
			}
		    
		    break;
		default:
			break;
		}

		ConsoleLog.PrintInfo(getClass(), resJSON);
		response.getWriter().write(resJSON);

	}

	/**
	 * Initialization of the servlet. <br>
	 * 
	 * @throws ServletException
	 *             if an error occurs
	 */
	public void init() throws ServletException 
	{   
		PropertiesReader propertiesReader = new PropertiesReader();
		BEF_savePath=propertiesReader.getPropertyByName("BEF_savePath");
		filePath =this.getServletContext().getRealPath("/")+BEF_savePath;
		File file=new File(filePath);
		if(!(file.exists()&&file.isDirectory()))
		{
			file.mkdirs();
		}
	}

	/**
	 * 
	 * ���������ڻ�ȡ��Ӧrequest��ͼƬ����, Ȼ�������תΪpng ���浽filePath+fileName��
	 * �����ϴ��Ĳ���Ϊ"a=img/....&a1=img..."; ��Ҫ�Ȼ�ȡa��ͼƬ ��ֻ����RequestParameter����"a"����
	 * ����SaveImage(request,"a0","D:/","a0.png");
	 * 
	 * @author zhouzhifeng
	 * @param request
	 *            ��ص�����ͷ,get,post������
	 * @param RequestParameter
	 *            �����Ӧ��ͼƬkey
	 * @param filePath
	 *            �ļ�·��
	 * @param fileName
	 *            �ļ���
	 * @throws IOException
	 */

	@SuppressWarnings("unused")
	private void SaveImage(HttpServletRequest request, String RequestParameter,
			String filePath, String fileName) throws IOException {

		String uu = request.getParameter(RequestParameter);
		String[] url = uu.split(",");
		String u = url[1];

		byte[] b = new BASE64Decoder().decodeBuffer(u);
		File flie=new File(filePath + fileName);
		OutputStream out = new FileOutputStream(flie);
		out.write(b);
		out.flush();
		out.close();
	}
	/**
	 * ���ڴ����������Ƶ�Ψһ��ʶ,����������ʱ����������
	 * @author zhouzhifeng
	 * @return 
	 */
	private String createTimestr()//Ψһ�ַ���
	{
		Calendar cal = Calendar.getInstance();
		year = cal.get(Calendar.YEAR);//��ȡ���
        month=cal.get(Calendar.MONTH)+1;//��ȡ�·�
        day=cal.get(Calendar.DAY_OF_MONTH);//��ȡ��
        hour=cal.get(Calendar.HOUR_OF_DAY);//Сʱ
        minute=cal.get(Calendar.MINUTE);//��           
        second=cal.get(Calendar.SECOND);//��
        
        return ""+year+month+day+hour+minute+second;
	}

	/**
	 * @author zhouzhifeng
	 * �������ڽ���Ԥ������ʾ��ͼ,�κ��뱨����صĶ�Ӧ����д�����servlet�� ��Ӧ��ҳYSQL.jsp ǰ�����Ѿ������ݻ�ȡ
	 * 
	 * @return
	 */
	private String getPreviewMessage(String requestString, int max, int index) 
	{
		BriefingSummarize briefingSummarize = new BriefingSummarize();
		return briefingSummarize.getPreviewMessage(requestString, max, index);
	}
    /**
      * @author zhouzhifeng
      * @param max ��ȡ�ı�����������
      * @param index ����=(�ڼ�ҳ*max),����Ҳ��0��ʼ
      * @param userid �û���¼��id
      * @param basePath��ǰ���ʵ�Ŀ¼��ַ
      * @return
     */
	private String getIntimateBriefing(int max,int index,int userid,String basePath)
	{
		BriefingSummarize briefingSummarize=new BriefingSummarize();
		return briefingSummarize.getIntimateBriefing(max, index, userid,basePath);
	}
	

	
	
	/**
	 * @author zhouzhifeng
	 * ��ȡѡ��Ĵ���֮���Ԥ����ͼ��
	 * 
	 * @param requestString
	 *            �ֱ��Ӧѡ��������֮���id���ԡ�id1,id2,id3,...������
	 * @return
	 */

	private String getPreviewChart(String requestString,String templaterName) 
	{   
		
		String resJSON="";
		switch (templaterName) 
		{
		case "template1":
			BriefingSummarize briefingSummarize = new BriefingSummarize();
			resJSON=briefingSummarize.getPreviewChart1(requestString);
			break;
		default:
			break;
		}
		return resJSON;
	}

	/**
	 * @author zhouzhifeng
	 * ��Ϊ�Ǵ�ǰ�˴�������ͼ��ֻ�����ַ�������ʽ������, û�취�������,����ÿ��key�Ͳ�һ����,������ʱ���뷨
	 * �����ж��ٸ�ģ��,��Ҫ�ж��ٸ���Ӧ��ģ�庯��,׼ȷ��λÿ��λ��. ����ģ��1�ķ�����
	 * ÿ��������ÿ�����������Ҫ��,������servlet���Զ�����ѡ��
	 * 
	 * @param request
	 *            ���Ǵ���servlet�е�һЩ��Ϣ
	 * @throws IOException 
	 */
	@SuppressWarnings("deprecation")
	private void createTemplate1(HttpServletRequest request, HttpServletResponse response) throws IOException 
	{     
        //�����Ե�����.�ֱ��ӦNewsTend����tendClass����
		String[] state = { "��", "��", "��" };
		//template1.ftl �Ķ�Ӧͼ��
		String[] chartName = { "mtcome", "gTofmedia" };
        
		//����ģ���ļ�С����
		DocumentHandler documentHandler = new DocumentHandler();
		//freemarkerӳ������
		Map<String, Object> dataMap = new HashMap<String, Object>();
        //��������
		Calendar cal = Calendar.getInstance();
		//������������ģ��,��Ҫ��������
		BriefingData briefing = new BriefingData();
		//д��Ҫ��Ҫ������ļ���,filePathĬ��Ϊ��ǰservlet�����filepath
		briefing.setFilePath(filePath);
		//��ȡ��ѡ������ŵ�id���󴮣����С�requestStringΪ������id1,id2,id3,id4,...idn";
		String requestString = request.getParameter("requestString");
		//ʹ�õĶ�Ӧģ��
		briefing.setTemplateName("template1.ftl");
		//����Ҫ�������ļ���
		String uniqueName=createTimestr();
		briefing.setFileName("BEF"+uniqueName+".doc");
	
		
		//��ȡ�м���ͼƬ
		int limited = Integer.parseInt(request.getParameter("length"));
		
		//�����ϴ�������ͼƬ
		for(int i = 0; i < limited; i++) 
		{
			try 
			{  
				//��request�л�ȡ��Ӧ��base64��,��������ci��������
				SaveImage(request, "c" + i, filePath, "c" + i + ".png");
				//��ȡ��Ӧ�ĸոմ洢�õ�ͼƬ���뵽ӳ������
				dataMap.put(chartName[i], documentHandler.getImageStr(filePath
						+ "c" + i + ".png"));
			} catch (IOException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	
		
        //��Щ���Կ��Կ�template1.docһ�����ԡ�
		dataMap.put("theme", "�ϻ���ѧ");
		dataMap.put("year", cal.get(Calendar.YEAR));
		dataMap.put("month", cal.get(Calendar.MONTH) + 1);
		dataMap.put("day", cal.get(Calendar.DAY_OF_MONTH));

		System.out.println(cal.get(Calendar.YEAR) + " "
				+ cal.get(Calendar.MONTH) + cal.get(Calendar.DAY_OF_MONTH));
		
		//���ոյ�request��requeString���
		String[] obj = requestString.split(",");
        
		dataMap.put("NumberOfNews", obj.length);

		
		//������DAO��ȡʵ��
		NewsDAO newsDAO = new NewsDAO();
		//����������DAO��ȡʵ��
		NewsTendDAO newsTendDAO = new NewsTendDAO();
        
		//����ͳ����Դ����
		Map<String, Integer> countWeb = new HashMap<>();
		//����ͳ���������
		Map<String, Integer> countTendClass = new HashMap<>();
        
		//��template1.doc���Բ鿴,���Ƕ�Ӧ���ƵĹؼ�
		List<Map<String, Object>> newsList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> allnewsList = new ArrayList<Map<String, Object>>();
		
		//����
		for (int i = 0; i < obj.length; i++) 
		{
			News tmpNews = newsDAO.findById(Integer.parseInt(obj[i]));
			List<NewsTend> newsTends = newsTendDAO.findByNewsId(Integer
					.parseInt(obj[i]));
			NewsTend newsTend = newsTends.get(0);

			if (countWeb.get(tmpNews.getWeb()) == null) {
				countWeb.put(tmpNews.getWeb(), 1);
			} else {
				countWeb.put(tmpNews.getWeb(),
						countWeb.get(tmpNews.getWeb()) + 1);
			}

			if (countTendClass.get(state[checkstate(newsTend.getTendclass())]) == null) {
				countTendClass.put(state[checkstate(newsTend.getTendclass())], 1);
			} else {
				countTendClass.put(state[checkstate(newsTend.getTendclass())],
						countTendClass.get(state[checkstate(newsTend.getTendclass())]) + 1);
			}

			Map<String, Object> listKey = new HashMap<String, Object>();
			listKey.put("id", i+1);
			listKey.put("title", tmpNews.getTitle());
			listKey.put("web", tmpNews.getWeb());
			listKey.put("tendClass", state[checkstate(newsTend.getTendclass())]);
			newsList.add(listKey);
			
			
			Map<String, Object> alllistKey = new HashMap<String, Object>();
			alllistKey.put("id", i+1);
			alllistKey.put("title",tmpNews.getTitle());
			alllistKey.put("web", tmpNews.getWeb());
			String  timestr=tmpNews.getTime().toString();
			alllistKey.put("TTime",timestr);
			alllistKey.put("tendclass", state[checkstate(newsTend.getTendclass())]);
			alllistKey.put("tendscore", newsTend.getTendscore());
			alllistKey.put("allContent", tmpNews.getAllContent());
			alllistKey.put("url", tmpNews.getUrl());
			
			allnewsList.add(alllistKey);

		}
	
		
		//��template1.doc���Բ鿴
		dataMap.put("newsList", newsList);
		dataMap.put("allnewsList", allnewsList);

		String DetailOfNews = "";
		String MaxOfWeb = "";
		int vMaxOfWeb = 0;

		for (Entry<String, Integer> entry : countWeb.entrySet()) {
			DetailOfNews += (entry.getKey() + entry.getValue() + "ƪ��" + "ռ��"
					+(int)((double)entry.getValue() / obj.length * 100.0) + "%,");
			if (vMaxOfWeb < entry.getValue()) {
				vMaxOfWeb = entry.getValue();
				MaxOfWeb = entry.getKey();
			}
		}
		//��template1.doc���Բ鿴
		dataMap.put("DetailOfNews", DetailOfNews);
		dataMap.put("MaxOfWeb", MaxOfWeb);
		dataMap.put("vMaxOfWeb", vMaxOfWeb);
		String cMaxOfWeb = (int)((double)vMaxOfWeb / obj.length * 100.0) + "%";
		dataMap.put("cMaxOfWeb", cMaxOfWeb);

		int NevOfNews = 0;
		String vNevOfNews = "0%";

		int CenOfNews = 0;
		String vCenOfNews = "0%";

		int PosOfNews = 0;
		String vPosOfNews = "0%";

		for (Entry<String, Integer> entry : countTendClass.entrySet()) 
		{
			if (entry.getKey() == state[0]) {
				NevOfNews = entry.getValue();
				vNevOfNews = (int)((double)entry.getValue() / obj.length * 100.0) + "%";
			} else if (entry.getKey() == state[1]) {
				CenOfNews = entry.getValue();
				vCenOfNews = (int)((double)entry.getValue() / obj.length * 100.0) + "%";
			} else {
				PosOfNews = entry.getValue();
				vPosOfNews = (int)((double)entry.getValue() / obj.length * 100.0) + "%";
			}
		}
        
		//��template1.doc���Բ鿴
		dataMap.put("NevOfNews", NevOfNews);
		dataMap.put("vNevOfNews", vNevOfNews);

		dataMap.put("CenOfNews", CenOfNews);
		dataMap.put("vCenOfNews", vCenOfNews);

		dataMap.put("PosOfNews", PosOfNews);
		dataMap.put("vPosOfNews", vPosOfNews);
        
		
		briefing.setDataMap(dataMap);

		String userPath=(userid+"/");//Ϊÿ���û���������һ���ļ����Լ�������
		
		documentHandler.createWord(briefing,userPath);
		documentHandler.wordToPDF(briefing.getFilePath()+userPath+briefing.getFileName()
				                 ,briefing.getFilePath()+userPath+"BEF"+uniqueName+".pdf");//������һ��pdf��������վ��Ԥ��
		
		//����ʵ������ݿ�
	    BriefingDAO briefingDAO=new BriefingDAO();
		Briefing briefingEntity=new Briefing();
		briefingEntity.setName(briefing.getFileName());
		cal.set(year, month, day);
		Date date=cal.getTime();		
		briefingEntity.setTime(date);
		briefingEntity.setBasepath(basePath);
		briefingEntity.setDocpath(userPath+briefing.getFileName());
		briefingEntity.setPdfpath(userPath+"BEF"+uniqueName+".pdf");
		briefingEntity.setUserid(userid);
		briefingEntity.setEntityurl(briefing.getFilePath()+userPath+briefing.getFileName());
		briefingEntity.setVirtualname("�����"+year+"-"+month+"-"+day);
		briefingDAO.save(briefingEntity);
		
		
		ConsoleLog.PrintInfo(getClass(), "�������ɳɹ�:"+briefing.getFilePath()+userPath + briefing.getFileName());
		
		
	}
	
	private int checkstate(int val)
	{
		if(val==0) return 1;//��
		else if(val>0) return 2;//��
		
		return 0;//��
	}

}
