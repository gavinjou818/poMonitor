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
 * .ftl  freemarket文档加载器
 * @author zhouzhifeng
 *
 */
public class DocumentHandler 
{
	 //freemarket加载器
     private Configuration configuration=null;
       
     //初始化参数,并设置默认编码
     public  DocumentHandler()
     {   
    	 //创建配置实例
    	 configuration =new Configuration();
    	 //设置编码
    	 configuration.setDefaultEncoding("utf-8");
     }
     
     
     /**
      * @Desc:生成word文件
      * @author zhouzhifeng
      * @Date 2016/12/16
      * 模板都存在TEMPLATE包里
      * @param dataMap  key->value 对应模板的EA表达中一一对应
      * @param templateName  模板的名称.ftl
      * @param filePath  输出文件路径
      * @param fileName  输出文件名称
      */
     public void createWord(Briefing briefing)
     {   
        //加载模板根目录
        configuration.setClassForTemplateLoading(this.getClass(), "/TEMPLATE");
        //获取指定模板
        Template template=null;
        try 
        {
			template=configuration.getTemplate(briefing.getTemplateName());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
        //基本模板输出流.
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
        
        //映射模板并输出到指定
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
