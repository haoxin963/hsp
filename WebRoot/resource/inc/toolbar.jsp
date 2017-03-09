<%@ page language="java" contentType="text/html; charset=UTF-8" import="java.util.*,net.hsp.service.sys.rbac.*" trimDirectiveWhitespaces="true"   pageEncoding="UTF-8"%>
<%@page import="net.hsp.web.util.SpringCtx"%> 
<%@page import="net.hsp.common.constants.PlatFormConstant"%>
<%!private List<String> loadButton(HttpServletRequest request){
		String custId = (String)request.getSession().getAttribute(PlatFormConstant.CURRENT_SYSINSTANCE);
		String url = (String)request.getAttribute("url");
		Object userId = request.getSession().getAttribute(PlatFormConstant.CURRENT_USERID);
		if (url!=null && url.endsWith(".do") && userId!=null) {
			Map<String,Map<Long,Map<String,List<String>>>> btns = (Map<String, Map<Long, Map<String, List<String>>>>) request.getSession().getServletContext().getAttribute("btns");
			if (btns==null) {
				btns = new HashMap<String,Map<Long,Map<String,List<String>>>>();
			}
			Map<Long,Map<String,List<String>>> c = btns.get(custId);
			if (c==null) {
				c = new HashMap<Long,Map<String,List<String>>>();
			} 
			Map<String,List<String>> menu = c.get(userId);
			if (menu==null) {
				menu = new HashMap<String,List<String>>();
			}
			List<String> btnIds = menu.get(url);
			if (btnIds==null) {
				btnIds = new ArrayList<String>();
				FunctionService service = (FunctionService) SpringCtx.getBean("functionServiceImpl"); 
				String userName = (String) request.getSession().getAttribute(PlatFormConstant.CURRENT_USERNAME);
				boolean isAdmin = false;
				if (PlatFormConstant.ADMIN_USERNAME.equals(userName)) {
					isAdmin = true;
				}
				List<Map<String,Object>> list = service.getButton(isAdmin,userId+"",url);
				if(list!=null){
					for (Map<String, Object> map : list) {
						btnIds.add(map.get("id")+""); 
					}
				}
				menu.put(url,btnIds);
				c.put(Long.parseLong(userId+""), menu);
				btns.put(custId,c);
				request.getSession().getServletContext().setAttribute("btns", btns);
			}
			return btnIds;
		} 
		return null;
	}

%>
  
<div style=" display:inline-block;">
	<%
		
		List<String> btnList = null;
		boolean isAdmin = PlatFormConstant.ADMIN_USERNAME.equals(session.getAttribute(PlatFormConstant.CURRENT_USERNAME)+"");
		String url = (String)request.getAttribute("url"); 
		if(FunctionServiceImpl.getPageButton((String)session.getAttribute(PlatFormConstant.CURRENT_SYSINSTANCE)).get(url) == null ){
			isAdmin = true;
		}else{
			btnList = loadButton(request);
		}
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		String[] texts = request.getParameterValues("texts");
		if(texts!=null){ 
			String[] ids = request.getParameterValues("ids");
			String[] icons = request.getParameterValues("icons");
			String[] functions = request.getParameterValues("funs"); 
				for (int i = 0; i < ids.length; i++) { 
					if(!isAdmin){
						if(btnList==null || btnList.isEmpty()){
							break;
						}
						if(!btnList.contains(ids[i]) ){
							continue;
						}
					}
					String dis = "";
					String plain = "plain";
					String fun = functions[i];
					String tip = texts[i];
					String icon = icons[i];
			%> 
			<a href="javascript:;" onclick="<%=fun%>" id="<%=ids[i]%>"  title="<%=tip%>" class="btn btn-primary radius"><i class="Hui-iconfont"><%=icon%></i><%=texts[i]%></a>
			<% 
			}
		}
		
		String customImport = request.getParameter("customImport");
		if(null!= customImport && !customImport.isEmpty()){
			%>
				<a href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-add'"  onclick="dataImport('<%=customImport%>',null);">导入</a>
			<%
		}
		
		String commonImport = request.getParameter("commonImport");
		if(null!= commonImport && !commonImport.isEmpty()){
			%>
				<a href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-add'"  onclick="dataImport(null,'<%=commonImport%>');">导入</a>
			<%
		}
	%> 
	<!-- <a href="#" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-print'">打印</a> -->
</div>
<a class="btn btn-success radius r" style="line-height:1.6em;float:right;" href="javascript:location.replace(location.href);" title="刷新"><i class="Hui-iconfont">&#xe68f;</i></a>
<script>
	function dataImport(type,entity){
		var url = '';
		if(null == type){
			url = 'entity='+entity;
		}else{
			url = 'type='+type;
		}
		top.ymPrompt.win({
			title:'数据导入',
			autoClose:false,
			width:550,
			height:350,
			fixPosition:true,
			maxBtn:true,
			minBtn:false,
			handler:formHandler,
			btn:[['关闭','cancel']],
			iframe:{
				id:'import',
				name:'import',
				src:path+'/sys/dataimport/toImport.do?'+url
				}
		});
	}
	var isAdmin = <%=isAdmin%>;
	var btnIds = [];
	<%
	if(btnList!=null){
		for(int i=0;i<btnList.size();i++){
			out.println("btnIds.push('"+btnList.get(i)+"');");
		}
	}
	%>
</script>