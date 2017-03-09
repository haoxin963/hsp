<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"
	trimDirectiveWhitespaces="true"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page import="javax.servlet.http.Cookie"%>
<%@page import="net.hsp.web.util.HttpSessionFactory"%>
<%
	pageContext.setAttribute("station",
			HttpSessionFactory.getCurrentStationInfo());
	String custId_ = (String) session
			.getAttribute("CurrentSystemInstance");
	if (custId_ == null) {
		Cookie[] cs = request.getCookies();
		if (cs != null) {
			for (int i = 0; i < cs.length; i++) {
				if ("custId".equals(cs[i].getName())) {
					custId_ = cs[i].getValue();
				}
			}
		}
		if (custId_ != null) {
			response.sendRedirect(request.getContextPath() + "/"
					+ custId_ + ".login");
			return;
		} else {
			response.sendRedirect(request.getContextPath()
					+ "/com/error/401.jsp");
			return;
		}
	} else {
		if (session.getAttribute("userId") == null) {
			response.sendRedirect(request.getContextPath() + "/"
					+ custId_ + ".login");
			return;
		}
	}
	//当前用户对象	 
	pageContext.setAttribute("currentUser", HttpSessionFactory
			.getInstance().getUserInfoByUserId(request));

	String indexPage = "/page/" + custId_ + "/index.jsp";
	if (new java.io.File(session.getServletContext().getRealPath(
			indexPage)).exists()) {
		response.sendRedirect(request.getContextPath() + indexPage);
		return;
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="utf-8">
<head id="Head1">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="renderer" content="webkit|ie-comp|ie-stand" />
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<meta name="viewport"
	content="width=device-width,initial-scale=1,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no" />
<meta http-equiv="Cache-Control" content="no-siteapp" />
<LINK rel="Bookmark" href="/favicon.ico" />
<LINK rel="Shortcut Icon" href="/favicon.ico" />
<title>${station.statName}</title>
<!--[if lt IE 9]>
<script type="text/javascript" src="${path}/resource/html5.js"></script>
<script type="text/javascript" src="${path}/resource/respond.min.js"></script>
<script type="text/javascript" src="${path}/resource/PIE_IE678.js"></script>
<![endif]-->
<link rel="stylesheet" type="text/css"
	href="${path}/resource/h-ui/css/H-ui.min.css" />
<link rel="stylesheet" type="text/css"
	href="${path}/resource/Hui-iconfont/1.0.7/iconfont.css" />
<link rel="stylesheet" type="text/css"
	href="${path}/resource/h-ui/skin/default/skin.css" id="skin" />
<script type="text/javascript"
		src="${path}/resource/jquery/1.9.1/jquery.min.js"></script>
<jsp:include page="/resource/inc/incMenu.jsp">
	<jsp:param name="menuObjectType" value="json" />
</jsp:include>
<!--[if IE 6]>
<script type="text/javascript" src="http://lib.h-ui.net/DD_belatedPNG_0.0.8a-min.js" ></script>
<script>DD_belatedPNG.fix('*');</script>
<![endif]-->
<script>
	var path = "${path}";
</script>
	<script type="text/javascript">
	var menuHtml="";
		$(function() {
			//使用方法 则是将initMenu数字改变一下，initMenu2是两级，initMenu3是多级
			
			initMenumore(allMenuJsonObj);
			$("#menusdiv").append(menuHtml);
			
			$("#menusdiv").find("ul").eq(0).attr("class","dropDown-menu menu radius box-shadow");
		});
	
		function initMenu3(allMenuJsonObj){
			if (allMenuJsonObj.length > 0) {
				menuHtml+="<ul class='menu'>";
				for(var i=0;i<allMenuJsonObj.length;i++){
					var functionName = allMenuJsonObj[i]["funcName"];
					var linkAddress =path + allMenuJsonObj[i]["linkAddr"];
					if(allMenuJsonObj[i]["linkAddr"]==""){
						menuHtml= menuHtml+"<li><a href=\"javascript:void(0)\">"+ functionName + "<i class='arrow Hui-iconfont'></i></a>";
					}else{
						menuHtml= menuHtml+"<li><a data-href=\""+linkAddress+"\" data-title=\""+functionName+"\" href=\"javascript:void(0)\">"+ functionName + "</a>";
					}
					
					var childrens =allMenuJsonObj[i]["children"];
					if(childrens.length>0){
 						initMenu3(childrens);
					}
					else{
						menuHtml+="</li>";	
					}
				}
				menuHtml+="</ul>";	
			}
		}
		
		function initMenumore(allMenuJsonObj){
			if (allMenuJsonObj.length > 0) {
				menuHtml+="<ul class='menu'>";
				for(var i=0;i<allMenuJsonObj.length;i++){
					var functionName = allMenuJsonObj[i]["funcName"];
					var linkAddress =path + allMenuJsonObj[i]["linkAddr"];
					var picAddr =allMenuJsonObj[i]["picAddr"];
					if (typeof (picAddr) == "undefined" || picAddr=='') {
						picAddr = "&#xe616;";
					}
					if(allMenuJsonObj[i]["linkAddr"]==""){
						menuHtml= menuHtml+"<li><a href=\"javascript:void(0)\" class='inactive'><i class=\"Hui-iconfont\">"+picAddr+"</i> "+ functionName + "<i class=\"Hui-iconfont menu_dropdown-arrow\">&#xe6d5;</i></a>";
					}else{
						menuHtml= menuHtml+"<li><a data-href=\""+linkAddress+"\" data-title=\""+functionName+"\" href=\"javascript:void(0)\">"+ functionName + "</a>";
					}
					
					var childrens =allMenuJsonObj[i]["children"];
					if(childrens.length>0){
 						initMenumore(childrens);
					}
					else{
						menuHtml+="</li>";	
					}
				}
				menuHtml+="</ul>";	
			}
		}
		
		function initMenu2(){
			var menuhtml="";
			for (var i = 0; i < allMenuJsonObj.length; i++) {
				var fid = allMenuJsonObj[i]["funcId"];
				var funcName = allMenuJsonObj[i]["funcName"];
				var str = "<dl id=\"menu-"+fid+"\"><dt><i class=\"Hui-iconfont\">&#xe616;</i> "+funcName+"<i class=\"Hui-iconfont menu_dropdown-arrow\">&#xe6d5;</i></dt><dd><ul>"
				var menuchildren = allMenuJsonObj[i]["children"];
				if (menuchildren.length > 0) {
					for(var j=0;j<menuchildren.length;j++){
						var functionNamesec = menuchildren[j]["funcName"];
						var linkAddresssec =path + menuchildren[j]["linkAddr"];
						str+="<li><a data-href=\""+linkAddresssec+"\" data-title=\""+functionNamesec+"\" href=\"javascript:void(0)\">"+functionNamesec+"</a></li>";
					}
				}
				str +="</ul></dd></dl>"
				menuhtml +=str;
			}
			$("#menusdiv").append(menuhtml);
		}
			

			$(function(){
				var hrefStr=$("#skin").attr("href");
				var hrefRes=hrefStr.substring(0,hrefStr.lastIndexOf('skin/'))+'skin/';
				  $.ajax({
				             type: 'POST',
				             url: '${path}/sys/tool/toolcolor/loadCss.json',
				          
				             success:  function(res){
				             result = jQuery.parseJSON(res);
				             $(window.frames.document).contents().find("#skin").attr("href",hrefRes+result.cssname);
				             }    
				  });
				         
				$("#Hui-skin .dropDown-menu a").click(function(){
				var v = $(this).attr("data-val");
				//外面的js皮肤，参考
				var data = {
				                 filepath:v
				             }
				  $.ajax({
				             type: 'POST',
				             url: '${path}/sys/tool/toolcolor/loadCss.json',
				             data: data ,
				             success:  function(res){
				             result = jQuery.parseJSON(res);
				             $(window.frames.document).contents().find("#skin").attr("href",hrefRes+result.cssname);
				             location.reload();
				             } 
				  }); 
				});
				
			})
		
	</script>
</head>
<body class="easyui-layout" style="overflow-y: hidden" fit="true"
	scroll="no">
	<noscript>
		<div
			style="position: absolute; z-index: 100000; height: 2046px; top: 0px; left: 0px; width: 100%; background: white; text-align: center;">
			<img src="css/images/noscript.gif" alt='抱歉，请开启脚本支持！' />
		</div>
	</noscript>

	<header class="navbar-wrapper">
	<div class="navbar navbar-fixed-top">
		<div class="container-fluid cl">
			<a class="logo navbar-logo f-l mr-10 hidden-xs" href=""><span
				id="title" style="font-size:25px;margin-left:30px"></span> <img
				src="${path}/fileServer?moduleName=/logo&fileName=logo2.gif&tt=<%=Math.random() %>"
				onerror="this.style.display='none';document.getElementById('title').innerHTML=document.title;" /></a>
			<nav class="nav navbar-nav"> </nav>
			<nav id="Hui-userbar" class="nav navbar-nav navbar-userbar hidden-xs">
			<ul class="cl">
				<li><img data-href='${path}/sys/userSettings/doLoad.do' data-title='个人信息' onclick="Hui_admin_tab(this)"
				style="width:28px;border-radius:50%;cursor:pointer;margin-top:7px;"
				src="${path}/sys/userSettings/viewPhoto.json"
				onerror="this.src='${vpath }/sys/userSettings/images/photobg.jpg';this.onerror=null;;" /></li>
				<li class="dropDown dropDown_hover"><a href="#"
					class="dropDown_A">${sessionScope.userName} <i
						class="Hui-iconfont">&#xe6d5;</i></a>
					<ul class="dropDown-menu menu radius box-shadow">
						<li><a data-href='${path}/main/modifypwd.jsp' data-title='修改密码'
							href="javascript:void(0);"  onclick="Hui_admin_tab(this)">修改密码</a></li>
						<li><a href="javascript:void(0)"
							onclick="if(window.confirm('确认退出?')){window.location='${path}/main/exit.jsp';}"
							class="easyui-linkbutton"
							data-options="plain:true,iconCls:'icon-search'">退出</a></li>
					</ul></li>
				<li id="Hui-msg"><a
					href="javascript:ckId_not_menu_click('消息查看','${path}/sys/message/doStatusList.do?filter[status]=1');"
					title="消息"><span class="badge badge-danger">1</span><i
						class="Hui-iconfont" style="font-size:18px">&#xe68a;</i></a></li>
				<li id="Hui-skin" class="dropDown right dropDown_hover"><a
					href="javascript:;" class="dropDown_A" title="换肤"><i
						class="Hui-iconfont" style="font-size:18px">&#xe62a;</i></a>
					<ul class="dropDown-menu menu radius box-shadow">
						<li><a href="javascript:;" data-val="default" title="默认（黑色）">默认（黑色）</a></li>
						<li><a href="javascript:;" data-val="blue" title="蓝色">蓝色</a></li>
						<li><a href="javascript:;" data-val="green" title="绿色">绿色</a></li>
						<li><a href="javascript:;" data-val="red" title="红色">红色</a></li>
						<li><a href="javascript:;" data-val="yellow" title="黄色">黄色</a></li>
						<li><a href="javascript:;" data-val="orange" title="绿色">橙色</a></li>
						<li><a href="javascript:;" data-val="zdy" title="自定义">自定义</a></li>
					</ul></li>
			</ul>
			</nav>
		</div>
	</div>
	</header>

	<aside class="Hui-aside">
	<div class="menu_dropdown bk_2" id="menusdiv">
	</div>
	</aside>
	<div class="dislpayArrow hidden-xs">
		<a class="pngfix" href="javascript:void(0);"
			onClick="displaynavbar(this)"></a>
	</div>
	<section class="Hui-article-box">
	<div id="Hui-tabNav" class="Hui-tabNav hidden-xs">
		<div class="Hui-tabNav-wp">
			<ul id="min_title_list" class="acrossTab cl">
				<li class="active"><span title="我的桌面" data-href="welcome.html"><span class="Hui-iconfont">&#xe625;</span>首页</span><em></em></li>
			</ul>
		</div>
		<div class="Hui-tabNav-more btn-group">
			<a id="js-tabNav-prev" class="btn radius btn-default size-S"
				href="javascript:;"><i class="Hui-iconfont">&#xe6d4;</i></a><a
				id="js-tabNav-next" class="btn radius btn-default size-S"
				href="javascript:;"><i class="Hui-iconfont">&#xe6d7;</i></a>
		</div>
	</div>
	<div id="iframe_box" class="Hui-article">
		<div class="show_iframe">
			<div style="display:none" class="loading"></div>
			<iframe id="iframe_son" scrolling="yes" frameborder="0" src="${path}/main/desktop.jsp" ></iframe>
		</div>
	</div>
	</section>

	<script type="text/javascript"
		src="${path}/resource/layer/3.0.1/layer.js"></script>
	<script type="text/javascript" src="${path}/resource/h-ui/js/H-ui.js"></script>
</body>
</html>
