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
<link href="css/YQSL.css" rel="stylesheet" type="text/css" />
<link href="css/bootstrap.css" rel="stylesheet" type="text/css" />

<link rel="stylesheet" type="text/css" href="./css/master.css">
<script src="js/jquery-3.1.1.js"></script>
<script src="js/echarts.js"></script>
<script type="text/javascript" src="./js/master.js"></script>
<script src="js/bootstrap.js"></script>
<script src="js/examples.js"></script>
<script type="text/javascript" src="./js/smoothscroll.js"></script>
<script type="text/javascript" src="./js/briefingChart.js"></script>

<style>
.Point {
	cursor: pointer;
}

.Right {
	float: right;
}


.pagination {
	margin: 0px !important;
}
</style>
<!-- 预加载图片 -->
<script type="text/javascript">

    
    var IMGroot=["image/nev.png","image/cen.png","image/pos.png"];
    var images = new Array();  
    var Charts=new Array();
    
    function preload()
    {
       for(var i=0;i<IMGroot.length;i++)
       {
         images[i]=new Image();
         images[i].width="25";
         images[i].height="25";
         images[i].src=IMGroot[i];
         images[i].setAttribute("class","Right"); 
       }
       initData(1,'./servlet/sImageAndcTemplateServlet','bbyl');
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

       document.getElementById('startTime').value='<%=session.getAttribute("startTime")%>';
       document.getElementById('endTime').value='<%=session.getAttribute("endTime")%>';
        
    }
    
    function exportImage(servletMethod)
    {     
         
          var data=("method="+servletMethod);
          data+=("&length=2");//有多少个图表
          data+=("&requestString="+requestString);
          for(var i=0;i<Charts.length;i++)
          {
               data+="&c"+i+"="+encodeURIComponent(Charts[i].getDataURL("png"));
          }
                    
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
                    xmlhttp.open("POST","<%=path%>/servlet/sImageAndcTemplateServlet",true);  
                    xmlhttp.setRequestHeader("Content-type","application/x-www-form-urlencoded");  
                    xmlhttp.onreadystatechange = function() 
                    {  
                        if (xmlhttp.readyState == 4 && xmlhttp.status == 200) 
                        {  
                            alert("报表生成成功");  
                        }  
                    };
                  xmlhttp.send(data);  
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
	var sum = 5;//最大页数5;
	var barsum = 5;//最大条目数5;
	var pagesum;//总共有的页数
	var curpage = 1;//当前页
    var requestString='<%=request.getParameter("requestString")%>';
	
	function initData(start, dataroot, tparent) 
	{
       
		curpage = start;
		$.ajax({
					type : "POST",
					dataType : "json",
					url : dataroot,
					data : 
					{
						
						"requestString":requestString,
						"userId" : 1,
						"method" : 'getPreviewBriefing',
						"max" : barsum,
						"index" : (curpage - 1)*barsum
					},
					success:function(data)
					{

						//获取ul节点
						//先获取总的页数
						pagesum = parseInt(data.sum / barsum);
						//如果有超出页数范围多加一页
						pagesum += (data.sum % barsum ? 1 : 0);

						//获取要添加数据的模块
						var parent = document.getElementById(tparent);
						parent = parent.getElementsByTagName("ul");
						parent = parent[0];

						//清除节点
						while (parent.hasChildNodes())
							parent.removeChild(parent.firstChild);
						//先清除所有节点

						// 加入<<节点
						// 创建li节点
						var son1 = document.createElement("li");
						// 添加小手指
						son1.setAttribute("class", "Point");
						//添加标号<<
						var son2 = document.createElement("a");
						son2.innerHTML = "&laquo;";

						if ((pagesum - curpage + 1) < sum) {
							start = (pagesum - sum + 1);
							if (start <= 0) {
								start = 1;
							}
						}

						//确定开始标号范围.
						if (parseInt(start) - 3 > 0) {
							son2.addEventListener("click", function() {
								initData(start - 3, dataroot, tparent);
							});
						}
						son1.appendChild(son2);
						parent.appendChild(son1);

						var i = start - 2;
						if (i <= 0) {
							i = 1;
						}
						for (var DEF = 0; DEF < sum && i <= pagesum; i++, DEF++)//创建导航条,DEF为计数器
						{
							var son1 = document.createElement("li");
							son1.setAttribute("class", "Point");
							var son2 = document.createElement("a");

							son2.innerText = i;
							son2.addEventListener("click", function() {
								initData(this.innerText, dataroot, tparent);
							});
							son1.appendChild(son2);
							parent.appendChild(son1);
						}

						// 加入>>号
						son1 = document.createElement("li");
						// 添加小手指
						son1.setAttribute("class", "Point");
						//添加标号>>
						son2 = document.createElement("a");
						son2.innerHTML = "&raquo;";

						//确定开始标号范围.
						var End = parseInt(start) + 3;
						if (start == 1 && (parseInt(start) + 5) < pagesum) {
							End = parseInt(start) + 5;
						}

						//如果右标签的最后一个超出了page的最大值,那么就不添加事件了。
						if (End <= pagesum) {
							son2.addEventListener("click", function() {
								initData(End, dataroot, tparent);
							});
						}
						son1.appendChild(son2);
						parent.appendChild(son1);

						var parent2 = document.getElementById(tparent);
						parent2 = parent2.getElementsByTagName("div");
						parent2 = parent2[0];
						//先清除所有标签
						while (parent2.hasChildNodes())
							parent2.removeChild(parent2.firstChild);

						//加载每页上的数据
						for (i = 0; i < data.now; i++) 
						{

							//创建面板
							var div1 = document.createElement("div");
							div1.setAttribute("class", "panel panel-primary");

							//加入标题
							var div2 = document.createElement("div");
							div2.setAttribute("class", "panel-heading");
							div2.style.setProperty("font-weight", "bold");

							div2.appendChild(images[parseInt(data.gAM_Result_Briefings[i].tendclass) + 1]);
							div2.innerHTML +=" "+data.gAM_Result_Briefings[i].title;

							//加入内容
							var div3 = document.createElement("div");
							div3.setAttribute("class", "panel-body");
							div3.innerText = data.gAM_Result_Briefings[i].content;

							//加入页脚
							var div4 = document.createElement("div");
							div4.setAttribute("class", "panel-footer");
							//添加描述
							var time = new Date(
									data.gAM_Result_Briefings[i].date);
							div4.innerHTML = "[采集自] "
									+ data.gAM_Result_Briefings[i].web + "    "
									+ "[发布时间] " + time.getFullYear() + "-"
									+ (time.getMonth() + 1) + "-"
									+ time.getDate();

							div1.appendChild(div2);
							div1.appendChild(div3);
							div1.appendChild(div4);

							parent2.appendChild(div1);

						}

					}
				});

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
			<a class="smoothScroll" href="<%=basePath%>YQSL.jsp#head1"
				title="报表预览">报表预览</a>
		</div>
	</div>
	

	<div id="rightbody">
		<!--主体-->
		<div class="container">
			<div class="row">


				<!-- 预览报表 -->
				<div class="Ancestor" id="head1">
					<h1 class="h1Title">预览报表</h1>

					<h2 class="h2Title">新闻选择</h2>
					<div class="tab-content">
						<div role="tabpanel" class="tab-pane active" id="bbyl"
							style="padding-top:10px">
							<div></div>
							<ul class="pagination pagsize" style="margin-bottom: 50px">


							</ul>
							<div style="float: right;margin-bottom: 50px" >     
							    <!--createTemplate1 是暂时的参数,换多模板需要更换  -->
								<button type="button" class="btn btn-default" onclick="exportImage('createTemplate1')">生成报表</button>
							</div>


						</div>
					</div>

					<div style="clear: both"></div>
				</div>


				<div class="Ancestor" id="head2">
					<h2 class="h2Title">信息来源图</h2>
					<div id="MTcome" style="width: 800px;height:600px;margin: 0 auto"></div>
					<div style="clear: both"></div>
				</div>


				<div class="Ancestor" id="head3">
					<h2 class="h2Title">媒体总倾向性分类图</h2>
					<div id="gTofmedia" style="width: 800px;height:600px;margin:0 auto"></div>
					<div style="clear: both"></div>
				</div>




			</div>
		</div>
	</div>


	<!-- foot -->

	<script type="text/javascript">
    $(document).ready(function()
    {    
    
              
			 $.ajax
			 ({
		         type: "GET",
		         url:"./servlet/sImageAndcTemplateServlet",
		         dataType: "json",
		         data: 
		 		 {      
		        	    "requestString":'<%=request.getParameter("requestString")%>',
		 				"method":'getPreviewChart',
		 				"templateName":'template1' //当要做多模板的时候则需要,将该名称去掉,这是个暂时的参数
		 		 },
		         success: function(datt) 
		         {
                     Charts[0]=BriefingInformationSourcesChart(datt,"MTcome");
                     Charts[1]=BriefingGeneralTendencyOfmediaChart(datt,"gTofmedia");
		         }
		     });
         //生成所选词条的新闻来源占比
         
    });
</script>



</body>
</html>
