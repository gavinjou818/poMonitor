package pomonitor.analyse.articlesubanalyse;

import java.util.ArrayList;
import java.util.List;
import pomonitor.analyse.entity.Article;
import pomonitor.analyse.entity.Sentence;

/**
 * 分析文章主题句的处理类
 * @author zhaolong
 * 2015年12月15日 下午2:26:10
 */

public class SubSentenceGet {
	//主题类加权方法处理器
	private ISentenceSubCountByWeight countWeight;
	//需要处理的文章
	private Article article;
	//处理文章中各种影响因子的分析类列表
	public List<ISubScoreAdd> adderList;
	

	public void addScoreAdder(ISubScoreAdd adder) {
		adderList.add(adder);
	}

	/**
	 * 
	 * @param countByWeight 主题类加权方法
	 * @param artice 需要分析的文章对象
	 */
	public SubSentenceGet(ISentenceSubCountByWeight countByWeight,Article artice) {
		adderList = new ArrayList<ISubScoreAdd>();
		this.countWeight=countByWeight;
		this.article=artice;
	}

	

	/**
	 * 对文章每一个句子做主题分析，并将其存储到相应句子的相应分数，
	 *并计算出每个句子的主题总分,并保存到主题总分字段
	 */
	public void countSubScore() {
		for (Sentence sentence : article.getSentences()) {
			for (ISubScoreAdd add : adderList) {
				add.add(article, sentence);
			}
			//加权处理四种影响因子
			countWeight.sentenceSubCount(sentence);
		}
	}

	/**
	 * 处理并存储每一篇文章中能代表主题的前几个句子 
	 * @param outCount 需要提取的主题句多少
	 */
	public void getSubSentence( int outCount) {
		List<Sentence> sentences = article.getSentences();
		List<Sentence> subSentences = new ArrayList<Sentence>();
		int count = 0;
		int index;
		for (int i = 0; i < sentences.size(); i++) {
			if (count > outCount) {
				break;
			}
			count++;
			index = i;
			for (int j = i; j < sentences.size() - 1; j++) {
				if (sentences.get(j).getTendScore() > sentences.get(i)
						.getTendScore()) {
					index = j;
				}
			}
			subSentences.add(sentences.get(index));
		}
		article.setSubSentences(subSentences);
	}

}
