function heartbeatBack(r){
	try{
		if(r.msg==0){
			$(".msgCount").fadeOut();
			$(".msgCount").html(0);
		}else{
			$(".msgCount").html(r.msg);
			$(".msgCount").fadeIn();
		} 
		//$("#userOnline").html(r.online);
	}catch(e){
	}
};

function heartbeatFun(){
	$.ajax({
	   type: "get",
	   dataType:"json",
	   url: path+"/main/heartbeat.jsp?t="+Math.random(),
	   success: function(r){
			heartbeatBack(r);
	   }
	});
}

function heartbeat(){
	heartbeatFun();
	setTimeout(heartbeat, 60000);//1分钟一次
};
 
$(document).ready(function(){
 	 heartbeat();
 });
