<%@page language="java" import="java.util.*" pageEncoding="UTF-8"
	trimDirectiveWhitespaces="true"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="f"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>权限表列表</title>
<jsp:include page="/resource/inc/incMeta.jsp" />
<jsp:include page="/resource/inc/incCssJs.jsp" />
<script type="text/javascript"
	src="${path}/resource/layout/jquery-ui.js"></script>
<script type="text/javascript"
	src="${path}/resource/layout/jquery.layout.js"></script>
<script>
	$(document).ready(function() {
		$("body").layout({
			applyDefaultStyles : true,
			spacing_open : 3
		});
	});
</script>
</head>

<body>
	<div class="ui-layout-west">
		<jsp:include page="/resource/inc/incSimpleTree.jsp">
			<jsp:param name="serviceBeanId" value="functionServiceImpl" />
			<jsp:param name="callBack" value="expendFirstLevel" />
		</jsp:include>
	</div>
	<div class="ui-layout-center" style="padding:0px">
		<div class="cl pd-5 bg-1 toolbarDiv" id="toolbar">
			<span class="l"> <jsp:include page="/resource/inc/toolbar.jsp">
					<jsp:param name="texts" value="新建"></jsp:param>
					<jsp:param name="texts" value="删除"></jsp:param>
					<jsp:param name="texts" value="编辑"></jsp:param>
					<jsp:param name="texts" value="导出"></jsp:param>
					<jsp:param name="funs" value="doLoad('add','')"></jsp:param>
					<jsp:param name="funs" value="doDelete('')"></jsp:param>
					<jsp:param name="funs" value="doLoad('edit','')"></jsp:param>
					<jsp:param name="funs" value="report()"></jsp:param>
					<jsp:param name="ids" value="id1"></jsp:param>
					<jsp:param name="ids" value="id2"></jsp:param>
					<jsp:param name="ids" value="id3"></jsp:param>
					<jsp:param name="ids" value="id4"></jsp:param>
					<jsp:param name="icons" value="&#xe600;"></jsp:param>
					<jsp:param name="icons" value="&#xe6e2"></jsp:param>
					<jsp:param name="icons" value="&#xe6df;"></jsp:param>
					<jsp:param name="icons" value="&#xe644;"></jsp:param>
				</jsp:include>
			</span>
		</div>
		<c:if test="${searchType ne 'advance' }">
		<div class="pd-10" style="background-color: #ffffff">
			<div class="text-l">
				<form id="searchForm" method="post" class="searchForm">
					<input type="text" style="display: none" name="exportHead"  id="exportHead" /> 
					<input type="text" style="display: none" name="exportTitle" id="exportTitle" /> 
					<input type="text" style="display: none" name="exportField" id="exportField" /> 
					<input type="text" style="display: none" name="filter[areaId]"  id="areaId" /> 
					区域名称: <input type='text' class="input-text"  style="width:120px" name="filter[areaName]">
					<jsp:include page="/resource/inc/searchBtn.jsp"></jsp:include>
				</form>
			</div>
		</div>
		</c:if>
		<c:if test="${searchType eq 'advance' }">
			<jsp:include page="/resource/inc/searchBtn.jsp">
				<jsp:param name="hidenFields"
					value="[{'name':'filter[areaId]','id':'areaId'}]"></jsp:param>
				<jsp:param name="baseSearch"
					value="[{'field':'areaName','text':'区域名称'}]"></jsp:param>
				<jsp:param name="advanceSearch"
					value="{'items':[{'field':'areaName','text':'区域名称','type':'string'}]}"></jsp:param>
			</jsp:include>
		</c:if>


		<div class="searchTableDiv">
			<table id="searchTable">
				<tr>
					<th w_num="total_line" style="width:35px"></th>
					<th w_check="true" w_index="areaId" style="width:30px"></th>
					<th w_index="areaCode"  width="30%;"  report="true">编号</th>
					<th w_index="areaName" width="30%;" report="true">区域名称</th>
					<th w_render="operate" width="15%;">操作</th>
				</tr>

			</table>
		</div>
	</div>
</body>
<script type="text/javascript"
	src="${vpath}/sys/basedata/script/area.js"></script>
<script>
var zTree;
var setting = {
	data: {
		simpleData: {
			enable: true,
			pIdKey:"parentId"
		}
	},
	async: {
		enable: true,
		url:"${path}/system/pubmodule/doTree.do",
		autoParam:["id=filter[id]", "name=filter[name]", "level=filter[level]"],
		otherParam:{"filter[serviceBeanId]":"areaServiceImpl"}
	},
	callback: { 
        onClick: onTreeClickBack  
    }  
};

function onTreeClickBack(){ 
	var nodes = zTree.getSelectedNodes();
	if(nodes.length>0){ 
		document.getElementById("areaId").value = nodes[0].id;
		doList();
	}
};

$(document).ready(function(){
	zTree = $.fn.zTree.init($("#tree"), setting);
}); 
</script>
</html>
