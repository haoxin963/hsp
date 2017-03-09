<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="net.hsp.service.util.*"%>
<%@ page import="net.hsp.entity.dform.*"%>
<%@ page import="net.sf.json.*"%>
<%@ page import="net.hsp.web.util.HttpSessionFactory;"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="f"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html> 
<jsp:include page="/com/easyui/inc.jsp">
<jsp:param name="com" value="core"></jsp:param>
</jsp:include>
<body>
<script>
$.extend($.fn.datagrid.methods, {
    editCell: function(jq,param){
        return jq.each(function(){
            var opts = $(this).datagrid('options');
            var fields = $(this).datagrid('getColumnFields',true).concat($(this).datagrid('getColumnFields'));
            for(var i=0; i<fields.length; i++){
                var col = $(this).datagrid('getColumnOption', fields[i]);
                col.editor1 = col.editor;
                if (fields[i] != param.field){
                    col.editor = null;
                }
            }
            $(this).datagrid('beginEdit', param.index);
            for(var i=0; i<fields.length; i++){
                var col = $(this).datagrid('getColumnOption', fields[i]);
                col.editor = col.editor1;
            }
        });
    }
});
var editIndex = undefined;
function endEditing(){
    if (editIndex == undefined){return true}
    if ($('#formTable').datagrid('validateRow', editIndex)){
   		
        $('#formTable').datagrid('endEdit', editIndex);
        editIndex = undefined;
        return true;
    } else {
        return false;
    }
}

function onClickCell(index, field){
    if (endEditing()){
       $('#formTable').datagrid('selectRow', index).datagrid('editCell', {index:index,field:field});
        editIndex = index;
    }
}
             
function onClickRow(index){
    if (editIndex != index){
        if (endEditing()){
            $('#formTable').datagrid('selectRow', index).datagrid('beginEdit', index);
            editIndex = index;
        } else {
            $('#formTable').datagrid('selectRow', editIndex);
        }
    }
}
function appendField(){
    if (endEditing()){
        $('#formTable').datagrid('appendRow',{
        	fieldType:'text',
        	display:'true',
        	gridField:'true'
        });
        editIndex = $('#formTable').datagrid('getRows').length-1;
        $('#formTable').datagrid('selectRow', editIndex)
                .datagrid('beginEdit', editIndex);
    }
}
function removeField(){
 	if(endEditing()){
        var selectedRow = $('#formTable').datagrid('getSelected');
        if(null != selectedRow){
	        var selectedIndex = $('#formTable').datagrid('getRowIndex',selectedRow);
	        $('#formTable').datagrid('deleteRow',selectedIndex);
        }else{
        	alert("请先选中一行");
        }
    }  
}
function accept(){
    if (endEditing()){
        $('#formTable').datagrid('acceptChanges');
    }
}
function reject(){
    $('#formTable').datagrid('rejectChanges');
    editIndex = undefined;
}
function getChanges(){
    var rows = $('#formTable').datagrid('getChanges');
    alert(rows.length+' rows are changed!');
}

var checkboxOptions = {
	type:'checkbox',options:{on:true,off:false}
}

var dataList = <%
FormEntity form = (FormEntity)request.getAttribute("command");
JsonConfig jsonConfig = new JsonConfig();
jsonConfig.registerDefaultValueProcessor(Integer.class,new DefaultNullValueProcessor());
out.println(JSONArray.fromObject(form.getFields(),jsonConfig).toString());
%>;
var fieldTypes = <%=FieldTypeUtil.fieldTypeJson%>;

var validators = [
	{'value':'integer','label':'integer'},
	{'value':'number','label':'number'},
	{'value':'eamil','label':'eamil'},
	{'value':'mobile','label':'mobile'},
	{'value':'zip','label':'zip'},
];

var dics =<%
String custId = HttpSessionFactory.getCustId(request);
out.println(DicUtil.dicTypeJson(custId));%>;

var forms =<%=FormCache.getJsonForms()%>;

var entitys =[];
$(function(){
	$('#formTable').datagrid({
		data:dataList,
		singleSelect:true,
		//onClickRow:onClickRow,
		onClickCell:onClickCell,
		toolbar:'#formToolBar',
		columns:[[
			{field:'fieldId',checkbox:true},
        	//{field:'propertyName',title:'属性名',width:80,editor:{type:'text',options:{required:true}}},
        	{field:'propertyName',title:'属性名',width:80,hidden:true},
        	{field:'label',title:'控件label',width:65,
        		editor:{
        			type:'text',
        			options:{
        				required:true
        			}
        		}
        	},
        	{field:'fieldType',title:'控件',width:70,
        		formatter:function(value,row){
        			$.each(fieldTypes,function(i,fieldType){
        				if(fieldType.typeName==value){
        					value = fieldType.typeLabel;
        					return;
        				}
        			});
        			return value;
                },
        		editor:{
        			type:'combobox',
        			options:{
	                    valueField:'typeName',
	                    textField:'typeLabel',
	                    data:fieldTypes,
	                    required:true
                    }
        		}
        	},
        	{field:'maxlength',title:'最大长度',width:55,editor:'numberbox'},
        	{field:'validators',title:'校验规则',width:60,
        		editor:{
        			type:'combobox',
        			options:{
	                    valueField:'value',
	                    textField:'label',
	                    data:validators
                    }
        		}
        	},
        	{field:'refDic',title:'引用字典',width:100,
        		editor:{
        			type:'combobox',
        			options:{
	                    valueField:'typeName',
	                    textField:'typeLabel',
	                    data:dics
                    }
        		}
        	},
        	{field:'required',title:'必填',width:40,editor:checkboxOptions},
        	{field:'display',title:'参与提交',width:55,editor:checkboxOptions},
        	{field:'filterField',title:'参与搜索',width:55,editor:checkboxOptions},
        	{field:'gridField',title:'参与列表',width:55,editor:checkboxOptions},
        	{field:'refFormId',title:'引用表单',width:70,
        		editor:{
        			type:'combobox',
        			options:{
	                    valueField:'formId',
	                    textField:'formName',
	                    data:forms
                    }
        		}
        	},
        	{field:'defaultValue',title:'默认值',width:50,editor:'text'},
        	{field:'data',title:'可选值',width:90,editor:'text'},
        	{field:'autocomplete',title:'自动联想',width:60,editor:'text'},
        	{field:'checkExists',title:'检查重复',width:55,editor:checkboxOptions},
        	{field:'labelProperty',title:'引用显示',width:55,editor:checkboxOptions}
        ]] 
	});
});
</script>
	<f:form id="formForm"
		method="POST">
	<input type="hidden" name="formId" value="${command.formId}"/>	
	<div id="formToolBar" style="padding:5px;height:auto">
		表单名称: <input type="text" name="formName" value="${command.formName}" class="easyui-text" style="width:100px"/>
		<a href="#" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="appendField();">添加属性</a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="removeField();">删除</a>
    </div>
	<table id="formTable" width="100%" height="600px">
	</table>
	</f:form>
</body>
</html>