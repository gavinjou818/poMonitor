package pomonitor.analyse.articlesubanalyse;

import pomonitor.analyse.entity.Article;
import pomonitor.analyse.entity.Sentence;

/**
 * �������µ������ķ���
 * 
 * @author zhaolong
 * 
 */
public interface ISubScoreAdd {
	public Sentence add(Article article, Sentence sentence);

}
