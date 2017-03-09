var basePath = path + '/sys/rbac/user';
var pk = "id";
doList(basePath + "/toViewUserAuth.json?userid="+userid);

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
		event : {
			selectRowEvent : function(record, rowIndex, trObj, options) {
				$.fn.zTree.destroy("tempTree");
				createTree("tempTree", tempTreeSetting, record.id);
			}
		}
	});
}


function viewUserAuth() {
	$.fn.zTree.destroy("tempTree");
	createTree("tempTree", tempTreeSetting, "");
}

// 创建树
function createTree(treeId, setting, roleId) {
	var treeUrl = basePath + "/buildRoleMenuTree.do";
	var zNodes;
	$.ajax({
		url : treeUrl,
		data : {
			"roleId" : roleId,
			"userid" : userid
		},
		type : 'POST',
		dataType : "json",
		success : function(result) {
			var treeObj = $.fn.zTree
					.init($("#" + treeId + ""), setting, result);
			var rootNode = treeObj.getNodeByParam("functionId", "0001");
			treeObj.expandAll(true);
		},
		error : function(msg) {

		}
	});
}
