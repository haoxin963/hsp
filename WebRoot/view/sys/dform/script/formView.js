var dg;
$(function(){
	var i=0;
	if(typeof(dataList)!="undefined"){
		dg = $('#dg').datagrid({
		    data:dataList,
		    pagination:true,
		    toolbar: '#tb',
		    fitColumns:true,
		    striped:true,
		    rownumbers:true,
		    singleSelect:true,
		    onBeforeLoad:function(){
		    	if(i==1){
			    	var opts = $(this).datagrid('options');
			    	opts.url=path+'/dform/formview/list.json?formId='+formId; 
		    	}
		    	i++;
		    }
		}); 
	}else{
		dg = $('#dg').datagrid({
		    url:path+'/dform/formview/list.json?formId='+formId,
		    pagination:true,
		    toolbar: '#tb',
		    fitColumns:true,
		    striped:true,
		    rownumbers:true,
		    singleSelect:true
		}); 
	}
}); 