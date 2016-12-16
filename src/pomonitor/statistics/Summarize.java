package pomonitor.statistics;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.alibaba.fastjson.JSON;

import pomonitor.analyse.ArticleTendAnalyse;
import pomonitor.entity.EntityManagerHelper;
import pomonitor.entity.News;
import pomonitor.entity.NewsDAO;
import pomonitor.entity.NewsTend;
import pomonitor.entity.NewsTendDAO;

public class Summarize {
	public String getTendency(String startTime, String endTime)
			throws ParseException {
		class Series {
			public Series(List<Date> dates, HashMap<Date, Integer> allNews,
					HashMap<Date, Integer> negNews) {
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

		class JSONData {
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
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date start = simpleDateFormat.parse(startTime);
		Date end = simpleDateFormat.parse(endTime);
		Calendar calendar = Calendar.getInstance();
		List<Date> dates = new ArrayList<>();
		HashMap<Date, Integer> allNews = new HashMap<>();
		HashMap<Date, Integer> negNews = new HashMap<>();
		while (start.getTime() <= end.getTime()) {
			allNews.put(start, 0);
			negNews.put(start, 0);
			dates.add(start);
			calendar.setTime(start);
			calendar.add(Calendar.DAY_OF_YEAR, 1);
			start = calendar.getTime();
		}
		for (int i = 0; i < newsTends.size(); i++) {
			Date date = newsTends.get(i).getDate();
			int allNewsKey = allNews.get(date);
			allNewsKey++;
			allNews.put(date, allNewsKey);
			int tend = newsTends.get(i).getTendclass();
			if (tend < 0) {
				int negNewsKey = negNews.get(date);
				negNewsKey++;
				negNews.put(date, negNewsKey);
			}
		}
		Series series = new Series(dates, allNews, negNews);
		JSONData json = new JSONData(series);

		return JSON.toJSONString(json);
	}

	public String checkStatus() throws ParseException {
		class NewsTendencyClassifyByWeb {
			public List<Integer> totalNum;
			public List<Integer> negativeNum;
			public List<String> websiteName;

			public NewsTendencyClassifyByWeb(List<NewsTend> newsTendLists) {
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
		Date date = sdf.parse("2015-09-14");
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
	public String getLatestMessage() throws ParseException {
		
		class Result {
			
			public String date;//日期，示例：今日，昨日
			public String source;//来源
			public String time;//日期，格式：yy-MM-dd
			public String title;//标题
			public String url;//网址
			
			public Result() {
				
			}
			
			public Result(String date, String source, String time, String title, String url) {
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
		
		Result[] results = new Result[5];
		
	    String date = "";
		String source = "";
		String time = "";
		String title = "";
		String url = "";
		
		int count = 0;
		//优先把当天的新闻提取出来
		for(int i=0, j=0; i<newsList.size() && j<5; i++) {
			News news = newsList.get(i);
			Date newsdate = news.getTime();
			String dateString = simpleDateFormat.format(newsdate);
			if(dateString.equals(nowday)) {
				date = "今日";
				source = news.getWeb();
				SimpleDateFormat sDateFormat = new SimpleDateFormat("yy-MM-dd");
				time = sDateFormat.format(newsdate);
				title = news.getTitle();
				url = news.getUrl();
				results[j++] = new Result(date, source, time, title, url);
				count = j;
			} 
		}
		
		//若当天的新闻不足五条，再从前一天的新闻中提取
		if(count < 5) {
			for(int i=0, j=count; i<newsList.size() && j<5; i++) {
				News news = newsList.get(i);
				Date newsdate = news.getTime();
				String dateString = simpleDateFormat.format(newsdate);
				//if(dateString.equals(yesterday)) {
					date = "昨日";
					source = news.getWeb();
					SimpleDateFormat sDateFormat = new SimpleDateFormat("yy-MM-dd");
					time = sDateFormat.format(newsdate);
					title = news.getTitle();
					url = news.getUrl();
					results[j++] = new Result(date, source, time, title, url);
				//} 
			}
		}
		//System.out.println(JSON.toJSONString(results));
		return JSON.toJSONString(results);
	}

}
