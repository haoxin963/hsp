var basePath = path+'/sys/rbac/role';
var dg;
$(function(){
	var i=0;
	var listPath = basePath+"/toChooseRole.json";
	 
	dg = $('#dg').datagrid({
	    url:listPath,
		loadFilter: function(r){
			if (r.command){ 
				return r.command;
			} else {
				return r;
			}
		}, 
	    pagination:false, 
	    fitColumns:constants.fitColumns,
	    striped:true,
	    rownumbers:true,
	    singleSelect:true,
	    onLoadSuccess:function(data){
	    	//勾选用户已经被分配的角色
	    	$(data.rows).each(function(rIndex,d){
	    		 for(var v in select_roles){
	    		 	var rid = d["id"];	    		 	    			 
	    			if(rid==select_roles[v]){	    				
	    				$('#dg').datagrid("selectRow",rIndex);
	    			}
	    		 }
	    	});
	    }
	}); 
}); 


function getSelectedRoles(){
   return $('#dg').datagrid('getSelections');
}
 
function searchFun(){
	var data = $("#searchForm").serializeJson(); 
  	$('#dg').datagrid('load',data); 
};
