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
	<body >  
		<%@include file="/view/sys/monitor/nav.jsp" %>
		<script type="text/javascript">
        $(function () {
            //设置自适应高度
           // $('#tt').height(window.document.documentElement.scrollHeight-40);
            $('#tt').tabs({
                onSelect: function (title) {
                    loadHtmlFile(title);
                }
            });
        });
 
        function loadHtmlFile(title) {
            switch (title) {
                case "webBean":
                    {
                        $("#A").attr("src", "${path}/sys/monitor/bean/webBean.jspx");
                        break;
                    }
                case "serviceBean":
                    {
                        $("#B").attr("src", "${path}/sys/monitor/bean/serviceBean.jspx");
                        break;
                    } 
            }
        }
 
       
    </script>
  
  
    <div id="tt" class="pagetabs" style="overflow: hidden;">
        <div title="webBean" style="overflow: hidden; padding: 0px;">
            <iframe id="A" src="" frameborder="0" style="width: 100%; height: 90%"></iframe>
        </div>
        <div title="serviceBean" style="overflow: hidden; padding: 0px;"> 
            <iframe id="B" frameborder="0" src="" style="width: 100%; height: 90%;"></iframe>
        </div> 
    </div> 
 
	</body>
</html>
