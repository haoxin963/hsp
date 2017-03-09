<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"
	trimDirectiveWhitespaces="true"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="f"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<jsp:include page="/com/inc/incMeta.jsp">
			<jsp:param name="com" value="core"></jsp:param>
		</jsp:include>
		<title>流程详情</title>
		<jsp:include page="/com/inc/incCssJs.jsp">
			<jsp:param name="com" value="core"></jsp:param>
		</jsp:include>
	</head>

	<body class="bodyLayout">
		<div class="toolbarDiv">
			<jsp:include page="/com/inc/toolbar.jsp"></jsp:include>
		</div>
		<f:form class="inputForm">

			<fieldset>
				<legend>
					当前步骤
				</legend>
				<c:forEach var="step" items="${currentSteps}">
					<table cellspacing="1" cellpadding="1" border="0"
						class="inputFormTable" width="100%">
						<tr>
							<td class="lableTd">
								步骤名 ${separator}
							</td>
							<td class="valueTd">
								${step.STEP_NAME }
							</td>

							<td class="lableTd">
								执行者 ${separator}
							</td>
							<td class="valueTd">
								${step.trueName }
							</td>
						</tr>
						<tr>
							<td class="lableTd">
								开始时间 ${separator}
							</td>
							<td class="valueTd">
								${step.START_DATE }
							</td>

							<td class="lableTd">
								完成时间 ${separator}
							</td>
							<td class="valueTd">
								${step.FINISH_DATE }
							</td>
						</tr>
					</table>
					<br/>
				</c:forEach>
			</fieldset>
			<fieldset>
				<legend>
					历史步骤
				</legend>
				<c:forEach var="step" items="${historySteps}">
					<table cellspacing="1" cellpadding="1" border="0"
						class="inputFormTable" width="100%">
						<tr>
							<td class="lableTd">
								步骤名 ${separator}
							</td>
							<td class="valueTd">
								${step.STEP_NAME }
							</td>

							<td class="lableTd">
								执行者 ${separator}
							</td>
							<td class="valueTd">
								${step.trueName }
							</td>
						</tr>
						<tr>
							<td class="lableTd">
								开始时间 ${separator}
							</td>
							<td class="valueTd">
								${step.START_DATE }
							</td>

							<td class="lableTd">
								完成时间 ${separator}
							</td>
							<td class="valueTd">
								${step.FINISH_DATE }
							</td>
						</tr>
					</table>
					<br/>
				</c:forEach>
			</fieldset>
		</f:form>
		</div>
	</body>
</html>