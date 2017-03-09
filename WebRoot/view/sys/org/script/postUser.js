var basePath = path+'/system/pubmodule';
var dg;
$(function(){
	var i=0;
	var listPath = basePath+"/postUser.json";
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
	if(action=='edit'){
		if(id==''){
			var rows = $('#dg').datagrid('getSelections'); 
			if (rows.length == 1) { 
				actionURL+="?id="+rows[0].id;
			} else { 
				jQuery.messager.alert(constants.alertTitle, constants.noDataSelected, 'error');
				return;
			}
		}else{
			actionURL+="?id="+id;
		}
	}else{
		//获取树节点当前选中的节点。
		var treeObj = $.fn.zTree.getZTreeObj("tree");
		var nodes = treeObj.getSelectedNodes();
		if(nodes.length==0){
			jQuery.messager.alert(constants.alertTitle, "请先在左侧选中您要新增的员工的部门下相应的岗位!", 'info');
			return;
		}
		//alert(nodes[0].id+"_"+nodes[0].name);
		var nid = nodes[0].id;
		var cls = nodes[0].cls;
		nid = nid.substring(4);
		if(cls == "dept"){							
			$.messager.alert(constants.alertTitle, "请先在左侧选中您要新增的员工的岗位!", 'info');
			return;
		}
		actionURL+="?postId="+nid;
	}
	var v = top.ymPrompt.win({title:constants.newForm,autoClose:false,width:850,height:580,fixPosition:true,maxBtn:true,minBtn:false,handler:formHandler,btn:[[constants.save,'ok'],[constants.cancel,'cancel']],iframe:{id:'employee',name:'employee',src:actionURL}});
} 

function fmt(value,row,index)
{
	return row.username +"/"+row.truename;
}

function rowformater(value,row,index)
{
return "<a href='javascript:void(0);' name='editBtn' onclick=doLoad('edit',"+row.id+")><img src='"+path+"/com/images/icon/pencil.png' title='"+constants.editForm+"'/></a>&nbsp;&nbsp;<a href='javascript:void(0);'  name='delBtn' onclick=doDelete("+row.id+")><img src='"+path+"/com/images/icon/edit_remove.png' title='"+constants.deleteForm+"'/></a>";
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
					if(result.status!='500'){
						if(result.status=='0'){
							parent.jQuery.messager.alert(constants.alertTitle, result.msg, 'error');
						}else{
							jQuery.each(rows, function (i, n) {
							 	dg.datagrid('deleteRow',dg.datagrid('getRowIndex',n));
							});
						}
					}else{
						parent.jQuery.messager.alert(constants.alertTitle, result.errorInfo, 'error');
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
		parent.jQuery.messager.alert(constants.alertTitle, constants.noDataSelected, 'error');
	}  
};


function searchFun(){
	var data = $("#searchForm").serializeJson(); 
  	$('#dg').datagrid('load',data); 
};
 

	var setting = {
			data: {
				simpleData: {
					enable: true,
					idKey:"id",
					pIdKey:"parentId"
				}		
			},
			callback: { 
		        onClick: onTreeClickBack 
		    }  
		};
 		 
		function onTreeClickBack(){ 
			var nodes = zTree.getSelectedNodes();
			$("#deptId").val("");
			$("#postId").val("");
			if(nodes.length>0){				
				var nid = nodes[0].id;
				var cls = nodes[0].cls;
				nid = nid.substring(4);
				if(cls == "dept"){							
					$("#deptId").val(nid);
				}else{	
					$("#postId").val(nid);	
				}				 
				searchFun();
			}
		};
		
		$(function(){
			 createTree();
			 
		}); 


function doBatchToUser()
{
	var actionURL = basePath+'/toBatchCreateUser.do';
	var ids = [];
	var rows = $('#dg').datagrid('getSelections');	
	jQuery.each(rows, function (i, n) {
		ids.push(n.id); 
	});
	if (ids.length > 0){
		actionURL += "?ids="+ids.join(",");
		var v = top.ymPrompt.win({title:"批量生成用户",autoClose:false,width:850,height:580,fixPosition:true,maxBtn:true,minBtn:false,handler:createUserHandler,btn:[[constants.save,'ok'],[constants.cancel,'cancel']],iframe:{id:'createuser',name:'createuser',src:actionURL}});	
		
	}else {
		parent.jQuery.messager.alert(constants.alertTitle, constants.noDataSelected, 'error');
	}  
}

function createUserHandler(tp){
	if(tp=='ok'){
		top.ymPrompt.getPage().contentWindow.doSave()
	}else if(tp=='cancel'){
		top.ymPrompt.close();
	}else if(tp=='close'){
		top.ymPrompt.close();
	}
}

function doAllToUser(){
	var ids = [];
	var rows = $('#dg').datagrid('getSelections'); 
	
	jQuery.each(rows, function (i, n) {
		ids.push(n.id); 
	}); 
	
	if (ids.length > 0){
		 
	}else {
		parent.jQuery.messager.alert(constants.alertTitle, constants.noDataSelected, 'error');
	}  
}

/**
*展开第一层级
*/
function expendFirstLevel(zTree){
	zTree.expandNode(zTree.getNodes()[0], true);
}
