var basePath = path+'/sys/org/post';
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
			alertError('只能选择一个岗位!');
			return;
		}else if(ids.length<1){
			alertError('请选择一个列表中的岗位!');
			return;
		}else{
			url +="?id="+ids[0];
		}
		showDialog("编辑", url,'','','view');
	}else{
		showDialog("新建", url,'','','view');
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


/****加载左侧树结构的方法***/
var setting = {
	data: {
		simpleData: {
			enable: true,
			idKey:"id",
			pIdKey:"parentId"
		}		
	},
	edit:{
		drag:{
			isMove:true,
			prev:true,
			inner:true,
			next:true
		},
		enable: true,
		showRemoveBtn: false,
		showRenameBtn: false
	},
	callback: { 
        onClick: onTreeClickBack,
        beforeDrag: beforeDrag, 
		onDrop:onDrop
    }  
};
	 
function onTreeClickBack(){ 
	var nodes = zTree.getSelectedNodes();
	if(nodes.length>0){ 
		var nid = nodes[0].id;
		var cls = nodes[0].cls;		
		if(cls == "dept"){		
			nid = nid.substring(4);	
			document.getElementById("deptId").value = nid;
			document.getElementById("parentId").value = '';
		}else{			
			document.getElementById("parentId").value = nid;	
			document.getElementById("deptId").value = '';
		}
		doList();
	}
};

/**
*加载左侧数结构的方法
* @showType 展示方式 0.按部门层级[默认]  1.按岗位层级
*/
function loadTree(showType) {
       var zNodes;
       $.ajax({
           url: path +'/system/pubmodule/doTree.do',
           data: {
           	"filter[serviceBeanId]": "postServiceImpl",
           	"filter[showType]":showType
           },
           type: 'POST',
           dataType: "json",
           success: function(result) { 
           		//zTree属于全局变量
              	zTree = $.fn.zTree.init($("#tree"), setting, result);               	             
           },
           error: function(msg) {
             
           }
       });
} 

/**
*当前岗位树节点展现方式 0:部门视图 1:岗位视图
*/
var currentPostTreeStyle = 0;

$(function(){
	 //createTree();
	 loadTree(currentPostTreeStyle);
	 //初始化岗位树结构展示方式
	 initShowTypeSpan();
});


function initShowTypeSpan(){
	$("#postviewul>li").each(function(){
		$(this).click(function(){
			var type = $(this).attr("showType");
			$.fn.zTree.destroy("#tree");
			currentPostTreeStyle = type;
			loadTree(currentPostTreeStyle);
			$("#postviewul>li.active").attr("class","common");
			$(this).attr("class","active");			
		});
	});
}
 
//正在被拖拽的节点
var dragNode;
//被拖拽节点原来的父节点
var srcParentNode;
function beforeDrag(treeId, treeNodes) {
	if(treeNodes.length>1){
		top.$.messager.alert("提示","每次只允许调整一个部门!","info");
		return false;
	}	
	dragNode = treeNodes[0];
	if(dragNode.cls == "dept"){
		top.$.messager.alert("提示","部门节点不能移动!若需调整部门请到部门管理管理模块！","info");
		return false;
	}else if(currentPostTreeStyle==0){		 
		top.$.messager.alert("提示","请在岗位视图下进行层级及顺序调整!","info");
		return false;	 
	}else{
		srcParentNode = dragNode.getParentNode();
	}
	/**
	if(srcParentNode==null){
		$.messager.alert("提示","根级节点不能移动!","info");
		return false;
	}
	**/
}
 
/**
*拖拽节点以后
*/
function onDrop(event,treeId, treeNodes, targetNode, moveType,isCopy){
	//alert(dragNode.getParentNode().name);
	//getTreeSortNo(treeNodes,sortNo,nodeList)
	var sortNo = 0;
	var nodeList = [];
	var treeObj = $.fn.zTree.getZTreeObj(treeId);
	var nodes = treeObj.getNodes();
	getTreeSortNo(nodes,sortNo,nodeList);
	//alert(nodeList.length);
	var arr = [];
	for(var i=0;i<nodeList.length;i++){
		var nodeobj = nodeList[i];
		var sort = nodeobj.sortno;
		var name = nodeobj.node.name;
		var oldSortNo = nodeobj.node.sortNo;
		//arr.push(name+"_"+sort+"_"+oldSortNo);
		var sArr = [];
		//排序发生变化的节点ID及序号 
		if(sort!=oldSortNo){
			sArr.push(nodeobj.node.id);
			sArr.push(sort);
			arr.push(sArr.join(","));
		}
	}
	var str = arr.join(";");
	 
	var type = 0; //节点层级是否变化。0.未变化(默认) 1.变化
	var dragPNodeId = 0;
	var srcNewParentNode = dragNode.getParentNode();
		
	if(srcParentNode!=null){
		if(srcNewParentNode!=null){
			if(srcParentNode.id != srcNewParentNode.id){
				type=1;
				dragPNodeId = srcNewParentNode.id;
			}
		}else{			
			type=1;
		}
	}else{
		if(srcNewParentNode!=null){			
			type=1;
			dragPNodeId = srcNewParentNode.id;
		}
	}	
	//如果既没有变更父节点也没有更改排序
	if(type==0&&str==""){
		return false;
	}	
	//提交到后台
	$.ajax({
		url:path+"/sys/org/post/changePostLevel.json",
		type:"POST",
		dataType:"json",
		data:{
			id:dragNode.id,
			type:type,
			parentId:dragPNodeId,
			sortList:str
		},
		success:function(result){					 
			if(result.status=="1"){
				//$.messager.alert("提示","success!","info");
				//zTree.refresh();
			}
		}
	});
} 

/***
*1.得到拖拽之前的所有节点的id，序号的键值对
*2.得到拖拽之后的所有节点的id，序号的键值对
*
*
*/

/**
*按照层级顺序迭代所有的树节点
*
*/
function getTreeSortNo(treeNodes,sortNo,nodeList){
	var sno = sortNo;
	for(var i =0;i<treeNodes.length;i++){
		var cNode = treeNodes[i];
		//sortNo += i+1;
		sortNo = sno+i+1;
		//构造节点排序对象
		var nodeObj = {};
		nodeObj.sortno = sortNo;
		nodeObj.node= cNode;
		nodeList.push(nodeObj);		
	}	
	var childrenNodes = [];	
	for(var i=0;i<treeNodes.length;i++){		 
		var cNodes = treeNodes[i].children;	
		if(typeof(cNodes)!='undefined'){
			for(var j=0;j<cNodes.length;j++){
				childrenNodes.push(cNodes[j]);
			}
		}
	}
	if(childrenNodes.length>0){
		getTreeSortNo(childrenNodes,sortNo,nodeList)
	}
}
 