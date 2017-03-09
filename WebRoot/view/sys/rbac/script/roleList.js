var basePath = path + '/sys/rbac/role';
var pk = "id";
doList(basePath + "/doList.json");

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
	showDialog("角色", url, '', '', 'view');
}

function operate(row, rowIndex, colIndex, options) {
	var id = getRowVal(row, 'id');
	var str = getButton("edit", "doLoad('edit','" + id + "')", "&#xe6df;");
	str += getButton("delete", "doDelete('" + id + "')", "&#xe6e2;");
	return str;
}

function refFunc() {
	var actionURL = basePath + '/toGrantFunc.do';
	var ids = getCheckedIds();
	if (ids.length != 1) {
		alertError('请选择1条要操作的数据!');
		return;
	}
	actionURL += "?rid=" + ids[0];
	showDialog("角色模块配置", actionURL, 800, 500, 'refFunc');
}

function roleToUser() {
	var actionURL = basePath + '/roleToUser.do';
	var ids = getCheckedIds();
	if (ids.length != 1) {
		alertError('请选择1条要操作的数据!');
		return;
	}
	actionURL += "?roleId=" + ids[0];
	showDialog("角色用户关系", actionURL, 800, 600, 'roleToUser');
}

$(document).keypress(function(event){
	if(event.keyCode==13){
		doList();
	}
});
