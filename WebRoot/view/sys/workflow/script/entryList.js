var basePath = path+'/sys/workflow';
var dg;
$(function(){
	var i=0;
	var listPath = basePath+"/listEntry.json";
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
	var id = row.ENTRY_ID

	var rtn = "<a href=\"javascript:viewEntry("+id+")\"  name=\"viwEntryBtn\">查看详情</a>&nbsp;&nbsp;" ;
	return rtn;
};

function viewEntry(id){
	titleTxt = '流程详情';
	var url = basePath + '/viewEntry.do?entryId='+id;
	var v = top.ymPrompt.win({title:titleTxt,autoClose:false,width:800,height:600,fixPosition:true,maxBtn:true,minBtn:false,handler:formHandler,btn:[[constants.cancel,'cancel']],iframe:{id:'entryView',name:'entryView',src:url}});
	
}

function searchFun(){
	var data = $("#searchForm").serializeJson(); 
  	$('#dg').datagrid('load',data); 
};

