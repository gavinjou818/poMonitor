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
	
	/**��������������õ�,
	 * ��ѡ���˴���֮��,��Ҫ��ʾ��Ӧ��ѡ��֮��Ĵ���,
	 * ͨ��������id���Ϳ�������ѡ���� 
	 * @author zhouzhifeng
	 * @param requestString  ����Ĵ��ԡ�id1,id2,id3������ʽ����
	 * @param max  �����Ŀ����
	 * @param index ��ʼָ�� index=(��ǰҳΪ�ڼ�ҳ)*(max);һ��ʼ��0��һ������
	 * @return
	 */
	public String getPreviewMessage(String requestString,int max,int index)
	{
		
		class JSONData 
		{
			public int sum;//�����ʱ����ڵ�����������
			public int now;//��ǰ�ж���������
			public List<gAM_Result> gAM_Result_Briefings;//��ȡ�Ĵ����Ľ����,��Ӧ��ҳλ��
		
			
			public JSONData(int sum,int now,List<gAM_Result> gAM_Result_Briefings) 
			{
				 this.sum=sum;
			     this.gAM_Result_Briefings=gAM_Result_Briefings;
			     this.now=now;
			}
		}
		//�����Ȼ�ȡ���е�id;
		String[] obj=requestString.split(",");
		//��������DAO
		NewsDAO newsDAO=new NewsDAO();
		//��ȡ����DAO
		NewsTendDAO newsTendDAO=new NewsTendDAO();
		
		//��ȡ�����趨�����Ŵ���
		List<gAM_Result> gAM_Result_Briefings=new ArrayList<>();
	    
		int DEF=0;//������,����һ��һ�������˶�����
		for(int i=index;DEF<max&&i<obj.length;DEF++,i++)
		{   
			//�Ƚ���Ӧid�����������
			News tmpNews=newsDAO.findById(Integer.parseInt(obj[i]));
		    //�Ѷ�Ӧ���ŵ������Է������
			List<NewsTend> newsTends=(List<NewsTend>) newsTendDAO.findByNewsId(Integer.parseInt(obj[i]));
			NewsTend newsTend=newsTends.get(0);
			//��������е�����
			gAM_Result gResult=new gAM_Result(tmpNews.getRelId(),tmpNews.getTitle(), 
					tmpNews.getContent(),tmpNews.getWeb(),newsTend.getTendclass(),newsTend.getDate());
			//��ӽ�ȥ
			gAM_Result_Briefings.add(gResult);
		}
		//��json����
		JSONData jsonData=new JSONData(obj.length, DEF, gAM_Result_Briefings);
		
		return JSON.toJSONString(jsonData);
	}
	
	
	
	
	
	/**
	 * @author zhouzhifeng
	 * @param requestString ������ַ����Ǵ���������ѡ�������id�ԡ�id1,id2,id3....��
	 *        ��ֵ����
	 * @return ������json��ǰ��,��������ص��඼д����,
	 *         ��Ҫ������YQSL.jsp
	 * ��һ��ͼ������Ҫ��ȡ����Ϣ.        
	 */
	public String getPreviewChart1(String requestString)
	{    
		
		String[] obj=requestString.split(",");
		
		//ͼ��ר��,value,name��Ӧ
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
		
		//��Ϣ��Դͼ
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
		String[] state = {"��","��","��"};
		
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
	  
		//�������ͼ������������Ϣ
		mtCome.setTypename("���ģʽ");
		mtCome.setTitle("��Ϣ��Դͼ");
		mtCome.setSubtitle("��ý����Ϣ��Դ");
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
