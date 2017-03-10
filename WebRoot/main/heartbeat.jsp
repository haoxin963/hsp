<%@ page language="java" pageEncoding="UTF-8" 	trimDirectiveWhitespaces="true"%>
<%@page import="net.hsp.service.sys.msg.MessageService"%>
<%@page import="net.hsp.web.util.SpringCtx"%>
<%@page import="net.hsp.common.constants.PlatFormConstant"%>
<%@page import="net.hsp.common.CacheUtil"%>
<%
	String msg = "0";
	int size = 1;
	Object userId = session.getAttribute(PlatFormConstant.CURRENT_USERID);
	Object custId = session.getAttribute(PlatFormConstant.CURRENT_SYSINSTANCE);
//	try{ 
//		if(userId!=null){
//			String name = "session1800_sync_online";
//			CacheUtil cache = CacheUtil.getInstance();
//			if( cache!=null && cache.getCache(name).get(userId.toString())==null){ 
//				cache.add(name,custId+"_"+userId.toString(),session.getAttribute(PlatFormConstant.CURRENT_USERNAME).toString());
//			}
//			size = cache.queryKeyCount(name, custId+"_");
			//size = cache.getCache(name).getKeys().size();
//		}
//	}catch(Exception e){
		
//	}
	try{ 
		if(userId!=null){
			MessageService service = (MessageService) SpringCtx.getBean("msgServiceImpl");
			msg=service.unread(session.getAttribute(PlatFormConstant.CURRENT_USERID).toString()).toString();
		}
	}catch(Exception e){
		e.printStackTrace();
	}
	out.print("{\"online\":\""+size+"\",\"msg\":\""+msg+"\"}");
%>