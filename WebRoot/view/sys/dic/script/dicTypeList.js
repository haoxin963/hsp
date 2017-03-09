var basePath = path+'/sys/dic/dicType';
var pk = "id";
doList(basePath + "/doList.json");



function operate(row, rowIndex, colIndex, options) {
	var id = getRowVal(row, 'dicTypeId');
	var str = getButton("edit", "view("+id+")","&#xe6df;");
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
	var url = basePath + "/toAdd.do";
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
		url += "?id=" + ids[0];
	}
	showDialog("新建", url,'','','view');
}



function view(dicTypeId){
	window.location =path+'/sys/dic/dictionary/doList.do?dicTypeId='+dicTypeId;
}

/**
 * 启用/禁用用户
 */
