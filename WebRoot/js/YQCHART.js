		//本js保存所需要的所有图表,方便报表使用
		
		//传入时间,转换成数据库适用格式
		function setFormat(date)
		{   
			  if(date instanceof Date)
		      {
				  var year = date.getFullYear();
		          var month = date.getMonth();
		          var date = date.getDate();
		          return (year + "-" + month + "-" + date);
		      }
			  
			  return date;
		};
		//地域分布图
		function ChinaPictureChart(loadurl,elementId, starttime, endtime,userId,method)
		{
			 var Chart;
				$.ajax({
			        type: "GET",
			        url:loadurl,
			        dataType: "json",
			        data : 
					{
						"start_time" : setFormat(starttime),
						"end_time" : setFormat(endtime),
					    "userId" : userId,
						"method" : method
					},
			        success: function(datt) {
			            
			        	
			            var myChart = echarts.init(document.getElementById(elementId));
		
		
			              option = {
			                title : {
			                    text: datt.title,
			                    subtext: datt.subtitle,
			                    x:'center'
			                },
			                tooltip : {
			                    trigger: 'item'
			                },
			                legend: {
			                    orient: 'vertical',
			                    x:'left',
			                    data:datt.legenddata
			                },
			                dataRange: {
			                    min: 0,
			                    max: 2500,
			                    x: 'left',
			                    y: 'center',
			                    text:['高','低'],           // 文本，默认为数值文本
			                    calculable : true
			                },
			                toolbox: {
			                    show: true,
			                    orient : 'vertical',
			                    x: 'right',
			                    y: 'center',
			                    feature : {
			                        mark : {show: true},
			                        dataView : {show: true, readOnly: false},
			                        restore : {show: true},
			                        saveAsImage : {show: true}
			                    }
			                },
			                roamController: {
			                    show: true,
			                    x: 'right',
			                    mapTypeControl: {
			                        'china': true
			                    }
			                },
			                series : [
			                    {
			                        name: 'iphone3',   //图例
			                        type: 'map',
			                        mapType: 'china',
			                        roam: false,
			                        itemStyle:{
			                            normal:{label:{show:true}},
			                            emphasis:{label:{show:true}}
			                        },
			                        data:datt.partprovince1  //图例对应数据
			                    },
			                    {
			                        name: 'iphone4',
			                        type: 'map',
			                        mapType: 'china',
			                        itemStyle:{
			                            normal:{label:{show:true}},
			                            emphasis:{label:{show:true}}
			                        },
			                        data:datt.partprovince2
			                    },
			                    {
			                        name: 'iphone5',  
			                        type: 'map',
			                        mapType: 'china',
			                        itemStyle:{
			                            normal:{label:{show:true}},
			                            emphasis:{label:{show:true}}
			                        },
			                        data:datt.partprovince3
			                    }
			                ]
			            };
			            
			            myChart.setOption(option);
			            Chart=myChart;
		
			        }
			    })
			    return Chart;
		}
		
		
		//今日舆情图
		function PublicOpinionTodayChart(loadurl,elementId, starttime, endtime,userId,method)
		{  
			 
			 var Chart;
			 $.ajax({
		         type: "GET",
		         url:loadurl,
		         dataType: "json",
		         data : 
					{
						"start_time" : setFormat(starttime),
						"end_time" : setFormat(endtime),
					    "userId" : userId,
						"method" : method
					},
		
		         success: function(datt) 
		         {
		          var myChart = echarts.init(document.getElementById(elementId));
		
				
				option = 
				{
				tooltip : {
				 trigger: 'axis',
				 axisPointer : {            // 坐标轴指示器，坐标轴触发有效
				     type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
				 }
				},
				legend: {                   //图例
				 data: ['正面', '负面'],        //图例数据
				},
				grid: {
				 left: '0%',
				 right: '0%',
				 top:'5%',
				 bottom:'10%',
				 containLabel: true
				},
				xAxis:  {
				 type: 'value'
				},
				yAxis: {
				 type: 'category',
				 data: datt.websiteName
				},
				series:
				[
				 {
		     name: '正面',
		     type: 'bar',
		     stack: '总量',
		     label: {
		         normal: {
		             show: true,
		             position: 'insideRight'
		         }
		     },
		     data:
		     (
		        function()
		        {
		           var arrobj=new Array();
		           var sum=datt.totalNum.length;
		        
		           for(var i=0;i<sum;i++)
		           {
		              arrobj.push(datt.totalNum[i]-datt.negativeNum[i]);
		           }
		            return arrobj;
		        }
		        
		     )()
			 },
			 {
			     name: '负面',
			     type: 'bar',
			     stack: '总量',
			     label: {
			         normal: {
			             show: true,
			             position: 'insideRight'
			         }
			     },
			     data: datt.negativeNum
			 }
			
			]
			};
			             
			             
			             myChart.setOption(option);
			             Chart=myChart;
			
			         }
			     })
			     return Chart;
			}
		
		
		//一周走势图
		function WeeklyChart(loadurl,elementId, starttime,endtime,userId,method)
		{   
			//获取这一周的走势图
			
			var Chart;
			var endTime=setFormat(endtime);
		    
			var now = endtime;
		    var pre = endtime;
		    pre.setDate(now.getDate()-6);
		    
		    var startTime=setFormat(pre);
		    
		    var arrobj=new Array;
		    var temp=endtime;
		    for(var i=6;i>=0;i--)
		    {
		       pre.setDate(now.getDate()+1);
		       arrobj[i]=setFormat(temp);
		    }
		    arrobj.reverse();
		    
		    $.ajax({
		        type: "GET",
		        //url:"./servlet/IndexServlet",
		        
		        url:loadurl,
		        dataType:"json",
		        data : 
		        {
		            "startTime": startTime,
		            "endTime":  endTime,
		            "userId" : userId,
		            "method" : method,
		         },
		
		        success: function(datt) 
		        {
		
		            var myChart = echarts.init(document.getElementById(elementId));
		
		
		              option = {
		title: {
		text: '一周走势图',
		},
		tooltip: {
		trigger: 'axis'
		},
		legend: {
		data:['总数','负面']
		},
		toolbox: 
		{
		show: true,
		},
		xAxis:  {
		type: 'category',
		boundaryGap: false,
		data: arrobj
		},
		yAxis: {
		type: 'value',
		axisLabel: {
		    formatter: '{value} 条数'
		}
		},
		series: [
		{
		    name:'总数',
		    type:'line',
		    data:datt.series.all,
		    markPoint: 
		    {
		        data: 
		        [
		            {type: 'max', name: '最大值'},
		            {type: 'min', name: '最小值'}
		        ]
		    }
		},
		{
		    name:'负面',
		    type:'line',
		    data:datt.series.neg,
		    markPoint: 
		    {
		        data: 
		        [
		            {type: 'max', name: '最大值'},
		            {type: 'min', name: '最小值'}
		        ]
		    }
		},
		]
		};
		
		            myChart.setOption(option);
		            Chart=myChart;
		        }
		    })
			
			return Chart;
		}
		
		//媒体分析图，在情感分析页面
		function MediaAnalysisChart(loadurl,elementId, starttime, endtime,userId,method)
		{
			 var Chart;
			 $.ajax({
		         type: "GET",
		         url:loadurl,
		         dataType: "json",
		         data : 
				 {
						"start_time" : setFormat(starttime),
						"end_time" : setFormat(endtime),
					    "userId" : userId,
						"method" : method
				 },
		         success: function(datt) 
		         {
		
		             var myChart = echarts.init(document.getElementById(elementId));
		
		
						option = {
							tooltip : {
								trigger: 'axis',
								axisPointer : {            // 坐标轴指示器，坐标轴触发有效
									type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
								}
							},
							legend: {
								data: datt.lengdata //图例表示
							},
							grid: {
								left: '3%',
								right: '4%',
								bottom: '3%',
								containLabel: true
							},
							xAxis:  {
								type: 'value'
							},
							yAxis: {
								type: 'category',
								data: datt.xAxisdata   //纵坐标表示数据
							},
							series: [
								{
									name: datt.lengd1,   //图例1内容
									type: 'bar',
									stack: '总量',
									label: {
										normal: {
											show: true,
											position: 'insideRight'
										}
									},
									data: datt.lengd1data  //图例1对应数据
								},
								{
									name: datt.lengd2,
									type: 'bar',
									stack: '总量',
									label: {
										normal: {
											show: true,
											position: 'insideRight'
										}
									},
									data: datt.lengd2data
								},
								{
									name: datt.lengd3,
									type: 'bar',
									stack: '总量',
									label: {
										normal: {
											show: true,
											position: 'insideRight'
										}
									},
									data: datt.lengd3data
								},
								{
									name: datt.lengd4,
									type: 'bar',
									stack: '总量',
									label: {
										normal: {
											show: true,
											position: 'insideRight'
										}
									},
									data: datt.lengd4data
								},
								{
									name: datt.lengd5,
									type: 'bar',
									stack: '总量',
									label: {
										normal: {
											show: true,
											position: 'insideRight'
										}
									},
									data: datt.lengd5data
								}
							]
						};
		             
		             
		             myChart.setOption(option);
		             Chart=myChart;
		
		         }
		     })
		     return Chart;
		};
		
		//整体走势图
		function OverallTrendChart(loadurl,elementId, starttime, endtime,userId,method)
		{
			 var Chart;
			 $.ajax({
		         type: "GET",
		         //url:"json/otrendAnalysis.json",
		         url:loadurl,
		         dataType: "json",
		         data : 
				 {
						"start_time" : setFormat(starttime),
						"end_time" : setFormat(endtime),
					    "userId" : userId,
						"method" : method
				 },
		         success: function(datt) {
		         
		         //'otrendAnalysis'
		         var myChart = echarts.init(document.getElementById(elementId));
		
		            
				   option = {
						title: {
							text: datt.title,   //主标题
							subtext: datt.subtitle  //副标题
						},
						tooltip: {
							trigger: 'axis'
						},
						legend: {
							data:datt.lengddata, //图例内容
							bottom:10,
						},
						toolbox: {
							show: true,
							feature: {
								dataZoom: {
									yAxisIndex: 'none'
								},
								dataView: {readOnly: false},
								magicType: {type: ['line', 'bar']},
								restore: {},
								saveAsImage: {}
							},
							 orient:'vertical',
						},
						xAxis:  {
							type: 'category',
							boundaryGap: false,
							data: datt.xAxisdata   //横坐标数据
						},
						yAxis: {
							type: 'value',
							axisLabel: {
								formatter: '{value} °C'
							}
						},
						series: [
							{
								name:datt.heigh,        //图例名称
								type:'line',
								data:datt.heighdata,    //图例数据
						
							},
							{
								name:datt.low,
								type:'line',
								data:datt.lowdata,
							  
							}
							,{
								name:datt.none,
								type:'line',
								data:datt.nonedata,
							}
						]
					};
		
		
		             
		             
		             myChart.setOption(option);
		             Chart=myChart;
		
		         }
		     })
		     return Chart;
		}
		
		//信息敏感分类图
		function InformationSensitiveClassificationChart(loadurl,elementId, starttime, endtime,userId,method)
		{  
			
			var Chart;
			$.ajax({
		        type: "GET",
		        //url:"json/SensitiveInfo.json",
		        url:loadurl,
		        dataType: "json",
		        data : 
				 {
						"start_time" : setFormat(starttime),
						"end_time" : setFormat(endtime),
					    "userId" : userId,
						"method" : method
				 },
		
		        success: function(datt) {
		        
		        //'SensitiveInfo'
		        var myChart = echarts.init(document.getElementById(elementId));
		
		           
		    option = {
				tooltip: {
					trigger: 'item',
					formatter: "{a} <br/>{b}: {c} ({d}%)"
				},
				legend: {
					orient: 'vertical',
					x: 'left',
					data:datt.lengdata        //图例数据
				},
				series: [
				  
					{
						name:datt.hiddentip,    //隐藏的提示信息
						type:'pie',
						radius: ['40%', '60%'],
						data:datt.hiddendata    //隐藏图例数据
					} 
				]
			};
		            myChart.setOption(option);
		            Chart=myChart;
		
		        }
		    })	
		    return Chart;
		}
		
		//媒体负面趋势图
		function MediaNegativeTendencyChart(loadurl,elementId, starttime, endtime,userId,method)
		{
			
			var Chart;
			$.ajax({
		        type: "GET",
		        //url:"json/NegativeBias.json",
		        url:loadurl,
		        dataType: "json",
		        data : 
				 {
						"start_time" : setFormat(starttime),
						"end_time" : setFormat(endtime),
					    "userId" : userId,
						"method" : method
				 },
		        success: function(datt) 
		        {
		        //'NegativeBias'
		        var myChart = echarts.init(document.getElementById(elementId));
				
				option = {
						 
						  tooltip: {
							  trigger: 'axis'
						  },
						  legend: {
							  data:datt.lengdata    //图例数据
						  },
						  grid: {
							  left: '3%',
							  right: '4%',
							  bottom: '3%',
							  containLabel: true
						  },
						  toolbox: {
							  feature: {
								  saveAsImage: {}
							  }
						  },
						  xAxis: {
							  type: 'category',
							  boundaryGap: false,
							  data: datt.xAxisdata  //横坐标数据
						  },
						  yAxis: {
							  type: 'value'
						  },
						  series: [
							  {
								  name:datt.lengd1,     //图例名称
								  type:'line',
								  stack: '总量',       
								  data:datt.lengd1data   //图例数据
							  },
							  {
								  name:datt.lengd2,
								  type:'line',
								  stack: '总量',
								  data:datt.lengd2data
							  },
							  {
								  name:datt.lengd3,
								  type:'line',
								  stack: '总量',
								  data:datt.lengd3data
							  },
							  {
								  name:datt.lengd4,
								  type:'line',
								  stack: '总量',
								  data:datt.lengd4data
							  },
							  {
								  name:datt.lengd5,
								  type:'line',
								  stack: '总量',
								  data:datt.lengd5data
							  }
						  ]
					  };
		
		            
		            myChart.setOption(option);
		            Chart=myChart;
		        }
		    })
			return Chart;
		}
		
		//媒体来源
		function MediaSourcesChart(loadurl,elementId, starttime, endtime,userId,method)
		{   
			var Chart;
			$.ajax({
		        
		        //url : "./servlet/SiteEvaluationServlet",
				url:loadurl,
			    type : "POST",
				/*data : 
				{
					"start_time" : "2012-09-10",
					"end_time" : "2016-12-21",
				    "userId" : '1',
					"method" : 'getWebTend'
				},*/
		        data: 
				{
						"start_time" : setFormat(starttime),
						"end_time" : setFormat(endtime),
					    "userId" : userId,
						"method" : method
				},
		        success: function(datt) 
		        {
		        //'MTorigin'
		        var myChart = echarts.init(document.getElementById(elementId));
		
		option = 
		{
		title: {
		text: '南华大学舆情情况分布',     //没有改变，直接是servlet加载的
		subtext: '数据来自网络'
		},
		tooltip: {
		trigger: 'axis',
		axisPointer: {
		    type: 'shadow'
		}
		},
		legend: {
		data: ['褒', '中','贬']
		},
		grid: {
		left: '3%',
		right: '4%',
		bottom: '3%',
		containLabel: true
		},
		xAxis: 
		{
		type: 'value',
		
		},
		yAxis: {
		type: 'category',
		data:datt.results.xAxis
		},
		series: [
		{
		    name: datt.results.series[0].name,
		    type: 'bar',
		    data: datt.results.series[0].data
		},
		{
		    name: datt.results.series[1].name,
		    type: 'bar',
		    data: datt.results.series[1].data
		},
		{
		    name: datt.results.series[2].name,
		    type: 'bar',
		    data: datt.results.series[2].data
		}
		]
		};
		            myChart.setOption(option);
		            Chart=myChart;
		
		        }
		    })
		    return Chart;
		}
		
		//信息来源图
		function InformationSourcesChart(loadurl,elementId, starttime, endtime,userId,method)
		{    
			 var Chart;
			 $.ajax({
		         type: "GET",
		         //url:"json/MTcome.json",
		         url:loadurl,
		         dataType: "json",
		         data: 
		 		 {
		 				"start_time" : setFormat(starttime),
		 				"end_time" : setFormat(endtime),
		 			    "userId" : userId,
		 				"method" : method
		 		 },
		         success: function(datt) 
		         {
		         
		         //'MTcome'
		         var myChart = echarts.init(document.getElementById(elementId));
		        
		
		      option = {
						  title : {
							  text: datt.title,       //图标标题
							  subtext:datt.subtitle,   //图标二级标题
							  x:'center'
						  },
						  tooltip : {
							  trigger: 'item',
							  formatter: "{a} <br/>{b} : {c} ({d}%)"
						  },
						  legend: {
							  x : 'center',
							  y : 'bottom',
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
								  data:datt.data            //面积模式图标中的数据
							  }
						  ]
					  }; 
		           myChart.setOption(option);
		           Chart=myChart;
		
		         }
		     })	
		     return Chart;
		}
		//媒体活跃度图
		function MediaActivityChart(loadurl,elementId, starttime, endtime,userId,method)
		{
			
			var Chart;
			$.ajax({
		        type: "GET",
		        //url:"json/MTactive.json",
		        url:loadurl,
		        dataType: "json",
		        data: 
				{
						"start_time" : setFormat(starttime),
						"end_time" : setFormat(endtime),
					    "userId" : userId,
						"method" : method
				},
		        success: function(datt) 
		        {
		        //'MTactive
		        var myChart = echarts.init(document.getElementById(elementId));
		       
		
		            var option = {
									title: {
										text: datt.title
									},
									tooltip: {},
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
									legend: {
										data:datt.lengdname
									},
									xAxis: {
										data: datt.lengdata,
									},
									yAxis: {},
									series: [{
										name: datt.lengdname,
										type: 'bar',
										data: datt.lengdcount
									}]
								};
		
		          myChart.setOption(option);
		          Chart=myChart;
		        }
		    })	
		    return Chart;
		}
		
		//传播路径图d3.js
		
		function PropagationPathChart(loadurl,elementId, starttime, endtime,userId,method)
		{
			$.ajax({
		        type: "GET",
		        //url:"json/MTactive.json",
		        url:loadurl,
		        dataType: "json",
		        data: 
				{
						"start_time" : setFormat(starttime),
						"end_time" : setFormat(endtime),
					    "userId" : userId,
						"method" : method
				},
		        success: function(datt) 
		        {  
		        	var width = 500,height = 500;
		        	
		        	var tree = d3.layout.tree()
		        		.size([width, height-200])
		        		.separation(function(a, b) { return (a.parent == b.parent ? 1 : 2); });
		        	
		        	var diagonal = d3.svg.diagonal()
		        		.projection(function(d) { return [d.y, d.x]; });
		        	
		        	var svg = d3.select("#"+elementId).select("svg")
		        		.attr("width", width)
		        		.attr("height", height)
		        		.append("g")
		        		.attr("transform", "translate(40,0)");
		 
		        		
		        	    root=datt;
		        		var nodes = tree.nodes(root);
		        		var links = tree.links(nodes);
		        		
		        		
		        		var link = svg.selectAll(".link")
		        		  .data(links)
		        		  .enter()
		        		  .append("path")
		        		  .attr("class", "link")
		        		  .attr("d", diagonal);
		        		
		        		var node = svg.selectAll(".node")
		        		  .data(nodes)
		        		  .enter()
		        		  .append("g")
		        		  .attr("class", "node")
		        		  .attr("transform", function(d) { return "translate(" + d.y + "," + d.x + ")"; })
		        		
		        		node.append("circle")
		        		  .attr("r", 4.5);
		        		
		        		node.append("text")
		        		  .attr("dx", function(d) { return d.children ? -8 : 8; })
		        		  .attr("dy", 3)
		        		  .style("text-anchor", function(d) { return d.children ? "end" : "start"; })
		        		  .text(function(d) { return d.name; });
		  
		        }
		    })
			
		};
		
		//舆情热点图
		
		function HotPublicOpinion(loadurl,elementId, starttime, endtime,userId,method)
		{
			var Chart;
			$.ajax({
		           //url : "./servlet/HotWordsServlet",
				   url:loadurl,
				   type : "POST",
				   data : 
				   {
								"startTime" : setFormat(starttime),
								"endTime" : setFormat(endtime),
								// "startTime" : '2012-09-10',
								// "endTime" : '2013-01-10',
								// 默认先给1，后期需要自动获得
								"userId" : userId,
								"method" : method
					},
		         dataType: "json",
		         success: function(datt) 
		         {
		         
		         
		         var myChart = echarts.init(document.getElementById(elementId));
		var graph = {};//数据
		graph.nodes = datt.results.nodes;
		
		
		//以下为连线关系数据，每一个{}里面为一个关系，source（起点，对应上面的name），target（终点，对应上面的name）
		//value(起点到终点的距离，值越大，权重越大，距离越短)
		for(var i=0;i<datt.results.links.length;i++)
		{
		    datt.results.links[i].value=datt.results.links[i].weight;
		}
		graph.links = datt.results.links;
		
		//categories为node节点分类，categoriesshort为显示图例，后者比前者短，可以使得图例中没有主干人物
		graph.categories = [{name:'褒'},{name:'中'},{name:'正'} ];
		
		// 设置关系图节点标记的大小
		graph.nodes.forEach(function (node) 
		{
		 node.symbolSize = node.value*3;
		});
		var option = {
		title: {
		 text: '舆情热点',//标题
		 subtext: '舆情热点关系图',//标题副标题
		 top: 'top',//相对在y轴上的位置 
		 left: 'center'//相对在x轴上的位置
		},
		tooltip : {//提示框，鼠标悬浮交互时的信息提示
		 trigger: 'item',//数据触发类型
		 formatter: function(params){//触发之后返回的参数，这个函数是关键
		  
		   if (params.data.category !=undefined) {//如果触发节点
		     return '关键词:'+params.data.name;//返回标签
		   }else {//如果触发边
		     
		     return params.data.source+"->"+params.data.target;
		   }
		 },
		},
		//工具箱，每个图表最多仅有一个工具箱
		toolbox: {
		 show : true,
		 feature : {//启用功能
		   //dataView数据视图，打开数据视图，可设置更多属性,readOnly 默认数据视图为只读(即值为true)，可指定readOnly为false打开编辑功能
		   dataView: {show: true, readOnly: true},
		   restore : {show: true},//restore，还原，复位原始图表
		   saveAsImage : {show: true}//saveAsImage，保存图片
		 }
		},
		//全局颜色，图例、节点、边的颜色都是从这里取，按照之前划分的种类依序选取
		color:['rgb(194,53,49)','rgb(178,144,137)','rgb(97,160,168)'],
		//图例，每个图表最多仅有一个图例
		legend: [{
		 x: 'left',//图例位置
		
		}],
		//sereis的数据: 用于设置图表数据之用
		series : 
		[
		 {
		   name: '舆情热点',//系列名称
		   type: 'graph',//图表类型
		   layout: 'force',//echarts3的变化，force是力向图，circular是和弦图
		   draggable: true,//指示节点是否可以拖动
		   data: graph.nodes,//节点数据
		   links: graph.links,//边、联系数据
		   categories: graph.categories,//节点种类
		   focusNodeAdjacency:true,//当鼠标移动到节点上，突出显示节点以及节点的边和邻接节点
		   roam: true,//是否开启鼠标缩放和平移漫游。默认不开启。如果只想要开启缩放或者平移，可以设置成 'scale' 或者 'move'。设置成 true 为都开启
		   label: 
		   {//图形上的文本标签，可用于说明图形的一些数据信息
		     normal: 
		     {
		       show : true,//显示
		       position: 'right',//相对于节点标签的位置
		       //回调函数，你期望节点标签上显示什么
		       formatter: function(params){
		         return params.data.name;
		       },
		     }
		   },
		   //节点的style
		   itemStyle:{
		     normal:{
		       opacity:0.9,//设置透明度为0.8，为0时不绘制
		     },
		   },
		   // 关系边的公用线条样式
		   lineStyle: {
		     normal: {
		       show : true,
		       color: 'target',//决定边的颜色是与起点相同还是与终点相同
		       curveness: 0.3//边的曲度，支持从 0 到 1 的值，值越大曲度越大。
		     }
		   },
		   force: {
		     edgeLength: [100,200],//线的长度，这个距离也会受 repulsion，支持设置成数组表达边长的范围
		     repulsion: 100//节点之间的斥力因子。值越大则斥力越大
		   }
		 }
		]
		};
		
		
		           myChart.setOption(option);
		           Chart=myChart;
		         }
		     })
		
		      return Chart;
		}
		    
