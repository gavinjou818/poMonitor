package pomonitor.analyse;

import java.util.List;

import org.junit.Test;

import pomonitor.analyse.articletend.ArticleTendAnalyseRealize;
import pomonitor.analyse.articletend.IArticleTendAnalyse;
import pomonitor.analyse.entity.TendAnalyseArticle;
import pomonitor.analyse.entity.TendSentence;
import pomonitor.analyse.entity.TendWord;
import pomonitor.entity.EntityManagerHelper;
import pomonitor.entity.News;
import pomonitor.entity.NewsDAO;
import pomonitor.entity.NewsTend;
import pomonitor.entity.NewsTendDAO;

/**
 * �����Է������������ṩ�Ľӿ�
 * 
 * @author zhaolong 2015��12��25�� ����2:11:35
 */
public class TendDiscoveryAnalyse {
	private NewsDAO newsDao;
	private NewsTendDAO newsTendDao;

	public TendDiscoveryAnalyse() {
		newsDao = new NewsDAO();
		newsTendDao = new NewsTendDAO();
	}

	/**
	 * ������������,ÿ��ֻ����û����ɵģ�������News�е�isFinish��Ϊ�Ƿ������ɣ� 1��ʾû�У�0��ʾ�Ѿ����
	 */
	public void startTendAnalyse() {
		NewsDAO newsDao = new NewsDAO();
		// ֻ����Щû���������Է���������
		List<News> list = newsDao.findByIsFinsh(1);
		// ���������Է�����
		IArticleTendAnalyse articleTendAnalyse = new ArticleTendAnalyseRealize();
		for (News news : list) {
			try {
				TendAnalyseArticle article = articleTendAnalyse
						.analyseArticleTend(news);
				// ���÷�����ɱ�־
				news.setIsFinsh(0);
				// ����news������
				EntityManagerHelper.beginTransaction();
				newsDao.update(news);
				EntityManagerHelper.commit();
				NewsTend newsTend = new NewsTend();
				newsTend.setDate(news.getTime());
				newsTend.setTendscore(article.getTendScore());
				newsTend.setNewsId(news.getRelId());
				newsTend.setWeb(news.getWeb());
				// �˴��ļ������һ������
				newsTend.setTendclass(1);
				// �־û��������
				EntityManagerHelper.beginTransaction();
				newsTendDao.save(newsTend);
				EntityManagerHelper.commit();
				// �м䲿�����������̵Ĺ۲������Ϊ�˲���
				// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
				System.out.println("��ǰ�����ܾ��ӵ���Ŀ�ǣ�"
						+ article.getSentences().size());
				System.out.println("��ǰ������������Ŀ�ǣ�"
						+ article.getSubSentences().size());
				System.out.println("��ǰ���ŵ��������ܷ�Ϊ��" + article.getTendScore());
				for (TendSentence sentence : article.getSubSentences()) {
					String allSentenceStr = "";
					for (TendWord word : sentence.getWords()) {
						allSentenceStr += word.getCont();
					}
					System.out.println("��ǰ���ӣ�  " + allSentenceStr);
					System.out.println("��������ǣ� " + sentence.getSubjectScore());
					System.out.println("�����Է����� " + sentence.getTendScore());
				}
				// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * ����ʱ����������ص�ǰ��newsTend�б�������ǰ̨��Ҫ��������ʽ����ʽ�д��̶�,�����д����
	 */

	@Test
	public void test() {
		startTendAnalyse();

	}

}
