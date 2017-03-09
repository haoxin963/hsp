$.fn.bsgrid.defaults.requestParamsName.pageSize = 'rows';
$.fn.bsgrid.defaults.requestParamsName.curPage = 'page';
var gridObj;
var dialog;

(function($) {
	$.fn.serializeJson = function() {
		var serializeObj = {};
		var array = this.serializeArray();
		var str = this.serialize();
		$(array).each(
				function() {
					if (serializeObj[this.name]) {
						if ($.isArray(serializeObj[this.name])) {
							serializeObj[this.name].push(this.value);
						} else {
							serializeObj[this.name] = [
									serializeObj[this.name], this.value ];
						}
					} else {
						serializeObj[this.name] = this.value;
					}
				});
		return serializeObj;
	};
})(jQuery);

/**
 * 初始化表格
 * @param url
 */
function loadGrid(url) {
	gridObj = $.fn.bsgrid.init('searchTable', {
		url : url,
		pageSizeSelect : true,
		pageSize : 15,
		rowHoverColor : true,
		stripeRows : true,
		multiSort : true,
		lineWrap : true,
		autoLoad : false,
		pageIncorrectTurnAlert : false,
		displayBlankRows : false,
		displayPagingToolbarOnlyMultiPages : true,
		pagingLittleToolbar : true,
        extend: {
            settings: {
                supportColumnMove: true // if support extend column move, default false
            }
        }
	});
}

/**
 * 获取chekbox选择行的id
 * @returns
 */
function getCheckedIds() {
	return gridObj.getCheckedValues(pk);
}

/**
 * 获取chekbox选择行
 * @returns
 */
function getCheckedRows(){
	return gridObj.getCheckedRowsRecords();
}

/**
 * 刷新表格
 */
function refreshTable(){
	gridObj.refreshPage();
}

/**
 * 获取行的某个字段值
 * @param row
 * @param field
 * @returns
 */
function getRowVal(row,field){
	return gridObj.getRecordIndexValue(row, field);
}

/**
 * 删除
 * 
 * @param id
 */
function doDelete(id) {
	var ids = [];
	if (typeof (id) == "undefined" || id == '') {
		ids = getCheckedIds();
	} else {
		ids.push(id);
	}
	if (ids.length < 1) {
		alertError('请选择要删除的记录!');
		return;
	}
	parent.layer.confirm('确定要删除此数据?', function(index) {
		function success(result) {
			if (result.status == '1') {
				gridObj.refreshPage();
				parent.layer.close(index);
				alertOk("删除成功！");
			} else {
				alertError('删除失败!');
			}
		}
		jQuery.ajax({
			type : 'POST',
			url : basePath + "/doDelete.json",
			traditional : true,
			data : {
				"ids" : ids
			},
			success : success,
			dataType : "json"
		});
	});
}

/**
 * 一般提示
 * 
 * @param msg
 */
function alertTip(msg){
	parent.layer.msg(msg, {
		icon : 0,
		time : 1000
	});
}

/**
 * 警告提示
 * @param msg
 */
function alertWarning(msg){
	parent.layer.msg(msg, {
		icon : 5,
		shift : 6,
		time : 1000
	});
}
/**
 * 成功提示
 * 
 * @param msg
 */
function alertOk(msg){
	parent.layer.msg(msg, {
		icon : 1,
		time : 1000
	});
}

/**
 * 失败提示
 * 
 * @param msg
 */
function alertError(msg){
	parent.layer.msg(msg, {
		icon : 2,
		time : 1000
	});
}

/**
 * 弹框
 * @param title 弹框名称
 * @param url 弹框url
 * @param w 弹框宽度
 * @param h 弹框高度
 * @param id 弹框id
 * @param btn 弹框按钮
 */
function showDialog(title,url,w,h,id,btn){
	if (title == null || title == '') {
		title='窗口';
	};
	if (url == null || url == '') {
		url="404.html";
	};
	if (w == null || w == '') {
		w=800;
	};
	if (h == null || h == '') {
		h=($(window).height() - 50);
	};
	if(btn == null || btn == undefined) {
		btn = ['确定','关闭'];
	}
	dialog = parent.layer.open({
		type: 2,
		area: [w+'px', h +'px'],
		fix: false,
		maxmin: true,
		anim:0,//弹框动画：0-6
		shade:0.4,
		title: title,
		moveOut:true,
		resize: true,
		btn: btn,
		btnAlign: 'c',
		id:id,
		yes: function(index, layero){
			yesCallback(index, layero,id);
		},
		cancel: function(index, layero){
			layer.close(index); 
	    },
		content: url
	});
}

/**
 * 弹框点确定的回调函数
 * @param index
 * @param layero
 * @param id
 * @returns {Boolean}
 */
function yesCallback(index, layero,id){
	var iframeWin = parent.window[layero.find('iframe')[0]['name']];
	iframeWin.doSave(window);
	return false;
}

/**
 * 查询参数
 * @returns
 */
function paramJoin() {
	var list = $("#searchForm").serializeJson();
	return list;
}

/**
 * 表格查询
 */
function doList(url) {
	if(url != undefined && url != null && url != '') {
		loadGrid(url);
	}
	var list = paramJoin();
	gridObj.search(list);
}

/**列头、属性默认实现*/
function loadReportHead(){
	try{
		var exportHead = [];
		var exportField = [];
		
		var searchForm =  document.getElementById("searchForm");
		var f = "exportField";
		if(!document.getElementById(f) && searchForm){
			var v = document.createElement("input");
			v.type = "text";
			v.style.display="none";
			v.name = f;
			v.id = f;
			v.value = "";
			searchForm.appendChild(v);
		}
		if($('#exportHead').val()=="" ){
			$("#searchTable  tr:eq(0)").children().each(function(i) {
				if ($(this).attr("report") == "true") {
					exportHead.push(jQuery.trim($(this).text()));
					exportField.push($(this).attr("w_index"));
				}
			}); 
			if(exportHead.length>0){
				$('#exportHead').val(exportHead.join(","));
				$('#'+f).val(exportField.join(","));
			}
		}
		if($('#exportTitle').val()==""){
			$('#exportTitle').val(document.title);
		}
	}catch(e){
	}
};

/**
 * 导出
 * @param urlPrefix 请求url
 * @param isAll 是否导出全部，默认当前页
 */
function report(urlPrefix,isAll){ 
	try{
		var prefix = "doList";
		if(urlPrefix){
			prefix = urlPrefix;
		}
		var url = basePath+"/"+prefix+".xlsx";
		if(isAll == null || isAll == undefined || isAll == true) {
			url +="?all=1";
		} 
		loadReportHead();
		var searchForm =  document.getElementById("searchForm");
		searchForm.setAttribute("action",url); 
		searchForm.submit();
	}catch(e){
	}
};

/**
 * 弹框关闭的回调函数，刷新表格
 * @param msg
 */
function closeAndRef(msg) {
	if(msg == null || msg == undefined) {
		msg="操作成功!";
	}
	parent.layer.close(dialog)
	alertOk(msg);
	gridObj.refreshPage();
}

/**
 * 生成表格按钮（权限控制）
 * @param btnId 按钮id
 * @param f 按钮执行函数
 * @param icon 按钮图标
 * @returns
 */
function getButton(btnId,f,icon){ 
	var str = "<a style='text-decoration:none' class='toolbar' onClick="+f+" href='javascript:;'  name='"+btnId+"' ><i class='Hui-iconfont'>"+icon+"</i></a>&nbsp;"
	if(typeof(isAdmin)=='undefined' || isAdmin==null || isAdmin=='' || isAdmin){
		return str;
	}
	if(typeof(btnIds)=='undefined' || btnIds==null || btnIds.length==0){
		return str;
	}
	return $.inArray(btnId,btnIds)>-1 ? str : "";
}

/**
 * checkbox选中时间
 * @param id
 * @param ischecked
 */
function checkboxEvent(id, ischecked) {};

function closeWin(msg) {
	if(msg == null || msg == undefined) {
		msg="操作成功!";
	}
	parent.layer.close(dialog)
	alertOk(msg);
}

