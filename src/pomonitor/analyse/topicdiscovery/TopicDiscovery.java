package pomonitor.analyse.topicdiscovery;

import java.util.List;
import java.util.Map;

import pomonitor.analyse.entity.Topic;

/**
 * ���ⷢ��
 * @author caihengyi
 * 2015��12��14�� ����9:33:43
 */
public class TopicDiscovery {
	//���������ı�����
	private List<Map<String,Double>> newsVectors;
	
	//����֮��Ľ������
	private List<List<Map<String,Double>>> newsClusterResults;
	
	//�Խ�����ϴ���֮��Ļ��⼯��
	private List<Topic> topics;
	
	
}
