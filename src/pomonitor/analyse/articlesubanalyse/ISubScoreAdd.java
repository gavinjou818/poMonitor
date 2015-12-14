package pomonitor.analyse.articlesubanalyse;

import pomonitor.analyse.articletendanalyse.entity.Article;
import pomonitor.analyse.articletendanalyse.entity.Sentence;

/**
 * 对于文章的主题句的分析
 * 
 * @author zhaolong
 * 
 */
public interface ISubScoreAdd {
	public Sentence add(Article article, Sentence sentence);

}
