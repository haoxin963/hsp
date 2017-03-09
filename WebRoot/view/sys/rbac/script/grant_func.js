var basePath = path+'/sys/rbac/role';

var zTree;  
var treeNodes;
var refFuncList;


var setting = {
	check: {
				enable: true,
				chkboxType: { "Y": "ps", "N": "s" }				
	       },
    data: {
			key: { 
				name: "functionName" 
			},
			simpleData: { 
				idKey: "functionId",
				pIdKey: "parentId",
				enable: true
			}
	},
	callback: {
		onNodeCreated: zTreeOnNodeCreated
	}
};
 
 function zTreeOnNodeCreated(event, treeId, treeNode) { 
 	var treeObj = $.fn.zTree.getZTreeObj("tree");
    for(var rf in refFuncList){
		var rfid = refFuncList[rf]["functionId"];
		if(treeNode.functionId ==rfid){			
			var node = treeObj.getNodeByTId(treeNode.tId);
			treeObj.checkNode(node,true, false,false);
			break;
		}
	}
	//如果关联的值，勾选根节点。
	if(refFuncList.length>0&&treeNode.functionId=="0001"){
		var root = treeObj.getNodeByTId(treeNode.tId);
		treeObj.checkNode(root,false, false);
	}
}; 
 
function filter(node) {
	for(var rf in refFuncList){
		var rfid = refFuncList[rf]["functionId"];
		if(node.functionId ==rfid){
			return true;
		} 
	}
}
 
$(function(){
	$.ajax({
		type: 'POST',
		dataType : "json",
		url: basePath+"/doTreePage.json?rid="+roleId,
		error: function () {
			alert('操作失败');
		},
		success:function(data){
			//alert(data);
			treeNodes = data["treeList"];
			refFuncList = data["functionList"];
			
			zTree = $.fn.zTree.init($("#tree"), setting, treeNodes);
			//展开所有
			zTree.expandAll(true);		 
		}
	});	
});


function getCheckedTreeNode(){
	var treeObj = $.fn.zTree.getZTreeObj("tree");
	var nodes = treeObj.getCheckedNodes(true);
	return nodes;
}

function doSave(obj){ 
		var ckNodes = getCheckedTreeNode();
		if(ckNodes.length<1){
			alertWarning("请勾选您要分配的角色");
			return;
		}else{
			var fids = [];
			jQuery.each(ckNodes, function (i, n) {
				//根节点不保存到数据库
				if(n.functionId!='0001'){
					fids.push(n.functionId);
				}				 
			});			
			//提交到后台保存
			var url = path+'/sys/rbac/role/doGrantFunc.json';
			jQuery.ajax({
				type: 'POST',
				url:url+"?rid="+roleId+"&fids="+fids.join(","),
				data: {},
				success: function(result){
					if(result.status=='1'){
						obj.closeAndRef();
					}else{
						alertError("操作失败，请稍后再试！");
					}
				},
				dataType: "json"
			});
		}
		 
} 