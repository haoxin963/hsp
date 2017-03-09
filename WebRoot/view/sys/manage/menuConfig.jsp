<%@page language="java" import="java.util.*" pageEncoding="UTF-8"
	trimDirectiveWhitespaces="true"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="f"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<jsp:include page="/com/inc/incMeta.jsp">
			<jsp:param name="com" value="core"></jsp:param>
		</jsp:include>
		<title>站点菜单</title>
		<jsp:include page="/com/inc/incCssJs.jsp">
			<jsp:param name="dialog" value="v2" />
		</jsp:include>
		<jsp:include page="/com/inc/jsonList.jsp">
			<jsp:param name="command" value="command"></jsp:param>
		</jsp:include>
		<jsp:include page="/com/inc/incTreeCssJs.jsp"></jsp:include>

		<script>
			var custDomain = "${custDomain}"; 
		</script>
		<script type="text/javascript"
			src="${vpath}/sys/manage/script/menuConfig.js"></script>

	</head>
	<body class="easyui-layout">

		<div data-options="region:'west',split:true" title="${s.treeTitle}"
			style="width: 220px;">
			<ul id="tempTree" class="ztree"></ul>
		</div>
		<div data-options="region:'center',split:true" title="${s.treeTitle}"
			style="width: 220px;">
			<ul id="custTree" class="ztree"></ul>
		</div>
		<div data-options="region:'east'" style="width: 600px;">

			<c:if test="${ param.selector ne 'true' }">
				<div class="toolbarDiv" align="left">
					<jsp:include page="/com/inc/toolbar.jsp">
						<jsp:param name="texts" value="新建"></jsp:param>
						<jsp:param name="texts" value="删除"></jsp:param>
						<jsp:param name="texts" value="编辑"></jsp:param>
						<jsp:param name="funs" value="doLoad('add','')"></jsp:param>
						<jsp:param name="funs" value="doDelete('')"></jsp:param>
						<jsp:param name="funs" value="doLoad('edit','')"></jsp:param>
						<jsp:param name="ids" value="id1"></jsp:param>
						<jsp:param name="ids" value="id2"></jsp:param>
						<jsp:param name="ids" value="id3"></jsp:param>
						<jsp:param name="icons" value="icon-add"></jsp:param>
						<jsp:param name="icons" value="icon-remove"></jsp:param>
						<jsp:param name="icons" value="icon-edit"></jsp:param>
					</jsp:include>
				</div>
			</c:if>

			<div id="tb" class="searchTb">
				<form id="searchForm" method="post" class="searchForm">
					<input type="text" style="display: none" name="exportHead"
						id="exportHead" />
					<input type="text" style="display: none" name="exportTitle"
						id="exportTitle" />
					<input type="text" style="display: none" name="exportField"
						id="exportField" />

					<input type="text" style="display: none" name="filter[custDomain]"
						id="custDomain" value="${custDomain}" />
					<input type="text" style="display: none" name="filter[parent_id]"
						id="parentId" />
					节点名称:
					<input type='text' class='filter' name="filter[functionName]">
					<jsp:include page="/com/inc/searchBtn.jsp"></jsp:include>
				</form>
			</div>


			<table id="dg" width="100%" height="100%">
				<thead>
					<tr id="gridThead">
						<th data-options="field:'functionId',checkbox:true"></th>
						<th data-options="field:'functionName',editor:'text'" width=70
							report=true>
							节点名称
						</th>
						<th data-options="field:'linkAddress',editor:'text'" width=90
							report=true>
							功能地址
						</th>
						<th data-options="field:'pictureAddr',editor:'text'" width=70
							report=true>
							图片地址
						</th>
						<th data-options="field:'buttonId',editor:'text'" report=true>
							按钮ID
						</th>
						<th data-options="field:'type',formatter:displayformatter">
							是否显示
						</th>
					</tr>
				</thead>
			</table>
		</div>
	</body>
	<script>
		var tempTreeSetting = {
			data: {	
				simpleData: { 
					enable: true,
					idKey: "functionId",
					pIdKey: "parent_id"
				},			
				key: { 
					name: "functionName" 
				}				
			},
			view:{
				fontCss: setFontCss,
				selectedMulti:false 
			},
			edit:{
				drag:{
					isCopy:true,
					isMove:false,
					prev:false,
					inner:false,
					next:false
				},
				enable: true,
				showRemoveBtn: false,
				showRenameBtn: false
			},
			callback: { 
				beforeDrag: beforeDragTreeNodes,
				beforeDrop: beforeDropTreeNodes
		    }  
		};
		
		var custTreeSetting = {
			data: {	
				simpleData: { 
					enable: true,
					idKey: "functionId",
					pIdKey: "parent_id"
				},			
				key: { 
					name: "functionName" 
				}				
			},
			view:{
				fontCss: setFontCss,
				selectedMulti:false 
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
				beforeDrop: beforeDrop
		    }  
		};
 		 
		function onTreeClickBack(){ 
			var treeObj = $.fn.zTree.getZTreeObj("custTree");
			var nodes = treeObj.getSelectedNodes();
			if(nodes.length>0){  
				$("#parentId").attr("value",nodes[0].functionId); 
				treeObj.expandNode(nodes[0]);
				searchFun();
			}
		};
		function setFontCss(treeId, treeNode){
			return treeNode.type=="0"&&treeNode.tag=="f"?{"color":"#FF0000"}:{};			 
		}
		 
		
		//正在被拖拽的节点
		var dragNode;
		//被拖拽节点原来的父节点
		var srcParentNode;
		
		function beforeDrag(treeId, treeNodes) {
			if(treeNodes.length>1){
				top.$.messager.alert("提示","每次只允许调整一个节点!");
				return false;
			}
			dragNode = treeNodes[0];
			srcParentNode = dragNode.getParentNode();
		}
		
		function filterRootNode(node){
			return node.getParentNode()==null;
		}
		
		function beforeDrop(treeId, treeNodes, targetNode, moveType) {	
			var treeObj = $.fn.zTree.getZTreeObj("custTree");		 
			var id = dragNode.functionId;
			var type;			
			var parentId;
			var sortNo="";
			var sortList="";					
			if(targetNode==null&&moveType=='inner'&&srcParentNode!=null){
				//调整到根节点子节点的最后
				type = "1";	//调整层级 + 顺序变更
				parentId = "0";
				var outerNodes = treeObj.getNodesByFilter(filterRootNode);
				sortNo = outerNodes.length+1;					
			}else{
				var targetParentNode = targetNode.getParentNode();	
				//原节点不属于最外层
				if(srcParentNode!=null){					
					if(moveType == 'inner'){
						//跨层级调整到某个节点之后
						type = "1";
						parentId = targetNode.functionId;
						sortNo =targetNode.children?(targetNode.children.length+1):1;
					}else{
						if(targetParentNode!=null){
							if(srcParentNode["tId"]==targetParentNode["tId"]){
								//同层级顺序调整
								type = "0";
							}else{
								//跨级调整到中间位置
								type = "1";
								parentId = targetParentNode.functionId;
							}
							var cnodes = targetParentNode.children;							 
							sortList = getSortList(cnodes,dragNode,targetNode,moveType);
						}else{
							//调整成最外层节点的兄弟节点
							var outerNodes = treeObj.getNodesByFilter(filterRootNode);							 
							sortList = getSortList(outerNodes,dragNode,targetNode,moveType);
						}
					}
				}else{
					//原节点属于最外层
					if(targetParentNode==null){
						if(moveType=='inner'){
							//调整到最外层兄弟节点的下面
							type = "1";
							parentId = targetNode.functionId;	
							sortNo = targetNode.children?(targetNode.children.length+1):1;
						}else{
							//调整在最外的顺序
							type = "0";
							parentId = "0";
							var outerNodes = treeObj.getNodesByFilter(filterRootNode);							 
							sortList = getSortList(outerNodes,dragNode,targetNode,moveType);
						}					 								
					}else{
						//最外层跨层级调整到内层						
						type = "1";
						if(moveType=="inner"){
							parentId = targetNode.functionId;
							sortNo = targetNode.children?(targetNode.children.length+1):1;
						}else{
							parentId = targetParentNode.functionId;							
							var cnodes = targetParentNode.children;							 
							sortList = getSortList(cnodes,dragNode,targetNode,moveType);
						}
					}
				}				
			}		
	
			$.ajax({
				url:path+"/sys/manage/station/changeFuncLevel.json?custDomain="+custDomain,
				type:"POST",
				dataType:"json",
				data:{
					id:id,
					type:type,
					parentId:parentId,
					sortNo:sortNo,
					sortList:sortList
				},
				success:function(result){					 
					if(result.status=="1"){
						refreshTree();
					}
				}
			});
			
		}
		
		/**
		* 获取需要重新排序的列表
		*/
		function getSortList(cnodes,dragNode,targetNode,moveType){
			var treeObj = $.fn.zTree.getZTreeObj("custTree");	
			var sortlist = "";
			var childCount = cnodes.length;
			var targetIdx = treeObj.getNodeIndex(targetNode);
			var beforeArr = [];
			var afterArr = [];
			var splitIndex1 = moveType=="prev"?(targetIdx-1):targetIdx;
			var splitIndex2 = moveType=="prev"?targetIdx:(targetIdx+1);
			for(var i = 0;i<=splitIndex1;i++){
				//排除自己
				if(cnodes[i].functionId == dragNode.functionId){
					continue;
				}
				beforeArr.push(cnodes[i]);
			}
			beforeArr.push(dragNode);
			for(var i=splitIndex2;i<childCount;i++){
				//排除自己
				if(cnodes[i].functionId==dragNode.functionId){
					continue;
				}
				afterArr.push(cnodes[i]);					
			}
			//数组拼接
			var arr = beforeArr.concat(afterArr);
			for(var i = 0;i<arr.length;i++){
				var sno = i+1;
				sortlist += arr[i].functionId+","+sno+";";
			}
			sortlist = sortlist.length>0?sortlist.substring(0,sortlist.length-1):sortlist;
			return sortlist;
		}
		
		$.extend($.fn.datagrid.methods, {
			editCell: function(jq,param){
				return jq.each(function(){
					var opts = $(this).datagrid('options');
					var fields = $(this).datagrid('getColumnFields',true).concat($(this).datagrid('getColumnFields'));
					for(var i=0; i<fields.length; i++){
						var col = $(this).datagrid('getColumnOption', fields[i]);
						col.editor1 = col.editor;
						if (fields[i] != param.field){
							col.editor = null;
						}
					}
					$(this).datagrid('beginEdit', param.index);
					for(var i=0; i<fields.length; i++){
						var col = $(this).datagrid('getColumnOption', fields[i]);
						col.editor = col.editor1;
					}
				});
			}
		});
		
		var editIndex = undefined;
		function endEditing(){
			if (editIndex == undefined){return true}
			if ($('#dg').datagrid('validateRow', editIndex)){
				$('#dg').datagrid('endEdit', editIndex);
				editIndex = undefined;
				return true;
			} else {
				return false;
			}
		}
		function onDbClickCell(index, field){
			if (endEditing()){
				$('#dg').datagrid('selectRow', index)
						.datagrid('editCell', {index:index,field:field});
				editIndex = index;
			}
		}
		
		function onAfterEdit(index,row,changes)
		{	
			//判断是否有变化
			var isChanged = false;
			for(var v in changes){
				isChanged = true;
			}
			//如果没有变化
			if(!isChanged){
				return;
			}		
			$.ajax({
				url:path+"/sys/manage/station/doFunctionSave.json?custDomain="+custDomain,
				type:"POST",
				dataType:"json",
				data:{
					 functionId:row["functionId"],
					 buttonId:row["buttonId"],
					 functionName:row["functionName"],
					 child:row["child"],
					 flag:row["flag"],
					 linkAddress:row["linkAddress"],
					 parentId:row["parent_id"],
					 pictureAddr:row["pictureAddr"],
					 sortNo:row["sortNo"],
					 tag:row["tag"]
				},
				success:function(result){					 
					if(result.status=="1"){
						$('#dg').datagrid("reload");					
						window.location.reload();			
					}
				}
			});	
		 	
		}
		
		$(document).ready(function(){
			 createTree("tempTree",tempTreeSetting,""); 
			 createTree("custTree",custTreeSetting,"${custDomain}"); 
		}); 
 	</script>
</html>
