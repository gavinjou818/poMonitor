package pomonitor.test;

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

import pomonitor.briefing.Briefing;
import pomonitor.briefing.DocumentHandler;



/**
 * 用于测试方法
 * @author zhouzhifeng
 *
 */
public class TestBriefing 
{
     public static void main(String args[])
     {
    	 DocumentHandler documentHandler=new DocumentHandler();
    	 Briefing briefing=new Briefing();
    	 
    	 //组装模板所需数据
    	 Map<String, Object> dataMap=new HashMap<String,Object>();
    	 
    	 //组装数据
    	 
    	 dataMap.put("theme", "周志锋");
    	 Date date=new Date();
    	 dataMap.put("year", date.getYear()); 
    	 dataMap.put("month", date.getMonth()); 
    	 dataMap.put("day", date.getDay());
    	 
    	 dataMap.put("key_word", "周志锋"); 
    	 dataMap.put("keyword", "周志锋"); 
    	 
    	 
    	 dataMap.put("begin_year", date.getYear()); 
    	 dataMap.put("begin_month", date.getMonth()); 
    	 dataMap.put("begin_day", date.getDay());
    	 dataMap.put("begin_hour", date.getHours());
    	 dataMap.put("begin_minute", date.getMinutes());
    	 
    	 
    	 dataMap.put("end_year", date.getYear()); 
    	 dataMap.put("end_month", date.getMonth()); 
    	 dataMap.put("end_hour", date.getHours());
    	 dataMap.put("end_minute", date.getMinutes());
    	 
    	 dataMap.put("sum_infCount", "11");
    	 dataMap.put("max_year", "11");
    	 dataMap.put("max_month","12");
    	 dataMap.put("max_day", "11");
    	 dataMap.put("sum_artCount", "11");
    	 dataMap.put("first_title", "12");
    	 dataMap.put("media_type", "11");
    	 dataMap.put("media_list", "12");
    	 dataMap.put("tend", "fu");
    	 
    	 List<Map<String, Object>> newsList=new ArrayList<Map<String,Object>>();
    	 for(int i=1;i<=10;i++)
    	 {
    		   Map<String, Object> map=new HashMap<String, Object>();
    		   map.put("number", "标题"+i);
    	       map.put("content", "内容"+(i*2));
    	       map.put("web", "作者"+(i*3));
    	       newsList.add(map);
    	 }
    	 dataMap.put("newsList", newsList);
    	 
    	 
    	 dataMap.put("first_year", date.getYear()); 
    	 dataMap.put("first_month", date.getMonth()); 
    	 dataMap.put("first_day", date.getDay());
    	 dataMap.put("first_hour", date.getHours());
    	 dataMap.put("first_minute", date.getMinutes());
    	 dataMap.put("first_second", date.getHours());
    	 dataMap.put("media_source", date.getMinutes());
    	 dataMap.put("first_content", date.getMinutes());
    	 
    	 dataMap.put("max_year", date.getHours());
    	 dataMap.put("max_month", date.getMinutes());
    	 dataMap.put("max_day", date.getMinutes());
    	 dataMap.put("max_hour", date.getMinutes()); 
    	 
    	 
    	 briefing.setFilePath("D:/aaa/");
    	 briefing.setFileName("a.doc");
    	 briefing.setDataMap(dataMap);
    	 briefing.setTemplateName("template1.ftl");
    	 
    	 documentHandler.
    	 createWord(briefing);
    	
    	 
 	
    	 
    	 
     }
}
