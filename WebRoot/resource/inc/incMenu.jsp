<%@ page language="java" contentType="text/html; charset=UTF-8"  trimDirectiveWhitespaces="true"   pageEncoding="UTF-8"%>
<%@page import="net.hsp.web.util.SpringCtx"%> 
<%@page import="net.hsp.service.sys.rbac.FunctionService"%>
<%----
	使用说明：
	
	方案一：
	 需要传递参数menuObjectType = json 
		1.导入本页面
			 <jsp:include page="/com/inc/incMenu.jsp">
			 	<jsp:param name="menuObjectType" value="json" />
			 </jsp:include>
	    
	    2.在需要使用菜单数据的页面可以直接使用两个json对象：
	      //一级菜单的列表数据
	      var topLevelMenuJsonObj =
		      	[{
		      		funcId,
					funcName,
					parentId,
					linkAddr,
					buttonId,
					sortNo,
					picAddr,
					tag,	      	
		      	},
		      	........
		      	];      
	      
	      
	      //所有菜单的树形结构json（包含一级菜单）
	      var allMenuJsonObj = 
	         	[{
		      		funcId,
					funcName,
					parentId,
					linkAddr,
					children, //包含的子菜单集合 
					buttonId,
					sortNo,
					picAddr,
					tag,	      	
		      	},
		      	........
		      	];      
	     

-----%>
<%
	request.setCharacterEncoding("utf-8");
	response.setCharacterEncoding("utf-8");	
	
	String userId = String.valueOf(session.getAttribute("userId"));
	String custId = (String)session.getAttribute("CurrentSystemInstance"); 
	String basePath = request.getContextPath();
	FunctionService funcService =  (FunctionService)SpringCtx.getSpringContext().getBean("functionServiceImpl");
	
	//菜单对象的类型（）
	String menuObjectType = request.getParameter("menuObjectType");
	menuObjectType = menuObjectType!=null&&!"".equals(menuObjectType)?menuObjectType:"json";
	
	////////////////////方案一：生成json一个简单的 json数据文件到客户端
	if("json".equals(menuObjectType)){
		//1.判断文件是否存在。（每次用户登录，生成这个文件）
		if(funcService.checkUserMenuJsonFileExist(userId,custId)){
			//存在，直接引入文件
			%>
			<jsp:include page="/page/${sessionScope.CurrentSystemInstance}/menu/menu_json_${sessionScope.userId}.html"></jsp:include>
			<%			
		}else{
			//不存在，生成后再引入
			funcService.generateMenuJsonFile(userId,custId,basePath);
			%>
			<jsp:include page="/page/${sessionScope.CurrentSystemInstance}/menu/menu_json_${sessionScope.userId}.html"></jsp:include>
			<%
		}		
	}else{
		//其他参数值
	}
	
	/////////////////////方案二：得到一个MenuObj的Java对象	
	
%> 
	