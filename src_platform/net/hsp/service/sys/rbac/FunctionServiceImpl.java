package net.hsp.service.sys.rbac;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import net.hsp.common.MethodLogUtil;
import net.hsp.common.ServiceLogUtil;
import net.hsp.common.SystemContext;
import net.hsp.entity.sys.rbac.Function;
import net.hsp.entity.sys.rbac.Rolefunction;
import net.hsp.service.BaseServiceImpl;
import net.hsp.service.util.SQLBuild;
import net.hsp.web.util.PageInfo;
import net.hsp.web.util.SpringCtx;
import net.sf.json.JSONArray;

import org.apache.commons.lang.StringUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 * 权限业务类
 * 
 * @author lk0508
 */
@Service
@ServiceLogUtil(name = "权限业务类")
@Lazy(true)
public class FunctionServiceImpl extends BaseServiceImpl implements FunctionService {

	@MethodLogUtil(type = "", value = "多条件查询权限")
	public Map query(Map param, PageInfo p) {
		StringBuilder sb = new StringBuilder("select functionId,buttonId,functionName,child,flag,linkAddress,parent_id,pictureAddr,sortNo,tag,type,innerUrl from pubmodule_function_tbl where 1=1");
		Map sqlBuild = SQLBuild.buildEquals(param);
		sb.append(sqlBuild.get("sql") == null ? "" : sqlBuild.get("sql"));
		sb.append(" order by parent_id,sortNo");
		if (p == null) {
			Map<String, Object> map = new HashMap<String, Object>();
			List<Map<String, Object>> list = null;
			Object args = sqlBuild.get("args");
			if (args != null) {
				list = this.find(sb.toString(), (Object[]) args);
			} else {
				list = this.getDAO().queryForList(sb.toString());
			}
			map.put("rows", list);
			map.put("total", list.size());
			return map;
		} else {
			return this.findPageInfo(sb.toString(), (Object[]) sqlBuild.get("args"), p);
		}
	}

	@Override
	public List<Map<String, Object>> buildTree(Map map, PageInfo p) {
		
		String sql = "select * from pubmodule_function_tbl order by parent_id,sortNo ";
		return this.find(sql, new Object[] {});
	}

	@Override
	public Function addFunction(Function function) {
		
		List<Map<String, Object>> funcList = getAllChildFunctionById(function.getParentId() + "");
		int sortNo = funcList.size() > 0 ? (funcList.size() + 1) : 1;
		UUID uuid = UUID.randomUUID();
		String funcId = uuid.toString();
		funcId = funcId.replace("-", "").toLowerCase();
		function.setFunctionId(funcId);
		function.setSortNo(sortNo);		
		function.setFlag("0"); 
		this.save(function);
		if (funcList.size() == 0) {
			updateHasChild("1", function.getParentId());
		}
		return function;
	}

	private void updateHasChild(String hasChild, String id) {
		String sql = "update pubmodule_function_tbl set child = '" + hasChild + "' where functionId = '" + id + "'";
		this.getDAO().execute(sql);
	}

	@Override
	public int delFunctionById(String id) {
		
		String sql = "delete from pubmodule_function_tbl where functionId = '" + id + "'";
		this.getDAO().execute(sql);
		return 1;
	}

	@Override
	public Function getFunctionById(String id) {
		
		Function function = new Function();
		function.setFunctionId(id);
		Function retFunction = (Function) this.findById(function);
		return retFunction;
	}

	@Override
	public List<Function> getFunctionList() {
		
		// 根据层级排序 数据可用于前端树节点的展示
		String sql = "select * from pubmodule_function_tbl order by sortNo asc ";
		List<Function> functionLsit = (List<Function>) this.find(sql, new Object[] {}, Function.class);
		return functionLsit;
	}

	@Override
	public int modifyFunction(Function function) {
		
		return this.update(function);
	}

	@Override
	public List<Function> getFuncListByRoleId(String rid) {
		String sql = "select f.functionId,f.functionName,f.parent_id,f.lev from pubmodule_function_tbl f where exists (select rf.id from pubmodule_rolefunction_tbl rf where rf.functionId=f.functionId and rf.role_id=?)";
		List<Function> funcLsit = (List<Function>) this.find(sql, new Object[] { rid }, Function.class);
		return funcLsit;
	}

	@Override
	public int grantFuncs2Role(String roleid, String[] fids) {
		
		// 清空当前用户所有的角色授权
		String sql1 = "delete from pubmodule_rolefunction_tbl where role_id = " + roleid;
		this.getDAO().execute(sql1);
		// 新增用户的角色授权
		for (String fid : fids) {
			Rolefunction rf = new Rolefunction();
			rf.setRoleId(Long.parseLong(roleid));
			rf.setFunctionId(fid);
			rf.setType("1");
			save(rf);
		}
		return 1;
	}
	
	
	@Override
	public List<Function> listMenuDataByUserId(String userid,boolean isAdmin,boolean genBtn){
		List<Function> funcList = new ArrayList<Function>();
		String filterBtn = "";
		if(!genBtn){
			filterBtn =" and f.tag='f' ";
		}
		if (isAdmin) {
			String sql2 = "select f.functionId,f.functionName,f.parent_id,f.buttonId,f.child,f.flag,f.linkAddress,f.pictureAddr,f.sortNo,f.tag,f.innerUrl from pubmodule_function_tbl f where  f.type='1' "+filterBtn+" order by f.sortNo asc";
			funcList = (List<Function>) this.find(sql2, new Object[] {}, Function.class);
		} else {
			// 根据用户ID 查询相应角色关联的权限信息
			StringBuffer sb = new StringBuffer();
			sb.append("  select f.functionId,f.functionName,f.parent_id,f.buttonId,f.child,f.flag,f.linkAddress,f.pictureAddr,f.sortNo,f.tag,f.innerUrl from pubmodule_function_tbl f where   f.type='1' "+filterBtn+" and exists ( ");
			sb.append("     select rf.functionId from pubmodule_rolefunction_tbl rf where f.functionId = rf.functionId and  exists ( ");
			sb.append("        select ur.role_Id from pubmodule_userrole_tbl ur where rf .role_Id = ur.role_Id and ur.user_id = ? ");
			sb.append("    )  ");
			sb.append(" ) order by f.sortNo asc");
			funcList = (List<Function>) this.find(sb.toString(), new Object[] { userid }, Function.class);
		}
		return funcList;
	}
	

	@Override
	@Cacheable(value = "service3600")
	public List<Map<String, Object>> getMenuDataByUserId(String userid,boolean isAdmin,boolean genBtn) { 
		List<Function>funcList = listMenuDataByUserId(userid,isAdmin,genBtn); 
		List<Map<String, Object>> srst = FunctionUtils.serialTree(funcList);
		return srst;
	}

	
	private void fillMapList(List<Function> funclist, List<Map<String, Object>> mapList) {
		for (int i = 0; i < mapList.size(); i++) {
			Map<String, Object> funcMap = mapList.get(i);
			String funcId = funcMap.get("funcId").toString();
			for (int j = 0; j < funclist.size(); j++) {
				Function function = funclist.get(j);
				if (funcId.equals(function.getParentId())) {
					List<Map<String, Object>> listMap = (List<Map<String, Object>>) funcMap.get("children");
					
//					for(int k=0;k<listMap.size();k++){
//						Map<String,Object> mptemp = listMap.get(i);
//						if(function.getFunctionId().equals(mptemp.get("funcId").toString())){
//							break;
//						}						
//					}
					
					Map<String, Object> mp = new HashMap<String, Object>();
					mp.put("funcId", function.getFunctionId());
					mp.put("funcName", function.getFunctionName());
					mp.put("parentId", function.getParentId());					
					String linkaddr = function.getLinkAddress();
					if (linkaddr == null || "null".equals(linkaddr)) {
						linkaddr = "";
					}else{
						//兼容旧的数据，地址不是以/开头的
						if(!linkaddr.startsWith("/")){
							linkaddr = "/"+linkaddr;
						}
					}
					mp.put("linkAddr", linkaddr);
					mp.put("children", new ArrayList<Map<String, Object>>());
					mp.put("buttonId", function.getButtonId());
					mp.put("sortNo", function.getSortNo());
					String picaddr = function.getPictureAddr();
					if (picaddr == null || "null".equals(picaddr)) {
						picaddr = "";
					}
					mp.put("picAddr", picaddr);
					mp.put("tag", function.getTag());
					listMap.add(mp);
					
					if ("1".equals(function.getChild())) {
						this.fillMapList(funclist, listMap);
					}
					
				}
			}
		}
	}
	
	private String genPath(String path,String basePath){
		if (StringUtils.isBlank(path)) {
			return "javascript:void(0)";
		}else{
			if (path.startsWith("http")) {
				return path;
			}else{
				return basePath + path;
			}
		}
	}

	@Override
	public String generateHtmlByMenuData(List<Map<String, Object>> data, String basePath) {
		if(data==null){
			return null;
		}
		String url = "javascript:void(0)";		
		StringBuffer html = new StringBuffer();
		html.append("<ul class=\"sdmenu\" id=\"my_menu\">");
		// 第一层级
		for (int i = 0; i < data.size(); i++) {
			Map<String, Object> map1 = data.get(i);
			List<Map<String, Object>> children1 = (List<Map<String, Object>>) map1.get("children"); 	
			url = genPath(map1.get("linkAddr")+"",basePath);
			if (children1.size() > 0) {
				html.append("<li>");
				html.append("	<a onfocus=\"this.blur()\" href=\"#\" class=\"span\">");

				if (!"".equals(map1.get("picAddr"))) {
					html.append("   <img src=\"" + basePath + "/" + map1.get("picAddr") + "\" onerror=\"this.onerror=null;this.src='" + basePath + "/main/images/1-2-4.gif'\" /> ");
				} else {
					html.append("   <img src=\"" + basePath + "/main/images/1-2-4.gif\" /> ");
				}
				html.append("	" + map1.get("funcName") + "</a>");
				// 第二个层级
				html.append("	<ul style=\"display: none;\">");
				for (int j = 0; j < children1.size(); j++) {
					Map<String, Object> map2 = children1.get(j);
					List<Map<String, Object>> children2 = (List<Map<String, Object>>) map2.get("children");			 	
					url = genPath(map2.get("linkAddr")+"",basePath);
					if (children2.size() > 0) {
						html.append("<li>");
						html.append("	<a ");
						html.append("		target=\"rightFrame\" href=\"javascript:void(0);\" ><span>" + map2.get("funcName") + "</span>");
						html.append("	</a>");

						// 第三个层级
						html.append("	<ul style=\"display: none;\">");
						for (int k = 0; k < children2.size(); k++) {
							Map<String, Object> map3 = children2.get(k);
							List<Map<String, Object>> children3 = (List<Map<String, Object>>) map3.get("children");
							url = genPath(map3.get("linkAddr")+"",basePath);
							if (children3.size() > 0) {
								html.append("<li>");
								html.append("	<a");
								html.append("		target=\"rightFrame\" href=\"javascript:void(0);\" ><span>" + map3.get("funcName") + "</span>");
								html.append("	</a>"); 
								// 第三个层级
								html.append("	<ul style=\"display: none;\">");
								for (int m = 0; m < children3.size(); m++) {
									Map<String, Object> map4 = children3.get(m);
									// List<Map<String,Object>> children4 =
									// (List<Map<String,Object>>)map4.get("children");									 
									url = genPath(map4.get("linkAddr")+"",basePath);
									html.append("<li>");
									html.append("	<a onclick=\"ckId('" + map4.get("funcId") + "');\" onfocus=\"this.blur();\"");
									html.append("		target=\"rightFrame\" href=\"" + url + "\"><span>" + map4.get("funcName") + "</span>");
									html.append("	</a>");
									html.append("	<ul style=\"display: none;\"></ul>");
									html.append("	</li>");
								}
								html.append("	</ul>");
							} else {
								html.append("<li>");
								html.append("	<a onclick=\"ckId('" + map3.get("funcId") + "');\" onfocus=\"this.blur();\"");
								html.append("		target=\"rightFrame\" href=\"" + url + "\"><span>" + map3.get("funcName") + "</span>");
								html.append("	</a>");
								html.append("	<ul style=\"display: none;\"></ul>");
							}
							html.append("	</li>");
						}
						html.append("	</ul>");
					} else {
						html.append("<li>");
						html.append("	<a onclick=\"ckId('" + map2.get("funcId") + "');\" onfocus=\"this.blur();\"");
						html.append("		target=\"rightFrame\" href=\"" + url + "\"><span>" + map2.get("funcName") + "</span>");
						html.append("	</a>");
						html.append("	<ul style=\"display: none;\"></ul>");
					}
					html.append("	</li>");
				}
				html.append("	</ul>");
			} else {
				html.append("<li>");
				html.append("	<a target=\"rightFrame\" onfocus=\"this.blur()\" href=\"" + url + "\" class=\"span\">");
				if (!"".equals(map1.get("picAddr"))) {
					html.append("   <img src=\"" + basePath + "/" + map1.get("picAddr") + "\" onerror=\"this.onerror=null;this.src='" + basePath + "/main/images/1-2-4.gif'\" /> ");
				} else {
					html.append("   <img src=\"" + basePath + "/main/images/1-2-4.gif\" /> ");
				}
				html.append("	" + map1.get("funcName") + "</a>");
				html.append("	<ul style=\"display: none;\"></ul>");
			}
			html.append("</li>");
		}
		html.append("</ul>");
		return html.toString();
	}

	@Override
	public int generateMenuHtmlFile(String userId, String custId, String basePath) {
		
		List<Map<String, Object>> mapList = getMenuDataByUserId(userId,userId.equals("1"),false);
		String htmlStr = generateHtmlByMenuData(mapList, basePath);
		String diskPath = SystemContext.getProperty("webApp.root");
		String filePath = diskPath + "page/" + custId + "/menu/" + userId + ".html";
		BufferedWriter bufferedWriter = null;
		try {
			File mfile = new File(filePath);
			if (!mfile.getParentFile().exists()) {
				mfile.getParentFile().mkdirs();
			}
			FileOutputStream fileOutputStream = new FileOutputStream(mfile);
			bufferedWriter = new BufferedWriter(new OutputStreamWriter(fileOutputStream));
			bufferedWriter.write(htmlStr);
		} catch (Exception e) { 
			e.printStackTrace();
			return 0;
		} finally {
			try {
				bufferedWriter.close();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		return 1;
	}

	@Override
	public boolean checkUserMenuFileExist(String userId, String custId) {
		
		String diskPath = SystemContext.getProperty("webApp.root");
		String filePath = diskPath + "/page/" + custId + "/menu/" + userId + ".html";
		File menu = new File(filePath);
		return menu.exists();
	}

	@Override
	public void clearAllMenuFile(String custId) {
		
		String diskPath = SystemContext.getProperty("webApp.root");
		String filePath = diskPath + "/page/" + custId + "/menu/";
		File menuFolder = new File(filePath);
		File[] files = menuFolder.listFiles();
		for (File file : files) {
			String fileName = file.getName();
			if (fileName.endsWith(".html")) {
				file.delete();
			}
		}
	}

	@Override
	public void clearUserMenuFile(String custId, String userId) {
		String diskPath = SystemContext.getProperty("webApp.root");
		String filePath = diskPath + "/page/" + custId + "/menu/" + userId + ".html";
		String filePrefix = "menu_json_";
		String filePathJson = diskPath + "page/" + custId + "/menu/" + filePrefix + userId + ".html";
		File file = new File(filePath);
		if (file.exists()) {
			file.delete();
		}
		File fileJson = new File(filePathJson);
		if (fileJson.exists()) {
			fileJson.delete();
		}
		
	}

	@Override
	public List<Map<String, Object>> getAllChildFunctionById(String id) {
		
		String sql = "select f.* from pubmodule_function_tbl f where f.parent_id = ? ";
		List<Map<String, Object>> funcList = this.find(sql, new Object[] { id });
		// List<Function> funcList =
		// (List<Function>)this.baseDAO.queryForList(sql,Function.class, new
		// Object[]{id} );
		return funcList;
	}

	@Override
	public int changeFunctionLevel(String id, String type, String parentId, String sortNo, String[] sortList) {
		
		StringBuffer sb = new StringBuffer();
		if ("1".equals(type)) {
			// 更新节点层级
			sb.append("update pubmodule_function_tbl set parent_id = '" + parentId + "'");
			if (!"".equals(sortNo)) {
				sb.append(",sortNo = " + sortNo);
			}
			sb.append(" where functionId = '" + id + "'");
			this.getDAO().execute(sb.toString());
			sb.delete(0, sb.length());
			// 更新原来父节点及兄弟节点的结构
		}
		// 调整关联部门顺序
		for (int i = 0; i < sortList.length; i++) {
			if (sortList[i].indexOf(",") < 0) {
				continue;
			}
			String[] sortArr = sortList[i].split(",");
			String sortid = sortArr[0];
			String sortno = sortArr[1];
			sb.append("update pubmodule_function_tbl set sortNo = " + sortno + " where functionId = '" + sortid + "'");
			this.getDAO().execute(sb.toString());
			sb.delete(0, sb.length());
		}
		return 1;
	}

	@Override
	public List<Map<String, Object>> getButton(boolean isAdmin, String userId, String parentUrl) {
		StringBuffer sb = new StringBuffer();
		List<Map<String, Object>> list = null;
		if (isAdmin) {
			sb.append("select buttonId as id,pictureAddr as icon from pubmodule_function_tbl  where parent_id in(select functionid from pubmodule_function_tbl where linkAddress = ?)");
			list = this.find(sb.toString(), new Object[] { parentUrl });
		} else {
			sb.append(" select  buttonId as id,pictureAddr as icon from pubmodule_function_tbl f where  ");
			sb.append(" exists ( ");
			sb.append(" 	select rf.functionId from pubmodule_rolefunction_tbl rf where exists ( ");
			sb.append(" 	select ur.role_id from pubmodule_userrole_tbl ur where ur.user_id=? and rf.role_id= ur.role_id  ");
			sb.append(" )  ");
			sb.append(" and f.functionId = rf.functionId ) ");
			sb.append(" and exists (select f1.* from pubmodule_function_tbl f1 where f1.linkAddress = ? and f1.functionId = f.parent_id ) ");
			list = this.find(sb.toString(), new Object[] { userId, parentUrl });
		}
		return list;
	}

	@Override
	public List<Map<String, Object>> getAllChildrenMenuByParentId(String parentId) {
		
		
		
		return null;
	}

	
	@Override
	public List<Map<String, Object>> getFirstLevelMenu(String userId,boolean isAdmin) { 
		String sql = null;
		List<Map<String,Object>> list;
		if(isAdmin){
			sql = "select f.* from pubmodule_function_tbl f where f.parent_id = '0001' and type = 1  order by sortNo asc";
			list = find(sql, new Object[]{});
		}else{
			sql = "select f.* from pubmodule_function_tbl f where f.parent_id = '0001' and type = 1  ";
			sql+="and exists(select rf.functionId from pubmodule_rolefunction_tbl rf where f.functionId = rf.functionId and  exists (";
			sql+="select ur.role_Id from pubmodule_userrole_tbl ur where rf .role_Id = ur.role_Id and ur.user_id = ?)) order by sortNo asc";
			list = find(sql, new Object[]{userId});
		}
		return list;
	} 

	
	@Override
	public int checkFuncHasChild(String[] ids) {
		
		StringBuffer sb = new StringBuffer();
		for(int i=0;i<ids.length;i++){
			if(i==ids.length-1){
				sb.append("'"+ids[i]+"'");
			}else{
				sb.append("'"+ids[i]+"',");
			}
		}
		String sql = "select f.* from pubmodule_function_tbl f where f.parent_id in ("+sb.toString()+") ";
		List list = find(sql, new Object[]{});
		if(list.size()>0){
			return 1;
		}else{
			return 0;
		}
	}

	

	@Override
	public boolean checkUserMenuJsonFileExist(String userId, String custId) {
		
		String diskPath = SystemContext.getProperty("webApp.root");
		String filePrefix = "menu_json_";
		String filePath = diskPath + "page/" + custId + "/menu/" + filePrefix + userId + ".html";
		File menu = new File(filePath);
		return menu.exists();
	}

	@Override
	public int generateMenuJsonFile(String userId, String custId, String basePath) { 
		List<Map<String,Object>>  firstLevelMenu = getFirstLevelMenu(userId,userId.equals("1"));		
		List<Map<String, Object>> allMenuList = getMenuDataByUserId(userId,userId.equals("1"),false);		
		StringBuffer htmlStr = new StringBuffer();
		JSONArray jsonArr1 = JSONArray.fromObject(firstLevelMenu);
		JSONArray jsonArr2 = JSONArray.fromObject(allMenuList);
		htmlStr.append("<script type='text/javascript'>");
		htmlStr.append("var topLevelMenuJsonObj =  ");
		htmlStr.append(jsonArr1.toString());
		htmlStr.append(";");
		htmlStr.append("var allMenuJsonObj =  ");
		htmlStr.append(jsonArr2.toString());
		htmlStr.append(";");
		htmlStr.append("</script>");
		String diskPath = SystemContext.getProperty("webApp.root");
		String filePrefix = "menu_json_";
		String filePath = diskPath + "page/" + custId + "/menu/" + filePrefix + userId + ".html";
		BufferedWriter bufferedWriter = null;
		try {
			File mfile = new File(filePath);
			if (!mfile.getParentFile().exists()) {
				mfile.getParentFile().mkdirs();
			}
			FileOutputStream fileOutputStream = new FileOutputStream(mfile);
			bufferedWriter = new BufferedWriter(new OutputStreamWriter(fileOutputStream));
			bufferedWriter.write(htmlStr.toString());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return 0;
		} finally {
			try {
				bufferedWriter.close();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		return 1;
	}

	@Override
	public Map<String,  Integer> getPageButton() {
		Map<String,Integer> btns = new HashMap<String,  Integer>();
		String sql = "select functionid,linkaddress,parent_id as pid,tag from pubmodule_function_tbl where  functionid in(select parent_id from pubmodule_function_tbl where tag = 'b' and buttonId <> '') union select functionid,linkaddress,parent_id as pid,tag from pubmodule_function_tbl where tag = 'b' and buttonId <> ''";
		List<Map<String,Object>> list = this.getDAO().queryForList(sql);
		for (Map<String, Object> map : list) {
			if ("f".equals(map.get("tag"))) {
				Integer t = 0;
				for (int i = 0; i < list.size(); i++) {
					if(map.get("functionid").equals(list.get(i).get("pid"))){
						t++;
					}
				}
				if (t>0) {
					btns.put(map.get("linkaddress").toString(),t);
				}
			}
		}
		return btns;
	}

	
	private static Map<String, Map<String, Integer>> stationsBtn = new java.util.concurrent.ConcurrentHashMap<String, Map<String, Integer>>();

	public static Map<String, Integer> getPageButton(String custId) {
		stationsBtn.clear();
		Map<String, Integer> map = stationsBtn.get(custId);
		if (map == null) {
			FunctionService functionService = (FunctionService) SpringCtx.getBean("functionServiceImpl");
			map = functionService.getPageButton();
			stationsBtn.put(custId, map);
		}
		return map;
	}

}
