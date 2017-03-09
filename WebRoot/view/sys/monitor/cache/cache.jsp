<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="utf-8"%>
<%@ page import="net.sf.ehcache.*"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.List"%>
<%@page import="net.hsp.common.CacheUtil"%>
<%
	CacheUtil obj = CacheUtil.getInstance();
	String[] names = obj.getCacheNames();
	if (names == null || names.length == 0) {
		return;
	}
	String n = request.getParameter("n");
	if (n == null) {
		n = names[0];
	}
	net.sf.ehcache.Cache cache = obj.getCache(n);
	DateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<jsp:include page="/com/inc/incMeta.jsp">
			<jsp:param name="com" value="core"></jsp:param>
		</jsp:include>

		<jsp:include page="/com/easyui/inc.jsp">
			<jsp:param name="com" value="core"></jsp:param>
		</jsp:include>
		<script>
			function go(name){
				window.location="?n="+name;
			}
		</script>
	</head>

	<body class="bodyLayout">
		<div class="toolbarDiv">
			缓存名称${separator}
			<select onchange="go(this.value)">
				<%
					for (int i = 0; i < names.length; i++) {
						if (n.equals(names[i])) {
							out.println("<option value='" + names[i] + "' selected >" + names[i] + "</option>");
						} else {
							out.println("<option value='" + names[i] + "' >" + names[i] + "</option>");
						}
					}
				%>
			</select>
			&nbsp;
			共<%=names.length %>个缓存集
		</div>
		<table class="easyui-datagrid" style="width: auto; border: false, height :         auto">
			<thead>

				<tr>
					<th data-options="field:'a',width:110">
						区域
					</th>
					<th data-options="field:'b',width:220">
						键
					</th>
					<th data-options="field:'d',width:130">
						创建时间
					</th>
					<th data-options="field:'e',width:130">
						最后访问时间
					</th>
					<th data-options="field:'f',width:130">
						过期时间
					</th>
					<th data-options="field:'g',width:80">
						命中次数
					</th>
					<th data-options="field:'h',width:80">
						存活时间(秒)
					</th>
					<th data-options="field:'i',width:80">
						空闲时间(秒)
					</th>
				</tr>
			</thead>
			<%
				List elements = cache.getKeys();
				List keys = cache.getKeys();
				for (Object key : elements) {
					if (!keys.isEmpty()) {
						Element ele = cache.get(key);
			%>
			<tr bgcolor='#ffffff'>
				<td><%=cache.getName()%></td>
				<td><%=key%></td>
				<td><%=sf.format(ele.getCreationTime())%></td>
				<td><%=sf.format(ele.getLastAccessTime())%></td>
				<td><%=sf.format(ele.getExpirationTime())%></td>
				<td><%=ele.getHitCount()%></td>
				<td><%=ele.getTimeToLive()%></td>
				<td><%=ele.getTimeToIdle()%></td>
			</tr>
			<%
				}
				}
			%>
		</table>
		<br>
		<h3>
			缓存总统计
		</h3>
		<%
			Statistics stat = cache.getStatistics();
		%>
		<table class="easyui-datagrid" style="width: auto; border: false, height :         auto">
			<thead>

				<tr>
					<th data-options="field:'a',width:80">
						总数量
					</th>
					<th>
						内存数量
					</th>
					<th>
						磁盘数量
					</th>
					<th>
						命中次数
					</th>
					<th>
						内存命中
					</th>
					<th>
						磁盘命中
					</th>
				</tr>
			</thead>
			<tr>
				<td><%=cache.getSize()%></td>
				<td><%=cache.getMemoryStoreSize()%></td>
				<td><%=cache.getDiskStoreSize()%></td>
				<td><%=stat.getCacheHits()%></td>
				<td><%=stat.getInMemoryHits()%></td>
				<td><%=stat.getOnDiskHits()%></td>
			</tr>
		</table>
	</body>
<Html>