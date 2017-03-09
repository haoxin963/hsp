var basePath = path+'/sys/workflow';
var dg;
$(function(){
	var i=0;
	var listPath = basePath+"/listToDo.json";
	if(typeof(dataList)!="undefined" ){
		dg = $('#dg').datagrid({
		    data:dataList,
		    pagination:true, 
		    fitColumns:constants.fitColumns,
		    striped:true,
		    rownumbers:true,
		    loadFilter: function(r){
				if (r.command){ 
					return r.command;
				} else {
					return r;
				}
			}, 
		    onBeforeLoad:function(){
		    	if(i==1){
			    	var opts = $(this).datagrid('options');
			    	opts.url= listPath; 
		    	}
		    	i++;
		    }
		}); 
	}else{
		dg = $('#dg').datagrid({
		    url:listPath,
		    loadFilter: function(r){
				if (r.command){ 
					return r.command;
				} else {
					return r;
				}
			}, 
		    pagination:true, 
		    fitColumns:constants.fitColumns,
		    striped:true,
		    rownumbers:true
		}); 
	}
	
}); 


function formHandler(tp){ 
		if(tp=='ok'){
			top.ymPrompt.getPage().contentWindow.doSave($('#dg'))
		}else if(tp=='cancel'){
			top.ymPrompt.close();
		}else if(tp=='close'){
			top.ymPrompt.close();
		}
}


function rowformater(value,row,index)
{
	var id = row.ID
	var actionURL = basePath + '/toDo.do?id='+id;
	var rtn = "<a href=\""+actionURL+"\"  name=\"todoBtn\">"+value+"</a>&nbsp;&nbsp;" ;
	return rtn;
};


function toDo(entryId,stepId,stepName){
	var actionURL = basePath + '/toDo.do?entryId='+entryId+'&stepId='+stepId;
	var v = top.ymPrompt.win({title:stepName,autoClose:false,width:600,height:450,fixPosition:true,maxBtn:true,minBtn:false,handler:formHandler,btn:[],iframe:{id:'workflow',name:'workflow',src:actionURL}});
}

function searchFun(){
	var data = $("#searchForm").serializeJson(); 
  	$('#dg').datagrid('load',data); 
};

