<%@page language="java" import="java.util.*" pageEncoding="UTF-8"
	trimDirectiveWhitespaces="true"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="f"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>权限表列表</title>
<jsp:include page="/resource/inc/incMeta.jsp" />
<jsp:include page="/resource/inc/incCssJs.jsp" />
<script type="text/javascript"
	src="${path}/resource/layout/jquery-ui.js"></script>
<script type="text/javascript"
	src="${path}/resource/layout/jquery.layout.js"></script>
<script>
	$(document).ready(function() {
		$("body").layout({
			applyDefaultStyles : true,
			spacing_open : 3
		});
	});
</script>
</head>

<body>
	<div class="ui-layout-west">
		<jsp:include page="/resource/inc/incSimpleTree.jsp">
			<jsp:param name="serviceBeanId" value="functionServiceImpl" />
			<jsp:param name="callBack" value="expendFirstLevel" />
		</jsp:include>
	</div>
	<div class="ui-layout-center" style="padding:0px">
		<div class="cl pd-5 bg-1 toolbarDiv" id="toolbar">
			<span class="l"> <jsp:include page="/resource/inc/toolbar.jsp">
					<jsp:param name="texts" value="新建"></jsp:param>
					<jsp:param name="texts" value="删除"></jsp:param>
					<jsp:param name="texts" value="编辑"></jsp:param>
					<jsp:param name="funs" value="doLoad('add','')"></jsp:param>
					<jsp:param name="funs" value="doDelete('')"></jsp:param>
					<jsp:param name="funs" value="doLoad('edit','')"></jsp:param>
					<jsp:param name="ids" value="id1"></jsp:param>
					<jsp:param name="ids" value="id2"></jsp:param>
					<jsp:param name="ids" value="id3"></jsp:param>
					<jsp:param name="icons" value="&#xe600;"></jsp:param>
					<jsp:param name="icons" value="&#xe6e2;"></jsp:param>
					<jsp:param name="icons" value="&#xe6df;"></jsp:param>
				</jsp:include>
			</span>
		</div>
		<div class="pd-10" style="background-color: #ffffff">
			<div class="text-l">
				<form id="searchForm" method="post" class="searchForm">
					<input type="text" style="display: none" name="exportHead"
						id="exportHead" /> <input type="text" style="display: none"
						name="exportTitle" id="exportTitle" /> <input type="text"
						style="display: none" name="exportField" id="exportField" /> <input
						type="text" style="display:none" name="filter[parent_id]"
						id="parentId" /> 节点名称: <input type='text' class="input-text"
						style="width:120px" name="filter[functionName]">
					<jsp:include page="/resource/inc/searchBtn.jsp"></jsp:include>
				</form>
			</div>
		</div>


		<div class="searchTableDiv">
			<table id="searchTable">
				<tr>
					<th w_num="total_line" style="width:35px"></th>
					<th w_check="true" w_index="functionId" style="width:30px"></th>
					<th w_index="pictureAddr" w_render="iconformatter" width="30px;">图标</th>
					<th w_index="functionName" width="15%;" report="true">节点名称</th>
					<th w_index="linkAddress" width="25%;" report="true">功能地址</th>
					<th w_index="buttonId" width="15%;" report="true">按钮ID</th>
					<th w_index="type" w_render="display" width="8%;">是否显示</th>
					<c:if test="${ param.selector ne 'true' }">
						<th w_render="operate" width="15%;">操作</th>
					</c:if>
				</tr>

			</table>
		</div>
	</div>
</body>
<script type="text/javascript"
	src="${vpath}/sys/rbac/script/functionList.js"></script>
<script>
	var setting = {
		data : {
			simpleData : {
				enable : true,
				idKey : "functionId",
				pIdKey : "parent_id"
			},
			key : {
				name : "functionName"
			}
		},
		view : {
			fontCss : setFontCss
		},
		edit : {
			drag : {
				isMove : true,
				prev : true,
				inner : true,
				next : true
			},
			enable : true,
			showRemoveBtn : false,
			showRenameBtn : false
		},
		callback : {
			onClick : onTreeClickBack,
			beforeDrag : beforeDrag,
			beforeDrop : beforeDrop
		}
	};

	function onTreeClickBack() {
		var nodes = zTree.getSelectedNodes();
		if (nodes.length > 0) {
			document.getElementById("parentId").value = nodes[0]["functionId"];
			doList();
		}
	};

	function setFontCss(treeId, treeNode) {
		return treeNode.type == "0" && treeNode.tag == "f" ? {
			"color" : "#FF0000"
		} : {};
	}

	//正在被拖拽的节点
	var dragNode;
	//被拖拽节点原来的父节点
	var srcParentNode;

	function beforeDrag(treeId, treeNodes) {
		if (treeNodes.length > 1) {
			top.$.messager.alert("提示", "每次只允许调整一个节点!");
			return false;
		}
		dragNode = treeNodes[0];
		srcParentNode = dragNode.getParentNode();
	}

	function filter1(node) {
		return node.getParentNode() == null;
	}

	function beforeDrop(treeId, treeNodes, targetNode, moveType) {
		var id = dragNode.functionId;
		var type;
		var parentId;
		var sortNo = "";
		var sortList = "";
		if (targetNode == null && moveType == 'inner' && srcParentNode != null) {
			//调整到最根部节点的最后
			//alert('调整到最根部节点的最后');
			type = "1"; //调整层级 + 顺序变更
			parentId = "0";
			var outerNodes = zTree.getNodesByFilter(filter1);
			sortNo = outerNodes.length + 1;
		} else {
			var targetParentNode = targetNode.getParentNode();
			//原节点不属于最外层
			if (srcParentNode != null) {
				if (moveType == 'inner') {
					//跨层级调整到某个节点之后
					type = "1";
					parentId = targetNode.functionId;
					sortNo = targetNode.children ? (targetNode.children.length + 1)
							: 1;
				} else {
					if (targetParentNode != null) {
						if (srcParentNode["tId"] == targetParentNode["tId"]) {
							//同层级顺序调整
							type = "0";
						} else {
							//跨级调整到中间位置
							type = "1";
							parentId = targetParentNode.functionId;
						}
						var cnodes = targetParentNode.children;
						sortList = getSortList(cnodes, dragNode, targetNode,
								moveType);
					} else {
						//调整成最外层节点的兄弟节点
						var outerNodes = zTree.getNodesByFilter(filter1);
						sortList = getSortList(outerNodes, dragNode,
								targetNode, moveType);
					}
				}
			} else {
				//原节点属于最外层
				if (targetParentNode == null) {
					if (moveType == 'inner') {
						//调整到最外层兄弟节点的下面
						type = "1";
						parentId = targetNode.functionId;
						sortNo = targetNode.children ? (targetNode.children.length + 1)
								: 1;
					} else {
						//调整在最外的顺序
						type = "0";
						parentId = "0";
						var outerNodes = zTree.getNodesByFilter(filter1);
						sortList = getSortList(outerNodes, dragNode,
								targetNode, moveType);
					}
				} else {
					//最外层跨层级调整到内层						
					type = "1";
					if (moveType == "inner") {
						parentId = targetNode.functionId;
						sortNo = targetNode.children ? (targetNode.children.length + 1)
								: 1;
					} else {
						parentId = targetParentNode.functionId;
						var cnodes = targetParentNode.children;
						sortList = getSortList(cnodes, dragNode, targetNode,
								moveType);
					}
				}
			}
		}

		$.ajax({
			url : path + "/sys/rbac/function/changeFuncLevel.json",
			type : "POST",
			dataType : "json",
			data : {
				id : id,
				type : type,
				parentId : parentId,
				sortNo : sortNo,
				sortList : sortList
			},
			success : function(result) {
				if (result.status == "1") {
					zTree.refresh();
				}
			}
		});
	}

	/**
	 * 获取需要重新排序的列表
	 */
	function getSortList(cnodes, dragNode, targetNode, moveType) {
		var sortlist = "";
		var childCount = cnodes.length;
		var targetIdx = zTree.getNodeIndex(targetNode);
		var beforeArr = [];
		var afterArr = [];
		var splitIndex1 = moveType == "prev" ? (targetIdx - 1) : targetIdx;
		var splitIndex2 = moveType == "prev" ? targetIdx : (targetIdx + 1);
		for (var i = 0; i <= splitIndex1; i++) {
			//排除自己
			if (cnodes[i].functionId == dragNode.functionId) {
				continue;
			}
			beforeArr.push(cnodes[i]);
		}
		beforeArr.push(dragNode);
		for (var i = splitIndex2; i < childCount; i++) {
			//排除自己
			if (cnodes[i].functionId == dragNode.functionId) {
				continue;
			}
			afterArr.push(cnodes[i]);
		}
		//数组拼接
		var arr = beforeArr.concat(afterArr);
		for (var i = 0; i < arr.length; i++) {
			var sno = i + 1;
			sortlist += arr[i].functionId + "," + sno + ";";
		}
		sortlist = sortlist.length > 0 ? sortlist.substring(0,
				sortlist.length - 1) : sortlist;
		return sortlist;
	}


	$(document).ready(function() {
		createTree();
	});

	/**
	 *展开第一层级
	 */
	function expendFirstLevel(zTree) {
		zTree.expandNode(zTree.getNodes()[0], true);
	}
</script>
</html>
