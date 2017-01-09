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

    <link rel="stylesheet" type="text/css" href="./css/bootstrap.css">
    <script type="text/javascript" src="./js/jquery-3.1.1.js"></script>
    <link rel="stylesheet" type="text/css" href="./css/master.css">
    <script type="text/javascript" src="./js/master.js"></script>


    <script type="text/javascript">



      $(document).ready(function(){
            $("#flip1").click(function(){
                $("#Menu_panel1").slideToggle("slow");
            });

             $("#flip2").click(function(){
                $("#Menu_panel2").slideToggle("slow");
            });
        });

        var barstate=false;
        var state=false;


       function changeTIME()
       {
            if(state==true)
            {
                $("#cansetTime").animate({left:"-220px"},500);
                state=false;
            }
            else
            {
                 if(barstate==true)
                 {
                     $("#cansetTime").animate({left: "250px"}, 500);
                     state = true;
                 }
                 else
                 {
                     $("#cansetTime").animate({left: "30px"}, 500);
                     state = true;
                 }
            }
       }

        function changeBar()
        {

             if(barstate==true)
             {
                 $("#leftbar").animate({left:"-220px"},500);
                 barstate=false;
             }
             else
             {
                 $("#leftbar").animate({left:"0px"},500);
                 barstate=true;
             }

             $("#cansetTime").animate({left:"-220px"},500);
             state=false;
        }
      <%
         if(session.getAttribute("startTime")==null)
         {
            Calendar cal=Calendar.getInstance();
            Date date=cal.getTime();
            SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd");
            session.setAttribute("startTime", sf.format(date));
         }

         if(session.getAttribute("endTime")==null)
         {

            Calendar cal=Calendar.getInstance();
            Date date=cal.getTime();
            SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd");
            session.setAttribute("endTime", sf.format(date));
         }
     %>


    </script>
</head>


<body>

   <!--测试专用语句  -->
   <% session.setAttribute("userId", "1"); %>

   <div id="leftbar">
		<!--头像-->
		<div id="leftbarHead">
			<div style="text-align: right">
				<img src="./image/timeright.png"
					style="width: 25px;height:25px;cursor: pointer;" title="时间设置显示"
					onclick="changeTIME()">
			</div>

			<div id="leftbarBottom">
				<div id="jrzl" class="leftbarBottom_div">
					<img src="./image/jrzl.PNG"><span class="Number">68754</span>
				</div>
				<div id="mg" class="leftbarBottom_div">
					<img src="./image/mg.PNG"><span class="Number">230</span>
				</div>
				<div id="fmg" class="leftbarBottom_div">
					<img src="./image/fmg.PNG"><span class="Number">66524</span>
				</div>
				<div id="mgbz" class="leftbarBottom_div">
					<img src="./image/mgbz.PNG"><span class="Number">3.4%</span>
				</div>
			</div>
		</div>

		<div id="leftbarBody">
			<div id="leftbarBody_Menu">
				<div class="Menu_fa">
					<a href="HomePage.jsp" class="Menu">首页</a>
				</div>
				<div class="Menu_fa" id="flip1">
					<img src="./image/right.png" style="width: 20px;height: 20px"><span
						class="Menu">舆情分析</span>
				</div>

				<div id="Menu_panel1">
					<div class="Menu_fa">
						<a href="MediaAnalysis.jsp" class="Menu_son">媒体分析</a>
					</div>
					<div class="Menu_fa">
						<a href="EmotionAnalysis.jsp" class="Menu_son">情感分析</a>
					</div>
					<div class="Menu_fa">
						<a href="PublicOpinionWarning.jsp" class="Menu_son">舆情预警</a>
					</div>
				</div>

				<div class="Menu_fa">
					<a class="Menu">伪舆情分析</a>
				</div>

				<div class="Menu_fa" id="flip2">
					<img src="./image/right.png" style="width:20px;height: 20px"><span
						class="Menu">舆情报表</span>
				</div>
				<div id="Menu_panel2">
					<div class="Menu_fa">
						<a href="TimeBriefing.jsp" class="Menu_son">时间报表</a>
					</div>
					<div class="Menu_fa">
						<a href="EventBriefing.jsp" class="Menu_son">事件报表</a>
					</div>

					 <%
				         if(session.getAttribute("userId")!=null)
				          {
				     %>
				    <div class="Menu_fa">
					      <a href="PersonalBriefing.jsp" class="Menu_son">个人报表</a>
				    </div>

				    <%
				          }
				     %>
				</div>



			</div>
		</div>
		<div style="text-align: right">
			<img src="./image/timeright.png"
				style="width: 25px;height:25px;cursor: pointer;" title="收缩左边栏"
				onclick="changeBar()">
		</div>
		<div style="clear: both"></div>

	</div>




	<div id="rightbar">
		<img src="./image/nanhua_logo.png" />
	</div>




</body>
</html>
