package pomonitor.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import pomonitor.analyse.HotWordDiscoveryAnalyse;
import pomonitor.analyse.entity.HotWord;
import pomonitor.analyse.entity.HotWordNewsResponse;
import pomonitor.analyse.entity.HotWordResponse;
import pomonitor.analyse.entity.HotWordResult;
import pomonitor.analyse.entity.HotWordsListResponse;
import pomonitor.analyse.entity.RetHotWord;
import pomonitor.analyse.entity.RetLink;
import pomonitor.util.ConsoleLog;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * 每一个servlet对应于前端的一个逻辑页面，前端可见的只是和当前页面相关的若干个请求接口， 每一个servlet不需要和后台的功能模块一一对应，特此说明
 * 
 * 当前servlet对应于热词发现页面（包括热词线球图和新闻列表）
 * 
 * @author hengyi
 * 
 */
public class HotWordsServlet extends HttpServlet {
	HotWordDiscoveryAnalyse tdDiscovery;
	
	public HotWordsServlet() {
		super();
	}

	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	/**
	 * doGet或者doPost在这里相当于转发器，根据method参数调用对应方法，统一向外返回resultJson(不同分支赋予不同内容)
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * 返回热词集合
	 * 
	 * 问题：话题下巨多的新闻列表信息，需要延迟加载
	 * 
	 * @param startDateStr
	 * @param endDateStr
	 * @param userId
	 * @return
	 */
	private String getHotWords(String startDateStr, String endDateStr,
			int userId) {
		/******************* 将热词列表处理为JSON格式 *****************************/
		ArrayList<RetHotWord> retNodes = tdDiscovery.getRetHotWords();
		double[][] relevanceMat = tdDiscovery.getRelevanceMat();
		
		ArrayList<RetLink> retLinks = new ArrayList<RetLink>();
		for (int i = 0; i < relevanceMat.length; i++) {
			for (int j = i + 1; j < relevanceMat.length; j++) {

				if (relevanceMat[i][j] < 0.25)
					continue;
				else {
					RetLink _link = new RetLink();
					_link.setSource(i);
					_link.setTarget(j);
					_link.setWeight(relevanceMat[i][j] * 1000);
					retLinks.add(_link);
				}
			}
		}
		HotWordResult results = new HotWordResult();
		results.setLinks(retLinks);
		results.setNodes(retNodes);
		HotWordResponse hotWordResponse = new HotWordResponse();
		hotWordResponse.setResults(results);
		hotWordResponse.setMessage("处理成功!");
		hotWordResponse.setStatus(0);
		String retJSON = JSON.toJSONString(hotWordResponse);
		/***********************************************************************/
		ConsoleLog.PrintInfo(getClass(), retJSON);
		return retJSON;
	}
	/**
	 * 返回所有热词信息
	 * @return
	 */
	private String getHotWordsList(String startDateStr, String endDateStr,
			int userId) {
		List<RetHotWord> hotwords=tdDiscovery.getRetHotWords();
		HotWordsListResponse hotWordsListResponse = new HotWordsListResponse();
		hotWordsListResponse.setResults(hotwords);
		hotWordsListResponse.setMessage("处理成功!");
		hotWordsListResponse.setStatus(0);
		String resJSON = JSON.toJSONString(hotWordsListResponse);
		return resJSON;
	}
	
	/**
	 * 根据指定话题返回相关的新闻列表
	 * 
	 * @return
	 */
	private String getNewsByHotWord(int hotwordid) {
		List<HotWord> hotwords=tdDiscovery.getHotwords();
		HotWord res=hotwords.get(hotwordid);
		HotWordNewsResponse hotWordNewsResponse=new HotWordNewsResponse();
		String resJSON="";
		if(res!=null){
			hotWordNewsResponse.setResults(res);
			hotWordNewsResponse.setMessage("处理成功!");
			hotWordNewsResponse.setStatus(0);
			resJSON = JSON.toJSONString(hotWordNewsResponse);

		}
		else{
			hotWordNewsResponse.setMessage("未找到此热词！");
			hotWordNewsResponse.setStatus(1);
			hotWordNewsResponse.setResults(null);
			resJSON = JSON.toJSONString(hotWordNewsResponse);
		}
		return resJSON;
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		HttpSession session=request.getSession(true);
		
		String methodName = request.getParameter("method");
		String resultJson = "";
		if (methodName == null) {
			HotWordResponse res = new HotWordResponse();
			res.setMessage("method参数为空！");
			res.setStatus(1);
			res.setResults(null);
			resultJson = JSON.toJSONString(res);
		} else {
			String startDateStr = request.getParameter("startTime");
			String endDateStr = request.getParameter("endTime");
			int userId = Integer.parseInt(request.getParameter("userId"));
			this.tdDiscovery = new HotWordDiscoveryAnalyse();
			tdDiscovery.discoverHotWords(startDateStr, endDateStr, userId);
			
			// 根据请求的方法，返回对应信息 resultJson
			switch (methodName) {
			case "getHotWords":
				// 返回热词集合
				resultJson = getHotWords(startDateStr, endDateStr, userId);
				break;
			case "getNewsByHotWord":
				// 根据热词List的id返回新闻列表
				int hotwordid = Integer.parseInt(request.getParameter("hotwordid"));
				resultJson = getNewsByHotWord(hotwordid);
				break;
			case "getHotWordsList":
				resultJson =getHotWordsList(startDateStr, endDateStr, userId);
				break;
			default:
				break;
			}
		}
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(resultJson);
	}

	public void init() throws ServletException {
		// Put your code here
	}

}
