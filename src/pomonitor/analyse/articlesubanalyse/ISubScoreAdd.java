package pomonitor.analyse.articlesubanalyse;

import pomonitor.analyse.entity.TendAnalyseArticle;
import pomonitor.analyse.entity.Sentence;

/**
 * �������µ�ÿһ������������ԵĽӿ�
 * 
 * @author zhaolong
 * 
 */
public interface ISubScoreAdd {

	/**
	 * 
	 * @param article
	 * @param sentence
	 * @return sentence
	 */
	public Sentence add(TendAnalyseArticle article, Sentence sentence);

}
