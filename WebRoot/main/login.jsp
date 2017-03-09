<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="net.hsp.web.util.HttpSessionFactory"%>
<%@page import="net.hsp.dao.DynamicDataSource"%>
<%@page import="net.hsp.web.util.ActionUtil"%>
<%
	pageContext.setAttribute("station",
			HttpSessionFactory.getCurrentStationInfo());
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>${station.statName}</title>
<link rel="stylesheet" type="text/css"
	href="${path}/main/login/css/login.css" />
<jsp:include page="/resource/inc/incMeta.jsp" />
<jsp:include page="/resource/inc/incCssJs.jsp" />
<script>
	var path = "${path}";
	var CUSERID = "${CurrentSystemInstance}";
	
	$(function(){
		$("#password").focus(function(){
			$(".handdiv").toggleClass("hiden");
		})
		$("#password").blur(function(){
			$(".handdiv").toggleClass("hiden");
		})
	})
	
</script>
<script type="text/javascript" src="${path}/main/script/login2.js"></script>
<script type="text/javascript" src="${path}/main/script/cloud.js"></script>
</head>

<body
	style="background-color:#1c77ac; background-image:url(${path}/main/login/images/light.png); background-repeat:no-repeat; background-position:center top; overflow:hidden;">
	<div id="mainBody">
		<div id="cloud1" class="cloud"></div>
		<div id="cloud2" class="cloud"></div>
	</div>
	<form class="layui-form" action="${path}/sys/rbac/user/anon/doLogin.json"
		id="loginForm">
		<div class="loginbody">
			<span class="systemlogo"
				style="text-align: center;font-size: 30px;color: #fff">${station.statName}</span>
			<div class="loginbox">
			<div class="tou"></div>
			<div class="initial_left_hand handdiv "></div>
			<div class="initial_right_hand handdiv"></div>
			
			<div class="left_hand hiden handdiv" ></div>
			<div class="right_hand hiden handdiv"></div>
				<ul>
					<li class="po_re"><i class="icon Hui-iconfont logini" >&#xe60d;</i><input name="username" id="username" type="text"
						autocomplete="off" style="width:320px" placeholder="用户名"
						class="layui-input" lay-verify="required" /></li>
					<li class="po_re"><i class="icon Hui-iconfont logini">&#xe63f;</i><input name="password" id="password" type="password"
						class="layui-input" lay-verify="required" style="width:320px"
						autocomplete="off" placeholder="密码" /></li>
					<li class="rembermydom"><span class="fl fl_left"><input id="verifycode"
							autocomplete="off" lay-verify="required"
							class="layui-input" style="width:80px" name="verifycode"
							type="text" value="" placeholder="验证码" /></span> <a
						href="javascript:refreshImg();"> <img id="verifyImg"
							src="${path}/code" width="75" height="38" class="fl_left"/></a>
							<div class="fl_left div_inline">
								<input type="checkbox" name="savepwd"  checked   id="savepwd" value="1" lay-skin="switch" />
								<p class="remberpass_login">记住密码</p> 
							</div>
							
						</li>
					<li class="rembermydom"><input name="" type="button"
						class="loginbtn" value="登录" onclick="doLogin();" /></li>
				</ul>
			</div>
		</div>
	</form>
	<div class="loginbm">©2017 HSP</div>
</body>
</html>