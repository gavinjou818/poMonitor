package pomonitor.analyse.articlesubanalyse;

import java.util.Set;

import pomonitor.analyse.entity.TendAnalyseArticle;
import pomonitor.analyse.entity.TendSentence;
import pomonitor.analyse.entity.TendWord;

/**
 * ���±���ӷ֣��˴�������ؼ��ּӷֺϲ������Ժ�����ϸ��
 * 
 * @author Administrator
 * 
 */
public class SubScoreAddTitle implements ISubScoreAdd {

	@Override
	public TendSentence add(TendAnalyseArticle article, TendSentence sentence) {
		// ����ʵ�ʵ���Ŀ
		int count = 0;
		// title��������ʵ��
		Set<String> set = article.getSet();
		for (TendWord td : sentence.getWords()) {
			// ����˾仰����title�к�keyWords�еĴ���ӷ�
			if (set.contains(td.getCont())) {
				count++;
			}
		}
		// ����ȡ�ðٷֱȵļ��㷽���������д��Ľ�
		float titleAndKeyScore = (float) count / set.size();
		sentence.setTitleScore(titleAndKeyScore);

		return sentence;
	}

}
