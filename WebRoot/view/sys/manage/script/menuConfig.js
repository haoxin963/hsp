var basePath = path+'/sys/manage/station';
var dg;
$(function(){
	var i=0;
	var listPath = basePath+"/doFunctionList.json";
	if(typeof(dataList)!="undefined" ){
		dg = $('#dg').datagrid({
		    data:dataList,
		    pagination:false, 
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
		    },
		    onDblClickCell:onDbClickCell,
		    onAfterEdit:onAfterEdit
		}); 
	}else{
		dg = $('#dg').datagrid({
		    url:listPath,
		    pagination:false,
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
			onDblClickCell:onDbClickCell
		}); 
	}
	
}); 

function doLoad(action,id){ 
	var actionURL = basePath+'/doFunctionLoad.do';
	var bt = [{name: constants.save,callback:buttonHandler,focus: true},{name: constants.cancel}];
	if(action=='edit'){
		if(id==''){
			var rows = $('#dg').datagrid('getSelections'); 
			if (rows.length == 1) { 
				actionURL+="?functionId="+rows[0].functionId+"&custDomain="+custDomain;
			} else { 
				top.jQuery.messager.alert(constants.alertTitle, constants.noDataSelected, 'error');
				return;
			}
		}else{
			actionURL+="?functionId="+id;			
		}
	}else{
		//获取树节点当前选中的节点。
		var nodes = $.fn.zTree.getZTreeObj("custTree").getSelectedNodes();
		if(nodes.length==0){
			top.jQuery.messager.alert(constants.alertTitle, "请先在左侧选中您要增加菜单的上级菜单!", 'error');
			return;
		}
		actionURL+="?functionId="+nodes[0].functionId+"&functionName="+nodes[0].functionName+"&custDomain="+custDomain;	
	}
	dialogW = jQuery.dialog({
		title: constants.newForm,
		width: 850,
		height:500,
	    id: 'menuDialog', 
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

function iconformatter(value,row,index){
	if(row["tag"]=='b'&&row["pictureAddr"]!=""){
		return "<img src='"+path+row["pictureAddr"]+"' />";
	}else{
		return "";
	}
}

function displayformatter(value,row,index){
	if(row["type"]=='1'){
		return "显示";
	}else{
		return "隐藏";
	}
}

function doDelete(id){ 
	var confirmCnt = ""; 
	var hasChild = false;
	var ids = [];
	var rows = $('#dg').datagrid('getSelections'); 
	if(id==''){
		jQuery.each(rows, function (i, n) {
			ids.push(n.functionId); 
			
			var treeObj = $.fn.zTree.getZTreeObj("custTree");	
			var delNode = treeObj.getNodeByParam("functionId",n.functionId);
			if(delNode.children){
				hasChild = true;
				confirmCnt = confirmCnt + n.functionName + ",";
			}
		}); 
	}else{
		ids.push(id); 
	}
	if(hasChild){
		confirmCnt = confirmCnt.substring(0,confirmCnt.length-1) + "</br>";
		confirmCnt = confirmCnt + "上述菜单下存在下级菜单，确认删除?";
	}else{
		confirmCnt = constants.confirmDelete;
	}
	if (ids.length > 0){
		top.jQuery.messager.confirm(constants.confirmDelete,confirmCnt,function(r){
			if (r){ 
				function success(result){
					if(result.status!='500'){
						if(result.status!='1'){
							top.jQuery.messager.alert(constants.alertTitle, result.msg, 'error');
						}else{					
							refreshTree();
						}
					}else{
						top.jQuery.messager.alert(constants.alertTitle, result.errorInfo, 'error');
					}
				};
				jQuery.ajax({
				type: 'POST',
				url: basePath+"/doFunctionDelete.json?ids="+ids.join(",")+"&custDomain="+custDomain,
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

function refreshTree(){
	 $.fn.zTree.destroy("custTree");
	 createTree("custTree",custTreeSetting,custDomain); 
}
function beforeDragTreeNodes(treeId, treeNodes){
	if(treeNodes[0].getParentNode() == null){
		top.$.messager.alert("提示","不能拖动根节点!");
		return false;
	}
}

function beforeDropTreeNodes(treeId, treeNodes, targetNode, moveType){
	if(targetNode != null){  
		var targetParentNode = targetNode.getParentNode();
		if(targetParentNode != null && moveType != 'inner'){  //如果不是根节点,且moveType为prev和next
			var sortNo = moveType == "prev"?targetNode.sortNo:targetNode.sortNo+1;
			treeNodes[0].sortNo = sortNo;
			treeNodes[0].parent_id = targetNode.parent_id;
			if(targetNode.getNextNode() == null && moveType == 'next'){
				saveTreeNodes(setFunctionObj(treeNodes[0],targetNode.level),"true");
			}else{
				saveTreeNodes(setFunctionObj(treeNodes[0],targetNode.level),"false");
			}    
		}else if(moveType == 'inner'){   //如果moveType为inner(包括根节点和非根节点)
			var sortNo = targetNode.children?(targetNode.children.length+1):1;	
			treeNodes[0].sortNo = sortNo;
			treeNodes[0].parent_id = targetNode.functionId;
			saveTreeNodes(setFunctionObj(treeNodes[0],targetNode.level+1),"true");
		}else{
			return false;
		}
		$("#parentId").attr("value",targetNode.functionId); 
	}else{
		return false;
	}
}

function setFunctionObj(treeNode,level){
	var obj = getFunctionObj(treeNode,level);
	if(treeNode.children){
		jQuery.each(treeNode.children, function (idx, childNode) {
			var childobj = getFunctionObj(childNode,level+1);
			obj.children.push(childobj);
			setFunctionObj(childNode,level+1);
		}); 
	}	
	return obj;
}
function getFunctionObj(treeNode,level){
	obj = new Object();
	obj.functionId = treeNode.functionId;
	obj.buttonId = treeNode.buttonId;
	obj.child = treeNode.child;
	obj.flag = treeNode.flag;
	obj.functionName = treeNode.functionName;
	obj.level = level;
	obj.linkAddress = treeNode.linkAddress;
	obj.parentId = treeNode.parent_id;
	obj.pictureAddr = treeNode.pictureAddr;
	obj.sortNo = treeNode.sortNo;
	obj.tag = treeNode.tag;
	obj.type = treeNode.type;
	obj.innerUrl = treeNode.innerUrl;
	obj.children = [];
	return obj;
}
function saveTreeNodes(functionObj,isAppend){
	$.ajax({
		url:path+"/sys/manage/station/saveTreeNodes.json",
		type:"POST",
		dataType:"json",
		data:{
			"custDomain": custDomain,
			"functionString": JSON.stringify(functionObj),
			"isAppend" : isAppend
		},
		success:function(result){					 
			if(result.status == "1"){
				refreshTree();
			}
		}
	});
}

function searchFun(){
	var data = $("#searchForm").serializeJson(); 
  	$('#dg').datagrid('load',data); 
}
//创建树
function createTree(treeId,setting,custDomain) {
	var treeUrl = basePath+"/buildMenuTree.do";
    var zNodes;
    $.ajax({
        url: treeUrl,
        data: {
        	"custDomain": custDomain
        },
        type: 'POST',
        dataType: "json",
        success: function(result) {  
	          var treeObj =  $.fn.zTree.init($("#"+treeId+""), setting, result); 
	          var rootNode = treeObj.getNodeByParam("functionId","0001");
	          treeObj.expandNode(rootNode,true);
	          if(treeId == "custTree"){	
	          	if($("#parentId").val() == ""){
	          		treeObj.selectNode(rootNode,false);  
	          		$("#parentId").attr("value",rootNode.functionId);  
	          	}else{  // $("#parentId") 有值说明是刷新树，则根据functionId获取节点，展开并选中该节点
					 var selNodeId = $("#parentId").val();
				     var selNode = treeObj.getNodeByParam("functionId",selNodeId);
				     treeObj.expandNode(selNode,true);
				     treeObj.selectNode(selNode,false); 
					 searchFun();
	          	}  
	          }
        },
        error: function(msg) {
          
        }
    });
}
