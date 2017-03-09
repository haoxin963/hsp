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
			    	opts.url=path+'/dform/form/list.json'; 
		    	}
		    	i++;
		    }
		}); 
	}else{
		dg = $('#dg').datagrid({
		    url:path+'/dform/form/list.json',
		    pagination:true,
		    toolbar: '#tb',
		    fitColumns:true,
		    striped:true,
		    rownumbers:true,
		    singleSelect:true
		}); 
	}
}); 

function start(){ 
	var row = dg.datagrid('getSelected');
	if (row){
		top.jQuery.messager.confirm('Confirm','确认开启任务?',function(r){
			if (r){
				var index = dg.datagrid('getRowIndex',row);
				jQuery.post(path+'/dform/form/start.json',{id:row.id},function(){
					$('#dg').datagrid('reload');
				});
			}
		});
	}else {
		top.$.messager.alert('提示', '请勾选要开启的任务！', 'error');
	} 
	return false;
};

function stop(){ 
	var row = dg.datagrid('getSelected');
	if (row){
		top.jQuery.messager.confirm('Confirm','确认停止任务?',function(r){
			if (r){
				var index = dg.datagrid('getRowIndex',row);
				jQuery.post(path+'/dform/form/stop.json',{id:row.id},function(){
					$('#dg').datagrid('reload');
				});
			}
		});
	}else {
		top.$.messager.alert('提示', '请勾选要停止的任务！', 'error');
	} 
	return false;
};
function toAdd(){
	var p = parent.jext.dialog({
		title : '新增表单',
		href : path+'/dform/form/toAdd.do', 
		width : 1020,
		height : 500,
		buttons : [ {
			text : '保存更新',
			handler : function() {
				var f = p.find('form');
				f.form('submit', {
					url : path+'/dform/form/add.json',
					onSubmit: function(param){
						$('#formTable').datagrid('acceptChanges');
				        var rows = $('#formTable').datagrid('getRows');
				       	$.each(rows,function(i,row){
				      		for(var rowKey in row){
					      		var key = 'fields['+i+'].'+rowKey;
					       		param[key] = row[rowKey];
				      		}
				       	});
				    },
					success : function(d) {
						if(d){
							 $('#dg').datagrid('reload');
							p.dialog('close');
						} 
						jQuery.messager.show({
							title:'提示',
							msg:'操作成功!',
							showType:'fade',
							timeout:1000,
							style:{
								right:'',
								bottom:''
							}
						});
					}
				});
			}
		} ],
		onLoad : function() {
			  
		}
	});
}

function toEdit() {   
	var rows = $('#dg').datagrid('getSelections'); 
	if (rows.length == 1) {
		var p = parent.jext.dialog({
			title : '编辑',
			href : path+'/dform/form/toEdit.do?formId='+rows[0].formId, 
			width : 1020,
			height : 500,
			buttons : [ {
				text : '保存更新',
				handler : function() {
					var f = p.find('form');
					f.form('submit', {
						url : path+'/dform/form/edit.json',
						onSubmit: function(param){
							$('#formTable').datagrid('acceptChanges');
					        var rows = $('#formTable').datagrid('getRows');
					        console.log(rows);
					       	$.each(rows,function(i,row){
					      		for(var rowKey in row){
						      		var key = 'fields['+i+'].'+rowKey;
						       		param[key] = row[rowKey];
					      		}
					       	});
					    },
						success : function(d) {
							if(d){
								 $('#dg').datagrid('reload');
								p.dialog('close');
							} 
							jQuery.messager.show({
								title:'提示',
								msg:'操作成功!',
								showType:'fade',
								timeout:1000,
								style:{
									right:'',
									bottom:''
								}
							});
						}
					});
				}
			} ],
			onLoad : function() {
				  
			}
		});
	} else if (rows.length > 1) {
		top.$.messager.alert('提示', '请选择一条记录！', 'error');
	} else { 
		top.$.messager.alert('提示', '请选择要编辑的记录！', 'error');
	}
};

function toEditPage() {   
	var rows = $('#dg').datagrid('getSelections'); 
	if (rows.length == 1) {
		var p = parent.jext.dialog({
			title : '编辑',
			href : path+'/dform/form/toEditPage.do?formId='+rows[0].formId, 
			width : 700,
			height : 450,
			buttons : [ {
				text : '下一步',
				handler : function() {
					var f = p.find('form');
					f.form('submit', {
						url : path+'/dform/form/editPage.json',
						success : function(d) {
							if(d){
								var data = eval('(' + d + ')')
								p.dialog({
									titlt:'编辑',
									href: path+'/dform/form/toEditPageField.do?formId='+rows[0].formId,
									width : data.pageWidth,
									height : data.pageHeight,
									buttons : [ {
										text : '保存更新',
										handler : function() {
											var f = p.find('form');
											f.form('submit', {
												url : path+'/dform/form/editPageField.json',
												success : function(d) {
													if(d){
														 $('#dg').datagrid('reload');
														p.dialog('close');
													} 
													jQuery.messager.show({
														title:'提示',
														msg:'操作成功!',
														showType:'fade',
														timeout:1000,
														style:{
															right:'',
															bottom:''
														}
													});
												}
											});
										}
									}]
								});
							} 
						}
					});
				}
			} ]
		});
	} else if (rows.length > 1) {
		top.$.messager.alert('提示', '请选择一条记录！', 'error');
	} else { 
		top.$.messager.alert('提示', '请选择要编辑的记录！', 'error');
	}
};

function view(){
	var rows = $('#dg').datagrid('getSelections'); 
	if (rows.length == 1) {
		window.open (path+'/dform/formview/list.do?formId='+rows[0].formId);
	}
	else if (rows.length > 1) {
		top.$.messager.alert('提示', '请选择一条记录！', 'error');
	} else { 
		top.$.messager.alert('提示', '请选择要查看的记录！', 'error');
	}
}

function searchFun(){
	var data = $("#searchForm").serializeJson(); 
  	$('#dg').datagrid('load',data); 
};



