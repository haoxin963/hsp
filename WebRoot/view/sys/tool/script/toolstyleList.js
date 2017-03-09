var basePath = path+"/sys/tool/toolstyle";
var pk = "id";
doList(basePath + "/doList.json");



function operate(row, rowIndex, colIndex, options) {
	var id = getRowVal(row, 'id');
	var str = "<a style='text-decoration:none' class='toolbar' onClick=doLoad('edit','"
			+ id + "')  href='javascript:;'  name='editBtn' ><i class='Hui-iconfont'>&#xe6df;</i></a>&nbsp;";
	str += "<a style='text-decoration:none' class='toolbar' onClick=doDelete('"
			+ id + "') name='delBtn'  href='javascript:;'  ><i class='Hui-iconfont'>&#xe6e2;</i></a>";
	return str;
}

function styletype(row, rowIndex, colIndex, options){
	  var value=getRowVal(row,'styletype');
	  var str="颜色";
	  if(value==0){
		 str="颜色";
	  }else if(value==1){
		  str="图片";
	  }else if(value==2){
		  str="线条";
	  }else{
		  str="字体";
	  }
	  return str;
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
	showDialog("自定义皮肤修改属性", url,'','','view');
}





/**
 * 启用/禁用用户
 */
