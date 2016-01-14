package pomonitor.analyse.hotworddiscovery;

import java.util.List;

import pomonitor.analyse.entity.ArticleShow;
import pomonitor.analyse.entity.HotWord;
import pomonitor.util.PropertiesReader;

/**
 * 
 * @author luoxu
 * 2016��1��14�� ����4:34:21
 */
public class DistanceMatrix {
	private double[][] classDistMat;
	private double[][] overlapMat;
	private double sameClassVal;
	private List<HotWord> hotWords;
	public DistanceMatrix(List<HotWord> sumHotWords){
		PropertiesReader propertiesReader = new PropertiesReader();
		this.sameClassVal = Integer.parseInt(propertiesReader.getPropertyByName("sameClassVal"));
		this.hotWords=sumHotWords;
	}
	
	public double[][] calClassDistMat(double[][] globalTDCentroidDist){
		int len=hotWords.size();
		classDistMat=new double[len][len];
		for(int i=0;i<len;i++){
			for(int j=0;j<len;j++){
				HotWord word1=hotWords.get(i),word2=hotWords.get(j);
				if(word1.getBelongto()!=word2.getBelongto()) {
					classDistMat[i][j]=
							globalTDCentroidDist[word1.getBelongto()][word2.getBelongto()];
				}
				else 
					classDistMat[i][j]=sameClassVal;
			}
		}
		//��һ��
		classDistMat=normalizeMat(classDistMat,len);
		return classDistMat;
	}
	//����ͬ����
	public double[][] calOverlapMat(){
		int len=hotWords.size();
		overlapMat=new double[len][len];
		for(int i=0;i<len;i++)
			for(int j=0;j<len;j++){
				double overlap=0;
				//�����ȴʴ�������ż��ϣ��󽻼�
				List<ArticleShow> articlesi =hotWords.get(i).articleViews;
				List<ArticleShow> articlesj=hotWords.get(j).articleViews;
				double sum=articlesi.size()+articlesj.size();
				for(ArticleShow si :articlesi){
					for(ArticleShow sj:articlesj){
						if(si.equals(sj)){
							overlap++;
							sum--;
						}
					}
				}
				overlapMat[i][j]=overlap/sum;
			}
		//��һ��
		overlapMat=normalizeMat(overlapMat,len);
		return overlapMat;
	}
	/**
	 * �����һ��
	 * @param mat
	 * @param len
	 * @return
	 */
	public double[][] normalizeMat(double[][] mat,int len){
		
		double sum=0,maxVal=mat[0][0],minVal=mat[0][0];
		for(int i=0;i<len;i++){
			for(int j=0;j<len;j++){
				sum+=mat[i][j];
				maxVal=Math.max(maxVal, mat[i][j]);
				minVal=Math.min(minVal, mat[i][j]);
			}
		}
		double avgVal = sum/(len*len);
		
		for(int i=0;i<len;i++){
			for(int j=0;j<len;j++){
				mat[i][j]=(mat[i][j]-avgVal)/(maxVal - minVal);
			}
		}
		return mat;
	}
	
}
