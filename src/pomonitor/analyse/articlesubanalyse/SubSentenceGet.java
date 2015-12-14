package pomonitor.analyse.articlesubanalyse;

import java.util.ArrayList;
import java.util.List;

import pomonitor.analyse.articletendanalyse.entity.Article;
import pomonitor.analyse.articletendanalyse.entity.Sentence;

/**
 * ��ȡ��������
 * @author Administrator
 *
 */

public class SubSentenceGet {
	
	
	public void addScoreAdder(ISubScoreAdd adder){
		adderList.add(adder);
	}
	
	public SubSentenceGet(){
		adderList=new ArrayList<ISubScoreAdd>();
	}
	
	public List<ISubScoreAdd> adderList;
	
	//��������ÿһ�����ӵ��������
	public void countSubScore(Article article){
		for(Sentence sentence:article.getSentences()){
			for(ISubScoreAdd add:adderList){
				add.add(article, sentence);
			}
		}
	}
	
	
	
	//���ÿһƪ�������ܴ��������ǰ��������
	public void getSubSentence(Article article ,int outCount){
		List<Sentence> sentences =  article.getSentences();
		List<Sentence> subSentences =new ArrayList<Sentence>();
		int count = 0;
		int index;
		for(int i=0;i<sentences.size();i++){
			if(count>outCount){
				break;
			}
			count++;
			index=i;
			for(int j=i;j<sentences.size()-1;j++){
				if(sentences.get(j).getTendScore()>
				sentences.get(i).getTendScore()){
					index=j;
				}
				
			}
			subSentences.add(sentences.get(index));
		}
		article.setSubSentences(subSentences);
	}

}
