package pomonitor.briefing;

import java.util.Date;

/**
 * 
 * 
 * @author zhouzhifeng
 * pomonitor.statistics.Summarize��getAllMessage_Briefing��JPQL��ѯ
 * ����������� ��Ҫ�õ��Զ����result��,
 * �κ��뱨���йص����Է�����+ʵ������������ࡣ
 *
 */
public class gAM_Result {

	private int id;// ���ŵ��˺�
	private String title;// ���ŵı���
	private String content;// ���ŵĶ�����
	private String web;// ������Դ
	private int tendclass;// ���ŵ������,-1����,0��,1��
    private Date date;//ʱ��

	public gAM_Result(int id, String title, String content,
			String web, int tendclass,Date date) {
		this.id = id;
		this.title = title;
		this.content = content;
		this.web = web;
		this.tendclass = tendclass;
		this.date=date;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getWeb() {
		return web;
	}

	public void setWeb(String web) {
		this.web = web;
	}

	public int getTendclass() {
		return tendclass;
	}

	public void setTendclass(int tendclass) {
		this.tendclass = tendclass;
	}

}
