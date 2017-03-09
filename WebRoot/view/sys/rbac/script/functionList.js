var basePath = path+'/sys/rbac/function';
var pk = "functionId";
doList(basePath + "/doList.json");

function doLoad(action, id) {
	var url = basePath + "/doLoad.do";
	if (action == "edit") {
		var ids = [];
		if (typeof (id) == "undefined" || id=='') {
			ids = getCheckedIds();
		} else {
			ids.push(id);
		}
		if (ids.length != 1) {
			alertError('请选择一条要操作的数据!');
			return;
		}
		url += "?functionId=" + ids[0];
	}else{
		//获取树节点当前选中的节点。
		var treeObj = $.fn.zTree.getZTreeObj("tree");
		var nodes = treeObj.getSelectedNodes();
		if(nodes.length==0){
			alertError('请先在左侧选中您要增加菜单的上级菜单!');
			return;
		}
		url+="?functionId="+nodes[0].functionId+"&functionName="+nodes[0].functionName;	
	}
	showDialog("菜单", url,'','','view');
}


function operate(row, rowIndex, colIndex, options) {
	var id = getRowVal(row, "functionId");
	var str = getButton("edit", "doLoad('edit','"+ id + "')","&#xe6df;");
	str += getButton("delete", "doDelete('"+ id + "')","&#xe6e2;");
	return str;
}

function iconformatter(row, rowIndex, colIndex, options) {
	var icon = getRowVal(row, 'pictureAddr');
	var str = "<i class='Hui-iconfont'>"+icon+"</i>";
	return str;

}

function display(row, rowIndex, colIndex, options) {
	var disabled = getRowVal(row, 'type');
	if (disabled == "1") {
		return "<span class='label label-success radius'>显示</span>";
	} else {
		return "<span class='label label-danger radius'>隐藏</span>";
	}
}


function doDelete(id) {
	var ids = [];
	if (typeof (id) == "undefined" || id == '') {
		ids = getCheckedIds();
	} else {
		ids.push(id);
	}
	if (ids.length < 1) {
		alertError('请选择要删除的记录!');
		return;
	}
	parent.layer.confirm('确定要删除此数据?', function(index) {
		function success(result) {
			if (result.status == '1') {
				gridObj.refreshPage();
				parent.layer.close(index);
				zTree.destroy();
				createTree();
				alertOk("删除成功！");
			} else {
				alertError('删除失败!');
			}
		}
		jQuery.ajax({
			type : 'POST',
			url : basePath + "/doDelete.json",
			traditional : true,
			data : {
				"ids" : ids
			},
			success : success,
			dataType : "json"
		});
	});
}

function closeAndRef(msg) {
	if(msg == null || msg == undefined) {
		msg="操作成功!";
	}
	parent.layer.close(dialog)
	gridObj.refreshPage();
	zTree.destroy();
	createTree();
	alertOk(msg);
}

$(document).keypress(function(event){
	if(event.keyCode==13){
		doList();
	}
});
