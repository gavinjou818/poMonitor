package pomonitor.analyse.entity;
/*
 * 语义角色
 */

public class TendArg 
{
	// 序号
	private String id;
	// 角色名称
	private String type;
	// 结束序号
	private int end;

	// 开始序号
	private int beg;

	public TendArg() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getEnd() {
		return end;
	}

	public void setEnd(int end) {
		this.end = end;
	}

	public int getBeg() {
		return beg;
	}

	public void setBeg(int beg) {
		this.beg = beg;
	}

}
