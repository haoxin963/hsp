var basePath = path+'/sys/org/employee';
var pk = "id";
doList(basePath + "/doAdressBook.json");

$(document).keypress(function(event){
	if(event.keyCode==13){
		doList();
	}
});

function report(exportType){
	var url = basePath+"/doList.xlsx";
	if(exportType>2){
		url = basePath+"/doList.pdfx";
	}
	if(exportType==2 || exportType==4){
		url +="?all=1";
	}
	var exportHead = [];
	$("#gridThead").children().each(function(i){
   		if($(this).attr("report")=="true"){
   			exportHead.push(jQuery.trim($(this).text()));
   		}
 	});  
 	if(exportHead.length>0){
 		$('#exportHead').val(exportHead.join(","));
 		$('#exportTitle').val(document.title);
 	}
	var data = $("#searchForm").serializeJson();
	var searchForm =  document.getElementById("searchForm");
	searchForm.setAttribute("action",url); 
	searchForm.submit();
};

/************************/

	var setting = {
			data: {
				simpleData: {
					enable: true,
					idKey:"id",
					pIdKey:"parentId"
				}		
			},
			callback: { 
		        onClick: onTreeClickBack  
		    }  
		};
 		 
		function onTreeClickBack(){ 
			var nodes = zTree.getSelectedNodes();
			$("#deptId").val("");
			$("#postId").val("");
			if(nodes.length>0){				
				var nid = nodes[0].id;
				var cls = nodes[0].cls;
				nid = nid.substring(4);
				if(cls == "dept"){							
					$("#deptId").val(nid);
				}else{	
					$("#postId").val(nid);	
				}				 
				doList();
			}
		};
		
		$(function(){
			 createTree(); 
		}); 


