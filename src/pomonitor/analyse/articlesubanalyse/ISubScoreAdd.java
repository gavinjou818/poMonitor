package pomonitor.analyse.articlesubanalyse;

import pomonitor.analyse.entity.TendAnalyseArticle;
import pomonitor.analyse.entity.Sentence;

/**
 * �������µ������ķ���
 * 
 * @author zhaolong
 * 
 */
public interface ISubScoreAdd {
	public Sentence add(TendAnalyseArticle article, Sentence sentence);

}
