var basePath = path+'/dform/formview';
var listPath = basePath+"/list.json?formId="+formId;
var toSavePath = basePath+"/toSave.do?formId="+formId;
var deletePath = basePath+"/delete.json?formId="+formId;
var dg;
$(function(){
	var i=0;
	if(typeof(dataList)!="undefined" && typeof(dataList.status)=="undefined" ){
		dg = $('#dg').datagrid({
		    data:dataList,
		    pagination:true,
		    toolbar: '#tb',
		    fitColumns:true,
		    striped:true,
		    rownumbers:true,
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
		    pagination:true,
		    toolbar: '#tb',
		    fitColumns:true,
		    striped:true,
		    rownumbers:true
		}); 
	}
	
}); 

function formHandler(tp){ 
		if(tp=='ok'){
			top.ymPrompt.getPage().contentWindow.doSave($('#dg'))
		}else if(tp=='cancel'){
			top.ymPrompt.close();
		}else if(tp=='close'){
			top.ymPrompt.close();
		}
}

function doLoad(action){ 
	var actionURL = toSavePath;
	var bt = [{name: constants.save,callback:buttonHandler,focus: true},{name: constants.cancel}];
	if(action=='edit'){
		var rows = $('#dg').datagrid('getSelections'); 
		if (rows.length == 1) { 
			actionURL += "&field0="+rows[0].field0;
		} else { 
			top.jQuery.messager.alert(constants.alertTitle, constants.noDataSelected, 'error');
			return;
		}
	}
	dialogW = jQuery.dialog({
		title: constants.newForm,
		width: 850,
		height:500,
	    id: 'dbDialog', 
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
	return " ";
};


function doDelete(){
	var rows = $('#dg').datagrid('getSelections'); 
	var ids = [];
	jQuery.each(rows, function (i, n) {
	 	ids.push(n.field0); 
	}); 
	if (ids.length > 0){
		top.jQuery.messager.confirm('Confirm',constants.confirmDelete,function(r){
			if (r){ 
				jQuery.ajax({
					type: 'POST',
					url: deletePath+"&ids="+ids.join(","),
					data: {},
					success: function(result){
							if(result.status!='500'){
								dg.datagrid('reload');
								//jQuery.each(rows, function (i, n) {
								//	dg.datagrid('deleteRow',dg.datagrid('getRowIndex',n));
								//});
							}else{
								top.jQuery.messager.alert(constants.alertTitle, result.errorInfo, 'error');
							}
						},
					dataType: "json"
				});
			}
		});
	}else {
		top.jQuery.messager.alert(constants.alertTitle, constants.noDataSelected, 'error');
	}  
};


function searchFun(){
	var data = $("#searchForm").serializeJson(); 
  	$('#dg').datagrid('load',data); 
};

function report(exportType){
	var url = basePath+"/doList.xlsx";
	if(exportType>2){
		url = basePath+"/doList.pdfx";
	}
	if(exportType==2 || exportType==4){
		url +="?all=1";
	}
	var exportHead = [];
	$("#gridThead").children().each(function(i){
   		if($(this).attr("report")=="true"){
   			exportHead.push(jQuery.trim($(this).text()));
   		}
 	});  
 	if(exportHead.length>0){
 		$('#exportHead').val(exportHead.join(","));
 		$('#exportTitle').val(document.title);
 	}
	var data = $("#searchForm").serializeJson();
	var searchForm =  document.getElementById("searchForm");
	searchForm.setAttribute("action",url); 
	searchForm.submit();
};

