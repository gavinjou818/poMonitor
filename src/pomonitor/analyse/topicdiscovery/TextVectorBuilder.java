package pomonitor.analyse.topicdiscovery;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pomonitor.analyse.entity.TDArticle;
import pomonitor.analyse.entity.TDArticleTerm;

/**
 * �������ռ�ģ�ͱ�ʾ�����ı�
 * 
 * @author caihengyi 2015��12��14�� ����5:26:03
 */
public class TextVectorBuilder {

	// �����ı�����
	private List<TDArticle> topicDisArticleList = null;
	// ��ȡ�ٷֱ�
	private final double EXTRACT_PERCENT = 0.15;
	// �ܵ���������
	Map<String, Double> globalFeatureCollections = null;
	//���д˴ʻ��ĵ���Ƶ��
	Map<String, Double> globalDocumentFrequency=null;
	/**
	 * ���������ı����Ϻ�ָ������ȡ�ٷֱȣ������Ч��������ϣ�������������
	 * 
	 * @param topicDisArticleList
	 * @param percentage
	 * @return ���������ϣ��䳤�ȼ�Ϊ��������
	 */
	private Map<String, Double> getFeatureSet(
			List<TDArticle> topicDisArticleList, double percentage) {
		
		// ������ÿƪ�����д�����ۺ�Ȩֵ֮�󣬸�ֵ��this.newsList;

		return null;
	}

	/**
	 * �����ܵ��������Ϻ������ı����󼯺ϣ�������������
	 * 
	 * @param topicDisArticleList
	 * @param globalFeatureCollections
	 * @return
	 */
	public List<TDArticleTerm> buildVectors(
			List<TDArticle> topicDisArticleList) {
		globalFeatureCollections = getFeatureSet(topicDisArticleList,
				EXTRACT_PERCENT);

		// ����this.newsListȥ��������

		
		return null;
	}
	
	/**
	 * ����һƪ���µ�tf-idf 
	 * @param TDArticle
	 * @return Map<String, Double> ÿƪ����������Ȩ��
	 * δ�淶����������;����Ϊ��ȡ�ٷֱ� ��EXTRACT_PERCENT
	 */
	public TDArticleTerm buildArticleVector(TDArticle article){
		Map<String, Double> articleVector=new HashMap<String,Double>();
		
		
		return null;
	}
}
