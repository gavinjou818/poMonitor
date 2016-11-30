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
 * ÿһ��servlet��Ӧ��ǰ�˵�һ���߼�ҳ�棬ǰ�˿ɼ���ֻ�Ǻ͵�ǰҳ����ص����ɸ�����ӿڣ� ÿһ��servlet����Ҫ�ͺ�̨�Ĺ���ģ��һһ��Ӧ���ش�˵��
 * 
 * ��ǰservlet��Ӧ���ȴʷ���ҳ�棨�����ȴ�����ͼ�������б�
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
	 * doGet����doPost�������൱��ת����������method�������ö�Ӧ������ͳһ���ⷵ��resultJson(��ͬ��֧���費ͬ����)
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * �����ȴʼ���
	 * 
	 * ���⣺�����¾޶�������б���Ϣ����Ҫ�ӳټ���
	 * 
	 * @param startDateStr
	 * @param endDateStr
	 * @param userId
	 * @return
	 */
	private String getHotWords(String startDateStr, String endDateStr,
			int userId) {
		/******************* ���ȴ��б���ΪJSON��ʽ *****************************/
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
		hotWordResponse.setMessage("����ɹ�!");
		hotWordResponse.setStatus(0);
		String retJSON = JSON.toJSONString(hotWordResponse);
		/***********************************************************************/
		ConsoleLog.PrintInfo(getClass(), retJSON);
		return retJSON;
	}
	/**
	 * ���������ȴ���Ϣ
	 * @return
	 */
	private String getHotWordsList(String startDateStr, String endDateStr,
			int userId) {
		List<RetHotWord> hotwords=tdDiscovery.getRetHotWords();
		HotWordsListResponse hotWordsListResponse = new HotWordsListResponse();
		hotWordsListResponse.setResults(hotwords);
		hotWordsListResponse.setMessage("����ɹ�!");
		hotWordsListResponse.setStatus(0);
		String resJSON = JSON.toJSONString(hotWordsListResponse);
		
		ConsoleLog.PrintInfo(getClass(), resJSON);
		return resJSON;
	}
	
	/**
	 * ����ָ�����ⷵ����ص������б�
	 * 
	 * @return
	 */
	private String getNewsByHotWord(int hotwordid) {
		List<HotWord> hotwords=tdDiscovery.getHotwords();
		HotWord res = null;
		res=hotwords.get(hotwordid);
		HotWordNewsResponse hotWordNewsResponse=new HotWordNewsResponse();
		String resJSON="";
		if(res!=null){
			hotWordNewsResponse.setResults(res);
			hotWordNewsResponse.setMessage("����ɹ�!");
			hotWordNewsResponse.setStatus(0);
			resJSON = JSON.toJSONString(hotWordNewsResponse);

		}
		else{
			hotWordNewsResponse.setMessage("δ�ҵ����ȴʣ�");
			hotWordNewsResponse.setStatus(1);
			hotWordNewsResponse.setResults(null);
			resJSON = JSON.toJSONString(hotWordNewsResponse);
		}
		ConsoleLog.PrintInfo(getClass(), resJSON);
		return resJSON;
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String methodName = request.getParameter("method");
		String resultJson = "";
		if (methodName == null) {
			HotWordResponse res = new HotWordResponse();
			res.setMessage("method����Ϊ�գ�");
			res.setStatus(1);
			res.setResults(null);
			resultJson = JSON.toJSONString(res);
		} else {
			String startDateStr = request.getParameter("startTime");
			String endDateStr = request.getParameter("endTime");
			int userId = Integer.parseInt(request.getParameter("userId"));
			HttpSession session=request.getSession(true);
			this.tdDiscovery=(HotWordDiscoveryAnalyse)session.getAttribute(startDateStr+endDateStr+userId);
			if(this.tdDiscovery==null){
				this.tdDiscovery = new HotWordDiscoveryAnalyse();
				tdDiscovery.discoverHotWords(startDateStr, endDateStr, userId);
				session.setAttribute(startDateStr+endDateStr+userId,this.tdDiscovery);
			}
			// ��������ķ��������ض�Ӧ��Ϣ resultJson
			switch (methodName) {
			case "getHotWords":
				// �����ȴʼ���
				resultJson = getHotWords(startDateStr, endDateStr, userId);
				break;
			case "getNewsByHotWord":
				// �����ȴ�List��id���������б�
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
