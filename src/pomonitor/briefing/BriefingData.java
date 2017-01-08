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
 *   ģ���Ӧģ����
 *   
 *  
 *   @author zhouzhifeng
 * 
 */

public class BriefingData 
{
    private String filePath;//�ļ�·����,�Ѿ�������,���������޸�
    private String fileName;//�ļ�����
    private Map<String, Object> dataMap;//����Ķ�Ӧ����
    private String templateName;//��Ӧģ����
  
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
