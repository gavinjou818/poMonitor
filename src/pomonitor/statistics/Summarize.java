package pomonitor.statistics;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.json.Json;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import javax.persistence.criteria.From;

import org.apache.xmlbeans.impl.xb.xsdschema.Public;

import com.alibaba.fastjson.JSON;
import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;

import pomonitor.analyse.ArticleTendAnalyse;
import pomonitor.briefing.gAM_Result;
import pomonitor.entity.EntityManagerHelper;
import pomonitor.entity.News;
import pomonitor.entity.NewsDAO;
import pomonitor.entity.NewsTend;
import pomonitor.entity.NewsTendDAO;

public class Summarize 
{
	public String getTendency(String startTime, String endTime)throws ParseException 
	{   
		
	   
		class Series 
		{
			public Series(List<Date> dates, HashMap<Date, Integer> allNews,HashMap<Date, Integer> negNews) 
			{
				all = new ArrayList<>();
				neg = new ArrayList<>();
				for (int i = 0; i < dates.size(); i++) {
					Date date = dates.get(i);
					all.add(allNews.get(date));
					neg.add(negNews.get(date));
				}
			}

			public ArrayList<Integer> neg;
			public ArrayList<Integer> all;
		}

		class JSONData 
		{
			public Series series;
			public String message;
			public int status;

			public JSONData(Series series) {
				this.series = series;
				this.message = "Query success!";
				this.status = 0;
			}

		}
		
		NewsTendDAO newsTendDAO = new NewsTendDAO();
		List<NewsTend> newsTends = new ArrayList<>();
		EntityManagerHelper.beginTransaction();
		newsTends = newsTendDAO.findBetweenDate(startTime, endTime);
		
		EntityManagerHelper.commit();
		EntityManagerHelper.closeEntityManager();//zhouzhifeng 加的，完成事务必须关闭
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date start = simpleDateFormat.parse(startTime);
		Date end = simpleDateFormat.parse(endTime);
		Calendar calendar = Calendar.getInstance();
		List<Date> dates = new ArrayList<>();
		HashMap<Date, Integer> allNews = new HashMap<>();
		HashMap<Date, Integer> negNews = new HashMap<>();
		while (start.getTime() <= end.getTime())
		{   
			
			
			allNews.put(start, 0);
			negNews.put(start, 0);
			dates.add(start);
			calendar.setTime(start);
			calendar.add(Calendar.DAY_OF_YEAR, 1);
		    start = calendar.getTime();
		}
		for (int i = 0; i < newsTends.size(); i++) 
		{
			Date date = newsTends.get(i).getDate();
			int allNewsKey = allNews.get(date);
			allNewsKey++;
			allNews.put(date, allNewsKey);
			int tend = newsTends.get(i).getTendclass();
			if (tend < 0) 
			{
				int negNewsKey = negNews.get(date);
				negNewsKey++;
				negNews.put(date, negNewsKey);
			}
		}
		Series series = new Series(dates, allNews, negNews);
		JSONData json = new JSONData(series);

		return JSON.toJSONString(json);
	}
    //原来没数据状态 @zhouzhifeng,修改了接口
	public String checkStatus(String endTime) throws ParseException 
	{
		class NewsTendencyClassifyByWeb 
		{
			public List<Integer> totalNum;
			public List<Integer> negativeNum;
			public List<String> websiteName;

			public NewsTendencyClassifyByWeb(List<NewsTend> newsTendLists) 
			{
				totalNum = new ArrayList<>();
				negativeNum = new ArrayList<>();
				websiteName = new ArrayList<>();
				HashMap<String, Integer> hashMap = new HashMap<>();
				for (int i = 0; i < newsTendLists.size(); i++) {
					NewsTend newsTend = newsTendLists.get(i);
					String webName = ArticleTendAnalyse
							.EnglishWebNameToChinese(newsTend.getWeb());
					if (hashMap.containsKey(webName)) {
						int index = hashMap.get(webName);
						totalNum.set(index, totalNum.get(index) + 1);
						if (newsTend.getTendclass() < 0) {
							negativeNum.set(index, negativeNum.get(index) + 1);
						}
					} else {
						websiteName.add(webName);
						totalNum.add(1);
						int index = websiteName.size() - 1;
						hashMap.put(webName, index);
						if (newsTend.getTendclass() < 0) {
							negativeNum.add(1);
						} else {
							negativeNum.add(0);
						}
					}
				}
			}
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = sdf.parse(endTime);//2015-09-14;
		// date = new Date(System.currentTimeMillis());
		NewsTendDAO newsTendDAO = new NewsTendDAO();
		List<NewsTend> newsTendLists = new ArrayList<>();
		EntityManagerHelper.beginTransaction();
		newsTendLists = newsTendDAO.findBetweenDate(sdf.format(date),
				sdf.format(date));
		EntityManagerHelper.commit();
		NewsTendencyClassifyByWeb classifyByWeb = new NewsTendencyClassifyByWeb(
				newsTendLists);
		return JSON.toJSONString(classifyByWeb);
	}

	
	/**
	 * 最新舆情
	 * @return
	 */
	public String getLatestMessage() throws ParseException 
	{
		 
		final int limit=13;//限制获取条数
		class Result {
			
			public String date;//日期，示例：今日，昨日
			public String source;//来源
			public String time;//日期，格式：yyyy-MM-dd
			public String title;//标题
			public String url;//网址
			
			public Result() {
				
			}
			
			public Result(String date, String source, String time, String title, String url) 
			{
				this.date = date;
				this.source = source;
				this.time = time;
				this.title = title; 
				this.url = url;
			}
		}
		
		
	    Calendar cal = Calendar.getInstance(); 
	    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	    String nowday = simpleDateFormat.format(cal.getTime());
	    
	    cal.add(Calendar.DAY_OF_MONTH, -1);
	    
	    String yesterday = simpleDateFormat.format(cal.getTime());
	    
		NewsDAO newsDAO = new NewsDAO();
		List<News> newsList = new ArrayList<>();
		EntityManagerHelper.beginTransaction();
		
		newsList = newsDAO.findBetweenDate("2015-1-1", nowday);
		EntityManagerHelper.commit();
		EntityManagerHelper.closeEntityManager();//zhouzhifeng 加的，完成事务必须关闭
		
		
		Result[] results = new Result[limit];
		
	    String date = "";
		String source = "";
		String time = "";
		String title = "";
		String url = "";
		
		int count = 0;
		//优先把当天的新闻提取出来
		for(int i=0, j=0; i<newsList.size() && j<limit; i++) 
		{
			News news = newsList.get(i);
			Date newsdate = news.getTime();
			
		    
			String dateString = simpleDateFormat.format(newsdate);
			//System.out.println("========= date"+dateString);
			if(dateString.equals(nowday)) {
				date = "今日";
				source = news.getWeb();
				SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
				time = sDateFormat.format(newsdate);
				title = news.getTitle();
				url = news.getUrl();
				results[j++] = new Result(date, source, time, title, url);
				count = j;
			} 
		}
		
		//若当天的新闻不足五条，再从前一天的新闻中提取
		if(count < limit) 
		{
			for(int i=0, j=count; i<newsList.size() && j<limit; i++) {
				News news = newsList.get(i);
				Date newsdate = news.getTime();
				String dateString = simpleDateFormat.format(newsdate);
				//if(dateString.equals(yesterday)) {
					date = "昨日";
					source = news.getWeb();
					SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
					time = sDateFormat.format(newsdate);
					title = news.getTitle();
					url = news.getUrl();
					results[j++] = new Result(date, source, time, title, url);
				//} 
			}
		}
		System.out.println("!!!!"+JSON.toJSONString(results));
		
		return JSON.toJSONString(results);
	}
	
	
    /**
     * 	 @author zhouzhifeng
	 *  
	 *   目的:用来存储报表，待别人选择词条，然后存去session中,然后选择对应词条之后,
	 *   生成报表，报表会显示对应的词条。
	 *   待改进:这样的处理方法可能很慢,最好预先缓存。
	 *   
     * @param startTime  开始时间
     * @param endTime    结束时间
     * @param max        每页多少条数据
     * @param index      指引=(第几页*max), 以0索引开始
     * @return
     * @throws Exception
     */
	
	@SuppressWarnings("unchecked")
	public String getAllMessage_Briefing(String startTime, String endTime,int max,int index) throws Exception 
	{
		   
		
		class JSONData 
		{
			public int sum;//在这个时间段内的总数据条数
			public int now;//当前有多少条返回
			public List<gAM_Result> gAM_Result_Briefings;//获取的词条的结果集,对应网页位置
		
			
			public JSONData(int sum,int now,List<gAM_Result> gAM_Result_Briefings) 
			{
				 this.sum=sum;
			     this.gAM_Result_Briefings=gAM_Result_Briefings;
			     this.now=now;
			}
		}
		   
		
		 
		  //获取实体管理者
		  EntityManager  entityManager=EntityManagerHelper.getEntityManager();
		  
		  //JPQL语句,获取数据库表新闻类和新闻倾向类的联合关系中的信息。
		  final String queryString ="SELECT new pomonitor.briefing.gAM_Result(n.relId,n.title,n.content,n.web,nt.tendclass,nt.date) as t "
				  +" from News n JOIN NewsTend nt where (n.relId = nt.newsId and (n.time between ?1 and ?2))";
          //创建查询
		  Query query = entityManager.createQuery(queryString);
		  
		  //格式化时间
		  Date startDate = null;
	      Date endDate = null;
	      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		   try 
		    {
				startDate = sdf.parse(startTime);
				endDate = sdf.parse(endTime);
			} 
		   catch (ParseException e) 
			{
				e.printStackTrace();
			}
		   
		  //设置对应位置的参数 
	      query.setParameter(1, startDate, TemporalType.DATE);
		  query.setParameter(2, endDate, TemporalType.DATE);
		  //先获取全部数据,得到总条数。
		  int sum=query.getResultList().size();
		  //获取分页后的数据。
		  List<gAM_Result> gAM_Result_Briefings=query.setMaxResults(max).setFirstResult(index).getResultList();
		  //多少有多少数据返回
		  int now=gAM_Result_Briefings.size();
		  //用JSON往前台输出。
		  JSONData jsonData=new JSONData(sum,now,gAM_Result_Briefings);
		  //关闭实体管理者
		  entityManager.close();

		  //返回json
		  return JSON.toJSONString(jsonData);
		  
	}
	
	
	public String getMediaAnalysis(String startTime,String endTime)
	{   
		
		class element3
		{
			public boolean show;
			public String position;
			public element3(boolean show,String position)
			{
				this.show=show;
				this.position=position;
			}
		}
		class element2
		{
			public element3 normal;
			public element2(element3 normal)
			{
				this.normal=normal;
			}
		}
		class element1
		{
			public String name;
			public String type;
			public String stack;
			public element2 label;
			public Integer[] data;
			public element1(String name,String type,String stack,element2 label,Integer[] data)
   			{
	                  this.name=name;
	                  this.type=type;
	                  this.stack=stack;
	                  this.label=label;
	                  this.data=data;
			}
		}
		class JSONData 
		{
			public String[] lengdata;
			public String[] xAxisdata;
			public List<element1> series;
			public JSONData(String[] lengdata,String[] xAxisdata,List<element1> series)
			{
				this.lengdata=lengdata;
				this.xAxisdata=xAxisdata;
				this.series=series;
			}
		}
		

		  //获取实体管理者
		  EntityManager  entityManager=EntityManagerHelper.getEntityManager();
		  
		  //JPQL语句,获取数据库表新闻类和新闻倾向类的联合关系中的信息。
		  final String queryString ="SELECT new pomonitor.briefing.gAM_Result(n.relId,n.title,n.content,n.web,nt.tendclass,nt.date) as t "
				  +" from News n JOIN NewsTend nt where (n.relId = nt.newsId and (n.time between ?1 and ?2))";
          //创建查询
		  Query query = entityManager.createQuery(queryString);
		  
		  //格式化时间
		  Date startDate = null;
	      Date endDate = null;
	      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		   try 
		    {
				startDate = sdf.parse(startTime);
				endDate = sdf.parse(endTime);
			} 
		   catch (ParseException e) 
			{
				e.printStackTrace();
			}
		   
		  //设置对应位置的参数 
	      query.setParameter(1, startDate, TemporalType.DATE);
		  query.setParameter(2, endDate, TemporalType.DATE);

		  //获取分页后的数据。
		  List<gAM_Result> gAM_Result_Briefings=query.getResultList();
		  
		  Map<String,Integer[]> countWebState=new HashMap<>();
		  
		  for(int i=0;i<gAM_Result_Briefings.size();i++)
		  {
			  
			   gAM_Result grm_Result=gAM_Result_Briefings.get(i);
			  if(countWebState.get(grm_Result.getWeb())==null)
				{   
				    Integer[] status={0,0,0};
				    status[checkstate(grm_Result.getTendclass())]+=1;
				    countWebState.put(grm_Result.getWeb(),status);
				}
				else 
				{    
					  Integer[] status=countWebState.get(grm_Result.getWeb());
					  status[checkstate(grm_Result.getTendclass())]+=1;
					  countWebState.put(grm_Result.getWeb(),status);
			    }
		  }
		  
		  
		  String[] xAxisdata=new String[countWebState.size()]; 
		  String[] lengdata={"负","中","正"};
		  Integer[] neg= new Integer[countWebState.size()];
		  Integer[] cen= new Integer[countWebState.size()];
		  Integer[] pos= new Integer[countWebState.size()];
          
		  List<element1> series=new ArrayList<>();
		  
		  int index=0;
		  for( Entry<String, Integer[]> entry:countWebState.entrySet())
		  {
			  
			  Integer[] tmp=entry.getValue();
			  xAxisdata[index]=entry.getKey();
			  neg[index]=tmp[0];
			  cen[index]=tmp[1];
			  pos[index++]=tmp[2];
			  
	          
		  }
          
		  element3 e3=new element3(true, "insideRight");
		  element2 e2=new element2(e3);
		  element1 e1=new element1(lengdata[0], "bar", "总量", e2,neg);
		  JSONData jsonData=new JSONData(lengdata, xAxisdata, series);
		  series.add(e1);
		  
		  e3=new element3(true, "insideRight");
		  e2=new element2(e3);
		  e1=new element1(lengdata[1], "bar", "总量", e2,cen);
		  jsonData=new JSONData(lengdata, xAxisdata, series);
		  series.add(e1);
		  
		  e3=new element3(true, "insideRight");
		  e2=new element2(e3);
		  e1=new element1(lengdata[2], "bar", "总量", e2,pos);
		  jsonData=new JSONData(lengdata, xAxisdata, series);
		  series.add(e1);
		  
		  //System.out.println(JSON.toJSON(jsonData));
		  entityManager.close();
		 
		  return JSON.toJSONString(jsonData);
	}
	private int checkstate(int val)
	{
		if(val==0) return 1;//中
		else if(val>0) return 2;//正
		return 0;//负
	}
	
	//zhouzhifeng ----->
	
}
