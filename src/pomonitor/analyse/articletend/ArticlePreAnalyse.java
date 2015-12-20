package pomonitor.analyse.articletend;

import java.util.List;

import pomonitor.analyse.entity.TendAnalyseArticle;
import pomonitor.analyse.entity.TendSentence;
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

	// ���·�����
	private ArticleSplier articleSplier;

	public ArticlePreAnalyse(ArticleSplier articleSplier) {
		this.articleSplier = new ArticleSplier();
		sentenceSplier = new SentenceSplier();
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
		List<TendSentence> relSentences = articleSplier.spil(content);
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
