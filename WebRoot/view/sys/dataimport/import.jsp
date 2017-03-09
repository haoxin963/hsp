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
		<title>数据导入</title>
		<jsp:include page="/com/inc/incCssJs.jsp">
			<jsp:param name="com" value="core"></jsp:param>
		</jsp:include>
	</head>
	<body class="bodyLayout">
		<form id="importForm" class="inputForm" action="${path}/sys/dataimport/doImport.do" enctype="multipart/form-data" method="post">
			<table cellspacing="1" cellpadding="1" border="0"
				class="inputFormTable" width="100%">
				<tr>
					<td class="lableTd" style="padding-right: 10px">
						步骤1：
					</td>
					<td class="valueTd" style="padding-left: 10px">
						<a
							href="${path}/sys/dataimport/exportTemplate.xlsx?type=${command.type}&entity=${command.entity}" style="font-size: 13px;color:red">点这儿下载模版</a>&nbsp;后请根据模版格式填写要导入的数据
					</td>
				</tr>
				<tr style="display: none">
					<td class="lableTd" style="padding-right: 10px">
						步骤2：	请选择XLS文件上传
					</td>
					<td class="valueTd" style="padding-left: 10px">
						<input type="file" name="dataFile">
						<input type="hidden" name="type" value="${command.type}">
						<input type="hidden" name="entity" value="${command.entity}">
						<input type="submit" value="导入">

					</td>
				</tr>
				<tr>
					<td class="lableTd" style="padding-right: 10px">
						步骤2：
					</td>
					<td class="valueTd" style="padding-left: 10px">
						<div
							style="margin-top: 10px; overflow: hidden; height: 100%; width: 100%";>
							<jsp:include page="/com/swfupload/swfUpload.jsp">
								<jsp:param name="filePath" value="/temp" />
								<jsp:param name="type" value="*.xls" />
								<jsp:param name="isDB" value="false" />
								<jsp:param name="single" value="false" />
								<jsp:param name="rename" value="false" />
								<jsp:param name="fileSize" value="2000" />
								<jsp:param name="btnText" value="点这儿上传文件" />
								<jsp:param name="mode" value="auto" />
								<jsp:param name="isListExisting" value="false" />
								<jsp:param name="seqId" value="1" />
							</jsp:include>
							<div>
								<input type="hidden" path="attachment" maxlength='150'
									value="/temp" />
					</td>
				</tr>
			</table>
		</form>
	</body>
	<script type="text/javascript">
		function callPageFun(file, serverData){
			var filePath = "/temp/"+serverData.name;
			var data = {
				filePath:filePath,
				type:'${command.type}',
				entity:'${command.entity}'
			};
			
			var imageSrc = path+"/com/images/loading.gif";
			var disabledImageZone = document.getElementById("disabledImageZone");
			if (!disabledImageZone) {
				disabledImageZone = document.createElement("div");
				disabledImageZone.setAttribute("id", "disabledImageZone");
				disabledImageZone.style.position = "absolute";
				disabledImageZone.style.zIndex = "1000";
				disabledImageZone.style.left = "0px";
				disabledImageZone.style.top = "5px";
				disabledImageZone.style.width = "100%";
				disabledImageZone.style.height = "99%";
				disabledImageZone.style.background = "rgb(0, 0, 0)";
				disabledImageZone.style.opacity = "0.1";

				disabledImageZone.align = "center";
				var imageZone = document.createElement("img");
				imageZone.setAttribute("id", "imageZone");
				imageZone.setAttribute("src", imageSrc);
				imageZone.style.position = "absolute";
				imageZone.style.top = "48%";
				disabledImageZone.appendChild(imageZone);
				document.body.appendChild(disabledImageZone);
			} else {
				$("imageZone").src = imageSrc;
				disabledImageZone.style.visibility = "visible";
			}
			window.location.href= "${path}/sys/dataimport/doHughImport.do?type=${command.type}&entity=${command.entity}&filePath="+filePath;
		}
	</script>
</html>