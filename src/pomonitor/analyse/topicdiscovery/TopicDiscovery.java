package pomonitor.analyse.topicdiscovery;

import java.util.List;
import java.util.Map;

import pomonitor.analyse.entity.Topic;

/**
 * ���ⷢ��
 * 
 * @author caihengyi 2015��12��14�� ����9:33:43
 */
public class TopicDiscovery {
	// ���������ı�����
	private List<Map<String, Double>> articleVectors;

	// ����֮��Ľ������
	private List<List<Map<String, Double>>> articleClusterResults;

	// �Խ�����ϴ���֮��Ļ��⼯��
	private List<Topic> topics;

	// ���������ı����Ϻ��û������дʿ⣬��ȡ����
	private List<Topic> getTopics(List<Object> articleLists,
			List<Object> sensitiveDict) {

		return null;
	}
}
