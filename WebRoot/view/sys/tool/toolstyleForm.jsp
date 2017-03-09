<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"
	trimDirectiveWhitespaces="true"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="f"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>自定义皮肤修改项</title>
<jsp:include page="/resource/inc/incMeta.jsp" />
<jsp:include page="/resource/inc/incCssJs.jsp" />
</head>
<body>

	<br>
	<f:form class="layui-form" action="${path}/sys/tool/toolstyle/doSave.json"
		id="toolstyleForm" modelAttribute="command" method="POST">
		<f:hidden path="id" required='true' number='true' maxlength='255' />
		<table class="table_one">
			<tr>
				<td>
					<div class="layui-form-item">
						<label class="layui-form-label"><span class='required'>*</span>
						      属性名 ${separator}</label>
						<div class="layui-input-block">
							<f:input path="stylename" maxlength='50' autocomplete="off"
								placeholder="请输入属性名" class="layui-input" lay-verify="required" />
						</div>
					</div>
				</td>
				<td>
							<div class="layui-form-item">
						<label class="layui-form-label"><span class='required'>*</span>
						      属性规则 ${separator}</label>
						<div class="layui-input-block">
							<f:input path="stylefunction" maxlength='100' autocomplete="off"
								placeholder="请输入属性规则" class="layui-input" lay-verify="required" />
						</div>
					</div>
				</td>
			</tr>
			<tr>
			      <td>
					<div class="layui-form-item">
						<div class="layui-form-item">
						<label class="layui-form-label">属性类型 ${separator}</label>
						<div class="layui-input-block">
							<f:radiobutton path="styletype" value="0" title="颜色"  checked="checked"  lay-filter="type"/>
							<f:radiobutton path="styletype" value="1" title="图片"   lay-filter="type"/>
                            <f:radiobutton path="styletype" value="2" title="线条"   lay-filter="type"/>
                            <f:radiobutton path="styletype" value="3" title="字体"   lay-filter="type"/>
						</div>
					</div>
					</div>
				</td>
				 <td>
							<div class="layui-form-item">
						<label class="layui-form-label"><span class='required'>*</span>
						      属性初始值${separator}</label>
						<div class="layui-input-block"  id="value">
							<f:input path="stylevalue" maxlength='50' autocomplete="off"
								placeholder="属性初始值" class="layui-input" lay-verify="required" />
						</div>
					</div>
				</td>
				
			</tr>
			<tr  id="file"  >
				 <td>
							<div class="layui-form-item">
						<label class="layui-form-label">
						      图片所在文件夹${separator}</label>
						<div class="layui-input-block">
							<f:input path="filepath" maxlength='50' autocomplete="off"
								placeholder="图片所在文件夹" class="layui-input"  />
						</div>
					</div>
				</td>
				</tr>
		</table>


	</f:form>
	<script>
		var serverURL = "${path}/sys/tool/toolstyle";
		var formId = "toolstyleForm";

		layui.use('form', function(){
			  var form = layui.form();
			  
				form.on('radio(type)', function(data){
				      return cc(data.value);
				}); 
			});
	
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
	   $(function(){
		   if($('input:radio:checked').val()==1){
		        $("#file").show();
		   }else{
			   $("#file").hide();
		   }
	   });
	   
	   function cc(str){
             if(str==1){
            	 $("#file").show();
             }else{
            	 $("#file").hide();
             }
           
	   }
	</script>

</body>


</html>