<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
ID: ${entryId}
<br/>
<c:forEach var="action" items="${actions}">
<a href="${path}/sys/workflow/doAction.do?entryId=${entryId}&actionId=${action.id}">${action.name}</a>
</c:forEach>