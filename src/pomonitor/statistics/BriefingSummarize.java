package pomonitor.statistics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.alibaba.fastjson.JSON;

import pomonitor.briefing.gAM_Result;
import pomonitor.entity.News;
import pomonitor.entity.NewsDAO;
import pomonitor.entity.NewsTend;
import pomonitor.entity.NewsTendDAO;

public class BriefingSummarize 
{
	
	/**这个方法是这样用的,
	 * 当选择了词条之后,就要显示对应的选择之后的词条,
	 * 通过给定的id集就可以这样选择了 
	 * @author zhouzhifeng
	 * @param requestString  给予的串以“id1,id2,id3”的形式赋予
	 * @param max  最大条目个数
	 * @param index 开始指引 index=(当前页为第几页)*(max);一开始以0第一次索引
	 * @return
	 */
	public String getPreviewMessage(String requestString,int max,int index)
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
		//首先先获取所有的id;
		String[] obj=requestString.split(",");
		//创建新闻DAO
		NewsDAO newsDAO=new NewsDAO();
		//获取新闻DAO
		NewsTendDAO newsTendDAO=new NewsTendDAO();
		
		//获取本来设定的新闻词条
		List<gAM_Result> gAM_Result_Briefings=new ArrayList<>();
	    
		int DEF=0;//数量戳,计算一下一共返回了多少条
		for(int i=index;DEF<max&&i<obj.length;DEF++,i++)
		{   
			//先将对应id的新闻提出来
			News tmpNews=newsDAO.findById(Integer.parseInt(obj[i]));
		    //把对应新闻的倾向性分析提出
			List<NewsTend> newsTends=(List<NewsTend>) newsTendDAO.findByNewsId(Integer.parseInt(obj[i]));
			NewsTend newsTend=newsTends.get(0);
			//提出词条中的新闻
			gAM_Result gResult=new gAM_Result(tmpNews.getRelId(),tmpNews.getTitle(), 
					tmpNews.getContent(),tmpNews.getWeb(),newsTend.getTendclass(),newsTend.getDate());
			//添加进去
			gAM_Result_Briefings.add(gResult);
		}
		//以json返回
		JSONData jsonData=new JSONData(obj.length, DEF, gAM_Result_Briefings);
		
		return JSON.toJSONString(jsonData);
	}
	
	
	
	
	
	/**
	 * @author zhouzhifeng
	 * @param requestString 请求的字符串是代表新闻所选择的所有id以“id1,id2,id3....”
	 *        传值过来
	 * @return 完整的json到前端,于新闻相关的类都写在这,
	 *         主要服务于YQSL.jsp
	 * 第一个图表所需要获取的信息.        
	 */
	public String getPreviewChart1(String requestString)
	{    
		
		String[] obj=requestString.split(",");
		
		//图表专用,value,name对应
		class ValueAndName
	    {
	        public String getValue() {
				return value;
			}
			public void setValue(String value) {
				this.value = value;
			}
			public String getName() {
				return name;
			}
			public void setName(String name) {
				this.name = name;
			}
			private String value;
	        private String name;
	         
	    }
		
		//信息来源图
		class MTCome
		{
			public String getTypename() {
				return typename;
			}
			public void setTypename(String typename) {
				this.typename = typename;
			}
			public String[] getLenddate() {
				return lenddate;
			}
			public void setLenddate(String[] lenddate) {
				this.lenddate = lenddate;
			}
			public String getTitle() {
				return title;
			}
			public void setTitle(String title) {
				this.title = title;
			}
			public String getSubtitle() {
				return subtitle;
			}
			public void setSubtitle(String subtitle) {
				this.subtitle = subtitle;
			}
			public List<ValueAndName> getData() {
				return data;
			}
			public void setData(List<ValueAndName> data) {
				this.data = data;
			}
			private String typename;
			private String[] lenddate;
			private String title;
			private String subtitle;
			private List<ValueAndName> data;
			
		}
		
		class GTofmedia
		{
			public String[] getLegenddata() {
				return legenddata;
			}
			public void setLegenddata(String[] legenddata) {
				this.legenddata = legenddata;
			}
			public List<ValueAndName> getSeriesdata() {
				return seriesdata;
			}
			public void setSeriesdata(List<ValueAndName> seriesdata) {
				this.seriesdata = seriesdata;
			}
			private String[] legenddata;
			private List<ValueAndName> seriesdata;
		}
		
		
		
		class JSONData 
		{
			
			public MTCome mtCome;
			public GTofmedia gTofmedia;
			public JSONData(MTCome mtCome,GTofmedia gTofmedia) 
			{
				 this.mtCome=mtCome;
				 this.gTofmedia=gTofmedia;
			}
		}
		String[] state = {"负","中","正"};
		
		MTCome mtCome=new MTCome();
		GTofmedia gTofmedia=new GTofmedia();
		
		NewsDAO newsDAO=new NewsDAO();
		NewsTendDAO newsTendDAO=new NewsTendDAO();
		
		Map<String,Integer> countWeb=new HashMap<>();
		Map<String,Integer> countTendClass=new HashMap<>();
		
		for(int i=0;i<obj.length;i++)
		{   
			News tmpNews=newsDAO.findById(Integer.parseInt(obj[i]));
			List<NewsTend> newsTends=newsTendDAO.findByNewsId(Integer.parseInt(obj[i]));
			NewsTend newsTend=newsTends.get(0);
			
			if(countWeb.get(tmpNews.getWeb())==null)
			{
				countWeb.put(tmpNews.getWeb(),1);
			}
			else {countWeb.put(tmpNews.getWeb(),
				  countWeb.get(tmpNews.getWeb())+1);}
			
			if(countTendClass.get(state[newsTend.getTendclass()+1])==null)
			{
				countTendClass.put(state[newsTend.getTendclass()+1], 1);
			}
			else 
			{      
				   countTendClass.put(state[newsTend.getTendclass()+1],
				   countTendClass.get(state[newsTend.getTendclass()+1])+1);
			}
			
		}
		
		String[] lenddate=new String[countWeb.size()];
		List<ValueAndName> data=new ArrayList<>();
		
		int index=0;
		for(Entry<String, Integer> entry:countWeb.entrySet())
		{
			ValueAndName valueAndName=new ValueAndName();
			valueAndName.setName(entry.getKey());
			valueAndName.setValue(entry.getValue().toString());
			lenddate[index++]=entry.getKey();
			data.add(valueAndName);
		}
	  
		//导入相关图标的相关属性信息
		mtCome.setTypename("面积模式");
		mtCome.setTitle("信息来源图");
		mtCome.setSubtitle("各媒体信息来源");
		mtCome.setLenddate(lenddate);
		mtCome.setData(data);
		
		List<ValueAndName> seriesdata=new ArrayList<>();
		String[] gTofmedia_legenddata=new String[countTendClass.size()];
		index=0;
		for(Entry<String, Integer> entry:countTendClass.entrySet())
		{
			ValueAndName valueAndName=new ValueAndName();
			valueAndName.setName(entry.getKey());
			valueAndName.setValue(entry.getValue().toString());
			gTofmedia_legenddata[index++]=entry.getKey();
			seriesdata.add(valueAndName);
		}
		
		gTofmedia.setLegenddata(gTofmedia_legenddata);
		gTofmedia.setSeriesdata(seriesdata);
		
		JSONData jsonData=new JSONData(mtCome,gTofmedia);
		return JSON.toJSONString(jsonData);
		
	}
}
