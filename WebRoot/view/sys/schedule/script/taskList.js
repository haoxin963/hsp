var basePath = path+'/sys/schedule';
var pk = "id";
doList(basePath + "/listTask.json");

	

function toAdd(){ 
	var actionURL = basePath + '/toAddTask.do';
	showDialog("新建", actionURL,'','','view');
} 

function operate(row, rowIndex, colIndex, options) {
	var id = getRowVal(row, "taskClass");
	var str = getButton("delete", "doDelete('"+ id + "')","&#xe6e2;");
	return str;
}


function doDelete(taskClass){ 
	if (taskClass != null && taskClass != ""){
	parent.layer.confirm('确定要删除此数据?', function(index) {
		function success(result) {
			if (result.status == '1') {
				gridObj.refreshPage();
				parent.layer.close(index);
				alertOk("删除成功！");
			} else {
				alertError('删除失败!');
			}
		}
		jQuery.ajax({
			type : 'POST',
			url : basePath + "/deleteTask.json",
			traditional : true,
			data : {
				"taskClass" : taskClass
			},
			success : success,
			dataType : "json"
		});
	});}
};

