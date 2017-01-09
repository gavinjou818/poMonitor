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
 * 理解前先要理解freemarket+echarts
 * @author zhouzhifeng 2016/12/19 本Servlet用于接收前端发界面发来的png,并且用于生成相应模板报表,
 *         前提一定要每个一模板位置都要有对应的输入.相应数据传输在briefing.html中
 */
public class sImageAndcTemplateServlet extends HttpServlet
{
	private static String filePath;//文件在系统中的真实路径
	private static String basePath;//文件在访问地址中的相对路径
	private static String BEF_savePath;//所在保存的包
	
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
		case "getPreviewMessage"://获取预览所选择的词条信息

			// 获取最大新闻条数
			max = Integer.parseInt(request.getParameter("max"));
			// 获取开始索引 index=(max*第几页) 开始索引为0
			index = Integer.parseInt(request.getParameter("index"));
			resJSON = getPreviewMessage(requestString, max, index);
			break;
		case "getPreviewChart"://根据所选择的词条信息,创建所需要的图表
			// 获取最图表
			resJSON = getPreviewChart(requestString,request.getParameter("templateName"));
			break;
		case "createTemplate1":
			resJSON = "{\"message\":\"成功生成报表\"}";
			createTemplate1(request,response);
			break;
		case "getIntimateBriefing":	//得到私人生成的报表,这是不公开的,自己生成的便是自己的报表.
			// 获取最大字条
			max = Integer.parseInt(request.getParameter("max"));
			// 获取开始序列
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
	 * 本功能用于获取相应request的图片参数, 然后将其解码转为png 保存到filePath+fileName中
	 * 例如上传的参数为"a=img/....&a1=img..."; 若要先获取a的图片 则只需在RequestParameter输入"a"即可
	 * 例子SaveImage(request,"a0","D:/","a0.png");
	 * 
	 * @author zhouzhifeng
	 * @param request
	 *            相关的请求头,get,post都可以
	 * @param RequestParameter
	 *            请求对应的图片key
	 * @param filePath
	 *            文件路径
	 * @param fileName
	 *            文件名
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
	 * 用于创建报表名称的唯一标识,根据年月日时分秒来定。
	 * @author zhouzhifeng
	 * @return 
	 */
	private String createTimestr()//唯一字符串
	{
		Calendar cal = Calendar.getInstance();
		year = cal.get(Calendar.YEAR);//获取年份
        month=cal.get(Calendar.MONTH)+1;//获取月份
        day=cal.get(Calendar.DAY_OF_MONTH);//获取日
        hour=cal.get(Calendar.HOUR_OF_DAY);//小时
        minute=cal.get(Calendar.MINUTE);//分           
        second=cal.get(Calendar.SECOND);//秒
        
        return ""+year+month+day+hour+minute+second;
	}

	/**
	 * @author zhouzhifeng
	 * 该类用于建立预览报表示意图,任何与报表相关的都应该填写在这个servlet中 对应网页YSQL.jsp 前提是已经把数据获取
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
      * @param max 获取的报表的最大条数
      * @param index 索引=(第几页*max),索引也从0开始
      * @param userid 用户登录的id
      * @param basePath当前访问的目录地址
      * @return
     */
	private String getIntimateBriefing(int max,int index,int userid,String basePath)
	{
		BriefingSummarize briefingSummarize=new BriefingSummarize();
		return briefingSummarize.getIntimateBriefing(max, index, userid,basePath);
	}
	

	
	
	/**
	 * @author zhouzhifeng
	 * 获取选择的词条之后的预览的图表
	 * 
	 * @param requestString
	 *            分别对应选择了新闻之后的id，以“id1,id2,id3,...”输入
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
	 * 因为是从前端传过来的图表只能用字符串的形式传过来, 没办法用数组存,对于每个key就不一样了,所以暂时的想法
	 * 就是有多少个模板,就要有多少个对应的模板函数,准确定位每个位置. 创造模板1的方法。
	 * 每个报表有每个报表的生成要求,所以在servlet中自动进行选择。
	 * 
	 * @param request
	 *            这是存在servlet中的一些信息
	 * @throws IOException 
	 */
	@SuppressWarnings("deprecation")
	private void createTemplate1(HttpServletRequest request, HttpServletResponse response) throws IOException 
	{     
        //倾向性的趋势.分别对应NewsTend表下tendClass类型
		String[] state = { "负", "中", "正" };
		//template1.ftl 的对应图表
		String[] chartName = { "mtcome", "gTofmedia" };
        
		//创建模板文件小助手
		DocumentHandler documentHandler = new DocumentHandler();
		//freemarker映射数据
		Map<String, Object> dataMap = new HashMap<String, Object>();
        //创建日历
		Calendar cal = Calendar.getInstance();
		//创建基本报表模型,主要填入数据
		BriefingData briefing = new BriefingData();
		//写入要所要存入的文件夹,filePath默认为当前servlet定义的filepath
		briefing.setFilePath(filePath);
		//获取所选择的新闻的id请求串，其中“requestString为”：“id1,id2,id3,id4,...idn";
		String requestString = request.getParameter("requestString");
		//使用的对应模板
		briefing.setTemplateName("template1.ftl");
		//设置要创建的文件名
		String uniqueName=createTimestr();
		briefing.setFileName("BEF"+uniqueName+".doc");
	
		
		//获取有几个图片
		int limited = Integer.parseInt(request.getParameter("length"));
		
		//储存上传过来的图片
		for(int i = 0; i < limited; i++) 
		{
			try 
			{  
				//从request中获取对应的base64码,其中我以ci来命名的
				SaveImage(request, "c" + i, filePath, "c" + i + ".png");
				//获取对应的刚刚存储好的图片加入到映射类中
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
	
		
        //这些属性可以看template1.doc一个个对。
		dataMap.put("theme", "南华大学");
		dataMap.put("year", cal.get(Calendar.YEAR));
		dataMap.put("month", cal.get(Calendar.MONTH) + 1);
		dataMap.put("day", cal.get(Calendar.DAY_OF_MONTH));

		System.out.println(cal.get(Calendar.YEAR) + " "
				+ cal.get(Calendar.MONTH) + cal.get(Calendar.DAY_OF_MONTH));
		
		//将刚刚的request的requeString拆分
		String[] obj = requestString.split(",");
        
		dataMap.put("NumberOfNews", obj.length);

		
		//打开新闻DAO获取实体
		NewsDAO newsDAO = new NewsDAO();
		//打开新闻倾向DAO获取实体
		NewsTendDAO newsTendDAO = new NewsTendDAO();
        
		//用来统计来源个数
		Map<String, Integer> countWeb = new HashMap<>();
		//用来统计倾向个数
		Map<String, Integer> countTendClass = new HashMap<>();
        
		//打开template1.doc可以查看,都是对应名称的关键
		List<Map<String, Object>> newsList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> allnewsList = new ArrayList<Map<String, Object>>();
		
		//计算
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
	
		
		//打开template1.doc可以查看
		dataMap.put("newsList", newsList);
		dataMap.put("allnewsList", allnewsList);

		String DetailOfNews = "";
		String MaxOfWeb = "";
		int vMaxOfWeb = 0;

		for (Entry<String, Integer> entry : countWeb.entrySet()) {
			DetailOfNews += (entry.getKey() + entry.getValue() + "篇，" + "占比"
					+(int)((double)entry.getValue() / obj.length * 100.0) + "%,");
			if (vMaxOfWeb < entry.getValue()) {
				vMaxOfWeb = entry.getValue();
				MaxOfWeb = entry.getKey();
			}
		}
		//打开template1.doc可以查看
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
        
		//打开template1.doc可以查看
		dataMap.put("NevOfNews", NevOfNews);
		dataMap.put("vNevOfNews", vNevOfNews);

		dataMap.put("CenOfNews", CenOfNews);
		dataMap.put("vCenOfNews", vCenOfNews);

		dataMap.put("PosOfNews", PosOfNews);
		dataMap.put("vPosOfNews", vPosOfNews);
        
		
		briefing.setDataMap(dataMap);

		String userPath=(userid+"/");//为每个用户独立建立一个文件夹以减少索引
		
		documentHandler.createWord(briefing,userPath);
		documentHandler.wordToPDF(briefing.getFilePath()+userPath+briefing.getFileName()
				                 ,briefing.getFilePath()+userPath+"BEF"+uniqueName+".pdf");//多生成一份pdf用于在网站上预览
		
		//保存实体进数据库
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
		briefingEntity.setVirtualname("经典版"+year+"-"+month+"-"+day);
		briefingDAO.save(briefingEntity);
		
		
		ConsoleLog.PrintInfo(getClass(), "报表生成成功:"+briefing.getFilePath()+userPath + briefing.getFileName());
		
		
	}
	
	private int checkstate(int val)
	{
		if(val==0) return 1;//中
		else if(val>0) return 2;//正
		
		return 0;//负
	}

}
