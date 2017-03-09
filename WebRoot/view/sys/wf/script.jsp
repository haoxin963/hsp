<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<script>
	function getWin(){
		//iframe模式时
		return document.getElementById("workFlowFormIframe").contentWindow;
		//return window;
	}

	function appendInput(inputId,value,vkey){
		var win = getWin();
		var myform = win.document.getElementById(win.formId); 
		if(win.document.getElementById(inputId)==null){
			var v = document.createElement("input");
			v.type = "hidden"; 
			v.name = inputId;
			v.id = inputId;
			v.value = value;
			if(vkey){
				v.setAttribute("vkey",true);
			}
			myform.appendChild(v); 
		}
	}
	
	function formSetup(){
		//wfKey 
		appendInput("wfKey","${requestScope.wfKey}");
		//taskId    
		appendInput("taskId","${requestScope.taskId}");		
		//processInstanceId    
		appendInput("processInstanceId","${param.processInstanceId}");		
		//variables    
		var vkey = getVariables(getWin());
		appendInput("variables",vkey.join(","));		 
	} 
	
	function getVariables(win){
		var values = win.$("#"+win.formId).serializeArray(); 
		var vkey = [];
		for(p in values){
			 var v = win.$("#"+values[p].name).attr("vkey");
			 if(v){
			 	vkey.push(values[p].name);
			 }
		}
		return vkey;
	}
	 
	function formSubmit(){
		var win = getWin();
		win.doSave(window);
	}
	 
	function start(){
		formSetup();		
		formSubmit();
	}
	
	function redirectToSuccess(){
		window.location = "${path}/sys/wf/success.jspx";
	} 
	</script>
