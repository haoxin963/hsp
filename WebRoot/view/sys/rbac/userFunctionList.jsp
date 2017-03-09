<%@page language="java" import="java.util.*" pageEncoding="UTF-8"
	trimDirectiveWhitespaces="true"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="f"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>用户权限视图</title>
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
	var userid = "${userid}";
</script>

</head>
<body>
	<div class="ui-layout-center">
		<div class="toolbarDiv" align="left">
			<a href="javascript:void(0);" iconCls="icon-refrole"
				class="easyui-linkbutton" plain="true" onclick="viewUserAuth()">查看所有权限</a>
		</div>

		<div class="searchTableDiv">
			<table id="searchTable">
				<tr>
					<th w_num="total_line" style="width:35px"></th>
					<th w_index="role" width="40%;" report="true">角色标记</th>
					<th w_index="roleName" width="50%;" report="true">角色名称</th>
				</tr>
			</table>
		</div>
	</div>
	<div class="ui-layout-east">
		<ul id="tempTree" class="ztree"></ul>
	</div>

</body>
<script type="text/javascript"
	src="${vpath}/sys/rbac/script/userFunctionList.js"></script>
<script>
	var tempTreeSetting = {
		data : {
			simpleData : {
				enable : true,
				idKey : "functionId",
				pIdKey : "parent_id"
			},
			key : {
				name : "functionName"
			}
		},
		view : {
			fontCss : setFontCss,
			selectedMulti : false
		},
		edit : {
			drag : {
				isCopy : false,
				isMove : false,
				prev : false,
				inner : false,
				next : false
			},
			enable : true,
			showRemoveBtn : false,
			showRenameBtn : false
		}
	};

	function setFontCss(treeId, treeNode) {
		return treeNode.type == "0" && treeNode.tag == "f" ? {
			"color" : "#FF0000"
		} : {};
	}

	$(document).ready(function() {
		createTree("tempTree", tempTreeSetting, "");
	});
</script>
</html>
