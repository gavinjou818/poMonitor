<%@page import="java.text.SimpleDateFormat"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">

<title>南华大学核电舆情系统</title>
<link href="css/PseudoPublicOpinionAnalysis.css" rel="stylesheet" type="text/css" />
<link href="css/bootstrap.css" rel="stylesheet" type="text/css" />

<link rel="stylesheet" type="text/css" href="./css/master.css">
<script src="js/jquery-3.1.1.js"></script>
<script src="js/echarts.js"></script>
<script type="text/javascript" src="./js/master.js"></script>
<script src="js/bootstrap.js"></script>
<script src="js/examples.js"></script>
<script type="text/javascript" src="./js/smoothscroll.js"></script>
<script type="text/javascript" src="./js/briefingChart.js"></script>


<!-- 预加载图片 -->
<script type="text/javascript">
 

    function preload()
    {
         
        initData(1,'json/test_data.json','PublicOpinionEvents');
        <%
            if (session.getAttribute("startTime") == null) {
				Calendar cal = Calendar.getInstance();
				Date date = cal.getTime();
				SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
				session.setAttribute("startTime", sf.format(date));
			}
			if (session.getAttribute("endTime") == null) {

				Calendar cal = Calendar.getInstance();
				Date date = cal.getTime();
				SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
				session.setAttribute("endTime", sf.format(date));
		}%>

         var startTime=document.getElementById('startTime').value='<%=session.getAttribute("startTime")%>';
         var endTime=document.getElementById('endTime').value='<%=session.getAttribute("endTime")%>';
         var userId='<%=session.getAttribute("userId")%>';
         getChart(startTime,endTime,userId);
       

    }
 


    function setTimeOpera()
    {

                    var data=("startTime="+document.getElementById('startTime').value);
                    data+=("&endTime="+document.getElementById('endTime').value);
                    data+=("&method="+"setTimeInteraction");
                    var xmlhttp;
                    if (window.XMLHttpRequest)
                    {
                        // code for IE7+, Firefox, Chrome, Opera, Safari
                        xmlhttp = new XMLHttpRequest();
                    }
                    else
                    {   // code for IE6, IE5
                        xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
                    }
                    xmlhttp.open("POST","<%=path%>/servlet/BasicInteractionServlet",true);
                    xmlhttp.setRequestHeader("Content-type","application/x-www-form-urlencoded");
                    xmlhttp.onreadystatechange = function()
                    {
                        if (xmlhttp.readyState == 4 && xmlhttp.status == 200)
                        {

                        }
                    };
                    xmlhttp.send(data);

      }
</script>

<!--加载数据表-->
<script type="text/javascript">
	    var operate=["查看","导出","邮箱发送"];
        var sum=5;//最大页数5;
        var barsum=10;//最大条目数2;
        var pagesum;//总共有的页数
        var curpage=1;//当前页


        function initData(start,dataroot,tparent)
        {

            curpage=start;
            $.ajax
            ({
                type: "GET",
                dataType: "json",
                url: dataroot,
                data:{"curpage":curpage},
                success: function (data)
                {

                    //获取ul节点
                    pagesum=parseInt(data[0].sum/barsum);
                    pagesum+=(data[0].sum%barsum?1:0);
                    var parent=document.getElementById(tparent);//获取对应模块
                    parent=parent.getElementsByTagName("ul");
                    parent=parent[0];


                    //清除节点
                    while(parent.hasChildNodes())parent.removeChild(parent.firstChild);
                    //先清除所有节点


                    // 加入<<节点
                    var son1=document.createElement("li");
                    var son2=document.createElement("a");
                    son2.setAttribute("href","#");
                    son2.innerHTML="&laquo;";
                    if((pagesum-curpage+1)<sum)
                    {
                        start=(pagesum-sum+1);
                        if(start<=0){start=1;}
                    }

                    //确定开始标号范围.
                    if(start-1>0)
                    {
                        son2.addEventListener("click",function () {
                            initData(start-1,dataroot,tparent);
                        });
                    }
                    son1.appendChild(son2);
                    parent.appendChild(son1);

                    var i;
                    for(i=start,DEF=0;DEF<sum&&i<=pagesum;i++,DEF++)//创建导航条,DEF为计数器
                    {
                        var son1=document.createElement("li");
                        var son2=document.createElement("a");
                        son2.setAttribute("href","#");
                        son2.innerText=i;
                        son2.addEventListener("click",function ()
                        {
                            initData(this.innerText,dataroot,tparent);
                        });
                        son1.appendChild(son2);
                        parent.appendChild(son1);
                    }

                    // 加入>>号
                    son1=document.createElement("li");
                    son2=document.createElement("a");
                    son2.setAttribute("href","#");
                    son2.innerHTML="&raquo;";

                    if(i<=pagesum)
                    {
                        son2.addEventListener("click",function ()
                        {
                            initData(i,dataroot,tparent);
                        });
                    }

                    //组装
                    son1.appendChild(son2);
                    parent.appendChild(son1);




                    //下面是加载数据


                    var parent2=document.getElementById(tparent);
                    var parent2=parent2.getElementsByTagName("tbody");
                    parent2=parent2[0];

                    while(parent2.hasChildNodes())parent2.removeChild(parent2.firstChild);

                    //加载每页上的数据
                    for(i=barsum*(curpage-1)+1,DEF=0;DEF<barsum&&i<=data[0].now;i++,DEF++)
                    {
                        var tr=document.createElement("tr");

                        //加载id
                        var td1=document.createElement("td");
                        var td1input=document.createElement("input");
                        td1input.setAttribute("type","checkbox");
                        td1input.setAttribute("name",tparent+"cb");
                        td1input.setAttribute("value",data[i].id);
                        td1.appendChild(td1input);
                        td1.innerHTML+=data[i].id;
                        tr.appendChild(td1);

                        //加载时间
                        var td2=document.createElement("td");
                        td2.innerText=data[i].time;
                        tr.appendChild(td2);

                        //加标题
                        var td3=document.createElement("td");
                        var td3a=document.createElement("a");
                        td3a.innerText=data[i].name;
                        td3a.setAttribute("href","#");
                        td3.appendChild(td3a);
                        tr.appendChild(td3);

                        //加载链接查看与信息
                        var td4=document.createElement("td");
                        for(var j=0;j<1;j++)
                        {
                            if(j!=0)
                            {
                                var textnode=document.createTextNode("|");
                                td4.appendChild(textnode);
                            }
                            var a1=document.createElement("a");
                            a1.setAttribute("href","#");
                            a1.innerHTML=operate[j];
                            td4.appendChild(a1);
                        }
                        tr.appendChild(td4);
                        parent2.appendChild(tr);
                    }


                }
            });

        }
        
         //全选事件
        function allseleck(papername)
        {

            var parent=document.getElementById(papername);
            parent=parent.getElementsByTagName("table");
            parent=parent[0];
            parent=parent.getElementsByTagName("tbody");
            parent=parent[0];
            parent=parent.getElementsByTagName("tr");


            for(var i=0;i<parent.length;i++)
            {
                /*var son=parent[i].getElementsByTagName("td");
                 son=son[0];*/
                var son=parent[i].getElementsByTagName("input");
                son=son[0];
                son.checked=true;
            }
        }
</script>




</head>
<body onload="preload()">



    <jsp:include page="master.jsp"  flush="true"/>

    <div id="cansetTime">
		<div class="cansetTimeR">
			<span>从</span><input type="date" id="startTime" name="startTime" />
		</div>
		<div class="cansetTimeR">
			<span>到</span><input type="date" id="endTime" name="endTime" />
		</div>
		<button type="button" class="btn btn-primary .btn-sm"
			style="float: right;margin-right: 5px" onclick="setTimeOpera()">确定</button>
	</div>

	<div id="Catalog">
		<div style="text-align: left">
			<img src="./image/barleft.png"
				style="width: 25px;height:25px;cursor: pointer;" title="时间设置显示"
				onclick="changeCatalog()">
		</div>
		<div>
			<a class="smoothScroll" href="<%=basePath%>PseudoPublicOpinionAnalysis.jsp#head1" title="舆情事件">舆情事件</a><br/>
			<a class="smoothScroll" href="<%=basePath%>PseudoPublicOpinionAnalysis.jsp#head2" title="舆情事件真伪标性分布">舆情事件真伪标性分布</a>
			<br/>
			<a class="smoothScroll" href="<%=basePath%>PseudoPublicOpinionAnalysis.jsp#head3" title="伪舆情事件媒体分布">伪舆情事件媒体分布</a>
		</div>
	</div>

	<!--主体-->
	<div id="rightbody">
		<div class="container">
			<div class="row">


				<!-- 舆情事件 -->
		    <div class="Ancestor" id="head1">
                <h1 class="h1Title">舆情事件 </h1>

                <!--Tab panes-->

                <div class="tab-content">
                    <!--事件报表-->
                    <div role="tabpanel" class="tab-pane active" id="PublicOpinionEvents" style="padding-top:10px" >
                        <table class="table table-hover " >
                            <thead>
                            <th style="width:10%;">编号</th>
                            <th style="width:20%;">时间</th>
                            <th style="width:50%">事件标题</th>
                            <th style="width:20%;">操作</th>
                            </thead>
                            <tbody>



                            </tbody>
                        </table>

                        <ul class="pagination pagsize">


                        </ul>
                        <div style="float: right">
                            <button type="button" class="btn btn-default" onclick="allseleck('PublicOpinionEvents')">全选</button>
                            <button type="button" class="btn btn-default">邮件发送</button>
                            <button type="button" class="btn btn-default">导出</button>
                        </div>


                    </div>
                </div>

                <div style="clear: both"></div>
            </div>


	            <!-- 舆情事件真伪标性分布-->
				<div class="Ancestor" id="head2">
					
					<h1 class="h1Title">舆情事件真伪标性分布</h1>
					<div class="Father" style="z-index: 98">
						<div id="AuthenticityStandard" style="width: 800px;height:600px;margin: 0 auto"></div>
						<div class="FatherDown">
							<img src="./image/down.png"
								style="width: 25px;height: 25px;cursor: pointer"
								onclick="changeSon('AuthenticityStandardSon')">
						</div>
					</div>

					<div class="Son" id="AuthenticityStandardSon" style="z-index: 97">
						<div class="SonTitle">简介:</div>
						<div class="SonContext">aaa</div>
					</div>

				</div>


	            <!-- 伪舆情事件媒体分布 -->
				<div class="Ancestor" id="head3">
					<h1 class="h1Title">伪舆情事件媒体分布</h1>
					<div class="Father" style="z-index: 96">
						<div id="PseudoPublicOpinionDistribution" style="width: 800px;height:600px;margin: 0 auto"></div>
						<div class="FatherDown">
							<img src="./image/down.png"
								style="width: 25px;height: 25px;cursor: pointer"
								onclick="changeSon('PseudoPublicOpinionDistributionSon')">
						</div>
					</div>

					<div class="Son" id="PseudoPublicOpinionDistributionSon" style="z-index: 95">
						<div class="SonTitle">简介:</div>
						<div class="SonContext">aaa</div>
					</div>
				</div>


			</div>
		</div>
	</div>


	<!-- foot -->

	<script type="text/javascript">
    function getChart(startTime,endTime,userId)
    {
            //舆情事件真伪标性分布
            AuthenticityStandardChart("json/MediaAnalysis.json",'AuthenticityStandard',startTime,endTime,userId,null);
            //伪舆情事件媒体分布
            PseudoPublicOpinionDistributionChart("json/otrendAnalysis.json",'PseudoPublicOpinionDistribution',startTime,endTime,userId,null);
    }
    
    function AuthenticityStandardChart(loadurl, elementId, startTime, endTime,userId, method) 
    {

	var Chart;
	$.ajax
	({
		type : "GET",
		url : loadurl,
		dataType : "json",
		data : {
			"startTime" : startTime,
			"endTime" : endTime,
			"userId" : userId,
			"method" : method
		},

		success : function(datt) {
			var myChart = echarts.init(document.getElementById(elementId));

			option = {
    title : {
        text: '舆情事件真伪标性分布',
        
        x:'center'
    },
    tooltip : {
        trigger: 'item',
        formatter: "{a} <br/>{b} : {c} ({d}%)"
    },
    legend: 
    {
        x : 'center',
        y : 'bottom',
        data:['真实舆情','伪舆情']
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
        }
    },
    calculable : true,
    series : [
    
        {
            name:'面积模式',
            type:'pie',
            radius : [30, 110],
            center : ['50%', '50%'],
            roseType : 'area',
            data:[
                {value:10, name:'真实舆情'},
                {value:5, name:'伪舆情'},
           
            ]
        }
    ]
};

			myChart.setOption(option);
			Chart = myChart;

		}
	})
	return Chart;
}

function PseudoPublicOpinionDistributionChart(loadurl, elementId, startTime, endTime,userId, method) 
{

	var Chart;
	$.ajax
	({
		type : "GET",
		url : loadurl,
		dataType : "json",
		data : {
			"startTime" : startTime,
			"endTime" : endTime,
			"userId" : userId,
			"method" : method
		},

		success : function(datt) 
		{
	
	  var myChart = echarts.init(document.getElementById(elementId));
	  option = 
	  {
            title: {
                text: '伪舆情事件媒体分析'
            },
            tooltip: {},
            legend: {
                data:['百分比']
            },
            xAxis: {
                data: ["I类媒体","II类媒体","III类媒体"]
            },
            yAxis: 
            {
            type: 'value',
            name: '百分比',
    
            axisLabel: {
                formatter: '{value} %'
            }
            },
            series: 
            [{
                name: '百分比',
                type: 'bar',
                data: [20, 40, 40],
                label: {
                 normal: {
                show: true,
                position: 'outside',
                 }
                }
            }]
         };

			myChart.setOption(option);
			Chart = myChart;

		}
	})
	return Chart;
}
</script>



</body>
</html>
