var basePath = path+'/sys/dic/dictionary';
var pk = "dictId";
doList(basePath + "/doList.json?dicTypeId="+dicTypeId);



function operate(row, rowIndex, colIndex, options) {
	var id = getRowVal(row, 'dictId');
	var str = getButton("edit", "doLoad('edit','"+ id + "')","&#xe6df;");
	str += getButton("delete", "doUp("+id+");return false;","<i class='Hui-iconfont'>上移</i>");
	str += getButton("delete", "doDown("+id+");return false;","<i class='Hui-iconfont'>下移</i>");
	return str;
}

function state(row, rowIndex, colIndex, options){
	  var value=getRowVal(row,'state');
	  if(value==1){
		 return "<span class='label label-success radius'>显示</span>";
	  }else{
		  return "<span class='label radius'>不显示</span> ";
	  }
}
function delTag(row, rowIndex, colIndex, options){
	  var value=getRowVal(row,'delTag');
	  if(value==1){
			 return "<span class='label label-success radius'>是</span>";
		  }else{
			  return "<span class='label radius'>否</span> ";
		  }
		
}



function doLoad(action, id) {
	var url = basePath + "/toEdit.do";
	if (action == "edit") {
		var ids = [];
		if (typeof (id) == "undefined" || id == '') {
			ids = getCheckedIds();
		} else {
			ids.push(id);
		}
		if (ids.length != 1) {
			alertError('请选择一条要操作的数据!');
			return;
		}
		url += "?dictId=" + ids[0];
	}else{
		url = basePath + '/toAdd.do?dicTypeId='+dicTypeId;
		
	}
	showDialog("编辑", url,'','','view');
}

function enabled(){
	var ids = [];
	var rows = getCheckedRows();
	jQuery.each(rows, function(i, n) {
		ids.push(n.dictId);
	});
	if (ids.length > 0) {
		jQuery.ajax({
			type : 'POST',
			url : basePath+'/enabled.json?dictIds='+ids.join(","),
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

function disableddic(){
	var ids = [];
	var rows = getCheckedRows();
	jQuery.each(rows, function(i, n) {
		ids.push(n.dictId);
	});
	if (ids.length > 0) {
		jQuery.ajax({
			type : 'POST',
			url : basePath+'/disabled.json?dictIds='+ids.join(","),
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
function doUp(dictId){
	jQuery.ajax({
		type : 'POST',
		url : basePath+'/doUp.json?dictId='+dictId,
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
}

function doDown(dictId){
	jQuery.ajax({
		type : 'POST',
		url : basePath+'/doDown.json?dictId='+dictId,
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
}

function doDelete(){
	var ids = [];
	var rows = getCheckedRows();
	jQuery.each(rows, function(i, n) {
		ids.push(n.dictId);
	});
	if (ids.length > 0) {
		jQuery.ajax({
			type : 'POST',
			url : basePath+'/doDel.json?dictIds='+ids.join(","),
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
