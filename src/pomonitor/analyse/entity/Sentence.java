package pomonitor.analyse.entity;

import java.util.List;

/**
 * ÿƪ�������ֵľ��Ӷ���
 * 
 * @author Administrator
 * 
 */
public class Sentence {
	// �����������е����
	private int id;
	// ���Ӱ�����ʵ�ʼ���
	private List<String> words;

	// �����Է���
	private float tendScore;

	// �������
	private float subjectScore;

	// �ʹ��׵ľ����������
	private float wordSubScore;

	// ���⹱�׵ľ����������
	private float titleScore;

	// ����λ�ù��׵ľ��ӷ���
	private float posScore;

	// ���Ŵʹ��׵ľ��ӷ���
	private float thinkScore;

	public float getTendScore() {
		return tendScore;
	}

	public void setTendScore(float tendScore) {
		this.tendScore = tendScore;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public List<String> getWords() {
		return words;
	}

	public void setWords(List<String> words) {
		this.words = words;
	}

}
