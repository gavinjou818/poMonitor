package pomonitor.analyse.topicdiscovery;

import java.util.ArrayList;
import java.util.List;

import pomonitor.analyse.entity.ArticleShow;
import pomonitor.analyse.entity.Attitude;
import pomonitor.analyse.entity.TDArticle;
import pomonitor.analyse.entity.TDCentroid;
import pomonitor.analyse.entity.Topic;
import pomonitor.entity.Sensword;

/**
 * ���ⷢ��
 * 
 * @author caihengyi 2015��12��14�� ����9:33:43
 */
public class TopicDiscovery {
	private List<String> mBaseStrings;
	private final int k = 13;

	// ���������ı����Ϻ��û������дʿ⣬��ȡ����
	public List<Topic> getTopics(List<TDArticle> articleLists,
			List<Sensword> sensitiveDict) {
		TextVectorBuilder tvb = new TextVectorBuilder();
		List<TDArticle> tdArticlesWithVector = tvb.buildVectors(articleLists);
		mBaseStrings = tvb.globalFeatureCollections;
		// ���ı��������о���
		List<TDCentroid> resTDCentroid = KmeansCluster.ArticleCluster(k,
				tdArticlesWithVector);
		// �Ծ��������д���
		List<Topic> topics=new ArrayList<Topic>();
		for (TDCentroid tdCentroid : resTDCentroid){
			Topic tp=new Topic();
			String content="";
			double[] _vec = tdCentroid.GroupedArticle.get(0).vectorSpace;
			double _maxVar = getMax(_vec);
			for (int i = 0; i < _vec.length; i++) {
				if (_vec[i] > 0.1 * _maxVar) {
					content+=mBaseStrings.get(i)+" ";
				}
			}
			tp.setContent(content);
			topics.add(tp);
		}
		// �Ի��������� ���дʿ� �ٴν��м�Ȩ

		return topics;
	}

	public List<Topic> getTopicFromCentroid(TDCentroid tdc,
			List<String> baseSring) {
		List<Topic> topicList = new ArrayList<Topic>(baseSring.size());
		for (int i = 0; i < baseSring.size(); i++) {
			Topic t = new Topic();
			t.articleViews = new ArrayList<>();
			t.weight = 0.0;
			t.setAttitude(Attitude.NEUTRAL);
			t.setContent("");
			t.setSensitiveWords(false);
			topicList.add(t);
		}
		double[] base = tdc.GroupedArticle.get(0).vectorSpace;
		for (int i = 1; i < tdc.GroupedArticle.size(); i++) {
			ArticleShow as = new ArticleShow();
			/****** ÿ����������һƪ����(ArticleShow ������������������û������) *****/
			as.setComeFrom(tdc.GroupedArticle.get(i).getComeFrom());
			as.setDescription(tdc.GroupedArticle.get(i).getDescription());
			as.setTimestamp(tdc.GroupedArticle.get(i).getTimestamp());
			as.setTitle(tdc.GroupedArticle.get(i).getTitle());
			as.setUrl(tdc.GroupedArticle.get(i).getUrl());
			/*****************************************************************/
			for (int j = 0; j < base.length; j++) {
				if (tdc.GroupedArticle.get(i).vectorSpace[j] > base[j])
					as.heat += tdc.GroupedArticle.get(i).vectorSpace[j];
			}
			for (int j = 0; j < base.length; j++) {
				if (tdc.GroupedArticle.get(i).vectorSpace[j] > base[j]) {
					topicList.get(j).articleViews.add(as);
					topicList.get(j).weight += tdc.GroupedArticle.get(i).vectorSpace[j];
					topicList.get(j).setContent(baseSring.get(j));
					// �û����Ƿ������дʣ�������Ϣ��û������
				}
			}

		}
		return topicList;
	}
	
	
	
	public static double getMax(double[] arr) {
		double maxVar = arr[0];
		for (int i = 0; i < arr.length; i++) {
			if (arr[i] > maxVar)
				maxVar = arr[i];
		}
		return maxVar;
	}
	public static double getMin(double[] arr) {
		double minVar = arr[0];
		for (int i = 0; i < arr.length; i++) {
			if (arr[i] < minVar)
				minVar = arr[i];
		}
		return minVar;
	}
	public static String vectorToString(double[] arr) {
		String str = "";
		for (double d : arr) {
			str += d + "->";
		}
		return str;
	}
}
