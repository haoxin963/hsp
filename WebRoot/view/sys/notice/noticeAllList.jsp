<%@page language="java" import="java.util.*" pageEncoding="UTF-8"
	trimDirectiveWhitespaces="true"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="f"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		
		<title>pubmodule_notice_tbl列表</title>
	   <jsp:include page="/resource/inc/incMeta.jsp" />
       <jsp:include page="/resource/inc/incCssJs.jsp" />


	</head>
	<body class="bodyLayout">
		<c:if test="${ param.selector ne 'true' }">
			<div class="cl pd-5 bg-1 toolbarDiv" id="toolbar">
			<span class="l"> <jsp:include page="/resource/inc/toolbar.jsp">
					<jsp:param name="texts" value="公告拟稿"></jsp:param>
					<jsp:param name="texts" value="批量发布"></jsp:param>
					<jsp:param name="texts" value="批量删除"></jsp:param>
					<jsp:param name="texts" value="导出"></jsp:param>
					<jsp:param name="funs" value="doLoad('add','')"></jsp:param>
					<jsp:param name="funs" value="doRelease('')"></jsp:param>
					<jsp:param name="funs" value="doDelete('')"></jsp:param>
					<jsp:param name="funs" value="report('doAllList')"></jsp:param>
					<jsp:param name="ids" value="id1"></jsp:param>
					<jsp:param name="ids" value="id2"></jsp:param>
					<jsp:param name="ids" value="id3"></jsp:param>
					<jsp:param name="ids" value="id4"></jsp:param>
					<jsp:param name="icons" value="&#xe600;"></jsp:param>
					<jsp:param name="icons" value="&#xe6df;"></jsp:param>
					<jsp:param name="icons" value="&#xe6e2"></jsp:param>
					<jsp:param name="icons" value="&#xe644;"></jsp:param>
				</jsp:include>
			</span>
		</div>
		</c:if>

<div class="pd-10" style="background-color: #ffffff">
			<div class="text-l">
			<form id="searchForm" method="post" class="searchForm">
				
			
				标　题${separator}
				<input type='text' class="input-text"  style="width:120px"name="filter[title]">
				栏　目${separator}
				<input type='hidden' id="categoryId" class='filter' name="filter[categoryId]">
				<input type='text' id="categoryName"  class='refTree input-text'  style="width:120px"  readonly="readonly"  onclick="show()"/> 
			   <script>
			        function show(){
			        	showDialog("新建", path+"/sys/notice/noticeTreeSelecter.jspx","500","500","view");
			        }

					function selectorCategoryBack(objs){ 
						if(objs!=null && objs.length==1){
							$("#categoryName").val(objs[0].name);
							$("#categoryId").val(objs[0].id); 
						}  
					}
			   </script>
						<input type="text" style="display: none" name="exportHead"
					id="exportHead" />
				<input type="text" style="display: none" name="exportTitle"
					id="exportTitle" />
				创建日期${separator}
				<input type='text' class="input-text"  style="width:120px"name="filter[createDateStart]"  id="LAY_demorange_s">
				~
				<input type='text' class="input-text"  style="width:120px"name="filter[createDateEnd]" id="LAY_demorange_e">
　
				<jsp:include page="/resource/inc/searchBtn.jsp"></jsp:include>
			</form>
			</div>
		</div>
			
		<div class="searchTableDiv">
			<table id="searchTable">
				<tr>
					<th w_num="total_line" style="width:35px"></th>
					<th w_check="true" w_index="id" style="width:30px"></th>
					<th w_index="title" width="30%" report=true>
						标题
					</th>
					<th w_index="name" width="10%" report=true>
						栏目
					</th>
					<th w_index="userName" width="11%" report=true>
						发布人
					</th>
					<th w_index="createTime" width="15%"report=true>
						创建时间
					</th>
					<th w_index="taskopen" width="15%" report=true>
						生效时间
					</th>
					<th w_index="taskclose" width="15%" report=true>
						关闭时间
					</th>
					<th w_index="hits" width="9%" report=true>
						点击量
					</th>
					<th w_render="rowStatus"  width="10%" >
						状态
					</th>
					<th w_render="operate" width="15%;">操作</th>
					</tr>

			</table>
		</div>
			<script type="text/javascript"
			src="${vpath}/sys/notice/script/noticeAllList.js"></script>
			<script>
			layui.use('laydate', function(){
				  var laydate = layui.laydate;
				  
				  var start = {
				    min: ''
				    ,max: '2099-06-16 23:59:59'
				    ,istoday: false
				    ,choose: function(datas){
				      end.min = datas; //开始日选好后，重置结束日的最小日期
				      end.start = datas //将结束日的初始值设定为开始日
				    }
				  };
				  
				  var end = {
				    min: ''
				    ,max: '2099-06-16 23:59:59'
				    ,istoday: false
				    ,choose: function(datas){
				      start.max = datas; //结束日选好后，重置开始日的最大日期
				    }
				  };
				  
				  document.getElementById('LAY_demorange_s').onclick = function(){
				    start.elem = this;
				    laydate(start);
				  }
				  document.getElementById('LAY_demorange_e').onclick = function(){
				    end.elem = this
				    laydate(end);
				  }
				  
				});
			</script>
	</body>
</html>
