var basePath = path+'/sys/org/department';
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
			alertError('只能选择一个部门!');
			return;
		}else if(ids.length<1){
			alertError('请选择一个部门!');
			return;
		}else{
			url +="?departmentId="+ids[0];
		}
		showDialog("编辑", url,'','','view');
	}else{
		var treeObj = $.fn.zTree.getZTreeObj("tree");
		var nodes = treeObj.getSelectedNodes();
		if(nodes.length==0){
			alertError("请先在左侧选中您要增加部门的上级部门!");
			return;
		}
		url+="?departmentId="+nodes[0].id+"&departmentName="+ encodeURIComponent(nodes[0].name);
		showDialog("新建", url,'','','view');
	}
	
}
 


function operate(row, rowIndex, colIndex, options) {
	var id = getRowVal(row, "id");
	var str = getButton("edit", "doLoad('edit','"+ id + "')","&#xe6df;");
	str += getButton("delete", "doDelete('"+ id + "')","&#xe6e2;");
	return str;
}

function fdisplayformatter(row, rowIndex, colIndex, options){
	var value = getRowVal(row, "isdisplay");
	return value=='1'?"显示":"不显示";
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
		$("#parentId").val("");
		doList();
	}
});


 

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


		var setting = {
			data: {
				simpleData: {
					enable: true,
					idKey:"id",
					pIdKey:"parentId"
				},			
				key: { 
					name: "shortName" 
				}
			},
			view:{
				fontCss: setFontCss
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
				document.getElementById("parentId").value = nodes[0].id;
				doList();
			}
		};
		
		function setFontCss(treeId, treeNode){
			return treeNode.isdisplay=="0"?{"color":"#FF0000"}:{};			 
		}
		
		//正在被拖拽的节点
		var dragNode;
		//被拖拽节点原来的父节点
		var srcParentNode;
		function beforeDrag(treeId, treeNodes) {
			if(treeNodes.length>1){
				top.$.messager.alert("提示","每次只允许调整一个部门!");
				return false;
			}
			dragNode = treeNodes[0];
			srcParentNode = dragNode.getParentNode();
		}
		
		/**
		*拖拽节点以后
		*/
		function onDrop(event,treeId, treeNodes, targetNode, moveType,isCopy){			
			var sortNo = 0;
			var nodeList = [];
			var treeObj = $.fn.zTree.getZTreeObj(treeId);
			var nodes = treeObj.getNodes();
			getTreeSortNo(nodes,sortNo,nodeList);			 
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
				url:path+"/sys/org/department/changeDeptmentLevel.json",
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
						//排序成功
						//$.messager.alert("提示","success!","info");
						//zTree.refresh();
					}
				}
			});
		} 
		
		function reloadPage(){
			window.location.reload();
		}
		
		
		/**
		*展开第一层级
		*/
		function expendFirstLevel(zTree){
			zTree.expandNode(zTree.getNodes()[0], true);
		}
		
		/**
		*页面加载完之前执行
		*/
		$(function(){
			 createTree();
		}); 
				 
	
		