var basePath = path+'/sys/basedata/area';
var pk = "areaId";
doList(basePath + "/doList.json?");

function doLoad(action,id){  
	var url = basePath+'/doLoad.do';
	if (action == "edit") {
		var ids = [];
		if (id == "") {
			ids = getCheckedIds();
		} else {
			ids.push(id);
		}
		if (ids.length != 1) {
			alertError('请选择一条要操作的数据!');
			return;
		}
		url += "?areaId=" + ids[0];
		showDialog("编辑", url,'','','view');
	}else{
		var aid = document.getElementById("areaId").value;
		if(aid==""){
			alertError('请选择上级区域!');
		}
		url+= "?parentId="+$("#areaId").val();
		showDialog("新建", url,'','','view');
	}
	
	
} 


function operate(row, rowIndex, colIndex, options) {
	var id = getRowVal(row, 'areaId');
	var str = getButton("edit", "doLoad('edit','"+ id + "')","&#xe6df;");
	return str;
};


function operate(row, rowIndex, colIndex, options) {
	var id = getRowVal(row, 'areaId');
}

function doDelete(id){ 
	var ids = [];
	var rows = getCheckedRows();
	jQuery.each(rows, function(i, n) {
		ids.push(n.areaId);
	});
	if (ids.length > 0) {
		jQuery.ajax({
			type : 'POST',
			url : basePath+"/doDelete.json?ids="+ids.join(","),
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
};


$(document).keypress(function(event){
	if(event.keyCode==13){
		doList();
	}
});
