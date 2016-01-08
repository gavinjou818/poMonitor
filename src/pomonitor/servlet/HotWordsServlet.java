package pomonitor.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pomonitor.analyse.HotWordDiscoveryAnalyse;
import pomonitor.analyse.entity.HotWord;

/**
 * ÿһ��servlet��Ӧ��ǰ�˵�һ���߼�ҳ�棬ǰ�˿ɼ���ֻ�Ǻ͵�ǰҳ����ص����ɸ�����ӿڣ� ÿһ��servlet����Ҫ�ͺ�̨�Ĺ���ģ��һһ��Ӧ���ش�˵��
 * 
 * ��ǰservlet��Ӧ���ȴʷ���ҳ�棨�����ȴ�����ͼ�������б�
 * 
 * @author hengyi
 * 
 */
public class HotWordsServlet extends HttpServlet {

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
	String methodName = request.getParameter("method");
	String resultJson = "";
	// ��������ķ��������ض�Ӧ��Ϣ resultJson
	switch (methodName) {
	case "getHotWords":
	    // �����ȴʼ���
	    String startDateStr = request.getParameter("startTime");
	    String endDateStr = request.getParameter("endTime");
	    int userId = Integer.parseInt(request.getParameter("userId"));
	    resultJson = getHotWords(startDateStr, endDateStr, userId);
	    break;
	case "getNewsByTopic":
	    // �����ȴʱ�ŷ��������б�
	    resultJson = getNewsByTopic();
	    break;
	default:
	    break;
	}

	response.setContentType("application/json");
	response.setCharacterEncoding("UTF-8");
	response.getWriter().write(resultJson);
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
	HotWordDiscoveryAnalyse tdDiscovery = new HotWordDiscoveryAnalyse();
	List<HotWord> topics = tdDiscovery.DiscoverTopics(startDateStr,
		endDateStr, userId);
	String resJSON = "";
	/******************* �������б���ΪJSON��ʽ *****************************/

	/***********************************************************************/
	return resJSON;
    }

    /**
     * ����ָ�����ⷵ����ص������б�
     * 
     * @return
     */
    private String getNewsByTopic() {
	String resJSON = "";
	return resJSON;
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException {
	doGet(request, response);
    }

    public void init() throws ServletException {
	// Put your code here
    }

}
