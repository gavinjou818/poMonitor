package pomonitor.analyse.articlesubanalyse;

import pomonitor.analyse.entity.TendAnalyseArticle;
import pomonitor.analyse.entity.TendSentence;

/**
 * ���±���ӷ֣��˴�������ؼ��ּӷֺϲ������Ժ�����ϸ��
 * 
 * @author Administrator
 * 
 */
public class SubScoreAddTitle implements ISubScoreAdd {

	// ��Ҫ�ӷֵĴ���
	private String propertys[] = { "a", "i", "j", "k", "m", "n", "nd", "nh",
			"ni", "nl", "ns", "nt", "nz", "v", "ws" };

	@Override
	public TendSentence add(TendAnalyseArticle article, TendSentence sentence) {
		return null;
	}

}
