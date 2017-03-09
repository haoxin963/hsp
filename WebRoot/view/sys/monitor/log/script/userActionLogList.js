var basePath = path+'/sys/monitor/log/userLog';
var dg;
$(function(){
	var i=0;
	var listPath = basePath+"/doActionLogList.json";
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

function formHandler(tp){ 
		if(tp=='ok'){
			top.ymPrompt.getPage().contentWindow.doSave($('#dg'))
		}else if(tp=='cancel'){
			top.ymPrompt.close();
		}else if(tp=='close'){
			top.ymPrompt.close();
		}
}

function doLoad(action,id){ 
	var actionURL = basePath+'/doLoad.do';
	var titleTxt = constants.newForm;
	if(action=='edit'){
		if(id==''){
			var rows = $('#dg').datagrid('getSelections'); 
			if (rows.length == 1) { 
				actionURL+="?id="+rows[0].id;
			} else { 
				top.jQuery.messager.alert(constants.alertTitle, constants.noDataSelected, 'error');
				return;
			}
		}else{
			actionURL+="?id="+id;
		}
		actionURL+="&t="+new Date().getTime();
		titleTxt = constants.editForm;
	}
	var v = top.ymPrompt.win({title:titleTxt,autoClose:false,width:850,height:580,fixPosition:true,maxBtn:true,minBtn:false,handler:formHandler,btn:[[constants.save,'ok'],[constants.cancel,'cancel']],iframe:{id:'userActionLog',name:'userActionLog',src:actionURL}});
} 

function rowformater(value,row,index)
{
	var str =  genButton('id1',"<a class='easyui-linkbutton l-btn l-btn-plain' href='javascript:void(0);' name='editBtn' onclick=doLoad('edit',"+row.id+")><span class='l-btn-left'><span class='l-btn-text'><span class='l-btn-empty icon-edit'>&nbsp;</span></span></span></a>");
	str += genButton('id2',"<a class='easyui-linkbutton l-btn l-btn-plain' href='javascript:void(0);'  name='delBtn'  onclick=doDelete("+row.id+")><span class='l-btn-left'><span class='l-btn-text'><span class='l-btn-empty icon-remove'>&nbsp;</span></a>");
	return str;
};


function doDelete(id){ 
	var ids = [];
	var rows = $('#dg').datagrid('getSelections'); 
	if(id==''){
		jQuery.each(rows, function (i, n) {
			ids.push(n.id); 
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


function searchFun(){
	var data = $("#searchForm").serializeJson(); 
  	$('#dg').datagrid('load',data); 
};