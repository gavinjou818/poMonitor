<%@ page import="java.text.SimpleDateFormat"%>
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

<link href="css/HomePage.css" rel="stylesheet" type="text/css" />
<link href="css/bootstrap.css" rel="stylesheet" type="text/css" />
<link href="css/bootstrap-theme.css" rel="stylesheet" type="text/css" />
<!--在线jq-->
<script src="js/jquery-3.1.1.js"></script>
<script src="js/bootstrap.js"></script>
<script src="js/echarts.js"></script>
<script src="js/china.js"></script>

<script type="text/javascript" src="./js/YQCHART.js"></script>
<link rel="stylesheet" type="text/css" href="./css/master.css">
<script type="text/javascript" src="./js/master.js"></script>
<script type="text/javascript" src="./js/smoothscroll.js"></script>



<!--加载数据表-->
<script type="text/javascript">

        var sum = 13;//限制最大条数

        var starttime = new Date();
        var endttime = new Date();

        //图片指向
        var imgsrc = ["image/nev.jpg", "image/cen.jpg", "image/pos.jpg"];

        function preload()
        {

             //设置开始时间
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
			}
			%>

			var startTime='<%=session.getAttribute("startTime")%>';
			var endTime='<%=session.getAttribute("endTime")%>';
            //记录登录id
			var userId='<%=session.getAttribute("userId")%>';


            document.getElementById('startTime').value=startTime;
            document.getElementById('endTime').value=endTime;


             //最新舆情加载
            getLatestMessage(startTime,endTime);
             //热点舆情加载
            getPointMessage(startTime,endTime);
             //数据图表加载
            getChart(startTime,endTime,userId);

         }

         //设置时间提交
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

	<div id="Catalog">
		<div style="text-align: left">
			<img src="./image/barleft.png"
				style="width: 25px;height:25px;cursor: pointer;" title="时间设置显示"
				onclick="changeCatalog()">
		</div>
		<div>
			<a class="smoothScroll" href="<%=basePath%>HomePage.jsp#head1" title="最新舆情">最新舆情</a>
		</div>
		<div>
			<a class="smoothScroll" href="<%=basePath%>HomePage.jsp#head2"
				title="舆情走势">舆情走势</a>
		</div>
		<div>
			<a class="smoothScroll" href="<%=basePath%>HomePage.jsp#head3"
				title="全国舆情情况">全国舆情情况</a>
		</div>
		<div>
			<a class="smoothScroll" href="<%=basePath%>HomePage.jsp#head4"
				title="媒体倾向度分析">媒体倾向度分析</a>
		</div>

	</div>


        <!--主体-->
	<div id="rightbody">
		<div class="container">
			<div class="row">


        <!--最新舆情-->
				<div class="Ancestor" id="head1">
					<h1 class="h1Title">
						最新舆情 <a href="AllPublicOpinion.jsp" style="float: right;font-size: 15px">more<span
							class="glyphicon glyphicon-chevron-right"></span></a>
					</h1>
					<div class="col-md-6 col-xs-6 col-lg-6">
						<h2 class="h2Title">最新舆情</h2>
						<div id="newyq"></div>
					</div>
					<div class="col-md-6 col-xs-6 col-lg-6">
						<h2 class="h2Title">热点舆情</h2>
						<div id="pointyq"></div>
					</div>
					<div style="clear: both"></div>
				</div>


        <!--一周舆情走势-->
				<div class="Ancestor" id="head2">
					<h1 class="h1Title">一周走势情况</h1>
					<div class="Father" style="z-index: 100">
						<div id="yqto" style="width: 800px;height:600px;margin: 0 auto"></div>
						<div class="FatherDown">
							<img src="./image/down.png"
								style="width: 25px;height: 25px;cursor: pointer"
								onclick="changeSon('yqtoson')">
						</div>
					</div>

					<div class="Son" id="yqtoson" style="z-index: 99">
						<div class="SonTitle">简介:</div>
						<div class="SonContext">aaa</div>
					</div>

				</div>

        <!--全国舆情情况-->
				<div class="Ancestor" id="head3">

					<h1 class="h1Title">全国舆情情况</h1>
					<div class="Father" style="z-index: 98">
						<div id="ChinaPiture"
							style="width: 800px;height:600px;margin: 0 auto"></div>
						<div class="FatherDown">
							<img src="./image/down.png"
								style="width: 25px;height: 25px;cursor: pointer"
								onclick="changeSon('ChinaPitureson')">
						</div>
					</div>
					<div class="Son" id="ChinaPitureson" style="z-index: 97">
						<div class="SonTitle">简介:</div>
						<div class="SonContext">aaa</div>
					</div>
					<div style="clear: both"></div>

				</div>

              <!--媒体倾向度分析-->
				<div class="Ancestor" id="head4">

					<h1 class="h1Title"><%=session.getAttribute("endTime") %>&nbsp;&nbsp;媒体倾向度分析</h1>
					<div class="Father" style="z-index: 96">
						<div id="medtendency"
							style="width: 800px;height:600px;margin: 0 auto"></div>
						<div class="FatherDown">
							<img src="./image/down.png"
								style="width: 25px;height: 25px;cursor: pointer"
								onclick="changeSon('medtendencyson')">
						</div>
					</div>
					<div class="Son" id="medtendencyson" style="z-index: 95">
						<div class="SonTitle">简介:</div>
						<div class="SonContext">aaa</div>
					</div>
					<div style="clear: both"></div>

				</div>

			</div>


		</div>


	</div>


	<!--foot-->


	<script type="text/javascript">
    function getLatestMessage(startTime,endTime)
    {
       //最新舆情加载
        $.ajax
         ({
                type: "GET",
                dataType: "json",
                url: "./servlet/IndexServlet",
                data :
				{
					"startTime" : startTime,
					"endTime" : endTime,
				    "userId" : 1,
					"method" : 'getLatestMessage'
				},
                success: function (data)
                {
                    var limit = Math.min(data.length, sum);


                    var parent = document.getElementById('newyq');
                    while (parent.hasChildNodes())parent.removeChild(parent.firstChild);
                    for (var i = 0; i < limit; i++) {
                        //创建div节点
                        var div1 = document.createElement("div");
                        //创建内嵌节点
                        var span1 = document.createElement("span");

                        //创建链接节点
                        var a1 = document.createElement("a");
                        a1.setAttribute("href", data[i].url);
                        a1.innerText = data[i].title;

                        //创建时间节点
                        var span2 = document.createElement("span");
                        span2.setAttribute("class", "Time");
                        span2.innerText = data[i].time;

                        //组装
                        //span1.appendChild(img1);

                        div1.appendChild(span1);
                        div1.appendChild(a1);
                        div1.appendChild(span2);

                        parent.appendChild(div1);
                    }


                }
            });

    };

    function getPointMessage(startTime,endTime)
    {

      $.ajax
      ({
                type: "GET",
                dataType: "json",
                url: "json/pointyq.json",

                data:
                {
                    "starttime": startTime,
                    "endtime": endTime
                },
                success: function (data)
                {
                    var limit = Math.min(data.length, sum);

                    var parent = document.getElementById('pointyq');
                    while (parent.hasChildNodes())parent.removeChild(parent.firstChild);
                    for (var i = 0; i < limit; i++) {
                        //创建div节点
                        var div1 = document.createElement("div");

                        //创建链接节点
                        var a1 = document.createElement("a");
                        a1.setAttribute("href", data[i].alink);
                        a1.innerText = data[i].title;

                        //创建时间节点
                        var span2 = document.createElement("span");
                        span2.setAttribute("class", "Time");
                        span2.innerText = data[i].time;

                        //组装
                        div1.appendChild(a1);
                        div1.appendChild(span2);
                        parent.appendChild(div1);
                    }
                }
            });

    }

    function getChart(startTime,endTime,userId)
    {

        //地域分布图
        ChinaPictureChart("./json/ChinaPiture.json",'ChinaPiture',startTime,endTime,userId,null);
        //今日舆情图
        PublicOpinionTodayChart('./servlet/IndexServlet','medtendency',startTime,endTime,userId,'checkStatus');
        //测试用
        //PublicOpinionTodayChart('./json/medtendency.json','medtendency',new Date(),new Date(),null,'checkStatus');
        //一周走势图
         WeeklyChart("./servlet/IndexServlet",'yqto',startTime,endTime,userId,'getTendency');
        //测试用
        //WeeklyChart("./json/yqto.json",'yqto',new Date(),new Date(),null,'getTendency');

    }


</script>



</body>
</html>
