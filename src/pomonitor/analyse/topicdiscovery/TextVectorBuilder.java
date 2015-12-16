package pomonitor.analyse.topicdiscovery;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import pomonitor.analyse.entity.TDArticle;
import pomonitor.analyse.entity.TDArticleTerm;
import pomonitor.analyse.entity.TDPosition;

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
	// body�ʵ�Ȩ��ϵ��
	private final double BODY_WEIGHT=1;
	// meta�ʵ�Ȩ��ϵ��
	private final double META_WEIGHT=3;
	// �ܵ���������
	List<String> globalFeatureCollections = null;
	//���д˴ʻ��ĵ���Ƶ��
	Map<String, Double> globalDocumentFrequency=null;
	/**
	 * ���������ı����Ϻ�ָ������ȡ�ٷֱȣ������Ч��������ϣ�������������
	 * 
	 * @param topicDisArticleList
	 * @param percentage
	 * @return ���������ϣ��䳤�ȼ�Ϊ��������
	 */
	private List<String> getFeatureSet(
			List<TDArticle> topicDisArticleList, double percentage) {
		//��������ÿƪ���µ�Ȩֵ
		for(TDArticle article :topicDisArticleList){
			List<TDArticleTerm> allTerms=article.getArticleAllTerms();
			Collections.sort(allTerms,new Comparator<TDArticleTerm>(){
				public int compare(TDArticleTerm o1, TDArticleTerm o2) {
					double o1w=o1.getweight(),o2w=o2.getweight();
					if(o1w>o2w)return -1;
					if(o1w<o2w)return 1;
					return 0;
				}
			});
			//��ȡָ��������ȫ������������
			int extractsize= (int) (allTerms.size()*EXTRACT_PERCENT);
			globalFeatureCollections=new ArrayList<String>();
			for(int i=0;i<extractsize;i++){
				globalFeatureCollections.add(allTerms.get(i).getvalue());
			}
		}
		//ȥ��
		globalFeatureCollections= new ArrayList<String>(new HashSet<String>(globalFeatureCollections));

		return globalFeatureCollections;
	}

	/**
	 * �����ܵ��������Ϻ������ı����󼯺ϣ�������������
	 * 
	 * @param topicDisArticleList
	 * @param globalFeatureCollections
	 * @return
	 */
	public List<TDArticle> buildVectors(
			List<TDArticle> topicDisArticleList) {
		//����globalDocumentFrequency
		globalDocumentFrequency=new HashMap<String, Double>();
		for(TDArticle article :topicDisArticleList){
			for(TDArticleTerm term:article.getArticleAllTerms()){
				String termValue=term.getvalue();
				if(!globalDocumentFrequency.containsKey(termValue)){
					globalDocumentFrequency.put(termValue, (double) 0);
				} 
				globalDocumentFrequency.put(termValue,globalDocumentFrequency.get(termValue)+1);
			}
		}
		//����buildArticleVector������ÿƪ�������дʵ�Ȩ��
		for(TDArticle article :topicDisArticleList){
			buildArticleVector(article);
		}
		//����globalFeatureCollections ȫ����������
		globalFeatureCollections = getFeatureSet(topicDisArticleList,
				EXTRACT_PERCENT);
		return topicDisArticleList;
	}
	
	/**
	 * ����һƪ���������д����tf-idf(�ֲ�)
	 * @param TDArticle
	 * @return TDArticle
	 */
	public TDArticle buildArticleVector(TDArticle article){
		//������������
		int sumArticle=topicDisArticleList.size();   
		//���µ��ʷ���map��
		Map<String, Double> tf=new HashMap<String,Double>(); 
		//��ƪ���µ�������
		int sumTerm=article.getArticleAllTerms().size();  
		//�����Ƶֵ
		for(TDArticleTerm term:article.getArticleAllTerms()){ 
			String termValue=term.getvalue();
			if(!tf.containsKey(termValue)){
				tf.put(termValue, (double) 0);
			} 
			if(term.getposition()==TDPosition.META){
				tf.put(termValue,tf.get(termValue)+META_WEIGHT);
			}
			else if(term.getposition()==TDPosition.BODY){
				tf.put(termValue,tf.get(termValue)+BODY_WEIGHT);
			}
		}
		//�õ�tfֵ
		for(Map.Entry<String, Double> tfentry : tf.entrySet()){
			tfentry.setValue(tfentry.getValue()/sumArticle);
		}
		
		//��ÿ�����ʸ���Ӧweight,����ȥ��
		List<TDArticleTerm> articleAllTerms=article.getArticleAllTerms();
		Iterator<TDArticleTerm> iter = articleAllTerms.iterator();  
		while(iter.hasNext()){
			TDArticleTerm term=iter.next();
			String termValue=term.getvalue();
			//ȥ��
			if(!tf.containsKey(termValue))
				iter.remove();
			else{
				double idf=Math.log(sumArticle/globalDocumentFrequency.get(termValue)+0.01);
				term.setweight(tf.get(termValue)*(idf));  
				tf.remove(termValue);
			}
		}
		return article;
	}
}
