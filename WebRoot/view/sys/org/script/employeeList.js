var basePath = path+'/sys/org/employee';
var pk = "id";
doList(basePath + "/doList.json");


function doLoad(action, id) {
	var url = basePath + "/doLoad.do";
	if (action == "edit") {
		var ids = [];
		if (typeof (id) == "undefined" || id == '') {
			ids = getCheckedIds();
		} else {
			ids.push(id);
		}
		if (ids.length>1) {
			alertError('只能选择一条数据!');
			return;
		}else if(ids.length<1){
			alertError('请选择一条数据!');
			return;
		}else{
			url +="?id="+ids[0];
		}
		showDialog("编辑", url,'','','view');
	}else{
		var treeObj = $.fn.zTree.getZTreeObj("tree");
		var nodes = treeObj.getSelectedNodes();
		if(nodes.length==0){
			alertError("请先在左侧选中您要新增的员工的部门下相应的岗位!");
			return;
		}
		//alert(nodes[0].id+"_"+nodes[0].name);
		var nid = nodes[0].id;
		var cls = nodes[0].cls;
		nid = nid.substring(4);
		if(cls == "dept"){							
			alertError("请先在左侧选中您要新增的员工的岗位!");
			return;
		}
		url+="?postId="+nid;
		showDialog("新建", url,'','','view');
	}
	
}


function status(row, rowIndex, colIndex, options) {
	var value = getRowVal(row, "status");
	if(value=="0"){
	 	return "在职";
	}else if(value=="1"){
	 	return "离职";
	}
}

function operate(row, rowIndex, colIndex, options) {
	var id = getRowVal(row, "id");
	var str = getButton("edit", "doLoad('edit','"+ id + "')","&#xe6df;");
	str += getButton("delete", "doDelete('"+ id + "')","&#xe6e2;");
	return str;
}


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
			url : basePath + "/doDelete.json",
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

/************************/

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
				doList();
			}
		};
		
		$(function(){
			 createTree();
			 
		}); 


function doBatchToUser()
{
	var actionURL = basePath+'/toBatchCreateUser.do';
	
	var ids = [];
	ids = getCheckedIds();
	if (ids.length > 0){
		actionURL += "?ids="+ids.join(",");
		showDialog("批量生成用户", actionURL,'','','view');
		//var v = top.ymPrompt.win({title:"批量生成用户",autoClose:false,width:850,height:580,fixPosition:true,maxBtn:true,minBtn:false,handler:createUserHandler,btn:[[constants.save,'ok'],[constants.cancel,'cancel']],iframe:{id:'createuser',name:'createuser',src:actionURL}});	
		
	}else {
		alertError('请选择数据！');
	}  
}

function doSelectUser()
{
	var actionURL = basePath+'/doListUser.do';
	var ids = [];
	ids = getCheckedIds();
	if (ids.length != 1) {	
		alertError('请选择一条数据！');
		return;
	}else{
		actionURL  += "?employeeId="+ids[0];
	}	
	showDialog("手动关联用户", actionURL,'','','view');
	
}

function formSelectUserSuccessCallback(result){
	if(result.status=='1'){					
		top.jQuery.messager.alert("提示","操作成功", 'info');
	}else if(result.status=='2'){
		top.jQuery.messager.alert("提示","员工不存在！", 'error');
	}else if(result.status=='3'){
		top.jQuery.messager.alert("提示","当前员工已经生成了用户！", 'error');
	}else if(result.status=='4'){
		top.jQuery.messager.alert("提示","用户不存在！", 'error');
	}else if(result.status=='5'){
		top.jQuery.messager.alert("提示","当前用户已经被关联！", 'error');
	}
	dialogW.close();
}


function doAllToUser(){
	var ids = [];
	ids = getCheckedIds();
	
	if (ids.length > 0){
		 
	}else {
		alertError('请选择一条数据！');
	}  
}

/**
*展开第一层级
*/
function expendFirstLevel(zTree){
	zTree.expandNode(zTree.getNodes()[0], true);
}
