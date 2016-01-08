package pomonitor.analyse;

import java.util.ArrayList;
import java.util.List;

import pomonitor.analyse.entity.TDArticle;
import pomonitor.analyse.entity.TDArticleTerm;
import pomonitor.analyse.entity.TDPosition;
import pomonitor.analyse.entity.HotWord;
import pomonitor.analyse.hotworddiscovery.HotWordDiscovery;
import pomonitor.analyse.segment.TermsGenerator;
import pomonitor.entity.News;
import pomonitor.entity.NewsDAO;
import pomonitor.entity.SenswordDAO;
import pomonitor.util.ConsoleLog;

import com.hankcs.hanlp.seg.common.Term;

/**
 * ���ⷢ��ģ��, ����Controller�����Ļ��ⷢ��ģ��֮��;���϶�controller����; ���µ��û��ⷢ�־���������ṩ�Ĺ���
 * 
 * @author caihengyi 2015��12��15�� ����4:12:07
 */
public class HotWordDiscoveryAnalyse {

    /**
     * �����ض��û������дʿ⣬��ȡһ��ʱ���������ı��Ļ��⼯��
     * 
     * @param startDateStr
     * @param endDateStr
     * @param userId
     * @return
     */
    public List<HotWord> DiscoverTopics(String startDateStr, String endDateStr,
	    int userId) {
	// ���û��ⷢ�ֹ���ģ�飬���ػ��⼯��
	HotWordDiscovery td = new HotWordDiscovery();
	SenswordDAO sd = new SenswordDAO();
	return td.getTopics(getArticlesBetweenDate(startDateStr, endDateStr),
		sd.findByProperty("userid", userId));
    }

    public List<TDArticle> getArticlesBetweenDate(String startDateStr,
	    String endDateStr) {
	// ������ֹʱ���ȡ���ݿ��е������ı�
	NewsDAO nd = new NewsDAO();
	long start = System.currentTimeMillis();
	List<News> newsList = nd.findBetweenDate(startDateStr, endDateStr);
	long end = System.currentTimeMillis();
	ConsoleLog.PrintInfo(HotWordDiscoveryAnalyse.class, "�����ݿ���ȡ��"
		+ startDateStr + "��" + endDateStr + "���ı�������ʱ��Ϊ" + (end - start)
		+ "����");
	// ���ִʣ�����Ԥ����
	List<TDArticle> tdArticleList = new ArrayList<TDArticle>();
	TermsGenerator generateTerms = new TermsGenerator();
	for (News news : newsList) {
	    TDArticle tmpArt = new TDArticle();
	    List<TDArticleTerm> tmpTDArtTerms = new ArrayList<TDArticleTerm>();

	    List<Term> tmpTermList_allcontent = generateTerms.getTerms(news
		    .getAllContent());
	    List<Term> tmpTermList_keyword = generateTerms.getTerms(news
		    .getKeyWords());
	    List<Term> tmpTermList_title = generateTerms.getTerms(news
		    .getTitle());
	    for (Term term : tmpTermList_allcontent) {
		TDArticleTerm tmpArtTerm = new TDArticleTerm();
		tmpArtTerm.setposition(TDPosition.BODY);
		tmpArtTerm.setvalue(term.word);
		tmpTDArtTerms.add(tmpArtTerm);
	    }
	    for (Term term : tmpTermList_keyword) {
		TDArticleTerm tmpArtTerm = new TDArticleTerm();
		tmpArtTerm.setposition(TDPosition.META);
		tmpArtTerm.setvalue(term.word);
		tmpTDArtTerms.add(tmpArtTerm);
	    }
	    for (Term term : tmpTermList_title) {
		TDArticleTerm tmpArtTerm = new TDArticleTerm();
		tmpArtTerm.setposition(TDPosition.TITLE);
		tmpArtTerm.setvalue(term.word);
		tmpTDArtTerms.add(tmpArtTerm);
	    }
	    tmpArt.setArticleAllTerms(tmpTDArtTerms);
	    tmpArt.setComeFrom(news.getWeb());
	    tmpArt.setDescription(news.getContent());
	    tmpArt.setTimestamp(news.getTime());
	    tmpArt.setTitle(news.getTitle());
	    tmpArt.setUrl(news.getUrl());
	    tdArticleList.add(tmpArt);
	}
	return tdArticleList;
    }

}
