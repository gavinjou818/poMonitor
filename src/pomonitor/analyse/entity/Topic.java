package pomonitor.analyse.entity;

import java.util.List;
import pomonitor.view.entity.ArticleView;

/**
 * ����
 * 
 * @author caihengyi 2015��12��15�� ����10:17:13
 */
public class Topic {

	private String content;// ��������
	private Double weight;// ����Ȩ��
	private boolean isSensitiveWords;// �û����Ƿ������дʣ��������ʣ�
	private List<ArticleView> articleViews;// �����û�������¼���
	private Attitude attitude;// �û���İ���̬����Ϣ

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public boolean isSensitiveWords() {
		return isSensitiveWords;
	}

	public void setSensitiveWords(boolean isSensitiveWords) {
		this.isSensitiveWords = isSensitiveWords;
	}

	public List<ArticleView> getArticleViews() {
		return articleViews;
	}

	public void setArticleViews(List<ArticleView> articleViews) {
		this.articleViews = articleViews;
	}

	public Attitude getAttitude() {
		return attitude;
	}

	public void setAttitude(Attitude attitude) {
		this.attitude = attitude;
	}

}
