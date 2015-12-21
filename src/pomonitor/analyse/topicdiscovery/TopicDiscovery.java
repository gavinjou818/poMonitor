package pomonitor.analyse.topicdiscovery;

import java.util.List;
import java.util.Map;

import pomonitor.analyse.entity.TDArticle;
import pomonitor.analyse.entity.Topic;
import pomonitor.entity.Sensword;

/**
 * ���ⷢ��
 * 
 * @author caihengyi 2015��12��14�� ����9:33:43
 */
public class TopicDiscovery {
	// ���������ı�����
	private List<Map<String, Double>> mArticleVectors;

	// ����֮��Ľ������
	private List<List<Map<String, Double>>> mArticleClusterResults;

	// �Խ�����ϴ���֮��Ļ��⼯��
	private List<Topic> mTopics;

	// ���������ı����Ϻ��û������дʿ⣬��ȡ����
	public List<Topic> getTopics(List<TDArticle> articleLists,
			List<Sensword> sensitiveDict) {
		TextVectorBuilder tvb = new TextVectorBuilder();
		List<TDArticle> tdArticlesWithVector = tvb.buildVectors(articleLists);
		// ���ı��������о���

		// �Ծ��������д���

		// �Ի��������� ���дʿ� �ٴν��м�Ȩ

		return mTopics;
	}

}
