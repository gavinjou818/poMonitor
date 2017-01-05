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
<link href="css/MGYQ.css" rel="stylesheet" type="text/css" />
<link href="css/bootstrap.css" rel="stylesheet" type="text/css" />

<link rel="stylesheet" type="text/css" href="./css/master.css">
<script src="js/jquery-3.1.1.js"></script>
<script type="text/javascript" src="./js/master.js"></script>
<script src="js/bootstrap.js"></script>
<script src="js/examples.js"></script>
<script type="text/javascript" src="./js/smoothscroll.js"></script>

<style>
.Point {
	cursor: pointer;
}

.Right {
	float: right;
}

.tab-content {
	height: 800px;
}
</style>


<script type="text/javascript">
	//用于存储选择的数据的id;
	var SelectID={};
</script>

<!-- 预加载图片 -->
<script type="text/javascript">
	var IMGroot = [ "image/nev.png", "image/cen.png", "image/pos.png" ];
	var images = new Array();
	function preload() {
		for (var i = 0; i < IMGroot.length; i++) {
			images[i] = new Image();
			images[i].width = "25";
			images[i].height = "25";
			images[i].src = IMGroot[i];
			images[i].setAttribute("class", "Right");
		}

		initData(1, './servlet/IndexServlet', 'jryq');
		initData(1, 'json/MGYQ.json', 'mgyq');
		initData(1, 'json/MGYQ.json', 'fmgyq');
	}
</script>
<!--加载数据表-->
<script type="text/javascript">
	var sum = 5;//最大页数5;
	var barsum = 5;//最大条目数5;
	var pagesum;//总共有的页数
	var curpage = 1;//当前页

	function initData(start, dataroot, tparent) 
	{

		curpage = start;
		$.ajax({
					type : "POST",
					dataType : "json",
					url : dataroot,
					data : 
					{
						"startTime" : '2012-09-10',
						"endTime" : '2017-01-10',
						"userId" : 1,
						"method" : 'getAllMessage_Briefing',
						"max" : barsum,
						"index" : (curpage - 1)*barsum
					},
					success : function(data) {

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
						for (i = 0; i < data.now; i++) {

							//创建面板
							var div1 = document.createElement("div");
							div1.setAttribute("class", "panel panel-primary");

							//加入标题
							var div2 = document.createElement("div");
							var div2input = document.createElement("input");
							div2input.setAttribute("type", "checkbox");
							div2input.setAttribute("name", tparent + "cb");
							div2input.setAttribute("value",data.gAM_Result_Briefings[i].id);
	
							if(SelectID[data.gAM_Result_Briefings[i].id]==true)
							{
							   div2input.setAttribute("checked","true");
							}
							div2input.setAttribute("onclick", "setID(this)");
							

							div2.appendChild(div2input);
							div2.setAttribute("class", "panel-heading");
							div2.style.setProperty("font-weight", "bold");

							div2.appendChild(images[parseInt(data.gAM_Result_Briefings[i].tendclass) + 1]);
							div2.innerHTML += " "
									+ data.gAM_Result_Briefings[i].title;

							//加入偏向性

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


<script type="text/javascript">

       

    //得到所选的新闻id;
	function setID(cb)
	{
	    if(cb.checked)
	    {
	       SelectID[cb.getAttribute("value")]=true;
	    }
	    else
	    {
	       delete SelectID[cb.getAttribute("value")];
	    }
	}

	//全选事件
	function allseleck(papername)
	{
		var parent = document.getElementById(papername);
		parent = parent.getElementsByTagName("input");

		for (var i = 0; i < parent.length; i++) 
		{
			parent[i].checked = true;
			if(SelectID[parent[i].getAttribute("value")]!=true)
			{
			   SelectID[parent[i].getAttribute("value")]=true;
			}
		}
	}
	//模拟表单
	function Post()
	{   
	
	    //先建立要投送的地址
	    var URL='<%=basePath%>YQSL.jsp';
	    //对应的表单
	    var temp=document.createElement("form");
	    
	    //提交的URL
	    temp.action=URL;
	    //对应的方法
	    temp.method="post";
	    //隐藏
	    temp.style.display="none";
	    //创建text
	    var opt=document.createElement("textarea");
	    //命名
	    opt.name="requestString";
	    //将选择了的key全部传送过去.
	    var String="";
	    var flag=true;
	    for(var key in SelectID)
	    {
	       if(flag){String+=key;flag=false;}
	       else String+=(","+key);
	    }
	    
	    //赋值
	    opt.value=String;
	    //插入
	    temp.appendChild(opt);
	    //提交表单
	    temp.submit();
	}
</script>


</head>
<body onload="preload()">

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
					<a href="SY.jsp" class="Menu">首页</a>
				</div>
				<div class="Menu_fa" id="flip1">
					<img src="./image/right.png" style="width: 20px;height: 20px"><span
						class="Menu">舆情分析</span>
				</div>
				<div id="Menu_panel1">
					<div class="Menu_fa">
						<a href="MTFX.jsp" class="Menu_son">媒体分析</a>
					</div>
					<div class="Menu_fa">
						<a href="QGFX.jsp" class="Menu_son">情感分析</a>
					</div>
					<div class="Menu_fa">
						<a href="YQYJ.jsp" class="Menu_son">舆情预警</a>
					</div>
				</div>
				<div class="Menu_fa">
					<a href="#" class="Menu">伪舆情分析</a>
				</div>

				<div class="Menu_fa" id="flip2">
					<img src="./image/right.png" style="width:20px;height: 20px"><span
						class="Menu">舆情报表</span>
				</div>
				<div id="Menu_panel2">
					<div class="Menu_fa">
						<a href="TP.jsp" class="Menu_son">时间报表</a>
					</div>
					<div class="Menu_fa">
						<a href="SP.jsp" class="Menu_son">事件报表</a>
					</div>
				</div>

			</div>
		</div>
		<div style="text-align: right">
			<img src="./image/barleft.png"
				style="width: 25px;height:25px;cursor: pointer;" title="收缩左边栏"
				onclick="changeBar()">
		</div>
		<div style="clear: both"></div>

	</div>

	<div id="Catalog">
		<div style="text-align: left">
			<img src="./image/barleft.png"
				style="width: 25px;height:25px;cursor: pointer;" title="时间设置显示"
				onclick="changeCatalog()">
		</div>
		<div>
			<a class="smoothScroll" href="<%=basePath%>MGYQ.jsp#head1"
				title="今日全部舆情">今日全部舆情</a>
		</div>
		<div>
			<a class="smoothScroll" href="<%=basePath%>MGYQ.jsp#head2"
				title="今日敏感舆情">今日敏感舆情</a>
		</div>
		<div>
			<a class="smoothScroll" href="<%=basePath%>MGYQ.jsp#head3"
				title="今日非敏感舆情">今日非敏感舆情</a>
		</div>
	</div>
	<!--右边标题-->
	<div id="cansetTime">
		<div class="cansetTimeR">
			<span>从</span><input type="date" id="starttime" name="starttime" />
		</div>
		<div class="cansetTimeR">
			<span>到</span><input type="date" id="endtime" name="endtime" />
		</div>
		<button type="button" class="btn btn-primary .btn-sm"
			style="float: right;margin-right: 5px">确定</button>
	</div>
	<div id="rightbar">
		<img src="./image/nanhua_logo.png" />
	</div>
	<div id="rightbody">
		<!--主体-->
		<div class="container">
			<div class="row">


                <!-- 今日全部舆情 -->
				<div class="Ancestor" id="head1">
					<h1 class="h1Title">今日全部舆情</h1>

					<div class="tab-content">
						<div role="tabpanel" class="tab-pane active" id="jryq"
							style="padding-top:10px">
							<div></div>

							<ul class="pagination pagsize">


							</ul>
							<div style="float: right">
								<button type="button" class="btn btn-default"
									onclick="allseleck('jryq')">全选</button>
								<button type="button" class="btn btn-default">邮件发送</button>
								<button type="button" class="btn btn-default" onclick="Post()">导出</button>
							</div>


						</div>
					</div>
					<div style="clear: both"></div>
				</div>


				<div class="Ancestor" id="head2">
					<h1 class="h1Title">今日敏感舆情</h1>

					<div class="tab-content">
						<div role="tabpanel" class="tab-pane active" id="mgyq"
							style="padding-top:10px">
							<div></div>

							<ul class="pagination pagsize">


							</ul>
							<div style="float: right">
								<button type="button" class="btn btn-default"
									onclick="allseleck('mgyq')">全选</button>
								<button type="button" class="btn btn-default">邮件发送</button>
								<button type="button" class="btn btn-default" >导出</button>
							</div>


						</div>
					</div>
					<div style="clear: both"></div>
				</div>


				<div class="Ancestor" id="head3">
					<h1 class="h1Title">今日非敏感舆情</h1>
					<div class="tab-content">
						<div role="tabpanel" class="tab-pane active" id="fmgyq"
							style="padding-top:10px">
							<div></div>

							<ul class="pagination pagsize">


							</ul>
							<div style="float: right">
								<button type="button" class="btn btn-default"
									onclick="allseleck('fmgyq')">全选</button>
								<button type="button" class="btn btn-default">邮件发送</button>
								<button type="button" class="btn btn-default">导出</button>
							</div>


						</div>
					</div>
					<div style="clear: both"></div>
				</div>



			</div>



		</div>
     

	</div>





	<!-- foot -->



</body>
</html>
