<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"
	trimDirectiveWhitespaces="true"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="f"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<jsp:include page="/resource/inc/incMeta.jsp">
			<jsp:param name="com" value="core"></jsp:param>
		</jsp:include>
		<title>员工信息</title>
		<jsp:include page="/resource/inc/incCssJs.jsp">
			<jsp:param name="com" value="core"></jsp:param>
		</jsp:include>
		<jsp:include page="/resource/inc/incTreeCssJs.jsp"></jsp:include>
		<style type="text/css">
			.inputForm fieldset 
			{
				font-family:"微软雅黑";
				border:1px solid #E6E6E6;
				background-color:#FFFFFF;
				margin-top:15px;						
			}
			
			.inputForm fieldset legend
			{
				font-size:15px;								
			}
			.lableTd {    
			    width: 18%;
			}
			
			 
			.treediv{
				position:absolute;
				z-index:9px;				 
				width:200px;
				height:200px;
				overflow-y:auto;
				background-color:#eeeeee;		
			}
		    legend{
    font-size:16px;
}
		</style>
	</head>

	<body class="bodyLayout">
		<f:form class="layui-form"
			action="${path}/sys/org/employee/doSave.json" id="employeeForm"
			modelAttribute="command" method="POST">
			<fieldset>
				<legend>基本信息</legend>
					<table class="table_one">
					<!--新增 -->
					<c:if test="${empty command.id}">
					<tr>
					     <td>
					            <div class="layui-form-item">
						<label class="layui-form-label">所属部门 ${separator}</label>
						<div class="layui-input-block">${department.departmentName}
						 	<input type="hidden" id="deptId" name="deptId" value="${department.departmentId}" />
						</div>
					</div>
					 </td>
					 <td>
					       <div class="layui-form-item">
						<label class="layui-form-label">所属岗位${separator}</label>
						<div class="layui-input-block">
								${mainpost.postName}
						 	<input type="hidden" id="postids" name="postids" value="${mainpost.id},1" />
						</div>
					</div>
					 </td>
						
					</tr>
					</c:if>
					<!-- 修改 -->
					<c:if test="${!empty command.id}">
					<tr>
					 <td colspan=2>
					            <div class="layui-form-item">
						<label class="layui-form-label">部门/岗位 ${separator}</label>
						<div class="layui-input-block">
							<span id="defaultSpan">
						 	${department.departmentName}
						 	<input type="hidden" id="deptId" name="deptId" value="${department.departmentId}" />
						 	 /
						 	${mainpost.postName}
						 	</span>
						 		<input type="hidden" id="postids" name="postids" value="${mainpost.id},1${otherpostids}" />
						 		<script>
						 		function edit(action){
						 			if(action=="1"){
						 				$("#defaultSpan").hide();
						 				$('#temp').show();
						 				$('#showa').hide();
						 				$('#hidea').show();
						 			}else{
						 				$("#defaultSpan").show();
						 				$('#temp').hide();
						 				$('#showa').show();
						 				$('#hidea').hide();
						 			}
						 		}
						 	</script>
						 		<input type="text" style="display: none" id="temp" name="temp"   readonly onclick="showDeptTree();" class="arrow_down layui-input" 
						 		autocomplete="off"/>
						 	<a href="javascript:void" onclick="edit('1')" id="showa" style="color: red">编辑</a>
						 	<a href="javascript:void" onclick="edit('2')" id="hidea" style="display: none;color: red">撤消编辑</a>
						</div>
					</div>
					 </td>
					
						
					</tr>
					</c:if>
					<tr>
						<f:hidden path="id" required='true' integer='true' maxlength='255' />
						<f:hidden path="sysUserId" integer='true' maxlength='255' />
						<td>
						      	<div class="layui-form-item">
						<label class="layui-form-label"><span class='required'>*</span>
							工号 ${separator}</label>
						<div class="layui-input-block">
							<f:input path="empno" maxlength='50' autocomplete="off"
								placeholder="请输入工号" class="layui-input" lay-verify="required" />
						</div>
					</div>
						</td>
						
	                   	<td>
						      	<div class="layui-form-item">
						<label class="layui-form-label"><span class='required'>*</span>
							姓名${separator}</label>
						<div class="layui-input-block">
							<f:input path="empname" maxlength='255' autocomplete="off"
								placeholder="请输入姓名" class="layui-input" lay-verify="required" />
						</div>
					</div>
						</td>
					</tr>
					<tr>	
					        	<td>
						      	<div class="layui-form-item">
						<label class="layui-form-label">
							性别${separator}</label>
						<div class="layui-input-block">
							<f:select path="sex">
								<f:option value="男">男</f:option>
								<f:option value="女">女</f:option>							
							</f:select>	
						</div>
					</div>
						</td>
					      <td>
						      	<div class="layui-form-item">
						<label class="layui-form-label">
							<span class='required'>*</span>状态${separator}</label>
						<div class="layui-input-block">
							<f:select path="status">
								<f:option value="0">在职</f:option>
								<f:option value="1">离职</f:option>							
							</f:select>	
						</div>
					</div>
						</td>
						
	
					
					</tr>
					<tr>
					   	
	                   	<td>
						      	<div class="layui-form-item">
						<label class="layui-form-label">
							身份证号${separator}</label>
						<div class="layui-input-block">
							<f:input path="idCard" maxlength='50' autocomplete="off"
								class="layui-input" />
						</div>
					</div>
						</td>
						<td>
						      	<div class="layui-form-item">
						<label class="layui-form-label">
							入职日期 ${separator}</label>
						<div class="layui-input-block">
							<f:input path="entryDate" maxlength='10' autocomplete="off"
								class="layui-input"  id="LAY_demorange_s"/>
						</div>
					</div>
						</td>
					
	
						
					</tr>
					<tr>
					     <td>
						      	<div class="layui-form-item">
						<label class="layui-form-label">
								职离日期 ${separator}</label>
						<div class="layui-input-block">
							<f:input path="departureDate" maxlength='10' autocomplete="off"
								class="layui-input"  id="LAY_demorange_e"/>
						</div>
					</div>
						</td>
	
					      	<td>
						      	<div class="layui-form-item">
						<label class="layui-form-label">
							自定义用户名${separator}</label>
						<div class="layui-input-block">
							<f:input path="reusername" maxlength='50' autocomplete="off"
								class="layui-input" />
						</div>
					</div>
						</td>
					</tr>
					<tr>
						<td >
					<div class="layui-form-item">
						<label class="layui-form-label">
							备注 ${separator}</label>
						<div class="layui-input-block">
							<f:textarea path="remark" maxlength='255'   class="layui-textarea"/>
						</div>
					</div>
				</td>
						<td >
					<div class="layui-form-item">
						<label class="layui-form-label">
							其他岗位 ${separator}</label>
						<div class="layui-input-block">
							
							<c:if test="${empty command.id}">
								<textarea id="otherpost" name="otherpost" maxlength='255' class="layui-textarea"></textarea>
							</c:if>
							<!-- 修改 -->
							<c:if test="${!empty command.id}">
								<textarea id="otherpost" name="otherpost" maxlength='255' class="layui-textarea"  style="width:80%">${ohterpostnames}</textarea>
							</c:if>
							<span style="display:inline-block;width:20px;height:30px;line-height:30px; float:right;margin-top:-50px;">							
								<a href="javascript:void(0)" onclick="addPosition(true)" title="添加岗位"><img src="${path}/css/bluex/icons/add.png" /></a>&nbsp;
								<a href="javascript:void(0)" onclick="clearPost();" title="清空"><img src="${path}/css/bluex/icons/delete.png" /></a>
							</span>	
						</div>
					</div>
				</td>
					
					</tr>
					</table>
				</fieldset>
				
				<fieldset>
				<legend>联系方式</legend>
				<table class="table_one">
				<tr>
					<td colspan=2>
						      	<div class="layui-form-item">
						<label class="layui-form-label">
							地址${separator}</label>
						<div class="layui-input-block">
							<f:input path="address" maxlength='255' autocomplete="off"
								class="layui-input" />
						</div>
					</div>
						</td>
									
				</tr>				
				<tr>
                     <td>
                          	<div class="layui-form-item">
						<label class="layui-form-label">
							手机号${separator}</label>
						<div class="layui-input-block">
							<f:input path="mobile" maxlength='50' autocomplete="off"
								class="layui-input" />
								</div>
						</div>
                     </td>
                     <td>
                     
                      	<div class="layui-form-item">
						<label class="layui-form-label">
							其它手机号 ${separator}</label>
						<div class="layui-input-block">
							<f:input path="mobile2" maxlength='50' autocomplete="off"
								class="layui-input" />
								</div>
						</div>
                     </td>
				
				</tr>
				<tr>
				       <td>
                          	<div class="layui-form-item">
						<label class="layui-form-label">
							公室办电话${separator}</label>
						<div class="layui-input-block">
							<f:input path="officeTel" maxlength='50' autocomplete="off"
								class="layui-input" />
						</div>
						</div>
                     </td>
                     <td>
                     
                      	<div class="layui-form-item">
						<label class="layui-form-label">
							家庭电话 ${separator}</label>
						<div class="layui-input-block">
							<f:input path="homeTel" maxlength='50' autocomplete="off"
								class="layui-input" />
						</div>
						</div>
                     </td>
				
				
					
				</tr>
				<tr>
				
				         <td>
                          	<div class="layui-form-item">
						<label class="layui-form-label">
							QQ${separator}</label>
						<div class="layui-input-block">
							<f:input path="officeTel" maxlength='50' autocomplete="off"
								class="layui-input" />
						</div>
						</div>
                     </td>
                     <td>
                     
                      	<div class="layui-form-item">
						<label class="layui-form-label">
							邮箱 ${separator}</label>
						<div class="layui-input-block">
							<f:input path="email" maxlength='255' autocomplete="off"
								class="layui-input" />
						</div>
						</div>
                     </td>
					
				</tr>
					
			 	<f:hidden path="sortNo" required='true' maxlength='1' />
				<f:hidden path="delTag" required='true' maxlength='1' />
			</table>
			</fieldset>
		</f:form>
	</body>

	<script> 
			var serverURL = "${path}/sys/org/employee";
			var formId = "employeeForm";
			 
			function doSave(obj) {
				if (valid()) {
					$("#" + formId).ajaxSubmit({
						success : function(result) {
							result = jQuery.parseJSON(result);
							if (result.status == "1") {
								obj.closeAndRef();
							} else {
								alertError(result.msg);
							}
						}
					});

				}
			}

			
			
			//显示岗位树结构
			var postTree;			
			function initPostTree(){
				//配置
				var setting = {
					check: {
						enable: true,
						chkboxType: { "Y": "", "N": "" }				
			        },
					data: {
						simpleData: {
							enable: true,
							idKey:"id",
							pIdKey:"parentId"
						}		
					},					 
					callback: { 
						//点击事件
				        onClick:function(){
							
						}
				    }  
				};
			
				$.ajax({
		            url: '${path}/system/pubmodule/doTree.do',
		            data: {
		            	"filter[serviceBeanId]": "postServiceImpl",
		            	"filter[showType]":"1"
		            },
		            type: 'POST',
		            dataType: "json",
		            success: function(result) {  
		               	postTree = $.fn.zTree.init($("#ptree"), setting, result);
		            }
		        });
			}
			
			/**
			*展示岗位树结构
			*/		
			function addPosition(isAdd){
				if(!isAdd){
					return false;
				}
				if($("#treediv").length>0){
					$("#treediv").remove();					
				}else{			
					var $postTreeDiv = $('<div id="treediv" class="treediv"><div style="text-align:right;height:20px;overflow:hidden;padding-right:5px;"><a style="font-size:15px;" href="javascript:void(0)" onclick="selectpost()">确定</a></div><ul id="ptree" class="ztree"></ul></div>');
					$(document.body).append($postTreeDiv);
					var l = $("#otherpost").offset().left;
					var t = $("#otherpost").offset().top;
					t += $("#otherpost").height()+5;
					$postTreeDiv.css({"left":l+"px","top":t+"px"});
					initPostTree();					
					
					/**
					$(document).click(function(e){
						destoryPostTree(e);
					}); 
					**/
				}
			}
			
			function selectpost(){
				var nodes = postTree.getCheckedNodes(true);
				var postarray = [];
				var postarryname = [];				 
				$(nodes).each(function(i,v){
					var nid = v.id;
					var nname = v.name;
					//nid为岗位ID, 0表示非主要岗位
					postarray.push(nid+",0");
					postarryname.push(nname);				
				});
				if(postarray.length>0){
					$("#postids").val($("#postids").val()+";"+postarray.join(";"));
					$("#otherpost").val(postarryname.join(","));
				}				
				closePostTree();
			}
			
			var currMainPostId = "${mainpost.id}";
			function clearPost(){
				$("#postids").val(currMainPostId+",1");
				$("#otherpost").val("");
			}
			 
			var destoryPostTree = function(e){
				var $treediv = $("#treediv");
				if($treediv.length==0){
					return;
				}
				var x = e.clientX;
				var y = e.clientY;
				var treeX = $treediv.offset().left;
				var treeY = $treediv.offset().top;
				var treeW = $treediv.width();
				var treeH = $treediv.height();
				if(!((x>treeX&&x<(treeX+treeW))&&(y>treeY&&y<(treeY+treeH)))&&e.target.id!="parentName"){
					closePostTree();
				}
			}
			
			function closePostTree(){
				if($("#treediv").length>0){
					$("#treediv").remove();
					//$(document).unbind("click",destoryPostTree);
				}
			}	
			
			String.prototype.endWith=function(str){     
			  var reg=new RegExp(str+"$");     
			  return reg.test(this);        
			}
			
			String.prototype.startWith=function(str){     
			  var reg=new RegExp("^"+str);     
			  return reg.test(this);        
			}  
			//显示部门树结构
			var deptTree;			
			function initDeptTree(){
				//配置
				var setting = {
					data: {
						simpleData: {
							enable: true,
							idKey:"id",
							pIdKey:"parentId"
						}
					},		 
					callback: { 
						//点击事件
				        onClick:function(){
							var nodes = deptTree.getSelectedNodes();
							if(nodes.length>0){
								var nid = nodes[0].id;
								var nname = nodes[0].name;	
								if(nid.startWith("post")){
									$("#temp").val(nname); 
									nid = nid.replace("post","");
									var str = $("#postids").val();
									if(str!=""){
										var strArr = str.split(";");
										if(strArr.length==1){
											//当前只有一个主岗时
											if(str.endWith(",1")){
												$("#postids").val(nid+",1");
											}else{
												$("#postids").val(nid+",1;"+str);
											}
										}else{
											var tempArr = [];
											tempArr.push(nid+",1");
											for(var j=0;j<strArr.length;j++){
												if(!strArr[j].endWith(",1")){
													tempArr.push(strArr[j]);
												}
											}
											$("#postids").val(tempArr.join(";"));
										}
									}else{
										$("#postids").val(nid+",1");
									}
									currMainPostId = nid;
								}else{
									alert('请选择岗位!');
								} 
								closeDeptTree();
							}
						}
				    }  
				};
			
				$.ajax({
		            url: '${path}/system/pubmodule/doTree.do',
		            data: {
		            	"filter[serviceBeanId]": "employeeServiceImpl"		            	 
		            },
		            type: 'POST',
		            dataType: "json",
		            success: function(result) {  
		               	deptTree = $.fn.zTree.init($("#depttree"), setting, result);
		            }
		        });
			}
			
			/**
			*展示岗位树结构
			*/		
			function showDeptTree(){										 		 
				if($("#depttreediv").length>0){
					$("#depttreediv").remove();					
				}else{			
					var $deptTreeDiv = $('<div id="depttreediv" class="treediv"><div style="text-align:right;height:15px;overflow:hidden;padding-right:5px;"><a style="font-size:15px;" href="javascript:void(0)" onclick="closetree(\'deptId\',\'deptName\')">×</a></div><ul id="depttree" class="ztree"></ul></div>');
					$(document.body).append($deptTreeDiv);
					var l = $("#temp").offset().left;
					var t = $("#temp").offset().top;
					t += $("#temp").height()+5;
					$deptTreeDiv.css({"left":l+"px","top":t+"px"});
					initDeptTree();					
					 
					$(document).click(function(e){
						destoryDeptTree(e);
					});
				}
			}
			 
			var destoryDeptTree = function(e){
				var $treediv = $("#depttreediv");
				if($treediv.length==0){
					return;
				}
				var x = e.clientX;
				var y = e.clientY;
				var treeX = $treediv.offset().left;
				var treeY = $treediv.offset().top;
				var treeW = $treediv.width();
				var treeH = $treediv.height();
				if(!((x>treeX&&x<(treeX+treeW))&&(y>treeY&&y<(treeY+treeH)))&&e.target.id!="temp"){
					closeDeptTree();
				}
			}
			
			function closeDeptTree(){
				if($("#depttreediv").length>0){
					$("#depttreediv").remove();
					//$(document).unbind("click",destoryDeptTree);
				}
			}		
			
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
</html>