<%@page import="net.hsp.entity.sys.rbac.User"%>
<%@page language="java" import="java.util.*" pageEncoding="UTF-8"
	trimDirectiveWhitespaces="true"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="f"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>分配角色视图</title>
<jsp:include page="/resource/inc/incMeta.jsp" />
<jsp:include page="/resource/inc/incCssJs.jsp" />
</head>
<body class="bodyLayout">
	<div id="tb" class="searchTb" style="line-height: 35px">
		<b>当前选中的角色 ${separator}</b>${role.roleName}
	</div>

	<div class="pd-10" style="background-color: #ffffff">
		<div class="text-l">
			<form id="searchForm" method="post" class="searchForm">
				<input type="text" style="display: none" name="exportHead"
					id="exportHead" /> <input type="text" style="display: none"
					name="exportTitle" id="exportTitle" /> 用户名${separator} <input
					type='text' class="input-text" name="filter[userName]"
					style="width:120px"> 真实姓名${separator} <input type='text'
					class="input-text" style="width:120px" name="filter[trueName]">
				<jsp:include page="/resource/inc/searchBtn.jsp"></jsp:include>
			</form>
		</div>
	</div>

	<div class="searchTableDiv">
		<table id="searchTable">
			<tr>
				<th w_num="total_line" style="width:35px"></th>
				<th w_check="true" w_index="id" style="width:30px"></th>
				<th w_index="userName" width="40%;" report="true">用户名</th>
				<th w_index="trueName" width="40%;" report="true">真实姓名</th>
			</tr>
		</table>
	</div>

	<script type="text/javascript">
		var select_users = [ ${roledUserIds} ];
		var basePath = path + '/sys/rbac/role';
		var pk = "id";
		doList(basePath + "/toGiveRole.json?roleId=${role.id}");

		function loadGrid(url) {
			gridObj = $.fn.bsgrid.init('searchTable', {
				url : url,
				pageSizeSelect : true,
				pageSize : 5,
				rowHoverColor : true,
				stripeRows : true,
				multiSort : true,
				lineWrap : true,
				autoLoad : false,
				pageIncorrectTurnAlert : false,
				displayBlankRows : false,
				displayPagingToolbarOnlyMultiPages : true,
				pagingLittleToolbar : true,
				extend : {
					settings : {
						supportColumnMove : true
					}
				},
				additionalRenderPerColumn : function(record, rowIndex,
						colIndex, tdObj, trObj, options) {
					var columnModel = options.columnsModel[colIndex];
					if (columnModel.check == 'true') {
						var r = $.trim(record.id);
						if ($.inArray(r, select_users) > -1) {
							var checkboxObj = tdObj
									.find('input[type=checkbox]');
							$.bsgrid.adaptAttrOrProp(checkboxObj, 'checked',
									true);
						}
					}
				},
			});
		}

		function checkboxEvent(id, ischecked) {
			if (ischecked == false) {//删除
				if ($.inArray(id + "", select_users) > -1) {
					select_users.splice($.inArray(id + "", select_users), 1);
				}
			} else {//添加
				if ($.inArray(id + "", select_users) == -1) {
					select_users.push(id + "");
				}
			}
		}

		function doSave(obj) {
			//提交到后台保存
			var url = path + '/sys/rbac/role/updateRoleToAllUsers.json';
			jQuery.ajax({
				type : 'POST',
				url : url + "?roleId=${role.id}",
				data : {
					selectUserIds : select_users.toString()
				},
				success : function(result) {
					if (result.status == '1') {
						obj.closeAndRef();
					} else {
						alertError("操作失败，请稍后再试！");
					}
				},
				dataType : "json"
			});

		}
	</script>

</body>
</html>
