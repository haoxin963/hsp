var basePath = path+'/sys/manage/station';
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

function change(action){
	var ids = [];
	var rows = $('#dg').datagrid('getSelections'); 
	jQuery.each(rows, function (i, n) {
			ids.push(n.statId); 
	});
	if (ids.length > 0){
		var actionUrl = basePath+"/"+action+".do";
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
				url: actionUrl+"?domains="+ids.join(","),
				data: {},
				success: success,
				dataType: "json"
				});
			}
		});
	}else {
		top.jQuery.messager.alert(constants.alertTitle, constants.noDataSelected, 'error');
	}  
}

function doLoad(action,id){ 
	var actionURL = basePath+'/doLoad.do';
	var titleTxt = constants.newForm;
	var bt = [{name: constants.save,callback:buttonHandler,focus: true},{name: constants.cancel}];
	if(action=='edit' || action == "view") {
		if(id==''){
			var rows = $('#dg').datagrid('getSelections'); 
			if (rows.length == 1) { 
				id = rows[0].statId;
			} else { 
				top.jQuery.messager.alert(constants.alertTitle, constants.noDataSelected, 'error');
				return;
			}
		}
		actionURL+="?statId="+id;
		titleTxt = constants.editForm;
		if(action=="view"){
			titleTxt = constants.viewForm;
			bt = [[constants.close, "cancel"]];
		}
	}
	dialogW = jQuery.dialog({
		title: titleTxt,
		width: 850,
		height:500,
	    id: 'stationDialog', 
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
	return genButton('viewBtn',"<a class='easyui-linkbutton l-btn l-btn-plain' href='javascript:void(0);' name='viewBtn' onclick=doLoad('view','"+row.statId+"')><span class='l-btn-left'><span class='l-btn-text'><span class='l-btn-empty icon-view'>&nbsp;</span></span></span></a>");
};

function statusformater(value,row,index)
{
	return value=="1" ? "启用" :"禁用";
};

function buildMenu(){
	var actionURL = basePath+'/toMenuConfig.do';	
	var ids = [];
	var rows = $('#dg').datagrid('getSelections'); 
	jQuery.each(rows, function (i, n) {
		ids.push(n.domainAddress); 
	}); 
	if(ids.length!=1){
		top.jQuery.messager.alert(constants.alertTitle, "请选择一个待操作的站点!", 'error');
		return;
	}else{
		actionURL  += "?custDomain="+ids[0];
	}
	var v = top.ymPrompt.win({title:"站点菜单配置",autoClose:false,width:1050,height:680,fixPosition:true,maxBtn:true,minBtn:false,handler:menuConfigHandler,btn:[['关闭','cancel']],iframe:{id:'menuConfig',name:'menuConfig',src:actionURL}});	
	
}
function menuConfigHandler(tp){
	if(tp=='ok'){
		top.ymPrompt.getPage().contentWindow.doSave($('#dg'));
	}else if(tp=='cancel'){
		top.ymPrompt.close();
	}else if(tp=='close'){
		top.ymPrompt.close();
	}
}


function doDelete(id){ 
	var ids = [];
	var rows = $('#dg').datagrid('getSelections'); 
	if(id==''){
		jQuery.each(rows, function (i, n) {
			ids.push(n.statId); 
		}); 
	}else{
		ids.push(id); 
	}
	if (ids.length > 0){
		top.jQuery.messager.confirm(constants.confirmDelete,"此操作将移除当前选中站点相关重要配置!确认继续？",function(r){
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

$(document).keypress(function(event){
	if(event.keyCode==13){
		searchFun();
	}
});

function searchFun(){
	var data = $("#searchForm").serializeJson(); 
  	$('#dg').datagrid('load',data); 
};