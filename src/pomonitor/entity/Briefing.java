package pomonitor.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Briefing entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "briefing", catalog = "pomonitor")
public class Briefing implements java.io.Serializable 
{
    
	
	/**
	 *  @author zhouzhifeng
	 *  Briefing 表设计解释
	 *  
	 *  id 唯一id;
	 *  userid 用户对应的id,为外键,谁生成这份报表的
	 *  time 生成报表的时间
	 *  basepath 基本访问路径,好像localhost:8080 或者127.0.x.x从接口种访问的路径,该路径设置是以免服务器更换端口后,文件虽在,但
	 *           链接已变的问题
	 *  entityurl 实际储存的地址,因为file也就只能操作本地的
	 *  docpath  doc文件路径 filepath+filename;
	 *  pdfpath  pdf文件路径 filepath+filename;
	 *  
	 */
	
	// Fields

	private Integer id;
	private Integer userid;
	private Date time;
	private String basepath;
	private String name;
	private String entityurl;
	private String docpath;
	private String pdfpath;
	private String virtualname;

	// Constructors

	/** default constructor */
	public Briefing() {
	}

	/** full constructor */
	public Briefing(Integer userid, Date time, String basepath, String name,
			String entityurl, String docpath, String pdfpath) {
		this.userid = userid;
		this.time = time;
		this.basepath = basepath;
		this.name = name;
		this.entityurl = entityurl;
		this.docpath = docpath;
		this.pdfpath = pdfpath;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	

	@Temporal(TemporalType.DATE)
	@Column(name = "time", nullable = false, length = 10)
	public Date getTime() {
		return this.time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	@Column(name = "basepath", nullable = false)
	public String getBasepath() {
		return this.basepath;
	}

	public void setBasepath(String basepath) {
		this.basepath = basepath;
	}

	@Column(name = "name", nullable = false)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "entityurl", nullable = false)
	public String getEntityurl() {
		return this.entityurl;
	}

	public void setEntityurl(String entityurl) {
		this.entityurl = entityurl;
	}

	@Column(name = "docpath", nullable = false)
	public String getDocpath() {
		return this.docpath;
	}

	public void setDocpath(String docpath) {
		this.docpath = docpath;
	}

	@Column(name = "pdfpath", nullable = false)
	public String getPdfpath() {
		return this.pdfpath;
	}

	public void setPdfpath(String pdfpath) {
		this.pdfpath = pdfpath;
	}
    
	@Column(name = "userid", nullable = false)
	public Integer getUserid() {
		return userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}
    
	@Column(name = "virtualname", nullable = false)
	public String getVirtualname() {
		return virtualname;
	}

	public void setVirtualname(String virtualname) {
		this.virtualname = virtualname;
	}

}