package pomonitor.briefing;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.sleepycat.je.txn.ThinLockImpl;

import pomonitor.util.PropertiesReader;


/** 
 *   
 *   
 *   模拟对应模板类
 *   
 *  
 *   @author zhouzhifeng
 * 
 */

public class BriefingData 
{
    private String filePath;//文件路径名,已经定好了,可以自行修改
    private String fileName;//文件名称
    private Map<String, Object> dataMap;//存入的对应数据
    private String templateName;//对应模板名
  
    public String getFilePath() 
    {
		return filePath;
	}
    public void setFilePath(String filePath) 
    {
		this.filePath=filePath;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public Map<String, Object> getDataMap() {
		return dataMap;
	}
	public void setDataMap(Map<String, Object> dataMap) {
		this.dataMap = dataMap;
	}
	public String getTemplateName() {
		return templateName;
	}
	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}
	     
}
