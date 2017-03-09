<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>员工信息</title>
<jsp:include page="/resource/inc/incMeta.jsp" />
<jsp:include page="/resource/inc/incCssJs.jsp" />
<link rel="stylesheet" href="${vpath}/sys/userSettings/script/style.css"
	type="text/css" />
<script type="text/javascript"
	src="${vpath}/sys/userSettings/script/cropbox.js"></script>
</head>

<body>
	<form id="imageSave" method="post"
		action="${path}/sys/userSettings/savePhoto2.json">
		<div class="container">
			<div class="imageBox">
				<div class="thumbBox" style="border-radius:50%;"></div>
				<div class="spinner" style="display: none"></div>
			</div>

			<div class="action">
				<!-- <input type="file" id="file" style=" width: 200px">-->
				<div class="new-contentarea tc">
					<a href="javascript:void(0)" class="upload-img"> <label
						for="upload-file">请先选择图片...</label>
					</a> <input type="hidden" id="imageStr" name="imageStr"> <input
						type="file" class="" name="upload-file" id="upload-file" />
				</div>

				<!--     <input type="button"  id="btnOk"  class="Btnsty_peyton" value="OK"> -->
				<input type="button" id="btnZoomIn" class="Btnsty_peyton" value="+">
				<input type="button" id="btnZoomOut" class="Btnsty_peyton" value="-">
			</div>
	</form>
	<div class="cropped" style="padding-top:45%"></div>

	<script type="text/javascript">
		var formId = "imageSave";
		var img = "";
		function doSave(win) {
			if (img == "") {
				alert("请选择图片");
				return;
			}
			$('#imageStr').val(img);
			$("#" + formId).ajaxSubmit({
				success : function(result) {
					result = jQuery.parseJSON(result);
					if (result.status == "1") {
						win.closeAndRef();
					} else {
						alertError(result.msg);
					}
				}
			});
		}

		$(window)
				.load(
						function() {
							//$('#btnCrop').click();$("#idName").css("cssText","background-color:red!important");
							//$(".imageBox").css("cssText","background-position:88px 88px!important");$(".imageBox").css("cssText","background-size:222px 222px!important");
							var options = {
								thumbBox : '.thumbBox',
								spinner : '.spinner',
								imgSrc : ''
							}
							var cropper = $('.imageBox').cropbox(options);

							$('#upload-file').on('change', function() {
								var reader = new FileReader();
								reader.onload = function(e) {
									options.imgSrc = e.target.result;
									cropper = $('.imageBox').cropbox(options);
									getImg();
								}
								reader.readAsDataURL(this.files[0]);
								//this.files = [];
								//getImg();
							})

							$('#btnCrop').on('click', function() {
								alert("图片上传喽");
							})
							function getImg() {
								img = cropper.getDataURL();
								$('.cropped').html('');
								$('.cropped')
										.append(
												'<img src="'
														+ img
														+ '" align="absmiddle" style="width:92px;margin-top:4px;border-radius: 50%;box-shadow:0px 0px 12px #7E7E7E;" ><p>92px*92px</p>');
								//alert("nnn")
							}
							$(".imageBox").on("mouseup", function() {
								getImg();
								//alert("move");
							});
							$("body").mouseover(function() {
								getImg();
							});
							$(".imageBox").on("mouseup", function() {
								getImg();
							});

							$('#btnZoomIn').on('click', function() {
								cropper.zoomIn();
							})

							$('#btnZoomOut').on('click', function() {
								cropper.zoomOut();
							})

							// 	$('#btnOk').on('click', function(){
							// 		$('#imageStr').val(img);
							// 		$("#" + formId).form(
							// 					{
							// 						success : function(result) {
							// 							result = jQuery.parseJSON(result);
							// 							if (result.status == "1") {
							// 								parent.jQuery.messager
							// 										.alert(constants.alertTitle,
							// 												'上传成功！');
							// 									window.parent.location.reload();
							// 									window.close();
							// 							} else {
							// 								parent.jQuery.messager.alert(
							// 										constants.alertTitle, result.msg);
							// 							}
							// 						}
							// 					});
							// 			$('#' + formId).submit();
							// 	})

							// 	$('#btnOk').on('click', function(){
							// 		$("#" + formId).form(
							// 					{
							// 						success : function(result) {
							// 							result = jQuery.parseJSON(result);
							// 							if (result.status == "1") {
							// 								parent.jQuery.messager
							// 										.alert(constants.alertTitle,
							// 												'上传成功！');
							// 									top.ymPrompt.close();

							// 							} else {
							// 								parent.jQuery.messager.alert(
							// 										constants.alertTitle, result.msg);
							// 							}
							// 						}
							// 					});
							// 			$('#' + formId).submit();
							// 	})
						});
	</script>
</body>
</html>
