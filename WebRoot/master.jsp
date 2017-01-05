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
</head>


<body>
    <div id="leftbar">
         <!--头像-->
         <div id="leftbarHead">
             <div style="text-align: right"><img src="./image/timeright.png" style="width: 25px;height:25px;cursor: pointer;" title="时间设置显示" onclick="changeTIME()" ></div>

             <div id="leftbarBottom">
                 <div id="jrzl" class="leftbarBottom_div"><img src="./image/jrzl.PNG"><span class="Number">68754</span></div>
                 <div id="mg"   class="leftbarBottom_div"><img src="./image/mg.PNG"><span   class="Number">230</span></div>
                 <div id="fmg"  class="leftbarBottom_div"><img src="./image/fmg.PNG"><span  class="Number">66524</span></div>
                 <div id="mgbz" class="leftbarBottom_div"><img src="./image/mgbz.PNG"><span class="Number">3.4%</span></div>
             </div>
         </div>

         <div id="leftbarBody">
             <div id="leftbarBody_Menu">
                 <div class="Menu_fa"><a href="SY.jsp" class="Menu">首页</a></div>
                 <div class="Menu_fa" id="flip1" ><img src="./image/right.png" style="width: 20px;height: 20px"><span class="Menu">舆情分析</span></div>
                 <div id="Menu_panel1">
                     <div class="Menu_fa"><a href="MTFX.jsp" class="Menu_son" >媒体分析</a></div>
                     <div class="Menu_fa"><a href="QGFX.jsp" class="Menu_son" >情感分析</a></div>
                     <div class="Menu_fa"><a href="YQYJ.jsp" class="Menu_son" >舆情预警</a></div>
                 </div>
                 <div class="Menu_fa"><a href="#" class="Menu">伪舆情分析</a></div>

                 <div class="Menu_fa" id="flip2" ><img src="./image/right.png" style="width:20px;height: 20px"><span class="Menu">舆情报表</span></div>
                 <div id="Menu_panel2">
                     <div class="Menu_fa"><a href="TP.jsp" class="Menu_son" >时间报表</a></div>
                     <div class="Menu_fa"><a href="SP.jsp" class="Menu_son" >事件报表</a></div>
                 </div>

             </div>
         </div>
         <div style="text-align: right"><img src="./image/barleft.png" style="width: 25px;height:25px;cursor: pointer;" title="收缩左边栏" onclick="changeBar()"></div>
         <div style="clear: both"></div>

    </div>

    <div id="Catalog">
        <div style="text-align: left"><img src="./image/barleft.png" style="width: 25px;height:25px;cursor: pointer;" title="时间设置显示" onclick="changeCatalog()"></div>
    </div>
    <!--右边标题-->
    <div id="cansetTime">
         <div class="cansetTimeR"><span>从<span><input type="date" id="starttime" name="starttime" /></div>
         <div class="cansetTimeR"><span>到<span><input type="date" id="endtime" name="endtime" /></div>
         <button type="button" class="btn btn-primary .btn-sm" style="float: right;margin-right: 5px">确定</button>
    </div>
    <div id="rightbar">
        <img src="./image/nanhua_logo.png"/>
    </div>
    <div  id="rightbody">


    </div>
</body>
</html>
