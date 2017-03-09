<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>
<script type="text/javascript" src="${path}/com/ckeditor/ckeditor.js"></script>
<script type="text/javascript"> 
	CKEDITOR.replace( '${param.inputId}' , {${param.css}}); 
</script>