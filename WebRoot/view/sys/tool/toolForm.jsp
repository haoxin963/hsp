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
	<f:form class="layui-form" action="${path}/sys/tool/tool/doSave.json"
		id="toolForm" modelAttribute="command" method="POST">
		<f:hidden path="id" required='true' number='true' maxlength='255' />
		<table class="table_one">
			<tr>
				<td>
					<div class="layui-form-item">
						<label class="layui-form-label"><span class='required'>*</span>
						      修改项 ${separator}</label>
						<div class="layui-input-block">
							<f:input path="toolname" maxlength='50' autocomplete="off"
								placeholder="请输入修改项" class="layui-input" lay-verify="required" />
						</div>
					</div>
				</td>
				<td>
					<div class="layui-form-item">
						<label class="layui-form-label"><span class='required'>*</span>组件class
							${separator}</label>
						<div class="layui-input-block">
							<f:input path="toolclass" maxlength='50' autocomplete="off"
								placeholder="请输入修改项class" class="layui-input" lay-verify="required" />
						</div>
					</div>
				</td>
			</tr>
					<tr>
				<td colspan="2">
					<div class="layui-form-item">
                           <label class="layui-form-label">修改属性</label>
                                <div class="layui-input-block">
                                <c:forEach  var="list"  items="${ stylelist}">
                            
                                            <input type="checkbox" name="styleid" title="${list.stylename }"  value="${list.id }"
                                               <c:if  test="${list.check==1 }">
                                                     checked=""
                                               </c:if>
                                               >
                                </c:forEach>
                           
                               </div>
  </div>
				</td>
				
			</tr>

			
		</table>


	</f:form>
	<script>
		var serverURL = "${path}/sys/tool/tool";
		var formId = "toolForm";

		function formVerifyExt() {
			//自定义验证规则
		
		}

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
	</script>

</body>


</html>