package pomonitor.analyse.topicdiscovery;

import java.util.List;
import java.util.Map;

/**
 * �������ռ�ģ�ͱ�ʾ�����ı�
 * 
 * @author caihengyi 2015��12��14�� ����5:26:03
 */
public class TextVectorBuilder {

	// ���ż���
	private List<Object> newsList = null;
	// ��ȡ�ٷֱ�
	private final double EXTRACT_PERCENT = 0.15;
	// �ܵ���������
	Map<String, Double> globalFeatureCollections = null;

	/**
	 * ���������ı����Ϻ�ָ������ȡ�ٷֱȣ������Ч��������ϣ�������������
	 * 
	 * @param newsList
	 * @param percentage
	 * @return ���������ϣ��䳤�ȼ�Ϊ��������
	 */
	private Map<String, Double> getFeatureSet(List<Object> newsList,
			double percentage) {

		// ������ÿƪ�����д�����ۺ�Ȩֵ֮�󣬸�ֵ��this.newsList;

		return null;
	}

	/**
	 * �����ܵ��������Ϻ����Ŷ��󼯺ϣ�������������
	 * 
	 * @param newsLists
	 * @param globalFeatureCollections
	 * @return
	 */
	public List<Map<String, Double>> buildVectors(List<Object> newsLists) {
		globalFeatureCollections = getFeatureSet(newsLists, EXTRACT_PERCENT);

		// ����this.newsListȥ��������

		return null;
	}
}
