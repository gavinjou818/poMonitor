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
<body>

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
                <div id="Noticetitle"><div id="sonNoticetitle">公告</div></div>
            </div>
            <a href="HomePage.jsp" style="text-decoration: none"><div id="title">南華大学舆情系統</div></a>
        </div>

        <div id="Login_Center" role="form">
            <div id="upload"><div id="Logintitle">登录</div></div>
            <div id="body">
                <form method="post" action="./servlet/BasicInteractionServlet">
                    <div class="body_input"><div id="UserNameTitle">用户名  <span >username</span> <span class="glyphicon glyphicon-user"></span>  </div></div>
                    <div class="body_up"><input type="text" class="form-control" id="username" name="username" placeholder="请输入账号"/></div>
                    <div class="body_input">
                          <div id="passwordTitle">
                                                                             密&nbsp;&nbsp;&nbsp;码 
                          <span>password</span>
                          <span class="glyphicon glyphicon-lock"></span>
                          </div> 
                    </div>
                    <div class="body_up"><input type="password" class="form-control" id="userpwd" name="userpwd"  placeholder="请输入密码"/></div>
                    <div id="body_botton">
                        <input type="button" id="register" name="register" value="注册"/>
                        <input type="reset" id="reset" name="reset" value="重置"/>
                        <input type="submit"id="submit" name="submit" value="登录"  />
                        <div style="clear:both;"></div>
                    </div>
                    <div id="post-foot">
                          <input type="checkbox" id="Checkbox" value="Checkbox"/><span style="margin-left:5px;">记住我</span>
                          <span style="float:right"><a>忘记密码?</a></span>
                    
                    </div>
                    <div style="display:none;"><input type="text" id="method" name="method" value="verifyUserInteraction"/></div>
                </form>
            </div>
            <div id="Login_Foot">
                     <%
                        if(session.getAttribute("errorMessage")!=null)
                        {
                            
                     %>
                        <%=session.getAttribute("errorMessage") %>
                     <%
                           session.setAttribute("errorMessage","");
                        }
                     %>
            </div>
        </div>

    </div>
</body>
</html>
