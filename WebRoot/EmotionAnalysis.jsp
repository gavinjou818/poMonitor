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
    
    <script src="js/jquery-3.1.1.js" ></script>
    <link href="css/EmotionAnalysis.css" rel="stylesheet" type="text/css"  />
    <link href="css/bootstrap.css" rel="stylesheet" type="text/css"  />
    <link href="css/bootstrap-theme.css" rel="stylesheet" type="text/css" />
    <link href="css/bootstrap-table.css" rel="stylesheet" type="text/css" />
    <script src="js/bootstrap-table.js"></script>

    
    <script src="js/bootstrap.js" ></script>
    <script src="js/echarts.js" ></script>
    <script src="js/china.js"></script>
    <script src="js/d3.js"></script>
    <script src="js/docs.js"></script>
    <script src="js/examples.js"></script>
   


    <link rel="stylesheet" type="text/css" href="./css/master.css">
    <script type="text/javascript" src="./js/master.js"></script>
    <script type="text/javascript" src="./js/YQCHART.js"></script>
    <script type="text/javascript" src="./js/smoothscroll.js"></script>
    
    
    
    <script type="text/javascript">
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

<jsp:include page="master.jsp"  flush="true"/>

	<div id="cansetTime">
		<div class="cansetTimeR">
			<span>从</span><input type="date" id="startTime" name="startTime" />
		</div>
		<div class="cansetTimeR">
			<span>到</span><input type="date" id="endTime" name="endTime" />
		</div>
		<button type="button" class="btn btn-primary .btn-sm"
			style="float: right;margin-right: 5px" onclick="setTimeOpera()")>确定</button>
	</div>

<div id="Catalog">
    <div style="text-align: left"><img src="./image/barleft.png" style="width: 25px;height:25px;cursor: pointer;" title="时间设置显示" onclick="changeCatalog()"></div>
    <div><a class="smoothScroll" href="<%=basePath%>EmotionAnalysis.jsp#head1" title="媒体来源情况">地域舆情数据</a></div>
    <div><a class="smoothScroll" href="<%=basePath%>EmotionAnalysis.jsp#head2" title="媒体情感分析">媒体情感分析</a></div>
    <div><a class="smoothScroll" href="<%=basePath%>EmotionAnalysis.jsp#head3" title="整体信息走势分析">整体信息走势分析</a></div>
    <div><a class="smoothScroll" href="<%=basePath%>EmotionAnalysis.jsp#head4" title="媒体负面倾向性走势">媒体负面倾向性走势</a></div>
    <div><a class="smoothScroll" href="<%=basePath%>EmotionAnalysis.jsp#head5" title="信息敏感分类情况">信息敏感分类情况</a></div>

</div>



<div  id="rightbody">

    <!--主体-->
    <div class="container" id="mainbody">
        <div class="row">
           
           
           <!--地域舆情数据-->
            <div class="Ancestor" id="head1">
                <h1 class="h1Title" >地域舆情数据</h1>
                <div class="col-md-6 col-xs-6 col-lg-6" >
                    <h2 class="h2Title">地域分布表</h2>

                    <div class="bs-example">
                        <table data-toggle="table" data-url="json/data1.json" data-height="370">
                            <thead>
                            <tr>
                                <th data-field="id">Item ID</th>
                                <th data-field="name">Item Name</th>
                                <th data-field="price">Item Price</th>
                            </tr>
                            </thead>
                        </table>
                    </div>

                </div>
                <div class="col-md-6 col-xs-6 col-lg-6">
                    <h2 class="h2Title">地域分布表</h2>
                    <div class="bs-example">
                        <table data-toggle="table" data-url="json/data1.json" data-height="370">
                            <thead>
                            <tr>
                                <th data-field="id">Item ID</th>
                                <th data-field="name">Item Name</th>
                                <th data-field="price">Item Price</th>
                            </tr>
                            </thead>
                        </table>
                    </div>
                </div>
                <div style="clear: both"></div>
            </div>

           <!--媒体情感分析 -->
            <div class="Ancestor" id="head2">
                <h1 class="h1Title">媒体情感分析</h1>
                <div class="Father" style="z-index: 100">
                    <div id="MediaAnalysis" style="width: 800px;height:600px;margin: 0 auto" ></div>
                    <div class="FatherDown"><img src="./image/down.png" style="width: 25px;height: 25px;cursor: pointer" onclick="changeSon('MediaAnalysisson')"></div>
                </div>

                <div class="Son" id="MediaAnalysisson" style="z-index: 99">
                    <div class="SonTitle">简介:</div>
                    <div class="SonContext">aaa</div>
                </div>
            </div>

            <!--整体信息走势分析 -->
            <div class="Ancestor" id="head3">
                <h1 class="h1Title">整体信息走势分析</h1>
                <div class="Father" style="z-index: 98">
                    <div id="otrendAnalysis" style="width: 800px;height:600px;margin: 0 auto" ></div>
                    <div class="FatherDown"><img src="./image/down.png" style="width: 25px;height: 25px;cursor: pointer" onclick="changeSon('otrendAnalysisson')"></div>
                </div>

                <div class="Son" id="otrendAnalysisson" style="z-index: 97">
                    <div class="SonTitle">简介:</div>
                    <div class="SonContext">aaa</div>
                </div>
            </div>

             <!--媒体负面倾向性走势 -->
            <div class="Ancestor" id="head4">
                <h1 class="h1Title">媒体负面倾向性走势</h1>
                <div class="Father" style="z-index: 96">
                    <div id="NegativeBias" style="width: 800px;height:600px;margin: 0 auto" ></div>
                    <div class="FatherDown"><img src="./image/down.png" style="width: 25px;height: 25px;cursor: pointer" onclick="changeSon('NegativeBiasson')"></div>
                </div>

                <div class="Son" id="NegativeBiasson" style="z-index: 95">
                    <div class="SonTitle">简介:</div>
                    <div class="SonContext">aaa</div>
                </div>
            </div>

            <!--信息敏感分类情况 -->
            <div class="Ancestor" id="head5">
                <h1 class="h1Title">信息敏感分类情况</h1>
                <div class="Father" style="z-index: 94">
                    <div id="SensitiveInfo" style="width: 800px;height:600px;margin: 0 auto" ></div>
                    <div class="FatherDown"><img src="./image/down.png" style="width: 25px;height: 25px;cursor: pointer" onclick="changeSon('SensitiveInfoson')"></div>
                </div>

                <div class="Son" id="SensitiveInfoson" style="z-index: 93">
                    <div class="SonTitle">简介:</div>
                    <div class="SonContext">aaa</div>
                </div>
            </div>



        </div>
    </div>



    <!--foot-->

    <script type="text/javascript">
        function getChart(startTime,endTime,userId)
        {
            //媒体分析
            MediaAnalysisChart("./servlet/IndexServlet",'MediaAnalysis',startTime,endTime,userId,"getMediaAnalysis");
            //整体走势分析
            OverallTrendChart("json/otrendAnalysis.json",'otrendAnalysis',startTime,endTime,userId,null);
            //信息敏感分类图
            InformationSensitiveClassificationChart("json/SensitiveInfo.json",'SensitiveInfo',startTime,endTime,userId,null);
            //媒体负面倾向性走势图
            MediaNegativeTendencyChart("json/NegativeBias.json",'NegativeBias',startTime,endTime,userId,null);
        }
    </script>



</body>
</html>
