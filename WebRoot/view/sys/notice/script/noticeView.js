var basePath = path+'/sys/notice/notice';
var dg;
$(function(){
	var i=0;
	var listPath = basePath+"/doViewList.json";
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
 
function rowComment(value,row,index){
	var retStr="&nbsp;"+ row.trueName +"&nbsp;&nbsp;(" + row.userName +")&nbsp; 发表于&nbsp;"+ row.dateTime +"</br></br>";
	retStr += row.content;
	return retStr;
};

function doBack(){
//	var actionURL = basePath+"/doViewList.do?t="+new Date().getTime();
//	self.location=actionURL;
	history.back();
}

function commentSubmit(noticeId){
	var content = $("#commentContent").val();
	if(content.length<1){
		alert("请输入评论内容");
		return false;
	}
	var actionURL = basePath+"/../noticeComment/doSave.json";
	$.post(actionURL,{"noticeId":noticeId,"content":content},function(data){
		location.reload();
	});
	$("#commentContent").val("");
}

function searchFun(){
	var data = $("#searchForm").serializeJson(); 
  	$('#dg').datagrid('load',data); 
};