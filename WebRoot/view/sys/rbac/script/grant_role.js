var basePath = path + '/sys/rbac/role';
var pk = "id";
doList(basePath + "/doList.json");

function loadGrid(url) {
	gridObj = $.fn.bsgrid.init('searchTable', {
		url : url,
		pageSizeSelect : true,
		pageSize : 15,
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
		additionalRenderPerColumn : function(record, rowIndex, colIndex, tdObj,
				trObj, options) {
			var columnModel = options.columnsModel[colIndex];
			if (columnModel.check == 'true') {
				var r = $.trim(record.id);
				if($.inArray(r, select_roles) > -1) {
					var checkboxObj = tdObj.find('input[type=checkbox]');
					$.bsgrid.adaptAttrOrProp(checkboxObj, 'checked', true);
				}
			}
		},
	});
}

function doSave(obj) {
	var rows = getCheckedRows();
	if (rows.length < 1) {
		alertWarning("请勾选您要分配的角色！");
		return;
	} else {
		var rids = [];
		jQuery.each(rows, function(i, n) {
			rids.push(n.id);
		});
		// 提交到后台保存
		var url = path + '/sys/rbac/user/doGrantRole.json';
		jQuery.ajax({
			type : 'POST',
			url : url + "?userid=" + userId + "&rids=" + rids.join(","),
			data : {},
			success : function(result) {
				if (result.status == '1') {
					obj.closeAndRef("分配成功！");
				} else {
					alertError('操作失败，请稍后再试！');
				}
			},
			dataType : "json"
		});
	}
}
