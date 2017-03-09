var basePath = path+'/druid';
var dg;

function myLoader(param,success,error){
	var that = $(this);
	var opts = that.datagrid("options");
	if (!opts.url) {
		return false;
	}
	var cache = that.data().datagrid.cache;
	if (!cache) {
		$.ajax({
			type : opts.method,
			url : opts.url,
			data : param,
			dataType : "json",
			success : function (data) {
				that.data().datagrid['cache'] = data;
				success(bulidData(data));
			},
			error : function () {
				error.apply(this, arguments);
			}
		});
	} else {
		success(bulidData(cache));
	}
	
	function bulidData(data) {
		var temp = $.extend({},data);
		var tempRows = [];
		var start = (param.page - 1) * parseInt(param.rows);
		var end = start + parseInt(param.rows);
		var rows = data.Content;
		for (var i = 0; i < rows.length; i++) {
			if(rows[i]){
				tempRows.push(rows[i]);
			}else{
				break;
			}
		}
		temp.rows = tempRows;
		return temp;
	}
}

$(function(){
	var i=0;
	var listPath = basePath+"/sql.json";
	if(false){
	 
	}else{
		dg = $('#dg').datagrid({
		    url:listPath,
			loadFilter: function(r){
				if (r.command){ 
					return r.Content;
				} else {
					return r;
				}
			}, 
			 loader:myLoader, 
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

