<%@page import="java.text.SimpleDateFormat"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
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

<link href="css/bootstrap.css" rel="stylesheet" type="text/css" />
<link href="css/bootstrap-theme.css" rel="stylesheet" type="text/css" />
<script src="js/jquery-3.1.1.js"></script>
<script src="js/echarts.js"></script>
<script type="text/javascript" src="./js/YQCHART.js"></script>

<script src="js/bootstrap.js"></script>
<script src="js/d3.js"></script>

<link href="./css/MTFX.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css" href="./css/master.css">
<script type="text/javascript" src="./js/master.js"></script>
<script type="text/javascript" src="./js/smoothscroll.js"></script>
<script src="js/tagcloud.js"></script>

<style type="text/css">
.node circle {
	fill: #fff;
	stroke: steelblue;
	stroke-width: 1.5px;
}

.node {
	font: 12px sans-serif;
}

.link {
	fill: none;
	stroke: #ccc;
	stroke-width: 1.5px;
}
</style>
<script>


        var ballstate=false;
        function changeballstate()
        {
            if(ballstate==true)
            {
                $("#CloudBall").animate({right:"-340px"},500);
                ballstate=false;
            }
            else
            {
                $("#CloudBall").animate({right:"0px"},500);
                ballstate=true;
            }
        }
        
        function preload()
        {
        
            <%if (session.getAttribute("startTime") == null) {
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
                            location.reload();
                        }  
                    };
                    xmlhttp.send(data);  
            
        }
</script>

</head>

<body onload="preload()">

	<jsp:include page="master.jsp" flush="true" />

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

	<!-- 右边的列表牌子-->
	<div id="Catalog">
		<div style="text-align: left">
			<img src="./image/barleft.png"
				style="width: 25px;height:25px;cursor: pointer;" title="时间设置显示"
				onclick="changeCatalog()">
		</div>
		<div>
			<a class="smoothScroll" href="<%=basePath%>MTFX.jsp#head1"
				title="媒体来源情况">媒体来源情况</a>
		</div>
		<div>
			<a class="smoothScroll" href="<%=basePath%>MTFX.jsp#head2"
				title="信息来源类型">信息来源类型</a>
		</div>
		<div>
			<a class="smoothScroll" href="<%=basePath%>MTFX.jsp#head3"
				title="媒体活跃度">媒体活跃度</a>
		</div>
		<div>
			<a class="smoothScroll" href="<%=basePath%>MTFX.jsp#head4"
				title="热点关系图">热点关系图</a>
		</div>
		<div>
			<a class="smoothScroll" href="<%=basePath%>MTFX.jsp#head5"
				title="传播路径图">传播路径图</a>
		</div>
	</div>

	<!-- 词云球 -->
	<div id="CloudBall">
		<div style="text-align: left">
			<img src="./image/barleft.png"
				style="width: 25px;height:25px;cursor: pointer;" title="词云球"
				onclick="changeballstate()">词云球
		</div>
		<div id="Cloud" style="width: 400px;height: 400px"></div>
		<div style="clear: both"></div>
	</div>

	<div id="rightbody">

		<!--主体-->
		<div class="container">
			<div class="row">


				<div class="Ancestor" id="head1">
					<h1 class="h1Title">媒体来源情况</h1>
					<div class="Father" style="z-index: 100">
						<div id="MTorigin"
							style="width: 800px;height:600px;margin: 0 auto"></div>
						<div class="FatherDown">
							<img src="./image/down.png"
								style="width: 25px;height: 25px;cursor: pointer"
								onclick="changeSon('MToriginson')">
						</div>
					</div>

					<div class="Son" id="MToriginson" style="z-index: 99">
						<div class="SonTitle">简介:</div>
						<div class="SonContext">aaa</div>
					</div>

				</div>


				<div class="Ancestor" id="head2">
					<h1 class="h1Title">信息来源类型</h1>
					<div class="Father" style="z-index: 98">
						<div id="MTcome" style="width: 800px;height:600px;margin: 0 auto"></div>
						<div class="FatherDown">
							<img src="./image/down.png"
								style="width: 25px;height: 25px;cursor: pointer"
								onclick="changeSon('MTcomeson')">
						</div>
					</div>

					<div class="Son" id="MTcomeson" style="z-index: 97">
						<div class="SonTitle">简介:</div>
						<div class="SonContext">aaa</div>
					</div>

				</div>


				<div class="Ancestor" id="head3">
					<h1 class="h1Title">媒体活跃度</h1>
					<div class="Father" style="z-index: 96">
						<div id="MTactive"
							style="width: 800px;height:600px;margin: 0 auto"></div>
						<div class="FatherDown">
							<img src="./image/down.png"
								style="width: 25px;height: 25px;cursor: pointer"
								onclick="changeSon('MTactiveson')">
						</div>
					</div>

					<div class="Son" id="MTactiveson" style="z-index: 95">
						<div class="SonTitle">简介:</div>
						<div class="SonContext">aaa</div>
					</div>
				</div>



				<div class="Ancestor" id="head4">
					<h1 class="h1Title">热点关系图</h1>
					<div class="Father" style="z-index: 94">
						<div id="hotpoint"
							style="width: 800px;height:600px;margin: 0 auto"></div>
						<div class="FatherDown">
							<img src="./image/down.png"
								style="width: 25px;height: 25px;cursor: pointer"
								onclick="changeSon('hotpointson')">
						</div>
					</div>

					<div class="Son" id="hotpointson" style="z-index: 93">
						<div class="SonTitle">简介:</div>
						<div class="SonContext">aaa</div>
					</div>
				</div>


				<div class="Ancestor" id="head5">
					<h1 class="h1Title">传播路径图</h1>
					<div class="Father" style="z-index: 92">
						<div id="Transpath"
							style="width:800px;height:600px;margin:0 auto;">
							<svg style="width:800px;height:600px;"></svg>
						</div>
						<div class="FatherDown">
							<img src="./image/down.png"
								style="width: 25px;height: 25px;cursor: pointer"
								onclick="changeSon('Transpathson')">
						</div>
					</div>

					<div class="Son" id="Transpathson" style="z-index: 91">
						<div class="SonTitle">简介:</div>
						<div class="SonContext">aaa</div>
					</div>
				</div>

			</div>


		</div>
	</div>




	<!--foot-->


	<script type="text/javascript">
		        
		        
		        
				function getChart(startTime,endTime,userId)
				{
					//媒体来源图
					
					
					MediaSourcesChart("./servlet/SiteEvaluationServlet",
							'MTorigin', startTime, endTime, userId,
							'getWebTend');
					//测试专用
					//MediaSourcesChart("json/MTorigin.json",'MTorigin',"2012-09-10","2016-12-21",'1','getWebTend');
					
					//信息来源类型
					InformationSourcesChart("json/MTcome.json", "MTcome",
							startTime, endTime, userId, null);
					//媒体活跃度
					MediaActivityChart("json/MTactive.json", 'MTactive',
							startTime, endTime, userId, null);
					//传播路径图
					PropagationPathChart("json/city_tree.json", 'Transpath',
							startTime, endTime, userId, null);
					//舆情热点  数据返回仍然有问题
					HotPublicOpinion("./servlet/HotWordsServlet", 'hotpoint',
							startTime, endTime, userId, 'getHotWords');
					
					//测试专用		
					
					// HotPublicOpinion("json/hotpoint.json",'hotpoint','2012-09-10','2013-01-10','1','getHotWords');

				};
	</script>



</body>
</html>
