package pomonitor.analyse.articlesubanalyse;

import pomonitor.analyse.entity.TendSentence;

/**
 * ���������⹱�����,��Ϊ�ܵ���������
 * 
 * @author zhaolong 2015��12��20�� ����3:44:38
 */

public class SentenceSubCountByWeightAverage implements
		ISentenceSubCountByWeight {

	@Override
	public TendSentence sentenceSubCount(TendSentence sentence) {
		float allSubScore = sentence.getPosScore() + sentence.getThinkScore()
				+ sentence.getTitleScore();
		sentence.setSubjectScore(allSubScore);
		return sentence;
	}

}
