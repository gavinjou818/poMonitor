package pomonitor.analyse.entity;

import java.util.Date;
import java.util.List;

/**
 * ���ⷢ��ģ���д����ݿ�õ������ı�����֮�� ���·�װ�����ڻ��ⷢ�ֵ������ı�����
 * 
 * @author caihengyi 2015��12��15�� ����2:22:32
 */
public class TDArticle {

	private String title;
	private String url;
	private String description;
	private List<String> keyWords;
	private Date timestamp;
	private String comeFrom;// ��Դ��վ������
	private List<TDArticleTerm> articleTerms;// �����°����Ĵ����

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<String> getKeyWords() {
		return keyWords;
	}

	public void setKeyWords(List<String> keyWords) {
		this.keyWords = keyWords;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public String getComeFrom() {
		return comeFrom;
	}

	public void setComeFrom(String comeFrom) {
		this.comeFrom = comeFrom;
	}

	public List<TDArticleTerm> getArticleTerms() {
		return articleTerms;
	}

	public void setArticleTerms(List<TDArticleTerm> articleTerms) {
		this.articleTerms = articleTerms;
	}

}
