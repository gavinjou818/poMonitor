package pomonitor.analyse.articlesubanalyse;

import pomonitor.analyse.entity.Article;
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
	public Sentence add(Article article, Sentence sentence);

}
