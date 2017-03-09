/**
* 修改密码的方法
*/
function doModifyPwd(){
	//取值
	var $oldpwd = $("#oldpwd");
	var $newpwd = $("#newpwd");
	var $renewpwd = $("#renewpwd");
	
	//判空	
	if($.trim($oldpwd.val())==""){
		alertWarning("旧密码必填");
		return;
	}else if($.trim($newpwd.val())==""){
		alertWarning("新密码必填");
		return;
	}else if($.trim($renewpwd.val())==""){
		alertWarning("重复码必填");
		return;
	}else if($renewpwd.val()!=$newpwd.val()){
	    alertWarning("两次密码输入不一致，请重新输入");
		return;
	}else if($newpwd.val().length<6){
		alertWarning("新密码长度必须大于6位");
		return;
	}else if($newpwd.val() == $oldpwd.val()){
		alertWarning("新密码不能与旧密码相同");
		return;
	}
	
	$.ajax({
		type: 'POST',
		url: path+"/sys/rbac/user/doModifyUserPwd.json",
		data:{
			oldpwd:$.trim($oldpwd.val()),
			newpwd:$.trim($newpwd.val())
		},
		dataType:"json",
		success:function(r){
			if(r.status=='1'){					 
				parent.layer.open({
					  content: '修改密码成功！请重新登录系统',
					  yes: function(index, layero){
						  window.top.location.href = path +"/"+custId+".login";
					  }
					});
				return;
			}else{
				alertWarning(r.msg);
				return;					 
			}
		}		
	});
}
