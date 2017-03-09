var basePath = path+'/sys/monitor/log/userLog';
var pk = "id";
doList(basePath + "/doList.json");

function operate(row, rowIndex, colIndex, options)
{
	var id = getRowVal(row, "id");
	var str = getButton("delete", "doDelete('"+ id + "')","&#xe6e2;");
	return str;
};

function rowformater1(row, rowIndex, colIndex, options)
{
	var value=getRowVal(row, "logType");
    return value=="1" ? "PC Web" : "移动端";
};

$(document).keypress(function(event){
	if(event.keyCode==13){
		doList();
	}
});
 
