package pomonitor.analyse.entity;

import java.util.Map;

/**
 * ���ⷢ��ģ���У�����ʵ��
 * 
 * @author caihengyi 2015��12��15�� ����2:40:53
 */
public class TDArticleTerm {
	private String value;// ��������
	private TDPosition position;// �ô����ڸ��ı���λ��
	private double weight;   //�ô����ڸ��ı����Ȩ��
	
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
	public double getweight() {
		return this.weight;
	}

	public void setweight(double weight) {
		this.weight = weight;
	}
}
