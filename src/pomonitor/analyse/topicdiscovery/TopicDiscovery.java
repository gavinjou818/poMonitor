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
	private List<Map<String, Double>> _articleVectors;

	// ����֮��Ľ������
	private List<List<Map<String, Double>>> _articleClusterResults;

	// �Խ�����ϴ���֮��Ļ��⼯��
	private List<Topic> _topics;

	// ���������ı����Ϻ��û������дʿ⣬��ȡ����
	public List<Topic> getTopics(List<TDArticle> articleLists,
			List<Sensword> sensitiveDict) {

		return null;
	}
}
