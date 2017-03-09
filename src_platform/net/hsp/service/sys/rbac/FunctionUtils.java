package net.hsp.service.sys.rbac;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.hsp.entity.sys.rbac.Function;

public class FunctionUtils
{
	private static final String ROOT_ID = "0001";
	/** 是否是根功能节点 */
	private static boolean isRoot(FuncProxy pro)
	{
		if(ROOT_ID.equals(pro.getId())) { return true; }
		if( "0".equals(pro.getParentId()) || null==pro.getParentId() ) { return true; }
		return false;
	}
	
	/** 把从数据库查询到的列数据 序列化 成扁平的数据树 */
	public static List<Map<String, Object>> serialTree(List<Function> funcList)
	{
		List<FuncProxy> proxies = new ArrayList<FuncProxy>();
		boolean hasRoot = false;
		for(Iterator<Function> iter = funcList.iterator(); iter.hasNext();)
		{
			Function func = iter.next();
			FuncProxy pro = new FuncProxy(func);
			proxies.add(pro);
			if(isRoot(pro)) { hasRoot = true; }
		}
		
		FuncProxy root = null;
		if(!hasRoot)
		{
			Function rf = new Function();
			rf.setFunctionId(ROOT_ID);
			rf.setParentId("0");
			rf.setFunctionName("根功能");
			root = new FuncProxy(rf);
			proxies.add(root);
		}
		for(Iterator<FuncProxy> iter = proxies.iterator(); iter.hasNext();)
		{
			FuncProxy pro = iter.next();
			findParent(pro, proxies);
			//System.out.println("id=" + pro.getId() +  " ParentId="+pro.getParentId() + " fname=" + pro.func.getFunctionName());
			if( isRoot(pro) )
			{
				root = pro;
			}
		}
		
		if(null != root) { return flatten(root); }
		return null;
	}
	
	private static void findParent( FuncProxy orphan, List<FuncProxy> proxies )
	{
		for(Iterator<FuncProxy> iter = proxies.iterator(); iter.hasNext();)
		{
			FuncProxy pro = iter.next();
			if(orphan.equals(pro)) 
			{ 
				continue; 
			}
			if(orphan.getParentId().equals(pro.getId()))
			{
				pro.children.add(orphan);
				//break;如果一个孩子只能有一个归属parent则使用break，否则继续孤儿认亲
			}
		}
	}
	
	private static class FuncProxy
	{
		public Function func;
		
		public List<FuncProxy> children = new ArrayList<FuncProxy>();
		
		public FuncProxy(Function func)
		{
			this.func = func;
		}
		
		public String getId()
		{
			return func.getFunctionId();
		}
		
		public String getParentId()
		{
			return func.getParentId();
		}
		
		
	}
	
	/** 扁平化 */
	public static List<Map<String, Object>> flatten(FuncProxy proxy)
	{
		List<Map<String, Object>> result = new ArrayList<Map<String,Object>>();
		for(Iterator<FuncProxy> iter = proxy.children.iterator(); iter.hasNext();)
		{
			FuncProxy pro = iter.next();
			result.add( toMap(pro) );
		}
		return result;
	}
	
	public static Map<String, Object> toMap(FuncProxy proxy)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("funcId", proxy.func.getFunctionId());
		map.put("funcName", proxy.func.getFunctionName());
		map.put("parentId", proxy.func.getParentId());
		String linkaddr = proxy.func.getLinkAddress();
		if (linkaddr == null || "null".equals(linkaddr) || "".equals(linkaddr)) 
		{
			linkaddr = "";
		}
		else
		{
			//兼容旧的数据，地址不是以/开头的
			if(!linkaddr.startsWith("/") && !linkaddr.startsWith("http"))
			{
				linkaddr = "/"+linkaddr;
			}
		}
		map.put("linkAddr", linkaddr);
		map.put("innerUrl", proxy.func.getInnerUrl());
		map.put("buttonId", proxy.func.getButtonId());
		map.put("sortNo", proxy.func.getSortNo());
		String picaddr = proxy.func.getPictureAddr();
		if (picaddr == null || "null".equals(picaddr)) 
		{
			picaddr = "";
		}
		map.put("picAddr", picaddr);
		map.put("tag", proxy.func.getTag());
		map.put("children", flatten(proxy));
		return map;
	}
						
	
	
	
	
	
	
}
