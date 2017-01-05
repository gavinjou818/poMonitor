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
    <link href="css/QGFX.css" rel="stylesheet" type="text/css"  />
    <link href="css/bootstrap.css" rel="stylesheet" type="text/css"  />
    <link href="css/bootstrap-theme.css" rel="stylesheet" type="text/css" />
    <link href="css/bootstrap-table.css" rel="stylesheet" type="text/css" />

    <!--在线jq-->
    <script src="js/jquery-3.1.1.js" ></script>
    <script src="js/bootstrap.js" ></script>
    <script src="js/echarts.js" ></script>
    <script src="js/china.js"></script>
    <script src="js/d3.js"></script>
    <script src="js/docs.js"></script>
    <script src="js/examples.js"></script>
    <script src="js/bootstrap-table.js"></script>


    <link rel="stylesheet" type="text/css" href="./css/master.css">
    <script type="text/javascript" src="./js/master.js"></script>
    <script type="text/javascript" src="./js/YQCHART.js"></script>
    <script type="text/javascript" src="./js/smoothscroll.js"></script>

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
    <div><a class="smoothScroll" href="<%=basePath%>QGFX.jsp#head1" title="媒体来源情况">地域舆情数据</a></div>
    <div><a class="smoothScroll" href="<%=basePath%>QGFX.jsp#head2" title="媒体分析情况">媒体分析情况</a></div>
    <div><a class="smoothScroll" href="<%=basePath%>QGFX.jsp#head3" title="整体走势分析情况">整体走势分析情况</a></div>
    <div><a class="smoothScroll" href="<%=basePath%>QGFX.jsp#head4" title="媒体负面倾向性走势情况">媒体负面倾向性走势情况</a></div>
    <div><a class="smoothScroll" href="<%=basePath%>QGFX.jsp#head5" title="信息敏感分类情况">信息敏感分类情况</a></div>

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

    <!--主体-->
    <div class="container" id="mainbody">
        <div class="row">

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


            <div class="Ancestor" id="head2">
                <h1 class="h1Title">媒体分析情况</h1>
                <div class="Father" style="z-index: 100">
                    <div id="MediaAnalysis" style="width: 800px;height:600px;margin: 0 auto" ></div>
                    <div class="FatherDown"><img src="./image/down.png" style="width: 25px;height: 25px;cursor: pointer" onclick="changeSon('MediaAnalysisson')"></div>
                </div>

                <div class="Son" id="MediaAnalysisson" style="z-index: 99">
                    <div class="SonTitle">简介:</div>
                    <div class="SonContext">aaa</div>
                </div>
            </div>


            <div class="Ancestor" id="head3">
                <h1 class="h1Title">整体走势分析情况</h1>
                <div class="Father" style="z-index: 98">
                    <div id="otrendAnalysis" style="width: 800px;height:600px;margin: 0 auto" ></div>
                    <div class="FatherDown"><img src="./image/down.png" style="width: 25px;height: 25px;cursor: pointer" onclick="changeSon('otrendAnalysisson')"></div>
                </div>

                <div class="Son" id="otrendAnalysisson" style="z-index: 97">
                    <div class="SonTitle">简介:</div>
                    <div class="SonContext">aaa</div>
                </div>
            </div>


            <div class="Ancestor" id="head4">
                <h1 class="h1Title">媒体负面倾向性走势情况</h1>
                <div class="Father" style="z-index: 96">
                    <div id="NegativeBias" style="width: 800px;height:600px;margin: 0 auto" ></div>
                    <div class="FatherDown"><img src="./image/down.png" style="width: 25px;height: 25px;cursor: pointer" onclick="changeSon('NegativeBiasson')"></div>
                </div>

                <div class="Son" id="NegativeBiasson" style="z-index: 95">
                    <div class="SonTitle">简介:</div>
                    <div class="SonContext">aaa</div>
                </div>
            </div>

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
        $(document).ready(function()
        {
            //媒体分析
            MediaAnalysisChart("json/MediaAnalysis.json",'MediaAnalysis',new Date(),new Date(),null,null);
            //整体走势分析
            OverallTrendChart("json/otrendAnalysis.json",'otrendAnalysis',new Date(),new Date(),null,null);
            //信息敏感分类图
            InformationSensitiveClassificationChart("json/SensitiveInfo.json",'SensitiveInfo',new Date(),new Date(),null,null);
            //媒体负面倾向性走势图
            MediaNegativeTendencyChart("json/NegativeBias.json",'NegativeBias',new Date(),new Date(),null,null);
        });
    </script>



</body>
</html>
