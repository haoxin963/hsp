
String.prototype.endWith=function(str){     
	  var reg=new RegExp(str+"$");     
	  return reg.test(this);        
}



$.ajax({
	url : path+'/sys/wf/nextTask.json?taskId='+taskId,
	type : 'POST',
	data : datas.join("&"),
	dataType : "json",
	success : function(r) {
		r = r.nextTask;
		var i = 0;
		for (p in r) {
			var assignee = r[p].assignee;
			$("#name").html(r[p].name);
			if(assignee!=""){
				assignee = assignee.trim();
			}
			if(assignee.endWith("Assignee}") || assignee.endWith("CandidateUsers}") || assignee.endWith("CandidateGroups}")){
					document.getElementById("assigneeInput").style.display="block";
					$("#assignee").parent().parent().show();
					assigneeExp = assignee.replace("$","");
					assigneeExp = assigneeExp.replace("}","");
					assigneeExp = assigneeExp.replace("{",""); 
			}else if(assignee == "${createUser}"){
					$("#assignee").parent().parent().show();
					$("#assignee").html("流程发起人");
			}else{ 
				if(assignee!=""){
					$("#assignee").html(r[p].assignee);
					$("#assignee").parent().parent().show();
				}
				if(r[p].candidateUsers!=""){
					$("#candidateUsers").html(r[p].candidateUsers);
					$("#candidateUsers").parent().parent().show();
				}
				if(r[p].candidateGroups!=""){
					$("#candidateGroups").html(r[p].candidateGroups);
					$("#candidateGroups").parent().parent().show();
				}
			}
			i++;
		}
		if(i==0){
			$("#name").html("结束流程");
		}
	},
	error : function(msg) {
		alert(msg);
	}
}); 