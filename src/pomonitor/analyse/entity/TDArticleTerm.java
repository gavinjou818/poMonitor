package pomonitor.analyse.entity;

/**
 * ���ⷢ��ģ���У�����ʵ��
 * 
 * @author caihengyi 2015��12��15�� ����2:40:53
 */
public class TDArticleTerm {
	private String value;// ��������
	private TDPosition position;// �ô����ڸ��ı���λ��
	private int documentFrequency;// �ô����������ĵ��г��ֵ�Ƶ��
	private int termfrequency; // �ô����ڸ��ı�����ֵ�Ƶ��

	public String getvalue() {
		return value;
	}

	public void setvalue(String value) {
		this.value = value;
	}

	public TDPosition getposition() {
		return position;
	}

	public void setposition(TDPosition position) {
		this.position = position;
	}

	public int getdocumentFrequency() {
		return documentFrequency;
	}

	public void termfrequency(int documentFrequency) {
		this.documentFrequency = documentFrequency;
	}

	public int gettermfrequency() {
		return termfrequency;
	}

	public void settermfrequency(int termfrequency) {
		this.termfrequency = termfrequency;
	}
}
