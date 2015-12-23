package pomonitor.analyse.articletend;

import java.util.ArrayList;
import java.util.List;

import pomonitor.analyse.entity.TendSentence;
import pomonitor.analyse.entity.TendAnalyseArticle;
import pomonitor.analyse.entity.TendWord;
import pomonitor.entity.NewsEntity;

/**
 * ����Ԥ���������������ַ�������Ԥ����Ϊ��Ҫ������Article����
 * 
 * @author zhaolong 2015��12��16�� ����9:31:20
 */
public class ArticlePreAnalyse {

	private NewsEntity news;

	private TendAnalyseArticle article;

	// ���ӷ�����
	private SentenceSplier sentenceSplier;

	public ArticlePreAnalyse(SentenceSplier sentenceSplier) {
		this.sentenceSplier = new SentenceSplier();
	}

	/**
	 * ��ʼ��һƪ���£����ػ�������
	 * 
	 * @param news
	 */
	private void init(NewsEntity news) {
		this.news = news;
		article = new TendAnalyseArticle();
		article.setKeyWords(news.getKeywords());
		article.setTitle(news.getTitle());
	}

	/**
	 * �Ͼ䲢�ҷ���ÿһ�䣬��Ҫ������������
	 */
	private void splitArticle() {
		String content = news.getAllContent();
		String[] sentenceStrs = content.split("��");
		List<TendSentence> relSentences = new ArrayList<TendSentence>();
		for (int i = 0; i < sentenceStrs.length; i++) {
			String sentenceStr = sentenceStrs[i].trim();
			if (sentenceStr.length() > 5) {
				TendSentence sentence = new TendSentence();
				sentence.setId(i);
				List<TendWord> list = sentenceSplier.spil(sentenceStr);
				sentence.setWords(list);
				relSentences.add(sentence);
			}
		}
		article.setSentences(relSentences);
	}

	/**
	 * �����ṩ������Ԥ����������ʽ������õ������Դ�����
	 * 
	 * @param news
	 * @return TendAnalyseArticle
	 */
	public TendAnalyseArticle getPreArticle(NewsEntity news) {
		init(news);
		splitArticle();
		return article;
	}

}
