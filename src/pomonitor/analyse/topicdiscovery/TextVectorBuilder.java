package pomonitor.analyse.topicdiscovery;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
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
	private List<TDArticle> globalArticleList;
	// ��ȡ�ٷֱ�
	private final double EXTRACT_PERCENT = 0.1;
	// body�ʵ�Ȩ��ϵ��
	private final double BODY_WEIGHT = 1;
	// meta�ʵ�Ȩ��ϵ��
	private final double META_WEIGHT = 3;
	// �ܵ���������
	public List<String> globalFeatureCollections = new ArrayList<String>();

	/**
	 * �����ܵ��������Ϻ������ı����󼯺ϣ�������������
	 * 
	 * @param topicDisArticleList
	 * @param globalFeatureCollections
	 * @return
	 */
	public List<TDArticle> buildVectors(List<TDArticle> topicDisArticleList) {
		globalArticleList = topicDisArticleList;
		// ����ÿƪ�������д����Ȩ����Ϣ
		for (TDArticle article : globalArticleList) {
			Map<String, Double> map = new HashMap<String, Double>();
			for (TDArticleTerm _term : article.getArticleAllTerms()) {
				map.put(_term.getvalue(), getWeight(article, _term));
			}
			article.setTermsWeights(map);
		}
		// ����globalFeatureCollections ȫ�� ���������
		globalFeatureCollections = getFeatureSet();
		// ����ÿƪ�ı�������ģ��
		for (TDArticle tdArticle : globalArticleList) {
			tdArticle = buildArticleVector(tdArticle);
		}
		return globalArticleList;
	}

	/**
	 * �����ƪ�ı�������ģ��
	 * 
	 * @param TDArticle
	 * @return TDArticle
	 */

	private TDArticle buildArticleVector(TDArticle article) {
		TDArticle resTdArticle = article;
		double[] vec = new double[globalFeatureCollections.size()];
		for (int i = 0; i < vec.length; i++) {
			if (article.getTermsWeights().containsKey(
					globalFeatureCollections.get(i)))
				vec[i] = article.getTermsWeights().get(
						globalFeatureCollections.get(i));
			else
				vec[i] = 0;
		}
		resTdArticle.vectorSpace = vec;
		return resTdArticle;
	}

	/**
	 * ����ĳ�������Ȩ��ֵ����Ȩ��ֵ������ֵ�й� :
	 * 1.�ô���� (term)
	 * 2.�����ô�������� (article)
	 * 3.�������¼��� (globalArticleList)
	 * 
	 * �÷�����Ҫ����
	 * 
	 * @param article
	 * @param term
	 * @return
	 */
	private double getWeight(TDArticle article, TDArticleTerm term) {
		
		return findTFIDF(article, term);
	}

	/**
	 * ����ĳ���ʵ� tf-idf ֵ
	 * 
	 * @param article
	 * @param term
	 * @return
	 */
	private double findTFIDF(TDArticle article, TDArticleTerm term) {
		double tf = findTermFrequency(article, term.getvalue());
		double idf = findInverseDocumentFrequency(term.getvalue());
		return tf * idf;
	}

	/**
	 * ����ĳ������ĳƪ�����е� tf
	 * 
	 * @param article
	 * @param term
	 * @return
	 */
	private double findTermFrequency(TDArticle article, String term) {
		double termCount = 0;
		for (TDArticleTerm _term : article.getArticleAllTerms()) {
			if (term.equals(_term.getvalue()))
				termCount++;
		}
		return termCount / article.getArticleAllTerms().size();
	}

	/**
	 * ����ĳ���ʵ� idf
	 * 
	 * @param term
	 * @return
	 */
	private double findInverseDocumentFrequency(String term) {
		double count = 0;
		for (TDArticle article : globalArticleList) {
			if (article.getArticleAllTerms().contains(term))
				count++;
		}
		return Math.log((globalArticleList.size()) / (count + 0.001));
	}

	/**
	 * ���������ı����Ϻ�ָ������ȡ�ٷֱȣ������Ч��������ϣ�������������
	 * 
	 * @param topicDisArticleList
	 * @param percentage
	 * @return ���������ϣ��䳤�ȼ�Ϊ��������
	 */
	private List<String> getFeatureSet() {
		List<String> tmpGlobalFeatureCollections = new ArrayList<String>();

		for (TDArticle article : globalArticleList) {
			ArrayList<Map.Entry<String, Double>> descSortedList = DescSort(article
					.getTermsWeights());
			// ��ȡָ��������ȫ������������
			int extractsize = (int) (article.getTermsWeights().size() * EXTRACT_PERCENT);
			for (int i = 0; i < extractsize; i++) {
				tmpGlobalFeatureCollections.add(descSortedList.get(i).getKey());
			}
		}
		// ȥ��
		globalFeatureCollections = new ArrayList<String>(new HashSet<String>(
				tmpGlobalFeatureCollections));

		return globalFeatureCollections;
	}

	/**
	 * �� Map ���� value �������У����� key �� ArrayList
	 * 
	 * @param map
	 * @return
	 */
	private ArrayList<Map.Entry<String, Double>> DescSort(
			Map<String, Double> map) {
		ArrayList<Map.Entry<String, Double>> mapList = new ArrayList<Map.Entry<String, Double>>(
				map.entrySet());
		Collections.sort(mapList, new Comparator<Map.Entry<String, Double>>() {
			@Override
			public int compare(Map.Entry<String, Double> o1,
					Map.Entry<String, Double> o2) {
				// ����value��������
				if ((o2.getValue() - o1.getValue()) < 0)
					return -1;
				else if ((o2.getValue() - o1.getValue()) > 0)
					return 1;
				else
					return 0;
			}
		});
		return mapList;
	}
}
