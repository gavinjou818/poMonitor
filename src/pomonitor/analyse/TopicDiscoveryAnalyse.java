package pomonitor.analyse;

import java.util.ArrayList;
import java.util.List;

import pomonitor.analyse.entity.TDArticle;
import pomonitor.analyse.entity.TDArticleTerm;
import pomonitor.analyse.entity.TDPosition;
import pomonitor.analyse.entity.Topic;
import pomonitor.analyse.segment.TermsGenerator;
import pomonitor.analyse.topicdiscovery.TopicDiscovery;
import pomonitor.entity.News;
import pomonitor.entity.NewsDAO;
import pomonitor.entity.SenswordDAO;

import com.hankcs.hanlp.seg.common.Term;

/**
 * ���ⷢ��ģ��, ����Controller�����Ļ��ⷢ��ģ��֮��;���϶�controller����; ���µ��û��ⷢ�־���������ṩ�Ĺ���
 * 
 * @author caihengyi 2015��12��15�� ����4:12:07
 */
public class TopicDiscoveryAnalyse {

	/**
	 * �����ض��û������дʿ⣬��ȡһ��ʱ���������ı��Ļ��⼯��
	 * 
	 * @param startDateStr
	 * @param endDateStr
	 * @param userId
	 * @return
	 */
	public List<Topic> DiscoverTopics(String startDateStr, String endDateStr,
			int userId) {
		// ������ֹʱ���ȡ���ݿ��е������ı�
		NewsDAO nd = new NewsDAO();
		List<News> newsList = nd.findBetweenDate(startDateStr, endDateStr);
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
			tmpArt.setArticleTerms(tmpTDArtTerms);
			tmpArt.setComeFrom(news.getWeb());
			tmpArt.setDescription(news.getContent());
			tmpArt.setTimestamp(news.getTime());
			tmpArt.setTitle(news.getTitle());
			tmpArt.setUrl(news.getUrl());
			tdArticleList.add(tmpArt);
		}
		// ���û��ⷢ�ֹ���ģ�飬���ػ��⼯��
		TopicDiscovery td = new TopicDiscovery();
		SenswordDAO sd = new SenswordDAO();
		return td.getTopics(tdArticleList, sd.findByProperty("userid", userId));
	}
}
