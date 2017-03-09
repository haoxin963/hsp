<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>roleToUser</title>
<jsp:include page="/resource/inc/incMeta.jsp" />
<jsp:include page="/resource/inc/incCssJs.jsp" />
</head>

<body>

	<div class="layui-tab" lay-filter="role" style="height:480px">
		<ul class="layui-tab-title">
			<li class="layui-this">角色已经关联的用户</li>
			<li>编辑</li>
		</ul>
		<div class="layui-tab-content">
			<div class="layui-tab-item layui-show">
				<iframe id="viewFrame" scrolling="yes" frameborder="0"
					src="${path}/sys/rbac/role/doRoleUser.do?roleId=${role.id}"
					style="width:100%;height:85%;"></iframe>
			</div>
		</div>
	</div>


	<script>
		var index = 0;
		layui
				.use(
						'element',
						function() {
							var element = layui.element();

							//一些事件监听
							element
									.on(
											'tab(role)',
											function(data) {
												var viewFrame = document
														.getElementById("viewFrame");
												index = data.index;
												if (index == 1) {
													viewFrame.src = "${path}/sys/rbac/role/toGiveRole.do?roleId=${role.id}";
												} else {
													viewFrame.src = "${path}/sys/rbac/role/doRoleUser.do?roleId=${role.id}";
												}
											});
						});

		function doSave(obj) {
			if (index == 0) {
				obj.closeAndRef();
			} else {
				document.getElementById("viewFrame").contentWindow.doSave(obj);
			}
		}
	</script>
</body>
</html>
