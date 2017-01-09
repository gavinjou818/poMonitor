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
    <link href="css/EventBriefing.css" rel="stylesheet" type="text/css"  />
    <link href="css/bootstrap.css" rel="stylesheet" type="text/css"  />

    <!--在线jq-->
    <script src="js/jquery-3.1.1.js" ></script>
    <script src="js/bootstrap.js" ></script>
    <script src="js/examples.js"></script>


    <link rel="stylesheet" type="text/css" href="./css/master.css">
    <script type="text/javascript" src="./js/master.js"></script>
    <script type="text/javascript" src="./js/smoothscroll.js"></script>



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
                        for(var j=0;j<operate.length;j++)
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

    </script>


    <script type="text/javascript">
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

        function preload()
        {
           initData(1,'json/test_data.json','eventBriefing');

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
			style="float: right;margin-right: 5px" onclick="setTimeOpera()">确定</button>
	</div>

<div id="Catalog">
    <div style="text-align: left"><img src="./image/barleft.png" style="width: 25px;height:25px;cursor: pointer;" title="侧菜单栏" onclick="changeCatalog()"></div>
    <div><a class="smoothScroll" href="<%=basePath%>EventBriefing.jsp#head1" title="事件报表">事件报表</a></div>
</div>




<div  id="rightbody">
    <!--主体-->
    <div class="container" >
        <div class="row">


            <div class="Ancestor" id="head1">
                <h1 class="h1Title">事件报表</h1>

                <!--Tab panes-->

                <div class="tab-content">
                    <!--事件报表-->
                    <div role="tabpanel" class="tab-pane active" id="eventBriefing" style="padding-top:10px" >
                        <table class="table table-hover " >
                            <thead>
                            <th style="width:10%;">编号</th>
                            <th style="width:20%;">时间</th>
                            <th style="width:50%">报表名称</th>
                            <th style="width:20%;">操作</th>
                            </thead>
                            <tbody>



                            </tbody>
                        </table>

                        <ul class="pagination pagsize">


                        </ul>
                        <div style="float: right">
                            <button type="button" class="btn btn-default" onclick="allseleck('eventBriefing')">全选</button>
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




<!--foot-->




</body>
</html>
