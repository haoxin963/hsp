<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<jsp:include page="/com/inc/incMeta.jsp">
			<jsp:param name="com" value="core"></jsp:param>
		</jsp:include>
    		<jsp:include page="/com/inc/incCssJs.jsp">
			<jsp:param name="com" value="core" />
		</jsp:include>

	</head>
	<body>
		<%@include file="/view/sys/monitor/nav.jsp" %>
		<div class="easyui-tabs" style="width: auto; height:auto">
			<div title="APPLICATION SCOPE" style="padding: 10px">
				<table  class="easyui-datagrid" style="width: auto; height: auto">
					<thead>
						<tr>
							<th data-options="field:'itemid'">
								ID
							</th>
							<th data-options="field:'sql'">
								KEY
							</th>
							<th data-options="field:'VALUE'">
								VALUE
							</th>
						</tr>
					</thead>
					<tbody>
						<%
							int i = 0;
							for (Enumeration<String> enumSessionId = application.getAttributeNames(); enumSessionId.hasMoreElements();) {
								i++;
								out.println("<tr><td>" + i + "</td><td>" + enumSessionId.nextElement() + "</td><td>略</td></tr>");
							}
						%>
					</tbody>
				</table>
			</div>
			<div title="SESSION SCOPE"  
				style="padding: 10px">
				<table class="easyui-datagrid" style="width: auto; height: auto">
					<thead>
						<tr>
							<th data-options="field:'itemid'">
								ID
							</th>
							<th data-options="field:'sql'">
								KEY
							</th>
							<th data-options="field:'VALUE'">
								VALUE
							</th>
						</tr>
					</thead>
					<tbody>
						<%
							Enumeration e = request.getSession().getAttributeNames();
							i = 0;
							while (e.hasMoreElements()) {
								i++;
								String sessionName = (String) e.nextElement();
								out.println("<tr><td>" + i + "</td><td>" + sessionName + "</td><td>略</td></tr>");
							}
						%>
					</tbody>
				</table>
			</div>
		</div>
		<script>
		function defaultHaveScroll(gridid){
		 var opts=$('#'+gridid).datagrid('options');
		    // alert(Ext.util.JSON.encode(opts.columns));
		    var text='{';
		    for(var i=0;i<opts.columns.length;i++){
		     var inner_len=opts.columns[i].length;
		     for(var j=0;j<inner_len;j++){
		      if((typeof opts.columns[i][j].field)=='undefined')break;
		       text+="'"+opts.columns[i][j].field+"':''";
		       if(j!=inner_len-1){
		        text+=",";
		       }
		     }
		    }
		    text+="}";
		    text=eval("("+text+")");
		    var data={"total":1,"rows":[text]};
		    $('#'+gridid).datagrid('loadData',data);
		   // $('#grid').datagrid('appendRow',text);
		   $("tr[datagrid-row-index='0']").css({"visibility":"hidden"});
		}
		defaultHaveScroll("dg1");
		</script>
	</body>
</html>
