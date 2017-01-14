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
<link href="css/PersonalBriefing.css" rel="stylesheet" type="text/css" />
<link href="css/bootstrap.css" rel="stylesheet" type="text/css" />

<link rel="stylesheet" type="text/css" href="./css/master.css">
<script src="js/jquery-3.1.1.js"></script>
<script type="text/javascript" src="./js/master.js"></script>
<script src="js/bootstrap.js"></script>
<script src="js/examples.js"></script>
<script type="text/javascript" src="./js/smoothscroll.js"></script>




<script type="text/javascript">
	//用于存储选择的数据的id;
	var SelectID={};
</script>

<!-- 预加载图片 -->
<script type="text/javascript">

	var startTime;
	var endTime;
	var mailString;





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

        startTime=document.getElementById('startTime').value='<%=session.getAttribute("startTime")%>';
        endTime=document.getElementById('endTime').value='<%=session.getAttribute("endTime")%>';

        initData(1, './servlet/sImageAndcTemplateServlet', 'grbf');

	}
</script>
<!--加载数据表-->
<script type="text/javascript">


	var operate=["查看","下载","邮箱发送"];
	var sum = 5;//最大页数5;
	var barsum = 5;//最大条目数5;
	var pagesum;//总共有的页数
	var curpage = 1;//当前页

	function initData(start, dataroot, tparent)
	{

		curpage = start;
		$.ajax
	    ({
					type : "POST",
					dataType : "json",
					url : dataroot,
					data :
					{

					    /*
					     测试时间
					    "startTime" : '2012-09-10',
						"endTime" : '2017-01-10',
					    */
						"startTime" : startTime,
						"endTime" : endTime,
						"userId" : '<%=session.getAttribute("userId")%>',
						"method" : 'getIntimateBriefing',
						"max" : barsum,
						"index" : (curpage - 1)*barsum
					},
					success : function(data)
					{
                        
                        
                        var BEF_savePath='<%=session.getAttribute("BEF_savePath")%>';
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

						if ((pagesum - curpage + 1) < barsum) 
						{
							start = (pagesum - barsum + 1);
							if (start <= 0) {
								start = 1;
							}
						}

                       
                        var halfsum=parseInt(barsum/2)+1;
						//确定开始标号范围.
						if (parseInt(start) - halfsum > 0) 
						{
							son2.addEventListener("click", function() 
							{
								initData(start - halfsum, dataroot, tparent);
							});
						}
						son1.appendChild(son2);
						parent.appendChild(son1);

						var i = start - halfsum+1;
						if (i <= 0) {
							i = 1;
						}
						for (var DEF = 0; DEF < sum && i <= pagesum; i++, DEF++)//创建导航条,DEF为计数器
						{
							var son1 = document.createElement("li");
							son1.setAttribute("class", "Point");
							var son2 = document.createElement("a");

							son2.innerText = i;
							son2.addEventListener("click", function() 
							{
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
						var End = parseInt(start) + halfsum;
						
						if (start == 1 && (parseInt(start) + barsum) < pagesum)
						{
							End = parseInt(start) + barsum;
						}

						//如果右标签的最后一个超出了page的最大值,那么就不添加事件了。
						if (End <= pagesum)
						{
							son2.addEventListener("click", function() {
								initData(End, dataroot, tparent);
							});
						}
						son1.appendChild(son2);
						parent.appendChild(son1);

                        //加载数据
					    var parent2=document.getElementById(tparent);
                        var parent2=parent2.getElementsByTagName("tbody");
                        parent2=parent2[0];

						//先清除所有标签
						while (parent2.hasChildNodes())
							parent2.removeChild(parent2.firstChild);
  
						//加载每页上的数据
				
				for (i = 0; i < data.now; i++)
					{

	                     var tr=document.createElement("tr");

                          //加载id
	                     var td1=document.createElement("td");
                          
                          
                          td1.innerHTML+=(i+1+(curpage-1)*sum);
                        
                          tr.appendChild(td1);

                        //加载时间
                        var td2=document.createElement("td");
	                    var time=new Date(data.Briefings[i].time);
                        td2.innerText=time.getFullYear()+"-"+time.getMonth()+"-"+time.getDate();
                        tr.appendChild(td2);

                        //加标题
                        var td3=document.createElement("td");
                        td3.innerText=data.Briefings[i].virtualname;
                        tr.appendChild(td3);

                        //加载链接查看与信息
                        var td4=document.createElement("td");


	                    //插入查看
	                    var a0=document.createElement("a");
	                    a0.setAttribute("href",data.Briefings[i].basepath+BEF_savePath+data.Briefings[i].pdfpath);
	                    a0.innerHTML=operate[0];
	td4.appendChild(a0);

	var textnode1=document.createTextNode("|");
	td4.appendChild(textnode1);

	//插入下载

	var a1=document.createElement("a");
	a1.setAttribute("href",data.Briefings[i].basepath+BEF_savePath+data.Briefings[i].docpath);
	a1.innerHTML=operate[1];
	td4.appendChild(a1);

	var textnode2=document.createTextNode("|");
	td4.appendChild(textnode2);

	//插入邮箱发送
	var a2=document.createElement("a");
	a2.setAttribute("value", data.Briefings[i].id);
	a2.setAttribute("class","Point");
	a2.addEventListener("click", function()
	{        
	      mailString=this.getAttribute("value");
	      showModel();
	});
	a2.innerHTML=operate[2];
	td4.appendChild(a2);


                        tr.appendChild(td4);
                        parent2.appendChild(tr);

						}

					}
				});
	}
</script>


<script type="text/javascript">


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
                    xmlhttp.open("POST","<%=path%>/servlet/BasicInteractionServlet", true);
		            xmlhttp.setRequestHeader("Content-type","application/x-www-form-urlencoded");
		            xmlhttp.onreadystatechange = function( ) 
		            {
			           if (xmlhttp.readyState == 4 && xmlhttp.status == 200) 
			           {
				         location.reload();
			           }
		            
		            };
		            xmlhttp.send(data);

	}
	
	
	function sendEmail()
	{
	                var Tomail=document.getElementById('mailtext').value;
	                var data=("recipientString="+Tomail);
                    data+=("&requestString="+mailString);
                    data+=("&method="+"sendEmail");
                    var xmlhttp;
                    if (window.XMLHttpRequest)xmlhttp = new XMLHttpRequest();
                    else xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
                   
                    xmlhttp.open("POST","<%=path%>/servlet/sImageAndcTemplateServlet", true);
		            xmlhttp.setRequestHeader("Content-type","application/x-www-form-urlencoded");
		            xmlhttp.onreadystatechange = function(event) 
		            {
			            if(xmlhttp.readyState == 4 && xmlhttp.status == 200) 
			            {
				            alert(event.message);
			            }
		            };
		            xmlhttp.send(data);

	}
</script>

<script>
    function showModel() 
    {
        document.getElementById('myModel').style.display = 'block';
    }
    function closeModel() {
        document.getElementById('myModel').style.display = 'none';
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
			<a class="smoothScroll"
				href="<%=basePath%>PersonalBriefing.jsp#head1" title="个人报表">个人报表
			</a>
		</div>

	</div>



	<div id="rightbody">
		<!--主体-->
		<div class="container">
			<div class="row">


				<!--个人报表-->
				<div class="Ancestor" id="head1">
					<h1 class="h1Title">个人报表</h1>

					<div class="tab-content">

						<div role="tabpanel" class="tab-pane active" id="grbf"
							style="padding-top:10px">
							<table class="table table-hover ">
								<thead>
									<th style="width:10%;">编号</th>
									<th style="width:20%;">时间</th>
									<th style="width:50%">报表名称</th>
									<th style="width:20%;">操作</th>
								</thead>
								<tbody></tbody>
							</table>

							<ul class="pagination pagsize">
							</ul>

						</div>
					</div>

					<div style="clear: both"></div>
				</div>


			</div>
		</div>
	</div>


	<!-- foot -->
	
	
	<!--蒙层 -->
	<div id="myModel">
        <div class="model-content">
            <div class="panel panel-form" style=" border:0px;box-shadow: 0px 0px 10px black;">
                 
                 <div class="panel-heading" id="myModel-heading">
                     <h2 class>发送邮件</h2>
                     <p>注意要以;号隔开</p>
                 </div>
                 <div class="panel-boby" id="myModel-panel-body-input">
                     <div>
                       <input type="text" class="form-control" id="mailtext" name="mailtext" placeholder="请输入邮箱并以;号隔开"/>
                     </div>
                     <div id="myModel-panel-body-button">
                        <button class="btn btn-primary" onclick="sendEmail()" >确认</button>
                        <button class="btn btn-primary" onclick="closeModel()">关闭</button> 
                     </div>
                 </div>
            </div>
        </div>
    </div>



</body>
</html>
