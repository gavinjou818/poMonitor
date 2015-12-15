package pomonitor.view.entity;

import java.util.Date;

import pomonitor.analyse.entity.Attitude;

/**
 * չʾ�б��õ��ı���������ͼ�ϱ���Ϊһ����Ϣ�����������tag��
 * 
 * @author caihengyi 2015��12��15�� ����10:29:25
 */
public class ArticleView implements Comparable<ArticleView> {

	private String title;
	private String url;
	private String description;
	private Date timestamp;
	private Attitude attitude;// ���Ű���̬��
	private ArticleDegree degree;// ���ŷּ�
	private String comeFrom;// ��Դ��վ������
	private Double heat;// �������ŵ��ȶ�

	// ���·ּ�
	public enum ArticleDegree {
		A("һ��"), B("����"), C("����");// ����Խ��Խ����

		private String degree;

		// ö�ٶ����캯��
		private ArticleDegree(String degree) {
			this.degree = degree;
		}

		// ö�ٶ����ȡ����̬�ȵķ���
		public String getDegree() {
			return this.degree;
		}
	}

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

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public Attitude getAttitude() {
		return attitude;
	}

	public void setAttitude(Attitude attitude) {
		this.attitude = attitude;
	}

	public ArticleDegree getDegree() {
		return degree;
	}

	public void setDegree(ArticleDegree degree) {
		this.degree = degree;
	}

	public String getComeFrom() {
		return comeFrom;
	}

	public void setComeFrom(String comeFrom) {
		this.comeFrom = comeFrom;
	}

	public Double getHeat() {
		return heat;
	}

	public void setHeat(Double heat) {
		this.heat = heat;
	}

	@Override
	public int compareTo(ArticleView o) {
		if (this.heat < o.heat)
			return -1;
		if (this.heat > o.heat)
			return 1;
		return 0;
	}
}
