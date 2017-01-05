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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pomonitor.briefing.Briefing;
import pomonitor.briefing.DocumentHandler;
import pomonitor.entity.News;
import pomonitor.entity.NewsDAO;
import pomonitor.entity.NewsTend;
import pomonitor.entity.NewsTendDAO;
import pomonitor.statistics.BriefingSummarize;
import pomonitor.util.PropertiesReader;
import sun.misc.BASE64Decoder;

/**
 * 
 * @author zhouzhifeng 2016/12/19 本Servlet用于接收前端发界面发来的png,并且用于生成相应模板报表,
 *         前提一定要每个一模板位置都要有对应的输入.相应数据传输在briefing.html中
 */
public class sImageAndcTemplateServlet extends HttpServlet {
	private static String filePath;

	// 获取图片保存路径
	static {
		PropertiesReader propertiesReader = new PropertiesReader();
		filePath = propertiesReader.getPropertyByName("IMG_savePath");
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
		String method = request.getParameter("method");
		String requestString = request.getParameter("requestString");
		String resJSON = "";

		System.out.println("---->" + method);
		switch (method) {
		case "getPreviewBriefing":

			// 获取最大字条
			int max = Integer.parseInt(request.getParameter("max"));
			// 获取开始序列
			int index = Integer.parseInt(request.getParameter("index"));
			resJSON = getPreviewBriefing(requestString, max, index);
			break;
		case "getPreviewChart":
			// 获取最大字条

			resJSON = getPreviewChart(requestString);
			System.out.println("--->" + resJSON);
			break;
		case "createTemplate1":
			// 获取最大字条
			resJSON = "{\"message\":\"成功生成报表\"}";
			createTemplate1(request);
			break;
		default:
			break;
		}

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
		OutputStream out = new FileOutputStream(new File(filePath + fileName));
		out.write(b);
		out.flush();
		out.close();
	}

	/**
	 * 该类用于建立预览报表示意图,任何与报表相关的都应该填写在这个servlet中 对应网页YSQL.jsp 前提是已经把数据获取
	 * 
	 * @return
	 */
	private String getPreviewBriefing(String requestString, int max, int index) {
		BriefingSummarize briefingSummarize = new BriefingSummarize();
		return briefingSummarize.getPreviewMessage(requestString, max, index);
	}

	/**
	 * 获取选择的词条之后的预览的图表
	 * 
	 * @param requestString
	 *            分别对应选择了新闻之后的id，以“id1,id2,id3,...”输入
	 * @return
	 */

	private String getPreviewChart(String requestString) {
		BriefingSummarize briefingSummarize = new BriefingSummarize();
		return briefingSummarize.getPreviewChart(requestString);
	}

	/**
	 * 因为是从前端传过来的图表只能用字符串的形式传过来, 没办法用数组存,对于每个key就不一样了,所以暂时的想法
	 * 就是有多少个模板,就要有多少个对应的模板函数,准确定位每个位置. 创造模板1的方法。
	 * 每个报表有每个报表的生成要求,所以在servlet中自动进行选择。
	 * 
	 * @param request
	 *            这是存在servlet中的一些信息
	 */
	private void createTemplate1(HttpServletRequest request) {

		String[] state = { "负", "中", "正" };
		String[] chartName = { "mtcome", "gTofmedia" };

		DocumentHandler documentHandler = new DocumentHandler();
		Map<String, Object> dataMap = new HashMap<String, Object>();

		Calendar cal = Calendar.getInstance();
		// 创建基本报表模型,主要填入数据
		Briefing briefing = new Briefing();
		String requestString = request.getParameter("requestString");
		// 使用的对应模板
		briefing.setTemplateName("template1.ftl");
		briefing.setFileName("a.doc");

		int limited = Integer.parseInt(request.getParameter("length"));
		for (int i = 0; i < limited; i++) {
			try {
				SaveImage(request, "c" + i, filePath, "c" + i + ".png");
				dataMap.put(chartName[i], documentHandler.getImageStr(filePath
						+ "c" + i + ".png"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		dataMap.put("theme", "南华大学");

		dataMap.put("year", cal.get(Calendar.YEAR));
		dataMap.put("month", cal.get(Calendar.MONTH) + 1);
		dataMap.put("day", cal.get(Calendar.DAY_OF_MONTH));

		System.out.println(cal.get(Calendar.YEAR) + " "
				+ cal.get(Calendar.MONTH) + cal.get(Calendar.DAY_OF_MONTH));
		String[] obj = requestString.split(",");

		dataMap.put("NumberOfNews", obj.length);

		NewsDAO newsDAO = new NewsDAO();
		NewsTendDAO newsTendDAO = new NewsTendDAO();

		Map<String, Integer> countWeb = new HashMap<>();
		Map<String, Integer> countTendClass = new HashMap<>();

		List<Map<String, Object>> newsList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> allnewsList = new ArrayList<Map<String, Object>>();//
		
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

			if (countTendClass.get(state[newsTend.getTendclass()]) == null) {
				countTendClass.put(state[newsTend.getTendclass()], 1);
			} else {
				countTendClass.put(state[newsTend.getTendclass()],
						countTendClass.get(state[newsTend.getTendclass()]) + 1);
			}

			Map<String, Object> listKey = new HashMap<String, Object>();
			listKey.put("id", i+1);
			listKey.put("title", tmpNews.getTitle());
			listKey.put("web", tmpNews.getWeb());
			listKey.put("tendClass", state[newsTend.getTendclass()+1]);
			newsList.add(listKey);
			
			
			Map<String, Object> alllistKey = new HashMap<String, Object>();
			alllistKey.put("id", i+1);
			alllistKey.put("title",tmpNews.getTitle());
			alllistKey.put("web", tmpNews.getWeb());
			String  timestr=tmpNews.getTime().toString();
			alllistKey.put("TTime",timestr);
			alllistKey.put("tendclass", newsTend.getTendclass());
			alllistKey.put("tendscore", newsTend.getTendscore());
			alllistKey.put("allContent", tmpNews.getAllContent());
			alllistKey.put("url", tmpNews.getUrl());
			
			allnewsList.add(alllistKey);

		}

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

		for (Entry<String, Integer> entry : countTendClass.entrySet()) {
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

		dataMap.put("NevOfNews", NevOfNews);
		dataMap.put("vNevOfNews", vNevOfNews);

		dataMap.put("CenOfNews", CenOfNews);
		dataMap.put("vCenOfNews", vCenOfNews);

		dataMap.put("PosOfNews", PosOfNews);
		dataMap.put("vPosOfNews", vPosOfNews);

		briefing.setDataMap(dataMap);

		System.out.println(briefing.getFilePath() + "  "
				+ briefing.getFileName() + " " + briefing.getTemplateName());
		documentHandler.createWord(briefing);
	}

}
