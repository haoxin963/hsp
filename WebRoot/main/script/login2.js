var formId = "loginForm";
$(function(){
	$('.loginbox').css({
		'position' : 'absolute',
		'left' : ($(window).width() - 692) / 2
	});
	$(window).resize(function() {
		$('.loginbox').css({
			'position' : 'absolute',
			'left' : ($(window).width() - 692) / 2
		});
	})
	
	//初始化输入框提示信息
	initInputTips();
	
	//按enter键就登录
	$(document).keypress(function(event){
		if(event.keyCode==13){
			doLogin();
		}
	});
	
});


/**
* 登录的方法
*/
function doLogin(){
	//取值
	var $username = $("#username");
	var $password = $("#password");
	var $verifycode = $("#verifycode");
	var $savepwd = $("#savepwd");
	
	if (valid()) {
		$("#"+formId).ajaxSubmit({
			success : function(resObj) {
				resObj = jQuery.parseJSON(resObj);
				if(resObj["status"]=='1'){					 
					//保存用户密码到cookie
					if($savepwd.is(":checked")){
						var cname = $username.val()+CUSERID;
						var cvalue= $username.val()+"_"+$password.val()
						setCookie(cname,cvalue,30);
					}
					
					//跳转主页面
					window.location.href = path+"/main/main.jsp";
					return;
				}else if(resObj["status"]=='2'){
					//用户名不存在
					$username.val("");
					$password.val("");			 
				}else if(resObj["status"]=='3'){
					//密码错误
					$password.val("");				 
				}else if(resObj["status"]=='4'){
					//用户被禁用
					$username.focus(); 
				}else if(resObj["status"]=='5'){
					//验证码错误
					$verifycode.val("");					 
				}else if(resObj["status"]=='0'){
					//系统错误							 
				}
				//刷新验证码
				refreshImg();
				$verifycode.val("");
				
				//显示错误消息
				alertWarning(resObj["msg"]);
			}
		});

	}
}

/**
 *页面初始化提示信息
 */
function initInputTips()
{
	//用户名
	$("#username").focus(function(){
		
	}).blur(function(){
		if($.trim($(this).val())==""){
			
		}else{
			//检查cookie,填充密码
			var cname = $("#username").val() + CUSERID;
			var cvalue = getCookie(cname);
			if(cvalue!=""){
				var pwd = cvalue.substring(cvalue.indexOf("_")+1,cvalue.length);
				$("#password").val(pwd);
			}
		}
	});
}

function alertWarning(msg){
	parent.layer.msg(msg, {
		icon : 5,
		shift : 6,
		time : 500
	});
}

/**
*刷新验证码的方法
*/
function refreshImg(){
	var $vfimg = $("#verifyImg");
	$vfimg.attr("src",$vfimg.attr("src")+"?a="+Math.random());
}

/**
*获得cookie的方法
*/
function getCookie(c_name)
{
	if (document.cookie.length>0)
	{
	  c_start=document.cookie.indexOf(c_name + "=");
	  if (c_start!=-1)
	  { 
	    c_start=c_start + c_name.length+1;
	    c_end=document.cookie.indexOf(";",c_start);
	    if (c_end==-1) c_end=document.cookie.length;
	    return unescape(document.cookie.substring(c_start,c_end));
	  }
	}
	return "";
}

/**
*设置cookie的方法
*/
function setCookie(c_name,value,expiredays)
{
	var exdate=new Date();
	exdate.setDate(exdate.getDate()+expiredays);
	document.cookie = c_name+ "=" +escape(value)+ ((expiredays==null) ? "" : ";expires="+exdate.toGMTString());
}
