var basePath = path+'/sys/manage/customer';
var dg;
$(function(){
	var i=0;
	var listPath = basePath+"/doList.json";
	if(typeof(dataList)!="undefined" ){
		dg = $('#dg').datagrid({
		    data:dataList,
		    pagination:true, 
		    fitColumns:constants.fitColumns,
		    striped:true,
		    rownumbers:true,
			loadFilter: function(r){
				if (r.command){ 
					return r.command;
				} else {
					return r;
				}
			}, 
		    onBeforeLoad:function(){
		    	if(i==1){
			    	var opts = $(this).datagrid('options');
			    	opts.url= listPath; 
		    	}
		    	i++;
		    }
		}); 
	}else{
		dg = $('#dg').datagrid({
		    url:listPath,
			loadFilter: function(r){
				if (r.command){ 
					return r.command;
				} else {
					return r;
				}
			}, 
		    pagination:true, 
		    fitColumns:constants.fitColumns,
		    striped:true,
		    rownumbers:true
		}); 
	}
	
}); 

function doLoad(action,id){ 
	var actionURL = basePath+'/doLoad.do';
	var titleTxt = constants.newForm;
	var bt = [{name: constants.save,callback:buttonHandler,focus: true},{name: constants.cancel}];
	if(action=='edit' || action == "view") {
		if(id==''){
			var rows = $('#dg').datagrid('getSelections'); 
			if (rows.length == 1) { 
				id = rows[0].custId;
			} else { 
				top.jQuery.messager.alert(constants.alertTitle, constants.noDataSelected, 'error');
				return;
			}
		}
		actionURL+="?custId="+id;
		titleTxt = constants.editForm;
		if(action=="view"){
			titleTxt = constants.viewForm;
			bt = [[constants.close, "cancel"]];
		}
	}
	dialogW = jQuery.dialog({
		title: constants.newForm,
		width: 850,
		height:500,
	    id: 'customerDialog', 
	    content: 'url:'+actionURL,
	    lock:true,
	    button: bt
	});
} 

function buttonHandler(tp){ 
	dialogW.content.doSave(window);
	return false;
}

function formSaveSuccessCallback(r){
	 top.jQuery.messager.alert(constants.alertTitle,constants.success);
	 dg.datagrid('reload');
	 dialogW.close();
}

function rowformater(value,row,index)
{
	return genButton('viewBtn',"<a class='easyui-linkbutton l-btn l-btn-plain' href='javascript:void(0);' name='viewBtn' onclick=doLoad('view',"+row.custId+")><span class='l-btn-left'><span class='l-btn-text'><span class='l-btn-empty icon-view'>&nbsp;</span></span></span></a>");
};


function doDelete(id){ 
	var ids = [];
	var rows = $('#dg').datagrid('getSelections'); 
	if(id==''){
		jQuery.each(rows, function (i, n) {
			ids.push(n.custId); 
		}); 
	}else{
		ids.push(id); 
	}
	if (ids.length > 0){
		top.jQuery.messager.confirm(constants.confirmDelete,constants.confirmDelete,function(r){
			if (r){ 
				function success(result){
					if(result.status=='1'){
						rows = $('#dg').datagrid('getSelections'); 
						jQuery.each(rows, function (i, n) {
						 	dg.datagrid('deleteRow',dg.datagrid('getRowIndex',n));
						});
					}else{
						top.jQuery.messager.alert(constants.alertTitle, result.msg, 'error');
					}
				};
				jQuery.ajax({
				type: 'POST',
				url: basePath+"/doDelete.json?ids="+ids.join(","),
				data: {},
				success: success,
				dataType: "json"
				});
			}
		});
	}else {
		top.jQuery.messager.alert(constants.alertTitle, constants.noDataSelected, 'error');
	}  
};

function statusformater(value,row,index)
{
	return value=="0" ? "启用" :"禁用";
};

$(document).keypress(function(event){
	if(event.keyCode==13){
		searchFun();
	}
});

function searchFun(){
	var data = $("#searchForm").serializeJson(); 
  	$('#dg').datagrid('load',data); 
};