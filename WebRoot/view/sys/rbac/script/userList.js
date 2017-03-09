var basePath = path+"/sys/rbac/user";
var pk = "id";
doList(basePath + "/doList.json");

function disabled(row, rowIndex, colIndex, options) {
	var disabled = getRowVal(row, 'status');
	if (disabled == "1") {
		return "<span class='label label-success radius'>启用</span>";
	} else {
		return "<span class='label radius'>禁用</span>";
	}
}

function operate(row, rowIndex, colIndex, options) {
	var id = getRowVal(row, "id");
	var str = getButton("edit", "doLoad('edit','"+ id + "')","&#xe6df;");
	str += getButton("delete", "doDelete('"+ id + "')","&#xe6e2;");
	return str;
}

function resetPwd() {
	var ids = getCheckedIds();

	if (ids.length != 1) {
		alertError('请选择1条要操作的数据!');
		return;
	}
	$.ajax({
		url : basePath + '/doResetUserPwd.json',
		data : {
			userid : ids[0]
		},
		type : 'POST',
		async : false,
		dataType : "json",
		success : function(r) {
			if (r.status == "1") {
				alertOk('密码已经重置为：123456');
			}
		},
		error : function(msg) {
			return null;
		}
	});
}

function doLoad(action, id) {
	var url = basePath + "/doLoad.do";
	if (action == "edit") {
		var ids = [];
		if (typeof (id) == "undefined") {
			ids = getCheckedIds();
		} else {
			ids.push(id);
		}
		if (ids.length != 1) {
			alertError('请选择一条要操作的数据!');
			return;
		}
		url += "?id=" + ids[0];
	}
	showDialog("用户", url,'','','view');
}

function toGrantRole() {
	var ids = getCheckedIds();
	if (ids.length != 1) {
		alertError('请选择1条要操作的数据!');
		return;
	}
	var url = basePath + "/toGrantRole.do?userid=" + ids[0];
	showDialog("分配角色", url, 800, 500,'toGrantRole');
}

function getDirector(id) {
	var url = path + '/sys/rbac/user/doGetDirector.json';
	$.ajax({
		type : 'POST',
		url : url + "?id=" + id,
		success : function(result) {
			alert(result.user.trueName);
			if (result.status == '1') {
				alertOk('操作成功');
				refreshTable();
			} else {

			}
		},
		dataType : "json"
	});
}

function viewUserAuth() {
	var ids = getCheckedIds();
	if (ids.length != 1) {
		alertError('请选择1条要操作的数据!');
		return;
	}
	var url = basePath + "/toViewUserAuth.do?userid=" + ids[0];
	showDialog("用户权限视图", url, 650, 480,'viewUserAuth','');
}

/**
 * 启用/禁用用户
 */
function enableUser() {
	var actionURL = basePath + '/doEnableUser.do';
	// id集合
	var ids = [];
	// 状态集合
	var sts = [];
	var rows = getCheckedRows();
	jQuery.each(rows, function(i, n) {
		ids.push(n.id);
		// 反转用户当前的状态
		var st = n.status == "1" ? "0" : "1";
		sts.push(st);
	});
	if (ids.length > 0) {
		jQuery.ajax({
			type : 'POST',
			url : basePath + "/doEnableUser.json?keyids=" + ids.join(",")
					+ "&sts=" + sts.join(","),
			data : {},
			success : function(result) {
				if (result.status == '1') {
					alertOk('操作成功');
					refreshTable();
				} else {
					alertError(result.msg);
				}
			},
			dataType : "json"
		});
	} else {
		alertError("请选择1条要操作的数据!");
	}
}

$(document).keypress(function(event){
	if(event.keyCode==13){
		doList();
	}
});
