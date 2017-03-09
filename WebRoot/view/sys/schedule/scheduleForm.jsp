<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="f"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<jsp:include page="/resource/inc/incMeta.jsp" />
<jsp:include page="/resource/inc/incCssJs.jsp" />
<style>
td {
	width: 50%
}

.ccc {
	margin-left: 180px;
	margin-top: -38px;
}
</style>
<script>
	function triggerTypeChange() {
		var triggerType = $("input[name=triggerType]:checked").val();
		if (triggerType == 'simple') {
			$("#rowStartTime").css('display', '');
			$("#rowRepeat").css('display', '');

			$("#rowStartTime input").removeAttr('disabled');
			$("#rowRepeat input ").removeAttr('disabled');

			$("#advancedRow").css('display', 'none');
			$("#advancedRow input").attr('disabled', 'disabled');

			$("#rowExpression").css('display', 'none');
			$("#rowExpression input ").attr('disabled', 'disabled');

		} else if (triggerType == 'advanced') {
			$("#rowStartTime").css('display', 'none');
			$("#rowRepeat").css('display', 'none');

			$("#rowStartTime input").attr('disabled', 'disabled');
			$("#rowRepeat input").attr('disabled', 'disabled');

			$("#advancedRow").css('display', '');
			$("#advancedRow input").removeAttr('disabled');

			$("#rowExpression").css('display', 'none');
			$("#rowExpression input ").attr('disabled', 'disabled');
		} else if (triggerType == 'cron') {
			$("#rowStartTime").css('display', 'none');
			$("#rowRepeat").css('display', 'none');

			$("#rowStartTime").attr('disabled', 'disabled');
			$("#rowRepeat").attr('disabled', 'disabled');

			$("#advancedRow").css('display', 'none');
			$("#advancedRow input").attr('disabled', 'disabled');

			$("#rowExpression").css('display', '');
			$("#rowExpression input").removeAttr('disabled');
		}
	}
	var formId = "scheduleForm";

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
	layui.use([ 'form', 'laydate', 'element' ], function() {
		var form = layui.form();
		var laydate = layui.laydate;
		var element = layui.element;

		form.on('radio(type)', function(data) {
			triggerTypeChange();
		});
	});

	$(function() {
		triggerTypeChange();
	});
</script>
<body class="bodyLayout">
	<div class="cl pd-5 bg-1 toolbarDiv" id="toolbar" style="height:30px;">
		<span class="l"> <jsp:include page="/resource/inc/toolbar.jsp" />
		</span>
	</div>
	<f:form class="layui-form"
		action="${path}/sys/schedule/saveSchedule.json" id="scheduleForm"
		method="POST">
		<f:hidden path="scheduleId" />
		<table class="table_one">
			<tr>
				<td>
					<div class="layui-form-item">
						<label class="layui-form-label"><span class='required'>*</span>
							任务 ${separator}</label>
						<div class="layui-input-block">
							<f:select path="taskClass">
								<option>请选择</option>
								<c:forEach var="task" items="${tasks}">
									<f:option value="${task.taskClass}">${task.taskName}</f:option>
								</c:forEach>

							</f:select>
						</div>
					</div>
				</td>
				<td>
					<div class="layui-form-item">
						<label class="layui-form-label"> 当前站点 ${separator}</label>
						<div class="layui-input-block">${station}</div>
					</div>
				</td>
			</tr>
			<tr>
				<td colspan=2>
					<div class="layui-form-item">
						<label class="layui-form-label"><span class='required'>*</span>
							任务模式 ${separator}</label>
						<div class="layui-input-block">
							<f:radiobutton path="triggerType" value="simple" title="简单"
								lay-filter="type" />
							<f:radiobutton path="triggerType" value="advanced" title="高级"
								lay-filter="type" />

							<f:radiobutton path="triggerType" value="cron" title="表达式"
								lay-filter="type" />
						</div>
					</div>
				</td>
			</tr>
			<tr id="rowStartTime" style="display: none">
				<td>
					<div class="layui-form-item">
						<label class="layui-form-label"> 开始时间 ${separator}</label>
						<div class="layui-input-block">
							<f:input path="startTime" autocomplete="off"
								placeholder="请输入开始时间" class="layui-input"
								onclick="layui.laydate({elem: this, istime: true, format: 'YYYY-MM-DD hh:mm:ss'})" />
						</div>
					</div>
				</td>
				<td></td>
			</tr>
			<tr id="rowRepeat" style="display: none">
				<td>
					<div class="layui-form-item">
						<label class="layui-form-label"> 执行间隔 ${separator}</label>
						<div class="layui-input-inline">
							<f:input path="repeatInterval" autocomplete="off"
								placeholder="请输入执行间隔" class="layui-input" />
						</div>
						<div class="layui-form-mid layui-word-aux">毫秒</div>
					</div>
				</td>
				<td>
					<div class="layui-form-item">
						<label class="layui-form-label"> 执行次数 ${separator}</label>
						<div class="layui-input-block">
							<f:input path="repeatCount" autocomplete="off"
								placeholder="请输入执行次数" class="layui-input" />
						</div>
					</div>

				</td>
			</tr>
			<tr id="advancedRow" style="display: none">
				<td colspan=2>
					<div class="layui-tab">
						<ul class="layui-tab-title">
							<li class="layui-this">秒</li>
							<li>分</li>
							<li>时</li>
							<li>日</li>
							<li>周</li>
							<li>月</li>
							<li>年</li>
						</ul>
						<div class="layui-tab-content">
							<div class="layui-tab-item layui-show">
								<table class="table_one">
									<tr>
										<td colspan=2'>从<f:input path="fromSecond"
												style="width:60px" class="input-text" />秒 &nbsp;到 <f:input
												path="toSecond" style="width:60px" class="input-text" />秒
											&nbsp;每隔 <f:input path="intervalSecond" style="width:60px"
												class="input-text" />秒执行
										</td>
									</tr>
									<tr>
										<td colspan=2>
											<div class="layui-form-item">
												<label class="layui-form-label"> 指定时间(秒)
													${separator}</label>
											</div>
										</td>
									</tr>
									<tr>
										<td colspan="2" align="left"><c:forEach var="allSeconds"
												items="${command.allSeconds}">
												<f:checkbox path="checkedSeconds" value="${allSeconds }"
													title="${allSeconds }" />
											</c:forEach></td>
									</tr>
								</table>
							</div>
							<div class="layui-tab-item">
								<table class="table_one">
									<tr>
										<td colspan=2'>从<f:input path="fromMinute"
												style="width:60px" class="input-text" />分 &nbsp;到 <f:input
												path="toMinute" style="width:60px" class="input-text" />分
											&nbsp;每隔 <f:input path="intervalMinute" style="width:60px"
												class="input-text" />分执行
										</td>
									</tr>
									<tr>
										<td colspan=2>
											<div class="layui-form-item">
												<label class="layui-form-label"> 指定时间(分)
													${separator}</label>
											</div>
										</td>
									</tr>
									<tr>
										<td colspan="2" align="left"><c:forEach var="allMinutes"
												items="${command.allMinutes}">
												<f:checkbox path="checkedMinutes" value="${allMinutes }"
													title="${allMinutes }" />
											</c:forEach></td>
									</tr>
								</table>
							</div>
							<div class="layui-tab-item">
								<table class="table_one">
									<tr>
										<td colspan=2'>从<f:input path="fromHour"
												style="width:60px" class="input-text" />时 &nbsp;到 <f:input
												path="toHour" style="width:60px" class="input-text" />时
											&nbsp;每隔 <f:input path="intervalHour" style="width:60px"
												class="input-text" />时执行
										</td>
									</tr>
									<tr>
										<td colspan=2>
											<div class="layui-form-item">
												<label class="layui-form-label"> 指定时间(时)
													${separator}</label>
											</div>
										</td>
									</tr>
									<tr>
										<td colspan="2" align="left"><c:forEach var="allHours"
												items="${command.allHours}">
												<f:checkbox path="checkedHours" value="${allHours }"
													title="${allHours }" />
											</c:forEach></td>
									</tr>
								</table>

							</div>
							<div class="layui-tab-item">
								<table class="table_one">
									<tr>
										<td colspan=2'>从<f:input path="fromDay"
												style="width:60px" class="input-text" />日 &nbsp;到 <f:input
												path="toDay" style="width:60px" class="input-text" />日
											&nbsp;每隔 <f:input path="intervalDay" style="width:60px"
												class="input-text" />日执行
										</td>
									</tr>
									<tr>
										<td colspan=2>
											<div class="layui-form-item">
												<label class="layui-form-label"> 指定时间(日)
													${separator}</label>
											</div>
										</td>
									</tr>
									<tr>
										<td colspan="2" align="left"><c:forEach var="allDays"
												items="${command.allDays}">
												<f:checkbox path="checkedMinutes" value="${allDays }"
													title="${allDays }" />
											</c:forEach></td>
									</tr>
								</table>
							</div>
							<div class="layui-tab-item">
								<table class="table_one">
									<tr>
										<td colspan=2>
											<div class="layui-form-item">
												<label class="layui-form-label"> 指定时间(周)
													${separator}</label>
											</div>
										</td>
									</tr>
									<tr>
										<td colspan="2" align="left"><c:forEach var="allWeeks"
												items="${command.allWeeks}">
												<f:checkbox path="checkedWeeks" value="${allWeeks }"
													title="${allWeeks }" />
											</c:forEach></td>
									</tr>
								</table>
							</div>
							<div class="layui-tab-item">
								<table class="table_one">
									<tr>
										<td colspan=2>
											<div class="layui-form-item">
												<label class="layui-form-label"> 指定时间(月)
													${separator}</label>
											</div>
										</td>
									</tr>
									<tr>
										<td colspan="2" align="left"><c:forEach var="allMonths"
												items="${command.allMonths}">
												<f:checkbox path="checkedMonths" value="${allMonths }"
													title="${allMonths }" />
											</c:forEach></td>
									</tr>
								</table>
							</div>
							<div class="layui-tab-item">
								<table class="table_one">
									<tr>
										<td colspan=2>
											<div class="layui-form-item">
												<label class="layui-form-label"> 指定时间(年)
													${separator}</label>
											</div>
										</td>
									</tr>
									<tr>
										<td colspan="2" align="left"><c:forEach var="allYears"
												items="${command.allYears}">
												<f:checkbox path="checkedYears" value="${allYears }"
													title="${allYears }" />
											</c:forEach></td>
									</tr>
								</table>

							</div>
						</div>
					</div>
				</td>
			</tr>
			<tr id="rowExpression" style="display: none">
				<td class="lableTd" colspan=2>

					<div class="layui-form-item">
						<label class="layui-form-label"> 规则 ${separator}</label>
						<div class="layui-input-block">
							<f:input path="cronExpression" autocomplete="off"
								placeholder="请输入规则" class="layui-input" />
						</div>
					</div>
			</tr>
		</table>
	</f:form>
</body>
</html>
