var basePath = path+'/sys/message';
var pk = "id";
doList(basePath + "/doStatusList.json");


function view(actionURL,id){
	showDialog("消息查看", actionURL,'','','view','');
} 





function operate(row, rowIndex, colIndex, options) {
	var id = getRowVal(row, "id");
	var str = getButton("edit", "view('"+basePath+"/messageView.do?id="+row.id+"','"+ id + "')","&#xe6df;");
	return str;
};

function statusFormater(row, rowIndex, colIndex, options)
{   
	
	var value = getRowVal(row, "status");
	if(value==1 || value == true){
		return "<span class='label radius'>未读</span>";
	}else if(value==2){
		return "<span class='label label-success radius'>已读</span>";
	}else{
		//空也表示未读
		return "<span class='label radius'>未读</span>";
	}
	
};

function rowDateFormater(row, rowIndex, colIndex, options){
	var value = getRowVal(row, "sendtime");
	return value.toString().substring(0,10);
};

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
			url : basePath + "/doDeleteMine.json",
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
