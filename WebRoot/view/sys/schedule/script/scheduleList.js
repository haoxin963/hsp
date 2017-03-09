var basePath = path+'/sys/schedule';
var pk = "id";
doList(basePath + "/listSchedule.json");

function start(scheduleId){ 
	parent.layer.confirm('确认开启任务?', function(index) {
			jQuery.post(path+'/sys/schedule/start.json',{scheduleId:scheduleId},function(result){
				if(result.status == 1){
					alertOk("开启成功！");
				}else{
					alertError(result.msg);
				}
			},'json');
	});
};

function stop(scheduleId){ 
	parent.layer.confirm('确认停止任务?', function(index){
	
			jQuery.post(path+'/sys/schedule/stop.json',{scheduleId:scheduleId},function(result){
				if(result.status ==1){
					alertOk("停止成功！");
				}else{
					alertError(result.msg);
				}
			},'json');
	
	});
};


function flagformater(row, rowIndex, colIndex, options)
{
	var value = getRowVal(row, "openFlag");
	if(value == 0){
		return '停止';
	}else if(value == 1){
		return '运行中';
	}else if(value == 2){
		return '已结束';
	}
};

function operate(row, rowIndex, colIndex, options)
{
	var openFlag= getRowVal(row, "openFlag");
	var scheduleId=getRowVal(row,"scheduleId");
	var str = getButton("edit", "toEdit('"+ scheduleId+ "')","编辑");
	if(openFlag == 1){
	    str += getButton("stop", "stop('"+ scheduleId + "')","停止");
	}else{
		str += getButton("start", "start('"+ scheduleId + "')","运行");
	}
    str += getButton("delete", "doDelete('"+scheduleId + "')","删除");
	return str;
};

function doDelete(id){ 
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
			url : basePath + "/deleteSchedule.json",
			traditional : true,
			data : {
				"id" : id
			},
			success : success,
			dataType : "json"
		});
	});
};





function toAdd() {   
	var actionURL = basePath + '/toAddSchedule.do';
	showDialog("新建", actionURL,'','','view');

};


function toEdit(scheduleId) {   
	var actionURL = basePath + '/toEditSchedule.do?scheduleId='+scheduleId;
	
	showDialog("编辑", actionURL,'','','view');
};



$(document).keypress(function(event){
	if(event.keyCode==13){
		doList();
	}
});


