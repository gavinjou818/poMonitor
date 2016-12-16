package pomonitor.briefing;

import java.io.BufferedWriter;  
import java.io.File;  
import java.io.FileNotFoundException;  
import java.io.FileOutputStream;  
import java.io.IOException;  
import java.io.OutputStreamWriter;  
import java.io.UnsupportedEncodingException;  
import java.io.Writer;  
import java.util.Map;  
  
import freemarker.template.Configuration;  
import freemarker.template.Template;  
import freemarker.template.TemplateException; 
/**
 * .ftl  freemarket�ĵ�������
 * @author zhouzhifeng
 *
 */
public class DocumentHandler 
{
	 //freemarket������
     private Configuration configuration=null;
       
     //��ʼ������,������Ĭ�ϱ���
     public  DocumentHandler()
     {   
    	 //��������ʵ��
    	 configuration =new Configuration();
    	 //���ñ���
    	 configuration.setDefaultEncoding("utf-8");
     }
     
     
     /**
      * @Desc:����word�ļ�
      * @author zhouzhifeng
      * @Date 2016/12/16
      * ģ�嶼����TEMPLATE����
      * @param dataMap  key->value ��Ӧģ���EA�����һһ��Ӧ
      * @param templateName  ģ�������.ftl
      * @param filePath  ����ļ�·��
      * @param fileName  ����ļ�����
      */
     public void createWord(Briefing briefing)
     {   
        //����ģ���Ŀ¼
        configuration.setClassForTemplateLoading(this.getClass(), "/TEMPLATE");
        //��ȡָ��ģ��
        Template template=null;
        try 
        {
			template=configuration.getTemplate(briefing.getTemplateName());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
        //����ģ�������.
        File outFile=new File(briefing.getFilePath()+briefing.getFileName());
       
        if(!outFile.getParentFile().exists())
        {
        	outFile.getParentFile().mkdirs();
        }
        
        Writer out=null;
        FileOutputStream fos=null;
        try 
        {
			
           fos=new FileOutputStream(outFile);
           OutputStreamWriter oWriter=new OutputStreamWriter(fos,"UTF-8");
		   out=new BufferedWriter(oWriter);
        } catch (Exception e) {
           e.printStackTrace();		
		}
        
        //ӳ��ģ�岢�����ָ��
        try 
        {
        	template.process(briefing.getDataMap(), out);
        	out.close();
        	fos.close();
			
		} catch (Exception e) {
			e.printStackTrace();	
		}
    	 
     }
     
}
