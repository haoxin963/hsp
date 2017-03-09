var basePath = path+'/sys/weixin/weixinApp';
var pk = "id";
doList(basePath + "/doList.json");

function doLoad(action, id) {
	var url = basePath + "/doLoad.do";
	if (action == "edit") {
		var ids = [];
		if (typeof (id) == "undefined" || id == '') {
			ids = getCheckedIds();
		} else {
			ids.push(id);
		}
		if (ids.length>1) {
			alertError('只能选择一个数据!');
			return;
		}else if(ids.length<1){
			alertError('请选择一个数据!');
			return;
		}else{
			url +="?id="+ids[0];
		}
		showDialog("编辑", url,'','','view');
	}else{
		showDialog("新建", url,'','','view');
	}
	
}

function operate(row, rowIndex, colIndex, options) {
	var id = getRowVal(row, "id");
	var str = getButton("edit", "doLoad('edit','"+ id + "')","&#xe6df;");
	str += getButton("delete", "doDelete('"+ id + "')","&#xe6e2;");
	return str;
}





function doDelete(id){ 
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
};

$(document).keypress(function(event){
	if(event.keyCode==13){
		doList();
	}
});