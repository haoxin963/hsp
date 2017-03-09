var basePath = path+'/sys/notice/notice';
var pk = "id";
doList(basePath + "/doViewList.json");


 
function doView(id){
	var actionURL = basePath+"/doView.do?noticeId="+id+"&t="+new Date().getTime();
	self.location=actionURL;
}
/*
function showBack(id){
	var backURL = basePath+"/doView.do?id="+id+"&t="+new Date().getTime();
	var titleTxt = constants.newForm;
	var v = top.ymPrompt.win({title:titleTxt,autoClose:false,width:850,height:580,fixPosition:true,maxBtn:true,minBtn:false,handler:formHandler,btn:[['关闭','close']],iframe:{id:'notice',name:'notice',src:actionURL}});
}
*/

function rowType(row, rowIndex, colIndex, options){
	var str=getRowVal(row, "alluser");
	if(str == 1){
		return "一般";
	}else{
		return "推送";
	}
}

function rowComment(row, rowIndex, colIndex, options){
	var str=getRowVal(row, "iscomment");
	if(str == 1){
		return "是";
	}else{
		return "否";
	}
};

function rowMajor(row, rowIndex, colIndex, options){
	var str=getRowVal(row, "isMajor");
	if(str == 1){
		return "<font color='red'>！</font>";
	}else{
		return "";
	}
};

function rowTitle(row, rowIndex, colIndex, options){
	var retStr = "<a href='javascript:void(0);'";
	var str=getRowVal(row, "userId");
	var id=getRowVal(row, "id");
	var alluser=getRowVal(row, "alluser");
	var name=getRowVal(row, "name");
	var title=getRowVal(row, "title");
	if(str == null||str==""){
		retStr +="onClick='doView("+id+");'>";
		retStr +="<b>["+name+"] "+title+"</b></a>";
	}else{
		retStr +="onClick='doView("+id+");'>";
		retStr +="["+name+"] "+ title +"</a>";
	}
	return retStr;
};

$(document).keypress(function(event){
	if(event.keyCode==13){
		doList();
	}
});
