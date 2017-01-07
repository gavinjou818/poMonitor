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
	
    <title>南华大学舆情核电系统</title>
    
    <link rel="stylesheet" type="text/css" href="./css/bootstrap.css">
    <link rel="stylesheet" type="text/css" href="./css/index.css">
    <script type="text/javascript" src="./js/jquery-3.1.1.js"></script>


    <script>
      var state=true;

      function change()
      {
            if(state==true)
            {
                $("#Notice").animate({top:"0px"},1000);
                state=false;
            }
            else
            {
                $("#Notice").animate({top:"-300px"},1000);
                state=true;
            }
      }
      
      
       

    </script>

</head>
<body >

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
    <div id="Login_Container">

        <div id="Login_Top">

            <div id="Login_Top_fengche" onclick="change()"><img src="./image/fengche.png"> </div>
            <!--公告-->
            <div id="Notice">
                <div id="Noticetitle"><img src="./image/noticetitle.PNG"></div>
            </div>
            <a href="SY.jsp"><img src="./image/title.PNG" /></a>
        </div>

        <div id="Login_Center" role="form">
            <div id="upload"><img src="./image/Login.PNG"></div>
            <div id="body">
                <form method="post">
                    <div class="body_input"><img src="./image/UserName.PNG"></div>
                    <div class="body_up"><input type="text" class="form-control" id="username"  placeholder="请输入账号"/></div>
                    <div class="body_input"><img src="./image/password.PNG"></div>
                    <div class="body_up"><input type="password" class="form-control" id="password"  placeholder="请输入密码"/></div>
                    <div id="body_botton">
                        <input type="button" id="register"/>
                        <input type="reset" id="reset" value=""/>
                        <input type="submit" value="" id="submit"/>
                    </div>
                </form>
            </div>
        </div>
    </div>
</body>
</html>
