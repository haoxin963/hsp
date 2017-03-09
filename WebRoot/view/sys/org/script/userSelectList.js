var basePath = path+'/sys/org/employee';
var pk = "id";
doList(basePath + "/doList.json");

 
$(document).keypress(function(event){
	if(event.keyCode==13){
		doList();
	}
});

function doSave(obj) {
		var ids = [];
		ids = getCheckedIds();
		if (ids.length>1) {
			alertError('只能选择一个值!');
			return;
		}else if(ids.length<1){
			alertError('请选择一个值!');
			return;
		}else{
			var employeeId = $("#employeeId").val();
			var url = basePath+"/doLinkUser.json"
			jQuery.ajax({
				type: 'POST',
				url:url+"?userId="+ids[0]+"&employeeId="+employeeId,
				data: {},
				async: false,
				success: function(result){
						obj.closeAndRef();
				},
				dataType: "json"
			});
		}
}


 