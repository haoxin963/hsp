var basePath = path+"/sys/tool/toolcolor";
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
	showDialog("用户自定义皮肤", url,'','','view');
}


function doCreat(){
	var data = {   }	   
        $.ajax({
            type: 'POST',
            url: basePath+"/doCreat.json",
            data: data ,
         success:  function(res){
            alert("生成完毕！");
            parent.location.reload();
	     }       	 	 
        } );   
	
}




/**
 * 启用/禁用用户
 */
