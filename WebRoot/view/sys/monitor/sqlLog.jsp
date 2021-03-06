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
                case "SQL日志下载":
                    {
                        $("#A").attr("src", "${path}/sys/monitor/log/sqlLogList.jspx");
                        break;
                    }
                case "info SQL(最近500条)":
                    {
                        //$("#B").attr("src", "${path}/sys/monitor/log/sqlInfo.jspx");
                        $("#B").attr("src", "${path}/sys/monitor/log/sql/infoLogList.do");
                        break;
                    }
                case "warn SQL(最近500条)":
                    {
                        //$("#C").attr("src", "${path}/sys/monitor/log/sqlWarn.jspx");
                        $("#C").attr("src", "${path}/sys/monitor/log/sql/warnLogList.do");
                        break;
                    }
            }
        }
 
        $(window).resize(function () {
            resizeTabs();
        });
        function resizeTabs() {
            $('#tt').tabs('resize');
        }
    </script>
  
 
    <div class="bodyDiv">
	    <div id="tt" class="pagetabs">
	        <div title="SQL日志下载" style="overflow: auto; padding: 0px;">
	            <iframe id="A" src="" frameborder="0" style="width: 100%; height: 95%"></iframe>
	        </div>
	        <div title="info SQL(最近500条)" style="overflow: auto; padding: 0px;"> 
	            <iframe id="B" frameborder="0" src="" style="width: 100%; height: 95%;"></iframe>
	        </div>
	        <div title="warn SQL(最近500条)" style="overflow: auto; padding: 0px;">
	            <iframe id="C" src="" frameborder="0" style="width: 100%; height: 95%"></iframe>
	        </div>
	    </div>
    </div>
 
	</body>
</html>
