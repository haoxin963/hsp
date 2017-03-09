//this file has not been used, has bug
var basePath = path + '/sys/rbac/role';
	var dg;
	$(function() {
		var i = 0;
		var listPath = "/viewRoleGranted.json?filter[rid]=${role.id}"
		if (typeof (dataList) != "undefined") {
			dg = $('#dg').datagrid({
				data : dataList,
				pagination : true, 
				fitColumns : constants.fitColumns,
				striped : true,
				rownumbers : true,
				loadFilter : function(r) {
					if (r.command) { 
						return r.command;
					} else {
						return r;
					}
				},
				onBeforeLoad : function() {
					if (i == 1) {
						var opts = $(this).datagrid('options');
						opts.url = listPath;
					}
					i++;
				}
			});
		} else {
			dg = $('#dg').datagrid({
				url : listPath,
				loadFilter : function(r) {
					if (r.command) {
						return r.command;
					} else {
						return r;
					}
				},
				pagination : true,
				fitColumns : constants.fitColumns,
				striped : true,
				rownumbers : true
			});
		}

	});