package pomonitor.briefing;

import java.util.Date;

/**
 * 
 * 
 * @author zhouzhifeng
 * pomonitor.statistics.Summarize的getAllMessage_Briefing的JPQL查询
 * 语句的联结关联 需要用的自定义的result类,
 * 任何与报表有关的需以方法名+实体名相关联定类。
 *
 */
public class gAM_Result {

	private int id;// 新闻的账号
	private String title;// 新闻的标题
	private String content;// 新闻的短内容
	private String web;// 新闻来源
	private int tendclass;// 新闻的倾向度,-1代表负,0中,1正
    private Date date;//时间

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
