//生成报表所要的所有图表都在这里生成,前面生成的是网页上所需的图表,请求方式的不同所以要分成两种
//都应以Briefing开头


//媒体信息来源
/**
 *  loadurl 往哪里头这个方法
 *  requestString: 所选择的词条,其中字符串要以“id1,id2,id3,id4,...idn)的格式存入
 *  method:所请求的地方的方法
 *  elementId:在哪个地方加载
 *  
 */
function BriefingInformationSourcesChart(Datt,elementId)
{    
			   var Chart;
		       var datt=Datt.mtCome;
		       var myChart = echarts.init(document.getElementById(elementId));
		        
		
		         option = 
		         {
						  title : {
							  text: datt.title,       //图标标题
							  subtext:datt.subtitle,   //图标二级标题
							  x:'center'
						  },
						  tooltip : {
							  trigger: 'item',
							  formatter: "{a} <br/>{b} : {c} ({d}%)"
						  },
						   legend: 
						   {
			    		        orient: 'vertical',
			    		        left: 'left',
			    		        data: datt.lenddate
			    		   },
						  toolbox: {
							  
							   
							  show : true,
							  feature : {
								  mark : {show: true},
								  dataView : {show: true, readOnly: false},
								  magicType : {
									  show: true, 
									  type: ['pie', 'funnel']
								  },
								  restore : {show: true},
								  saveAsImage : {show: true}
							  },
							  orient : 'vertical',
						  },
						  calculable : true,
						  series : [
						   
							  {
								  name:datt.typename,
								  type:'pie',
								  radius : [30, 110],
								  center : ['50%', 200],
								  roseType : 'area',
								  x: '50%',               // for funnel
								  max: 40,                // for funnel
								  sort : 'ascending',     // for funnel
								  data:datt.data,         //面积模式图标中的数据
								  itemStyle: 
			    		           {
			    		            	normal: {label:{  
			    		                     show:true,  
			    		                     formatter:'{b} : {c} ({d}%)'  
			    		                },  
			    		                labelLine:{show:true}},      
			    		           }
							  },
							  
					
						  ]
					  }; 
		           myChart.setOption(option);
		           Chart=myChart;
		     
		     return Chart;
}


//獲取當前的所有負,中,正的总数
//媒体总倾向性分布图
function BriefingGeneralTendencyOfmediaChart(Datt,elementId)
{    
			   var Chart;
		       var datt=Datt.gTofmedia;
		       var myChart = echarts.init(document.getElementById(elementId));

		       option = 
		       {
		    		    title : 
		    		    {
		    		        text: '媒体总倾向性分布图',
		    		        subtext: '关于媒体总倾向性分析',
		    		        x:'center'
		    		    },
		    		    tooltip : {
		    		        trigger: 'item',
		    		        formatter: "{a} <br/>{b} : {c} ({d}%)"
		    		    },
		    		    toolbox:
		    		    {
							  
							   
							  show : true,
							  feature : {
								  mark : {show: true},
								  dataView : {show: true, readOnly: false},
								  magicType : {
									  show: true, 
									  type: ['pie', 'funnel']
								  },
								  restore : {show: true},
								  saveAsImage : {show: true}
							  },
							  orient : 'vertical',
						},
		    		    legend: {
		    		        orient: 'vertical',
		    		        left: 'left',
		    		        data: datt.legenddata
		    		    },
		    		    series : [
		    		        {
		    		            name: '媒体总倾向性',
		    		            type: 'pie',
		    		            radius : '55%',
		    		            center: ['50%', '60%'],
		    		            data:datt.seriesdata,  
		    		            itemStyle: 
			    		        {
			    		            	normal: {label:{  
			    		                     show:true,  
			    		                     formatter:'{b} : {c} ({d}%)'  
			    		                },  
			    		                labelLine:{show:true}},      
			    		        }
		    		        }
		    		    ]
		    		};
		           myChart.setOption(option);
		           Chart=myChart;
		     
		     return Chart;
}